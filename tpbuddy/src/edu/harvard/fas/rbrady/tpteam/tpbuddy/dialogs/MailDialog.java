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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class MailDialog extends TitleAreaDialog {
    // IDs for MailDialog buttons
    // We use large integers because we don’t want 
    // to conflict with system constants
    public static final int OPEN = 9999;
    public static final int DELETE = 9998;
    // List widget
    List list;
    // Initial content of the list
    String[] items;
    // Selected items
    String[] itemsToOpen;
    /**
     * Constructor for MailDialog.
     * @param shell - Containing shell
     * @param items – Mail messages passed to the dialog
     */
    public MailDialog(Shell shell, String[] items) {
        super(shell);
        this.items = items;
    }
    /**
     * @see org.eclipse.jface.window.Window#create()
     * We complete the dialog with a title and a message
     */
    public void create() {
        super.create();
        setTitle("Mail");
        setMessage(
            "You have mail!\n It could be vital for this evening…");
    }
    /**
     * @see org.eclipse.jface.dialogs.Dialog#
     * createDialogArea(org.eclipse.swt.widgets.Composite)
     * Here we fill the center area of the dialog
     */     
    protected Control createDialogArea(Composite parent) {
        // Create new composite as container
        final Composite area = new Composite(parent, SWT.NULL);
        // We use a grid layout and set the size of the margins
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 15;
        gridLayout.marginHeight = 10;
        area.setLayout(gridLayout);
        // Now we create the list widget
        list = new List(area, SWT.BORDER | SWT.MULTI);
        // We define a minimum width for the list
        final GridData gridData = new GridData();
        gridData.widthHint = 200;
        list.setLayoutData(gridData);
        // We add a SelectionListener
        list.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // When the selection changes, we re-validate the list
                validate();
            }
        });
        // We add the initial mail messages to the list
        for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
        }
        return area;
    }
    private void validate() {
        // We select the number of selected list entries
        boolean selected = (list.getSelectionCount() > 0);
        // We enable/disable the Open and Delete buttons
        getButton(OPEN).setEnabled(selected);
        getButton(DELETE).setEnabled(selected);
        if (!selected)
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
         * We replace the OK and Cancel buttons by our own creations
         * We use the method createButton() (from Dialog), 
         * to create the new buttons
         */
        protected void createButtonsForButtonBar(Composite parent) {
            // Create Open button
            Button openButton = createButton(parent, OPEN, 
                "Open", true);
            // Initially deactivate it
            openButton.setEnabled(false);
            // Add a SelectionListener
            openButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    // Retrieve selected entries from list
                    itemsToOpen = list.getSelection();
                    // Set return code
                    setReturnCode(OPEN);
                    // Close dialog
                    close();
                }
            });
            // Create Delete button
            Button deleteButton = 
                createButton(parent, DELETE, "Delete", false);
            deleteButton.setEnabled(false);
            // Add a SelectionListener
            deleteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // Get the indices of the selected entries
                int selectedItems[] = list.getSelectionIndices();
                // Remove all these entries
                list.remove(selectedItems);
                // Now re-validate the list because it has changed
                validate();
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
    /**
     * Method getItemsToOpen.
     * @return String[] - the selected items
     */
    public String[] getItemsToOpen() {
        return itemsToOpen;
    }
}
