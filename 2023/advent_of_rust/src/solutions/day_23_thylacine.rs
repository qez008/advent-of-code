use std::collections::{HashMap, HashSet};
use std::error::Error;
use std::fs::read_to_string;

type Map = Vec<Vec<char>>;
type Graph = HashMap<Point, Vertex>;
type Point = (usize, usize);
type Edge = (usize, usize, usize);

#[derive(Debug, Clone, Eq, PartialEq, Hash)]
struct Vertex {
    point: Point,
    edges: Vec<Edge>,
}

impl Vertex {
    fn new(pos: Point, adj: Vec<Point>) -> Self {
        Self {
            point: pos,
            edges: adj.into_iter().map(|p| (p.0, p.1, 1)).collect(),
        }
    }

    fn collapse_edges(&mut self, graph: &Graph) {
        for adj_i in 0..self.degree() {
            let mut curr = &*self;
            let mut weight = 1;
            let mut adj_j = adj_i;
            while let Some(adj_v) = graph.get(&curr.edge(adj_j)) {
                if adj_v.degree() == 2 {
                    if adj_v.edge(0) == curr.point {
                        adj_j = 1;
                    } else {
                        adj_j = 0;
                    }
                    weight += 1;
                    curr = adj_v;
                } else {
                    curr = adj_v;
                    break;
                }
            }
            self.edges[adj_i] = (curr.point.0, curr.point.1, weight);
        }
    }

    fn degree(&self) -> usize {
        self.edges.len()
    }

    fn edge(&self, i: usize) -> Point {
        (self.edges[i].0, self.edges[i].1)
    }

    fn edge_weight(&self, i: usize) -> usize {
        self.edges[i].2
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
                if i == 0 { lstack.push(Finish(v)); }
                if i < graph[&v].degree() {
                    let v2 = graph[&v].edge(i);
                    let wt = graph[&v].edge_weight(i);
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

pub fn solve() {
    let input = read_to_string("src/inputs/day_23_input.txt").unwrap();

    println!("{:?}", part_2(&input));
}

fn part_2(input: &str) -> Result<usize, Box<dyn Error>> {
    let map = input
        .lines()
        .map(|l| l.chars().collect())
        .collect::<Map>();
    let (m, n) = (map.len(), map[0].len());
    let start = (0usize, 1usize);
    let end = (m - 1, n - 2);

    let mut graph = Graph::new();

    macro_rules! map { ($p: expr) => { (map[$p.0][$p.1]) } }

// Create the adjacency map.
    for (i, row) in map.iter().enumerate() {
        for j in 0..row.len() {
            if map[i][j] == '#' {
                continue;
            }
            let test = [
                (i.saturating_sub(1), j), (i, j.saturating_sub(1)),
                ((i + 1).min(m - 1), j), (i, (j + 1).min(n - 1))
            ];

            let mut adj = Vec::with_capacity(4);

            for p in &test {
                if map!(*p) != '#' { adj.push(*p); }
            }
            adj.retain(|p| *p != (i, j));
            graph.insert((i, j), Vertex::new((i, j), adj));
        }
    }
// Collapse the edges of each Vertex that has more than two edges.
    for point in graph.keys().copied().collect::<Vec<_>>() {
        if graph[&point].degree() > 2 {
            let mut v = graph[&point].clone();
            v.collapse_edges(&graph);
            graph.insert(point, v);
        }
    }
// Basic DFS traversal to find longest path to end.
    let steps = dfs(start, end, &graph);

    println!("Part 2 Total steps...: {}", steps);
    Ok(steps)
}

