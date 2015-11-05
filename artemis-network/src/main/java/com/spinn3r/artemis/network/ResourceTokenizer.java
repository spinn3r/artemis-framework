package com.spinn3r.artemis.network;

import com.spinn3r.log5j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Functionality for tokenizing URLS and managing them.  Warning... be careful
 * when modifying this class as you might break semantic link relationships in
 * the future.
 #
 * http://en.wikipedia.org/wiki/URL_normalization
 *
 * http://tools.ietf.org/html/rfc3986#section-6
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 * @version $Id: ResourceTokenizer.java,v 1.57 2005/03/29 01:24:15 burton Exp $
 */
public class ResourceTokenizer {

    public static Logger log = Logger.getLogger();

    /**
     * For certain long urls that end in .html BUT have query data we will
     * remove the query data.  We assume the query data is just for URL tagging
     * as in partner=userland and so forth.
     *
     * Example:
     *
     *  http://nytimes.com/2004/08/07/international/middleeast/07iraq.html?8bl
     *
     *  Would become:
     *
     *  http://nytimes.com/2004/08/07/international/middleeast/07iraq.html
     */
    public static boolean TOKENIZE_HTML_QUERY_ENABLE = false;

    /**
     * Min width we need in a URL to enable .html query tokenizing.
     */
    public static int TOKENIZE_HTML_QUERY_WIDTH = 35;

    /**
     * When we detect an inline redirect within a URL such as:
     *
     * http://transfer.go.com/cgi/transfer.pl?goto=http://abcnews.go.com/sections/Nightline/World/abu_ghraib_witnesses_040808-1.html&name=ALSO_ON&srvc=nws
     *
     * We will restore the original URL.
     *
     * http://abcnews.go.com/sections/Nightline/World/abu_ghraib_witnesses_040808-1.html
     *
     * We assume this is generated from a "broken" CMS that is creating too much
     * URL cruft or just a standard part of operation.  Maybe sites such as
     * nytimes, etc do this and if we didn't remove these they would break out
     * NPI.
     *
     * There are other reasons for removing redirect URLs.  For starters it
     * gives our competitors like Google News and Yahoo News an edge.
     *
     * Also some sites like Yahoo News check the redirect.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin A. Burton</a>
     */
    public static boolean TOKENIZE_REMOVE_REDIRECT_ENABLE = false;

    private static final char[] WEB_TOKENIZE_ENCODED_CHARS;

    private static final Pattern hasSchemePattern = Pattern.compile(
                                                                     "^[^:/]*://.*$");
    private static final Pattern case_fold_end_p = Pattern.compile(
                                                                    "[/#?]" );
    private static final Pattern sessionIDPattern = Pattern.compile(
                                                                     "(?i)((phpsessid|;jsessionid)=[^?&]+)([&])?" );
    private static final Pattern defaultPortPattern = Pattern.compile(
                                                                       "https?://[^:/]+(:[0-9]+)" );
    private static final Pattern pathInfoPattern = Pattern.compile(
                                                                    "https?://[^/]+" );
    private static final Pattern secondLevelDomainCheckPattern = Pattern.compile(
                                                                                  "\\.(ac|co|com|gov|ltd|me|mod|net|nic|nhs|org|plc|police|sch)\\.([a-z][a-z])$" );
    private static final Pattern level3DomainPattern = Pattern.compile(
                                                                        "(http[s]?://)([A-Za-z0-9_-]+\\.)*([A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+)" );
    private static final Pattern level2DomainPattern = Pattern.compile(
                                                                        "(http[s]?://)([A-Za-z0-9_-]+\\.)*([A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+)" );
    private static final Pattern level1DomainPattern = Pattern.compile(
                                                                        "(http[s]?://)([A-Za-z0-9_-]+\\.)*([A-Za-z0-9_-]+)" );
    private static final Pattern ipHostPattern = Pattern.compile(
                                                                  "(http[s]?://[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)" );

    static {
        WEB_TOKENIZE_ENCODED_CHARS = new char[]{'+', '?', '&', '#'};
    }

