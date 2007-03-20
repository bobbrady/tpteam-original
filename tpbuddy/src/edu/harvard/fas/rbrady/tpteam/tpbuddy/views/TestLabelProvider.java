/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
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

public class TestLabelProvider extends LabelProvider {

	private HashMap<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();

	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	public String getText(Object obj) {
		if (obj instanceof TPEntity) {
			return ((TPEntity) obj).getName();
		}
		return obj.toString();
	}

	public Image getImage(Object element) {

		Image image = null;

		TPEntity treeEnt = (TPEntity) element;

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
