/****************************************************************************
 * Copyright (c) 2007 Remy Suen, Composent Inc., and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 *****************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.wizard;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.ui.IConnectWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.actions.URIClientConnectAction;

public class XMPPConnectWizard extends Wizard implements IConnectWizard {

	XMPPConnectWizardPage page;


	public static final String CONTAINER_TYPE = "ecf.xmpps.smack";

	public void addPages() {
		page = new XMPPConnectWizardPage();
		addPage(page);
	}

	public boolean performFinish() {
		page.saveDialogSettings();
		URIClientConnectAction client = null;

		client = new URIClientConnectAction(CONTAINER_TYPE,
				page.getConnectID(), null, page.getPassword());
		client.run(null);

		return true;
	}

	public void init(IWorkbench workbench, IContainer container) {
	}

}
