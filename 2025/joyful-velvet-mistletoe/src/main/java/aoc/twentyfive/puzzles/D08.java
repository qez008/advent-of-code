package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.InputUtil;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class D08 extends PuzzleSolver<List<D08.JunctionBox>> {

    private static final int N = 1000;
    
    @Override public List<JunctionBox> parseInput() {
        return InputUtil.getInput(8).stream().map(JunctionBox::of).toList();
    }

    @Override public long part1(List<JunctionBox> junctionBoxes) {
        Graph<JunctionBox, DefaultEdge> graph = buildGraph(junctionBoxes);
        
        var inspector = new ConnectivityInspector<>(graph);
        for (var edge : allEdges(junctionBoxes).subList(0, N)) {
            if (!inspector.pathExists(edge.u, edge.v)) {
                graph.addEdge(edge.u, edge.v);
                inspector = new ConnectivityInspector<>(graph);
            }
        }
        return productOfLargestCircuits(new ConnectivityInspector<>(graph).connectedSets());
    }

    @Override public long part2(List<JunctionBox> junctionBoxes) {
        Graph<JunctionBox, DefaultEdge> graph = buildGraph(junctionBoxes);
        
        var inspector = new ConnectivityInspector<>(graph);
        for (var edge : allEdges(junctionBoxes)) {
            if (!inspector.pathExists(edge.u, edge.v)) {
                graph.addEdge(edge.u, edge.v);
                inspector = new ConnectivityInspector<>(graph);
                if (inspector.connectedSets().size() == 1) {
                    IO.println(edge);
                    return edge.u.x * edge.v.x;
                }
            }
        }
        return -1L;
    }

    private Graph<JunctionBox, DefaultEdge> buildGraph(List<JunctionBox> junctionBoxes) {
        Graph<JunctionBox, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (var junctionBox : junctionBoxes) {
            graph.addVertex(junctionBox);
        }
        return graph;
    }

    private static List<Edge> allEdges(List<JunctionBox> junctionBoxes) {
        List<Edge> edges = new ArrayList<>();
        for (var i = 0; i < junctionBoxes.size(); i++) {
            for (var ii = i + 1; ii < junctionBoxes.size(); ii++) {
                var a = junctionBoxes.get(i);
                var b = junctionBoxes.get(ii);
                var distance = a.distanceSquared(b);
                edges.add(new Edge(a, b, distance));
            }
        }
        edges.sort(Comparator.comparing(Edge::dist));
        return edges;
    }
    
    private static long productOfLargestCircuits(List<Set<JunctionBox>> circuitSets) {
        var circuitSizes = circuitSets
                .stream()
                .map(set -> (long) set.size())
                .sorted(Comparator.comparingLong(Long::longValue).reversed())
                .toList();
        return circuitSizes
                .subList(0, Math.min(3, circuitSizes.size()))
                .stream()
                .peek(IO::println)
                .reduce(1L, (a, b) -> a * b);
    }

    public record JunctionBox(long x, long y, long z) {

        public double distanceSquared(JunctionBox other) {
            double dx = other.x - this.x;
            double dy = other.y - this.y;
            double dz = other.z - this.z;
            return dx * dx + dy * dy + dz * dz;
        }

        public static JunctionBox of(String csv) {
            var split = csv.split(",");
            return new JunctionBox(
                    Long.parseLong(split[0]),
                    Long.parseLong(split[1]),
                    Long.parseLong(split[2])
            );
        }

        @Override
        public @NotNull String toString() {
            return String.format("%d,%d,%d", x, y, z);
        }
    }

    public record Edge(JunctionBox u, JunctionBox v, double dist) {}
}
