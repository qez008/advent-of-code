use std::collections::HashMap;
use std::fmt::{Display, Formatter};
use std::fs::read_to_string;
use crate::util::matrix::Matrix;
use sorted_list::SortedList;
use crate::util::direction::Direction;


pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_17_input.txt")
        .expect("failed to read input");
    let matrix = parse_input(input);
    println!("{}", &matrix);
    println!("min heat loss={}", part_one(&matrix));
}

#[derive(Eq, PartialEq, Hash, Copy, Clone, Debug)]
struct State {
    pos: (usize, usize),
    dir: Direction,
    steps_in_dir: u8,
    heat_loss: u64,
}

impl Display for State {
    fn fmt(&self, f: &mut Formatter) -> std::fmt::Result {
        write!(f, "({} {}) ({:?} {}) {}",
               self.pos.0, self.pos.1, self.dir, self.steps_in_dir, self.heat_loss)
    }
}


fn part_one(hl_matrix: &Matrix<MatrixType>) -> u64 {
    let mut states: SortedList<u64, State> = SortedList::new();

    let mut vs = vec![vec![' '; hl_matrix.m]; hl_matrix.n];

    let start = State {
        pos: (0usize, 0usize),
        dir: Direction::South,
        steps_in_dir: 0,
        heat_loss: 1u64,
    };
    states.insert(start.heat_loss, start);

    let finish = (hl_matrix.n - 1, hl_matrix.m - 1);
    let mut min_heat_loss = u64::MAX;
    let mut found_finish = false;

    let mut visited: HashMap<((usize, usize), Direction, u8), u64> = HashMap::new();

    while states.len() != 0 {
        let mut next_states: SortedList<u64, State> = SortedList::new();

        for (_, state) in states {
            if state.heat_loss > min_heat_loss {
                continue;
            }
            vs[state.pos.0][state.pos.1] = state.dir.to_string().chars().next().unwrap();
            let v = visited.get(&(state.pos, state.dir, state.steps_in_dir));
            if v.is_some() && v.unwrap() < &state.heat_loss {
                // already visited a better..
                continue;
            }
            visited.insert((state.pos, state.dir, state.steps_in_dir), state.heat_loss);
            //println!("{:?}", visited);

            if state.pos == finish  {
                found_finish = true;
                min_heat_loss = state.heat_loss;
                // println!("{} {}", min_heat_loss, state.steps_in_dir);
                // for row in &vs {
                //     println!("{}", row.iter().collect::<String>());
                // }
                // println!();
                continue;
            }

            let bounds = (hl_matrix.n, hl_matrix.m);

            for (dir, steps_in_dir) in [
                (state.dir, state.steps_in_dir + 1),
                (state.dir.turn_left(), 0),
                (state.dir.turn_right(), 0)] {
                if steps_in_dir < 10 && (state.dir == dir || state.steps_in_dir >= 4){
                    let next_pos = dir.move_with_bounds(&state.pos, &bounds);
                    if next_pos.is_ok() {
                        let heat_loss = state.heat_loss + hl_matrix[next_pos.unwrap()];
                        let next_state = State {
                            pos: next_pos.unwrap(),
                            dir,
                            steps_in_dir,
                            heat_loss,
                        };
                        next_states.insert(heat_loss, next_state);
                    }
                }
            }
        }
        states = next_states;
    }

    if found_finish { min_heat_loss } else { 0 }
}


type MatrixType = u64;

fn parse_input(input: String) -> Matrix<MatrixType> {
    let lines = input
        .lines()
        .collect::<Vec<&str>>();
    Matrix {
        n: lines.len(),
        m: lines[0].len(),
        data: lines
            .iter()
            .map(|line| line
                .chars()
                .map(|c| c.to_digit(10u32).unwrap() as MatrixType)
                .collect::<Vec<MatrixType>>())
            .collect::<Vec<Vec<MatrixType>>>(),
    }
}


