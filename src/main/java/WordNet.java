import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private Digraph digraph;

    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/synsets.txt", "/hypernyms.txt");
    }

    public WordNet(String synsetFilePath, String hypernymFilePath) {

        In inputReader = new In(synsetFilePath);
        int vertexCounter = 0;
        while (inputReader.hasNextLine()) {
            String line = inputReader.readLine();
            String[] lineParts = line.split(",");
            String[] synsetArray = lineParts[1].split(" ");

            int vertexNumber = Integer.parseInt(lineParts[0]);

            Set<String> synset = new HashSet<>(Arrays.asList(synsetArray));


            Map<Integer, Set<String>> synsetWordMap = new HashMap<>();
            Map<Integer, Set<Integer>> synsetVertexMap = new HashMap<>();
            synsetWordMap.putIfAbsent(vertexNumber, synset);
            vertexCounter++;

        }

        digraph = new Digraph(vertexCounter);

        inputReader = new In(hypernymFilePath);
        while (inputReader.hasNextLine()) {
            String line = inputReader.readLine();
            String[] lineElements = line.split(",");
        }
    }
}
