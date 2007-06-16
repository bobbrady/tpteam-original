/********************************************************************
 * 
 * File		:	ProjectLabelProvider.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides the text and images for the columns in the 
 * 				Project View table
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;

/*******************************************************************************
 * File 		: 	ProjectLabelProvider.java
 * 
 * Description 	: 	Provides the text and images for the columns in the 
 * 					Project View table
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ProjectLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	/**
	 * Extracts table column text
	 * @param element the Object to extract text from
	 * @param index the column index
	 */
	public String getColumnText(Object element, int index) {
		Project proj = (Project) element;
		switch (index) {
		case 0:
			return String.valueOf(proj.getId());
		case 1:
			return proj.getName();
		case 2:
			return proj.getDescription();
		case 3:
			return proj.getProduct().getName();
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