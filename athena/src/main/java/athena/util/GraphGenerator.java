package athena.util;

import org.bson.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaIndexField;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GraphGenerator extends ApplicationFrame {
    String feature = null;

    public GraphGenerator(final String title) {
        super(title);
        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    public GraphGenerator(final String title, AthenaFeatures athenaFeatures, String feature) {
        super(title);
        this.feature = feature;
        final XYDataset dataset = createDatasetFromFeatureData(athenaFeatures, feature);
        final JFreeChart chart = createChart(dataset);
        chart.setTitle("");
        LegendTitle legend = (LegendTitle) chart.getLegend();
        chart.removeLegend();
        Font nwfont = new Font("Arial",1,12);
        legend.setItemFont(nwfont);
        legend.setPosition(RectangleEdge.TOP);
//        legend.setWidth(200);
        legend.setItemLabelPadding(new RectangleInsets(3, 3, 3, 3));
        legend.setHeight(10);
//        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
        XYTitleAnnotation ta = new XYTitleAnnotation(0.99, 0.98, legend, RectangleAnchor.TOP_RIGHT);
        ta.setMaxWidth(0.95);
//        chart.addLegend(legend);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainZeroBaselinePaint(Color.gray);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setDomainGridlineStroke(new BasicStroke(0.7f));
        plot.setRangeGridlinePaint(Color.gray);
        plot.setRangeGridlineStroke(new BasicStroke(0.7f));
        plot.setDomainMinorGridlinePaint(Color.black);
        plot.addAnnotation(ta);
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.black);
        renderer.setSeriesShape(0, ShapeUtilities.createDiamond(5));
        renderer.setSeriesPaint(1, Color.red);
        renderer.setSeriesShape(1, ShapeUtilities.createUpTriangle(5));
        renderer.setSeriesPaint(2, Color.blue);
        Shape shape  = new Ellipse2D.Double(-5.0,-5.0,10,10);
        renderer.setSeriesShape(2, shape);
        renderer.setShapesFilled(false);
//        renderer.setSeriesShapesVisible(1, false);

        //apply theme
//        StandardChartTheme.createJFreeTheme().apply(chart);

        plot.setRenderer(renderer);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setLabel(feature + " (K)");
        yAxis.setAxisLineVisible(false);
        yAxis.setTickUnit(new NumberTickUnit(50000));
        yAxis.setNumberFormatOverride(new ByteFormat());
        yAxis.setRange(new Range(0, 160000));
        plot.getRenderer().setBaseItemLabelsVisible(true);
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setAxisLineVisible(false);
        xAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss"));
        xAxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 3));
        xAxis.setLabelFont(new Font("Arial",1,12));
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(631, 381));
        chartPanel.setMouseZoomable(true, true);
        setContentPane(chartPanel);
        try { 
            ChartUtilities.saveChartAsPNG(new File("result.png"), chart, 631, 381);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Long convertObjectToLong(Object obj) {
        if (obj instanceof Integer) {
            Integer integer = (Integer) obj;
            return new Long(integer.longValue());
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Double) {
            return ((Double) obj).longValue();
        } else {
            return null;
        }
    }

    public String generatePartialKey(String key, String val) {
        return "[" + key + "-" + val + "]";
    }

    public XYDataset createDatasetFromFeatureData(AthenaFeatures athenaFeatures, String feature) {
        AthenaFeatureField athenaFeatureField = new AthenaFeatureField();
        if (!athenaFeatureField.isElements(feature)) {
            return null;
        }

//        Iterable<Document> documents = (Iterable<Document>) athenaFeatures.getFeatures();
        Iterable<Document> documentsResult = (Iterable<Document>) athenaFeatures.getFeatures();
        List<Document> documents = new ArrayList();
        for (Document doc : documentsResult) {
            if (((Document) doc.get("_id")).getLong(AthenaIndexField.SWITCH_DATAPATH_ID) == 3
                    || ((Document) doc.get("_id")).getLong(AthenaIndexField.SWITCH_DATAPATH_ID) == 6) {
                // documents.add(doc);
                
//                if (((Document) doc.get("_id")).getInteger(AthenaIndexField.MATCH_TCP_DST, 0) == 20
//                        || ((Document) doc.get("_id")).getInteger(AthenaIndexField.MATCH_TCP_SRC, 0) == 20) {
                    documents.add(doc);
//                }
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();


        //extract documents to data per aggregated Index!
        Date date_prev = null;
        List<TimeSeriesData> timeSeriesDataList = new ArrayList<>();
        TimeSeriesData timeSeriesData = null;
        for (Document doc : documents) {
            //Extract aggregated values
            Document innerAggregated = (Document) doc.get("_id");
            String key = null;
            Iterator innerAggratedIt = innerAggregated.entrySet().iterator();

            while (innerAggratedIt.hasNext()) {
                Map.Entry pair = (Map.Entry) innerAggratedIt.next();
                if (((String) pair.getKey()).startsWith(AthenaIndexField.TIMESTAMP)) {
                    continue;
                }
                if (key != null) {
                    key = key + generatePartialKey(pair.getKey().toString(), pair.getValue().toString());
                } else {
                    key = generatePartialKey(pair.getKey().toString(), pair.getValue().toString());
                }
            }
            Long data = convertObjectToLong(doc.get(feature));

            if (date_prev == null) {
                date_prev = (Date) innerAggregated.get(AthenaIndexField.TIMESTAMP);
                timeSeriesData = new TimeSeriesData(date_prev);
                timeSeriesData.insertdataEntry(key, data);
                continue;
            }

            if (date_prev.equals(innerAggregated.get(AthenaIndexField.TIMESTAMP))) {
                date_prev = (Date) innerAggregated.get(AthenaIndexField.TIMESTAMP);
                timeSeriesData.insertdataEntry(key, data);
            } else {
                date_prev = (Date) innerAggregated.get(AthenaIndexField.TIMESTAMP);
                timeSeriesDataList.add(timeSeriesData);
                timeSeriesData = new TimeSeriesData(date_prev);
                timeSeriesData.insertdataEntry(key, data);
            }
        }


        TimeSeries series = null;        //Create Series!
        HashMap<String, TimeSeries> individualSeries = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < timeSeriesDataList.size(); i++) {
            TimeSeriesData entry = timeSeriesDataList.get(i);
            Date date = entry.getDate();
            cal.setTime(date);
            Second dateSecond = new Second(cal.get(Calendar.SECOND), cal.get(Calendar.MINUTE),
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
            HashMap<String, Long> elements = entry.getDataEntry();


            Iterator it = elements.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String key = (String) pair.getKey();
                Long value = (Long) pair.getValue();
                if (individualSeries.containsKey(key)) {
                    series = individualSeries.get(key);
                    series.addOrUpdate(dateSecond, value);
                    individualSeries.put(key, series);
                } else {
                    series = new TimeSeries(key);
                    series.addOrUpdate(dateSecond, value);
                    individualSeries.put(key, series);
                }
            }
        }

        //addSeries
        Iterator it = individualSeries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            dataset.addSeries((TimeSeries) pair.getValue());
        }


        return dataset;
    }


    public XYDataset createDataset() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        TimeSeries series = new TimeSeries("Random Data");
        Second current = new Second();
        double value = 100.0;
        for (int i = 0; i < 4000; i++) {
            try {
                value = value + Math.random() - 0.5;
                series.add(current, new Double(value));
                current = (Second) current.next();
            } catch (SeriesException e) {
                System.err.println("Error adding to series");
            }
        }
        dataset.addSeries(series);

        return dataset;
    }
    /*
    private XYDataset createDataset() {
        TimeSeries series1 = new TimeSeries("Data");
        Date date = new Date();
        series1.add(new Day(date),46.6);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        return dataset;
    }
    */


    public JFreeChart createChart(final XYDataset dataset) {
        return ChartFactory.createTimeSeriesChart(
                feature,
                "Timestamp",
                "Value",
                dataset,
                true,
                false,
                false);
    }
    
    /**
    * A formatter that displays bytes.
    */
    public class ByteFormat extends NumberFormat {
        private static final long serialVersionUID = 1;

        /** Bytes. */
        private static final String B = "";

        /** Kilobytes. */
        private static final String KB = "K";

        /** Megabytes. */
        private static final String MB = "M";

        /** Gigabytes. */
        private static final String GB = "G";

        /** The bytes. */
        public final String[] BYTES = {
            B, KB, MB, GB
        };

        /**
         * Creates a new formatter.
         */
        public ByteFormat() {
            super();
        }

        public int computeIndex(double bytes) {
            int index = 0;

            for (int i = 0; i < BYTES.length; i++) {
                int result = (int)(bytes / 1000);
                if (result == 0) {
                    break;
                } else {
                    bytes /= 1000;
                    index++;
                }
            }
            
            return index;
        }

        /**
         * Returns a string representing the bytes.
         *
         * @param bytes  the number of bytes.
         *
         * @return A string.
         */
        public String getBytes(double bytes) {

            int index = computeIndex(bytes);

            for (int i = 0; i < index; i++) {
                bytes /= 1000;
            }

            String str;
            if ((bytes % 1) == 0) {
                str = String.valueOf(Math.round(bytes));
            } else {
                str = String.valueOf(bytes);
            }

            return str;

        }

        /**
         * Formats a number into the specified string buffer.
         *
         * @param number  the number to format.
         * @param toAppendTo  the string buffer.
         * @param pos  the field position (ignored here).
         *
         * @return The string buffer.
         */
        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo,
                                   FieldPosition pos) {
            return toAppendTo.append(getBytes(number));
        }

        /**
         * Formats a number into the specified string buffer.
         *
         * @param number  the number to format.
         * @param toAppendTo  the string buffer.
         * @param pos  the field position (ignored here).
         *
         * @return The string buffer.
         */
        @Override
        public StringBuffer format(long number, StringBuffer toAppendTo,
                                   FieldPosition pos) {
            return toAppendTo.append(getBytes(number));
        }

        /**
         * This method returns <code>null</code> for all inputs.  This class cannot
         * be used for parsing.
         *
         * @param source  the source string.
         * @param parsePosition  the parse position.
         *
         * @return <code>null</code>.
         */
        @Override
        public Number parse(String source, ParsePosition parsePosition) {
            return null;
        }
    }
}