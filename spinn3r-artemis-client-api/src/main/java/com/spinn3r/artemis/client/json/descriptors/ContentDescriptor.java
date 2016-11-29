package com.spinn3r.artemis.client.json.descriptors;

/**
 *
 */
@SuppressWarnings("unusedImports")
public class ContentDescriptor {

    public final String TABLE_NAME = "content";

    /**
     * <p>
     * The bucket to write this content (timestamp prefix and suffix valued from 0-99).  This allows us to use the random partitioner and still get decent parallel client read performance.
     * </p>
     */
    public final String BUCKET = "bucket";

    /**
     * <p>
     * The time our robot saw the post and wrote it to the database.  This is a sequence timestamp and supports distributed write.  This can be used as an external primary key as it's gauranteed to always be unique.  The value is opaque and not designed to be readable by humans and the format can change at any time.
     * </p>
     */
    public final String SEQUENCE = "sequence";

    /**
     * <p>
     * The sequence as a range of values between 0 and 999,999 (sequence % 100000).  This allows you to filter values by range to accept just a sample of values.
     * </p>
     */
    public final String SEQUENCE_RANGE = "sequence_range";

    /**
     * <p>
     * base64filesafe(sha1(resource)) ... Essentially the base 64 (filesafe) encoding of the sha1 of the tokenized permalink/url
     * </p>
     */
    public final String HASHCODE = "hashcode";

    /**
     * <p>
     * Tokenized form of the permalink.
     * </p>
     */
    public final String RESOURCE = "resource";

    /**
     * <p>
     * The time we fetched and added this content to our index.
     * </p>
     */
    public final String DATE_FOUND = "date_found";

    /**
     * <p>
     * The method that we used to discovery and index the content.  We have various algorithms to discover content and this lets the algorithm tag the content.
     * </p>
     */
    public final String INDEX_METHOD = "index_method";

    /**
     * <p>
     * The method we used to detect this URL was new and recently published. 
     * </p>
     */
    public final String DETECTION_METHOD = "detection_method";

    /**
     * <p>
     * The HTML content of this permalink as fetched by our robot.  Note that this is RAW content.  No cleanup is done.  Javascript is present, etc.  If you want to work with this content you must make sure to clean/sanitize it yourself.  See the 'body' field for a clean version of the document.  In some situations it's possible to not have any html.  An example is when we're using an API or firehose where the original full-html isn't present or not would just be wasteful.
     * </p>
     */
    public final String HTML = "html";

    /**
     * <p>
     * The length of the HTML
     * </p>
     */
    public final String HTML_LENGTH = "html_length";

    /**
     * <p>
     * The SHA1 checksum of the HTML.
     * </p>
     */
    public final String HTML_CHECKSUM = "html_checksum";

    /**
     * <p>
     * zlib compressed HTML content from our crawler.  Used for legacy customers who need full HTML. 
     * </p>
     */
    public final String HTML_BLOB = "html_blob";

    /**
     * <p>
     * The length of the HTML
     * </p>
     */
    public final String HTML_BLOB_LENGTH = "html_blob_length";

    /**
     * <p>
     * The SHA1 checksum of the HTML.
     * </p>
     */
    public final String HTML_BLOB_CHECKSUM = "html_blob_checksum";

    /**
     * <p>
     * $member.description
     * </p>
     */
    public final String EXTRACT_BLOB = "extract_blob";

    /**
     * <p>
     * The version of Spinn3r used to write this content.  
     * </p>
     */
    public final String VERSION = "version";

    /**
     * <p>
     * The last time we updated the metadata on this content.  On initial record creation last_updated and date_found will be identical but last_updated will change over time as we update metadata.
     * </p>
     */
    public final String LAST_UPDATED = "last_updated";

    /**
     * <p>
     * base64filesafe(sha1(resource)) of the source.  Essentially the base 64 (filesafe) encoding of the sha1 of the tokenized permalink/url of the source.
     * </p>
     */
    public final String SOURCE_HASHCODE = "source_hashcode";

