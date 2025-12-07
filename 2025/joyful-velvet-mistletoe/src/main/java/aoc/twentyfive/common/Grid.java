package aoc.twentyfive.common;

import java.util.List;
import java.util.NoSuchElementException;

public record Grid<T>(List<? extends List<T>> data) {

    public Vec2<Integer> posOfFirst(T value) {
        for (var y = 0; y < data.size(); y++) {
            for (var x = 0; x < data.get(y).size(); x++) {
                if (data.get(y).get(x).equals(value)) {
                    return new Vec2<>(x, y);
                }
            }
        }
        throw new NoSuchElementException();
    }

    public int h() {
        return data.size();
    }

    public T get(Vec2<Integer> pos) {
        return data.get(pos.y()).get(pos.x());
    }

    public void set(Vec2<Integer> pos, T value) {
        data.get(pos.y()).set(pos.x(), value);
    }
}
