/********************************************************************
 * 
 * File		:	EventHistoryView.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides a view of all TPEvents received by TPBuddy
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge.TPBridgeClient;

/*******************************************************************************
 * File 		: 	EventHistoryView.java
 * 
 * Description 	: 	Provides a view of all TPEvents received by TPBuddy
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class EventHistoryView extends ViewPart implements Observer {
	/** The view ID */
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.eventhistview";
	/** The encapsulated SWT Table used */
	private Table mTable;
	/** The TableViewer that renders the TPEvents */
	private TableViewer mTableViewer;
	/** Helper class for updating view */
	private TableUpdater mTableUpdater;
	/** Reference to the TPBuddy TPBridge client */
	private TPBridgeClient mTPBridgeClient;

	/**
	 * Constructor
	 * 
	 * Adds this view as an observer to the
	 * TPBuddy EventAdminHandler and gets a handle to
	 * the TPBuddy TPBridge client
	 */
	public EventHistoryView() {
		mTPBridgeClient = Activator.getDefault().getTPBridgeClient();
		Activator.getDefault().getEventAdminHandler().addObserver(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		initTableViewer(parent);
		mTableUpdater = new TableUpdater(mTableViewer);
	}

	/**
	 * Initializes the TableViewer
	 * @param parent the Composite parent of the view
	 */
	private void initTableViewer(Composite parent) {
		mTableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		initTable();
		initColumns();
		mTableViewer.setLabelProvider(new TPEventLabelProvider());
		mTableViewer.setContentProvider(new ArrayContentProvider());
		mTableViewer.setInput(getTPBridgeEvents());
	}

	private void initTable() {
		mTable = mTableViewer.getTable();
		mTable.setHeaderVisible(true);
		mTable.setLinesVisible(true);
	}

	/**
	 * Initializes column display properties
	 */
	private void initColumns() {
		String[] columnNames = new String[] { "From", "To", "Topic", "Project", "Test", "ID", "Status" };
		int[] columnWidths = new int[] { 150, 150, 150, 150, 150, 150, 150 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT,
				SWT.LEFT, SWT.LEFT, SWT.LEFT };
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(mTable,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

	}

	@Override
	public void setFocus() {
	}

	/**
	 * Helper class
	 * 
	 * Adds TPEvents to the view's table as they 
	 * are received in real time.
	 *
	 */
	private static class TableUpdater implements Runnable {

		private Object mTableViewerObject = null;

		private TableViewer mTableViewer;

		public TableUpdater(TableViewer tableViewer) {
			mTableViewer = tableViewer;
		}

		public void insertObject(Object objectToInsert) {
			mTableViewerObject = objectToInsert;
			Display.getDefault().syncExec(this);
		}

		public void run() {
			mTableViewer.add(mTableViewerObject);
		}
	}

	private ArrayList<TPEvent> getTPBridgeEvents() {
		return mTPBridgeClient.getEventLog();
	}

	public void dispose() {
		super.dispose();
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
	}

	/**
	 * Update called when TPBuddy EventAdminHandler receives 
	 * a TPTeam event.  All received TPEvents will be added
	 * to the view's table
	 * 
	 * @param observable the object that called the update
	 * @param object the TPEvent to be handled
	 */
	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler && object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			System.out.println("EventHistoryView: update called for "
					+ tpEvent.getTopic() + " Event for "
					+ tpEvent.getTestName());
			mTableUpdater.insertObject(tpEvent);
		}
	}
}
