package adventofcode.solutions;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
class Day9 {

    private String parse(String input) {
        try {
            return Files.readString(Path.of(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    long one(String input) {
        var string = parse(input);
        var expandedList = expand(string);
        var relocatedList = relocate(expandedList);
        return io.vavr.collection.List
                .ofAll(relocatedList)
                .zipWithIndex()
                .filter(indexedValue -> indexedValue.apply((value, index) -> value != -1))
                .map(indexedValue -> indexedValue.apply((value, index) -> value * index))
                .reduce(Long::sum);
    }

    List<Long> expand(String input) {
        var expandedList = new ArrayList<Long>();
        var isFreeSpace = false;
        long id = 0L;
        for (var c : input.toCharArray()) {
            long symbol = isFreeSpace ? -1L : id;
            var number = Integer.parseInt("" + c);
            for (var i = 0; i < number; i++) {
                expandedList.add(symbol);
            }
            if (isFreeSpace) {
                id += 1;
                isFreeSpace = false;
            } else {
                isFreeSpace = true;
            }
        }
        return expandedList;
    }

    List<Long> relocate(List<Long> expanded) {
        var relocatedList = new ArrayList<>(expanded);
        var start = 0;
        var end = relocatedList.size() - 1;
        while (start <= end) {
            // find the next free slot:
            while (start < end && relocatedList.get(start) != -1) {
                start++;
            }
            // find the next last element to move:
            while (end > start && relocatedList.get(end) == -1) {
                end--;
            }
            if (end <= start) {
                break;
            }
            relocatedList.set(start, relocatedList.get(end));
            relocatedList.set(end, -1L);
        }

        return relocatedList;
    }

    String joinList(List<Long> integers) {
        var characters = integers.stream().map(c -> c == -1 ? "." : c).toList();
        return (Joiner.on("").join(characters));
    }

    long two(String input) {
        var string = parse(input);
        var diskSpaces = createDiskMap(string);
        var relocatedList = relocateWholeFiles(diskSpaces);
        return checksum(relocatedList);
    }

    sealed interface DiskSpace {

        long size();

        default String string() {
            return switch (this) {
                case File(int id, long size) -> Integer.toString(id).repeat((int) size);
                case Free(long size) -> ".".repeat((int) size);
            };
        }

        record Free(long size) implements DiskSpace {
            Free shrink(DiskSpace diskSpace) {
                return new Free(this.size() - diskSpace.size());
            }
        }

        record File(int id, long size) implements DiskSpace {
            Free free() {
                return new Free(this.size());
            }
        }
    }

    private List<DiskSpace> createDiskMap(String input) {
        var diskSpaces = new ArrayList<DiskSpace>();
        var isFreeSpace = false;
        int id = 0;
        for (var c : input.toCharArray()) {
            var size = Integer.parseInt("" + c);
            if (isFreeSpace) {
                diskSpaces.add(new DiskSpace.Free(size));
                id += 1;
                isFreeSpace = false;
            } else {
                diskSpaces.add(new DiskSpace.File(id, size));
                isFreeSpace = true;
            }
        }
        return diskSpaces;
    }

    List<DiskSpace> relocateWholeFiles(List<DiskSpace> diskSpaces) {
        var relocatedList = new ArrayList<>(diskSpaces);

        for (var freeIndex = 0; freeIndex < relocatedList.size(); freeIndex++) {
            for (var fileIndex = relocatedList.size() - 1; fileIndex > freeIndex; fileIndex--) {
                if (!(relocatedList.get(freeIndex) instanceof DiskSpace.Free space)) {
                    continue;
                }
                if (!(relocatedList.get(fileIndex) instanceof DiskSpace.File file)) {
                    continue;
                }
                if (space.size() < file.size()) {
                    continue;
                }
                relocatedList.set(fileIndex, file.free());
                relocatedList.set(freeIndex, space.shrink(file));
                relocatedList.add(freeIndex, file);
                break;
            }
        }
        return relocatedList;
    }

    long checksum(List<DiskSpace> list) {
        long sum = 0;
        int index = 0;
        for (var e : list) {
            sum += switch (e) {
                case DiskSpace.File file -> {
                    long fileSum = 0;
                    for (var i = 0; i < file.size(); i++) {
                        fileSum += (long) file.id() * (index + i);
                    }
                    index += (int) file.size;
                    yield fileSum;
                }
                case DiskSpace.Free free -> {
                    index += (int) free.size();
                    yield 0;
                }
            };
        }
        return sum;
    }
}
