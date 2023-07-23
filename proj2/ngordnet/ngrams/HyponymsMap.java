package ngordnet.ngrams;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import edu.princeton.cs.algs4.In;

public class HyponymsMap {

    WordsGraph wordGraphs;

    public HyponymsMap(String synsets, String hyponyms) {

        In hyponym = new In(hyponyms);
        Graph<Integer> hyp = new Graph<>();
        while (!hyponym.isEmpty()) {
            String[] children = hyponym.readLine().split(",");
            int n = Integer.parseInt(children[0]);
            if (hyp.get(n) == null) {
                hyp.addNode(n);
            }
            for (int i = 1; i < children.length; i++) {
                hyp.addEdge(n, Integer.parseInt(children[i]));
            }
        }

        In synset = new In(synsets);
        Map<Integer, String> wordId = new HashMap<>();
        while (!synset.isEmpty()) {
            String[] stuff = synset.readLine().split(",");
            wordId.put(Integer.parseInt(stuff[0]), stuff[1]);
        }

        wordGraphs = new WordsGraph();
        for (int n: hyp.keySet()) {
            Set<wordNode> tmp = new HashSet<>();
            for (int i: hyp.get(n)) {
                tmp.add(new wordNode(i, wordId.get(i)));
            }
            wordGraphs.put(new wordNode(n, wordId.get(n)), tmp);
        }

    }

    public WordsGraph graph() { return wordGraphs; }
}
