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
import java.util.Hashtable;
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

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.model.TPTestEntity;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.model.TreeObject;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.model.TreeParent;

public class TestView extends ViewPart implements Observer {
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.testview";

	private Action exitAction;

	private TreeViewer viewer;

	private HashMap<String, TPTestEntity> mTPEntities;

	public TestView() {

		/***********************************************************************
		 * For demo purposes only! This should be done only after connection
		 * established with bridge via some sort of dialog
		 **********************************************************************/
		Activator.getDefault().getEventAdminHandler().addObserver(this);

		mTPEntities = new HashMap<String, TPTestEntity>();

		exitAction = new Action("Add...") {
			public void run() {
				updateAction();
			}
		};
	}

	private void updateAction() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Iterator selectionIter = selection.iterator();
		while (selectionIter.hasNext()) {
			TreeObject treeObject = (TreeObject) selectionIter.next();
			System.out.println("\n\nTestView: Selection "
					+ treeObject.getName());
			if (treeObject instanceof TPTestEntity) {
				Hashtable<String, String> dictionary = ((TPTestEntity) treeObject)
						.getDictionary();
				TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_RESULT_TOPIC,
						dictionary);
				sendMsgToEventAdmin(tpEvent);
			}
		}

	}

	private void sendMsgToEventAdmin(TPEvent tpEvent) {
		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}

	private void createActions() {
		exitAction.setEnabled(true);
		exitAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/runjunit.gif"));

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// updateAction();
			}
		});

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(exitAction);

	}

	/**
	 * We will set up a dummy model to initialize tree heararchy. In real code,
	 * you will connect to a real model and expose its hierarchy.
	 */
	private TreeObject createDummyModel() {
		TreeParent pBuddy = new TreeParent("TPBuddy",
				TreeObject.TEST_SUITE_TYPE, TreeObject.TEST_SUITE_IMAGE);
		TreeParent pManager = new TreeParent("TPManager",
				TreeObject.TEST_SUITE_TYPE, TreeObject.TEST_SUITE_IMAGE);
		TreeParent pBridge = new TreeParent("TPBridge",
				TreeObject.TEST_SUITE_TYPE, TreeObject.TEST_SUITE_IMAGE);
		TreeParent pProj = new TreeParent("Demo Project (TPTeam)",
				TreeObject.FOLDER_TYPE, TreeObject.FOLDER_IMAGE);
		pProj.addChild(pBuddy);
		pProj.addChild(pManager);
		pProj.addChild(pBridge);

		TreeParent pAdmin = new TreeParent("Admin", TreeObject.TEST_SUITE_TYPE,
				TreeObject.TEST_SUITE_IMAGE);
		TreeParent pUI = new TreeParent("UI", TreeObject.TEST_SUITE_TYPE,
				TreeObject.TEST_SUITE_IMAGE);

		pBuddy.addChild(pAdmin);
		pBuddy.addChild(pUI);

		TPTestEntity pAdmin1 = new TPTestEntity("TestEditTestSuite",
				TreeObject.JUNIT_TYPE, TreeObject.JUNIT_IMAGE);
		TPTestEntity pAdmin2 = new TPTestEntity("TestAddUser",
				TreeObject.JUNIT_TYPE, TreeObject.JUNIT_IMAGE);

		pAdmin.addChild(pAdmin1);
		pAdmin.addChild(pAdmin2);

		pAdmin1.setProject("Demo Project (TPTeam)");
		pAdmin1.setDesc("Edit TestSuite feature check");
		pAdmin1
				.setStatus("Pass: 07/21/2006 14:07:32",
						TreeObject.TEST_OK_IMAGE);
		pAdmin1.setID("1");

		pAdmin2.setProject("Demo Project (TPTeam)");
		pAdmin2.setDesc("Add user feature check");
		pAdmin2.setStatus("Fail: 07/21/2006 15:51:23",
				TreeObject.TEST_FAIL_IMAGE);
		pAdmin2.setID("2");

		mTPEntities.put(pAdmin1.getID(), pAdmin1);
		mTPEntities.put(pAdmin2.getID(), pAdmin2);

		TreeParent root = new TreeParent("", TreeObject.FOLDER_TYPE,
				TreeObject.FOLDER_IMAGE);
		root.addChild(pProj);

		return root;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new TestContentProvider());
		viewer.setLabelProvider(new TestLabelProvider());
		viewer.setInput(createDummyModel());
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
			final TPTestEntity tpEntity = mTPEntities.get(tpEvent.getID());

			if (tpEntity != null
					& tpEvent.getTopic().equals(
							ITPBridge.TEST_EXEC_RESULT_TOPIC)) {
				System.out.println("TestView: update called for "
						+ tpEvent.getTopic() + " Event for "
						+ tpEvent.getTestName());
				String status = tpEvent.getStatus();
				String successPath = TreeObject.TEST_OK_IMAGE;
				if (status.indexOf("pass") < 0) {
					successPath = TreeObject.TEST_FAIL_IMAGE;
				}
				tpEntity.setStatus(tpEvent.getStatus(), successPath);
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						viewer.refresh(tpEntity, true);
						// viewer.update(tpEntity, null);
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
