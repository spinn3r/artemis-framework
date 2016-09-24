package com.spinn3r.artemis.jcommander.converters.optional;

import com.beust.jcommander.IStringConverter;

import java.util.Optional;

/**
 *
 */
public class OptionalStringConverter


    @Override
    public Optional<String> convert(String value) {

        if ( value == null )
            return Optional.empty();

        return Optional.of(value);

    }

}
