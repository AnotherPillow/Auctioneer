package com.anotherpillow.auctioneer.util;

import java.util.concurrent.TimeUnit;

public class TimingEntry {
    private long number;
    private TimeUnit unit;

    public TimingEntry(long number, TimeUnit unit) {
        this.number = number;
        this.unit = unit;
    }

    public long toDays() {
        return TimeUnit.DAYS.convert(this.number, this.unit);
    }
    public long toHours() {
        return TimeUnit.HOURS.convert(this.number, this.unit);
    }
    public long toMinutes() {
        return TimeUnit.MINUTES.convert(this.number, this.unit);
    }
    public long toSeconds() {
        return TimeUnit.SECONDS.convert(this.number, this.unit);
    }
    public long toMilliseconds() {
        return TimeUnit.MILLISECONDS.convert(this.number, this.unit);
    }

    public String format() {
        long seconds = this.toSeconds();
        if (seconds >= 60*60*24) return this.toDays() + " days"; // >= 24 hours
        if (seconds >= 60*60) return this.toHours() + " hours"; // >= 1 hour
        if (seconds >= 60) return this.toMinutes() + " minutes"; // >= 1 minute
        return seconds + " seconds";

    }
}
