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
import edu.harvard.fas.rbrady.tpteam.tpbuddy.dialogs.AddTestDialog;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.dialogs.UpdateDialog;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

public class TestView extends ViewPart implements Observer {
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.testview";

	private Action mExecTest;

	private Action mDelTest;

	private Action mUpdateTest;

	private Action mShowTest;

	private Action mAddFolder;

	private Action mAddTest;

	private TreeViewer mViewer;

	private TestContentProvider mTestContentProvider;

	private TreeNodeModel mTreeNodeModel;

	private String mProjID;

	private String mProjName;

	/**
	 * Extracts node information from tree, wraps into
	 * TPEvent requesting execution
	 * 
	 * Performed when user clicks on exec test icon in view
	 */
	private void execTestAction() {
		// Get selected node in test tree
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		// Get parent shell to display dialog, if necessary
		Shell parent = getViewSite().getShell();

		// Throw error dialog if execution attempted on folder node
		if (treeEnt.getType().equals(TPEntity.FOLDER)) {
			MessageDialog
					.openError(parent, "Exec Test Error",
							"Execute Test Error: Folders can not be executed as test definitions.");
			return;
		}

		// Send TPEvent requesting test execution
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_REQ_TOPIC, dictionary);
		sendMsgToEventAdmin(tpEvent);
	}

	private void delTestAction() {

		// Get the selected node in test tree
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		// Get parent shell in case error dialog needs to be shown
		Shell parent = getViewSite().getShell();

		// Show error dialog if user attemped to delete test tree root node
		if (Integer.parseInt(treeEnt.getID()) < 1) {
			MessageDialog.openError(parent, "Delete Test Error",
					"Delete Test Error: The root node can not be deleted.");
			return;
		}

		// Confirmation needed for deletions, for safety.
		boolean confirm = MessageDialog.openConfirm(parent,
				"Delete Test Confirmation",
				"Are you sure you want to delete test entity "
						+ treeEnt.getName() + "?");
		if (!confirm)
			return;

		// Wrap info into TPEvent and send delete request
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
		// Get the selected node in test tree
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		// Get parent shell in case error dialog needs to be shown
		Shell parent = getViewSite().getShell();

		// Show error dialog if user attempts to update test tree root
		if (Integer.parseInt(treeEnt.getID()) < 1) {
			MessageDialog.openError(parent, "Update Test Error",
					"Update Test Error: The root node can not be updated.");
			return;
		}

		// Wrap update request into TPEvent and send
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
		// Get parent folder where new child folder will be added
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		// Get the parent shell so add folder dialog can be shown
		Shell parent = getViewSite().getShell();

		// Error checking
		if (!treeEnt.getType().equals(TPEntity.FOLDER)) {
			MessageDialog
					.openError(parent, "Add Test Error",
							"Add Test Error: Folders can not be added to test definitions.");
			return;
		}

		// Show add folder dialog and pull-out info
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

			// Wrap info into TPEvent and send add folder request
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

	private void addTestAction() {
		// Get parent folder where test definition will be added
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		// Get parent shell so add test dialog can be shown
		Shell parent = getViewSite().getShell();

		// Error check
		if (!treeEnt.getType().equals(TPEntity.FOLDER)) {
			MessageDialog
					.openError(parent, "Add Test Error",
							"Add Test Error: Test definitions can not be added to test definitions.");
			return;
		}

		// Show add dialog and pullout info
		AddTestDialog addTestDialog = new AddTestDialog(parent, Integer
				.parseInt(treeEnt.getID()));

		if (addTestDialog.open() == AddTestDialog.OK) {
			Test testStub = addTestDialog.getTestStub();
			TpteamUser addUser = new TpteamUser();
			addUser.setEcfId(Activator.getDefault().getTPBridgeClient()
					.getTargetIDName());
			testStub.setCreatedBy(addUser);
			Project proj = new Project();
			proj.setId(Integer.parseInt(mProjID));
			testStub.setProject(proj);
			String testXML = TestXML.getXML(testStub);

			// Wrap info into TPEvent and send add test request
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
		// Get selected node
		IStructuredSelection selection = (IStructuredSelection) mViewer
				.getSelection();
		final TPEntity treeEnt = (TPEntity) selection.getFirstElement();

		// Wrap info into TPEvent and send test detail request
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(treeEnt.getID()));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_DETAIL_REQ_TOPIC,
				dictionary);
		sendMsgToEventAdmin(tpEvent);
		// Give focus to test detail view now
		showDetailView();
	}

	/**
	 * Gives focus to test detail view while waiting for test detail results to
	 * arrive.
	 */
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

	/**
	 * Submits a TPEvent that request a response event containing
	 * the entire project test tree in serialized XML form.
	 *
	 */
	private void getTestTreeAction() {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTargetIDName());
		dictionary.put(TPEvent.PROJECT_ID_KEY, mProjID);
		dictionary.put(TPEvent.PROJECT_KEY, mProjName);

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_TREE_GET_REQ_TOPIC, dictionary);
	}

	/**
	 *  Sends a TPEvent to the EventAdmin, which will submit
	 *  to the OSGi Event service and route to all interested
	 *  subscribers
	 *  
	 * @param tpEvent
	 */
	private void sendMsgToEventAdmin(TPEvent tpEvent) {
		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}

	/**
	 * Helper method to enable the various test tree 
	 * actions a user can carry out through the view
	 */
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

		mAddTest.setEnabled(true);
		mAddTest.setImageDescriptor(Activator
				.getImageDescriptor("icons/new_testcase.gif"));

		mViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// updateAction();
			}
		});

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(mShowTest);
		mgr.add(mAddFolder);
		mgr.add(mAddTest);
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

		mAddTest = new Action("Add Test Definition...") {
			public void run() {
				addTestAction();
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

		mProjID = Activator.getDefault().getProjID();
		mProjName = Activator.getDefault().getProjName();

		if (mProjID != null && !mProjID.equalsIgnoreCase("")
				&& Integer.parseInt(mProjID) > 0)
			getTestTreeAction();

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
				mProjName = tpEvent.getDictionary().get(TPEvent.PROJECT_KEY);
				Activator.getDefault().setProjID(mProjID);
				Activator.getDefault().setProjName(mProjName);
			} else if (((TPEvent) object).getTopic().equals(
					ITPBridge.CHART_GET_DATA_REQ_TOPIC)) {
				mProjID = tpEvent.getDictionary().get(TPEvent.PROJECT_ID_KEY);
				mProjName = tpEvent.getDictionary().get(TPEvent.PROJECT_KEY);
				Activator.getDefault().setProjID(mProjID);
				Activator.getDefault().setProjName(mProjName);
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
						if (testStub.getParent().getId() == 0) {
							parent = mTreeNodeModel.get("0");
						} else {
							parent = mTreeNodeModel.get(String.valueOf(testStub
									.getParent().getId()));
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
