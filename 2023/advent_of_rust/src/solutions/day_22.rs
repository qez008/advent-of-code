use std::cmp::{max};
use std::fs::read_to_string;
use std::ops::Range;

pub(crate) fn solve() {
    let tower: Tower = parse_input(false);
    print_tower(&tower);
    println!();
    println!("/////////\n");

    let settled_tower: Tower = settle(&tower);
    print_tower(&settled_tower);

    println!();

    let settled_tower: Tower = settle_2(&tower);
    print_tower(&settled_tower);
}

fn parse_input(sample: bool) -> Tower {
    let path = if sample {
        "src/inputs/day_22_sample.txt"
    } else {
        "src/inputs/day_22_input.txt"
    };
    let input = read_to_string(path).unwrap();
    let mut shapes = input
        .lines()
        .enumerate()
        .map(Shape::from_str)
        .collect::<Vec<Shape>>();

    shapes.sort_by(|a, b| a.bottom().cmp(&b.bottom()));
    shapes
}

type Vec3 = (usize, usize, usize);
type ShapeBounds = (Range<usize>, Range<usize>);
type Tower = Vec<Shape>;

#[derive(Debug, Eq, PartialEq, Copy, Clone)]
enum Orientation { X, Y, Z }

#[derive(Debug, Copy, Clone)]
struct Shape {
    id: usize,
    position: Vec3,
    length: usize,
    orientation: Orientation,
}

impl Shape {
    fn from_str((id, str): (usize, &str)) -> Shape {
        let points = str
            .split("~")
            .map(|s| {
                if let [x, y, z] = s
                    .split(',')
                    .map(|s| s.parse::<usize>().unwrap())
                    .collect::<Vec<_>>()[..] {
                    (x, y, z)
                } else {
                    unreachable!()
                }
            })
            .collect::<Vec<Vec3>>();

        let [position, end] = points[..] else { unreachable!() };

        let (orientation, length) = if position.0 != end.0 {
            (Orientation::X, end.0 - position.0 + 1)
        } else if position.1 != end.1 {
            (Orientation::Y, end.1 - position.1 + 1)
        } else {
            (Orientation::Z, end.2 - position.2 + 1)
        };

        Shape { id, position, orientation, length }
    }

    fn top(&self) -> usize {
        match self.orientation {
            Orientation::Z => self.position.2 + self.length,
            Orientation::X | Orientation::Y => self.position.2
        }
    }

    fn bottom(&self) -> usize {
        self.position.2
    }

    fn x_bounds(&self) -> Range<usize> {
        let end = match self.orientation {
            Orientation::X => self.position.0 + self.length,
            Orientation::Y | Orientation::Z => self.position.0 + 1
        };
        self.position.0..end
    }

    fn y_bounds(&self) -> Range<usize> {
        let end = match self.orientation {
            Orientation::Y => self.position.1 + self.length,
            Orientation::X | Orientation::Z => self.position.1 + 1
        };
        self.position.1..end
    }

    fn x_overlap(&self, other: Shape) -> bool {
        let a = self.x_bounds().start;
        let b = self.x_bounds().end;
        let c = other.x_bounds().start;
        let d = other.x_bounds().end;

        a < d && c < b
    }

    fn y_overlap(&self, other: Shape) -> bool {
        let a = self.y_bounds().start;
        let b = self.y_bounds().end;
        let c = other.y_bounds().start;
        let d = other.y_bounds().end;

        a < d && c < b
    }

    fn drop(&mut self, hm: &mut Vec<Vec<usize>>) {
        match self.orientation {
            Orientation::X => {
                let mut max_z = 0;
                for x in 0..self.length {
                    max_z = max(max_z, hm[self.position.0 + x][self.position.1])
                }
                self.position.2 = max_z + 1;
                for x in 0..self.length {
                    hm[self.position.0 + x][self.position.1] = max_z + 1;
                }
            }
            Orientation::Y => {
                let mut max_z = 0;
                for y in 0..self.length {
                    max_z = max(max_z, hm[self.position.0][self.position.1 + y])
                }
                self.position.2 = max_z + 1;
                for y in 0..self.length {
                    hm[self.position.0][self.position.1 + y] = max_z + 1;
                }
            }
            Orientation::Z => {
                self.position.2 = hm[self.position.0][self.position.1] + 1;
                hm[self.position.0][self.position.1] = self.top();
            }
        }
    }

    pub(crate) fn rests_on(&self, other: Shape) -> bool {
        self.bottom() == other.top() + 1 && self.x_overlap(other) && self.y_overlap(other)
    }
}

