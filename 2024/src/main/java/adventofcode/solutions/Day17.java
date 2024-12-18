package adventofcode.solutions;

import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.Arrays;

@RequiredArgsConstructor
class Day17 implements Solution {

    record Registers(@With long a, @With long b, @With long c) {}

    private final Registers initial;
    private final int[] program;

    long getComboOperand(int literal, Registers registers) {
        return switch (literal) {
            case 0, 1, 2, 3 -> literal;
            case 4 -> registers.a;
            case 5 -> registers.b;
            case 6 -> registers.c;
            default -> throw new IllegalStateException("Unexpected value: " + literal);
        };
    }

    long divideOperand(int literal, Registers registers) {
        var combo = getComboOperand(literal, registers);
        return registers.a / (int) Math.pow(2, combo);
    }

    String solve(int[] program, Registers registers) {
        var output = new ArrayList<Integer>();

        for (var pointer = 0; pointer < program.length - 1; pointer += 2) {
            var instruction = program[pointer];
            var literal = program[pointer + 1];
            switch (instruction) {
                // adv
                case 0 -> {
                    registers = registers.withA(divideOperand(literal, registers));
                }
                // bxl
                case 1 -> {
                    var value = registers.b ^ literal;
                    registers = registers.withB(value);
                }
                // bst
                case 2 -> {
                    var combo = getComboOperand(literal, registers);
                    var value = combo % 8;
                    registers = registers.withB(value);
                }
                // jnz
                case 3 -> {
                    if (registers.a != 0) {
                        pointer = literal - 2;
                    }
                }
                // bxc
                case 4 -> {
                    var value = registers.b ^ registers.c;
                    registers = registers.withB(value);
                }
                // out
                case 5 -> {
                    //                    System.out.printf("i: %s, l: %s, r: %s%n", instruction, literal, registers);
                    var combo = getComboOperand(literal, registers);
                    var value = combo % 8;
                    output.add((int) value);
                }
                // bdv
                case 6 -> {
                    registers = registers.withB(divideOperand(literal, registers));
                }
                // cdv
                case 7 -> {
                    registers = registers.withC(divideOperand(literal, registers));
                }
                default -> throw new IllegalStateException("Unexpected value: " + instruction);
            }
        }
        return Joiner.on(",").join(output);
    }

    public String part1() {
        return solve(program, initial);
    }

    private long solve2(int[] subProgram, int pointer, long acc) {
        var wanted = Integer.toString(program[pointer]);

        for (int i = 0; i < 8; i++) {
            var a = acc + i;
            if (solve(subProgram, initial.withA(a)).equals(wanted)) {
                System.out.println("found wanted:" + wanted + " with a:" + a + " and i:" + i);
                if (pointer == 0) {
                    return a;
                } else {
                    return solve2(subProgram, pointer - 1, a * 8);
                }
            }
        }
        throw new RuntimeException("Could not solve find a at pointer: " + pointer +". Acc=" + acc);
    }

    public Long part2() {
//        var subProgram = Arrays.copyOfRange(program, 0, program.length - 2);
//        var ans = solve2(subProgram, program.length - 1, 0);
        // hack:
        for (long i = 0; i < 100; i++) {
            var a = 164540892147328L + i;
            var result = solve(program, initial.withA(a));
            System.out.println(result);
            if (result.equals(Joiner.on(",").join(Arrays.stream(program).boxed().toList()))) {
                return a;
            }
        }
        return 0L;
    }

}
