use std::collections::{HashMap, HashSet, VecDeque};
use std::fs::read_to_string;
use std::slice::Iter;
use crate::solutions::day_23::Tile::{Forest, Path, Slope, Visited, Start, Finish};

type Point = (usize, usize);
type Map = Vec<Vec<Tile>>;

#[derive(PartialEq, Copy, Clone)]
enum Tile {
    Path,
    Forest,
    Slope(char),
    Visited,
    Start,
    Finish,
}

impl Tile {
    fn parse(char: char) -> Tile {
        match char {
            '<' | '>' | 'v' | '^' => Slope(char),
            '.' => Path,
            '#' => Forest,
            _ => unreachable!()
        }
    }

    fn symbol(&self) -> char {
        match self {
            Path => '.',
            Forest => '#',
            Slope(dir) => *dir,
            Visited => 'O',
            Start => 'S',
            Finish => 'F'
        }
    }
}

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_23_input.txt").unwrap();

    let map: Map = input
        .lines()
        .map(|line| line
            .chars()
            .map(Tile::parse)
            .collect())
        .collect();

    dynprog(&map);
}

fn dynprog(map: &Map) {
    let (n, m): Point = (map.len(), map[0].len());
    let start: Point = (0, 1);
    let end: Point = (n - 1, m - 2);

    let mut map: Map = map.clone();
    //map[start.0][start.1] = Start;
    //map[n - 1][m - 2] = Finish;

    let graph = create_graph((n, m), &map);
    println!("{:?}", dfs(start, end, &graph));
    //println!("{:?}", wander_improved(vec![(1, 1)], 1, &graph, &end));
}

#[derive(Debug, Clone)]
struct Graph {
    nodes: Vec<Point>,
    edges: HashMap<Point, Vec<(Point, usize)>>,
}

fn create_graph((n, m): Point, map: &Map) -> Graph {
    let start: Point = (0, 1);
    let end: Point = (n - 1, m - 2);
    let mut graph = Graph { nodes: Vec::from([start, end]), edges: HashMap::new() };

    for i in 0..n {
        for j in 0..m {
            match map[i][j] {
                Path | Slope(_) => {
                    let p = (i, j);
                    let next_tiles = new_tiles_2(p, map);
                    if next_tiles.len() >= 3 {
                        graph.nodes.push(p);
                    }
                }
                _ => {}
            };
        }
    }

    let nodes = graph.nodes.clone();

    for u in &graph.nodes {
        for (v, steps) in new_tiles_2(*u, map)
            .iter()
            .map(|p| explore(vec![*u, *p], &nodes, map)) {
            //
            if let Some(list) = graph.edges.get_mut(u) {
                list.push((v, steps));
            } else {
                graph.edges.insert(*u, vec![(v, steps)]);
            }
            if let Some(list) = graph.edges.get_mut(&v) {
                list.push((*u, steps));
            } else {
                graph.edges.insert(v, vec![(*u, steps)]);
            }
        }
    }

    graph
}

fn explore(path: Vec<Point>, nodes: &Vec<Point>, map: &Map) -> (Point, usize) {
    let last = path.last().unwrap();
    if nodes.contains(last) {
        (*last, path.len()-1)
    } else {
        let mut p2 = path.clone();
        let foo = new_tiles_2(*last, map);
        p2.push(foo
            .iter()
            .filter(|p| !path.contains(p))
            .last()
            .cloned().expect(&format!("{:?} does not have any nodes to connect to {:?}, path {:?}", last, foo, path)));
        explore(p2, nodes, map)
    }
}

fn dfs(start: Point, end: Point, graph: &Graph) -> usize {
    enum Stage {
        Enter(Point, usize),
        Loop(Point, usize, usize),
        Finish(Point),
    }
    use Stage::*;

    let mut seen = HashSet::new();
    let mut lstack = vec![Enter(start, 0)];
    let mut d_max = 0;

    while let Some(stage) = lstack.pop() {
        match stage {
            Enter(v, depth) => {
                if v == end {
                    d_max = d_max.max(depth);
                } else {
                    seen.insert(v);
                    lstack.push(Loop(v, 0, depth));
                }
            }
            Loop(v, i, depth) => {
                if i == 0 {
                    lstack.push(Finish(v));
                }
                if i < graph.edges.get(&v).unwrap().len() {
                    let (v2, wt) = graph.edges.get(&v).unwrap()[i];
                    lstack.push(Loop(v, i + 1, depth));
                    if !seen.contains(&v2) {
                        lstack.push(Enter(v2, depth + wt));
                    }
                }
            }
            Finish(v) => {
                seen.remove(&v);
            }
        }
    }
    d_max
}

fn wander_improved(path: Vec<Point>, steps: usize, graph: &Graph, finish: &Point) -> Option<(Vec<Point>, usize)> {
    let last = path.last().unwrap();
    if last == finish {
        Some((path, steps))
    } else {
        graph.edges
            .get(last)
            .unwrap()
            .iter()
            .map(|(next, n)| {
                if path.contains(next) {
                    None
                } else {
                    let mut path = path.clone();
                    path.push(*next);
                    wander_improved(path, steps + n - 1, graph, finish)
                }
            })
            .filter(Option::is_some)
            .map(Option::unwrap)
            .max_by(|a, b| a.1.cmp(&b.1))
    }
}


fn wander(path: Vec<Point>, map: &Map) -> Option<Vec<Point>> {
    let (i, j) = *path.last().unwrap();
    if i + 1 == map.len() && j + 2 == map[0].len() {
        Some(path)
    } else if i == 0 {
        None
    } else {
        new_tiles_2((i, j), map)
            .iter()
            .filter(|p| !path.contains(p))
            .map(|p| {
                let mut p2 = path.clone();
                p2.push(*p);
                wander(p2, map)
            })
            .filter(Option::is_some)
            .map(Option::unwrap)
            .max_by(|a, b| a.len().cmp(&b.len()))
    }
}

fn new_tiles((i, j): Point, map: &Map) -> Vec<Point> {
    if let Slope(dir) = map[i][j].clone() {
        let p = match dir {
            '<' => (i, j - 1),
            '>' => (i, j + 1),
            'v' => (i + 1, j),
            '^' => (i - 1, j),
            _ => unreachable!()
        };
        vec![p]
    } else {
        [(i + 1, j), (i - 1, j), (i, j + 1), (i, j - 1)]
            .iter()
            .filter(|(i, j)| map[*i][*j] != Forest)
            .filter(|p| p.0 != 0)
            .map(|p| *p)
            .collect::<Vec<Point>>()
    }
}

fn new_tiles_2((i, j): Point, map: &Map) -> Vec<Point> {
    let i = i as isize;
    let j = j as isize;
    [(i + 1, j), (i - 1, j), (i, j + 1), (i, j - 1)]
        .iter()
        .filter(|(i, j)| i >= &0 && j >= &0)
        .map(|(i, j)| (*i as usize, *j as usize))
        .filter(|(i, j)| i < &map.len() && j < &map[0].len())
        .filter(|(i, j)| map[*i][*j] != Forest)
        .map(|p| p)
        .collect::<Vec<Point>>()
}
