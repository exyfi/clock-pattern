package com.exyfi.clock;


import java.time.Instant;
import java.time.temporal.TemporalUnit;

/**
 * Clock pattern implementation.
 */
public class SetableClock implements Clock {

    private Instant now;

    public SetableClock(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    public void plus(long amountToAdd, TemporalUnit unit) {
        setNow(now.plus(amountToAdd, unit));
    }

    public void minus(long amountToExtract, TemporalUnit unit) {
        setNow(now.minus(amountToExtract, unit));
    }
}
