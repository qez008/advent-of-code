use std::cmp::min;
use std::fs::read_to_string;
use pathfinding::prelude::dijkstra;
use crate::util::direction::Direction;

#[derive(Clone, Debug, Eq, Hash, Ord, PartialEq, PartialOrd)]
struct State(usize, usize, i64, Direction);


fn successors(&State(x, y, count, dir): &State, grid: &Vec<Vec<usize>>, min: i64, max: i64) -> Vec<(State, usize)> {
    let mut results = vec![];
    for next_dir in [dir, dir.turn_left(), dir.turn_right()] {
        if next_dir == dir && count == max || next_dir != dir && count < min {
            continue;
        }
        let Ok((ny, nx)) = next_dir
            .move_with_bounds(&(y, x), &(grid.len(), grid[0].len()))
            else {
                continue;
            };
        let next_count = if next_dir == dir { count + 1 } else { 1 };
        let heat_loss = grid[ny][nx];
        results.push((State(nx, ny, next_count, next_dir), heat_loss));
    }
    results
}

fn success(&State(x, y, count, _): &State, grid: &Vec<Vec<usize>>) -> bool {
    x == grid[0].len() - 1 && y == grid.len() - 1 //&& count <= 4
}


pub fn solve() {
    let input = read_to_string("src/inputs/day_17_sample.txt")
        .expect("!!");

    let map = input
        .lines()
        .collect::<Vec<_>>()
        .iter()
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .map(|line| {
            line.chars()
                .map(|x| x.to_string().parse::<usize>().unwrap())
                .collect::<Vec<usize>>()
        })
        .collect::<Vec<Vec<usize>>>();

    let (_, min_heat_loss_east) = dijkstra(
        &State(0, 0, 0, Direction::East),
        |p| successors(p, &map, 4, 10),
        |p| success(p, &map))
        .unwrap();
    let (_, min_heat_loss_south) = dijkstra(
        &State(0, 0, 0, Direction::South),
        |p| successors(p, &map, 4, 10),
        |p| success(p, &map))
        .unwrap();
    println!("{}", min(min_heat_loss_east, min_heat_loss_south));
}

