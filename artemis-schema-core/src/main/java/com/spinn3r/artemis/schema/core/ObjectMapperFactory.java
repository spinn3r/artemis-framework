package com.spinn3r.artemis.schema.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * Get a Jackson ObjectMapper with the right features enabled/disabled.
 */
public class ObjectMapperFactory {

    public static ObjectMapper newObjectMapper() {
        return newObjectMapper( false );
    }

    public static ObjectMapper newObjectMapper( boolean useUnderscoreNamingStrategy ) {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.getFactory().enable( com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS );

        if ( useUnderscoreNamingStrategy ) {
            objectMapper.setPropertyNamingStrategy( new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy() );
        }

        return objectMapper;

    }

}
