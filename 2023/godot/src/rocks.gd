extends Node2D


@onready var top: Node2D = $top
@onready var bottom: Node2D = $bottom
@onready var right: Node2D = $right
@onready var left: Node2D = $left


@onready var round_rock: Node2D = $round_rocks/rock
@onready var square_rock: Node2D = $square_rocks/rock

@onready var round_rocks = $round_rocks
@onready var square_rocks = $square_rocks

@export var sand: Color
@export var rock: Color
@export var air: Color


var n: int
var m: int

var grid: PackedStringArray

var track: Vector2i = Vector2i(3, 6)


func _ready() -> void:
	var input: String = Day14Input.new().input
	var lines: PackedStringArray = input.split("\n")
	n = lines.size()
	m = lines[0].length()

	print(str(n) + " " + str(m))
	for y in n:
		for x in m:
#			if lines[y][x] == 'O':
#				var new_rock: Node2D = round_rock.duplicate()
#				round_rocks.add_child(new_rock)
#				new_rock.global_position = Vector2(x * 10, y * 10)
#				print("adding round rock at ", new_rock.global_position)
			if lines[y][x] == '#':
				var new_rock: Node2D = square_rock.duplicate()
				square_rocks.add_child(new_rock)
				new_rock.global_position = Vector2(x * 10, y * 10)
#				print("adding square rock at ", new_rock.global_position)

	round_rock.queue_free()
	square_rock.queue_free()
	grid = lines

#	top.global_position.y = 10
#	bottom.global_position.y = top.global_position.y + n * 10

#	left.global_position.x = 10
#	right.global_position.x = left.global_position.x + m * 10

	var timer = Timer.new()
	add_child(timer)
	timer.wait_time = 0.1
	timer.one_shot = false
	timer.timeout.connect(process)
	timer.start()


var k = 0
var l = 0
var movement = true
var dir = 0

func _unhandled_input(event: InputEvent) -> void:
	if (event.is_action_pressed("ui_accept")):
		l += 1



func process() -> void:
	await get_tree().create_timer(1).timeout
	l += 1
	if k < l:
		match dir:
			0:
				if not slide_north():
					dir = 1
			1:
				if not slide_west():
					dir = 2
			2:
				if not slide_south():
					dir = 3
			3:
				if not slide_east():
					dir = 0

#		for row in grid:
#			print(row)
#		print()

#		if not movement:
#			print("complete")

		queue_redraw()
		k += 1


func slide_north() -> bool:
	var moved = false
	for j in range(0, m):
		for i in range(1, n):
			if grid[i][j] == 'O' and grid[i-1][j] == '.':
				grid[i][j] = '.'
				grid[i-1][j] = 'O'
				moved = true
				if i == track.x and j == track.y:
					track = Vector2i(i-1, j)
	return moved


func slide_south() -> bool:
	var moved = false
	for j in range(0, m):
		for i in range(n-2, -1, -1):
			if grid[i][j] == 'O' and grid[i+1][j] == '.':
				grid[i][j] = '.'
				grid[i+1][j] = 'O'
				moved = true
				if i == track.x and j == track.y:
					track = Vector2i(i+1, j)
	return moved


func slide_west() -> bool:
	var moved = false
	for i in range(0, n):
		for j in range(1, m):
			if grid[i][j] == 'O' and grid[i][j-1] == '.':
				grid[i][j] = '.'
				grid[i][j-1] = 'O'
				moved = true
				if i == track.x and j == track.y:
					track = Vector2i(i, j-1)
	return moved




func slide_east() -> bool:
	var moved = false
	for i in range(0, n):
		for j in range(m-2, -1, -1):
			if grid[i][j] == 'O' and grid[i][j+1] == '.':
				grid[i][j] = '.'
				grid[i][j+1] = 'O'
				moved = true
				if i == track.x and j == track.y:
					track = Vector2i(i, j+1)
	return moved


func _draw() -> void:
	for j in range(0, m):
		for i in range(1, n):
			var rect = Rect2(
				Vector2(j, i - 0.5) * 10,
				Vector2(10, 10)
			)
			match grid[i][j]:
				'O':
					if i == track.x and j == track.y:
						draw_rect(rect, Color.WHITE)
					else:
						draw_rect(rect, sand)
				'#':
					draw_rect(rect, rock)
				'.':
					draw_rect(rect, air)


