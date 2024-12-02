extends Node2D

var DisplayScene: PackedScene = load("res://src/display.tscn")
var running = false

var pattern: Pattern
var display: Display

func _ready() -> void:
	var raw_patterns = parse_input("res://in/day_13_sample.txt")
	pattern = Pattern.new(raw_patterns[0])

	display = DisplayScene.instantiate();
	add_child(display)
	display.set_pattern(pattern)
#	display.set_progress(3, true, [[0,0], [3, 2], [1,2]])

func _unhandled_input(event: InputEvent) -> void:
	if event.is_action_pressed("ui_accept"):
		running = true


func _process(delta: float) -> void:
	if running:
		if pattern._state != 2:
			pattern.step()

		display.set_progress(pattern.index, pattern._state == 1, [])
		print(pattern.index, pattern._state)


func parse_input(path: String) -> Array[PackedStringArray]:
	var file := FileAccess.open(path, FileAccess.READ)

	var patterns: Array[PackedStringArray] = []
	patterns.append(PackedStringArray())

	while not file.eof_reached():
		var line = file.get_line()
		if line == "":
			patterns.append(PackedStringArray())
		else:
			patterns.back().append(line)

	return patterns
