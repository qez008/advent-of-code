use std::fs::read_to_string;
use pathfinding::num_traits::signum;

type Vec3 = (f64, f64, f64);

#[derive(Debug, Copy, Clone)]
struct Line {
    pos: Vec3,
    vel: Vec3,
}

impl Line {
    fn x(&self) -> f64 {
        self.pos.0
    }

    fn y(&self) -> f64 {
        self.pos.1
    }

    fn z(&self) -> f64 {
        self.pos.2
    }

    fn velX(&self) -> f64 {
        self.vel.0
    }

    fn velY(&self) -> f64 {
        self.vel.1
    }

    fn velZ(&self) -> f64 {
        self.vel.2
    }
}

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_24_input.txt")
        .unwrap();

    let data: Vec<Line> = input
        .lines()
        .map(|line| {
            let numbers: Vec<_> = line
                .split(|c| c == '@' || c == ',')
                .map(|n| n.trim().parse::<f64>()
                    .expect(&format!("failed to parse {}", n)))
                .collect();
            let [a, b, c, d, e, f] = numbers[..]
                else { unreachable!() };

            Line {
                pos: (a, b, c),
                vel: (d, e, f),
            }
        })
        .collect();

    //part_one(&data);
    part_two(&data);
}

//          759222261162169
// to low:  761691907059619
// to low:  761691907059620
//          761691907059633 ?
// to high: 761691907059648
fn part_two(data: &Vec<Line>) {
    let mut coffs = [[0.0; 4]; 4];
    let mut rhs = [0.0; 4];

    for i in 0..4 {
        let l1 = data[i];
        let l2 = data[i + 1];
        coffs[i][0] = l2.velY() - l1.velY();
        coffs[i][1] = l1.velX() - l2.velX();
        coffs[i][2] = l1.y() - l2.y();
        coffs[i][3] = l2.x() - l1.x();
        rhs[i] = -l1.x() * l1.velY() + l1.y() * l1.velX() + l2.x() * l2.velY() - l2.y() * l2.velX();
    }

    (coffs, rhs) = gaus_elim(&coffs, &rhs);

    let [x, y, vx, vy] = rhs
        .iter()
        .map(round_ties_to_pos_infinity)
        .collect::<Vec<_>>()[..] else { unreachable!() };

    let mut coffs = [[0.0; 2]; 2];
    let mut rhs = [0.0; 2];

    for i in 0..2 {
        let l1 = data[i];
        let l2 = data[i + 1];
        coffs[i][0] = l1.velX() - l2.velX();
        coffs[i][1] = l2.x() - l1.x();
        rhs[i] = -l1.x() * l1.velZ() + l1.z() * l1.velX() + l2.x() * l2.velZ() - l2.z() * l2.velX()
            - ((l2.velZ() - l1.velZ()) * x) - ((l1.z() - l2.z()) * vx);
    }

    (coffs, rhs) = gaus_elim(&coffs, &rhs);

    let [z, vz] = rhs[0..2]
        .iter()
        .map(round_ties_to_pos_infinity)
        .collect::<Vec<_>>()[..] else { unreachable!() };

    //let trajectory = Line { pos: (x, y, z), vel: (vx, vy, vz) };

    println!("{}", x + y + z);
}


fn round_ties_to_pos_infinity(x: &f64) -> f64 {
    x.round()
}

fn gaus_elim<const N: usize>(coffs: &[[f64; N]; N], rhs: &[f64; N]) -> ([[f64; N]; N], [f64; N]) {
    let mut coffs = coffs.clone();
    let mut rhs = rhs.clone();

    let n = coffs.len();
    for i in 0..n {
        let pivot = coffs[i][i];
        for j in 0..n {
            coffs[i][j] = coffs[i][j] / pivot;
        }
        rhs[i] = rhs[i] / pivot;
        for k in 0..n {
            if i != k {
                let factor = coffs[k][i];
                for j in 0..n {
                    coffs[k][j] = coffs[k][j] - factor * coffs[i][j];
                }
                rhs[k] = rhs[k] - factor * rhs[i];
            }
        }
    }
    (coffs, rhs)
}

fn part_one(data: &Vec<Line>) {
    let mut count = 0;
    for i in 0..data.len() {
        for j in 0..i {
            let a = data[i].clone();
            let b = data[j].clone();
            let test_area = 200000000000000.0..=400000000000000.0;
            if let Some((x, y)) = intersection(a.pos, b.pos, a.vel, b.vel) {
                if test_area.contains(&x) && test_area.contains(&y) {
                    //println!("{:?} crossed {:?} at {:?}, {:?}", data[i], data[j], x, y);
                    count += 1;
                }
            }
        }
    }
    println!("{}", count);
}

fn intersection(p1: Vec3, p2: Vec3, n1: Vec3, n2: Vec3) -> Option<(f64, f64)> {
    let u = (p1.1 * n2.0 + n2.1 * p2.0 - p2.1 * n2.0 - n2.1 * p1.0) / (n1.0 * n2.1 - n1.1 * n2.0);
    let v = (p1.0 + n1.0 * u - p2.0) / n2.0;
    if u > 0.0 && v > 0.0 {
        Some((p1.0 + n1.0 * u, p1.1 + n1.1 * u))
    } else {
        None
    }
}
