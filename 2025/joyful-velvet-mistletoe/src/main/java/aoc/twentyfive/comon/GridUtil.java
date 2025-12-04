package aoc.twentyfive.comon;

import java.util.ArrayList;
import java.util.List;

public class GridUtil {

    public static List<Index2D> indices2D(int width, int height) {
        var indices = new ArrayList<Index2D>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                indices.add(new Index2D(col, row));
            }
        }
        return indices;
    }

    public static List<Index2D> neighborsOf(Index2D index) {
        var neighbors = new ArrayList<Index2D>();
        for (var y = -1; y <= 1; y++) {
            for (var x = -1; x <= 1; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                neighbors.add(new Index2D(index.x() + x, index.y() + y));
            }
        }
        return neighbors;
    }
}
