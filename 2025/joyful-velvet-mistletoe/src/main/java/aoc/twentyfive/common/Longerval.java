package aoc.twentyfive.common;

import org.jetbrains.annotations.NotNull;

public record Longerval(Long start, Long end) implements Comparable<Longerval> {

    public static Longerval from(String str) {
        var split = str.split("-");
        return new Longerval(Long.parseLong(split[0]), Long.parseLong(split[1]));
    }

    public static Longerval combine(Longerval x, Longerval y) {
        return new Longerval(Math.min(x.start, y.start), Math.max(x.end, y.end));
    }

    public boolean contains(long value) {
        return value >= start && value <= end;
    }

    @Override public int compareTo(@NotNull Longerval o) {
        return this.start.compareTo(o.start);
    }

    public long size() {
        return end - start + 1;
    }

    @Override public @NotNull String toString() {
        return "Range[start=%d, end=%d, size=%d]".formatted(start, end, size());
    }

    public boolean isOverlapping(Longerval other) {
        return other.contains(this.start()) || other.contains(this.end())
               || this.contains(other.start()) || this.contains(other.end());
    }
}
