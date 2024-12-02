use std::cmp::max;
use std::collections::HashMap;
use std::fs::read_to_string;

pub fn solve() {
    let cubes = HashMap::from([
        ("red", 12),
        ("green", 13),
        ("blue", 14)
    ]);
    let input = read_to_string("day_02_input_1.txt")
        .expect("");

    let mut sum = 0;
    for line in input.split("\n") {
        let data = line
            .split(|c| c == ':' || c == ';')
            .collect::<Vec<&str>>();
        let game_id = &data[0][5..];
        //if fits(&cubes, &data[1..]) {
        //    sum += game_id.parse::<i32>().unwrap();
        //    //println!("{} fits", game_id)
        //}
        let power = power(&data[1..]);
        //println!("Game: {}, power: {}", game_id, power)
        sum += power;
    }
    println!("{}", sum)
}

fn fits(cubes: &HashMap<&str, i32>, game: &[&str]) -> bool {
    for samples in game {
        //println!("  {}", sample);
        for sample in samples.split(',') {
            let number_color = sample
                .split(' ')
                .collect::<Vec<&str>>();
            let number = number_color[1]
                .parse::<i32>()
                .unwrap();
            let color = number_color[2];
            if cubes.get(color).unwrap() < &number {
                return false;
            }
        }
    }
    return true;
}

fn power(game: &[&str]) -> i32 {
    let mut cubes = HashMap::from([
        ("red", 0),
        ("green", 0),
        ("blue", 0)
    ]);
    for samples in game {
        //println!("  {}", sample);
        for sample in samples.split(',') {
            let number_color = sample
                .split(' ')
                .collect::<Vec<&str>>();
            let number = number_color[1]
                .parse::<i32>()
                .unwrap();
            let color = number_color[2];
            if number > *cubes.get(color).unwrap() {
                cubes.insert(color, number);
            }
        }
    }
    return cubes.get("red").unwrap()
        * cubes.get("green").unwrap()
        * cubes.get("blue").unwrap();
}