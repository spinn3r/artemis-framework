package com.spinn3r.artemis.util.math;

import com.spinn3r.artemis.util.misc.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 *
 */
public class Median {

    private int scale = 2;

    private RoundingMode roundingMode = RoundingMode.DOWN;

    public Median setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public Median setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
        return this;
    }

    public BigDecimal of(Long... values) {
        return of(Arrays.asList(values));
    }

    public BigDecimal of(List<Long> values) {

        checkArgument(values.size() > 0, "No values given.");

        values = CollectionUtils.sort(values);

        int mid = values.size() / 2;

        if (values.size() % 2 == 0) {
            //even
            long v0 = values.get(mid-1);
            long v1 = values.get(mid);

            return new Mean().setScale(scale)
                             .setRoundingMode(roundingMode)
                             .of(v0, v1);

        } else {
            //odd
            return BigDecimal.valueOf(values.get(mid));
        }

    }

}
