/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		//layout.addView(TestView.ID, IPageLayout.LEFT, .5f, layout.getEditorArea());
		//layout.addView(TreeTestView.ID, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
		//layout.addStandaloneView(TestView.ID, false,IPageLayout.LEFT, 1.0f, layout.getEditorArea());
	}
}
