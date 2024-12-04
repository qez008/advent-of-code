package adeventofcode;

import lombok.With;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class Day4 {

    private final List<List<Character>> charGrid;

    Day4(String path) throws IOException {
        this.charGrid = Files
                .readAllLines(Path.of(path))
                .stream()
                .map(string -> string.chars().mapToObj(x -> (char) x).toList())
                .toList();
    }

    long one() {
        var directions = List.of(
                new Vec2(1, 0),
                new Vec2(1, 1),
                new Vec2(0, 1),
                new Vec2(-1, 1),
                new Vec2(-1, 0),
                new Vec2(-1, -1),
                new Vec2(0, -1),
                new Vec2(1, -1)
        );
        var count = 0L;

        for (var i = 0; i < charGrid.size(); i++) {
            for (var j = 0; j < charGrid.get(i).size(); j++) {
                var position = new Vec2(j, i);
                count += directions
                        .stream()
                        .filter(dir -> isStartOfXmas(position, dir))
                        .count();
            }
        }
        return count;
    }


    record Vec2(@With int x, @With int y) {

        Vec2 plus(Vec2 other) {
            return new Vec2(x + other.x, y + other.y);
        }

        Vec2 reverse() {
            return new Vec2(-x, -y);
        }
    }

    boolean isStartOfXmas(Vec2 position, Vec2 direction) {
        for (var c : List.of('X', 'M', 'A', 'S')) {
            try {
                if (!charGrid.get(position.y).get(position.x).equals(c)) {
                    return false;
                }
                position = position.plus(direction);

            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    int two() {
        var count = 0;
        for (var y = 0; y < charGrid.size(); y++) {
            for (var x = 0; x < charGrid.get(y).size(); x++) {
                if (charGrid.get(y).get(x).equals('A')) {
                    var position = new Vec2(x, y);
                    if (isMasX(position)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    boolean isMasX(Vec2 position) {
        var directions = List.of(
                new Vec2(1, 1),
                new Vec2(-1, 1),
                new Vec2(-1, -1),
                new Vec2(1, -1)
        );
        var count = 0;
        for (var direction : directions) {
            try {
                var mPosition = position.plus(direction.reverse());
                var sPosition = position.plus(direction);
                if (charGrid.get(mPosition.y).get(mPosition.x).equals('M') && charGrid.get(sPosition.y).get(sPosition.x).equals('S')) {
                    count++;
                }
            } catch (IndexOutOfBoundsException e) {}
        }

        return count == 2;
    }

}
