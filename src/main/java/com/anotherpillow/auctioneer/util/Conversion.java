package com.anotherpillow.auctioneer.util;

public class Conversion {
    public static long ticksUntil(long targetEpochMillis) {
        long now = System.currentTimeMillis();
        long diffMs = Math.max(0L, targetEpochMillis - now);
        return diffMs / 50L; // floor to whole ticks
    }
}
