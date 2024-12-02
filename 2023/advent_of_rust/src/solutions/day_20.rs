use std::collections::HashMap;
use std::fs::read_to_string;
use indexmap::IndexMap;

#[derive(Debug)]
#[derive(PartialEq)]
enum Pulse {
    Low,
    High,
}

type Signal = (Pulse, Vec<String>);

trait Module {
    fn name(&self) -> String;

    fn low_pulse(&mut self, sender: String) -> Option<Signal>;
    fn high_pulse(&mut self, sender: String) -> Option<Signal>;

    fn count(&self) -> usize {
        0
    }

    fn prime(&mut self, _: String) {}

    fn destinations(&self) -> Vec<String>;
}

#[derive(Debug)]
struct Broadcaster {
    destinations: Vec<String>,
}

impl Broadcaster {}

impl Module for Broadcaster {
    fn name(&self) -> String {
        "broadcaster".to_string()
    }

    fn low_pulse(&mut self, _: String) -> Option<Signal> {
        Some((Pulse::Low, self.destinations.clone()))
    }

    fn high_pulse(&mut self, _: String) -> Option<Signal> {
        Some((Pulse::High, self.destinations.clone()))
    }

    fn destinations(&self) -> Vec<String> {
        self.destinations.clone()
    }
}

#[derive(Debug)]
struct FlipFlop {
    name: String,
    on: bool,
    destinations: Vec<String>,
}

impl FlipFlop {
    fn new(name: String, destinations: Vec<String>) -> FlipFlop {
        FlipFlop { name, on: false, destinations }
    }
}

impl Module for FlipFlop {
    fn name(&self) -> String {
        self.name.to_string()
    }

    fn low_pulse(&mut self, _: String) -> Option<Signal> {
        if self.on {
            self.on = false;
            Some((Pulse::Low, self.destinations.clone()))
        } else {
            self.on = true;
            Some((Pulse::High, self.destinations.clone()))
        }
    }

    fn high_pulse(&mut self, _: String) -> Option<Signal> { None }

    fn count(&self) -> usize {
        if self.on { 1 } else { 0 }
    }

    fn destinations(&self) -> Vec<String> {
        self.destinations.clone()
    }
}

#[derive(Debug)]
struct Conjunction {
    name: String,
    memory: HashMap<String, bool>,
    destinations: Vec<String>,
}

impl Conjunction {
    fn pulse(&self) -> Option<Signal> {
        let pulse = if self.memory.values().all(|b| b == &true) {
            Pulse::Low
        } else {
            Pulse::High
        };
        Some((pulse, self.destinations.clone()))
    }
}

impl Module for Conjunction {
    fn name(&self) -> String {
        self.name.to_string()
    }
    fn low_pulse(&mut self, sender: String) -> Option<Signal> {
        self.memory.insert(sender, false);
        self.pulse()
    }

    fn high_pulse(&mut self, sender: String) -> Option<Signal> {
        self.memory.insert(sender, true);
        self.pulse()
    }

    fn prime(&mut self, sender: String) {
        self.memory.insert(sender, false);
    }

    fn destinations(&self) -> Vec<String> {
        self.destinations.clone()
    }
}

