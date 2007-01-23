/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.util.Observable;

import org.eclipse.ecf.core.ISharedObject;
import org.eclipse.ecf.core.ISharedObjectConfig;
import org.eclipse.ecf.core.ISharedObjectContext;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectDeactivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.Event;

public class TPSharedObject extends Observable implements ISharedObject {

    ISharedObjectConfig config = null;
    
    public TPSharedObject() {
        super();
    }
    protected ID getID() {
        return config.getSharedObjectID();
    }
    protected ISharedObjectContext getContext() {
        if (config == null) return null;
        else return config.getContext();
    }
    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#init(org.eclipse.ecf.core.ISharedObjectConfig)
     */
    public void init(ISharedObjectConfig initData)
            throws SharedObjectInitException {
        this.config = initData;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#handleEvent(org.eclipse.ecf.core.util.Event)
     */
    public void handleEvent(Event event) {
        if (event instanceof ISharedObjectActivatedEvent) {
            System.out.println("HELLO WORLD from "+getID()+".  I'm activated!");
        } else if (event instanceof ISharedObjectDeactivatedEvent) {
            System.out.println("GOODBYE from "+getID()+".  I'm deactivated!");
        } else if (event instanceof IContainerConnectedEvent) {
            System.out.println("Remote "+((IContainerConnectedEvent)event).getTargetID()+" joined!");
        } else if (event instanceof IContainerDisconnectedEvent) {
            System.out.println("Remote "+((IContainerDisconnectedEvent)event).getTargetID()+" departed!");
        } else if (event instanceof ISharedObjectMessageEvent) {
        	/*
            ISharedObjectMessageEvent evt = (ISharedObjectMessageEvent) event;
            System.out.println(getID() + " Got message "+evt.getData()+" from "+evt.getRemoteContainerID());
           */
            setChanged();
            notifyObservers(event);
        }
        /*
        ID[] objectIDs = getContext().getSharedObjectManager().getSharedObjectIDs();
        for(ID myID : objectIDs)
        {
        	System.out.println("SharedObjectMgr, SharedObject ID: " + myID.getName() );
        }
        System.out.println("\n\n");
        */
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#handleEvents(org.eclipse.ecf.core.util.Event[])
     */
    public void handleEvents(Event[] events) {
        for(int i=0; i < events.length; i++) {
            this.handleEvent(events[i]);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#dispose(org.eclipse.ecf.core.identity.ID)
     */
    public void dispose(ID containerID) {
        this.config = null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObject#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class clazz) {
        return null;
    }
}
