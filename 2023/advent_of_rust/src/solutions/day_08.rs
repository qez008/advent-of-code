use std::collections::HashMap;
use std::fs::read_to_string;
use regex::Regex;

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_08_input.txt")
        .expect("failed to read input");

    let lines = input
        .lines()
        .collect::<Vec<&str>>();

    let instructions = lines[0].chars().collect::<Vec<char>>();
    println!("{:?}", instructions);

    let re = Regex::new(r"\b\w{3}\b").unwrap();
    let mut map: HashMap<&str, (&str, &str)> = HashMap::new();
    for line in &lines[2..] {
        let matches = re
            .find_iter(line)
            .map(|m| m.as_str())
            .collect::<Vec<&str>>();
        map.insert(matches[0], (matches[1], matches[2]));
    }
    println!("{:?}", &map);
    println!("a1={}", part_1("AAA", "ZZZ", &instructions, &map));
    println!("a2={}", part_2(&instructions, &map));
}

fn part_1(start: &str,
          end: &str,
          instructions: &Vec<char>,
          map: &HashMap<&str, (&str, &str)>) -> i32 {

    let num_instructions = instructions.len() as i32;
    let mut position = start;
    let mut steps = 0;
    while !position.ends_with(end) {
        let (left, right) = map.get(position)
            .expect(&format!("position {} does not exists!", position));
        let instruction = instructions[(steps % num_instructions) as usize];
        position = match instruction {
            'L' => left,
            'R' => right,
            _ => !panic!("unknown instruction {}!", instruction),
        };
        steps += 1;
    }
    steps
}

fn part_2(instructions: &Vec<char>, map: &HashMap<&str, (&str, &str)>) -> u64 {
    map.keys()
        .filter(|k| k.ends_with("A"))
        .map(|start| {
            let steps = part_1(start, "Z", instructions, map) as u64;
            println!("{}", steps);
            steps
        })
        .fold(1u64, |acc, number| lcm(acc, number))
}

fn gcd(a: u64, b: u64) -> u64 {
    if b == 0 { a } else { gcd(b, a % b) }
}

fn lcm(a: u64, b: u64) -> u64 {
    a * b / gcd(a, b)
}

fn part_2_snail_edition(instructions: &Vec<char>, map: &HashMap<&str, (&str, &str)>) -> i32 {
    let num_instructions = instructions.len() as i32;
    let mut positions = map
        .keys()
        .filter(|k| k.ends_with("A"))
        .collect::<Vec<&&str>>();

    for steps in 0.. {
        if positions.iter().all(|s| s.ends_with("Z")) {
            return steps;
        }
        let instruction = instructions[(steps % num_instructions) as usize];
        for i in 0..positions.len() {
            let (left, right) = map.get(positions[i])
                .expect(&format!("position {} does not exists!", positions[i]));
            positions[i] = match instruction {
                'L' => left,
                'R' => right,
                _ => !panic!("unknown instruction {}!", instruction),
            };
        }
    }
    panic!("failed to find ..Z!")
}