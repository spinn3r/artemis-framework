package com.spinn3r.artemis.network;


import com.google.common.base.Charsets;
import com.spinn3r.log5j.Logger;
import java.net.HttpURLConnection;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * ResourceRequest implementation that uses java.net.URL as the backend.
 *
 * Differences from other ResourceRequests.
 *
 * setRequestMethod() - Allows us to change the request type (HEAD, etc).
 *
 * getContentLength() - Returns the length/size of the content represented by
 * this resource.  Can be used by clients with setRequestMethod( "HEAD" ) to
 * find the size of a remote resource without doing a full fetch.
 *
 */
// http://www.rgagnon.com/javadetails/java-debug-HttpURLConnection-problem.html
public class URLResourceRequest extends BaseResourceRequest implements ResourceRequest {

    private static final String COOKIE_HEADER_NAME = "Cookie";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String HTTP_MAX_REDIRECTS = "http.maxRedirects";

    private static Logger log = Logger.getLogger();

    private static final int ERROR_BUFFER_SIZE = 15000;
    /**
     * When true, we block HTTPS URLs.
     */
    public static boolean BLOCK_HTTPS = false;

    /**
     * If the remote HTTP server is lying about using GZIP encoding we detect
     * this and do not attempt to decompress the content.
     *
     */
    public static boolean ENABLE_SAFE_GZIP = false;

    /**
     * Maximum timeout from HTTP meta refresh redirects.
     *
     */
    public static int REDIRECT_FROM_META_REFRESH_EQUIV_MAX_TIMEOUT = 10;

    /**
     * Global feature toggle to enable content redirects to handle frame sets, meta http redirects, etc.
     *
     */
    public static boolean ENABLE_FOLLOW_CONTENT_REDIRECTS = true;

    /**
     * Instead of using -1 as a status code for connect timeouts we use negative
     * values.  This represents a read timeout.
     */
    public static final int STATUS_READ_TIMEOUT    = -1024;

    /**
     * Instead of using -1 as a status code for connect timeouts we use negative
     * values.  This represents a connect timeout.
     */
    public static final int STATUS_CONNECT_TIMEOUT = -1025;

    public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
    public static final String IF_NONE_MATCH_HEADER = "If-None-Match";
    public static final String GZIP_ENCODING = "gzip";
    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     *
     * Enable RFC 3228 HTTP Delta for feeds.
     *
     * http://bobwyman.pubsub.com/main/2004/09/using_rfc3229_w.html
     *
     *  http://bobwyman.pubsub.com/main/2004/09/implementations.html
     *
     */
    public static boolean ENABLE_HTTP_DELTA_FEED_IM = false;

    /**
     * The default user agent.
     */
    public static String USER_AGENT
        = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36";

    /**
     * If a domain is included in this map, we disable GZIP encoding.
     */
    public static Set<String> GZIP_DISABLE_DOMAINS = new HashSet<>();

    /**
     * Disable robots.txt if the given domain is included in this list.
     */
    public static Set<String> DISABLED_ROBOTS_TXT_DOMAINS = new HashSet<>();

    /**
     * Maximum number of bytes that a URL will attempt to fetch
     *
     */
    public static int MAX_CONTENT_LENGTH = 2000000;

    /**
     * Maximum number of redirects we should follow.  Technically, since the
     * java.net.URL class has its own 'follow redirects' semantics we could
     * follow up to 25 redirects (5x5) but even this is still acceptable.
     *
     */
    public static int MAX_FOLLOWED_REDIRECTS = 10;

    private URL _url = null;

    private URLConnection _urlConnection = null;

    private Proxy proxy = null;

    private boolean initConnection = false;

    private String requestMethod = null;

    /**
     * Number of followed redirects.
     *
     */
    private int redirects = 0;

    /**
     * The root domain of this URL without http://
     *
     */
    private String rootDomainTokenized = null;

    private String domainTokenized = null;

