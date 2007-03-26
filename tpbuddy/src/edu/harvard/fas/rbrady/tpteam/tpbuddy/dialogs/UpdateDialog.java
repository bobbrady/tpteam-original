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

public class UpdateDialog extends TitleAreaDialog {
    // ID for OK button
    // We use large integer so as not 
    // to conflict with system constants
    public static final int OK = 9999;
    
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
     * @param shell - Containing shell
     */
    public UpdateDialog(Shell shell, boolean isFolder) {
        super(shell);
        mIsFolder = isFolder;
    }
    
    /**
     * @see org.eclipse.jface.window.Window#create()
     * We complete the dialog with a title and a message
     */
    public void create() {
        super.create();
        setTitle("Update Test Node");
        setMessage(
            "Once current values have been loaded, edit the items to be changed.");
        if(mIsFolder)
        	getShell().setSize(600, 250);
        else
        	getShell().setSize(600, 400);
    }
    /**
     * @see org.eclipse.jface.dialogs.Dialog#
     * createDialogArea(org.eclipse.swt.widgets.Composite)
     * Here we fill the center area of the dialog
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
        
        mDescription = new Text(area, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
        GridData descData = new GridData(GridData.FILL_BOTH);
        descData.verticalSpan = 15;
        mDescription.setLayoutData(descData);

        if(!mIsFolder)
        {
        	createTestArea(area);
        }
    
        return area;
    }
    
    protected void createTestArea(Composite area)
    {
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
    
    
    private void validate() {
        getButton(OK).setEnabled(true);
        if (true)
            // If nothing was selected, we set an error message
            setErrorMessage("Select at least one entry!");
        else
            // Otherwise we set the error message to null
            // to show the intial content of the message area
            setErrorMessage(null);
        }
    
    
        /**
         * @see org.eclipse.jface.dialogs.Dialog#
         * createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
         * We replace the OK button by our own creation
         * We use the method createButton() (from Dialog), 
         * to create the new button
         */
        protected void createButtonsForButtonBar(Composite parent) {
            // Create Ok button
            Button okButton = createButton(parent, OK, 
                "Ok", true);
            // Initially deactivate it
            okButton.setEnabled(false);
            // Add a SelectionListener
            okButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    // Set return code
                    setReturnCode(OK);
                    // Close dialog
                    close();
                }
            });
            
        // Create Cancel button
        Button cancelButton = 
            createButton(parent, CANCEL, "Cancel", false);
        // Add a SelectionListener
        cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                setReturnCode(CANCEL);
                close();
            }
        });
    }

}
