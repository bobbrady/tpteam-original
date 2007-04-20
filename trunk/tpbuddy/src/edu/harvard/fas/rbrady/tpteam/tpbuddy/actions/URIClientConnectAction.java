/*******************************************************************************
 * Copyright (c) 2005 Ed Burnette, Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ed Burnette, Composent, Inc. - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge.TPBridgeClient;

public class URIClientConnectAction implements IWorkbenchWindowActionDelegate {
    
    protected String containerType = null;
    protected String uri = null;
    protected String nickname = null;
    protected Object data = null;
    protected String projectName = null;
    protected TPBridgeClient mTPBridgeClient = null;

    public URIClientConnectAction() {
    }
    
    public URIClientConnectAction(TPBridgeClient tpBridgeClient) {
    	mTPBridgeClient = tpBridgeClient;
    }
    
    public URIClientConnectAction(String containerType, String uri, String nickname, Object data) {
    	this();
    	this.containerType = containerType;
    	this.uri = uri;
    	this.nickname = nickname;
        this.data = data;
    }
    public class ClientMultiStatus extends MultiStatus {

		public ClientMultiStatus(String pluginId, int code, IStatus[] newChildren, String message, Throwable exception) {
			super(pluginId, code, newChildren, message, exception);
		}
		public ClientMultiStatus(String pluginId, int code, String message, Throwable exception) {
			super(pluginId, code, message, exception);
		}
    }
    protected void showExceptionInMultiStatus(int code, MultiStatus status, Throwable t) {
    	String msg = t.getMessage();
    	status.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,code++,msg,null));
    	StackTraceElement [] stack = t.getStackTrace();
    	for(int i=0; i < stack.length; i++) {
    		status.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,code++,"     "+stack[i],null));
    	}
    	Throwable cause = t.getCause();
    	if (cause != null) {
    		status.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,code++,"Caused By: ",null));
    		showExceptionInMultiStatus(code,status,cause);
    	}
    }
	public class ClientConnectJob extends Job {
        public ClientConnectJob(String name) {
            super(name);
        }
        public IStatus run(IProgressMonitor pm) {
        	String failMsg = "Connect to "+uri+" failed";
        	ClientMultiStatus status = new ClientMultiStatus(Activator.PLUGIN_ID, 0,
                    failMsg, null);
            try {
            	TPBridgeClient client = mTPBridgeClient;
                client.createAndConnectClient(containerType, uri,nickname, data, false);
                return status;
            } catch (final Exception e) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						String msg = "ECF Login Error: " + e.getMessage() + ". Please try again.";
						MultiStatus info = new MultiStatus(Activator.PLUGIN_ID, 1, msg, null);
						for(StackTraceElement trace : e.getStackTrace())
						{
							info.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 1, trace.toString(), null));
						}
		            	ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "ECF Login Error", null, info);
					}
				});

            	showExceptionInMultiStatus(15555,status,e);
                return status;
            }
        }        
    }
	public void run(IAction action) {
        ClientConnectJob clientConnect = new ClientConnectJob("Connect for "+projectName);
        clientConnect.schedule();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {

	}

	public void init(IWorkbenchWindow window) {
	}
	
	public void setTPBridgeClient(TPBridgeClient tpBridgeClient)
	{
		mTPBridgeClient = tpBridgeClient;
	}
}