package ngordnet.ngrams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordsGraph extends Graph<wordNode> {

    public Set<String> neighborWords(wordNode node) {

        Set<String> res = new HashSet<>(List.of(node.words()));
        if (get(node) != null) {
            for (wordNode Node: get(node)) {
                res.addAll(neighborWords(Node));
            }
        }

        return res;
    }


}