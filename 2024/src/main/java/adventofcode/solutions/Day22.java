package adventofcode.solutions;

import java.util.*;

@lombok.RequiredArgsConstructor
class Day22 implements Solution {

    private final List<Integer> initialSecretNumbers;

    public Object part1() {
        var ans = 0L;
        for (var secretNumber : initialSecretNumbers) {
            long number = secretNumber;
            for (var i = 0; i < 2000; i++) {
                number = evolve(number);
            }
            ans += number;
        }
        return ans;
    }

    public Object part2() {
        var sums = new HashMap<List<Integer>, Long>();
        for (var secretNumber : initialSecretNumbers) {
            var seen = findSequences(secretNumber);
            for (var entry : seen.entrySet()) {
                if (sums.get(entry.getKey()) instanceof Long value) {
                    sums.put(entry.getKey(), value + (long) entry.getValue());
                } else {
                    sums.put(entry.getKey(), (long) entry.getValue());
                }
            }
        }
        var ans = sums.values().stream().max(Long::compareTo).orElseThrow();
        System.out.println(
                sums.entrySet().stream().filter(e -> e.getValue().equals(ans)).toList());
        return ans;
    }

    static long evolve(long secret) {
        // step 1
        var a = secret * 64L;
        secret = prune(mix(a, secret));
        // step 2
        var b = (long) Math.floor(secret / 32.0);
        secret = prune(mix(b, secret));
        // step 3
        var c = secret * 2048L;
        secret = prune(mix(c, secret));

        return secret;
    }

    static long mix(long value, long secret) {
        return value ^ secret;
    }

    static long prune(long secret) {
        return secret % 16777216L;
    }

    static HashMap<List<Integer>, Integer> findSequences(int secret) {
        long number = secret;
        var previousValues = new LinkedList<Integer>() {
            public boolean add(Integer number) {
                super.add(number);
                if (this.size() > 5) {
                    this.removeFirst();
                }
                return true;
            }
        };

        previousValues.add(secret);

        var seen = new HashMap<List<Integer>, Integer>();

        for (var i = 0; i <= 2000; i++) {
            number = evolve(number);

            var value = (int) (number % 10);
            previousValues.add(value);

            if (previousValues.size() == 5) {
//                System.out.println(number);
                var sequence = seq(previousValues);
                if (seen.containsKey(sequence)) {
//                    System.out.println("seen sequence before: " + sequence);
                } else {
//                    System.out.println(previousValues);
//                    System.out.println(sequence);
                    seen.put(sequence, value);
                }
//                System.out.println();
            }
        }
        return seen;
    }

    static List<Integer> seq(LinkedList<Integer> previousValues) {
        var sequence = new ArrayList<Integer>();
        Integer prev = null;
        for (var e : previousValues) {
            if (prev != null) {
                sequence.add(e - prev);
            }
            prev = e;
        }
        return sequence;
    }
}
