/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Document;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.XMLUtil;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

public class TestView extends ViewPart implements Observer {
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.testview";

	private Action runTest;

	private TreeViewer viewer;

	private HashMap<String, TPEntity> mTPEntities;

	private void updateAction() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Iterator selectionIter = selection.iterator();
		while (selectionIter.hasNext()) {
			TPEntity treeEnt = (TPEntity) selectionIter.next();
			System.out.println("\n\nTestView: Selection "
					+ treeEnt.getName());
			/*
			if (treeObject instanceof TPTestEntity) {
				Hashtable<String, String> dictionary = ((TPTestEntity) treeObject)
						.getDictionary();
				TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_RESULT_TOPIC,
						dictionary);
				sendMsgToEventAdmin(tpEvent);
			}
			*/
		}

	}

	private void sendMsgToEventAdmin(TPEvent tpEvent) {
		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}

	private void createActions() {
		runTest.setEnabled(true);
		runTest.setImageDescriptor(Activator
				.getImageDescriptor("icons/runjunit.gif"));

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// updateAction();
			}
		});

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(runTest);

	}
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		Activator.getDefault().getEventAdminHandler().addObserver(this);

		mTPEntities = new HashMap<String, TPEntity>();

		runTest = new Action("Run...") {
			public void run() {
				updateAction();
			}
		};

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new TestContentProvider());
		viewer.setLabelProvider(new TestLabelProvider());
		viewer.setInput(null);
		getSite().setSelectionProvider(viewer);

		createActions();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			// final TPTestEntity tpEntity = mTPEntities.get(tpEvent.getID());

			System.out.println("TestView Got Update: " + tpEvent.getTopic());
			if (/*
				 * tpEntity != null &
				 */tpEvent.getTopic().equals(ITPBridge.TEST_EXEC_RESULT_TOPIC)) {
				
				System.out.println("TestView: update called for "
						+ tpEvent.getTopic() + " Event for "
						+ tpEvent.getTestName());
				/*
				String status = tpEvent.getStatus();
				String successPath = TreeObject.TEST_OK_IMAGE;
				if (status.indexOf("pass") < 0) {
					successPath = TreeObject.TEST_FAIL_IMAGE;
				}
				final TPTestEntity tpEntity = mTPEntities.get(tpEvent.getID());
				tpEntity.setStatus(tpEvent.getStatus(), successPath);
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						viewer.refresh(tpEntity, true);
						// viewer.update(tpEntity, null);
					}

				});
				*/
			} else if (tpEvent.getTopic().equalsIgnoreCase(
					ITPBridge.TEST_TREE_GET_RESP_TOPIC)) {
				String testTreeXML = tpEvent.getDictionary().get(
						TPEvent.TEST_TREE_XML_KEY);
				System.out.println(testTreeXML);
				final Document dom = XMLUtil.getDocFromXml(testTreeXML);

				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						//viewer.refresh(TestXML.getTPEntityFromDoc(dom), true);
						// viewer.update(tpEntity, null);
						viewer.setInput(TestXML.getTPEntityFromDoc(dom));
					}
				});
			}

		}

	}

	public void dispose() {
		super.dispose();
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
	}

}
