use std::fs::read_to_string;
use std::time::Instant;

pub fn solve() {
    let s = Instant::now();
    let input = read_to_string("src/inputs/day_04_input.txt")
        .expect("failed to read file");
    let data = parse_data(input);
    // part 1:
    println!("a1: {}", score(&data));
    // part 2:
    let a2 = matches(&data);
    let d = s.elapsed();
    println!("a2: {} (in {:?})", a2, d);
}

fn parse_data(input: String) -> Vec<(Vec<String>, Vec<String>)> {
    input
        .lines()
        .map(|l| {
            let data = l
                .split(|c| c == ':' || c == '|')
                .collect::<Vec<&str>>();
            let number_lists = data[1..]
                .iter()
                .map(|s| s
                    .split(' ')
                    .filter(|s| !s.is_empty())
                    .map(|s| s.to_string())
                    .collect::<Vec<String>>())
                .collect::<Vec<Vec<String>>>();
            (number_lists[0].to_vec(), number_lists[1].to_vec())
        })
        .collect::<Vec<(Vec<String>, Vec<String>)>>()
}

fn score(input: &Vec<(Vec<String>, Vec<String>)>) -> i32 {
    input
        .iter()
        .map(|(w, g)| {
            let score = w
                .iter()
                .fold(1, |acc, n| if g.contains(n) { acc * 2 } else { acc });
            score / 2
        })
        .sum::<i32>()
}

fn matches(input: &Vec<(Vec<String>, Vec<String>)>) -> i32 {
    let matches = input
        .iter()
        .map(|(w, g)| w.iter().filter(|n| g.contains(n)).count())
        .collect::<Vec<usize>>();
    let mut cards = matches
        .iter()
        .map(|_| 1)
        .collect::<Vec<i32>>();
    for i in 0..cards.len() {
        for j in 0..matches[i] {
            let new_card_id = i + 1 + j;
            cards[new_card_id] += cards[i];
        }
    }
    cards.iter().sum::<i32>()
}
