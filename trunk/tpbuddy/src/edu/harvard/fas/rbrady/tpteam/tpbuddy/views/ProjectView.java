/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
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

public class ProjectView extends ViewPart implements Observer {

	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.projectview";

	private Table mTable;

	private TableViewer mTableViewer;

	private TableUpdater mTableUpdater;

	private TPBridgeClient mTPBridgeClient;

	public ProjectView() {
		mTPBridgeClient = Activator.getDefault().getTPBridgeClient();
		Activator.getDefault().getEventAdminHandler().addObserver(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		initTableViewer(parent);
		mTableUpdater = new TableUpdater(mTableViewer);
	}

	private void initTableViewer(Composite parent) {
		mTableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		initTable();
		initColumns();

		mTableViewer.setLabelProvider(new TPEventLabelProvider());
		mTableViewer.setContentProvider(new ArrayContentProvider());
		mTableViewer.setInput(getTPBridgeEvents());
		// mTableViewer.setInput(TPEvent.getExamples());

	}

	private void initTable() {
		mTable = mTableViewer.getTable();
		mTable.setHeaderVisible(true);
		mTable.setLinesVisible(true);
	}

	private void initColumns() {
		String[] columnNames = new String[] { "Name", "Description"};
		int[] columnWidths = new int[] { 150, 300 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT};
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(mTable,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

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
			//mTableViewer.add(mTableViewerObject);
		}
	}

	private ArrayList<TPEvent> getTPBridgeEvents() {
		return mTPBridgeClient.getEventLog();
	}

	public void dispose() {
		super.dispose();
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
	}

	public void update(Observable observable, Object object) {
		/*
		if (observable instanceof EventAdminHandler && object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			System.out.println("EventHistoryView: update called for "
					+ tpEvent.getTopic() + " Event for "
					+ tpEvent.getTestName());
			mTableUpdater.insertObject(tpEvent);
		}
		*/
	}
}