    private int duration = -1;

    private InetAddress inetAddress;

    private ExclusionHelper exclusionHelper = new ExclusionHelper();

    public URLResourceRequest() {

        //this will be a new HTTP request so we should reset the password from
        //the last time.
        setAuthUsername( null );
        setAuthPassword( null );

    }

    /**
     *
     *
     */
    public void init() throws NetworkException {

        try {

            this.data = null;

            String resource = this.getResource();

            if ( BLOCK_HTTPS ) {

                if ( resource.startsWith( "https:" ) ) {

                    String message = String.format( "HTTPS URLs not supported: %s", resource );

                    throw new NetworkException( message, this, _url, _urlConnection );

                }

            }

            _url = new URL( this.getResource() );

            if ( proxy != null ) {

                log.debug( "Using proxied connection: %s" , proxy );

                _urlConnection = _url.openConnection( proxy );

            } else {
                _urlConnection = _url.openConnection();
            }

        } catch ( IOException e ) {

            NetworkException ne = new NetworkException( e, this, _url, _urlConnection );

            handleNetworkException( ne , e );

            throw ne;

        }

    }

    /**
     * Return true if rootDomainTokenized ir domainTokenize is within the given list.
     */

    private boolean isDomainWithinSet( Set<String> set ) {

        if ( set.contains( domainTokenized ) ) {
            return true;
        }

        if ( set.contains( rootDomainTokenized ) ) {
            return true;
        }

        return false;

    }

    private String trimToDomain( String resource ) {

        if ( resource != null ) {
            resource = resource.replaceAll( "http://", "" );
            resource = resource.replaceAll( "https://", "" );
        }

        return resource;

    }

    /**
     *  Opens a communications link to the resource referenced by this URL, if
     *  such a connection has not already been established.
     *
     */
    public void connect() throws NetworkException {
        initConnection();
    }

    public void initConnection() throws NetworkException {

        initConnection1();

    }

    /**
     * Init the actual connection.  Should be called AFTER init() but before
     * getInputStream() so that we can set any runtime params requestMethod,
     * etc.  If getInputStream() is called without an initConnection() we do
     * this automatically.  initConnection() might not want to be called when
     * doing a HEAD request.
     *
     *
     */
    private void initConnection1() throws NetworkException {
        initConnection1( getResource() );
    }

