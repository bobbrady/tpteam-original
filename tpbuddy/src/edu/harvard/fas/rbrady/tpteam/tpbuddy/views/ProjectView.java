/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.Hashtable;
import java.util.Iterator;
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
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ProjectXML;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

public class ProjectView extends ViewPart implements Observer {

	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.projectview";

	private Table mTable;

	private TableViewer mTableViewer;

	private TableUpdater mTableUpdater;

	private Action mGetTestTree;

	public ProjectView() {
		Activator.getDefault().getEventAdminHandler().addObserver(this);
		mGetTestTree = new Action("Get Proj TestTree") {
			public void run() {
				updateAction();
			}
		};
	}

	private void updateAction() {
		IStructuredSelection selection = (IStructuredSelection) mTableViewer
				.getSelection();
		Iterator selectionIter = selection.iterator();
		while (selectionIter.hasNext()) {
			Project proj = (Project) selectionIter.next();
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
					.getTPBridgeClient().getTPMgrECFID());
			dictionary.put(TPEvent.FROM, Activator.getDefault()
					.getTPBridgeClient().getTargetIDName());
			dictionary
					.put(TPEvent.PROJECT_ID_KEY, String.valueOf(proj.getId()));
			showTestView();
			Activator.getDefault().getEventAdminClient().sendEvent(
					ITPBridge.TEST_TREE_GET_REQ_TOPIC, dictionary);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		initTableViewer(parent);
		mTableUpdater = new TableUpdater(mTableViewer);
		createActions();
		// showTestView(IWorkbenchPage.VIEW_VISIBLE);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

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

	private void createActions() {
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
		mgr.add(mGetTestTree);

	}

	private void initTableViewer(Composite parent) {
		mTableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		initTable();
		initColumns();

		mTableViewer.setLabelProvider(new ProjectLabelProvider());
		mTableViewer.setContentProvider(new ArrayContentProvider());
		// mTableViewer.setInput(getTPBridgeEvents());
		// mTableViewer.setInput(TPEvent.getExamples());

	}

	private void initTable() {
		mTable = mTableViewer.getTable();
		mTable.setHeaderVisible(true);
		mTable.setLinesVisible(true);
	}

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

	public void dispose() {
		super.dispose();
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
	}

	public void update(Observable observable, Object object) {

		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent
				&& ((TPEvent) object).getTopic().equals(
						ITPBridge.PROJ_GET_RESP_TOPIC)) {
			TPEvent tpEvent = (TPEvent) object;
			System.out.println("ProjectView: update called for "
					+ tpEvent.getTopic());

			List<Project> projs = ProjectXML.getProjsFromXML(tpEvent
					.getDictionary().get(TPEvent.PROJ_PROD_XML_KEY));

			for (Project proj : projs) {
				mTableUpdater.insertObject(proj);
			}
		}
	}
}
