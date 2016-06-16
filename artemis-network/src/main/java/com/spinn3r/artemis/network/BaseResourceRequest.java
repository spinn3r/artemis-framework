package com.spinn3r.artemis.network;

import com.google.common.base.Charsets;
import com.spinn3r.log5j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: BaseResourceRequest.java 159214 2005-03-27 23:33:51Z burton $
 */
public abstract class BaseResourceRequest implements ResourceRequest {

    private static Logger log = Logger.getLogger();

    private static final int BUFFER_LENGTH = 15000;

    public static boolean ENABLE_CHARSET_FROM_META_CONTENT_TYPE = true;

    public static boolean ENABLE_CHARSET_FROM_XML_TAG = false;

    public static final byte[] DEFAULT_BYTES = new byte[0];

    public static boolean FOLLOW_REDIRECTS = false;

    public static boolean FOLLOW_REDIRECTS_MANUALLY = true;

    public static boolean USE_EXCLUSION = true;

    public static boolean USE_COMPRESSION = true;

    private String resource = null;

    private long _ifModifiedSince = -1;

    private int _responseCode = HttpURLConnection.HTTP_OK;

    private String _etag = null;

    protected byte[] data = null;

    private boolean followRedirects = FOLLOW_REDIRECTS;

    protected Map<String,String> requestHeaders = new HashMap<>();

    private String userAgent = URLResourceRequest.USER_AGENT;

    protected String authUsername = null;
    protected String authPassword = null;

    protected String outputContent = null;
    protected byte[] outputContentBytes = DEFAULT_BYTES;

    protected int outputContentLength = -1;
    protected String outputContentEncoding = null;
    protected String outputContentType = null;

    protected int maxContentLength = URLResourceRequest.MAX_CONTENT_LENGTH;

    /**
     * The detected character set we found (when available).
     *
     */
    protected String detectedCharset = null;

    /**
     * True if we should use our exclusion helper.
     *
     */
    protected boolean useExclusion = USE_EXCLUSION;

    /**
     * True when we should enable compression (gzip)
     *
     */
    protected boolean useCompression = USE_COMPRESSION;

    protected long readTimeout = ResourceRequestFactory.DEFAULT_READ_TIMEOUT;

    protected long connectTimeout = ResourceRequestFactory.DEFAULT_CONNECT_TIMEOUT;

    protected NetworkException cause;

    /**
     */
    private Map<String,String> cookies = new HashMap<>();

    private boolean followContentRedirects = true;
//    private Map<String, List<String>> responseHeaders;

    public void setReadTimeout( long v ) {
        this.readTimeout = v;
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }

    public void setConnectTimeout( long v ) {
        this.connectTimeout = v;
    }

    public long getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     *
     * Set the value of <code>maxContentLength</code>.
     *
     */
    public void setMaxContentLength( int maxContentLength ) {
        this.maxContentLength = maxContentLength;
    }

    /**
     *
     * Get the value of <code>maxContentLength</code>.
     *
     */
    public int getMaxContentLength() {
        return this.maxContentLength;
    }

    /**
     *
     * Get the value of <code>resource</code>.
     *
     *
     */
    public String getResource() {

        return this.resource;

    }

    /**
     *
     * Set the value of <code>resource</code>.
     *
     *
     */
    @Override
    public void setResource( String resource ) {
        this.resource = resource;
    }

    @Override
    public Map<String, String> getCookies() {
        return cookies;
    }

    @Override
    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    @Override
    public boolean getFollowContentRedirects() {
        return followContentRedirects;
    }

    @Override
    public void setFollowContentRedirects(boolean followContentRedirects) {
        this.followContentRedirects = followContentRedirects;
    }

    /**
     * @deprecated use getContentWithEncoding
     */
    public String getContent() throws NetworkException {

        try {
            return getContentWithEncoding();
        } catch ( NetworkException ne ) {
            throw ne;
        }

    }

    public String getContentWithEncoding() throws NetworkException {

        try {

            fetchAndCacheLocally();

            return getContentWithEncoding( this.data );

        } catch ( NetworkException ne ) {
            throw ne;
        } catch ( IOException e ) {
            throw new NetworkException( e ) ;
        }

    }

