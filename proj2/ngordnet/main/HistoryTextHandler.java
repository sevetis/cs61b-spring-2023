package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;

import java.util.List;
import ngordnet.ngrams.NGramMap;

public class HistoryTextHandler extends NgordnetQueryHandler {

    NGramMap data;

    HistoryTextHandler(NGramMap map) {
        data = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = "";
        for (String word: words) {
            response += word + ": " + data.weightHistory(word, startYear, endYear).toString() + "\n";
        }

        return response;
    }
}
