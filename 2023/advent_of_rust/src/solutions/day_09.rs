use std::fs::read_to_string;

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_09_input.txt")
        .expect("failed to read input");

    let data = input
        .lines()
        .map(|line| parse_line(line))
        .collect::<Vec<Vec<i64>>>();

    let a1 = data
        .iter()
        .map(|sequence| extrapolate(&sequence))
        .sum::<i64>();
    println!("a1={}", a1);

    let a2 = data
        .iter()
        .map(|sequence| extrapolate_backwards(&sequence))
        .sum::<i64>();
    println!("a2={}", a2);
}

fn extrapolate_backwards(sequence: &&Vec<i64>) -> i64 {
    if sequence[1..].iter().all(|n| &sequence[0] == n) {
        return sequence[0];
    }
    let next_sequence = sequence[1..]
        .iter()
        .enumerate()
        .map(|(index, value)| value - sequence[index])
        .collect::<Vec<i64>>();

    sequence.first().unwrap() - extrapolate_backwards(&&next_sequence)
}

fn extrapolate(sequence: &&Vec<i64>) -> i64 {
    if sequence[1..].iter().all(|n| &sequence[0] == n) {
        return sequence[0];
    }
    let next_sequence = sequence[1..]
        .iter()
        .enumerate()
        .map(|(index, value)| value - sequence[index])
        .collect::<Vec<i64>>();

    sequence.last().unwrap() + extrapolate(&&next_sequence)
}

fn parse_line(line: &str) -> Vec<i64> {
    line
        .split(" ")
        .map(|word| word.parse::<i64>().unwrap())
        .collect::<Vec<i64>>()
}