    protected String getContentWithEncoding( byte[] data ) throws NetworkException {

        try {

            // The HTTP protocol ([RFC2616], section 3.7.1) mentions ISO-8859-1
            // as a default character encoding when the "charset" parameter is
            // absent from the "Content-Type" header field. In practice, this
            // recommendation has proved useless because some servers don't
            // allow a "charset" parameter to be sent, and others may not be
            // configured to send the parameter. Therefore, user agents must not
            // assume any default value for the "charset" parameter.

            detectedCharset = Encodings.parseCharsetFromContentType( getResponseHeader( "Content-Type" ) );

            // To address server or configuration limitations, HTML documents
            // may include explicit information about the document's character
            // encoding; the META element can be used to provide user agents
            // with this information.

            if ( ENABLE_CHARSET_FROM_META_CONTENT_TYPE && detectedCharset == null ) {
                detectedCharset = Encodings.parseMetaContentType( data );
            }

            if ( ENABLE_CHARSET_FROM_XML_TAG && detectedCharset == null) {
                detectedCharset = parseEncodingFromXMLTag( parseXMLTag(data) );
            }

            //FIXME: this turns out to be a MAJOR performance bottleneck.  I can fix
            //this by creating references to ALL system charsets AHEAD of time
            //and then using lock free read-only data structures after that.

            if ( detectedCharset != null ) {

                //NOTE: What if it's a charset we don't support?  Only enable
                //this if he underlying VM supports this charset.  Is it better
                //to have broken content than no content?

                return new String( data, detectedCharset );

            }

            return new String( data, Charsets.ISO_8859_1 );

        } catch ( IOException e ) {
            throw new NetworkException( e ) ;
        }

    }

    public byte[] getError() throws NetworkException {
        throw new NetworkException( "not implemented" );
    }

    public String getErrorWithEncoding() throws NetworkException {
        throw new NetworkException( "not implemented" );
    }

