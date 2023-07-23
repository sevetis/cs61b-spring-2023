package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {

    NGramMap data;

    public HistoryHandler(NGramMap map) {
        data = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        List<TimeSeries> timeSeries = new ArrayList<>();
        int startYear = q.startYear();
        int endYear = q.endYear();

        for (String word: words) {
            timeSeries.add(data.weightHistory(word, startYear, endYear));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, timeSeries);
        return Plotter.encodeChartAsString(chart);
    }
}
