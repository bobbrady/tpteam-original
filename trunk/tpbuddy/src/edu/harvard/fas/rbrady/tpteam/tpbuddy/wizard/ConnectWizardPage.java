/*******************************************************************************
 * Copyright (c) 2005 Ed Burnette, Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ed Burnette, Composent, Inc. - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.wizard;

import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.ui.wizards.JoinGroupWizardPage;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

public class ConnectWizardPage extends JoinGroupWizardPage {
	public ConnectWizardPage(ContainerTypeDescription[] descriptions) {
		super(descriptions);
        setTitle(Activator.CONNECT_WIZARD_PAGE_TITLE);
        setDescription(Activator.CONNECT_WIZARD_PAGE_DESCRIPTION);
	}
	public ConnectWizardPage() {
		super();
	}
}
