package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ChartDataSetXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ProjectXML;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.BarChart;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.LineChart;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.PieChart;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

public class ReportView extends ViewPart implements Observer {

	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.reportview";

	private Action mGetPieChart;

	private Action mGetBarChart;

	private Action mGetLineChart;

	private Composite mParent;

	private ChartComposite mFrame;

	private JFreeChart mChart;

	public ReportView() {
		mGetPieChart = new Action("Get Proj Overview Chart") {
			public void run() {
				getPieAction();
			}
		};
		mGetBarChart = new Action("Get Proj User Chart") {
			public void run() {
				getBarAction();
			}
		};
		mGetLineChart = new Action("Get Proj Time Chart") {
			public void run() {
				getLineAction();
			}
		};
	}

	private void getPieAction() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				createAndShowPieChart();
				mParent.layout();
			}
		});
	}

	private void getBarAction() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				createAndShowBarChart();
				mParent.layout();
			}
		});
	}

	private void getLineAction() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				createAndShowLineChart();
				mParent.layout();
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		Activator.getDefault().getEventAdminHandler().addObserver(this);
		mParent = parent;
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		mParent.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		mParent.setLayoutData(data);
		createAndShowPieChart();
		createActions();
	}

	private void createActions() {

		mGetPieChart.setEnabled(true);
		mGetPieChart.setImageDescriptor(Activator
				.getImageDescriptor("icons/piecharticon.gif"));

		mGetBarChart.setEnabled(true);
		mGetBarChart.setImageDescriptor(Activator
				.getImageDescriptor("icons/chartselector.gif"));

		mGetLineChart.setEnabled(true);
		mGetLineChart.setImageDescriptor(Activator
				.getImageDescriptor("icons/linecharticon.gif"));

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(mGetPieChart);
		mgr.add(mGetBarChart);
		mgr.add(mGetLineChart);

	}

	private void createAndShowBarChart() {

		mChart = BarChart.getInstance().createChart();
		mFrame.setChart(mChart);
		mFrame.layout();
	}

	private void createAndShowLineChart() {

		mChart = LineChart.getInstance().createChart();
		mFrame.setChart(mChart);
		mFrame.layout();
	}

	private void createAndShowPieChart() {
		mChart = PieChart.getInstance().createChart();

		if (mFrame == null) {
			mFrame = new ChartComposite(mParent, SWT.NONE, mChart, true);

			GridData data = new GridData(GridData.FILL_BOTH);
			mFrame.setLayoutData(data);
			mFrame.pack(true);
		} else {
			mFrame.setChart(mChart);
			mFrame.layout();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void update(Observable observable, Object object) {

		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent
				&& ((TPEvent) object).getTopic().equals(
						ITPBridge.CHART_GET_DATA_RESP_TOPIC)) {
			TPEvent tpEvent = (TPEvent) object;
			System.out.println("ReportView: update called for "
					+ tpEvent.getTopic());

			String dataSetXML = tpEvent.getDictionary().get(
					TPEvent.CHART_DATASET_XML_KEY);
			ChartDataSet dataSet = ChartDataSetXML
					.getDataSetFromXML(dataSetXML);
			String chartType = tpEvent.getDictionary().get(
					ChartDataSet.CHART_TYPE);
			System.out.println("dataSetXML\n: " + dataSetXML);
			if (chartType.equalsIgnoreCase(ChartDataSet.BAR)) {
				// createAndShowPieChart(tpEvent);
			}
		}
	}
}
