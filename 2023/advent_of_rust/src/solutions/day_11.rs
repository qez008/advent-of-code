use std::cmp::{max, min};
use std::collections::{HashSet};
use std::fs::read_to_string;
use std::time::Instant;

type Point = (i32, i32);
type Universe = Vec<Vec<char>>;

pub(crate) fn solve() {
    let timer = Instant::now();
    let input = read_to_string("src/inputs/day_11_input.txt")
        .expect("failed to read input");

    let (universe, empty_columns, empty_rows) = parse(&input);
    let galaxies = universe
        .iter()
        .enumerate()
        .filter(|(_y, row)| row.contains(&'#'))
        .flat_map(|(y, row)| row
            .iter()
            .enumerate()
            .filter(|(_x, c)| c == &&'#')
            .map(|(x, _c)| (x as i32, y as i32))
            .collect::<Vec<Point>>())
        .collect::<Vec<Point>>();

    let mut sum = 0;
    for i in 0..galaxies.len() {
        for j in (i + 1)..galaxies.len() {
            sum += manhattan_distance_2(galaxies[i], galaxies[j], &empty_columns, &empty_rows);
        }
    }
    println!("sum={} (in {:?})", sum, timer.elapsed());
}

fn parse(input: &String) -> (Universe, HashSet<i32>, HashSet<i32>) {
    let universe = input
        .lines()
        .map(|s| s.chars().collect())
        .collect::<Universe>();

    let mut empty_columns: HashSet<i32> = HashSet::new();
    let mut empty_rows: HashSet<i32> = HashSet::new();

    for (y, row) in universe.iter().enumerate() {
        if !row.contains(&'#') {
            empty_rows.insert(y as i32);
        }
    }
    for x in 0..universe[0].len() {
        if (0..universe.len()).map(|y| universe[y][x]).all(|c| c == '.') {
            empty_columns.insert(x as i32);
        }
    }
    (universe, empty_columns, empty_rows)
}

fn manhattan_distance_2((x1, y1): Point, (x2, y2): Point, empty_columns: &HashSet<i32>, empty_rows: &HashSet<i32>) -> i64 {
    let x_distance = axis_distance(min(x1, x2), max(x1, x2), empty_columns);
    let y_distance = axis_distance(min(y1, y2), max(y1, y2), empty_rows);
    x_distance + y_distance
}

fn axis_distance(from: i32, to: i32, empty: &HashSet<i32>) -> i64 {
    let distance = (to - from) as i64;
    let num_empty = empty
        .iter()
        .filter(|i| (from..to).contains(i))
        .count();
    distance + (num_empty as i64 * 999999)
}