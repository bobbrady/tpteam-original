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

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class XMPPConnectWizardPage extends WizardPage {

	Text connectText;

	Text passwordText;

	protected static final String CLASSNAME = XMPPConnectWizardPage.class
			.getName();

	private static final String DIALOG_SETTINGS = CLASSNAME;

	XMPPConnectWizardPage() {
		super("");
		setTitle("XMPPS Connection Wizard");
		setDescription("Specify a XMPP account to connect to.");
		setPageComplete(false);
	}

	public void createControl(Composite parent) {
		parent.setLayout(new GridLayout());
		GridData fillData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		GridData endData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);

		Label label = new Label(parent, SWT.LEFT);
		label.setText("User ID:");

		connectText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		restoreDialogSettings();
		connectText.setLayoutData(fillData);
		connectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!connectText.getText().equals("")) { //$NON-NLS-1$
					updateStatus(null);
				} else {
					updateStatus("An connect ID must be specified.");
				}
			}
		});

		label = new Label(parent, SWT.RIGHT);
		label.setText("<user>@<xmppserver>[:port]");
		label.setLayoutData(endData);

		label = new Label(parent, SWT.LEFT);
		label.setText("Password:");
		passwordText = new Text(parent, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData(fillData);

		setControl(parent);
	}

	String getConnectID() {
		return connectText.getText();
	}

	String getPassword() {
		return passwordText.getText();
	}

	protected void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private void restoreDialogSettings() {
		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null) {
			IDialogSettings pageSettings = dialogSettings
					.getSection(DIALOG_SETTINGS);
			if (pageSettings != null) {
				String strVal = pageSettings.get("connectText");
				if (strVal != null) {
					connectText.setText(strVal);
				}
			}
		}
	}

	public void saveDialogSettings() {
		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null) {
			IDialogSettings pageSettings = dialogSettings
					.getSection(DIALOG_SETTINGS);
			if (pageSettings == null)
				pageSettings = dialogSettings.addNewSection(DIALOG_SETTINGS);

			pageSettings.put("connectText", connectText.getText());
		}
	}

}
