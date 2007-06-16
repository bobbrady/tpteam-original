/********************************************************************
 * 
 * File		:	DetailView.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides the view of a Test's details
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;

/*******************************************************************************
 * File 		: 	DetailView.java
 * 
 * Description 	: 	Provides the view of a Test's details
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DetailView extends ViewPart implements Observer {
	/** The view ID */
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.detailview";
	/** The encapsulated table for rendering test details */
	private Table mTable;
	/** The TableViewer for rendering test details */
	private TableViewer mTableViewer;

	/**
	 * Constructor
	 */
	public DetailView() {
	}

	/**
	 * Initializes the view by adding it as an observer to the
	 * TPBuddy EventAdminHandler and creating its TableViewer
	 * @param parent the GUI parent to this view
	 */
	@Override
	public void createPartControl(Composite parent) {
		Activator.getDefault().getEventAdminHandler().addObserver(this);
		initTableViewer(parent);
	}

	@Override
	public void setFocus() {

	}

	/**
	 * Initializes the TableView with label and content
	 * providers 
	 * @param parent the Composite parent to the view
	 */
	private void initTableViewer(Composite parent) {
		mTableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		initTable();
		initColumns();
		mTableViewer.setLabelProvider(new DetailLabelProvider());
		mTableViewer.setContentProvider(new ArrayContentProvider());
	}

	/**
	 * Helper method to set header & lines visible
	 */
	private void initTable() {
		mTable = mTableViewer.getTable();
		mTable.setHeaderVisible(true);
		mTable.setLinesVisible(true);
	}

	/**
	 * Helper method to set column properties
	 */
	private void initColumns() {
		String[] columnNames = new String[] { "Property", "Value" };
		int[] columnWidths = new int[] { 150, 500 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(mTable,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}
	}

	/**
	 * Update called when TPBuddy EventAdminHandler receives 
	 * a test detail response TPEvent
	 * 
	 * If the event is a test details response, then this view
	 * will extract the TPEntity and render its details
	 * 
	 * @param observable the object that called the update
	 * @param object the TPEvent to be handled
	 */
	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			System.out.println("DetailView Got Update: " + tpEvent.getTopic());
			if (tpEvent.getTopic().equals(ITPBridge.TEST_DETAIL_RESP_TOPIC)) {

				System.out.println("DetailView: update called for "
						+ tpEvent.getTopic() + " Event for "
						+ tpEvent.getID());

				String testPropXML = tpEvent.getDictionary().get(
						TPEvent.TEST_PROP_XML_KEY);
				
				System.out.println(testPropXML);
				
				final TPEntity[] tpEntities = TestXML.getTPEntitiesFromXML(testPropXML);
				
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						mTableViewer.setInput(tpEntities);
					}
				});
			}
		}
	}
}
