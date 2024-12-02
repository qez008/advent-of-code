use std::collections::HashMap;
use std::fs::read_to_string;
use std::rc::Rc;
use std::time::Instant;
use bitvec::prelude::*;
use crate::solutions::day_14::Rocks::{AIR, ROUND, SQUARE};

#[derive(Eq, PartialEq, Hash, Copy, Clone)]
enum Rocks {
    AIR,
    ROUND,
    SQUARE,
}

type Grid = Vec<Vec<Rc<Rocks>>>;

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_14_input.txt")
        .expect("failed to read input");

    let grid = input
        .lines()
        .map(|s| s.chars().map(|c|
            match c {
                '.' => Rc::new(AIR),
                'O' => Rc::new(ROUND),
                _ => Rc::new(SQUARE)
            }).collect())
        .collect::<Grid>();
    let (n, m) = (grid.len(), grid[0].len());

    let a1 = part_1(&grid, n, m);
    println!("{}", a1);

    let instant = Instant::now();
    let a2 = part_2(&grid, n, m);
    println!("{} (in {:?})", a2, instant.elapsed());
}

fn part_1(grid: &Grid, n: usize, m: usize) -> i64 {
    let mut grid_copy = grid.clone();

    let mut movement = true;
    while movement {
        movement = false;
        for j in 0..m {
            for i in 1..n {
                if *grid_copy[i][j] == ROUND && *grid_copy[i - 1][j] == AIR {
                    grid_copy[i][j] = Rc::new(AIR);
                    grid_copy[i - 1][j] = Rc::new(ROUND);
                    movement = true
                }
            }
        }
    }
    for row in &grid_copy {
        println!("{}", row.iter()
            .map(|rock| match **rock {
                ROUND => 'O',
                SQUARE => '#',
                AIR => '.'
            })
            .collect::<String>())
    }
    calculate_load(&grid_copy)
}

fn calculate_load(grid: &Grid) -> i64 {
    grid.iter()
        .rev()
        .enumerate()
        .map(|(i, row)| {
            let round_rocks = row.iter().filter(|c| ***c == ROUND).count() as i64;
            (i as i64 + 1) * round_rocks
        })
        .sum::<i64>()
}

fn part_2(grid: &Grid, n: usize, m: usize) -> i64 {
    let mut grid_copy: Grid = grid.clone();
    let mut solution_map: HashMap<BitVec, (i64, usize)> = HashMap::new();
    let mut cycles: Vec<(usize, i64)> = Vec::new();

    for i in 0..1000 {
        slide(&mut grid_copy, n, m);
        let total_load = calculate_load(&grid_copy);

        let bit_rep = to_bit(&grid_copy, n, m);
        if solution_map.contains_key(&bit_rep) {
            let (load, iter) = solution_map.get(&bit_rep).unwrap();
            if cycles.contains(&(*iter, *load)) {
                // got a loop...
                //println!("{:?}", cycles);
                let remaining_iterations = 1000000000 - i - 1;
                let (j, l) = cycles[remaining_iterations % cycles.len()];
                //println!("{} {}", j, l);
                return l;
            } else {
                cycles.push((*iter, *load));
                //println!("at iteration {} hit previous grid from iteration {} with load {}", i, iter, load);
            }
        } else {
            solution_map.insert(bit_rep, (total_load, i));
        }
    }
    -1
}

fn slide(grid_copy: &mut Grid, n: usize, m: usize) {
    let mut movement = true;
    // north
    while movement {
        movement = false;
        for j in 0..m {
            for i in 1..n {
                if *grid_copy[i][j] == ROUND && *grid_copy[i - 1][j] == AIR {
                    grid_copy[i][j] = Rc::new(AIR);
                    grid_copy[i - 1][j] = Rc::new(ROUND);
                    movement = true
                }
            }
        }
    }
    movement = true;
    // west
    while movement {
        movement = false;
        for i in 0..n {
            for j in 1..m {
                if *grid_copy[i][j] == ROUND && *grid_copy[i][j - 1] == AIR {
                    grid_copy[i][j] = Rc::new(AIR);
                    grid_copy[i][j - 1] = Rc::new(ROUND);
                    movement = true
                }
            }
        }
    }
    movement = true;
    // south
    while movement {
        movement = false;
        for j in 0..m {
            for i in (0..(n - 1)).rev() {
                if *grid_copy[i][j] == ROUND && *grid_copy[i + 1][j] == AIR {
                    grid_copy[i][j] = Rc::new(AIR);
                    grid_copy[i + 1][j] = Rc::new(ROUND);
                    movement = true
                }
            }
        }
    }
    movement = true;
    // east
    while movement {
        movement = false;
        for i in 0..n {
            for j in (0..(m - 1)).rev() {
                if *grid_copy[i][j] == ROUND && *grid_copy[i][j + 1] == AIR {
                    grid_copy[i][j] = Rc::new(AIR);
                    grid_copy[i][j + 1] = Rc::new(ROUND);
                    movement = true
                }
            }
        }
    }
}

fn hash(state: &Grid, n: usize, m: usize) -> u32 {
    let mut int = 0;
    for i in 0..n {
        for j in 0..m {
            int = int << 1;
            if *state[i][j] == ROUND {
                int += 1
            }
            int %= 4567
        }
    }
    int
}

fn to_bit(state: &Grid, n: usize, m: usize) -> BitVec {
    let mut bits = bitvec![0; n * m];
    for i in 0..n {
        for j in 0..m {
            bits.set(i * n + j, *state[i][j] == ROUND)
        }
    }
    bits
}