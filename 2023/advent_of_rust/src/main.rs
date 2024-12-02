use bitvec::macros::internal::funty::Fundamental;
use crate::solutions::*;

mod util {
    //pub(crate) mod matrix;
    pub(crate) mod direction;
    pub(crate) mod queue;
}

mod solutions {
    pub mod day_24;
}

fn main() {
    day_24::solve();
}
