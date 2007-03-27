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
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TreeNodeModel;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestExecutionXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.dialogs.AddFolderDialog;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.dialogs.UpdateDialog;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

public class TestView extends ViewPart implements Observer {
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.testview";

	private Action mExecTest;

	private Action mDelTest;

	private Action mUpdateTest;

	private Action mShowTest;

	private Action mAddFolder;

	private TreeViewer mViewer;

	private TestContentProvider mTestContentProvider;

	private TreeNodeModel mTreeNodeModel;

	private String mProjID;

	private void execTestAction() {
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		Iterator selectionIter = selection.iterator();
		while (selectionIter.hasNext()) {
			TPEntity treeEnt = (TPEntity) selectionIter.next();
			System.out.println("\n\nTestView: Selection " + treeEnt.getName());
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
			dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
					.getTPBridgeClient().getTPMgrECFID());
			dictionary.put(TPEvent.FROM, Activator.getDefault()
					.getTPBridgeClient().getTargetIDName());
			TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_REQ_TOPIC,
					dictionary);
			sendMsgToEventAdmin(tpEvent);
		}
	}

	private void delTestAction() {
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();
		System.out.println("\n\nTestView: Selection " + treeEnt.getName());
		Shell parent = getViewSite().getShell();

		if (Integer.parseInt(treeEnt.getID()) < 1) {
			MessageDialog.openError(parent, "Delete Test Error",
					"Delete Test Error: The root node can not be deleted.");
			return;
		}

		boolean confirm = MessageDialog.openConfirm(parent,
				"Delete Test Confirmation",
				"Are you sure you want to delete test entity "
						+ treeEnt.getName() + "?");
		if (!confirm)
			return;

		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_DEL_REQ_TOPIC, dictionary);
		sendMsgToEventAdmin(tpEvent);
	}

	private void updateTestAction() {
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		System.out.println("\n\nTestView: Selection " + treeEnt.getName());
		System.out.println("TestType: " + treeEnt.getType());
		System.out.println("Test ID: " + treeEnt.getID());

		Shell parent = getViewSite().getShell();

		if (Integer.parseInt(treeEnt.getID()) < 1) {
			MessageDialog.openError(parent, "Update Test Error",
					"Update Test Error: The root node can not be updated.");
			return;
		}

		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_UPDATE_DATA_REQ_TOPIC,
				dictionary);
		sendMsgToEventAdmin(tpEvent);

		UpdateDialog updateDialog = new UpdateDialog(parent, treeEnt.getType()
				.equals(TPEntity.FOLDER));

		if (updateDialog.open() == UpdateDialog.OK) {
			String testXML = TestXML.getXML(updateDialog.getTestStub());
			System.out.println("testXML:\n" + testXML);
			dictionary.put(TPEvent.TEST_XML_KEY, testXML);
			tpEvent = new TPEvent(ITPBridge.TEST_UPDATE_REQ_TOPIC, dictionary);
			sendMsgToEventAdmin(tpEvent);
		}
	}

	private void addFolderAction() {
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		System.out.println("\n\nTestView: Selection " + treeEnt.getName());
		System.out.println("TestType: " + treeEnt.getType());
		System.out.println("Test ID: " + treeEnt.getID());

		Shell parent = getViewSite().getShell();

		if (!treeEnt.getType().equals(TPEntity.FOLDER)) {
			MessageDialog
					.openError(parent, "Add Test Error",
							"Add Test Error: Folders can not be added to test definitions.");
			return;
		}

		AddFolderDialog addFolderDialog = new AddFolderDialog(parent, Integer
				.parseInt(treeEnt.getID()));

		if (addFolderDialog.open() == AddFolderDialog.OK) {
			Test testStub = addFolderDialog.getTestStub();
			TpteamUser addUser = new TpteamUser();
			addUser.setEcfId(Activator.getDefault().getTPBridgeClient()
					.getTargetIDName());
			testStub.setCreatedBy(addUser);
			Project proj = new Project();
			proj.setId(Integer.parseInt(mProjID));
			testStub.setProject(proj);
			String testXML = TestXML.getXML(testStub);
			System.out.println("testXML:\n" + testXML);

			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
			dictionary.put(TPEvent.PROJECT_ID_KEY, mProjID);
			dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
					.getTPBridgeClient().getTPMgrECFID());
			dictionary.put(TPEvent.FROM, addUser.getEcfId());

			TPEvent tpEvent = new TPEvent(ITPBridge.TEST_ADD_REQ_TOPIC,
					dictionary);
			dictionary.put(TPEvent.TEST_XML_KEY, testXML);
			sendMsgToEventAdmin(tpEvent);
		}
	}

	private void showTestAction() {
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();
		System.out.println("\n\nTestView: Selection " + treeEnt.getName());
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_DETAIL_REQ_TOPIC,
				dictionary);
		sendMsgToEventAdmin(tpEvent);
		showDetailView();
	}

	private void showDetailView() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			try {
				page.showView(DetailView.ID);
			} catch (PartInitException e) {

				e.printStackTrace();
			}
		}
	}

	private void sendMsgToEventAdmin(TPEvent tpEvent) {
		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}

	private void createActions() {
		mExecTest.setEnabled(true);
		mExecTest.setImageDescriptor(Activator
				.getImageDescriptor("icons/runjunit.gif"));

		mDelTest.setEnabled(true);
		mDelTest.setImageDescriptor(Activator
				.getImageDescriptor("icons/delete.gif"));

		mShowTest.setEnabled(true);
		mShowTest.setImageDescriptor(Activator
				.getImageDescriptor("icons/test.gif"));

		mUpdateTest.setEnabled(true);
		mUpdateTest.setImageDescriptor(Activator
				.getImageDescriptor("icons/update_tree.gif"));

		mAddFolder.setEnabled(true);
		mAddFolder.setImageDescriptor(Activator
				.getImageDescriptor("icons/folderadd_pending.gif"));

		mViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// updateAction();
			}
		});

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(mShowTest);
		mgr.add(mAddFolder);
		mgr.add(mUpdateTest);
		mgr.add(mDelTest);
		mgr.add(mExecTest);

	}

	/**
	 * This is a callback that will allow us to create the mViewer and
	 * initialize it.
	 */
	public void createPartControl(Composite parent) {

		Activator.getDefault().getEventAdminHandler().addObserver(this);

		mTreeNodeModel = new TreeNodeModel();

		mExecTest = new Action("Run...") {
			public void run() {
				execTestAction();
			}
		};

		mDelTest = new Action("Delete...") {
			public void run() {
				delTestAction();
			}
		};

		mShowTest = new Action("Details...") {
			public void run() {
				showTestAction();
			}
		};

		mUpdateTest = new Action("Update...") {
			public void run() {
				updateTestAction();
			}
		};

		mAddFolder = new Action("Add Test Folder...") {
			public void run() {
				addFolderAction();
			}
		};

		mViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		mTestContentProvider = new TestContentProvider();
		mViewer.setContentProvider(mTestContentProvider);
		mViewer.setLabelProvider(new TestLabelProvider());
		try {
			mViewer.setInput(null/* TestXML.getExample() */);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getSite().setSelectionProvider(mViewer);

		createActions();
	}

	/**
	 * Passing the focus request to the mViewer's control.
	 */
	public void setFocus() {
		mViewer.getControl().setFocus();
	}

	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			// final TPTestEntity tpEntity = mTPEntities.get(tpEvent.getID());

			System.out.println("TestView Got Update: " + tpEvent.getTopic());
			if (tpEvent.getTopic().equals(ITPBridge.TEST_EXEC_RESULT_TOPIC)) {

				System.out.println("TestView: update called for "
						+ tpEvent.getTopic() + " Event for "
						+ tpEvent.getTestName());

				updateExecution(tpEvent);

			} else if (tpEvent.getTopic().equalsIgnoreCase(
					ITPBridge.TEST_TREE_GET_REQ_TOPIC)) {
				mProjID = tpEvent.getDictionary().get(TPEvent.PROJECT_ID_KEY);
			} else if (tpEvent.getTopic().equalsIgnoreCase(
					ITPBridge.TEST_TREE_GET_RESP_TOPIC)) {
				String testTreeXML = tpEvent.getDictionary().get(
						TPEvent.TEST_TREE_XML_KEY);
				System.out.println(testTreeXML);
				final TPEntity projRoot = TestXML
						.getTPEntityFromXML(testTreeXML);

				mTreeNodeModel.clear();
				populateModel(projRoot);

				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						mViewer
								.setInput(new TPEntity[] { projRoot } /* projRoot.getChildren() */);
					}
				});
			} else if (tpEvent.getTopic().equalsIgnoreCase(
					ITPBridge.TEST_DEL_RESP_TOPIC)) {
				final String nodeID = tpEvent.getID();
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						ITreeNode delNode = mTreeNodeModel.get(nodeID);
						ITreeNode parent;
						if (delNode != null
								&& (parent = delNode.getParent()) != null) {
							parent.removeChild(delNode);
						}
					}
				});
			} else if (tpEvent.getTopic().equalsIgnoreCase(
					ITPBridge.TEST_UPDATE_RESP_TOPIC)) {
				final String nodeID = tpEvent.getID();
				final String testName = tpEvent.getDictionary().get(
						TPEvent.TEST_NAME_KEY);
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						ITreeNode updateNode = mTreeNodeModel.get(nodeID);
						updateNode.setName(testName);
					}
				});
			} else if (tpEvent.getTopic().equalsIgnoreCase(
					ITPBridge.TEST_ADD_RESP_TOPIC)) {
					String testXML = tpEvent.getDictionary().get(
						TPEvent.TEST_XML_KEY);
				System.out.println("testAddXML:\n" + testXML);
				final Test testStub = TestXML.getTestFromXML(testXML);
				final TPEntity tpEntity = TestXML.getTPEntityFromTest(testStub);
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						ITreeNode addNode = tpEntity;
						ITreeNode parent = null;
						if(testStub.getParent().getId() == 0)
						{
							parent = mTreeNodeModel.get("0");
						}
						else
						{
							parent = mTreeNodeModel.get(String.valueOf(testStub.getParent().getId()));
						}
						if (parent != null) {
							addNode.setParent(parent);
							parent.addChild(addNode);
							mTreeNodeModel.addNode(addNode);
						}
					}
				});

			}
		}
	}

	private void populateModel(ITreeNode treeNode) {
		mTreeNodeModel.put(treeNode.getID(), treeNode);
		for (ITreeNode child : treeNode.getChildren())
			populateModel(child);
	}

	private void updateExecution(final TPEvent tpEvent) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				String execXML = tpEvent.getDictionary().get(
						TPEvent.TEST_EXEC_XML_KEY);
				ITreeNode execNode = TestExecutionXML
						.getTPEntityFromXML(execXML);
				ITreeNode parent = null;
				if ((parent = mTreeNodeModel.get(execNode.getParent().getID())) != null) {
					execNode.setParent(parent);
					parent.addChild(execNode);
				}
			}
		});
	}

	public void dispose() {
		super.dispose();
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
	}

}