    /**
     * <p>
     * The tokenized URL for this source.
     * </p>
     */
    public final String SOURCE_RESOURCE = "source_resource";

    /**
     * <p>
     * The non-tokenized URL for this source.  Use this URL if you would like to fetch this source via HTTP.
     * </p>
     */
    public final String SOURCE_LINK = "source_link";

    /**
     * <p>
     * The publisher type (mainstream news, weblog, forum, etc) of this source encoded as an int.
     * </p>
     */
    public final String SOURCE_PUBLISHER_TYPE = "source_publisher_type";

    /**
     * <p>
     * A string representing the publisher sub type which is more specific than the publisher type.  The publisher subtype is usually the name of the social network hosting the content.
     * </p>
     */
    public final String SOURCE_PUBLISHER_SUBTYPE = "source_publisher_subtype";

    /**
     * <p>
     * The time we added this source to our index.  This is the time we found the source not when it was created.
     * </p>
     */
    public final String SOURCE_DATE_FOUND = "source_date_found";

    /**
     * <p>
     * The last time our crawler visited the source and processed it with a task.  This is always incremented even if the site isn't updated or even if the site is HTTP 500 or other network/transient errors.  This may not be updated if we aren't fetching the source via HTTP.
     * </p>
     */
    public final String SOURCE_LAST_UPDATED = "source_last_updated";

    /**
     * <p>
     * The last time this source published a new HTML file (as measured by content_sha1).  This may not be updated if we aren't fetching the source via HTTP.
     * </p>
     */
    public final String SOURCE_LAST_PUBLISHED = "source_last_published";

    /**
     * <p>
     * The last time this source posted a new piece of content
     * </p>
     */
    public final String SOURCE_LAST_POSTED = "source_last_posted";

    /**
     * <p>
     * The number of milliseconds between updates to re-fetch this source.  This is used to for cyclical updates of sources and usually depends on how often the source posts updates.
     * </p>
     */
    public final String SOURCE_UPDATE_INTERVAL = "source_update_interval";

    /**
     * <p>
     * The HTTP status code of the last request to this source.
     * </p>
     */
    public final String SOURCE_HTTP_STATUS = "source_http_status";

    /**
     * <p>
     * The probability, between 0 and 1, that this source is a spam source.  -1.0 if we have not yet classified it.
     * </p>
     */
    public final String SOURCE_SPAM_PROBABILITY = "source_spam_probability";

    /**
     * <p>
     * The length, in bytes, of this HTML from the last time we fetched the page.
     * </p>
     */
    public final String SOURCE_CONTENT_LENGTH = "source_content_length";

    /**
     * <p>
     * The SHA1 checksum of the content.
     * </p>
     */
    public final String SOURCE_CONTENT_CHECKSUM = "source_content_checksum";

    /**
     * <p>
     * The set of tags assigned to this source by the either customers or spinn3r (globally).  This is used so that your client can filter by assigned tags or search by them as well.  This is not to be confused with the tags field which are assigned by the site.  These tags are opaque strings and not human readable to avoid giving away any customer information in the API.  Any sources you manually register are assigned tags with your vendor auth code.  This will allow you to register sources, and then filter / search over them.
     * </p>
     */
    public final String SOURCE_ASSIGNED_TAGS = "source_assigned_tags";

    /**
     * <p>
     * The update strategy for computing the update interval.
     * </p>
     */
    public final String SOURCE_SETTING_UPDATE_STRATEGY = "source_setting_update_strategy";

    /**
     * <p>
     * The update stratey for computing the update interval.
     * </p>
     */
    public final String SOURCE_SETTING_INDEX_STRATEGY = "source_setting_index_strategy";

    /**
     * <p>
     * Policy on handling author metadata. 
     * </p>
     */
    public final String SOURCE_SETTING_AUTHOR_POLICY = "source_setting_author_policy";

