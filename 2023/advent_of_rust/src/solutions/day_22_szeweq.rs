use std::fs::read_to_string;

type Brick = ([usize; 3], [usize; 3]);

fn parse_pts(input: &str) -> Vec<Brick> {
    let mut pts = input
        .lines()
        .map(|l| {
            let (a, b) = l.split_once('~').unwrap();
            let (mut pa, mut pb) = ([0; 3], [0; 3]);
            for (i, (na, nb)) in a
                .splitn(3, ',')
                .zip(b.splitn(3, ','))
                .enumerate() {
                pa[i] = na.parse::<u16>().unwrap() as usize;
                pb[i] = nb.parse::<u16>().unwrap() as usize;
            }
            (pa, pb)
        })
        .collect::<Vec<_>>();
    pts.sort_unstable_by(|(a, _), (b, _)| a[2].cmp(&b[2]).then(a[1].cmp(&b[1]).then(a[0].cmp(&b[0]))));
    pts = iter_drop(pts.into_iter()).map(|x| x.0).collect::<Vec<_>>();
    pts
}

fn iter_drop(bricks: impl Iterator<Item=Brick>) -> impl Iterator<Item=(Brick, bool)> {
    let mut hmap = [0; 100];
    bricks.map(move |b| {
        let highest = (b.0[0]..=b.1[0])
            .flat_map(|x| (b.0[1]..=b.1[1]).map(move |y| x * 10 + y))
            .map(|i| hmap[i]).max().unwrap_or(0);
        let dz = b.0[2] - highest - 1;
        let nb = (
            [b.0[0], b.0[1], b.0[2] - dz],
            [b.1[0], b.1[1], b.1[2] - dz]
        );
        for x in nb.0[0]..=nb.1[0] {
            for y in nb.0[1]..=nb.1[1] {
                hmap[x * 10 + y] = nb.1[2];
            }
        }
        (nb, nb.0[2] != b.0[2])
    })
}

pub fn part1(input: &str) -> Option<usize> {
    let bricks = parse_pts(input);
    Some((0..bricks.len())
        .map(|i| iter_drop(bricks
            .iter()
            .enumerate()
            .filter_map(move |(j, x)| (i != j).then_some(*x)))
            .filter(|x| x.1)
            .count())
        .sum())
}

pub fn solve() {
    let input = read_to_string("src/inputs/day_22_input.txt").unwrap();
    println!("{:?}", part1(&*input));
}