    /**
     * Tokenize the URL so that it can be represented in a manner easily
     * represented in a web URI string.  For example instead of having to double
     * encode EVERYTHING we can just encode a few characters that are necessary.
     *
     * Note that this method needs to have a corresponding Detokenize method
     * which builds back the original string.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static String webTokenize( String resource ) {

        if ( resource == null )
            return null;

        resource = safeTokenize( resource );

        //get rid of the scheme
        int begin = resource.indexOf( "://" );
        if ( begin != -1 ) {
            resource = resource.substring( begin+3, resource.length() );
        }

        return webTokenizeEncode( resource );
    }

    /**
     * Encode certain characters that can't be included within a URL string and
     * decoded.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    private static String webTokenizeEncode( String resource ) {

        StringBuilder buff = new StringBuilder( resource.length() * 2 );

        for ( int i = 0; i < resource.length(); ++i ) {

            String s = null;

            char c = resource.charAt(i);
            for (int j = 0; j < WEB_TOKENIZE_ENCODED_CHARS.length; ++j) {
                if (c == WEB_TOKENIZE_ENCODED_CHARS[i]) {
                    s = URLEncoder.encode( resource.substring( i, i + 1 ) );
                    break;
                }
            }

            if ( s == null) {
                buff.append( c );
            } else {
                buff.append( s );
            }

        }

        return buff.toString();
    }

    /**
     * See safeTokenize
     */
    public static String safeTokenize( String resource ) {

        return safeTokenize( resource, 0 );

    }

    /**
     * Tokenize a URL so that the same resource can be represented.  This is
     * used with XML resources and so forth.  This just removes URL cruft that's
     * not necessary.  When complete this allows URIs to be (essentially) used
     * as public keys within virtual link graphs.
     *
     * The level parameter can be used to specify the level of safety when
     * tokenizing the URL.
     *
     * 0-5  : perform somewhat risky actions like remove the www. portion of the URL.
     *
     * 6-10 : perform safe actions like removing the default port, case folding,
     * and trailing slash.
     *
     */
    public static String safeTokenize( String resource, int level ) {

        if ( empty( resource ) )
            return null;

        if ( level <= 5 ) {

            // add an http:// if no scheme specified
            // Brad Neuberg
            boolean hasScheme = hasSchemePattern.matcher(resource).matches();
            if (hasScheme == false)
                resource = "http://" + resource;

            //This is actually NOT safe and is somewhat risky if the host
            //doesn't have DNS setup correctly.
            resource = doRemoveWWWPart( resource );

        }

        if ( level <= 10 ) {
            //TODO: removing the trailing slash should probably become about a
            //6....
            resource = doRemoveTrailingSlash( resource );
            resource = doRemoveDefaultPort( resource );

            resource = doCaseFold( resource );
        }

        if ( level <= 50 ) {
            resource = doRemoveRedundantHTTP( resource );
        }

        return resource;

    }

    public static boolean empty( String v ) {

        return v == null || "".equals( v );

    }

    private static HashSet INVALID_SCHEMES = new HashSet();

    static {
        INVALID_SCHEMES.add( "feed" );
        INVALID_SCHEMES.add( "javascript" );
        INVALID_SCHEMES.add( "mailto" );
    }

    /**
     * Return true if we support indexing the given scheme.  Right now this is a
     * blacklist.
     */
    private static boolean isSupportedScheme( String resource ) {

        int begin = resource.indexOf( ":" );

        if ( begin != -1 ) {

            String scheme = resource.substring( 0, begin );

            return ! INVALID_SCHEMES.contains( scheme );

        }

        return true;

    }

    /**
     * Exactly like tokenize but provide hostname case folding:
     *
     * http://en.wikipedia.org/wiki/URL#Case-sensitivity
     *
     * According to the current standard, the scheme and host components are
     * case-insensitive, and when normalized during processing, should be
     * lowercase. Other components should be assumed to be
     * case-sensitive. However, in practice case-sensitivity of the components
     * other than the protocol and hostname are up to the webserver and
     * operating system of the system hosting the website.
     *
     */
    public static String tokenize2( String resource ) {

        if ( resource == null )
            return null;

        resource = tokenize( resource );
        resource = doCaseFold( resource );

        return resource;

    }

    /**
     * Like tokenize2 but includes session ID tokenization.
     */
    public static String tokenize3( String resource ) {

        if ( resource == null )
            return null;

        resource = tokenize2( resource );
        resource = doRemoveSessionIDs( resource );

        return resource;

    }

    public static String tokenize4( String resource ) {

        if ( resource == null )
            return null;

        resource = resource.trim();
        return tokenize3( resource );

    }