    /**
     * <p>
     * The PSHB hub this source is using.
     * </p>
     */
    public final String SOURCE_PSHB_HUB = "source_pshb_hub";

    /**
     * <p>
     * The PSHB topic this source is using.
     * </p>
     */
    public final String SOURCE_PSHB_TOPIC = "source_pshb_topic";

    /**
     * <p>
     * The last time this source posted and sent a message to the PSHB endpoint.
     * </p>
     */
    public final String SOURCE_PSHB_LAST_POSTED = "source_pshb_last_posted";

    /**
     * <p>
     * The time this PSHB lease expires.
     * </p>
     */
    public final String SOURCE_PSHB_LEASE_EXPIRES = "source_pshb_lease_expires";

    /**
     * <p>
     * The number of user interactions from other sources on this social network computed from the graph as we index content.  This is periodically computed and loaded into our source index.  This could be the number of at mentions, comment replies, etc.
     * </p>
     */
    public final String SOURCE_USER_INTERACTIONS = "source_user_interactions";

    /**
     * <p>
     * The minimum metadata score before we can persist content
     * </p>
     */
    public final String SOURCE_SETTING_MINIMUM_CONTENT_METADATA_SCORE = "source_setting_minimum_content_metadata_score";

    /**
     * <p>
     * The next time we've scheduled the source to update
     * </p>
     */
    public final String SOURCE_NEXT_UPDATE = "source_next_update";

    /**
     * <p>
     * The title of the source. 
     * </p>
     */
    public final String SOURCE_TITLE = "source_title";

    /**
     * <p>
     * A short description of the source.
     * </p>
     */
    public final String SOURCE_DESCRIPTION = "source_description";

    /**
     * <p>
     * Unique handle for this source across the entire social media property.
     * </p>
     */
    public final String SOURCE_HANDLE = "source_handle";

    /**
     * <p>
     * The number of favorites this source has according to the website or social network.
     * </p>
     */
    public final String SOURCE_FAVORITES = "source_favorites";

    /**
     * <p>
     * The number of followers this source has according to the website or social network.
     * </p>
     */
    public final String SOURCE_FOLLOWERS = "source_followers";

    /**
     * <p>
     * The number of users / friends this source is following.
     * </p>
     */
    public final String SOURCE_FOLLOWING = "source_following";

    /**
     * <p>
     * True when this user account is verified to be authentic.
     * </p>
     */
    public final String SOURCE_VERIFIED = "source_verified";

    /**
     * <p>
     * Set of URLs on other social networking sites and weblogs for this user.  These are essentially alternate profiles for the user.  Their twitter site, facebook site, etc.
     * </p>
     */
    public final String SOURCE_PROFILES = "source_profiles";

    /**
     * <p>
     * The human readable location of the source.  Example: 'Washington, DC'
     * </p>
     */
    public final String SOURCE_LOCATION = "source_location";

    /**
     * <p>
     * The URL to the img which represents this source.
     * </p>
     */
    public final String SOURCE_IMAGE_SRC = "source_image_src";

    /**
     * <p>
     * The width of the image.
     * </p>
     */
    public final String SOURCE_IMAGE_WIDTH = "source_image_width";

    /**
     * <p>
     * The height of the image.
     * </p>
     */
    public final String SOURCE_IMAGE_HEIGHT = "source_image_height";

    /**
     * <p>
     * The telephone number for this source.  Only present in limited situations.  Specifically around REVIEW sites.
     * </p>
     */
    public final String SOURCE_TELEPHONE = "source_telephone";

    /**
     * <p>
     * Tags for the source provided by the user's profile.
     * </p>
     */
    public final String SOURCE_TAGS = "source_tags";

    /**
     * <p>
     * The rating for this item provided by the user.
     * </p>
     */
    public final String SOURCE_RATING_VALUE = "source_rating_value";

    /**
     * <p>
     * The URL to the favicon which represents this source.
     * </p>
     */
    public final String SOURCE_FAVICON_SRC = "source_favicon_src";

