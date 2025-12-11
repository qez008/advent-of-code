package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.InputUtil;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Arrays;
import java.util.List;

class D11 extends PuzzleSolver<Graph<String, DefaultEdge>> {

    record Device(String id, List<String> outputs) {}

    @Override public Graph<String, DefaultEdge> parseInput() {
        var deviceList = InputUtil
                .getInput(11)
                .stream()
                .map(line -> {
                    var split = line.split(": ");
                    var node = split[0];
                    var outputs = Arrays.asList(split[1].split(" "));
                    return new Device(node, outputs);
                })
                .toList();

        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        graph.addVertex("out");

        for (var device : deviceList) {
            graph.addVertex(device.id);
        }
        for (var device : deviceList) {
            for (var output : device.outputs) {
                graph.addEdge(device.id, output);
            }
        }
        return graph;
    }

    @Override public long part1(Graph<String, DefaultEdge> graph) {
        var pathFinder = new AllDirectedPaths<>(graph);
        return pathFinder.getAllPaths("you", "out", true, null).size();
    }

    // 23805145 too low
    @Override public long part2(Graph<String, DefaultEdge> graph) {
        var pathFinder = new AllDirectedPaths<>(graph);
        var pathsToFft = (long) pathFinder.getAllPaths("svr", "fft", true, null).size();
        var pathsToDac = (long) pathFinder.getAllPaths("fft", "dac", true, 100).size();
        var pathsToOut = (long) pathFinder.getAllPaths("dac", "out", true, null).size();
        return pathsToFft * pathsToDac * pathsToOut;
    }
}
