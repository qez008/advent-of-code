use std::collections::HashMap;
use std::fmt::{Debug, Display};
use std::fs::read_to_string;
use std::time::Instant;
use crate::solutions::day_19::Rule::{End, Predicate};
use indexmap::IndexMap;

type InputType = usize;

type Workflow = Vec<Rule>;

#[derive(Debug)]
enum Rule {
    Predicate(char, char, InputType, String),
    End(String),
}

type MachinePart = HashMap<char, InputType>;

type XmasRange = HashMap<char, (InputType, InputType)>;

fn combinations(workflow: &String, ranges: XmasRange, workflows: &IndexMap<String, Workflow>) -> InputType {
    if workflow == &"A" {
        return ranges.values()
            .fold(1, |acc, (f, t)| acc * (t - f + 1));
    }

    if workflow == &"R" {
        return 0;
    }

    let mut combos = 0;
    let mut current_range = ranges.clone();

    for rule in workflows.get(workflow).unwrap() {
        let mut range_copy = current_range.clone();
        match rule {
            End(destination) => {
                combos += combinations(destination, range_copy, workflows);
            }
            Predicate(field, op, target, destination) => {
                let (lower, upper) = current_range.get(field).unwrap();
                match op {
                    &'<' => {
                        if upper < target {
                            combos += combinations(destination, range_copy, workflows);
                        } else if lower < target {
                            range_copy.insert(*field, (*lower, target - 1));
                            combos += combinations(destination, range_copy, workflows);
                            current_range.insert(*field, (*target, *upper));
                        }
                    }
                    &'>' => {
                        if lower > target {
                            combos += combinations(destination, range_copy, workflows);
                        } else if upper > target {
                            range_copy.insert(*field, (target + 1, *upper));
                            current_range.insert(*field, (*lower, *target));
                            combos += combinations(destination, range_copy, workflows);
                        }
                    }
                    _ => unreachable!()
                }
            }
        };
    }
    combos
}

fn eval(part: &&MachinePart, workflows: &IndexMap<String, Workflow>) -> bool {
    let mut x = &"in".to_string();
    while let Some(rules) = workflows.get(x) {
        x = rules.iter()
            .filter(|r| match r {
                End(_) => {
                    true
                }
                Predicate(a, b, c, _) => {
                    let field = part.get(a).unwrap();
                    *b == '<' && field < c || *b == '>' && field > c
                }
            })
            .map(|r| match r {
                Predicate(_, _, _, d) => d,
                End(d) => d
            })
            .next()
            .unwrap();
    }
    x == &"A"
}

fn parse(input: &String) -> (IndexMap<String, Workflow>, Vec<MachinePart>) {
    let mut workflows: IndexMap<String, Vec<Rule>> = IndexMap::new();
    let mut parts: Vec<MachinePart> = Vec::new();

    let mut lines = input.lines();
    let mut parsing_workflows = true;

    while let Some(next) = lines.next() {
        if next.is_empty() {
            parsing_workflows = false;
        } else if parsing_workflows {
            let mut iter = next.split(|c| c == '{' || c == ',' || c == '}');
            let key = iter.next().unwrap();
            let values = iter
                .filter(|s| !s.is_empty())
                .map(parse_rule)
                .collect::<Vec<Rule>>();
            workflows.insert(key.to_string(), values);
        } else {
            let mut part: MachinePart = MachinePart::new();
            for description in next
                .trim_matches(|c| c == '{' || c == '}')
                .split(",") {
                let key = description.chars().nth(0).unwrap();
                let value = description[2..].parse::<InputType>().unwrap();
                part.insert(key, value);
            }
            parts.push(part);
        }
    }
    (workflows, parts)
}

fn parse_rule(s: &str) -> Rule {
    if s.contains("<") {
        parse_op('<', s)
    } else if s.contains(">") {
        parse_op('>', s)
    } else {
        End(s.to_string())
    }
}

fn parse_op(op: char, s: &str) -> Rule {
    let mut x = s.split(|c| c == op || c == ':');
    let field = x.next().unwrap().chars().nth(0).unwrap();
    let n = x.next().unwrap().parse::<InputType>().unwrap();
    let destination = x.next().unwrap().to_string();
    Predicate(field, op, n, destination)
}

pub(crate) fn solve() {
    let inst = Instant::now();
    let input = read_to_string("src/inputs/day_19_input.txt")
        .expect("failed to read input");

    let (workflows, parts) = parse(&input);
    let sum = parts
        .iter()
        .filter(|part| eval(part, &workflows))
        .map(|part| part.values().sum::<InputType>())
        .sum::<InputType>();
    println!("a1={}", sum);

    let mut ranges: XmasRange = XmasRange::new();
    ranges.insert('x', (1, 4000));
    ranges.insert('m', (1, 4000));
    ranges.insert('a', (1, 4000));
    ranges.insert('s', (1, 4000));
    println!("a2={:?}", combinations(&"in".to_string(), ranges, &workflows));
    println!("(in {:?})", inst.elapsed());
}
