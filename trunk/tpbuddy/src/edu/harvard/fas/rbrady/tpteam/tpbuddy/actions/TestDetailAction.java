/********************************************************************
 * 
 * File		:	TestDetailAction.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Action that displays the Test Details View
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.DetailView;

/*******************************************************************************
 * File 		: 	TestDetailAction.java
 * 
 * Description 	: 	Action that displays the Test Details View
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 ******************************************************************************/
public class TestDetailAction extends ActionDelegate implements
		IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	/** The ID of the DetailView */
	private final String viewID = DetailView.ID;

	public void run() {
	}

	protected IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Opens the Test Details view
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (window != null) {
			try {
				window.getActivePage().showView(viewID);
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error",
						"Error opening view:" + e.getMessage());
			}
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
