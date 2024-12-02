class_name Pattern extends Object

enum { VERTICAL, HORIZONTAL, COMPLETE }

var pattern: Array[PackedInt32Array]
var pattern_t: Array[PackedInt32Array]

var index: int
var _state: int = VERTICAL


func _init(raw_patter: PackedStringArray) -> void:
	# read raw pattern
	pattern = []
	for string in raw_patter:
		pattern.append(PackedInt32Array())
		for c in string:
			pattern.back().append(1 if c == "#" else 0)
	# transpose pattern
	pattern_t = []
	for x in pattern.size():
		for y in pattern[0].size():
			if x == 0:
				pattern_t.append(PackedInt32Array())
			pattern_t[y].append(pattern[x][y])


func step() -> void:
	match _state:
		COMPLETE:
			return
		VERTICAL:
			if is_reflection(pattern_t, index):
				_state = COMPLETE
			else:
				index += 1
			if index == (pattern.size() -1):
				_state = HORIZONTAL
				index = 0

		HORIZONTAL:
			if is_reflection(pattern, index):
				_state = COMPLETE
			else:
				index += 1


func is_reflection(pattern: Array[PackedInt32Array], split: int) -> bool:
	var num_left: int = split
	var num_right: int = pattern.size() - split
	var num = mini(num_left, num_right)

	var smudge = false

	for n in num:
		var i: int = split - 1 - n
		var j: int = split + n
		var diff = difference(pattern[i], pattern[j])
		if diff == 1 and not smudge:
			smudge = true
		elif diff > 0:
			return false

	return smudge


func difference(a: PackedInt32Array, b: PackedInt32Array) -> int:
	var diff = 0
	for i in a.size():
		if a[i] != b[i]:
			diff += 1
			if diff > 1:
				break
	return diff
