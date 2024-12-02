
pub fn solve() {
    let sample_rounds = vec![(7, 9), (15, 40), (30, 200)];
    let rounds = vec![(40, 219), (81, 1012), (77, 1365), (72, 1089)];

    println!("a1={}", part_1(&rounds));
    println!("a1={}", part_2(&rounds));
}
fn part_1(rounds: &Vec<(i64, i64)>) -> i64 {
    rounds
        .iter()
        .map(|(time, distance)| possibilities(*time, *distance))
        .reduce(|acc, possibilities| acc * possibilities)
        .unwrap()
}

fn possibilities(time: i64, distance: i64) -> i64 {
    let mut possibilities = 0;
    for ms in 1..time {
        if ms * (time - ms) > distance {
            possibilities += 1;
        }
    }
    possibilities
}

fn part_2(rounds: &Vec<(i64, i64)>) -> i64 {
    let time = rounds
        .iter()
        .map(|(time, _)| format!("{}", time))
        .collect::<String>()
        .parse::<i64>()
        .unwrap();
    let distance = rounds
        .iter()
        .map(|(_, distance)| format!("{}", distance))
        .collect::<String>()
        .parse::<i64>()
        .unwrap();

    possibilities(time, distance)
}
