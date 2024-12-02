use std::fmt::{Display, Formatter};
use pathfinding::num_traits::Num;
use crate::util::direction::Direction::{East, North, South, West};

#[derive(Debug, PartialEq, Eq, Hash, Clone, Copy, PartialOrd, Ord)]
pub enum Direction {
    North = 0,
    West = 1,
    South = 2,
    East = 3,
}

impl Display for Direction {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", match self {
            North => '^',
            West => '<',
            South => 'v',
            East => '>'
        })
    }
}

impl Direction {
    pub(crate) fn from_int(i: isize) -> Direction {
        [North, West, South, East][i as usize]
    }

    pub(crate) fn from_char(c: char) -> Direction {
        match c {
            'U' | 'u' | '^' => North,
            'L' | 'l' | '<' => West,
            'D' | 'd' | 'v' => South,
            'R' | 'r' | '>' => East,
            _ => panic!("unknown directional char {}", c),
        }
    }

    pub(crate) fn vertical(&self) -> bool {
        match self {
            North | South => true,
            _ => false
        }
    }

    pub(crate) fn turn_left(&self) -> Direction {
        if self == &East {
            North
        } else {
            [North, West, South, East][*self as usize + 1]
        }
    }

    pub(crate) fn turn_right(&self) -> Direction {
        if self == &North {
            East
        } else {
            [North, West, South, East][*self as usize - 1]
        }
    }

    pub fn move_with_bounds(&self, (i, j): &(usize, usize), (n, m): &(usize, usize)) -> Result<Pos, ()> {
        match self {
            North => if i > &0usize { Ok((i - 1, *j)) } else { Err(()) },
            South => if *i < n - 1 { Ok((i + 1, *j)) } else { Err(()) }
            West => if j > &0usize { Ok((*i, j - 1)) } else { Err(()) },
            East => if *j < m - 1 { Ok((*i, j + 1)) } else { Err(()) }
        }
    }

    pub fn move_n<T: Num>(&self, (i,j): (T, T), n: T) -> (T, T) {
        match self {
            North => (i - n, j),
            South => (i + n, j),
            West => (i, j - n),
            East => (i, j + n)
        }
    }
}

type Pos = (usize, usize);

