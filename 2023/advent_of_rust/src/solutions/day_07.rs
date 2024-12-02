use std::cmp::{max, Ordering};
use std::fs::read_to_string;

// Camel Cards (https://adventofcode.com/2023/day/7)

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_07_input.txt")
        .expect("failed to read file");

    println!("a1={}", solve_parts(&input, false));
    println!("a2={}", solve_parts(&input, true));
}

fn solve_parts(input: &String, with_joker: bool) -> i32 {
    let mut hands = input
        .lines()
        .map(|line| {
            let mut split_line = line.split_whitespace();
            let hand = split_line.next().unwrap();
            let bid = split_line.next().unwrap().parse::<i32>().unwrap();
            let alpha_hand = map_labels(hand, with_joker);
            let tier = hand_tier(&alpha_hand, with_joker);
            (alpha_hand, tier, bid)
        })
        .collect::<Vec<(String, i32, i32)>>();

    hands.sort_by(|a, b| compare_hands(a, b));
    hands.iter()
        .enumerate()
        .fold(0, |acc, (i, (_, _, bid))| acc + bid * (i as i32))
}

fn map_labels(hand: &str, with_joker: bool) -> String {
    let labels = if with_joker { "J23456789TQKA" } else { "23456789TJQKA" };
    let alphab = "ABCDEFGHIJKLM";
    hand.chars()
        .map(|label| alphab
            .chars()
            .nth(labels
                .find(label)
                .expect(&format!("could not find label {}", label)))
            .unwrap())
        .collect::<String>()
}

fn hand_tier(hand: &String, with_joker: bool) -> i32 {
    let mut label_count: Vec<i32> = vec![0; 13];
    for label in hand.chars() {
        let index = (label as u32) - 65;
        label_count[index as usize] += 1
    }
    let kind_count = highest_label_count(&label_count, with_joker);
    return
        if kind_count == 5 {
            7
        } else if kind_count == 4 {
            6
        } else if full_house(&label_count, with_joker) {
            5
        } else if kind_count == 3 {
            4
        } else if two_pairs(&label_count, with_joker) {
            3
        } else if kind_count == 2 {
            2
        } else {
            1
        };
}

fn highest_label_count(count: &Vec<i32>, with_joker: bool) -> i32 {
    if with_joker {
        count[1..].iter().fold(count[0], |acc, c| max(acc, c + count[0]))
    } else {
        *count.iter().reduce(|acc, c| max(acc, c)).unwrap()
    }
}

fn full_house(hand: &Vec<i32>, with_joker: bool) -> bool {
    if with_joker {
        let jokers = hand[0];
        let twos_count = hand[1..].iter().filter(|c| c == &&2).count();

        hand.contains(&3) && hand.contains(&2)
            || hand.contains(&3) && jokers >= 1
            || twos_count >= 2 && jokers >= 1
            || twos_count == 1 && jokers >= 2
    } else {
        hand.contains(&3) && hand.contains(&2)
    }
}

fn two_pairs(hand: &Vec<i32>, with_jokers: bool) -> bool {
    if with_jokers {
        let pair_count = hand[1..].iter().filter(|c| c == &&2).count();
        pair_count == 2 || (pair_count == 1 && hand[0] >= 1)
    } else {
        hand.iter().filter(|c| c == &&2).count() >= 2
    }
}

fn compare_hands((hand_a, tier_a, _): &(String, i32, i32),
                 (hand_b, tier_b, _): &(String, i32, i32)) -> Ordering {
    tier_a.cmp(&tier_b).then(hand_a.cmp(&hand_b))
}
