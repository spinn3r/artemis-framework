package com.spinn3r.artemis.jcommander.optionals;

import com.beust.jcommander.IStringConverter;

import java.util.Optional;

/**
 *
 */
public class OptionalStringConverter implements IStringConverter<Optional<String>> {

    @Override
    public Optional<String> convert(String value) {

        if ( value == null )
            return Optional.empty();

        return Optional.of(value);

    }

}