fn settle_2(tower: &Tower) -> Vec<Shape> {
    let mut hm = vec![vec![0; 10]; 10];

    let mut tower = tower.clone();

    let mut supports: Vec<Vec<usize>> = vec![Vec::new(); tower.len()];
    let mut supported_by: Vec<Vec<usize>> = vec![Vec::new(); tower.len()];

    for i in 0..tower.len() {
        tower[i].drop(&mut hm);
    }

    for i in 0..tower.len() {
        for j in 0..tower.len() {
            if tower[i].rests_on(tower[j]) {
                supports[j].push(i);
                supported_by[i].push(j);
            }
        }
    }

    println!("a1={}\n", (0..tower.len())
        .filter(|i| supports[*i].iter().all(|j| supported_by[*j].len() > 1))
        .count());

    tower
}

fn settle(tower: &Tower) -> Tower {
    let mut tower: Vec<Shape> = tower.clone();
    let mut supports: Vec<Vec<usize>> = vec![Vec::new(); tower.len()];
    let mut supported_by: Vec<Vec<usize>> = vec![Vec::new(); tower.len()];

    for i in 0..tower.len() {
        let mut landed = false;
        for j in (0..i).rev() {
            if !landed && tower[j].top() < tower[i].bottom() {
                tower[i].position.2 = tower[j].top() + 1;
            }
            if tower[i].rests_on(tower[j]) {
                landed = true;
                supported_by[i].push(j);
                supports[j].push(i);
            }
        }
        if !landed {
            tower[i].position.2 = 1;
        }
    }

    println!("a1={}\n", (0..tower.len())
        .filter(|i| supports[*i].iter().all(|j| supported_by[*j].len() > 1))
        .count());

    tower
}


//
//
//

fn print_tower(tower: &Tower) {
    print_tower_x(&tower);
    println!();
    print_tower_y(&tower);
}


fn print_tower_y(tower: &Tower) {
    let mut max_y = 0;
    let mut max_z = 0;
    for brick in tower {
        match brick.orientation {
            Orientation::X => {
                max_y = max(max_y, brick.position.1 + 1);
                max_z = max(max_z, brick.position.2 + 1);
            }
            Orientation::Y => {
                max_y = max(max_y, brick.position.1 + brick.length);
                max_z = max(max_z, brick.position.2 + 1);
            }
            Orientation::Z => {
                max_y = max(max_y, brick.position.1 + 1);
                max_z = max(max_z, brick.position.2 + brick.length);
            }
        }
    }

    let mut grid = vec![vec!['.'; max_y]; max_z];

    for i in 0..max_y {
        grid[0][i] = '-';
    }

    for brick in tower {
        let c = std::char::from_u32(brick.id as u32 + 65).unwrap();
        match brick.orientation {
            Orientation::X => {
                grid[brick.position.2][brick.position.1] =
                    if grid[brick.position.2][brick.position.1] == '.' { c } else { '?' };
            }
            Orientation::Y => {
                for x in brick.position.1..(brick.position.1 + brick.length) {
                    grid[brick.position.2][x] = if grid[brick.position.2][x] == '.' { c } else { '?' };
                }
            }
            Orientation::Z => {
                for z in brick.position.2..(brick.position.2 + brick.length) {
                    grid[z][brick.position.1] = if grid[z][brick.position.1] == '.' { c } else { '?' };
                }
            }
        }
    }

    for (i, row) in grid.iter().enumerate().rev() {
        println!("{} {}", row.iter().collect::<String>(), i);
    }
}


fn print_tower_x(tower: &Tower) {
    let mut max_x = 0;
    let mut max_z = 0;
    for brick in tower {
        match brick.orientation {
            Orientation::X => {
                max_x = max(max_x, brick.position.0 + brick.length);
                max_z = max(max_z, brick.position.2 + 1);
            }
            Orientation::Y => {
                max_x = max(max_x, brick.position.0 + 1);
                max_z = max(max_z, brick.position.2 + 1);
            }
            Orientation::Z => {
                max_x = max(max_x, brick.position.0 + 1);
                max_z = max(max_z, brick.position.2 + brick.length);
            }
        }
    }

    let mut grid = vec![vec!['.'; max_x]; max_z];

    for i in 0..max_x {
        grid[0][i] = '-';
    }

    for brick in tower {
        let c = std::char::from_u32(brick.id as u32 + 65).unwrap();
        match brick.orientation {
            Orientation::X => {
                for x in brick.position.0..(brick.position.0 + brick.length) {
                    grid[brick.position.2][x] = if grid[brick.position.2][x] == '.' { c } else { '?' };
                }
            }
            Orientation::Y => {
                grid[brick.position.2][brick.position.0] =
                    if grid[brick.position.2][brick.position.0] == '.' { c } else { '?' };
            }
            Orientation::Z => {
                for z in brick.position.2..(brick.position.2 + brick.length) {
                    grid[z][brick.position.0] = if grid[z][brick.position.0] == '.' { c } else { '?' };
                }
            }
        }
    }

    for (i, row) in grid.iter().enumerate().rev() {
        println!("{} {}", row.iter().collect::<String>(), i);
    }
}