    /**
     * <p>
     * The width of the favicon.
     * </p>
     */
    public final String SOURCE_FAVICON_WIDTH = "source_favicon_width";

    /**
     * <p>
     * The height of the favicon.
     * </p>
     */
    public final String SOURCE_FAVICON_HEIGHT = "source_favicon_height";

    /**
     * <p>
     * The time this account was created and is provided from the source.  
     * </p>
     */
    public final String SOURCE_CREATED = "source_created";

    /**
     * <p>
     * The number of Facebook likes for this source.
     * </p>
     */
    public final String SOURCE_LIKES = "source_likes";

    /**
     * <p>
     * A set of tags, optionally assigned by a site, which relate to this specific source.  Supported for medium.com only (for now)
     * </p>
     */
    public final String SOURCE_RELATED_TAGS = "source_related_tags";

    /**
     * <p>
     * The number of posts parsed/found when we last indexed this source.
     * </p>
     */
    public final String SOURCE_PARSED_POSTS = "source_parsed_posts";

    /**
     * <p>
     * The maximum number of parsed posts we've ever seen.  If parsed_posts_max greater than zero and parsed_posts is 0 then we are probably hitting a throttle or failing to parse the content.
     * </p>
     */
    public final String SOURCE_PARSED_POSTS_MAX = "source_parsed_posts_max";

    /**
     * <p>
     * The URL of the RSS feed.
     * </p>
     */
    public final String SOURCE_FEED_HREF = "source_feed_href";

    /**
     * <p>
     * The title of the feed. 
     * </p>
     */
    public final String SOURCE_FEED_TITLE = "source_feed_title";

    /**
     * <p>
     * The format of the feed as a token.  RSS or ATOM, etc.
     * </p>
     */
    public final String SOURCE_FEED_FORMAT = "source_feed_format";

    /**
     * <p>
     * The unique URL to the content.
     * </p>
     */
    public final String PERMALINK = "permalink";

    /**
     * <p>
     * A platform specific unique identifier for this post.  Note that this is NOT always present as some platforms lack the concept of unique identifiers.  Additionally, this may conflict with another identifier from another platform.
     * </p>
     */
    public final String IDENTIFIER = "identifier";

    /**
     * <p>
     * Same as permalink but if the site performs a 301 or 302 redirect this is the URL we were redirected to.
     * </p>
     */
    public final String PERMALINK_REDIRECT = "permalink_redirect";

    /**
     * <p>
     * The domain for the permalink_redirect. Identical in semantics to the domain field.
     * </p>
     */
    public final String PERMALINK_REDIRECT_DOMAIN = "permalink_redirect_domain";

    /**
     * <p>
     * The site for the permalink_redirect. The full hostname.  For example, www.cnn.com, alice.blogspot.com, etc.
     * </p>
     */
    public final String PERMALINK_REDIRECT_SITE = "permalink_redirect_site";

    /**
     * <p>
     * The primary link to the content.  The vast majority of the time, this is identical to permalink.  However, some publisher types (MEMETRACKER) have a different link to the content which is external to the site.  If the link is NOT the same as the permalink, then we include it in the links field for search and accuracy purposes.
     * </p>
     */
    public final String LINK = "link";

    /**
     * <p>
     * The domain for the link. Identical in semantics to the domain field.
     * </p>
     */
    public final String LINK_DOMAIN = "link_domain";

    /**
     * <p>
     * The site for the link. The full hostname.  For example, www.cnn.com, alice.blogspot.com, etc.
     * </p>
     */
    public final String LINK_SITE = "link_site";

    /**
     * <p>
     * The shortlink URL, if known.  This is the prefered 'short' URL discovered from either the content itself or through metdata.
     * </p>
     */
    public final String SHORTLINK = "shortlink";

    /**
     * <p>
     * The canonical URL to the content (as specified by the publisher) in rel=canonical (and other specs such as og:url).
     * </p>
     */
    public final String CANONICAL = "canonical";

