use std::fs::read_to_string;

use std::{collections::{VecDeque, HashSet}};

use pathfinding::prelude::Matrix;


type Garden = Vec<Vec<char>>;
type Position = (usize, usize);

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_21_input.txt")
        .expect("failed to read input");

    let garden = input.lines()
        .map(|l| l.chars().collect())
        .collect::<Garden>();

    for row in &garden {
        println!("{}", row.iter().collect::<String>());
    }

    let start: Position = find_start(&garden);
    walk(64, start, &garden);

    println!("Part 2 result: {}", part(input, 26501365));
}

fn walk(steps: usize, start: Position, gar: &Garden) {
    let mut queue: HashSet<Position> = HashSet::from([start]);

    for i in 0..steps {
        queue = queue.iter()
            .flat_map(|p| options(p, gar))
            .collect::<HashSet<Position>>();
    }
    println!("{}", queue.len());
}

fn find_start(garden: &Garden) -> Position {
    let start_i = garden.iter().position(|row| row.contains(&'S')).unwrap();
    let start_j = garden[start_i].iter().position(|c| c == &'S').unwrap();
    (start_j, start_j)
}

fn options((i, j): &Position, gar: &Garden) -> Vec<Position> {
    [(i + 1, *j), (i - 1, *j), (*i, j + 1), (*i, j - 1)]
        .iter()
        .filter(|(i, j)| gar[*i][*j] != '#')
        .map(Clone::clone)
        .collect::<Vec<Position>>()
}

fn part(input: String, goal: usize) -> usize {
    let grid = Matrix::from_rows(input.lines().map(str::bytes)).unwrap();
    let (sr, sc) = grid
        .items()
        .find_map(|(pos, b)| (*b == b'S').then_some(pos))
        .unwrap();

    let (rows, mut ys, mut reachable) = (grid.rows, vec![], HashSet::new());
    reachable.insert((sr as isize, sc as isize));

    for count in 1..=goal {
        for (r, c) in reachable.drain().collect::<Vec<_>>() {
            reachable.extend(
                [(r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)]
                    .iter()
                    .filter(|&&(nr, nc)| grid[grid.constrain((nr, nc))] != b'#'),
            );
        }
        if count % rows == rows / 2 {
            ys.push(reachable.len());
            if let &[y0, y1, y2] = &ys[..] {
                let x = goal / rows;
                return (x * x * (y0 + y2 - 2 * y1) + x * (4 * y1 - 3 * y0 - y2) + 2 * y0) / 2;
            }
        }
    }
    reachable.len()
}