    /**
     * Remove sessions IDs used by HTTP implementations like JSP, ASP, and PHP.
     *
     * Examples:
     *
     * http://www.server.com/foo.html;jsessionid=xxxxxxxxx?query=data
     *
     * http://www.server.com/foo.html?phpsessid=0e773e74de4723a362a354s42c22ft55
     *
     * http://en.wikipedia.org/wiki/Session_(computer_science)
     *
     * "A session token is a unique identifier (usually in the form of a hash
     * generated by a hash function) that is generated and sent from a server to
     * a client to identify the current interaction session. The client usually
     * stores and sends the token as an HTTP cookie and/or sends it as a
     * parameter in GET or POST queries. The reason to use session tokens is
     * that the client only has to handle the identifier (a small piece of data
     * which is otherwise meaningless and thus presents no security risk) - all
     * session data is stored on the server (usually in a database, to which the
     * client does not have direct access) linked to that identifier. Examples
     * of the names that some programming languages use when naming their cookie
     * include JSESSIONID (JSP), PHPSESSID (PHP), and ASPSESSIONID (Microsoft
     * ASP)."
     */
    public static String doRemoveSessionIDs( String resource ) {

        if ( resource == null )
            return null;

        // |;sesid
        Matcher m = sessionIDPattern.matcher( resource );

        if ( m.find() ) {

            int end = m.end( 1 );

            //peak ahead a bit and see if the next char is the beginning of
            //another param.
            if ( "&".equals( m.group( 3 ) ) )
                end = end + 1;

            resource = resource.substring( 0, m.start( 0 ) ) +
                         resource.substring( end, resource.length() )
            ;

        }

        //technically jsession ID could just be one of many parameters, BUT if
        //it's one of the only parameter then strip the ? characeter.

        if ( resource.endsWith( "?" ) )
            resource = resource.substring( 0, resource.length() - 1 );

        return resource;

    }

    /**
     * Tokenize the URL so that the smallest unit of document location is
     * available.  This method will:
     *
     * <li> remmove www. prefix portion of URL
     * <ii> remove fragment portion (#foobar)
     * <li> remove index portion (/index.html)
     * <li> add a default http:// scheme if no scheme is specified
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static String tokenize( String resource ) {

        if ( empty( resource ) )
            return null;

        if ( ! isSupportedScheme( resource ) )
            return resource;

        // add an http scheme to the beginning if no scheme exists
        // FIXME: Will this break strings that have the characters
        // :/ as query parameters?
        // Brad Neuberg, rojo.com
        if ( resource.indexOf(":/") == -1 )
            resource = "http://" + resource;

        resource = doRemoveWWWPart( resource );

        //make sure domain aliases trail with a slash.
        // FIXME: What about other TLDs than these?
        // Brad Neuberg
        if ( resource.endsWith( ".co.uk" ) ||
               resource.endsWith( ".gov" ) ||
               resource.endsWith( ".org" ) ||
               resource.endsWith( ".com" ) ||
               resource.endsWith( ".net" ) ) {

            resource += "/";

        }

        int fragmentBegin = resource.lastIndexOf( "#" );

        if ( fragmentBegin != -1 )
            resource = resource.substring( 0, fragmentBegin );

        //this is necessary because if we are given a URL **WITHOUT** the
        //trailing slash then we would have to connect to the server via HTTP
        //and do a HEAD request to get the full URL.  An added advantage is that
        //we save an additional 8 bits (16 on unicode) in our database without
        //the extra slash

        //NOTE: should we only do this with HTTP URLs?
        if ( resource.endsWith( "/" ) )
            resource = resource.substring( 0, resource.length() - 1 );

        //if this URL is a default URL (default.html, index.html, etc) strip the
        //ending of.
        resource = doRemoveIndexPart( resource );

        resource = doRemoveRedirect( resource );
        resource = doRemoveHTMLQueryPart( resource );
        resource = doRemoveDefaultPort( resource );

        //TODO: this should probably be done first.

        //always trim to remove \n or \t or  spaces.
        return resource.trim();

    }

    /**
     * Remove named anchors from URLs... no #foo
     */
    public static String tokenizeRemoveNamedAnchors( String resource ) {

        if ( resource == null )
            return null;

        int index = resource.indexOf( "#" );
        if ( index != -1 ) {
            resource = resource.substring( 0, index );
        }

        return resource;

    }

