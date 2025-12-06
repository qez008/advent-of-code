package aoc.twentyfive.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtil {

    public static int posMod(int x, int y) {
        var z = x % y;
        if (z < 0) {
            z += y;
        }
        return z;
    }
}
