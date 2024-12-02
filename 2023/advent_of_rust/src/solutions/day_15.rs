use std::fs::read_to_string;
use std::time::Instant;
use crate::solutions::day_15::Op::{DASH, EQUALS};

pub(crate) fn solve() {
    let instant = Instant::now();
    let input = read_to_string("src/inputs/day_15_input.txt")
        .expect("failed to read input");
    let read_time = instant.elapsed();
    println!("read input in {:?}", read_time);

    // part 1:
    let sum = input
        .split(",")
        .fold(0, |acc, word| acc + hash(word));
    let part_1_time = instant.elapsed();
    println!("sum={} (in {:?})", sum, part_1_time - read_time);

    // part 2:
    let mut boxes: Vec<Vec<(&str, usize)>> = vec![vec![]; 256];
    for instruction in input.split(",") {
        let (index, label, operation) = parse_instruction(instruction);
        let label_index = boxes[index].iter().position(|(l, _)| l == &label);
        match operation {
            DASH => {
                if label_index.is_some() {
                    boxes[index].remove(label_index.unwrap());
                }
            }
            EQUALS(x) => {
                if label_index.is_some() {
                    boxes[index][label_index.unwrap()] = (label, x);
                } else {
                    boxes[index].push((label, x));
                }
            }
        }
    }
    let total_focusing_power = boxes
        .iter()
        .enumerate()
        .map(|x| focusing_power(x))
        .sum::<usize>();
    let part_2_time = instant.elapsed();
    println!("power={} (in {:?})", total_focusing_power, part_2_time - part_1_time);
    println!("finshed in {:?}", part_2_time);
}

fn hash(word: &str) -> i64 {
    word.chars().fold(0, |acc, c| { (acc + c as u8 as i64) * 17 % 256 })
}

#[derive(Debug)]
enum Op {
    DASH,
    EQUALS(usize),
}

fn parse_instruction(word: &str) -> (usize, &str, Op) {
    if word.contains("-") {
        let label = word.trim_end_matches('-');
        (hash(label) as usize, label, DASH)
    } else {
        let split: Vec<_> = word.split('=').collect();
        (hash(split[0]) as usize, split[0], EQUALS(split[1].parse::<usize>().unwrap()))
    }
}

fn focusing_power((j, b): (usize, &Vec<(&str, usize)>)) -> usize {
    (j + 1) * b.iter().enumerate().map(|(i, (_, v))| (i + 1) * v).sum::<usize>()
}