    /**
     * Init the actual connection.  Should be called AFTER init() but before
     * getInputStream() so that we can set any runtime params requestMethod,
     * etc.  If getInputStream() is called without an initConnection() we do
     * this automatically.  initConnection() might not want to be called when
     * doing a HEAD request.
     *
     *
     */
    private void initConnection1( String resource ) throws NetworkException {

        if ( initConnection ) {

            // Don't perform duplicate init.  We may consider making this use a
            // double check idiom but there is no need as this code shouldn't be
            // multithreaded.

            return;
        }

        String logMethod = requestMethod;
        long before = System.currentTimeMillis();

        try {

            // determine if this URL is robots.txt blocked by dependency injection
            // of robot.txt providers at a higher ACCEPT_ENCODING_HEADER
            if ( getUseExclusion() &&
                 ! isDomainWithinSet( DISABLED_ROBOTS_TXT_DOMAINS ) &&
                 exclusionHelper.isExcluded( resource ) ) {

                throw new NetworkException( "URL is excluded: " + resource );

            }

            if ( logMethod == null ) {
                logMethod = "GET";
            }

            // NOTE: technically we should wait until this method has finished
            // before setting this to true.
            initConnection = true;

            String _userAgent = getUserAgent();

            //set the user agent if it hasn't ALREADY been set by the caller.
            if ( getRequestHeader( USER_AGENT_HEADER ) == null && _userAgent != null ) {
                _urlConnection.setRequestProperty( USER_AGENT_HEADER, _userAgent );
            }

            if ( authUsername != null && authPassword != null ) {

                /*
                String auth =
                  String.format( "Basic %s",
                                 Base64.encode( String.format( "%s:%s",
                                                               authUsername,
                                                               authPassword ).getBytes() ) );

                _urlConnection.setRequestProperty( AUTHORIZATION_HEADER, auth );
                */

                throw new NetworkException( "Authentication is not yet implemented." );

            }

            // TODO: also support deflate:
            // Accept-Encoding: gzip,deflate

            boolean _useCompression = getUseCompression();

            if ( rootDomainTokenized != null && GZIP_DISABLE_DOMAINS.contains( rootDomainTokenized ) ) {
                _useCompression = false;
            }

            // Only add this request property if the user has enabled gzip which is
            // true by default.
            if ( _useCompression ) {
                _urlConnection.setRequestProperty( ACCEPT_ENCODING_HEADER, GZIP_ENCODING );
            }

            //copy over any headers set in the request..


            for( String requestHeader : getRequestHeaders() ) {

                _urlConnection.setRequestProperty( requestHeader, getRequestHeader( requestHeader ) );

            }

            //perform cookie setting...

            if ( getCookies() != null && getCookies().size() > 0 ) {
                _urlConnection.setRequestProperty( COOKIE_HEADER_NAME, CookiesEncoder.encode( getCookies() ) );
            }

            if ( _urlConnection instanceof HttpURLConnection ) {

                HttpURLConnection httpURLConn = (HttpURLConnection)_urlConnection;

                httpURLConn.setFollowRedirects( getFollowRedirects() );
                httpURLConn.setInstanceFollowRedirects( getFollowRedirects() );

                if ( this.getIfModifiedSince() != -1 )
                    httpURLConn.setIfModifiedSince( this.getIfModifiedSince() );

                if ( getEtag() != null && ! "".equals( getEtag() ) ) {

                    httpURLConn.setRequestProperty( IF_NONE_MATCH_HEADER, getEtag() );

                }

                try {

                    if ( "GET".equals( requestMethod ) || "POST".equals( requestMethod ) || "PUT".equals( requestMethod ) ) {

                        httpURLConn.setDoInput(true);
                        httpURLConn.setDoOutput(true);
                        httpURLConn.setUseCaches(false);

                        if ( outputContentLength > -1 ) {

                            String len = Integer.toString( outputContentLength );

                            httpURLConn.setRequestProperty( CONTENT_LENGTH, len );

                            httpURLConn.setRequestProperty( CONTENT_TYPE,
                                                            String.format( "%s; charset=%s", outputContentType, outputContentEncoding ) );

                            try( OutputStream out = httpURLConn.getOutputStream() ) {

                                byte[] bytes = outputContentBytes;

                                if ( outputContent != null ) {

                                    // TODO: ideally we should use Charset and not the string
                                    // representation of the Charset because that's slower
                                    // and uses a lock.

                                    // log.info( "Using PUT content charset %s", outputContentEncoding );

                                    bytes = outputContent.getBytes( outputContentEncoding );
                                }

                                out.write( bytes );

                            }

                        }

                    }

                    //now copy over timeout and read settings
                    httpURLConn.setConnectTimeout( (int) getConnectTimeout() );
                    httpURLConn.setReadTimeout( (int) getReadTimeout() );

                    httpURLConn.connect();

                    // save and store the response code.
                    int _responseCode = httpURLConn.getResponseCode();

                    this.setResponseCode( _responseCode );

                } catch ( IOException e ) {

                    NetworkException ne = new NetworkException( e, this, _url, _urlConnection );

                    handleNetworkException( ne , e );

                    throw ne;

                } finally {

                    // *** update the request headers with what was actually used.
                    /*
                    for ( String requestHeader : httpURLConn.getRequestProperties().keySet() ) {

                        List<String> requestHeaderValues = httpURLConn.getRequestProperties().get( requestHeader );

                        for (String requestHeaderValue : requestHeaderValues) {
                            requestHeaders.put( requestHeader, requestHeaderValue );
                        }

                    }
                    */

                    try {

                        // note that this must be in the finally block because
                        // we also need the InetAddress for failed URLs.

                        this.inetAddress = InetAddressReader.read( httpURLConn );

                    } catch ( Exception e ) {
                        log.error( "Could not determine InetAddress for URL: " + resource, e );
                    }

                }

            }

            int contentLength = _urlConnection.getContentLength();

            //read the updated redirect URL.
            setResource( getResourceFromRedirect() );

            //bigger than 1 meg and it is a remote document (it is safe to process
            //local documents)
            if ( contentLength > getMaxContentLength() && ! getResource().startsWith( "file:" ) ) {

                //NOTE: make 100% sure this doens't just go ahead and download the
                //file FIRST before doing a HEAD.  I think that's what happens but I
                //might be wrong.

                throw new NetworkException( "Content is too large - " + contentLength + " - " + getResource() );

            }

        } finally {

            long after = System.currentTimeMillis();

            this.duration = (int)(after - before);

            log.info( "%s: %s, duration: %s, status: %s (%s) followRedirects=%s, http.maxRedirects=%s, cookies=%s, contentEncoding=%s proxy=%s",
                      logMethod, resource, duration, getResponseCode(), getResponseCodeFormatted(), getFollowRedirects(), System.getProperty( HTTP_MAX_REDIRECTS ), getCookies(), _urlConnection.getContentEncoding(), proxy );

        }

        try {

            // this will fetch all the data and store it in a byte[] backing in memory
            // which we can then use to process the content.
            fetchAndCacheLocally( getBackendInputStream() );

            if (getFollowRedirects()) {

                if (ENABLE_FOLLOW_CONTENT_REDIRECTS && getFollowContentRedirects()) {

                    if (followRedirect( parseRedirectFromFrameContent( data ) )) {
                        log.info( "Following redirect from frame content..." );
                        return;
                    }

                    if (followRedirect( parseRedirectFromMetaRefreshEquiv( data ) )) {
                        log.info( "Following HTTP meta redirect..." );
                        return;
                    }

                }

                if (followRedirect( parseSSLRedirect() )) {
                    log.info( "Following SSL redirect..." );
                    return;
                }

            }

        } catch ( NetworkException ne ) {
            throw ne;
        } catch ( IOException e ) {
            throw newNetworkException( e );
        }

    }

