package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

public class BarChart extends AbstractChart{
	
	
	private static BarChart mBarChart = null;

	private BarChart() {
	}

	public static synchronized BarChart getInstance() {
		if (mBarChart == null)
			mBarChart = new BarChart();
		return mBarChart;
	}


   /**
    * Returns a sample dataset.
    *
    * @return The dataset.
    */
   protected AbstractDataset createDataset() {

      // row keys...
      String series1 = PASS;
      String series2 = FAIL;
      String series3 = ERR;
      String series4 = INCONCL;

      // column keys...
      String user1 = "User 1";
      String user2 = "User 2";
      String user3 = "User 3";
      String user4 = "User 4";

      // create the dataset...
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      dataset.addValue(1.0, series1, user1);
      dataset.addValue(4.0, series1, user2);
      dataset.addValue(3.0, series1, user3);
      dataset.addValue(5.0, series1, user4);

      dataset.addValue(5.0, series2, user1);
      dataset.addValue(7.0, series2, user2);
      dataset.addValue(6.0, series2, user3);
      dataset.addValue(8.0, series2, user4);

      dataset.addValue(4.0, series3, user1);
      dataset.addValue(3.0, series3, user2);
      dataset.addValue(2.0, series3, user3);
      dataset.addValue(3.0, series3, user4);
      
      dataset.addValue(1.0, series4, user1);
      dataset.addValue(3.0, series4, user2);
      dataset.addValue(5.0, series4, user3);
      dataset.addValue(3.0, series4, user4);

      return dataset;

   }

   /**
    * Creates a sample chart.
    *
    * @param dataset
    *            the dataset.
    *
    * @return The chart.
    */
   public JFreeChart createChart() {

      CategoryDataset dataset = (CategoryDataset)createDataset();

      // create the chart...
      JFreeChart chart = ChartFactory.createBarChart("Project Users Overview", // title
            "User", // domain axis label
            "Number of Tests", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            true, // tooltips
            false // URLs
            );

      // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

      // set the background color for the chart...
      chart.setBackgroundPaint(Color.white);

      // get a reference to the plot for further customisation...
      CategoryPlot plot = chart.getCategoryPlot();
      plot.setBackgroundPaint(Color.lightGray);
      plot.setDomainGridlinePaint(Color.white);
      plot.setDomainGridlinesVisible(true);
      plot.setRangeGridlinePaint(Color.white);

      // set the range axis to display integers only...
      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // disable bar outlines...
      BarRenderer renderer = (BarRenderer) plot.getRenderer();
      renderer.setSeriesPaint(0, GREEN);
      renderer.setSeriesPaint(1, RED);
      renderer.setSeriesPaint(2, YELLOW);
      renderer.setSeriesPaint(3, BLUE);
      renderer.setDrawBarOutline(false);

      CategoryAxis domainAxis = plot.getDomainAxis();
      domainAxis.setCategoryLabelPositions(CategoryLabelPositions
            .createUpRotationLabelPositions(Math.PI / 6.0));
      // OPTIONAL CUSTOMISATION COMPLETED.

      return chart;

   }
}
