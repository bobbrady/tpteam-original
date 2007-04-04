/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.DetailView;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.EventHistoryView;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.ProjectView;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.ReportView;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.TestView;


public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);

        //layout.createPlaceholderFolder(IPageLayout.ID_EDITOR_AREA, IPageLayout.LEFT, 1.00f, layout.getEditorArea());
		 IFolderLayout topFolder = layout.createFolder("top", IPageLayout.TOP, 0.65f, layout.getEditorArea());
		 topFolder.addView(ProjectView.ID);
		 topFolder.addView(ReportView.ID);
		 topFolder.addView(TestView.ID);
		 topFolder.addView(DetailView.ID);
		//layout.addView(TestView.ID, IPageLayout.TOP, 0.65f, layout.getEditorArea());
		layout.addView("org.eclipse.ecf.ui.view.rosterview", IPageLayout.RIGHT, 0.7f, "top");
		layout.addView(EventHistoryView.ID, IPageLayout.BOTTOM, 0.35f, layout.getEditorArea());
		//layout.addStandaloneView(TestView.ID, false,IPageLayout.LEFT, 1.0f, layout.getEditorArea());

		/*
        layout.createPlaceholderFolder(EDITOR_AREA, IPageLayout.LEFT, 1.00f, IPageLayout.ID_EDITOR_AREA);
        layout.createPlaceholderFolder("left", IPageLayout.LEFT, 0.20f, EDITOR_AREA);
        layout.createPlaceholderFolder("bottom", IPageLayout.BOTTOM, 0.80f, EDITOR_AREA);
        layout.createPlaceholderFolder("right", IPageLayout.RIGHT, 0.70f, EDITOR_AREA);
        */
		/*
		 IFolderLayout topFolder = layout.createFolder("top", IPageLayout.TOP, 0.7f, layout.getEditorArea());
		 topFolder.addView(TestView.ID);
		 topFolder.addView("org.eclipse.ecf.ui.view.rosterview");
		 layout.addView(EventHistoryView.ID, IPageLayout.BOTTOM, 0.3f, layout.getEditorArea());
		 */

	}
}