    /**
     * Get the HTTP response code as a string to factor in timeouts and other
     * errors.
     *
     * @return
     */
    public String getResponseCodeFormatted() {

        switch( getResponseCode() ) {

            case STATUS_READ_TIMEOUT:
                return "read timeout";

            case STATUS_CONNECT_TIMEOUT:
                return "connect timeout";

            default:
                return Long.toString( getResponseCode() );

        }

    }

    //TODO: move this into a NetworkLogger which accepts different types...
    protected void logRequestHeaders( URLConnection conn ) {

        try {

            Map<String,List<String>> requestProperties = _urlConnection.getRequestProperties();

            for ( String key : requestProperties.keySet() ) {

                List<String> values = requestProperties.get( key ) ;

                for ( String val : values ) {
                    log.debug( "Header: %s: %s" , key, val );
                }

            }

        } catch ( Exception e ) {
            log.error( "Unable to log request headers: " , e );
        }

    }

    public byte[] getError() throws NetworkException {

        if (!( _urlConnection instanceof HttpURLConnection )) {
            throw new NetworkException( "Unable to get error from non HTTP connection" );
        }

        try {

            HttpURLConnection httpconn = (HttpURLConnection) _urlConnection;

            InputStream is = httpconn.getErrorStream();

            //first decompress
            if (GZIP_ENCODING.equals( _urlConnection.getContentEncoding() )) {

                //note.  the advanced input stream must be wrapped by a GZIP
                //input stream and not vice-versa or we will end up with
                //incorrect results.

                is = new GZIPInputStream( is );

            }

            //include length of content from the original site with contentLength
            ByteArrayOutputStream bos = new ByteArrayOutputStream( ERROR_BUFFER_SIZE );

            //now process the Reader...
            byte buff[] = new byte[ 2048 ];

            int readCount = 0;

            //FIXME: throw an IOException if this takes too long to download.  This
            //can DoS us by having web servers that trickle 1 byte every 10 seconds
            //as we will never hit a read timeout.  We handle this case right now
            //for HTTP 200s but not errors.

            while (( readCount = is.read( buff ) ) > 0) {
                bos.write( buff, 0, readCount );
            }

            is.close();
            bos.close();

            byte[] data = bos.toByteArray();

            return data;
        } catch ( IOException e ) {
            throw new NetworkException( e ) ;
        }

    }

