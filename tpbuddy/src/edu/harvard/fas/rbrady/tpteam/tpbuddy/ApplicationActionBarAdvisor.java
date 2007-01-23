/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.actions.OpenViewAction;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.EventHistoryView;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.views.TestView;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	private OpenViewAction openViewAction;
	private OpenViewAction testViewAction;
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {

    	openViewAction = new OpenViewAction(window, "Open Event History View", EventHistoryView.ID,"/icons/event_message.gif");
    	testViewAction = new OpenViewAction(window, "Open Test Plan View", TestView.ID,"icons/junit.gif");
    	
    	
    	register(testViewAction);
    	register(openViewAction);
    
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	/*
    	MenuManager testMenu = new MenuManager("&Test", "test");
    	testMenu.add(exitAction);
    	MenuManager helpMenu = new MenuManager("&Help", "help");
    	helpMenu.add(aboutAction);
    	
    	menuBar.add(testMenu);
    	menuBar.add(helpMenu);
    	*/
    }
    
    protected void fillCoolBar(ICoolBarManager coolbar)
    {
    	IToolBarManager toolbar = new ToolBarManager(coolbar.getStyle());
    	coolbar.add(toolbar);
    	toolbar.add(testViewAction);
    	toolbar.add(openViewAction);
    	//toolbar.add(exitAction);
    	
    }
    
	@SuppressWarnings("unused")
	private ImageDescriptor getImageDescriptor(String relativePath)
	{
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, relativePath);
	}

   
    
}