    /**
     * <p>
     * The domain name of the permalink.  blogspot.com, example,com, etc. 
     * </p>
     */
    public final String DOMAIN = "domain";

    /**
     * <p>
     * The site of the permalink including the full host name.  www.cnn.com would be a site and cnn.com would be a domain.
     * </p>
     */
    public final String SITE = "site";

    /**
     * <p>
     * The actual main content of the article.  The authoritative 'main' of the post derived by removing sidebar content. (html).  This content is sanitized, cleaned so that javascript, event handlers, etc are removed.  This is analagous to the HTML5 main element.  IE the main content of the page, with no header, footer, or sidebar content.
     * </p>
     */
    public final String MAIN = "main";

    /**
     * <p>
     * The length of the main field, in bytes.
     * </p>
     */
    public final String MAIN_LENGTH = "main_length";

    /**
     * <p>
     * The checksum of the main field.
     * </p>
     */
    public final String MAIN_CHECKSUM = "main_checksum";

    /**
     * <p>
     * True when the main content is 100% accurate and the extract is not needed.
     * </p>
     */
    public final String MAIN_AUTHORITATIVE = "main_authoritative";

    /**
     * <p>
     * The format of the main element (either HTML or text)
     * </p>
     */
    public final String MAIN_FORMAT = "main_format";

    /**
     * <p>
     * The extract of the content with applied chrome/boilerpipe removal algorithms applied.  
     * </p>
     */
    public final String EXTRACT = "extract";

    /**
     * <p>
     * The length of the extract field, in bytes.
     * </p>
     */
    public final String EXTRACT_LENGTH = "extract_length";

    /**
     * <p>
     * The checksum of the extract field.
     * </p>
     */
    public final String EXTRACT_CHECKSUM = "extract_checksum";

    /**
     * <p>
     * A summary of the document computed by our document summarizer. This summary is in plain text. If mulitiple paragraphs are present they are separated by a newline. If you would like to separate the paragraphs in your UI and you're rendering HTML you can split the summary text by newline and wrap each paragraph in a P element.
     * </p>
     */
    public final String SUMMARY_TEXT = "summary_text";

    /**
     * <p>
     * The title of the post.
     * </p>
     */
    public final String TITLE = "title";

    /**
     * <p>
     * The publisher name.  (CNN, MSNBC, Techcrunch, etc)
     * </p>
     */
    public final String PUBLISHER = "publisher";

    /**
     * <p>
     * Articles may belong to one or more 'sections' in a magazine or newspaper, such as Sports, Lifestyle, etc.
     * </p>
     */
    public final String SECTION = "section";

    /**
     * <p>
     * A short description of the item (HTML)
     * </p>
     */
    public final String DESCRIPTION = "description";

    /**
     * <p>
     * Tags for the item.
     * </p>
     */
    public final String TAGS = "tags";

    /**
     * <p>
     * Username mentions for users within the content of this post.
     * </p>
     */
    public final String MENTIONS = "mentions";

    /**
     * <p>
     * All outbound links in the main element.  Since main is the authoritative content, without chrome or sidebar content, this can be used for ranking purposes.
     * </p>
     */
    public final String LINKS = "links";

    /**
     * <p>
     * Date of first broadcast/publication.
     * </p>
     */
    public final String PUBLISHED = "published";

    /**
     * <p>
     * The date on which the content was most recently modified.
     * </p>
     */
    public final String MODIFIED = "modified";

    /**
     * <p>
     * This is identical to `published` except it's a partial value.  If an exact date is found we both fields are populated but if we only have a partial date then we only specify this field.  The value is ISO8601.  For example, 2014-01-01.
     * </p>
     */
    public final String PUBLISHED_PARTIAL = "published_partial";

    /**
     * <p>
     * This is identical to `modified` except it's a partial value.  If an exact date is found we both fields are populated but if we only have a partial date then we only specify this field.  The value is ISO8601.  For example, 2014-01-01.
     * </p>
     */
    public final String MODIFIED_PARTIAL = "modified_partial";

