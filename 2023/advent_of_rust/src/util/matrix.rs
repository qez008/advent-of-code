use std::fmt;
use std::fmt::{Display, Formatter};
use std::ops::{Index, IndexMut};

pub struct Matrix<T> {
    pub n: usize,
    pub m: usize,
    pub data: Vec<Vec<T>>,
}

impl<T> Index<(usize, usize)> for Matrix<T> {
    type Output = T;
    fn index(&self, (i, j): (usize, usize)) -> &Self::Output {
        &self.data[i][j]
    }
}

impl<T> IndexMut<(usize, usize)> for Matrix<T> {
    fn index_mut(&mut self, index: (usize, usize)) -> &mut Self::Output {
        &mut self.data[index.0][index.1]
    }
}


impl<T: Display> Display for Matrix<T> {
    fn fmt(&self, f: &mut Formatter) -> fmt::Result {
        for row in &self.data {
            for value in row {
                write!(f, "{}", value)?
            }
            write!(f, "\n")?
        }
        Ok(())
    }
}

