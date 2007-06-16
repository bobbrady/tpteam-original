/********************************************************************
 * 
 * File		:	AddFolderDialog.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides a dialog for users to enter data for new
 * 				test folder creation
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;

/*************************************************************************
 * File 		: 	AddFolderDialog.java
 * 
 * Description 	: 	Provides a dialog for users to enter data for new
 * 					test folder creation
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 ***********************************************************************/
public class AddFolderDialog extends TitleAreaDialog {
	/** ID for OK, large integer used to avoid system conflicts */
	public static final int OK = 9999;
	/** dialog OK button */
	private Button mOKBtn;
	/** Test folder name */
	private Text mName;
	/** Test folder description */
	private Text mDescription;
	/** Stubbed Test object to represent test folder */
	private Test mTestStub;
	/** TPTeam database ID of parent test folder */
	private int mParentID;

	/**
	 * Constructor
	 * 
	 * @param shell -
	 *            Containing shell
	 */
	public AddFolderDialog(Shell shell, int parentID) {
		super(shell);
		mParentID = parentID;
	}

	/**
	 * Complete the dialog with
	 *      a title and a message
	 * @see org.eclipse.jface.window.Window#create() 
	 */
	public void create() {
		super.create();
		setTitle("Add Test Folder");
		getShell().setSize(600, 250);
	}

	/**
	 * Fill center area of the dialog
	 * @see org.eclipse.jface.dialogs.Dialog#
	 *      createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {

		// Create new composite as container
		final Composite area = new Composite(parent, SWT.NULL);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		area.setLayout(gridLayout);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		area.setLayoutData(data);

		Label nameLabel = new Label(area, SWT.NONE);
		nameLabel.setText("Name:");
		mName = new Text(area, SWT.BORDER);

		GridData nameLData = new GridData();
		nameLData.widthHint = 60;
		nameLabel.setLayoutData(nameLData);

		GridData nameData = new GridData(GridData.FILL_HORIZONTAL);
		mName.setLayoutData(nameData);

		Label descLabel = new Label(area, SWT.NONE);
		descLabel.setText("Description:");

		GridData descLData = new GridData();
		descLData.widthHint = 60;
		descLData.verticalSpan = 15;
		descLabel.setLayoutData(descLData);

		mDescription = new Text(area, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		GridData descData = new GridData(GridData.FILL_BOTH);
		descData.verticalSpan = 15;
		mDescription.setLayoutData(descData);
		return area;
	}

	private boolean validate() {
		boolean returnVal = false;
		if (mName.getText() == null || mName.getText().equals("")) {
			setErrorMessage("Name value must not be empty.");
		} else {
			setErrorMessage(null);
			returnVal = true;
		}
		return returnVal;
	}

	/**
	 * Replace the OK button by createButton() from Dialog
	 * @see org.eclipse.jface.dialogs.Dialog#
	 *      createButtonsForButtonBar(org.eclipse.swt.widgets.Composite) 
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// Create Ok button
		mOKBtn = createButton(parent, OK, "Ok", true);
		// Initially deactivate it
		mOKBtn.setEnabled(true);
		// Add a SelectionListener
		mOKBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (validate()) {
					mTestStub = new Test();
					mTestStub.setName(mName.getText());
					mTestStub.setDescription(mDescription.getText());
					mTestStub.setIsFolder('Y');
					TestType testType = new TestType();
					testType.setName("Folder");
					mTestStub.setTestType(testType);
					Test parent = new Test();
					parent.setId(mParentID);
					parent.addChild(mTestStub);
					mTestStub.setParent(parent);
					setReturnCode(OK);
					close();
				}
			}
		});

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	/**
	 * Getter 
	 * @return the test stub representing the folder
	 */
	public Test getTestStub() {
		return mTestStub;
	}

}
