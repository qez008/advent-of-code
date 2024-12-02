func day6() {
    let sample = Array("mjqjpqmgbljsphdztnvjfqwrcgsmlb")
    let n = 4
    let answer = (n..<sample.count).first(where: { Set(sample[$0-n..<$0]).count == n })
    print(answer as Any)
}

day6()