package adeventofcode.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntVector2Test {

    @Test
    void streamTest() {
        var expected = List.of(
                new IntVector2(0, 0),
                new IntVector2(1, 0),
                new IntVector2(0, 1),
                new IntVector2(1, 1),
                new IntVector2(0, 2),
                new IntVector2(1, 2)
        );

        assertEquals(expected, IntVector2.indexStream(3, 2).toList());
    }

    @Test
    void rotateTest() {
        var expected = List.of(
                new IntVector2(0, 1),  // v
                new IntVector2(-1, 0), // <
                new IntVector2(0, -1), // ^
                new IntVector2(1, 0)   // >
        );

        var index = new IntVector2(1, 0);
        for (var e : expected) {
            index = index.rotateClockwise90();
            assertEquals(e, index);
        }
    }

}