    public String getErrorWithEncoding() throws NetworkException {
        return getContentWithEncoding( getError() );
    }

    /**
     * Follow the given redirect.
     *
     */
    private boolean followRedirect( String resource ) throws NetworkException {

        if ( resource == null )
            return false;

        if ( resource.equals( getResource() ) )
            return false;

        if ( redirects > MAX_FOLLOWED_REDIRECTS ) {

            String message = String.format( "Unable to follow URL.  Too many redirects: %s", redirects );

            throw new NetworkException( message, this, _url, _urlConnection );

        }

        ++redirects;

        if ( getResource().equals( resource ) ) {

            String message = String.format( "Redirected to identical URL: %s", resource );

            throw new NetworkException( message, this, _url, _urlConnection );

        }

        log.debug( "Following redirect: " + resource );

        initConnection = false;

        setResource( resource );
        init();
        initConnection();

        return true;

    }

    /**
     * Get a wrapped input stream that supports maximum content reads, gzip,
     * etc.
     *
     */
    public InputStream getBackendInputStream() throws NetworkException {

        try {

            InputStream inputStream = _urlConnection.getInputStream();

            if (ENABLE_SAFE_GZIP)
                inputStream = new BufferedInputStream( inputStream );

            inputStream = new NetworkInputStream( inputStream, getMaxContentLength(), this );

            //first decompress
            if (GZIP_ENCODING.equals( _urlConnection.getContentEncoding() )) {

                boolean wrapInputStream = true;

                if (ENABLE_SAFE_GZIP) {

                    try {

                        wrapInputStream = GZipDetector.detect( inputStream );

                    } catch (IOException e) {
                        log.error( "Could not detect potential gzip input stream: ", e );
                    }

                }

                //note.  the advanced input stream must be wrapped by a GZIP
                //input stream and not vice-versa or we will end up with
                //incorrect results.

                if (wrapInputStream) {
                    inputStream = new GZIPInputStream( inputStream );
                }

            }

            return inputStream;

        } catch ( NetworkException ne ) {
            throw ne;
        } catch ( IOException e ) {
            throw newNetworkException( e );
        }

    }

    @Override
    public InputStream getInputStream() throws NetworkException {

        if ( ! initConnection ) { initConnection(); }

        if ( data == null ) {
            //FIXME: I don't think this should be a NullPointerException... it
            //should be a generic NetworkException shouldn't it,
            throw new NetworkException( "Null 'data' for request: " + getResource() );
        }

        return new ByteArrayInputStream( data );

    }

    @Override
    public void disconnect() throws NetworkException {

        if ( _urlConnection != null ) {

            if ( _urlConnection instanceof HttpURLConnection ) {
                ((HttpURLConnection)_urlConnection).disconnect();
            }

        }

    }

    private NetworkException newNetworkException( IOException e ) {

        String message = null;

        //the modern VM buries the FileNotFoundException which prevents a
        //catch.  Very very ugly.
        if ( e.getCause() instanceof FileNotFoundException ) {
            message = "File not found: " + e.getCause().getMessage();
        } else {
            message = e.getMessage();
        }

        return new NetworkException( message, e, this, _url, _urlConnection );

    }

