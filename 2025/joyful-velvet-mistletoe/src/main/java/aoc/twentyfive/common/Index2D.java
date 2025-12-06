package aoc.twentyfive.common;

public record Index2D(int x, int y) {

    public Index2D up() {
        return new Index2D(x, y - 1);
    }

    public Index2D down() {
        return new Index2D(x, y + 1);
    }

    public Index2D left() {
        return new Index2D(x - 1, y);
    }

    public Index2D right() {
        return new Index2D(x + 1, y);
    }
}
