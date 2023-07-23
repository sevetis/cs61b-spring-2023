package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    // TODO: Add any necessary static/instance variables.
    private HashMap<String, TimeSeries> words;

    private TimeSeries countsOfYear;
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        words = new HashMap<>();
        countsOfYear = new TimeSeries();
        In wordsIn = new In(wordsFilename);
        In countsIn = new In(countsFilename);


        int year, count;
        while (!countsIn.isEmpty()) {
            String stuff = countsIn.readLine();
            String[] stuffs = stuff.split(",");
            countsOfYear.put(Integer.parseInt(stuffs[0]), Double.parseDouble(stuffs[1]));
        }

        String word;
        while (!wordsIn.isEmpty()) {
            word = wordsIn.readString();
            year = wordsIn.readInt();
            count = wordsIn.readInt();
            wordsIn.readInt();

            TimeSeries wordInfo = words.getOrDefault(word, new TimeSeries());
            wordInfo.put(year, count * 1.0);
            words.put(word, wordInfo);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        return new TimeSeries(words.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        // TODO: Fill in this method.
        return new TimeSeries(words.get(word), MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        return new TimeSeries(countsOfYear, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
//        System.out.println(word);
//        System.out.println(words.get(word));
//        System.out.println(words.get("response"));
//        System.out.println(word.equals("response"));
        return new TimeSeries(words.get(word).dividedBy(countsOfYear), startYear, endYear);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries res = new TimeSeries();
        for (String word: words) {
            res = res.plus(weightHistory(word, startYear, endYear));
        }
        return res;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        TimeSeries res = new TimeSeries();
        for (String word: words) {
            res = res.plus(weightHistory(word));
        }
        return res;
    }

    public boolean contains(String word) {
        return words.get(word) != null;
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
