use std::fs::read_to_string;
use std::ops::Range;

pub fn solve() {
    let input = read_to_string("src/inputs/day_05_input.txt")
        .expect("fail to read puzzle input");
    let lines = input
        .lines()
        .collect::<Vec<&str>>();
    //println!("{:?}", lines);

    let seed_to_soil = parse_range("seed-to-soil map:", &lines);
    let soil_to_fertilizer = parse_range("soil-to-fertilizer map:", &lines);
    let fertilizer_to_water = parse_range("fertilizer-to-water map:", &lines);
    let water_to_light = parse_range("water-to-light map:", &lines);
    let light_to_temperature = parse_range("light-to-temperature map:", &lines);
    let temperature_to_humidity = parse_range("temperature-to-humidity map:", &lines);
    let humidity_to_location = parse_range("humidity-to-location map:", &lines);

    let transformations = vec![
        seed_to_soil,
        soil_to_fertilizer,
        fertilizer_to_water,
        water_to_light,
        light_to_temperature,
        temperature_to_humidity,
        humidity_to_location
    ];

    let seeds: Vec<i64> = parse_seeds(lines[0]);
    let a1 = seeds
        .iter()
        .map(|seed| map_ranges(*seed, &transformations))
        .min()
        .unwrap();
    println!("a1={}", a1);

    let seed_ranges = parse_seed_ranges(lines[0]);
    println!("{:?}", seed_ranges);
    for i in 0.. {
        let seed = map_reverse(i, &transformations);
        //println!("location {} -> seed {}", i, seed);
        if seed_ranges
            .iter()
            .any(|r| r.contains(&seed)) {
            println!("a2={}", i);
            return;
        }
        //println!("{} is not a valid seed", seed);
    }
}

fn parse_seeds(line: &str) -> Vec<i64> {
    line.strip_prefix("seeds: ")
        .unwrap()
        .split(" ")
        .map(|s| s.parse::<i64>().expect(&format!("failed to parse seed {}", s)))
        .collect()
}

fn parse_seed_ranges(line: &str) -> Vec<Range<i64>> {
    parse_seeds(line)
        .chunks(2)
        .map(|c| c[0]..(c[0]+ c[1]))
        .collect::<Vec<Range<i64>>>()
}

fn parse_range(range_name: &str, lines: &Vec<&str>) -> Vec<(Range<i64>, Range<i64>)> {
    let mut range_map: Vec<(Range<i64>, Range<i64>)> = Vec::new();
    let range_start = lines
        .iter()
        .position(|l| l == &range_name)
        .expect("failed to find range");
    for i in (range_start+1)..lines.len() {
        if lines[i].is_empty() {
            break;
        }
        let numbers: Vec<i64> = lines[i]
            .split(" ")
            .map(|s| s.parse::<i64>().expect(&format!("failed to parse {}", s)))
            .collect();
        let destination = numbers[0];
        let source = numbers[1];
        let range_length = numbers[2];
        let source_range = source..(source + range_length);
        let destination_range = destination..(destination + range_length);
        range_map.push((source_range, destination_range));
    }
    range_map
}

fn map_ranges(seed: i64, transformations: &Vec<Vec<(Range<i64>, Range<i64>)>>) -> i64 {
    transformations
        .iter()
        .fold(seed, |acc, t| {
            for (source, destination) in t {
                if source.contains(&acc) {
                    let new_value = destination.start + (acc - source.start);
                    //println!("acc={} in {:?}. mapping to {:?} with new value {}", acc, source, destination, new_value);
                    return new_value;
                }
            }
            //println!("error! acc {} not in any of the ranges", acc);
            acc
    })
}

fn map_reverse(location: i64, transformations: &Vec<Vec<(Range<i64>, Range<i64>)>>) -> i64 {
    transformations
        .iter()
        .rev()
        .fold(location, |acc, t| {
            for (source, destination) in t {
                if destination.contains(&acc) {
                    let new_value = source.start + (acc - destination.start);
                    //println!("acc={} in {:?}. mapping to {:?} with new value {}", acc, destination, source, new_value);
                    return new_value;
                }
            }
            //println!("No mapping");
            acc
        })
}
