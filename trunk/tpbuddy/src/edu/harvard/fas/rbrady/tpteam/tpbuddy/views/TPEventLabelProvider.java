/********************************************************************
 * 
 * File		:	TPEventLabelProvider.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides the text and images for the columns in the 
 * 				EventHistoryView table
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	TPEventLabelProvider.java
 * 
 * Description 	: 	Provides the text and images for the columns in the 
 * 					EventHistoryView table
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPEventLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	/**
	 * Extracts table column text
	 * @param element the Object to extract text from
	 * @param index the column index
	 */
	public String getColumnText(Object element, int index) {
		TPEvent tpEvent = (TPEvent) element;
		switch (index) {
		case 0:
			return tpEvent.getDictionary().get(TPEvent.FROM);
		case 1:
			return tpEvent.getDictionary().get(TPEvent.SEND_TO);
		case 2:
			String[] topicPath = tpEvent.getTopic().split("/");
			return topicPath[topicPath.length-1];
		case 3:
			return tpEvent.getProject();
		case 4:
			return tpEvent.getTestName();
		case 5:
			return tpEvent.getID();
		case 6:
			return tpEvent.getStatus();
		default:
			return "unknown " + index;
		}
	}

	/**
	 * Gets the appropriate column image
	 * 
	 * @param element the object to get column image 
	 * @param columnIndex the column index
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}