    /**
     * Remove all query data from a URL.  This included ?foo=bar
     */
    public static String tokenizeRemoveQueryData( String resource ) {

        if ( resource == null )
            return null;

        int index = resource.indexOf( "?" );
        if ( index != -1 ) {
            resource = resource.substring( 0, index );
        }

        return resource;

    }

    /**
     * Case fold in the domain name.  Do NOT allow multiple forms of this.
     */
    private static String doCaseFold( String resource ) {

        if ( resource == null )
            return resource;

        if ( ! resource.startsWith( "http" ) )
            return resource;

        int begin = resource.indexOf( "://" ) + 3;

        if ( begin == -1 )
            return resource;

        Matcher m = case_fold_end_p.matcher( resource );

        int end = resource.length();
        if ( m.find( begin ) ) {
            end = m.start( 0 );
        }

        String schemepart = resource.substring( 0, begin );
        String hostpart = resource.substring( begin, end ).toLowerCase();
        String pathinfo = resource.substring( end, resource.length() );

        return schemepart + hostpart + pathinfo;

    }

    /**
     * Believe it or not this is common in some templates we've seen in the wild.
     *
     */
    private static String doRemoveRedundantHTTP( String resource ) {
        return resource.replaceAll( "^http://http://", "http://" );
    }

    /**
     *
     *
     * @author <a href="mailto:burton@rojo.com">Kevin A. Burton</a>
     */
    private static String doRemoveRedirect( String resource ) {

        //FIXME: what happens with MULTIPLE redirect URLs are encoded?

        int index = 0;

        if ( TOKENIZE_REMOVE_REDIRECT_ENABLE &&
               (index = resource.lastIndexOf( "http://" ))!= -1 ) {

            int end = resource.indexOf( "&", index );

            if ( end == -1 )
                end = resource.length();

            resource = resource.substring( index, end );

            resource = URLDecoder.decode( resource );

        }

        return resource;

    }

    private static String doRemoveHTMLQueryPart( String resource ) {

        if ( TOKENIZE_HTML_QUERY_ENABLE &&
               resource.length() > TOKENIZE_HTML_QUERY_WIDTH &&
               resource.lastIndexOf( ".html?" ) != -1 ) {

            resource = resource.substring( 0, resource.lastIndexOf( "?" ) );

        }

        return resource;

    }

    protected static String doRemoveWWWPart( String resource ) {

        resource = resource.replaceFirst( "http://www\\.", "http://" );
        resource = resource.replaceFirst( "https://www\\.", "https://" );

        return resource;

    }

    public static String doRemoveIndexPart( String resource ) {

        int slashend = resource.lastIndexOf( "/" );
        int dotend = resource.lastIndexOf( "." );

        if ( slashend != -1 && dotend != -1 && dotend > slashend ) {

            String part = resource.substring( slashend + 1, dotend );

            if ( "default".equals( part ) || "index".equals( part ) ) {

                String result = resource.substring( 0, slashend );

                //if there's query data in this string we'll loose it... which
                //is a big freaking problem.

                //FIXME: actually this doesn't totally do the right thing.  For
                //example if there's path info involved.

                int qidx = resource.indexOf( "?" );

                if ( qidx != -1 )
                    result = result + resource.substring( qidx, resource.length() );

                return result;

            }

        } else if ( resource.endsWith( "/" ) ) {
            resource = resource.substring( 0, resource.length() - 1 );
        }

        return resource;

    }

    private static String doRemoveDefaultPort( String resource ) {

        Matcher m = defaultPortPattern.matcher( resource );

        if ( m.find() ) {

            String port = m.group( 1 );

            if ( ":80".equals( port ) ) {

                resource =
                  resource.substring( 0, m.start( 1 ) ) +
                    resource.substring( m.end( 1 ), resource.length() );

            }

        }

        return resource;
    }

