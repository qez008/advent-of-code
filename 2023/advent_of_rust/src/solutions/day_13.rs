use std::cmp::min;
use std::fs::read_to_string;
use strum_macros::Display;

#[derive(Display)]
enum Reflection {
    Horizontal,
    Vertical,
    Unknown,
}

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_13_input.txt")
        .expect("failed to read input");
    let lines = input
        .lines()
        .map(|s| s.chars().collect())
        .collect::<Vec<Vec<char>>>();
    let patterns: Vec<_> = lines
        .split(|s| s.is_empty())
        .collect();

    let sum = patterns.iter()
        .map(|pattern| {
            let pattern_t = (0..pattern[0].len())
                .map(|y| (0..pattern.len()).map(|x| pattern[x][y]).collect::<Vec<char>>())
                .collect::<Vec<Vec<char>>>();
            let v_reflection = find_reflection(pattern);
            if v_reflection.is_some() {

                v_reflection.unwrap() * 100
            } else {
                find_reflection(&pattern_t).unwrap_or(0)
            }
        })
        .sum::<usize>();
    println!("answer={}", sum);
}

fn find_reflection(pattern: &[Vec<char>]) -> Option<usize> {
    (1..pattern.len()).find(|i| is_reflection(pattern, *i))
}

fn is_reflection(pattern: &[Vec<char>], split: usize) -> bool {
    let mut smudge = false;
    let (left, right) = pattern.split_at(split);
    let n = min(left.len(), right.len());
    for i in 0..n {
        let j = left.len() - 1 - i;
        let diff = &left[j]
            .iter()
            .enumerate()
            .filter(|(k, v)| v != &&right[i][*k])
            .count();
        if !smudge && diff == 1 {
            smudge = true;
        } else if diff > 0 {
            return false;
        }
    }
    smudge
}