    /**
     * <p>
     * The name of the author.  This is the human readable name like 'Barack Obama' or 'Michael Jordan'
     * </p>
     */
    public final String AUTHOR_NAME = "author_name";

    /**
     * <p>
     * The link for the author.
     * </p>
     */
    public final String AUTHOR_LINK = "author_link";

    /**
     * <p>
     * The handle of the author.  This is a unique token/handle for the author across the whole site.  For example 'barackobama' and would never conflict with another account.
     * </p>
     */
    public final String AUTHOR_HANDLE = "author_handle";

    /**
     * <p>
     * The number of followers for this author.
     * </p>
     */
    public final String AUTHOR_FOLLOWERS = "author_followers";

    /**
     * <p>
     * The location for this author.
     * </p>
     */
    public final String AUTHOR_LOCATION = "author_location";

    /**
     * <p>
     * The URL to the img which is an avatar for the user who posted this content.
     * </p>
     */
    public final String AUTHOR_AVATAR_IMG = "author_avatar_img";

    /**
     * <p>
     * The width of the avatar img.
     * </p>
     */
    public final String AUTHOR_AVATAR_WIDTH = "author_avatar_width";

    /**
     * <p>
     * The height of the avatar img.
     * </p>
     */
    public final String AUTHOR_AVATAR_HEIGHT = "author_avatar_height";

    /**
     * <p>
     * Deprecated.  Use author_handle instead
     * </p>
     */
    public final String AUTHOR_TWITTER_HANDLE = "author_twitter_handle";

    /**
     * <p>
     * Deprecated.  Use author_user_id instead.
     * </p>
     */
    public final String AUTHOR_TWITTER_USERID = "author_twitter_userid";

    /**
     * <p>
     * User ID in the target platform (when available)
     * </p>
     */
    public final String AUTHOR_USER_ID = "author_user_id";

    /**
     * <p>
     * When present, the gender of the author.
     * </p>
     */
    public final String AUTHOR_GENDER = "author_gender";

    /**
     * <p>
     * The human readable location of the source.  Example: 'Washington, DC'
     * </p>
     */
    public final String GEO_LOCATION = "geo_location";

    /**
     * <p>
     * The location identifier (if available) for this location.  This is platform specific.
     * </p>
     */
    public final String GEO_LOCATION_ID = "geo_location_id";

    /**
     * <p>
     * Name of the feature we're representing.
     * </p>
     */
    public final String GEO_FEATURENAME = "geo_featurename";

    /**
     * <p>
     * A point contains a single latitude-longitude pair, separated by whitespace.
     * </p>
     */
    public final String GEO_POINT = "geo_point";

    /**
     * <p>
     * A bounding box is a rectangular region, often used to define the extents of a map or a rough area of interest. A box contains two space seperate latitude-longitude pairs, with each pair separated by whitespace. The first pair is the lower corner, the second is the upper corner.
     * </p>
     */
    public final String GEO_BOX = "geo_box";

    /**
     * <p>
     * Id in geonames database.
     * </p>
     */
    public final String GEO_NAME_ID = "geo_name_id";

    /**
     * <p>
     * The human readable location including its parent locations
     * </p>
     */
    public final String GEO_NAME = "geo_name";

    /**
     * <p>
     * The human readable country derived from geo_location.  These are represented as ISO 3166-1 alpha-2: https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
     * </p>
     */
    public final String GEO_COUNTRY = "geo_country";

    /**
     * <p>
     * The human readable state derived from geo_location.  
     * </p>
     */
    public final String GEO_STATE = "geo_state";

    /**
     * <p>
     * The human readable city derived from geo_location.  
     * </p>
     */
    public final String GEO_CITY = "geo_city";

    /**
     * <p>
     * Contains the name of the field used to parse the geo data
     * </p>
     */
    public final String GEO_METHOD = "geo_method";

