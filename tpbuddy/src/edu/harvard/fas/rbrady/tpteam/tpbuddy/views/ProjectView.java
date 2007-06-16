/********************************************************************
 * 
 * File		:	ProjectView.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides a view of the TPTeam projects available
 * 				to a user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ProjectXML;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

/*******************************************************************************
 * File 		: 	ProjectView.java
 * 
 * Description 	: 	Provides a view of the TPTeam projects available
 * 					to a user
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ProjectView extends ViewPart implements Observer {
	/** The view ID */
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.projectview";
	/** The encapsulated SWT Table */
	private Table mTable;
	/** The TableView that encapsulates the SWT Table */
	private TableViewer mTableViewer;
	/** Action for requesting a project's test tree */
	private Action mGetTestTree;
	/** Action for requesting a project's report charts */
	private Action mGetProjReports;

	/**
	 * Constructor
	 *
	 * Adds this view as an observer to the TPBuddy EventAdminHandler.
	 * Creates the test tree and report request Actions.
	 */
	public ProjectView() {
		Activator.getDefault().getEventAdminHandler().addObserver(this);
		mGetTestTree = new Action("Get Proj TestTree") {
			public void run() {
				getTestTreeAction();
			}
		};
		mGetProjReports = new Action("Get Proj Reports") {
			public void run() {
				getProjReportsAction();
			}
		};

	}

	/**
	 * Requests the test tree TPEntity from the TPManager for 
	 * the selected project in the view. 
	 */
	private void getTestTreeAction() {
		IStructuredSelection selection = (IStructuredSelection) mTableViewer
				.getSelection();
			Project proj = (Project) selection.getFirstElement();
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
					.getTPBridgeClient().getTPMgrECFID());
			dictionary.put(TPEvent.FROM, Activator.getDefault()
					.getTPBridgeClient().getTargetIDName());
			dictionary
					.put(TPEvent.PROJECT_ID_KEY, String.valueOf(proj.getId()));
			dictionary.put(TPEvent.PROJECT_KEY, proj.getName());
			System.out.println("Project Name: " + dictionary.get(TPEvent.PROJECT_KEY));
			showTestView();
			Activator.getDefault().getEventAdminClient().sendEvent(
					ITPBridge.TEST_TREE_GET_REQ_TOPIC, dictionary);

	}
	
	/**
	 * Requests the pie chart TPEntity from the TPManager for 
	 * the selected project in the view. 
	 */

	private void getProjReportsAction() {
		IStructuredSelection selection = (IStructuredSelection) mTableViewer
				.getSelection();
			Project proj = (Project) selection.getFirstElement();
			
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
					.getTPBridgeClient().getTPMgrECFID());
			dictionary.put(TPEvent.FROM, Activator.getDefault()
					.getTPBridgeClient().getTargetIDName());
			dictionary
					.put(TPEvent.PROJECT_ID_KEY, String.valueOf(proj.getId()));
			dictionary.put(TPEvent.PROJECT_KEY, proj.getName());
			dictionary.put(ChartDataSet.CHART_TYPE, ChartDataSet.PIE);
			showProjReportView();
			Activator.getDefault().getEventAdminClient().sendEvent(
					ITPBridge.CHART_GET_DATA_REQ_TOPIC, dictionary);
		
	}


	@Override
	public void createPartControl(Composite parent) {
		initTableViewer(parent);
		createActions();
	}

	@Override
	public void setFocus() {

	}

	/**
	 * Helper method that will give the test tree
	 * view focus in the tabbed view of TPBuddy
	 */
	private void showTestView() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			try {
				page.showView(TestView.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Helper method that will give the report
	 * view focus in the tabbed view of TPBuddy
	 */
	private void showProjReportView() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			try {
				page.showView(ReportView.ID);
			} catch (PartInitException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * Helper class that initializes Actions 
	 */
	private void createActions() {
		mGetProjReports.setEnabled(true);
		mGetProjReports.setImageDescriptor(Activator
				.getImageDescriptor("icons/report.gif"));
		mGetTestTree.setEnabled(true);
		mGetTestTree.setImageDescriptor(Activator
				.getImageDescriptor("icons/testhier.gif"));
		mTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						// updateAction();
					}
				});
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(mGetProjReports);
		mgr.add(mGetTestTree);

	}

	private void initTableViewer(Composite parent) {
		mTableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		initTable();
		initColumns();
		mTableViewer.setLabelProvider(new ProjectLabelProvider());
		mTableViewer.setContentProvider(new ArrayContentProvider());
	}

	private void initTable() {
		mTable = mTableViewer.getTable();
		mTable.setHeaderVisible(true);
		mTable.setLinesVisible(true);
	}

	/**
	 * Initializes the TableView column sizes, headers,
	 * and other properties
	 */
	private void initColumns() {
		String[] columnNames = new String[] { "ID", "Name", "Description",
				"Product" };
		int[] columnWidths = new int[] { 50, 150, 300, 150 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT,
				SWT.LEFT };
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(mTable,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

	}

	public void dispose() {
		super.dispose();
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
	}

	/**
	 * Update used to refresh view when a project/product get
	 * response is received
	 * 
	 * @param observable the object issuing the update
	 * @param object the TPEvent to be handled
	 */
	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent
				&& ((TPEvent) object).getTopic().equals(
						ITPBridge.PROJ_GET_RESP_TOPIC)) {
			TPEvent tpEvent = (TPEvent) object;
			System.out.println("ProjectView: update called for "
					+ tpEvent.getTopic());
			final List<Project> projs = ProjectXML.getProjsFromXML(tpEvent
					.getDictionary().get(TPEvent.PROJ_PROD_XML_KEY));
			System.out.println(tpEvent
					.getDictionary().get(TPEvent.PROJ_PROD_XML_KEY));
			for(Project proj : projs)
			{
				System.out.println("Proj Name: " + proj.getName() + ", Desc: " + proj.getDescription());
			}
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					mTableViewer.setInput(projs);
				}
			});
		}
	}
}
