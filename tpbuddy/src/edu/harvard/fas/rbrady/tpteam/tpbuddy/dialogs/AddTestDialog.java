/********************************************************************
 * 
 * File		:	AddTestDialog.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides a dialog for users to enter data for new
 * 				test definiton creation
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

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;

/*************************************************************************
 * File 		: 	AddTestDialog.java
 * 
 * Description 	: 	Provides a dialog for users to enter data for new
 * 					test definition creation
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 ***********************************************************************/
public class AddTestDialog extends TitleAreaDialog {
	/** ID for OK, large integer used to avoid system conflicts */
	public static final int OK = 9999;
	/** dialog OK button */
	private Button mOKBtn;
	/** Test GUI type */
	private boolean mIsFolder;
	/** Name of the test definition */
	private Text mName;
	/** Description of the test definition */
	private Text mDescription;
	/** Home directory of the test */
	private Text mHome;
	/** Workspace directory location */
	private Text mWorkspace;
	/** Test project directory */
	private Text mProject;
	/** Test suite directory */
	private Text mTestSuite;
	/** Test report directory */
	private Text mReportDir;
	/** TPTP connection URL to be used for execution */
	private Text mConnURL;
	/** Stubbed version of Test object */
	private Test mTestStub;
	/** TPTeam database ID of test parent */
	private int mParentID;

	/**
	 * Constructor
	 * 
	 * @param shell -
	 *            Containing shell
	 */
	public AddTestDialog(Shell shell, int parentID) {
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
		setTitle("Add Test Node");
		getShell().setSize(600, 400);
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

		if (!mIsFolder) {
			createTestArea(area);
		}

		return area;
	}

	/**
	 * Helper function to create additional test 
	 * definition data inputs to dialog area
	 * 
	 * @param area the dialog area
	 */
	protected void createTestArea(Composite area) {
		Label homeLabel = new Label(area, SWT.NONE);
		homeLabel.setText("Eclipse Home:");
		mHome = new Text(area, SWT.BORDER);

		Label workLabel = new Label(area, SWT.NONE);
		workLabel.setText("Eclipse Workspace:");
		mWorkspace = new Text(area, SWT.BORDER);

		Label projLabel = new Label(area, SWT.NONE);
		projLabel.setText("Eclipse Project:");
		mProject = new Text(area, SWT.BORDER);

		Label testsuiteLabel = new Label(area, SWT.NONE);
		testsuiteLabel.setText("TPTP Testsuite:");
		mTestSuite = new Text(area, SWT.BORDER);

		Label reportLabel = new Label(area, SWT.NONE);
		reportLabel.setText("TPTP Report Dir:");
		mReportDir = new Text(area, SWT.BORDER);

		Label connLabel = new Label(area, SWT.NONE);
		connLabel.setText("TPTP Conn URL:");
		mConnURL = new Text(area, SWT.BORDER);

		GridData data = new GridData();
		data.widthHint = 100;
		homeLabel.setLayoutData(data);

		data = new GridData();
		data.widthHint = 100;
		projLabel.setLayoutData(data);

		data = new GridData();
		data.widthHint = 100;
		testsuiteLabel.setLayoutData(data);

		data = new GridData();
		data.widthHint = 100;
		reportLabel.setLayoutData(data);

		data = new GridData();
		data.widthHint = 100;
		connLabel.setLayoutData(data);

		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		mHome.setLayoutData(data2);

		data2 = new GridData(GridData.FILL_HORIZONTAL);
		mWorkspace.setLayoutData(data2);

		data2 = new GridData(GridData.FILL_HORIZONTAL);
		mProject.setLayoutData(data2);

		data2 = new GridData(GridData.FILL_HORIZONTAL);
		mTestSuite.setLayoutData(data2);

		data2 = new GridData(GridData.FILL_HORIZONTAL);
		mReportDir.setLayoutData(data2);

		data2 = new GridData(GridData.FILL_HORIZONTAL);
		mConnURL.setLayoutData(data2);
	}

	/**
	 * Validates user input
	 * @return true if required data present, false otherwise
	 */
	private boolean validate() {
		boolean returnVal = false;
		if (mName.getText() == null || mName.getText().equals("")) {
			setErrorMessage("Name value must not be empty.");
		} else if (!mIsFolder) {
			if (mHome.getText() == null || mHome.getText().equals("")
					|| mWorkspace.getText() == null
					|| mWorkspace.getText().equals("")
					|| mProject.getText() == null
					|| mProject.getText().equals("")
					|| mTestSuite.getText() == null
					|| mTestSuite.getText().equals("")
					|| mReportDir.getText() == null
					|| mReportDir.getText().equals("")
					|| mConnURL.getText() == null
					|| mConnURL.getText().equals("")) {
				setErrorMessage("JUnit properties must not be empty.");
			} else {
				returnVal = true;
			}
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
		mOKBtn.setEnabled(true);
		mOKBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (validate()) {
					mTestStub = new Test();
					mTestStub.setName(mName.getText());
					mTestStub.setDescription(mDescription.getText());
					mTestStub.setIsFolder('N');
					Test parent = new Test();
					parent.setId(mParentID);
					parent.addChild(mTestStub);
					mTestStub.setParent(parent);
					TestType testType = new TestType();
					testType.setName("JUnit");
					mTestStub.setTestType(testType);
					JunitTest junit = new JunitTest();
					junit.setEclipseHome(mHome.getText());
					junit.setWorkspace(mWorkspace.getText());
					junit.setProject(mProject.getText());
					junit.setTestSuite(mTestSuite.getText());
					junit.setReportDir(mReportDir.getText());
					junit.setTptpConnection(mConnURL.getText());
					mTestStub.addJunitTest(junit);

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
	 * @return the test stub
	 */
	public Test getTestStub() {
		return mTestStub;
	}

}
