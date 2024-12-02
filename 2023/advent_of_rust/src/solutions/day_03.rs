use std::collections::{HashMap, HashSet};
use std::fs::read_to_string;


pub fn solve() {
    let input: Vec<Vec<char>> = read_to_string("src/inputs/day_03_input_1.txt")
        .expect("failed to read file")
        .split('\n')
        .map(|x| x.chars().collect())
        .collect();
    let h = input.len();
    let w = input[0].len();

    let mut gears: HashMap<(i32, i32), Vec<i32>> = HashMap::new();
    for y in 0..h {
        for x in 0..w {
            if input[y][x] == '*' {
                gears.insert((x as i32, y as i32), Vec::new());
            }
        }
    }

    for y in 0..h {
        for x in 0..w {
            // check if this is the start of a word
            if input[y][x].is_numeric() && (x == 0 || !input[y][x - 1].is_numeric()) {
                // locate the end of the word
                let start = x;
                let mut end = x;

                while (end < w) && input[y][end].is_numeric() {
                    end += 1;
                }
                let ratio = input[y][start..end]
                    .iter()
                    .collect::<String>()
                    .parse::<i32>()
                    .unwrap();

                for i in (start as i32 - 1)..(end as i32 + 1) {
                    for j in (y as i32 - 1)..(y as i32 + 2) {
                        let nbr = (i, j);
                        if gears.contains_key(&nbr) {
                            println!("{} is near gear {:?}", ratio, nbr);
                            gears.get_mut(&nbr).unwrap().push(ratio);
                        }
                    }
                }
            }
        }
    }
    let sum = gears
        .iter()
        .map(|(k, v)| v)
        .filter(|v| v.len() == 2)
        .map(|v| v.iter().fold(1, |acc, &x| acc * x))
        .sum::<i32>();
    println!("–––\n{}", sum)
}

fn bar(input: &Vec<Vec<char>>,
       gears: &mut HashMap<(i32, i32), Vec<i32>>,
       x: usize,
       y: usize,
       w: usize) {

    // check if this is the start of a word
    if input[y][x].is_numeric() && (x == 0 || !input[y][x - 1].is_numeric()) {
        // locate the end of the word
        let start = x;
        let mut end = x;

        while (end < w) && input[y][end].is_numeric() {
            end += 1;
        }
        let ratio = input[y][start..end]
            .iter()
            .collect::<String>()
            .parse::<i32>()
            .unwrap();

        for i in (start as i32 - 1)..(end as i32 + 1) {
            for j in (y as i32 - 1)..(y as i32 + 2) {
                let nbr = (i, j);
                if gears.contains_key(&nbr) {
                    println!("{} is near gear {:?}", ratio, nbr);
                    gears.get_mut(&nbr).unwrap().push(ratio);
                }
            }
        }
    }
}

pub fn solve1() {
    let input: Vec<Vec<char>> = read_to_string("src/inputs/day_03_input_1.txt")
        .expect("failed to read file")
        .split('\n')
        .map(|x| x.chars().collect())
        .collect();
    let h = input.len();
    let w = input[0].len();

    let mut symbols: HashSet<(usize, usize)> = HashSet::new();
    for y in 0..h {
        for x in 0..w {
            if !input[y][x].is_numeric() && input[y][x] != '.' {
                symbols.insert((x - 1, y + 1));
                symbols.insert((x, y + 1));
                symbols.insert((x + 1, y + 1));
                symbols.insert((x + 1, y));
                symbols.insert((x + 1, y - 1));
                symbols.insert((x, y - 1));
                symbols.insert((x - 1, y - 1));
                symbols.insert((x - 1, y));
            }
        }
    }
    println!("{:?}", symbols);

    let mut sum = 0;

    for y in 0..h {
        for x in 0..w {
            sum += foo(&input, &symbols, x, y, w, h);
        }
    }
    println!("–––\n{}", sum)
}

fn foo(input: &Vec<Vec<char>>,
       nearby_symbols: &HashSet<(usize, usize)>,
       x: usize,
       y: usize,
       w: usize,
       h: usize) -> i32 {

    // check if this is the start of a word
    if input[y][x].is_numeric() && (x == 0 || !input[y][x - 1].is_numeric()) {
        // locate the end of the word
        let start = x;
        let mut end = x;

        let mut near_symbol = false;

        while (end < w) && input[y][end].is_numeric() {
            if !near_symbol && nearby_symbols.contains(&(end, y)) {
                near_symbol = true;
            }
            end += 1;
        }
        //println!("{} near symbol: {}", input[y][start..end].iter().collect::<String>(), near_symbol);
        if near_symbol {
            return input[y][start..end]
                .iter()
                .collect::<String>()
                .parse::<i32>()
                .unwrap();
        }
    }
    0
}