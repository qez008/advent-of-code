package adventofcode.solutions;

import adventofcode.util.IntVector2;
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
            // The current pos contains a box, move to the next
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
                // Update pos if the box can be pushed
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

    private long gpsCoords(IntVector2 v) {
        return (long) v.y() * 100 + v.x();
    }

    public Long part1() {
        return simulate().stream().map(this::gpsCoords).reduce(0L, Long::sum);
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
        if (IntVector2.VERTICAL_DIRECTIONS.contains(dir)) {
            if (boxes.containsKey(pos)) {
                var box = boxes.get(pos);
                affectedBoxes.add(box);
                var canPushLeft = canPush(box.left.plus(dir), dir, boxes, affectedBoxes);
                var canPushRight = canPush(box.right.plus(dir), dir, boxes, affectedBoxes);
                return canPushLeft && canPushRight;
            }
            return true;
        }
        if (IntVector2.HORIZONTAL_DIRECTIONS.contains(dir)) {
            if (boxes.containsKey(pos)) {
                var box = boxes.get(pos);
                affectedBoxes.add(box);
                var p = dir.equals(IntVector2.LEFT) ? box.left : box.right;
                return canPush(p.plus(dir), dir, boxes, affectedBoxes);
            }
            return true;
        }
        throw new IllegalStateException("Unexpected value: " + dir);
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
            var nextPosition = position.plus(instruction);
            if (walls.contains(nextPosition)) {
                continue;
            }
            var affectedBoxes = new HashSet<Box>();
            if (boxes.containsKey(nextPosition)) {
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
        return boxes;
    }

    public Long part2() {
        return simulateWide().values().stream().distinct().map(Box::left).map(this::gpsCoords).reduce(0L, Long::sum);
    }
}
