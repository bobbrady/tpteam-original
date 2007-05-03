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

public class DetailView extends ViewPart implements Observer {
	public static final String ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy.views.detailview";

	private Table mTable;

	private TableViewer mTableViewer;

	public DetailView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		Activator.getDefault().getEventAdminHandler().addObserver(this);
		initTableViewer(parent);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void initTableViewer(Composite parent) {
		mTableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		initTable();
		initColumns();

		mTableViewer.setLabelProvider(new DetailLabelProvider());
		mTableViewer.setContentProvider(new ArrayContentProvider());
		//mTableViewer.setInput(getSamples());
	}

	private void initTable() {
		mTable = mTableViewer.getTable();
		mTable.setHeaderVisible(true);
		mTable.setLinesVisible(true);
	}

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
