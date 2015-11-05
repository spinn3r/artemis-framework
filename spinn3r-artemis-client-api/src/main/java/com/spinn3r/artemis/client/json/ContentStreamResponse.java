package com.spinn3r.artemis.client.json;

/**
 *
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.schema.core.ObjectMapperFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encodes a JSON response for a client.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContentStreamResponse {

    protected Map<String,String> headers = new HashMap<>();

    protected List<Content> content = Lists.newArrayList();

    protected String query = null;

    protected long bucket = 0;

    protected String filter;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getBucket() {
        return bucket;
    }

    public void setBucket(long bucket) {
        this.bucket = bucket;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String toJSON() {

        try {
            ObjectMapper mapper = ObjectMapperFactory.newObjectMapper( Content.UNDERSCORE );
            return mapper.writeValueAsString( this );
        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }

    public static ContentStreamResponse fromJSON( String content ) {

        try {

            ObjectMapper mapper = ObjectMapperFactory.newObjectMapper( Content.UNDERSCORE );
            return mapper.readValue( content, ContentStreamResponse.class );

        } catch (IOException e) {
            throw new RuntimeException( e );
        }

    }

    public static ContentStreamResponse fromJSON( InputStream is ) throws IOException {

        ObjectMapper mapper = ObjectMapperFactory.newObjectMapper( Content.UNDERSCORE );
        return mapper.readValue( is, ContentStreamResponse.class );

    }

}
