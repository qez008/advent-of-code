package adeventofcode.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Index2dTest {

    @Test
    void test() {
        var expected = List.of(
                new Index2d(0, 0),
                new Index2d(1, 0),
                new Index2d(0, 1),
                new Index2d(1, 1),
                new Index2d(0, 2),
                new Index2d(1, 2)
        );

        assertEquals(expected, Index2d.createStream(3, 2).toList());
    }

}