package edu.harvard.fas.rbrady.tpteam.tpbuddy.dialogs;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestExecutionXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

public class UpdateDialog extends TitleAreaDialog implements Observer {
	// ID for OK button
	// We use large integer so as not
	// to conflict with system constants
	public static final int OK = 9999;
	
	private Button mOKBtn;

	private boolean mIsFolder;

	private Text mName;

	private Text mDescription;

	private Text mHome;

	private Text mWorkspace;

	private Text mProject;

	private Text mTestSuite;

	private Text mReportDir;

	private Text mConnURL;

	/**
	 * Constructor for UpdateDialog.
	 * 
	 * @param shell -
	 *            Containing shell
	 */
	public UpdateDialog(Shell shell, boolean isFolder) {
		super(shell);
		mIsFolder = isFolder;
	}

	/**
	 * @see org.eclipse.jface.window.Window#create() We complete the dialog with
	 *      a title and a message
	 */
	public void create() {
		super.create();
		setTitle("Update Test Node");
		setMessage("Once current values have been loaded, edit the items to be changed.");
		if (mIsFolder)
			getShell().setSize(600, 250);
		else
			getShell().setSize(600, 400);
		Activator.getDefault().getEventAdminHandler().addObserver(this);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#
	 *      createDialogArea(org.eclipse.swt.widgets.Composite) Here we fill the
	 *      center area of the dialog
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

	protected void createTestArea(Composite area) {
		Label homeLabel = new Label(area, SWT.NONE);
		homeLabel.setText("Eclipse Home:");
		mHome = new Text(area, SWT.BORDER);

		Label workLabel = new Label(area, SWT.NONE);
		workLabel.setText("Eclipse Workspace:");
		mWorkspace = new Text(area, SWT.BORDER);

		Label projLabel = new Label(area, SWT.NONE);
		projLabel.setText("Eclipse Workspace:");
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

	private boolean validate() {
		boolean returnVal = false;
		if (mName.getText() == null || mName.getText().equals(""))
		{
			setErrorMessage("Name value must not be empty.");			
		}
		else if(!mIsFolder)
		{
			if(mHome.getText() == null || mHome.getText().equals("")
					|| mWorkspace.getText() == null || mWorkspace.getText().equals("")
					|| mProject.getText() == null || mProject.getText().equals("")
					|| mTestSuite.getText() == null || mTestSuite.getText().equals("")
					|| mReportDir.getText() == null || mReportDir.getText().equals("")
					|| mConnURL.getText() == null || mConnURL.getText().equals(""))
			{
				setErrorMessage("JUnit properties must not be empty.");
			}
			else
			{
				returnVal = true;
			}
		}
		else
		{
			setErrorMessage(null);
			returnVal = true;
		}
		return returnVal;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#
	 *      createButtonsForButtonBar(org.eclipse.swt.widgets.Composite) We
	 *      replace the OK button by our own creation We use the method
	 *      createButton() (from Dialog), to create the new button
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// Create Ok button
		mOKBtn = createButton(parent, OK, "Ok", true);
		// Initially deactivate it
		mOKBtn.setEnabled(false);
		// Add a SelectionListener
		mOKBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(validate())
				{
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

	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {

			TPEvent tpEvent = (TPEvent) object;

			if (tpEvent.getTopic()
					.equals(ITPBridge.TEST_UPDATE_DATA_RESP_TOPIC)) {
				String testXML = tpEvent.getDictionary().get(
						TPEvent.TEST_XML_KEY);
				Test testStub = TestXML.getTestFromXML(testXML);
				populateWidgets(testStub);
			}
		}
	}

	private void populateWidgets(final Test test) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				mName.setText(test.getName());
				if(test.getDescription() != null)
					mDescription.setText(test.getDescription());
				
				if (test.getJunitTests() != null && test.getJunitTests().size() > 0) {
					for (JunitTest junit : test.getJunitTests()) {
						mHome.setText(junit.getEclipseHome());
						mWorkspace.setText(junit.getWorkspace());
						mProject.setText(junit.getProject());
						mTestSuite.setText(junit.getTestSuite());
						mReportDir.setText(junit.getReportDir());
						mConnURL.setText(junit.getTptpConnection());
					}
				}
				mOKBtn.setEnabled(true);	
			}
		});

	}

	public boolean close()
	{
		Activator.getDefault().getEventAdminHandler().deleteObserver(this);
		super.close();
		return true;
	}
	
}