    /**
     * Given an input string of:
     *
     * http://peerfear.org/foo/
     *
     * we return
     *
     * http://peerfear.org/foo
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    private static String doRemoveTrailingSlash( String resource ) {

        //don't do this when the URL has queryData.  You might end up doing
        //something stupid like removing the last char from this:
        //
        // http://rojo.com/export-atom/?q=Kerry&search-base=/

        if ( resource.indexOf( "?" ) != -1 )
            return resource;

        if ( resource.endsWith( "/" ) )
            resource = resource.substring( 0, resource.length() - 1 );

        return resource;

    }

    private static String doRemoveQueryData( String resource ) {

        if ( resource == null )
            return null; //should we throw an NPE

        int end = resource.lastIndexOf( "?" );

        //URLs like "http://server.com" won't have ends.
        if ( end == -1 ) {
            end = resource.length();
        }

        return resource.substring( 0, end );

    }

    public static String domainTokenize( String resource ) {
        return domainTokenize( resource, false );
    }

    /**
     * Return the domain URL of this resource.
     *
     * Example: http://server.com/foo/bar would return http://server.com
     */
    public static String domainTokenize( String resource, boolean stripHTTP ) {

        if ( resource == null )
            return null;

        int begin = "http://".length();

        if ( resource.startsWith( "https://" ) )
            begin = "https://".length();

        int end = resource.indexOf( "/", begin );

        //URLs like "http://server.com" won't have ends.
        if ( end == -1 ) {
            end = resource.length();
        }

        //this results in a lot of GC.
        resource = resource.substring( 0, end );
        resource = tokenize4( resource );
        resource = doRemoveQueryData( resource );

        if ( stripHTTP && resource != null ) {
            resource = resource.replaceAll( "http://" , "" );
            resource = resource.replaceAll( "https://" , "" );
        }

        resource = doRemoveQueryData( resource );

        return resource;

    }

    public static String rootDomainTokenize( String resource ) {
        return rootDomainTokenize( resource, false );
    }

    /**
     * Tokenize to the root domain so instead of foo.bar.com we reduce to
     * bar.com
     */
    public static String rootDomainTokenize( String resource, boolean stripHTTP ) {

        if ( resource == null )
            return null;

        // First, see if it's a second-level domain:
        //
        // http://en.wikipedia.org/wiki/.uk
        //
        // * .ac.uk - academic (tertiary education and research establishments) and learned societies.
        // * .co.uk - commercial/general
        // * .gov.uk - government (central and local)
        // * .ltd.uk - limited companies
        // * .me.uk - personal
        // * .mod.uk - Ministry of Defence and HM Forces public sites
        // * .net.uk - ISPs and network companies
        // * .nic.uk - network use only
        // * .nhs.uk - National Health Service institutions
        // * .org.uk - non-profit organisations
        // * .plc.uk - public limited companies
        // * .police.uk - police forces
        // * .sch.uk - schools, primary and secondary education

        Pattern p = null;
        Matcher m = null;

        //first strip the path info
        p = pathInfoPattern;
        m = p.matcher( resource );

        if ( m.find() ) resource = m.group( 0 );

        //now see if we're on a second level domain like co.uk.
        p = secondLevelDomainCheckPattern;
        m = p.matcher( resource );

        boolean isSecondLevel = m.find();

        if ( isSecondLevel ) {
            p = level3DomainPattern;
        } else {
            p = level2DomainPattern;
        }

        Pattern ip_pattern = ipHostPattern;
        Matcher ip_matcher = ip_pattern.matcher( resource );

        String result = null;

        if ( ip_matcher.find() ) {

            result = ip_matcher.group( 1 );

        } else {

            m = p.matcher( resource );

            if ( m.find() ) {
                result = m.group( 1 ) + m.group( 3 );
            }

        }

        if ( stripHTTP && result != null ) {
            result = result.replaceAll( "http://" , "" );
            result = result.replaceAll( "https://" , "" );
        }

        return result;

    }

    /**
     * Tokenze a domain by level.  For example.  A level of 1 will be a top
     * level domain:
     *
     * http://en.wikipedia.org/wiki/Top-level_domain
     *
     * A level of two will be a second level domain:
     *
     * http://en.wikipedia.org/wiki/Second-level_domain
     *
     * Three would be a third level domains.
     *
     * This was originally written because we needed support for parsing out
     * third level domains for spaces.live.com and spaces.msn.com
     *
     * I would have liked to have started with a level of ZERO but this is
     * undefined. The concept of a Second-level domain was already present
     * before this code was written.
     *
     * This is further complicated by the fact that our link depth parsing
     * starts off with a level of zero.
     *
     * If a domain at this level is not found, null is returned.
     *
     * This method isn't smart like rootDomainTokenize and does not assume to
     * introspect anything out of the domain structure.  It just returns the
     * parts of the domain that you need.
     *
     * Examples:
     *
     * http://foobar.spaces.live.com
     *
     * Level 1:  com
     * Level 2:  live.com
     * Level 3:  spaces.live.com
     *
     */
    public static String domainTokenizeByLevel( String resource, int level ) {

        if ( resource == null )
            return null;

        Pattern p = null;

        if ( level == 3 ) {
            p = level3DomainPattern;
        } else if ( level == 2 ) {
            p = level2DomainPattern;
        } else if ( level == 1 ) {
            p = level1DomainPattern;
        } else {
            throw new RuntimeException( "Level not supported: " + level );
        }

        Matcher m = p.matcher( resource );

        if ( m.find() ) {
            return m.group( 1 ) + m.group( 3 );
        }

        return null;

    }

