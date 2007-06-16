/********************************************************************
 * 
 * File		:	DetailLabelProvider.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides the text and images for the columns in the 
 * 				Test Details View table
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

/*******************************************************************************
 * File 		: 	DetailLabelProvider.java
 * 
 * Description 	: 	Provides the text and images for the columns in the 
 * 					Test Details View table
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DetailLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private HashMap<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();

	/**
	 * Removes images from the cache
	 */
	@SuppressWarnings("unchecked")
	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	/**
	 * Extracts table column text
	 * @param element the Object to extract text from
	 * @param index the column index
	 */
	public String getColumnText(Object element, int index) {
		TPEntity tpEntity = (TPEntity) element;
		switch (index) {
		case 0:
			return tpEntity.getType();
		case 1:
			return tpEntity.getDescription();
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
		TPEntity tpEntity = (TPEntity) element;
		Image image = null;
		if (tpEntity.getDescription() == null || columnIndex == 1)
			return image;

		if (tpEntity.getDescription().equals(TPEntity.FOLDER)) {
			image = PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_FOLDER);
		} else if (tpEntity.getDescription().equals(TPEntity.JUNIT_TEST)) {
			image = getImageFromString("icons/junit.gif");
		} else if (tpEntity.getType().equals(TPEntity.EXEC_PASS)) {
			image = getImageFromString("icons/testok.gif");
		} else if (tpEntity.getType().equals(TPEntity.EXEC_FAIL)) {
			image = getImageFromString("icons/testfail.gif");
		} else if (tpEntity.getType().equals(TPEntity.EXEC_ERROR)) {
			image = getImageFromString("icons/testerr.gif");
		} else if (tpEntity.getType().equals(TPEntity.EXEC_INCONCLUSIVE)) {
			image = getImageFromString("icons/testignored.gif");
		} else if (tpEntity.getType().toUpperCase().indexOf("ECLIPSE") >= 0) {
			image = getImageFromString("icons/eclipse.gif");
		} else if (tpEntity.getType().toUpperCase().indexOf("TPTP") >= 0) {
			image = getImageFromString("icons/eclipse.gif");
		}

		return image;
	}

	/**
	 * Helper method to get an image from a String
	 * type descriptor
	 * 
	 * @param type String type of image
	 * @return the image
	 */
	private Image getImageFromString(String type) {
		ImageDescriptor descriptor = Activator.getImageDescriptor(type);
		// obtain the cached image corresponding to the descriptor
		Image image = (Image) imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}
		return image;
	}

}