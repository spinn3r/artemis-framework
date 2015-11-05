package com.spinn3r.artemis.network;

/**
 *
 */

import com.spinn3r.log5j.Logger;

import java.util.regex.Pattern;

/**
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: ResourceExpander.java 159217 2005-03-27 23:47:14Z burton $
 */
public class ResourceExpander {

    private static Logger log = Logger.getLogger();

    /** A regexp to determine if a URL has a scheme, such as "http://foo.com".
     */
    static ThreadLocal schemePattern = new ThreadLocal() {

        protected synchronized Object initialValue() {
            return Pattern.compile( "^[a-zA-Z]*:.*" );
        }

    };

    public static String expand( String resource, String link ) {

        try {

            if ( link == null )
                return null;

            link = expand1( resource, link );
            link = doRemoveDotEntries( link );
            return link;

        } catch ( RuntimeException e ) {

            log.warn( "Unable to expand: " + resource +
                        " with " + link +
                        " - " + e.getMessage() );

            throw e;

        }

    }

    /**
     * Expand a link relevant to the current site.  This takes care of links
     * such as
     *
     * /foo.html -> http://site.com/base/foo.html
     *
     * foo.html -> http://site.com/base/foo.html
     *
     * Links should *always* be expanded before they are used.
     *
     * This is because if we use the URL http://site.com/base then we don't know
     * if it's a directory or a file.  http://site.com/base/ would be a directory.
     *
     * Note that all resource URLs will have correct trailing slashes.  If the URL
     * does not end with / then it is a file URL and not a directory.
     *
     * @param resource The absolute base URL that will be used to expand the
     * link, such as "http://www.codinginparadise.org".
     * @param link The link to possibly expand, such as "/index.rdf" or
     * "http://www.somehost.com/somepage.html".
     *
     *
     */
    public static String expand1( String resource, String link ) {

        // Unable to expand: http://carsonworkshops.com/summit with ../../index.html - String index out of range: -1
        // Exception in thread "main" java.lang.StringIndexOutOfBoundsException: String index out of range: -1
        //         at java.lang.String.substring(String.java:1768)
        //         at org.apache.commons.feedparser.locate.ResourceExpander.expand1(ResourceExpander.java:144)
        //         at org.apache.commons.feedparser.locate.ResourceExpander.expand(ResourceExpander.java:43)
        //         at org.apache.commons.feedparser.locate.LinkLocator$1.onAnchor(LinkLocator.java:75)
        //         at org.apache.commons.feedparser.locate.AnchorParser.parseAnchors(AnchorParser.java:133)
        //         at org.apache.commons.feedparser.locate.LinkLocator.locate(LinkLocator.java:220)
        //         at org.apache.commons.feedparser.locate.FeedLocator.locate(FeedLocator.java:92)
        //         at blogindex.robot.AutoDiscoveryHelper.exec(AutoDiscoveryHelper.java:98)
        //         at blogindex.console.PromotePendingWeblogs.doPromotion(PromotePendingWeblogs.java:84)
        //         at blogindex.console.PromotePendingWeblogs.main(PromotePendingWeblogs.java:163)

        if ( link == null )
            return null;

        //make sure we can use this.
        if ( !isValidScheme( link ) )
            return link;

        //nothing if ALREADY relativized
        if ( isExpanded( link ) )
            return link;

        //    From: http://www.w3.org/Addressing/rfc1808.txt
        //
        //    If the parse string begins with a double-slash "//", then the
        //    substring of characters after the double-slash and up to, but not
        //    including, the next slash "/" character is the network
        //    location/login (<net_loc>) of the URL.  If no trailing slash "/"
        //    is present, the entire remaining parse string is assigned to
        //    <net_loc>.  The double- slash and <net_loc> are removed from the
        //    parse string before
        //FIXME: What happens if resource is a "file://" scheme?
        if ( link.startsWith( "//" ) ) {
            return "http:" + link;
        }

        //keep going
        if ( link.startsWith( "/" ) ) {

            link = getSite( resource ) + link;

            return link;

        } else if ( link.startsWith( "#" ) ) {

            link = resource + link;

            return link;

        } else if ( link.startsWith( "?" ) ) {

            if ( resource.contains( "?" ) ) {
                resource = resource.substring( 0, resource.indexOf( "?" ) );
            }

            return resource + link;

        } else if ( link.startsWith( ".." ) ) {

            //ok.  We need to get rid of these .. directories.

            String base = getBase( resource ) + "/";

            while ( link.startsWith( ".." ) ) {

                //get rid of the first previous dir in the link
                int begin = 2;
                if ( link.length() > 2 && link.charAt( 2 ) == '/' )
                    begin = 3;

                link = link.substring( begin, link.length() );

                //get rid of the last directory in the resource

                int end = base.length();

                if ( base.endsWith( "/" ) )
                    --end;

                int new_end = base.lastIndexOf( "/", end - 1 );

                // we can't back up pased the root
                if( new_end > 0 )
                    base = base.substring( 0, new_end );

            }

            link = base + "/" + link;

            return link;

        }

        // If the resource ends with a common file ending, then chop
        // off the file ending before adding the link
        // Is this rfc1808 compliant? Brad Neuberg, bkn3@columbia.edu
        resource = getBase(resource);

        if ( ! link.startsWith( "http://" ) ) {

            link = resource + "/" + link;
            log.debug("link="+link);

        }

        return link;

    }