    /**
     * Get the domain 'name' from the link (cnn.com) without the top-level domain (TLD).
     *
     * Example: http://www.cnn.com/2004/US/Midwest/04/01/missing.student/index.html
     *
     * Would return "cnn"
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static String domainNameTokenize( String resource ) {

        String domainName = domainTokenize( resource );

        domainName = domainName.substring( 0, domainName.lastIndexOf( "." ) );

        int begin1 = domainName.lastIndexOf( "." );
        int begin2 = domainName.lastIndexOf( "/" );

        int begin = begin1;

        if ( begin2 > begin1 )
            begin = begin2;

        ++begin;

        return domainName.substring( begin, domainName.length() );

    }

    /**
     * Some resource URLs can't be safely tokenized and then fetched. This is
     * usually because they do not have a A name for server.com.  Expand these
     * so that callers don't break.
     *
     * The same thing applies to URLs that are given as www.  Some URLs are
     * syndicated without the correct www applied and we need to add this ifif
     * it doesn't resolve.
     *
     *
     * Examples:
     *
     *     FAILS                             SUCCEEDS
     *
     *     http://johnmunsch.com             http://www.johnmunsch.com
     *
     *     http://www.kiki.minidns.net       http://kiki.minidns.net
     *
     * Note this will block on gethostbyname()
     *
     * This is all necessary because internally our RDF link graph needs
     * canonicalized URLs and if we strip www we could break some.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static String expandResource( String resource ) throws UnknownHostException {

        //nothing we can do here.
        if ( resource == null )
            return null;

        if ( resource.startsWith( "file:" ) )
            return resource; //already expanded

        int begin = "http://".length();

        if ( resource.startsWith("http:") == false )
            begin = 0;

        //this would be bad but he caller was really stupid... let them handle it.
        if ( begin >= resource.length() )
            return resource;

        int end = resource.indexOf( "/", begin + 1 );

        if ( end == -1 )
            end = resource.length();

        String host = resource.substring( begin, end );

        try {

            //all we care is if an Exception is thrown.
            InetAddress addr = InetAddress.getByName( host );

            //We need to do a sanity check against www.server.com and server.com
            //names.  The theory here is if the user is given a URL of
            //server.com then the server is probably configured correctly and
            //www.server.com will resolve (because that would be a REALLY
            //stupid.  So then we should probably expand it.  But if we have
            //stripped it in the past then the www name configured correctly

            //no exception was thrown.
            return resource.trim();

        } catch ( UnknownHostException e ) {

            String newResource = null;

            if ( resource.startsWith( "http://www" ) ) {

                //the end of http://www.
                begin = 11;

                newResource = "http://" + resource.substring( begin, resource.length() );

            } else {

                newResource = "http://www." + resource.substring( begin, resource.length() );

            }

            //NOTE: should we do another getByName here and throw an
            //UnknownHostException if it fails?  It will end up failing anyway.

            return newResource.trim();

        } catch ( Exception t ) {

            //Java has a java.lang.ArrayIndexOutOfBoundsException: 1 on ipv6
            //addresses in JDK 1.4.2 that can cause this.  Don't pass it up
            //through the stack

            t.printStackTrace();
            return resource;

        }

    }

    public static String getHostName( String resource ) throws IOException {
        return new URL( resource ).getHost();
    }

    /**
     * Get a 4 byte IPv4 address (and one day an IPv6 address) from the given
     * string input.  Normally '1.1.1.1' format.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static byte[] getAddress( String input ) {

        Pattern p = PatternCache.getPattern( "\\." );

        String s[] = p.split( input );

        byte b[] = new byte[4];

        b[0] = Byte.parseByte( s[0] );
        b[1] = Byte.parseByte( s[1] );
        b[2] = Byte.parseByte( s[2] );
        b[3] = Byte.parseByte( s[3] );

        return b;

    }

    /**
     * Return true if this URL is network addressable outside of the current
     * host.  This needs to be compliant with RFC 1918:
     *
     * http://www.faqs.org/rfcs/rfc1918.html
     *
     * > 10.0.0.0        -   10.255.255.255  (10/8 prefix)
     * > 172.16.0.0      -   172.31.255.255  (172.16/12 prefix)
     * > 192.168.0.0     -   192.168.255.255 (192.168/16 prefix)
     *
     * > We will refer to the first block as "24-bit block", the second as
     * > "20-bit block", and to the third as "16-bit" block. Note that (in
     * > pre-CIDR notation) the first block is nothing but a single class A
     * > network number, while the second block is a set of 16 contiguous
     * > class B network numbers, and third block is a set of 256 contiguous
     * > class C network numbers.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static boolean isNetworkAddressable( String resource ) throws IOException {

        //NOTE: this should probably migrate into jakarta feedparser as we need
        //to assert that URLs are public there as well.  Either that or the
        //commons networking code.

        //first get the hostname
        String hostname = getHostName( resource );

        //it might be possible to use InetAddress but I need to look at the
        //source to make sure it doesn't do something stupid

        if ( hostname.matches( "[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+" ) ) {

            InetAddress addr = InetAddress.getByAddress( getAddress( hostname ) );

            return ! (addr.isSiteLocalAddress() ||
                        addr.isLoopbackAddress() ||
                        addr.isLinkLocalAddress() ||
                        addr.isMulticastAddress() );

        }

        return true;

    }

    /**
     * Return true if the given URL scheme for the resource is secure.  For
     * example http: is secure and so might some P2P URI schemes (maybe magnet
     * URIs) or .torrent links but file: links aren't secure.
     *
     * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
     */
    public static boolean isSecureScheme( String resource ) {

        if ( resource == null )
            return false;

        //NOTE: this was a bug with the URI expanding code.  We're smart enough
        //to avoid it now.
        if ( resource.startsWith( "http://javascript:" ) )
            return false;

        if ( resource.indexOf( "http://,." ) != -1 ) {
            return false;
        }

        if ( resource.indexOf( ":///" ) != -1 ) {

            /*

            http:///asdf/asdf

            or

            https:///asdf/asdf

            */

            return false; //this doesn't make sense
        }

        if ( resource.startsWith( "https:" ) )
            return true;

        if ( resource.startsWith( "http:" ) )
            return true;

        return false;

    }

