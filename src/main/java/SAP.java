import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

public class SAP {

    private Digraph digraph;
    Map<TwoVertex, VertexWithDistance> ancestorCache = new HashMap();

    public static void main(String args[]) {
        Digraph digraph = new Digraph(new In("/digraph1.txt"));
        SAP singleAncestorPath = new SAP(digraph);
        int vertex = singleAncestorPath.ancestor(7, 12);
        assert(vertex == 1);
        int length = singleAncestorPath.length(7, 12);
        assert(length == 5);

        vertex = singleAncestorPath.ancestor(7, 1);
        assert(vertex == 1);
        length = singleAncestorPath.length(7, 1);
        assert(length == 2);

        vertex = singleAncestorPath.ancestor(7, 2);
        assert(vertex == 0);
        length = singleAncestorPath.length(7, 2);
        assert(length == 4);

        vertex = singleAncestorPath.ancestor(2, 2);
        assert(vertex == 2);
        length = singleAncestorPath.length(2, 2);
        assert(length == 0);

        vertex = singleAncestorPath.ancestor(Arrays.asList(7,9), Arrays.asList(8, 11));
        assert(vertex == 3);
        length = singleAncestorPath.length(Arrays.asList(7,9), Arrays.asList(8, 11));
        assert(length == 2);

        vertex = singleAncestorPath.ancestor(Arrays.asList(7,4), Arrays.asList(5, 3));
        assert(vertex == 3);
        length = singleAncestorPath.length(Arrays.asList(7,4), Arrays.asList(5, 3));
        assert(length == 1);

        vertex = singleAncestorPath.ancestor(Arrays.asList(7,4), Arrays.asList(5, 12));
        assert(vertex == 1);
        length = singleAncestorPath.length(Arrays.asList(7,4), Arrays.asList(5, 12));
        assert(length == 2);

        System.out.println("vertex: " + vertex);
    }

    public SAP(Digraph digraph) {
        this.digraph = digraph;
    }

    public int ancestor(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        VertexWithDistance vertexWithDistance = ancestorHelper(verticesV, verticesW);
        return vertexWithDistance.vertex;
    }

    public int length(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        VertexWithDistance vertexWithDistance = ancestorHelper(verticesV, verticesW);
        return vertexWithDistance.distance;
    }

    private VertexWithDistance ancestorHelper(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        if (verticesV == null || verticesW == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        verticesV.forEach(vertex -> argumentChecker(vertex));
        verticesW.forEach(vertex -> argumentChecker(vertex));

        BreadthFirstDirectedPaths breadthFirstDirectedPathsV = new BreadthFirstDirectedPaths(digraph, verticesV);
        Queue<Integer> neighborQueue = new LinkedList<>();
        boolean[] visited = new boolean[digraph.V()];
        int[] distance = new int[digraph.V()];

        for (Integer vertex : verticesW) {
            neighborQueue.add(vertex);
            visited[vertex] = true;
            distance[vertex] = 0;
        }

        VertexWithDistance currentlyShortestVertexWithDistance = new VertexWithDistance(Integer.MAX_VALUE, -1);

        while (!neighborQueue.isEmpty()) {
            int currentVertex = neighborQueue.poll();
            if (breadthFirstDirectedPathsV.hasPathTo(currentVertex)) {
                int completeDistance = distance[currentVertex] + breadthFirstDirectedPathsV.distTo(currentVertex);
                VertexWithDistance vertexWithDistance = new VertexWithDistance(completeDistance, currentVertex);
                if (currentlyShortestVertexWithDistance.distance >= vertexWithDistance.distance) {
                    currentlyShortestVertexWithDistance = vertexWithDistance;
                }
            } else {
                for (Integer neighborVertex : digraph.adj(currentVertex)) {
                    if (!visited[neighborVertex]) {
                        visited[neighborVertex] = true;
                        neighborQueue.add(neighborVertex);
                        distance[neighborVertex] = distance[currentVertex] + 1;
                    }
                }
            }
        }
        return currentlyShortestVertexWithDistance;
    }


    public int length(int v, int w) {
        TwoVertex twoVertex = new TwoVertex(v, w);
        VertexWithDistance cachedVertexWithDistance = ancestorCache.get(twoVertex);
        if (cachedVertexWithDistance != null) {
            return cachedVertexWithDistance.distance;
        } else {
            VertexWithDistance vertexWithDistance = ancestorHelper(v, w);
            ancestorCache.put(twoVertex, vertexWithDistance);
            return vertexWithDistance.distance;
        }
    }

    public int ancestor(int v, int w) {
        TwoVertex twoVertex = new TwoVertex(v, w);
        VertexWithDistance cachedVertexWithDistance = ancestorCache.get(twoVertex);
        if (cachedVertexWithDistance != null) {
            return cachedVertexWithDistance.vertex;
        } else {
            VertexWithDistance vertexWithDistance = ancestorHelper(v, w);
            ancestorCache.put(twoVertex, vertexWithDistance);
            return vertexWithDistance.vertex;
        }
    }

    private void argumentChecker(int vertex) {
        if (!(vertex >= 0 && vertex <= digraph.V())) {
            throw new IllegalArgumentException("invalid argument");
        }
    }

    private VertexWithDistance ancestorHelper(int v, int w) {
        argumentChecker(v);
        argumentChecker(w);
        Queue<Integer> neighborQueue = new LinkedList<>();
        neighborQueue.add(v);
        Map<Integer, Integer> visitedVerticesWithLevelMap = new HashMap<>();

        int level = 0;
        Queue<Integer> newNeighborQueue = new LinkedList<>();
        while (!neighborQueue.isEmpty()) {
            int currentVertex = neighborQueue.poll();
            visitedVerticesWithLevelMap.put(currentVertex, level);
            for (Integer neighborVertex : digraph.adj(currentVertex)) {
                newNeighborQueue.add(neighborVertex);
            }
            if (neighborQueue.isEmpty()) {
                neighborQueue = newNeighborQueue;
                newNeighborQueue = new LinkedList<>();
                level++;
            }
        }

        neighborQueue.add(w);
        level = 0;
        VertexWithDistance commonAncestor = null;
        while (!neighborQueue.isEmpty()) {
            int currentVertex = neighborQueue.poll();
            Integer currentVertexesPreviousLevel = visitedVerticesWithLevelMap.get(currentVertex);
            if (currentVertexesPreviousLevel != null) {
                commonAncestor = new VertexWithDistance(level, currentVertex);
                commonAncestor.distance += currentVertexesPreviousLevel;
                break;
            }
            for (Integer neighborVertex : digraph.adj(currentVertex)) {
                newNeighborQueue.add(neighborVertex);
            }
            if (neighborQueue.isEmpty()) {
                neighborQueue = newNeighborQueue;
                newNeighborQueue = new LinkedList<>();
                level++;
            }
        }

        return commonAncestor;
    }

    private static class VertexWithDistance {
        int distance;
        int vertex;

        VertexWithDistance(int distance, int vertex) {
            this.distance = distance;
            this.vertex = vertex;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VertexWithDistance that = (VertexWithDistance) o;

            return vertex == that.vertex;
        }

        @Override
        public int hashCode() {
            return vertex;
        }
    }

    private static class TwoVertex {
        int v;
        int w;
        public TwoVertex(int v, int w) {
            this.v = v;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TwoVertex twoVertex = (TwoVertex) o;

            if (v != twoVertex.v) return false;
            return w == twoVertex.w;
        }

        @Override
        public int hashCode() {
            int result = v;
            result = 31 * result + w;
            return result;
        }
    }
}