    /**
     * Remove entries in the URL like /./././ which are redundant.
     *
     * Some people will have links like ./foo/bar.html
     *
     */
    public static String doRemoveDotEntries( String link ) {

        if ( link == null )
            return null;

        link = link.replaceAll( "/\\./", "/" );
        link = link.replaceAll( "/\\.$", "/" );

        return link;

    }

    /**
     * Return true if the given link is ALREADY relativized..
     *
     *
     */
    public static boolean isExpanded( String resource ) {
        return ( resource.startsWith( "http://" ) ||
                 resource.startsWith( "https://" ) ||
                 resource.startsWith( "file://" ));
    }

    /**
     * Return true if this is an valid scheme and should be expanded.
     *
     *
     */
    public static boolean isValidScheme( String resource ) {

        if (hasScheme(resource) == false)
            return true;

        //only on file: and http:

        if ( resource.startsWith( "http:" ) )
            return true;

        if ( resource.startsWith( "https:" ) )
            return true;

        if ( resource.startsWith( "file:" ) )
            return true;

        return false;

    }

    /**
     * Determines if the given resource has a scheme. (i.e. does it start with
     * "http://foo.com" or does it just have "foo.com").
     */
    public static boolean hasScheme( String resource ) {

        Pattern p = (Pattern)schemePattern.get();

        return p.matcher( resource ).matches();

    }

    /**
     * Get the site for this resource.  For example:
     *
     * http://www.foo.com/directory/index.html
     *
     * we will return
     *
     * http://www.foo.com
     *
     * for file: URLs we return file://
     *
     *
     */
    public static String getSite( String resource ) {

        if (resource.startsWith( "file:" )) {
            return "file://";
        }

        //start at 8 which is the width of http://

        int prefix = "http://".length();

        if (resource.startsWith( "https://" ) ) {
            prefix = "https://".length();
        }

        int end = 0;

        end = resource.indexOf( "/", prefix );
        if ( end != -1 ) {
            resource = resource.substring( 0, end );
        }

        end = resource.indexOf( "?", prefix );
        if ( end != -1 ) {
            resource = resource.substring( 0, end );
        }

        end = resource.indexOf( "#", prefix );
        if ( end != -1 ) {
            resource = resource.substring( 0, end );
        }

        // now try to strip trailing query string data if added.

        return resource;

    }

    /**
     * Given a URL get the domain name.
     *
     *
     */
    public static String getDomain( String resource ) {

        String site = getSite( resource );

        int firstIndex = -1;
        int indexCount = 0;

        int index = site.length();

        while ( (index = site.lastIndexOf( ".", index-1 )) != -1 ) {

            ++indexCount;

            if ( indexCount == 2 )
                break;

        }

        int begin = 7; // http:// length
        if ( indexCount >= 2 )
            begin = index + 1;

        return site.substring( begin, site.length() );

    }

    /**
     * Get the base of this URL.  For example if we are given:
     *
     * http://www.foo.com/directory/index.html
     *
     * we will return
     *
     * http://www.foo.com/directory
     *
     *
     *
     */
    public static String getBase( String resource ) {

        //FIXME: Brad says this method is totally broken.
        if ( resource == null )
            return null;

        int begin = "http://".length() + 1;

        int end = resource.lastIndexOf( "/" );

        if ( end == -1 || end <= begin ) {
            end = resource.length();
        }

        resource = resource.substring( 0, end );

        //strip query data.
        end = resource.indexOf( "?", begin );
        if ( end != -1 ) {
            resource = resource.substring( 0, end );
        }

        return resource;

    }

    public static void main( String[] args ) throws Exception {

        System.out.println( expand( "http://peerfear.org/foo/bar/", "../../blog" ) );

        System.out.println( expand( "http://peerfear.org/foo/bar/", "../../index.html" ) );

        System.out.println( expand( "http://peerfear.org/blog/", ".." ) );

        System.out.println( expand( "http://peerfear.org", "/blog" ) );
        System.out.println( expand( "http://peerfear.org", "http://peerfear.org" ) );

        System.out.println( expand( "http://peerfear.org", "blog" ) );
        System.out.println( expand( "http://peerfear.org/blog", "foo/bar" ) );

        System.out.println( expand( "file://projects/newsmonster/", "blog" ) );

        System.out.println( expand( "file:/projects/ksa/src/java/ksa/test/TestFeedTask_WithRelativePath.rss"
                                    , "/blog" ) );
    }

}

