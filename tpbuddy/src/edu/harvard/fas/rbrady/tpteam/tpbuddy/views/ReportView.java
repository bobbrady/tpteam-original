package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ChartDataSetXML;
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

	private String mProjID;

	private String mProjName;

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
		Shell parent = getViewSite().getShell();
		if (mProjID == null || mProjID.equalsIgnoreCase("")
				|| Integer.parseInt(mProjID) < 1) {
			MessageDialog
					.openError(
							parent,
							"Report View Error",
							"A Project must first Be selected in the Project View, then click on the \"Get Proj Reports\" icon.");
			return;
		}

		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		dictionary.put(TPEvent.PROJECT_ID_KEY, mProjID);
		dictionary.put(TPEvent.PROJECT_KEY, mProjName);
		dictionary.put(ChartDataSet.CHART_TYPE, ChartDataSet.PIE);

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.CHART_GET_DATA_REQ_TOPIC, dictionary);
	}

	private void getBarAction() {
		Shell parent = getViewSite().getShell();
		if (mProjID == null || mProjID.equalsIgnoreCase("")
				|| Integer.parseInt(mProjID) < 1) {
			MessageDialog
					.openError(
							parent,
							"Report View Error",
							"A Project must first Be selected in the Project View, then click on the \"Get Proj Reports\" icon.");
			return;
		}

		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		dictionary.put(TPEvent.PROJECT_ID_KEY, mProjID);
		dictionary.put(TPEvent.PROJECT_KEY, mProjName);
		dictionary.put(ChartDataSet.CHART_TYPE, ChartDataSet.BAR);

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.CHART_GET_DATA_REQ_TOPIC, dictionary);
	}

	private void getLineAction() {
		Shell parent = getViewSite().getShell();
		if (mProjID == null || mProjID.equalsIgnoreCase("")
				|| Integer.parseInt(mProjID) < 1) {
			MessageDialog
					.openError(
							parent,
							"Report View Error",
							"A Project must first Be selected in the Project View, then click on the \"Get Proj Reports\" icon.");
			return;
		}
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		dictionary.put(TPEvent.PROJECT_ID_KEY, mProjID);
		dictionary.put(TPEvent.PROJECT_KEY, mProjName);
		dictionary.put(ChartDataSet.CHART_TYPE, ChartDataSet.LINE);

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.CHART_GET_DATA_REQ_TOPIC, dictionary);

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

		mProjID = Activator.getDefault().getProjID();
		mProjName = Activator.getDefault().getProjName();

		if (mProjID != null && !mProjID.equalsIgnoreCase("")
				&& Integer.parseInt(mProjID) > 0)
			getPieAction();

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

	private void createAndShowBarChart(ChartDataSet[] dataSets) {
		mChart = BarChart.getInstance().createChart(dataSets, mProjName);
		mFrame.setChart(mChart);
		mFrame.layout();
	}

	private void createAndShowLineChart(ChartDataSet dataSet) {
		mChart = LineChart.getInstance().createChart(new ChartDataSet[]{dataSet}, mProjName);
		mFrame.setChart(mChart);
		mFrame.layout();
	}

	private void createAndShowPieChart(ChartDataSet dataSet) {
		mChart = PieChart.getInstance().createChart(new ChartDataSet[]{dataSet}, mProjName);

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
				&& object instanceof TPEvent) {
			if (((TPEvent) object).getTopic().equals(
					ITPBridge.CHART_GET_DATA_RESP_TOPIC)) {
				TPEvent tpEvent = (TPEvent) object;
				System.out.println("ReportView: update called for "
						+ tpEvent.getTopic());

				String dataSetXML = tpEvent.getDictionary().get(
						TPEvent.CHART_DATASET_XML_KEY);
				String chartType = tpEvent.getDictionary().get(
						ChartDataSet.CHART_TYPE);
				System.out.println("dataSetXML\n: " + dataSetXML);

				if (chartType.equalsIgnoreCase(ChartDataSet.PIE)) {
					final ChartDataSet dataSet = ChartDataSetXML
							.getDataSetFromXML(dataSetXML);
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							createAndShowPieChart(dataSet);
							mParent.layout();
						}
					});
				} else if (chartType.equalsIgnoreCase(ChartDataSet.BAR)) {
					final ChartDataSet[] dataSets = ChartDataSetXML
							.getDataSetsFromXML(dataSetXML);
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							createAndShowBarChart(dataSets);
							mParent.layout();
						}
					});
				} else if (chartType.equalsIgnoreCase(ChartDataSet.LINE)) {
					final ChartDataSet dataSet = ChartDataSetXML
							.getDataSetFromXML(dataSetXML);
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							createAndShowLineChart(dataSet);
							mParent.layout();
						}
					});
				}
			} else if (((TPEvent) object).getTopic().equals(
					ITPBridge.CHART_GET_DATA_REQ_TOPIC)) {
				TPEvent tpEvent = (TPEvent) object;
				mProjID = tpEvent.getDictionary().get(TPEvent.PROJECT_ID_KEY);
				mProjName = tpEvent.getDictionary().get(TPEvent.PROJECT_KEY);
				Activator.getDefault().setProjID(mProjID);
				Activator.getDefault().setProjName(mProjName);
			} else if (((TPEvent) object).getTopic().equals(
					ITPBridge.TEST_TREE_GET_REQ_TOPIC)) {
				TPEvent tpEvent = (TPEvent) object;
				mProjID = tpEvent.getDictionary().get(TPEvent.PROJECT_ID_KEY);
				mProjName = tpEvent.getDictionary().get(TPEvent.PROJECT_KEY);
				Activator.getDefault().setProjID(mProjID);
				Activator.getDefault().setProjName(mProjName);
			}
		}
	}
}