pub(crate) fn solve() {
    let input = read_to_string("src/inputs/day_20_input.txt")
        .expect("failed to read input");

    let mut network: IndexMap<String, Box<dyn Module>> = IndexMap::new();

    let data = input.lines()
        .map(|line| {
            let mut iter = line.split(" -> ");
            let module = iter.next().unwrap();
            let destinations = iter.next()
                .unwrap()
                .split(", ")
                .map(|s| s.to_string())
                .collect::<Vec<String>>();
            let symbol = if module == "broadcaster" {
                'b'
            } else if module.contains('%') {
                '%'
            } else if module.contains('&') {
                '&'
            } else {
                unreachable!()
            };
            let name = if symbol == '&' || symbol == '%' {
                &module[1..]
            } else {
                module
            };
            (symbol, name, destinations)
        })
        .collect::<Vec<(char, &str, Vec<String>)>>();

    for (s, n, d) in &data {
        let destinations = d.clone();
        match *s {
            'b' => {
                network.insert("broadcaster".to_string(), Box::new(Broadcaster { destinations }));
            }
            '%' => {
                network.insert(n.to_string(), Box::new(FlipFlop::new(n.to_string(), destinations)));
            }
            '&' => {
                let mut memory = HashMap::new();
                for a in data.iter()
                    .filter(|(_, _, x)| x.contains(&n.to_string()))
                    .map(|(_, x, _)| x.to_string()) {
                    memory.insert(a, false);
                }
                network.insert(n.to_string(), Box::new(Conjunction {
                    name: n.to_string(),
                    destinations,
                    memory,
                }));
            }
            _ => unreachable!()
        }
    }

    // let mut high = 0;
    // let mut low = 0;
    // for _ in 0..1000 {
    //     let (l, h) = button(&mut network);
    //     low += l;
    //     high += h;
    // }
    // println!("a1 = {} * {} = {}", low, high, low * high);

    let xs = [3907, 3797, 4093, 4021];
    println!("{}", xs.iter().product::<i64>());

    for presses in 1..10000 {
        if press_for_rx(&mut network) {
            println!("{}", presses);
            break;
        }
    }
}

fn button(network: &mut IndexMap<String, Box<dyn Module>>) -> (usize, usize) {
    let mut low = 1;
    let mut high = 0;

    let mut broadcaster: &mut Box<dyn Module> = network.get_mut("broadcaster").unwrap();
    let mut queue: Vec<(String, Signal)> = vec![(
        broadcaster.name(),
        broadcaster.low_pulse(broadcaster.name()).unwrap())];

    while !queue.is_empty() {
        let mut next_queue: Vec<(String, Signal)> = Vec::new();
        for (sender, (pulse, destinations)) in &queue {
            for d in destinations {
                match pulse {
                    Pulse::High => high += 1,
                    Pulse::Low => low += 1
                }
                //println!("{} -{:?} -> {}", sender, pulse, d);
                if let Some(module) = network.get_mut(d) {
                    let result = match pulse {
                        Pulse::High => module.high_pulse(sender.clone()),
                        Pulse::Low => module.low_pulse(sender.clone()),
                    };
                    if let Some(signal) = result {
                        next_queue.push((module.name(), signal));
                    }
                }
            }
        }
        queue = next_queue;
    }

    (low, high)
}

fn press_for_rx(network: &mut IndexMap<String, Box<dyn Module>>) -> bool {
    let mut rx_low = 0;
    let mut rx_high = 0;

    let mut broadcaster: &mut Box<dyn Module> = network.get_mut("broadcaster").unwrap();
    let mut queue: Vec<(String, Signal)> = vec![(
        broadcaster.name(),
        broadcaster.low_pulse(broadcaster.name()).unwrap())];


    // 3907 * 3797 * 4093 * 4021
    while !queue.is_empty() {
        let mut next_queue: Vec<(String, Signal)> = Vec::new();
        for (sender, (pulse, destinations)) in &queue {
            if sender == &"hn" && pulse == &Pulse::High {
                return true
            }
            for d in destinations {

                //println!("{} -{:?} -> {}", sender, pulse, d);
                if let Some(module) = network.get_mut(d) {
                    let result = match pulse {
                        Pulse::High => {
                            module.high_pulse(sender.clone())
                        }
                        Pulse::Low => {
                            module.low_pulse(sender.clone())
                        }
                    };
                    if let Some(signal) = result {
                        next_queue.push((module.name(), signal));
                    }
                }
            }
        }
        queue = next_queue;
    }
    if rx_low != 0 {
        println!("{}", rx_low);
    }
    //println!("{} {}", rx_low, rx_high);
    rx_low == 1
}
