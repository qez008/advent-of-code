use std::collections::HashMap;
use std::fmt::{Display, Formatter};
use std::fs::read_to_string;
use std::ops::Add;
use std::time::Instant;
use crate::solutions::day_10::Dir::*;

use strum::IntoEnumIterator;
use strum_macros::{Display, EnumIter};

type Grid = Vec<Vec<char>>;

#[derive(EnumIter, Clone, Eq, Hash, PartialEq, Display)]
enum Dir {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

#[derive(Clone)]
struct Pos {
    x: i32,
    y: i32,
}

impl Pos {
    fn within(&self, upper_bound: &Pos) -> bool {
        self.x >= 0
            && self.x < upper_bound.x
            && self.y >= 0
            && self.y < upper_bound.y
    }
}

impl Display for Pos {
    fn fmt(&self, f: &mut Formatter) -> std::fmt::Result {
        write!(f, "({}, {})", self.x, self.y)
    }
}

impl Add for Pos {
    type Output = Pos;
    fn add(self, other: Pos) -> Pos {
        Pos {
            x: self.x + other.x,
            y: self.y + other.y,
        }
    }
}

struct Pipe {
    in_dir: Dir,
    out_dir: Dir,
    symbol: char,
    position: (i32, i32),
}

impl Display for Pipe {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}: ({:?} {} {})", self.symbol, self.position, self.in_dir, self.in_dir)
    }
}

pub(crate) fn solve() {
    let start = Instant::now();
    let input = read_to_string("src/inputs/day_10_input.txt")
        .expect("failed to read input file");

    let grid: Grid = input
        .lines()
        .map(|line| line.chars().collect::<Vec<char>>())
        .collect();

    // pipes with incoming direction mapped to outgoing direction
    let pipes: HashMap<(char, Dir), Dir> = HashMap::from([
        (('L', DOWN), RIGHT),
        (('L', LEFT), UP),
        (('7', RIGHT), DOWN),
        (('7', UP), LEFT),
        (('|', DOWN), DOWN),
        (('|', UP), UP),
        (('-', LEFT), LEFT),
        (('-', RIGHT), RIGHT),
        (('J', DOWN), LEFT),
        (('J', RIGHT), UP),
        (('F', UP), RIGHT),
        (('F', LEFT), DOWN),
    ]);

    let pipe_loop = &part_1(&grid, &pipes);
    println!("a1={}", pipe_loop.len() / 2);
    println!("a2={}", part_2(pipe_loop, &grid));
    println!("finished in {:?}", start.elapsed());
}

fn part_1(grid: &Grid, pipes: &HashMap<(char, Dir), Dir>) -> Vec<(i32, i32)> {
    let grid_size = Pos { x: grid[0].len() as i32, y: grid.len() as i32 };
    let start = find_start(grid);
    for direction in Dir::iter() {
        let starting_pipe = Pipe {
            in_dir: direction.clone(),
            out_dir: direction.clone(),
            position: (start.x, start.y),
            symbol: 'S',
        };
        let result = explore(starting_pipe, grid, &grid_size, pipes);
        if result.is_some() {
            return result.unwrap();
        }
    }
    vec![]
}

fn find_start(grid: &Grid) -> Pos {
    grid.iter()
        .enumerate()
        .map(|(y, row)| (row.iter().position(|c| c == &'S'), y))
        .filter(|(x, _)| x.is_some())
        .map(|(x, y)| Pos { x: x.unwrap() as i32, y: y as i32 })
        .next()
        .unwrap()
}

fn explore(pipe: Pipe, grid: &Grid, pipes: &HashMap<(char, Dir), Dir>) -> Option<Vec<(i32, i32)>> {
    let mut p = pipe;
    let mut trail = vec![p.position.clone()];
    loop {
        let next_p = traverse(&p, grid, pipes);
        if next_p.is_none() {
            return None;
        }
        p = next_p.unwrap();
        trail.push(p.position.clone());
        if p.symbol == 'S' {
            return Some(trail);
        }
    }
}

fn traverse(pipe: &Pipe, grid: &Grid, pipes: &HashMap<(char, Dir), Dir>) -> Option<Pipe> {
    let next_position = match pipe.out_dir {
        UP => (pipe.position.0, pipe.position.1 - 1),
        DOWN => (pipe.position.0, pipe.position.1 + 1),
        LEFT => (pipe.position.0 - 1, pipe.position.1),
        RIGHT => (pipe.position.0 + 1, pipe.position.1)
    };
    if next_position.0 < 0 || next_position.0 >= grid[0].len() as i32
        || next_position.1 < 0 || next_position.1 >= grid.len() as i32 {
        return None;
    }
    match grid[next_position.1 as usize][next_position.0 as usize] {
        'S' => {
            let final_pipe = Pipe {
                in_dir: pipe.in_dir.clone(),
                out_dir: pipe.out_dir.clone(),
                position: next_position,
                symbol: 'S',
            };
            Some(final_pipe)
        }
        '.' => {
            None
        }
        next_symbol => {
            let connection = pipes.get(&(next_symbol, pipe.out_dir.clone()));
            if connection.is_none() {
                return None;
            }
            let next_pipe = Pipe {
                in_dir: pipe.out_dir.clone(),
                out_dir: connection.unwrap().clone(),
                position: next_position,
                symbol: next_symbol,
            };
            Some(next_pipe)
        }
    }
}

fn part_2(pipe_loop: &Vec<(i32, i32)>, grid: &Grid) -> i32 {
    let mut grid_copy = grid.clone();
    for (x, y) in pipe_loop {
        grid_copy[*y as usize][*x as usize] = '*';
    }
    count_inner_2(grid, &mut grid_copy)
}

fn count_inner_2(grid: &Grid, modified_grid: &mut Vec<Vec<char>>) -> i32 {
    let mut sum = 0;
    for (y, row) in modified_grid.clone().iter().enumerate() {
        let mut inside = false;
        let mut entry_symbol: Option<char> = None;
        for (x, symbol) in row.iter().enumerate() {
            if symbol == &'*' {
                match grid[y][x] {
                    '|' => {
                        inside = !inside;
                    }
                    'F' => {
                        if entry_symbol.is_some() {
                            entry_symbol = None;
                        } else {
                            entry_symbol = Some('F')
                        }
                    }
                    'J' => {
                        if entry_symbol.is_some() {
                            if entry_symbol.unwrap() == 'F' {
                                inside = !inside;
                            }
                            entry_symbol = None;
                        } else {
                            entry_symbol = Some('J')
                        }
                    }
                    '7' => {
                        if entry_symbol.is_some() {
                            if entry_symbol.unwrap() == 'L' {
                                inside = !inside;
                            }
                            entry_symbol = None;
                        } else {
                            entry_symbol = Some('7')
                        }
                    }
                    'L' => {
                        if entry_symbol.is_some() {
                            entry_symbol = None;
                        } else {
                            entry_symbol = Some('L')
                        }
                    }
                    _ => {}
                };
            } else if inside {
                sum += 1;
                modified_grid[y][x] = 'I';
            } else {
                modified_grid[y][x] = '0';
            }
        }
    }
    sum
}
