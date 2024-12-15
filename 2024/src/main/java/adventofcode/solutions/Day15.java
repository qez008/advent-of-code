package adventofcode.solutions;

import adventofcode.util.IntVector2;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
class Day15 implements Solution {

    private final IntVector2 startPosition;
    private final Set<IntVector2> startingBoxes;
    private final Set<IntVector2> walls;
    private final String instructions;

    private Optional<IntVector2> openPosition(IntVector2 pos, IntVector2 dir, Set<IntVector2> boxes) {
        while (true) {
            // Hit a wall before finding a free slot
            if (walls.contains(pos)) {
                return Optional.empty();
            }
            // Found a free slot
            if (!boxes.contains(pos)) {
                return Optional.of(pos);
            }
            // The current position contains a box, move to the next
            pos = pos.plus(dir);
        }
    }

    private Set<IntVector2> simulate() {
        var position = startPosition;
        var boxes = new HashSet<>(startingBoxes);

        for (var c : instructions.split("")) {
            var instruction = IntVector2.parse(c);
            var nextPosition = position.plus(instruction);
            if (walls.contains(nextPosition)) {
                // Hit a wall, do nothing:
                continue;
            }
            if (boxes.contains(nextPosition)) {
                var openPos = openPosition(nextPosition, instruction, boxes);
                // Update position if the box can be pushed
                if (openPos.isPresent()) {
                    boxes.remove(nextPosition);
                    boxes.add(openPos.get());
                    position = nextPosition;
                }
                continue;
            }
            position = nextPosition;
        }

        return boxes;
    }


    public long part1() {
        return simulate().stream().map(v -> (long) v.y() * 100 + v.x()).reduce(0L, Long::sum);
    }

    record Box(IntVector2 left, IntVector2 right) {

        public Box push(IntVector2 dir) {
            return new Box(left.plus(dir), right.plus(dir));
        }
    }

    private boolean canPush(IntVector2 pos,
                            IntVector2 dir,
                            Map<IntVector2, Box> boxes,
                            Set<Box> affectedBoxes) {

        if (walls.contains(pos)) {
            return false;
        }
        if (!boxes.containsKey(pos)) {
            return true;
        }
        return switch (dir) {
            case IntVector2 v when IntVector2.VERTICAL_DIRECTIONS.contains(v) -> {
                if (boxes.containsKey(pos)) {
                    var box = boxes.get(pos);
                    affectedBoxes.add(box);
                    yield canPush(box.left.plus(v), v, boxes, affectedBoxes)
                          && canPush(box.right.plus(v), v, boxes, affectedBoxes);
                }
                yield true;
            }
            case IntVector2 h when IntVector2.HORIZONTAL_DIRECTIONS.contains(h) -> {
                if (boxes.containsKey(pos)) {
                    var box = boxes.get(pos);
                    affectedBoxes.add(box);
                    var d = h.equals(IntVector2.LEFT) ? box.left : box.right;
                    yield canPush(d.plus(h), h, boxes, affectedBoxes);
                }
                yield true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + dir);
        };
    }

    private void updateBox(Box box, Map<IntVector2, Box> boxes, IntVector2 dir) {
        if (boxes.get(box.left) == box) {
            boxes.remove(box.left);
        }
        if (boxes.get(box.right) == box) {
            boxes.remove(box.right);
        }
        var newBox = box.push(dir);
        boxes.put(newBox.left, newBox);
        boxes.put(newBox.right, newBox);
    }

    private HashMap<IntVector2, Box> simulateWide() {
        var position = startPosition;

        var boxes = new HashMap<IntVector2, Box>();

        for (var pos : startingBoxes) {
            var box = new Box(pos, pos.plus(IntVector2.RIGHT));
            boxes.put(box.left, box);
            boxes.put(box.right, box);
        }

        for (var c : instructions.split("")) {
            var instruction = IntVector2.parse(c);

//            printGrid(boxes, position, instruction);
            var nextPosition = position.plus(instruction);
            if (walls.contains(nextPosition)) {
                // Hit a wall, do nothing:
                continue;
            }
            var affectedBoxes = new HashSet<Box>();
            if (boxes.containsKey(nextPosition)) {
                affectedBoxes.add(boxes.get(nextPosition));
                if (canPush(nextPosition, instruction, boxes, affectedBoxes)) {
                    for (var box : affectedBoxes) {
                        updateBox(box, boxes, instruction);
                    }
                } else {
                    continue;
                }
            }
            position = nextPosition;
        }

//        printGrid(boxes, position, null);

        return boxes;
    }

    private void printGrid(HashMap<IntVector2, Box> boxes, IntVector2 position, IntVector2 dir) {
        var w = 22;
        var h = 12;
        var grid = new Character[h][w];
        for (var i = 0; i < h; i++) {
            for (var j = 0; j < w; j++) {
                grid[i][j] = '.';
            }
        }
        for (var box : boxes.values().stream().distinct().toList()) {
            grid[box.left.y()][box.left.x()] = '[';
            grid[box.right.y()][box.right.x()] = ']';
        }
        for (var wall : walls) {
            grid[wall.y()][wall.x()] = '#';
        }
        grid[position.y()][position.x()] = (dir == null) ? '@' : dir.toChar();
        for (var row : grid) {
            System.out.println(Joiner.on("").join(row));
        }
    }

    public long part2() {
        return simulateWide().values().stream().distinct().map(Box::left)
                .map(v -> (long) v.y() * 100 + v.x()).reduce(0L, Long::sum);
    }
}
