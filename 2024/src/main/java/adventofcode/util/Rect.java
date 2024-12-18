package adventofcode.util;

public record Rect(IntVector2 start, IntVector2 end) {

    public boolean contains(IntVector2 point) {
        if (point.x() < start.x() || point.y() < start.y()) {
            return false;
        }
        if (point.x() > end.x() || point.y() > end.y()) {
            return false;
        }
        return true;
    }
}
