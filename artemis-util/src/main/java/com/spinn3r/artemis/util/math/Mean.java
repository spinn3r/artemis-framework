package com.spinn3r.artemis.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 *
 */
public class Mean {

    private int scale = 2;

    private RoundingMode roundingMode = RoundingMode.DOWN;

    public Mean setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public Mean setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
        return this;
    }

    public BigDecimal of(Long... input) {
        return of(Arrays.asList(input ) );
    }

    public BigDecimal of(List<Long> values) {

        checkArgument(values.size() > 0, "No values given.");

        BigDecimal sum = BigDecimal.valueOf(0);

        for (long current : values) {
            sum = sum.add(BigDecimal.valueOf(current));
        }

        return sum.divide(BigDecimal.valueOf(values.size()), scale, roundingMode);

    }

}