    /**
     * Handle the given network exception, setting local response codes wen
     * necessary.
     *
     */
    private void handleNetworkException( NetworkException ne, IOException cause ) {

        // record socket timeout and connect exceptions, otherwise, use
        // the HTTP status code returned.
        if ( cause instanceof SocketTimeoutException) {
            setResponseCode( STATUS_READ_TIMEOUT );
        } else if ( cause instanceof ConnectException ) {
            setResponseCode( STATUS_CONNECT_TIMEOUT );
        } else {
            setResponseCode( ne.getResponseCode() );
        }

    }

    java.lang.reflect.Field FIELD_HTTP_URL_CONNECTION_HTTP = null;
    java.lang.reflect.Field FIELD_HTTP_CLIENT_URL = null;

    /**
     * This method used Reflection to pull out the redirected URL in
     * java.net.URL.  Internally sun.net.www.protocol.http.HttpURLConnection
     * stores a reference to sun.net.www.http.HttpClient which then in turn does
     * all the redirection and stores the redirect java.net.URL.  We just use
     * reflection to FETCH this URL and then call toString to get the correct
     * value.
     *
     * Java needs the concept of readonly private variables.
     *
     *
     */
    public String getResourceFromRedirect() {

        try {

            if ( FIELD_HTTP_URL_CONNECTION_HTTP == null ) {

                //Note: when using a FILE URL this won't work!
                FIELD_HTTP_URL_CONNECTION_HTTP = _urlConnection.getClass().getDeclaredField( "http" );
                FIELD_HTTP_URL_CONNECTION_HTTP.setAccessible( true );

            }

            Object http = FIELD_HTTP_URL_CONNECTION_HTTP.get( _urlConnection );

            //when java.net.URL has already cleaned itself up 'http' will be
            //null here which isn't helpful.  I think this is only called when
            //the connection is closed.
            if ( http == null ) {
                return getResource();
            }

            if ( FIELD_HTTP_CLIENT_URL == null ) {

                FIELD_HTTP_CLIENT_URL = http.getClass().getDeclaredField( "url" );
                FIELD_HTTP_CLIENT_URL.setAccessible( true );

            }

            Object url = FIELD_HTTP_CLIENT_URL.get( http );

            //this will be a java.net.URL and now I can call the toString method
            //on it which will return our full URI.
            return url.toString();

        } catch ( Exception t ) {
            //log.error( t );
            return getResource();
        }

    }

    /**
     * Set the RequestMethod of this URLConnection.
     *
     *
     */
    public void setRequestMethod( String method ) throws NetworkException {

        try {

            if ( _urlConnection instanceof HttpURLConnection ) {

                ((HttpURLConnection)_urlConnection).setRequestMethod( method );
                requestMethod = method;

            }

        } catch ( ProtocolException pe ) {

            NetworkException ne = new NetworkException( pe );
            throw ne;

        }

    }

    @Override
    public int getContentLength() {

        if ( this.data != null )
            return this.data.length;

        return -1;

    }

    @Override
    public String getResponseHeader(String name) {
        return  _urlConnection.getHeaderField( name );
    }

    @Override
    public Set<String> getResponseHeaders() {
        return _urlConnection.getHeaderFields().keySet();
    }

    @Override
    public Map<String,List<String>> getResponseHeadersMap() {
        return _urlConnection.getHeaderFields();
    }

    @Override
    public boolean isNotModified() throws NetworkException {

        if ( ! initConnection ) { initConnection(); }

        return getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED;

    }