    /** Checks for what is valid, not invalid, in the resource that is given to see
     * if we have safe characters; testing for validity versus invalidity is
     * better from a security standpoint.
     * Valid is defined as having any of the following:
     * letters, numbers, spaces, \/#!@$%^&*(),."':;{}[]<>~`_+=-?|
     * There must also be at least one alphanumeric character (letter or number).
     *
     * TODO: Handle internationalized/Unicode characters in a secure
     * way; we currently reject internationalized characters like the euro
     * symbol.
     */
    public static boolean hasSafeCharacters(String resource) {
        if (resource == null) {
            return false;
        }

        resource = resource.trim();

        String correctStartingCharacter = "[A-Za-z0-9]+";
        // note: \p{Punct} only works with US-ASCII
        // fixme: re-write to use non-regexp code
        String correctTrailingInput = "(?:[\\p{Punct} A-Za-z0-9])*";
        Pattern correctResourcePattern =
          PatternCache.getPattern("^" + correctStartingCharacter +
                                    correctTrailingInput + "$");

        Matcher testMe = correctResourcePattern.matcher(resource);

        return testMe.find();
    }

    /**
     *  Returns true when the resource given has only a scheme and no other
     *  significant text. For example, something like http:// and no more text.
     */
    public static boolean onlyHasScheme(String resource) {
        // ^(word followed by a colon and then an optional // zero or one time)$
        // fixme: re-write to use non-regexp code
        Pattern incorrectPattern = PatternCache.getPattern("^\\w+\\:/?/?$");
        return incorrectPattern.matcher(resource).find();
    }

    /** Ensures that resources aren't too long when being displayed. */
    public static String truncate( URL resource, int displayResourceLength ) {
        return truncate(resource.toString(), displayResourceLength);
    }

    public static String truncate( String resource, int displayResourceLength ) {

        // truncate down the resource if it is too long
        if (resource.length() > displayResourceLength) {
            resource = resource.substring( 0, displayResourceLength ) + "...";
        }

        return resource;
    }


}

