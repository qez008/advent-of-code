use std::fs::read_to_string;

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_12_input.txt")
        .expect("failed ut read input");

    let sum = input
        .lines()
        .map(|line| {
            let (a, b) = parse_input(line);
            count_arrangements(&a, &b)
        })
        .sum::<i64>();

    println!("answer={}", sum);
}

fn parse_input(line: &str) -> (Vec<char>, Vec<bool>) {
    let mut iterator = line.split_whitespace();
    let x = iterator.next().unwrap();
    let chars: Vec<char> = format!(".{}?{}?{}?{}?{}.", x, x, x, x, x)
        .chars()
        .collect();
    let mut springs: Vec<bool> = vec![false; 1];
    let y: Vec<&str> = iterator.next().unwrap().split(",").collect();
    for _ in 0..5 {
        for n in &y {
            for _ in 0..n.parse::<usize>().unwrap() {
                springs.push(true);
            }
            springs.push(false);
        }
    }
    (chars, springs)
}

fn count_arrangements(chars: &Vec<char>, springs: &Vec<bool>) -> i64 {
    let (n, m) = (chars.len(), springs.len());
    let mut dp: Vec<Vec<i64>> = vec![vec![0; m + 1]; n + 1];

    dp[n][m] = 1;

    for i in (0..n).rev() {
        for j in (0..m).rev() {
            let (damaged, operational) = match chars[i] {
                '#' => (true, false),
                '.' => (false, true),
                _ => (true, true)
            };
            dp[i][j] = if damaged && springs[j] {
                dp[i + 1][j + 1]
            } else if operational && !springs[j] {
                dp[i + 1][j + 1] + dp[i + 1][j]
            } else {
                0
            };
        }
    }
    dp[0][0]
}