    /**
     * <p>
     * The rating for this item provided by the user. 
     * </p>
     */
    public final String RATING_VALUE = "rating_value";

    /**
     * <p>
     * The URL to the favicon which represents this source.
     * </p>
     */
    public final String FAVICON_SRC = "favicon_src";

    /**
     * <p>
     * The width of the favicon.
     * </p>
     */
    public final String FAVICON_WIDTH = "favicon_width";

    /**
     * <p>
     * The height of the favicon.
     * </p>
     */
    public final String FAVICON_HEIGHT = "favicon_height";

    /**
     * <p>
     * The URL to the img which represents this content.
     * </p>
     */
    public final String IMAGE_SRC = "image_src";

    /**
     * <p>
     * The width of the image.
     * </p>
     */
    public final String IMAGE_WIDTH = "image_width";

    /**
     * <p>
     * The height of the image.
     * </p>
     */
    public final String IMAGE_HEIGHT = "image_height";

    /**
     * <p>
     * True when this source was not published by the original user but actually shared from someone the source follows.  On microblogging platforms this is a retweet.  On others it's a shared post. 
     * </p>
     */
    public final String SHARED = "shared";

    /**
     * <p>
     * The type of shared content.
     * </p>
     */
    public final String SHARED_TYPE = "shared_type";

    /**
     * <p>
     * Deprecated: See shared_author_link
     * </p>
     */
    public final String SHARED_PROFILE_LINK = "shared_profile_link";

    /**
     * <p>
     * Deprecated: See shared_author_name
     * </p>
     */
    public final String SHARED_PROFILE_TITLE = "shared_profile_title";

    /**
     * <p>
     * The link to the profile of the person who originally posted this story.
     * </p>
     */
    public final String SHARED_AUTHOR_LINK = "shared_author_link";

    /**
     * <p>
     * The title of the profile of the person who originally posted this story.
     * </p>
     */
    public final String SHARED_AUTHOR_NAME = "shared_author_name";

    /**
     * <p>
     * User ID in the target platform (when available)
     * </p>
     */
    public final String SHARED_AUTHOR_USER_ID = "shared_author_user_id";

    /**
     * <p>
     * A platform specific unique identifier for this post.
     * </p>
     */
    public final String SHARED_IDENTIFIER = "shared_identifier";

    /**
     * <p>
     * The unique URL to the content.
     * </p>
     */
    public final String SHARED_PERMALINK = "shared_permalink";

    /**
     * <p>
     * The handle of the author.  This is a unique token/handle for the author across the whole site.  For example 'barackobama' and would never conflict with another account.
     * </p>
     */
    public final String SHARED_AUTHOR_HANDLE = "shared_author_handle";

    /**
     * <p>
     * True when this source was a reply, false otherwhise
     * </p>
     */
    public final String REPLIED = "replied";

    /**
     * <p>
     * The link to the profile of the person being replied to.
     * </p>
     */
    public final String REPLIED_PROFILE_LINK = "replied_profile_link";

    /**
     * <p>
     * The title of the profile of the person being replied to.
     * </p>
     */
    public final String REPLIED_PROFILE_TITLE = "replied_profile_title";

    /**
     * <p>
     * When present, the type of card that can be used to display this content within web applications
     * </p>
     */
    public final String CARD = "card";

    /**
     * <p>
     * The URL to an iframe which can be embedded to play this video.  HTTPS URL to iframe player. This must be a HTTPS URL which does not generate active mixed content warnings in a web browser
     * </p>
     */
    public final String VIDEO_PLAYER = "video_player";

    /**
     * <p>
     * The width of the player iframe.
     * </p>
     */
    public final String VIDEO_PLAYER_WIDTH = "video_player_width";

    /**
     * <p>
     * The height of the player iframe.
     * </p>
     */
    public final String VIDEO_PLAYER_HEIGHT = "video_player_height";

