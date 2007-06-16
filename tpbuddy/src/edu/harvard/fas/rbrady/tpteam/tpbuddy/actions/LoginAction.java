/********************************************************************
 * 
 * File		:	LoginAction.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Action that displays the XMPPConnectionWizard dialog
 * 				so that user may login to TPTeam
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.actions;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.wizard.XMPPConnectWizard;

/*******************************************************************************
 * File : LoginAction.java
 * 
 * Description : Action that displays the XMPPConnectionWizard dialog so that
 * user may login to TPTeam
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 ******************************************************************************/
public class LoginAction extends ActionDelegate implements
		IWorkbenchWindowActionDelegate {

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
	}

	/**
	 * Opens an XMPPConnectWizard dialog so that a user may login to TPTeam
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// Create the wizard dialog
		IContainer container = null;
		try {
			container = ContainerFactory.getDefault().createContainer(
					XMPPConnectWizard.CONTAINER_TYPE);
		} catch (ContainerCreateException e) {
			MessageDialog.openError(getWorkbench().getActiveWorkbenchWindow()
					.getShell(), "Create Error",
					"Could not create XMPP container.\n\nError: "
							+ e.getLocalizedMessage());
		}
		XMPPConnectWizard connectWizard = new XMPPConnectWizard();
		connectWizard.init(getWorkbench(), container);
		WizardDialog dialog = new WizardDialog(getWorkbench()
				.getActiveWorkbenchWindow().getShell(), connectWizard);
		// Open the wizard dialog
		dialog.open();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
