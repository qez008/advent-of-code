use std::cmp::{max, min};
use std::collections::{HashSet};
use std::fs::read_to_string;
use std::num::NonZeroU64;
use pathfinding::num_traits::Num;
use crate::util::direction::Direction;

type Input<T> = Vec<(char, T, String)>;
type Point = (i32, i32);

pub(crate) fn solve() {
    let input_str = read_to_string("src/inputs/day_18_input.txt")
        .expect("failed to read input");

    let input: Input<u8> = input_str
        .lines()
        .map(|line| {
            let mut iter = line.split_whitespace();
            let dir = iter.next()
                .unwrap()
                .chars()
                .next()
                .unwrap();
            let n = iter.next()
                .unwrap()
                .parse::<u8>()
                .unwrap();
            let color = iter.next()
                .unwrap()
                .trim_matches(|c| c == '(' || c == ')')
                .to_string();
            (dir, n, color)
        })
        .collect();

    for line in &input {
        println!("{:?}", line);
    }
    println!();

    //part_one(&input);
    part_two(&input);
    println!("{:?}", _solve(input_str.as_str(), map_part2));
}

fn part_two(input: &Input<u8>) {
    let adjusted_input: Input<u64> = input.iter()
        .map(|(_, _, color)| {
            let lrud = color.chars().nth(6).unwrap();
            let n = u64 ::from_str_radix(&color[1..6], 16).unwrap();
            (lrud, n, color.clone())
        })
        .collect::<Input<u64>>();

    for line in &adjusted_input {
        println!("{:?}", line);
    }
    println!();

    let mut vertices: HashSet<((i64, i64), u64)> = HashSet::new();
    let mut v: (i64, i64) = (0, 0);
    for (lrup, n, _) in &adjusted_input {
        let u = match lrup {
            '0' => (v.0.wrapping_add_unsigned(*n), v.1),
            '1' => (v.0, v.1.wrapping_add_unsigned(*n)),
            '2' => (v.0.wrapping_sub_unsigned(*n), v.1),
            '3' => (v.0, v.1.wrapping_sub_unsigned(*n)),
            _ => unreachable!()
        };
        vertices.insert((u, *n));
        v = u;
    }

    let xs: Vec<_> = vertices.into_iter().collect();

    println!("{:?}", shoelace_formula(xs.as_slice()));

}

fn map_part2(l: &str) -> (u8, u64) {
    let (_, b) = l.split_once('#').unwrap();
    let hex = u32::from_str_radix(&b[..6], 16).unwrap();
    ((hex & 3) as u8, (hex >> 4) as u64)
}

const fn pt_move(pt: (i64, i64), dir: u8, dist: u64) -> (i64, i64) {
    match dir {
        0 => (pt.0.wrapping_add_unsigned(dist), pt.1),
        1 => (pt.0, pt.1.wrapping_add_unsigned(dist)),
        2 => (pt.0.wrapping_sub_unsigned(dist), pt.1),
        3 => (pt.0, pt.1.wrapping_sub_unsigned(dist)),
        _ => unreachable!()
    }
}


fn _solve(input: &str, mapper: fn(&str) -> (u8, u64)) -> Option<NonZeroU64> {
    let mut spos = (0, 0);
    let (mut perim, mut sum) = (0, 0);
    for (dir, num) in input.lines().map(mapper) {
        let npos = pt_move(spos, dir, num);
        sum += (spos.1 + npos.1) * (spos.0 - npos.0);
        perim += num;
        spos = npos;
    }
    NonZeroU64::new(perim.wrapping_add_signed(sum) / 2 + 1)
}

fn shoelace_formula(points: &[((i64, i64), u64)]) -> Option<NonZeroU64> {
    let mut spos = (0, 0);
    let (mut perim, mut sum) = (0, 0);
    for (npos, num) in points {
        sum += (spos.1 + npos.1) * (spos.0 - npos.0);
        perim += num;
        spos = *npos;
    }
    NonZeroU64::new(perim.wrapping_add_signed(sum) / 2 + 1)
}

fn part_one(input: &Input<u8>) {
    let mut map: HashSet<Point> = HashSet::new();
    let mut point: Point = (0, 0);

    for (lrup, n, _) in input {
        let dir = Direction::from_char(*lrup);
        let next_point = dir.move_n(point, *n as i32);
        match dir {
            Direction::North => {
                for i in next_point.0..=point.0 {
                    map.insert((i, next_point.1));
                }
            }
            Direction::South => {
                for i in point.0..=next_point.0 {
                    map.insert((i, next_point.1));
                }
            }
            Direction::West => {
                for j in next_point.1..=point.1 {
                    map.insert((next_point.0, j));
                }
            }
            Direction::East => {
                for j in point.1..=next_point.1 {
                    map.insert((next_point.0, j));
                }
            }
        }
        point = next_point;
    }
    //println!("{:?}", &map);
    let (mut lower_i, mut upper_i, mut lower_j, mut upper_j) = (0, 0, 0, 0);
    for (i, j) in &map {
        lower_i = min(*i, lower_i);
        upper_i = max(*i, upper_i);
        lower_j = min(*j, lower_j);
        upper_j = max(*j, upper_j);
    }
    println!("(i j) lower/upper=({} {})/({} {})", lower_i, lower_j, upper_j, upper_j);

    let (n, m) = ((upper_i - lower_i + 1) as usize, (upper_j - lower_j + 1) as usize);
    let mut grid = vec![vec!['.'; m]; n];

    for (i, j) in &map {
        //println!("{} {}", i - lower_i, j - lower_j);
        grid[(i - lower_i) as usize][(j - lower_j) as usize] = '#';
    }
    //
    // for row in &grid {
    //     println!("{}", row.iter().collect::<String>());
    // }
    // println!();

    let mut grid_copy = grid.clone();
    let mut inside_tiles = 0;
    let mut queue = vec![(4usize, 235usize)];
    while !queue.is_empty() {
        let (i, j) = queue.pop().unwrap();
        if grid_copy[i][j] == '.' {
            inside_tiles += 1;
            grid_copy[i][j] = 'X';
            if i + 1 < n {
                queue.push((i + 1, j));
            }
            if i >= 1 {
                queue.push((i - 1, j));
            }
            if j + 1 < m {
                queue.push((i, j + 1));
            }
            if j >= 1 {
                queue.push((i, j - 1));
            }
        }
    }

    for row in &grid_copy {
        println!("{}", row.iter().collect::<String>());
    }
    println!();
    println!("{} (inside) + {} (path) = {}", inside_tiles, map.len(), inside_tiles + map.len());
}
