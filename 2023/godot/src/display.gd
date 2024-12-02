class_name Display extends Control

@onready var rows: Control = $rows
@onready var og_row: Control = $rows/row_0
@onready var og_col: Label = $rows/row_0/col_0


var n: int
var m: int


func set_pattern(pattern_obj: Pattern) -> void:
	n = pattern_obj.pattern.size()
	m = pattern_obj.pattern[0].size()

	for i in (m-1):
		var copy = og_col.duplicate(true)
		og_row.add_child(copy)

	for i in (n-1):
		var copy = og_row.duplicate(true)
		rows.add_child(copy)

	for i in n:
		for j in m:
			var cell: Label = rows.get_child(i).get_child(j)
			cell.text = "#" if (pattern_obj.pattern[i][j] == 1) else "."
			cell.get_child(0).color = Color.WHITE
#			cell.label_settings.font_color = Color.GREEN


func set_progress(index: int, vertical: bool, matching_values: Array) -> void:
	if vertical:
		for i in n:
			for j in index:
				rows.get_child(i).get_child(j).get_child(0).color = Color.CYAN

	for mv in matching_values:
		var label: Label = rows.get_child(mv[0]).get_child(mv[1])
		var label_settings = label.label_settings.duplicate()
		label_settings.font_color = Color.SPRING_GREEN
		label.label_settings = label_settings