    private String parseEncodingFromXMLTag( String xmlTag) {
        if ( xmlTag == null )
            return null;

        Pattern p = Pattern.compile( "encoding=[\"']?([^\\s\"';,]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher( xmlTag );

        if ( m.find() ) {

            String result = m.group( 1 );

            result = SafeCharsetLookup.lookup( result );

            return result;
        }

        return null;
    }

    private String parseXMLTag(byte data[]){

        String content;

        try {
            content = new String( data, "ISO-8859-1" );
            Pattern p1 = Pattern.compile("<\\?xml[^>]*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

            Matcher m1 = p1.matcher(content);

            if(m1.find()) {
                return m1.group(0);
            }

            return null;
        } catch (UnsupportedEncodingException e) {
            log.error( "Could not parse meta content type: ", e);
            return null;
        }

    }

    public byte[] getInputStreamAsByteArray() throws IOException {

        fetchAndCacheLocally();
        return data;

    }

    public InputStream getLocalInputStream() throws NetworkException {

        try {

            fetchAndCacheLocally();

            return new ByteArrayInputStream( data );

        } catch ( NetworkException n ) {
            throw n;
        } catch ( Exception t ) {
            throw new NetworkException( t );
        }

    }

    protected void fetchAndCacheLocally() throws IOException {
        fetchAndCacheLocally( null );
    }

    /**
     * When we have now 'data' member, initialize it.
     *
     */
    protected void fetchAndCacheLocally(InputStream is) throws IOException {

        //fail fast if we've already indexed this ...
        if ( data != null )
            return;

        if ( is == null ) {
            is = getInputStream();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream( BUFFER_LENGTH );

        //now process the Reader...
        byte buff[] = new byte[2048];

        int readCount = 0;

        long started = System.currentTimeMillis();

        while( ( readCount = is.read( buff ) ) > 0 ) {

            bos.write( buff, 0, readCount );

            //TODO: inject a clock as using currentTimeMillis is not a good idea...
            if ( System.currentTimeMillis() - started > ResourceRequestFactory.MAX_READ_TIMEOUT ) {

                // Throw an IOException if this takes too long to download.
                // This can DOS us by having web servers that trickle 1 byte
                // every 10 seconds as we will never hit a read timeout.
                // Further, they can be writing one byte every 29 seconds and
                // essentially stream content to us forever.

                //what NetworkException do I throw.
                throw new NetworkException( String.format( "Content took too long to download %s: %s",
                                                           ResourceRequestFactory.MAX_READ_TIMEOUT, getResource() ) );

            }

        }

        is.close();
        bos.close();

        this.data = bos.toByteArray();

    }

    /**
     * Copy this resource request to the given OutputStream
     *
     *
     */
    public void toOutputStream( OutputStream out ) throws IOException {

        InputStream is = getInputStream();

        //now process the Reader...
        byte data[] = new byte[200];

        int readCount = 0;

        while( ( readCount = is.read( data )) > 0 ) {

            out.write( data, 0, readCount );
        }

        is.close();

    }

    public long getIfModifiedSince() {
        return _ifModifiedSince;
    }

    public void setIfModifiedSince( long ifModifiedSince ) {
        this._ifModifiedSince = ifModifiedSince;
    }

    public String getEtag() {
        return _etag;
    }

    public void setEtag( String etag ) {
        this._etag = etag;
    }

    /**
     * Get and set an HTTP style response code.  Only used with HTTP URLs.
     *
     *
     */
    public int getResponseCode() {
        return this._responseCode;
    }

    public void setResponseCode( int responseCode ) {
        this._responseCode = responseCode;
    }
//
//    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
//        this.responseHeaders = responseHeaders;
//    }

    public int getContentLength() {
        return -1;
    }

    public String getResponseHeader(String name) {
        //default impl always returns null
        return  null;
    }

    public void setRequestHeader(String name, String value) {
        requestHeaders.put( name, value );
    }

    @Override
    public Set<String> getRequestHeaders() {
        return requestHeaders.keySet();
    }

    @Override
    public Map<String,String> getRequestHeadersMap() {
        return requestHeaders;
    }

    public String getRequestHeader(String name) {
        return requestHeaders.get( name );
    }

    public void setRequestMethod( String method ) throws NetworkException {
        throw new NetworkException( "not implemented" );
    }

    public boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects( boolean v ) {
        this.followRedirects = v;
    }

    public boolean getUseExclusion() {
        return useExclusion;
    }

    public void setUseExclusion( boolean v ) {
        this.useExclusion = v;
    }

    public boolean getUseCompression() {
        return useCompression;
    }

    public void setUseCompression( boolean v ) {
        this.useCompression = v;
    }

    public String getResourceFromRedirect() {
        return getResource();
    }

    /**
     * Set the user agent for this request.  We will use the default USER_AGENT
     * header otherwise.
     */
    public void setUserAgent( String userAgent ) {

        //don't allow null as this makes NO sense.  It's better to have the
        //default.
        if ( userAgent == null )
            return;

        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setAuthPassword( String v ) {
        authPassword = v;
    }
    public void setAuthUsername( String v ) {
        authUsername = v;
    }

    public void setOutputContent( String content,
                                  String encoding,
                                  String type ) {

        this.outputContent = content;
        this.outputContentLength = content.length();
        this.outputContentEncoding = encoding;
        this.outputContentType = type;

    }

    public void setOutputContent( byte[] content,
                                  String encoding,
                                  String type ) {

        this.outputContentBytes = content;
        this.outputContentLength = content.length;
        this.outputContentEncoding = encoding;
        this.outputContentType = type;

    }

    public boolean isRedirected() {

        String redirected_url = getResourceFromRedirect();

        if ( redirected_url != null && ! "".equals( redirected_url ) )
            return true;

        return false;

    }

    public boolean isModified() throws NetworkException {
        return ! isNotModified();
    }

    public String getDetectedCharset() {
        return detectedCharset;
    }

    @Override
    public NetworkException getCause() {
        return cause;
    }

    @Override
    public void setCause(NetworkException cause) {
        this.cause = cause;
    }

}
