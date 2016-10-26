package com.spinn3r.artemis.jcommander.converters;

import com.beust.jcommander.IStringConverter;
import com.spinn3r.artemis.datetime.durations.LiberalDurationParser;

import java.time.Duration;

/**
 *
 */
public class LiberalDurationConverter implements IStringConverter<Duration> {

    @Override
    public Duration convert(String text) {
        return LiberalDurationParser.parse(text).get();
    }

}
