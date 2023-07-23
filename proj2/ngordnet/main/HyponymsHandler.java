package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.HyponymsMap;
import ngordnet.ngrams.NGramMap;

import java.util.*;


public class HyponymsHandler extends NgordnetQueryHandler {

    HyponymsMap hyponymsMap;

    NGramMap nGramMap;

    public HyponymsHandler(HyponymsMap Hmap, NGramMap Nmap){
        hyponymsMap = Hmap;
        nGramMap = Nmap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        Set<String> res = new TreeSet<>();

        for (int i = 0; i < words.size(); i++) {
            Set<String> tmp = new TreeSet<>();
            for (var node: hyponymsMap.graph().keySet()) {
                if (node.contain(words.get(i))) {
                    tmp.addAll(hyponymsMap.graph().neighborWords(node));
                }
            }
            res = i == 0? tmp : res;
            res.retainAll(tmp);
        }

        int k = q.k();
        if (k != 0) {
            TreeMap<Double, List<String>> mp = new TreeMap<>();
            for (String word: res) {
                double sum = 0;
                if (nGramMap.contains(word)) {
                    for (Double weight : nGramMap.countHistory(word, q.startYear(), q.endYear()).data()) {
                        sum -= weight;
                    }
                }
                List<String> Words = mp.getOrDefault(sum, new ArrayList<>());
                Words.add(word);
                mp.put(sum, Words);
            }

            res.clear();

            for (List<String> Words: mp.values()) {
                for (String word: Words) {
                    if (k-- > 0) {
                        res.add(word);
                    } else {
                        return res.toString();
                    }
                }
            }

        }

        return res.toString();
    }

}
