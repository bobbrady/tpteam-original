/********************************************************************
 * 
 * File		:	TestLabelProvider.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides the text and images for the columns in the 
 * 				Test View tree
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

/*******************************************************************************
 * File 		: 	TestLabelProvider.java
 * 
 * Description 	: 	Provides the text and images for the columns in the 
 * 					Test View tree
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TestLabelProvider extends LabelProvider {
	/** A cache for images used in the test tree */
	private HashMap<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();

	/**
	 * Clears the image cache
	 */
	@SuppressWarnings("unchecked")
	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	/**
	 * Extracts the tree node name from 
	 * a given object
	 * 
	 * @param obj the object
	 */
	public String getText(Object obj) {
		if (obj instanceof TPEntity) {
			return ((TPEntity) obj).getName();
		}
		return obj.toString();
	}

	/**
	 * Gets the appropriate image corresponding to
	 * the given tree node object
	 * 
	 * @param element the object
	 */
	public Image getImage(Object element) {
		Image image = null;
		TPEntity treeEnt = (TPEntity) element;
		/* Get the appropriate image based upon the
		 * the type of TPEntity 
		 */
		if (treeEnt.getType().equals(TPEntity.FOLDER)) {
			image = PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_FOLDER);
		} else if (treeEnt.getType().equals(TPEntity.JUNIT_TEST)) {
			image = getImageFromString("icons/junit.gif");
		} else if (treeEnt.getType().equals(TPEntity.EXEC_PASS)) {
			image = getImageFromString("icons/testok.gif");
		} else if (treeEnt.getType().equals(TPEntity.EXEC_FAIL)) {
			image = getImageFromString("icons/testfail.gif");
		} else if (treeEnt.getType().equals(TPEntity.EXEC_ERROR)) {
			image = getImageFromString("icons/testerr.gif");
		} else if (treeEnt.getType().equals(TPEntity.EXEC_INCONCLUSIVE)) {
			image = getImageFromString("icons/testignored.gif");
		}
		return image;
	}

	/**
	 * Helper method to return an image based
	 * upon a given descriptor
	 * @param type the descriptor
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