    /**
     * Parse a <frame> from the content to see if we need to issue a redirect.
     *
     */
    private String parseRedirectFromFrameContent( byte[] data ) {

        try {

            //NOTE the performance of this could be improved by terminating the
            //parse when we get to <body>

            String content = new String( data, Charsets.ISO_8859_1 );

            // <frame src="http://example.com">

            Pattern p1 = Pattern.compile( "(?mis)<frame[^>]+>" );
            Matcher m1 = p1.matcher( content );

            Pattern p2 = Pattern.compile( "(?mis)src=[\"']([^\"']+)[\"']" );

            String result = null;

            while ( m1.find() ) {

                String element = m1.group( 0 );

                if ( element == null )
                    continue;

                Matcher m2 = p2.matcher( element );

                if ( ! m2.find() ) {
                    continue;
                }

                if ( result != null ) {
                    return null; //too many results
                }

                result = m2.group( 1 );

                //entity decode this content I think as it will be HTML encode
                //&amp; etc.
                result = cleanseRedirectURL( result );

            }

            return result;

        } catch ( Exception t ) {
            log.error( "Could not parse frame content: ", t );
        }

        return null;

    }

    /**
     * <meta http-equiv="refresh" content="0; url=http://hdn.live.mediaspanonline.com/">
     *
     * <meta http-equiv="refresh" content="1800;url=?refresh=1">
     *
     *
     */
    private String parseRedirectFromMetaRefreshEquiv( byte[] data ) {

        try {

            //NOTE the performance of this could be improved by terminating the
            //parse when we get to <body>

            String content = new String( data, Charsets.ISO_8859_1 );

            //we need to strip comments first.
            content = content.replaceAll( "(?mis)<!--.*?-->", "" );

            Pattern p1 = Pattern.compile( "(?mis)<meta[^>]+>" );
            Matcher m1 = p1.matcher( content );

            Pattern p2 = Pattern.compile( "(?mis)([0-9]+)\\s*;\\s*url=([^\"' ]+)" );

            String result = null;

            while ( m1.find() ) {

                String element = m1.group( 0 );

                if ( element == null )
                    continue;

                element=element.replaceAll( "\n" , " " );

                //verify that this is http-equiv
                if ( ! element.matches( "(?mi).*http-equiv=[\"']?refresh[\"']?.*" ) ) {
                    continue;
                }

                Matcher m2 = p2.matcher( element );

                if ( ! m2.find() ) {
                    continue;
                }

                if ( result != null ) {
                    return null; //too many results
                }

                int wait = Integer.parseInt( m2.group( 1 ) );

                //only if the wait is VERY low.
                if ( wait > REDIRECT_FROM_META_REFRESH_EQUIV_MAX_TIMEOUT ) {
                    continue;
                }

                result = m2.group( 2 );

                //entity decode this content I think as it will be HTML encode
                //&amp; etc.
                result = cleanseRedirectURL( result );

            }

            return result;

        } catch ( Exception t ) {
            log.error( "Could not parse frame content: ", t );
        }

        return null;

    }

    private String parseSSLRedirect() {

        if ( getResponseCode() == 301 || getResponseCode() == 302 ) {
            return getResponseHeader( "Location" );
        }

        return null;

    }

    private String cleanseRedirectURL( String value ) {

        if ( value != null ) {
            value = value.trim();
        }

        //don't accept empty strings.
        if ( "".equals( value ) )
            value = null;

        if ( value != null ) {
            value = EntityDecoder.decodeCDATA( value );
        }

        if ( ! value.startsWith( "http:" ) && ! value.startsWith( "https:" ) ) {

            value = ResourceExpander.expand( getResource(), value );
        }

        return value;

    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {

        if ( _urlConnection != null ) {
            throw new RuntimeException( "Unable to set proxy while already connected" );
        }

        this.proxy = proxy;
    }

    public ExclusionHelper getExclusionHelper() {
        return exclusionHelper;
    }

    public void setExclusionHelper(ExclusionHelper exclusionHelper) {
        this.exclusionHelper = exclusionHelper;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public InetAddress getInetAddress() {
        return inetAddress;
    }

}
