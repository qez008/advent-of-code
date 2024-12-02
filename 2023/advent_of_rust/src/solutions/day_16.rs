use std::cmp::max;
use std::collections::{HashMap, HashSet};
use std::f32::consts::E;
use std::fmt;
use std::fmt::{Display, Formatter};
use std::fs::read_to_string;
use std::time::Instant;
use crate::solutions::day_16::Cell::{EmptySpace, Mirror, Split};
use crate::solutions::day_16::Direction::*;

enum Cell {
    EmptySpace,
    Mirror(char),
    Split(char),
}

impl Display for Cell {
    fn fmt(&self, f: &mut Formatter) -> fmt::Result {
        match self {
            EmptySpace => write!(f, "."),
            Mirror(c) | Split(c) => write!(f, "{}", c),
        }
    }
}

#[derive(Debug, Eq, PartialEq, Hash, Copy, Clone)]
enum Direction {
    NORTH = 0,
    WEST = 1,
    SOUTH = 2,
    EAST = 3,
}

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_16_input.txt")
        .expect("failed to read input");

    let cell_matrix: Matrix<Cell> = parse_input(input);
    println!("{}", &cell_matrix);
    println!("{:?}", part_1(&cell_matrix, ((0, 0), EAST)));
    let inst = Instant::now();
    println!("{:?} (in {:?})", part_2(&cell_matrix), inst.elapsed());
}

fn parse_input(input: String) -> Matrix<Cell> {
    let lines: Vec<Vec<char>> = input
        .lines()
        .map(|line| line.chars().collect())
        .collect();
    Matrix {
        n: lines.len(),
        m: lines[0].len(),
        data: lines
            .iter()
            .map(|l| l
                .iter()
                .map(|x| match x {
                    '-' | '|' => Split(*x),
                    '/' | '\\' => Mirror(*x),
                    _ => EmptySpace
                })
                .collect::<Vec<Cell>>())
            .collect::<Vec<Vec<Cell>>>(),
    }
}

fn part_1(cell_matrix: &Matrix<Cell>, start: ((i32, i32), Direction)) -> usize {
    let mut energized_cells: Matrix<bool> = Matrix {
        n: cell_matrix.n,
        m: cell_matrix.m,
        data: vec![vec![false; cell_matrix.m]; cell_matrix.n],
    };

    let mut visited = vec![vec![vec![false; 4]; cell_matrix.m]; cell_matrix.n];

    //let mut memo: HashSet<((i32, i32), Direction)> = HashSet::new();

    let mut positions = vec![start];
    while !positions.is_empty() {
        for i in (0..positions.len()).rev() {
            let (pos, dir) = positions.remove(i);

            if //memo.contains(&(pos, dir)) ||
            pos.0 < 0 || pos.0 >= cell_matrix.n as i32
                || pos.1 < 0 || pos.1 >= cell_matrix.m as i32
                || visited[pos.0 as usize][pos.1 as usize][dir as usize] {
                continue;
            }

            energized_cells[(pos.0 as usize, pos.1 as usize)] = true;

            match cell_matrix[(pos.0 as usize, pos.1 as usize)] {
                EmptySpace => {
                    // match dir {
                    //     NORTH | SOUTH => {
                    //         memo.insert((pos, NORTH));
                    //         memo.insert((pos, SOUTH));
                    //     }
                    //     EAST | WEST => {
                    //         memo.insert((pos, EAST));
                    //         memo.insert((pos, WEST));
                    //     }
                    // }
                    //memo.insert((pos, dir));
                    visited[pos.0 as usize][pos.1 as usize][dir as usize] = true;
                    positions.push((move_in_dir(&dir, pos), dir));
                }
                Split(symbol) => {
                    match dir {
                        NORTH | SOUTH => {
                            //memo.insert((pos, dir));
                            visited[pos.0 as usize][pos.1 as usize][dir as usize] = true;
                            //memo.insert((pos, SOUTH));
                            if symbol == '|' {
                                positions.push((move_in_dir(&dir, pos), dir));
                            } else {
                                positions.push((move_in_dir(&WEST, pos), WEST));
                                positions.push((move_in_dir(&EAST, pos), EAST));
                            }
                        }
                        EAST | WEST => {
                            //memo.insert((pos, dir));
                            visited[pos.0 as usize][pos.1 as usize][dir as usize] = true;
                            //memo.insert((pos, WEST));
                            if symbol == '-' {
                                positions.push((move_in_dir(&dir, pos), dir));
                            } else {
                                positions.push((move_in_dir(&NORTH, pos), NORTH));
                                positions.push((move_in_dir(&SOUTH, pos), SOUTH));
                            }
                        }
                    }
                }
                Mirror(symbol) => {
                    let new_dir = match dir {
                        NORTH => if symbol == '/' { EAST } else { WEST },
                        SOUTH => if symbol == '/' { WEST } else { EAST },
                        WEST => if symbol == '/' { SOUTH } else { NORTH },
                        EAST => if symbol == '/' { NORTH } else { SOUTH },
                    };
                    //memo.insert((pos, dir));
                    visited[pos.0 as usize][pos.1 as usize][dir] = true;
                    positions.push((move_in_dir(&new_dir, pos), new_dir));
                }
            };
        }
    }
    energized_cells.data
        .iter()
        .map(|bools| bools.iter().filter(|b| **b).count())
        .sum::<usize>()
}

fn move_in_dir(dir: &Direction, pos: (i32, i32)) -> (i32, i32) {
    match *dir {
        NORTH => (pos.0 - 1, pos.1),
        SOUTH => (pos.0 + 1, pos.1),
        WEST => (pos.0, pos.1 - 1),
        EAST => (pos.0, pos.1 + 1),
    }
}

fn part_2(matrix: &Matrix<Cell>) -> usize {
    let mut max_energy = 0;
    for i in 0..matrix.n {
        max_energy = max(max_energy, part_1(matrix, ((i as i32, 0), EAST)));
        max_energy = max(max_energy, part_1(matrix, ((i as i32, matrix.n as i32 - 1), WEST)));
    }
    for j in 0..matrix.m {
        max_energy = max(max_energy, part_1(matrix, ((0, j as i32), SOUTH)));
        max_energy = max(max_energy, part_1(matrix, ((matrix.m as i32 - 1, j as i32), NORTH)));
    }
    max_energy
}

