package com.spinn3r.artemis.jcommander.optionals;

import com.beust.jcommander.IStringConverter;

import java.util.Optional;

/**
 *
 */
public class OptionalIntegerConverter implements IStringConverter<Optional<Integer>> {

    @Override
    public Optional<Integer> convert(String value) {

        if ( value == null )
            return Optional.empty();

        return Optional.of(Integer.parseInt(value));

    }

}
