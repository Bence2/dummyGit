import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class WordNet {

    private Digraph digraph;
    private Map<String, Set<Integer>> nounSynsetIdsMap;
    private SAP singleAncestorPath;
    private List<String> synsetIdIndexedNouns;

    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/synsets.txt", "/hypernyms.txt");

        int distance = wordNet.distance("worm", "bird");
        assert(distance == 5);
        String commonAncestor = wordNet.sap("worm", "bird");
        assert(commonAncestor.equals("animal animate_being beast brute creature fauna"));

        commonAncestor = wordNet.sap("individual", "edible_fruit");
        assert(commonAncestor.equals("physical_entity"));

        System.out.println("miau");
    }

    public WordNet(String synsetFilePath, String hypernymFilePath) {

        In inputReader = new In(synsetFilePath);
        int vertexCounter = 0;
        synsetIdIndexedNouns = new ArrayList<>();
        nounSynsetIdsMap = new HashMap<>();

        while (inputReader.hasNextLine()) {
            String line = inputReader.readLine();
            String[] lineParts = line.split(",");
            Integer synsetId = Integer.parseInt(lineParts[0]);

            String[] synsetArray = lineParts[1].split(" ");
            synsetIdIndexedNouns.add(lineParts[1]);
            for (String noun : synsetArray) {
               if (nounSynsetIdsMap.get(noun) == null) {
                   nounSynsetIdsMap.put(noun, new HashSet<>(Arrays.asList(synsetId)));
               } else {
                   nounSynsetIdsMap.get(noun).add(synsetId);
               }
            }
            vertexCounter++;

            // egy noun tartozhat több synsetbe, tehát amikor 2 noun közötti távolságot vesszük
            // akkor tulképp 2 synset halmazt kell átadni, hiszen 1 noun több synsetben lehet
            // tehát kell egy Map<Noun, Set<SynsetIds>>

        }

        digraph = new Digraph(vertexCounter);

        inputReader = new In(hypernymFilePath);
        while (inputReader.hasNextLine()) {
            String line = inputReader.readLine();
            String[] lineElements = line.split(",");
            Integer edgeFrom = Integer.parseInt(lineElements[0]);
            for (int i = 1; i < lineElements.length; i++) {
                digraph.addEdge(edgeFrom, Integer.parseInt(lineElements[i]));
            }
        }
        checkForRootedDag(digraph);
        singleAncestorPath = new SAP(digraph);
    }


    private void checkForRootedDag(Digraph digraph) {
        if (digraph.V() == 0) {
            return;
        }
        Iterator[] neighborVerticesIterator = new Iterator[digraph.V()];
        for (int i = 0; i < digraph.V(); i++) {
            neighborVerticesIterator[i] = digraph.adj(i).iterator();
        }

        boolean[] onStack = new boolean[digraph.V()];
        boolean[] isVisited = new boolean[digraph.V()];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < digraph.V(); i++) {
            if (!isVisited[i]) {
                stack.push(i);
                isVisited[i] = true;
                onStack[i] = true;

                while (!stack.isEmpty()) {
                    Integer currentVertex = stack.peek();
                    if (neighborVerticesIterator[currentVertex].hasNext()) {
                        Integer neighborVertex = (Integer) neighborVerticesIterator[currentVertex].next();

                        if (onStack[neighborVertex]) {
                            throw new IllegalArgumentException();
                        }
                        if (!isVisited[neighborVertex]) {
                            stack.push(neighborVertex);
                            isVisited[neighborVertex] = true;

                            onStack[neighborVertex] = true;
                        }
                    } else {
                        onStack[currentVertex] = false;
                        stack.pop();
                    }
                }
            }
        }

    }

    public Iterable<String> nouns() {
        return nounSynsetIdsMap.keySet();
    }

    public boolean isNoun(String word) {
        return nounSynsetIdsMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        Iterable<Integer> nounASynsetIds  = nounSynsetIdsMap.get(nounA);
        Iterable<Integer> nounBSynsetIds  = nounSynsetIdsMap.get(nounB);
        return singleAncestorPath.length(nounASynsetIds, nounBSynsetIds);
    }

    public String sap(String nounA, String nounB) {
        Iterable<Integer> nounASynsetIds  = nounSynsetIdsMap.get(nounA);
        Iterable<Integer> nounBSynsetIds  = nounSynsetIdsMap.get(nounB);
        Integer synsetId = singleAncestorPath.ancestor(nounASynsetIds, nounBSynsetIds);
        return synsetIdIndexedNouns.get(synsetId);
    }


}
