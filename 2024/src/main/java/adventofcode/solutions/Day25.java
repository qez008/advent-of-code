package adventofcode.solutions;

import com.google.common.collect.Lists;

import java.util.List;

@lombok.RequiredArgsConstructor
class Day25 implements Solution {

    private final List<int[]> locks;
    private final List<int[]> keys;

    public Integer part1() {
        return (int) Lists
                .cartesianProduct(locks, keys)
                .stream()
                .filter(xs -> fits(xs.get(0), xs.get(1)))
                .count();
    }

    public Object part2() {
        return null;
    }

    boolean fits(int[] lock, int[] key) {
        for (var i = 0; i < lock.length; i++) {
            if (lock[i] + key[i] > 5) {
                return false;
            }
        }
        return true;
    }
}