    /**
     * <p>
     * The type of this content as either a POST or a COMMENT.  This allows us to index posts and comments through the same API.
     * </p>
     */
    public final String TYPE = "type";

    /**
     * <p>
     * The overall sentiment for this content
     * </p>
     */
    public final String SENTIMENT = "sentiment";

    /**
     * <p>
     * ISO language code for this source.  All our language codes are ISO 639 two letter lang codes. We use the special lang code of U when we are unable to determine the language from the underlying text - usually because we don't have enough data.
     * </p>
     */
    public final String LANG = "lang";

    /**
     * <p>
     * Provides a map between algorithmically determined categories (entertainment, politics, technology, science, sports, business, health) and their probabilities.  The probabilities are between 0.0 and 1.0 and if you sum them all they will equal 1.0.  
     * </p>
     */
    public final String CATEGORIES = "categories";

    /**
     * <p>
     * Provides data on previously posted documents which are duplicates of this document.  Keys are sequence values for the documents and the is a double between 0.0 and 1.0 where 0.0 is no duplication and 1.0 is full duplication
     * </p>
     */
    public final String DUPLICATES = "duplicates";

    /**
     * <p>
     * The total number of duplicates.
     * </p>
     */
    public final String DUPLICATES_COUNT = "duplicates_count";

    /**
     * <p>
     * Provides a map between algorithmically determined classifications driven by customers.  The keys are keys given to customers identify their classification and the value is the probability of that classification.  The values DO NOT sum to 1.0 as there may be multiple classifications here.
     * </p>
     */
    public final String CLASSIFICATIONS = "classifications";

    /**
     * <p>
     * See content.hashcode
     * </p>
     */
    public final String PARENT_HASHCODE = "parent_hashcode";

    /**
     * <p>
     * See content.permalink
     * </p>
     */
    public final String PARENT_PERMALINK = "parent_permalink";

    /**
     * <p>
     * See content.title
     * </p>
     */
    public final String PARENT_TITLE = "parent_title";

    /**
     * <p>
     * See content.lang
     * </p>
     */
    public final String PARENT_LANG = "parent_lang";

    /**
     * <p>
     * See content.resource
     * </p>
     */
    public final String PARENT_RESOURCE = "parent_resource";

    /**
     * <p>
     * The number of likes for this post (when we first find it).  Note that this field DOES NOT update dynamically.
     * </p>
     */
    public final String LIKES = "likes";

    /**
     * <p>
     * The number of dislikes for this post (when we first find it).  Note that this field DOES NOT update dynamically.
     * </p>
     */
    public final String DISLIKES = "dislikes";

    /**
     * <p>
     * The number of comments for this post (when we first find it).  Note that this field DOES NOT update dynamically.
     * </p>
     */
    public final String COMMENTS = "comments";

    /**
     * <p>
     * The number of views for this post (when we first find it).  Note that this field DOES NOT update dynamically.
     * </p>
     */
    public final String VIEWS = "views";

    /**
     * <p>
     * The quality of the metadata on this post. Used internally to audit the quality of Spinn3r data.  Not very applicable to customer use.
     * </p>
     */
    public final String METADATA_SCORE = "metadata_score";

    /**
     * <p>
     * The number of shares for this post.  For some microblogging platforms this could be a rewtweet but for others its a share.  Most platforms have this concept.
     * </p>
     */
    public final String SHARES = "shares";

    /**
     * <p>
     * The number of updates to metadata we have
     * </p>
     */
    public final String METADATA_UPDATES = "metadata_updates";

    /**
     * <p>
     * True when when the user has pinned this content to their profile effectively locking the post in place.
     * </p>
     */
    public final String PINNED = "pinned";

    /**
     * <p>
     * The underlying serialization format
     * </p>
     */
    public final String SERIALIZATION_FORMAT = "serialization_format";

    /**
     * <p>
     * The serialized form of this record
     * </p>
     */
    public final String SERIALIZED = "serialized";

}