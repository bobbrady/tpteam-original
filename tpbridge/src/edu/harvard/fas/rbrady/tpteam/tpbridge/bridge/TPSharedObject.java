/********************************************************************
 * 
 * File		:	TPSharedObject.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Implementation of the SharedObject for TPTeam 
 * 				Messaging
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.util.Observable;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.ISharedObjectConfig;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContext;
import org.eclipse.ecf.core.sharedobject.SharedObjectInitException;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectDeactivatedEvent;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.util.Event;

/*******************************************************************************
 * File 		: 	TPSharedObject.java
 * 
 * Description 	: 	Implementation of the SharedObject for TPTeam 
 * 					Messaging
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPSharedObject extends Observable implements ISharedObject {

	/** The ECF Configuration of the shared object */
    ISharedObjectConfig config = null;
    
    /**
     * Default Constructor
     */
    public TPSharedObject() {
        super();
    }
    
    /**
     * Getter 
     * @return The ECFID of the shared object
     */
    protected ID getID() {
        return config.getSharedObjectID();
    }
    
    /**
     * Getter
     * @return The ECF context of the shared object
     */
    protected ISharedObjectContext getContext() {
        if (config == null) return null;
        else return config.getContext();
    }

    /**
     * Initializes the shared object
     */
    public void init(ISharedObjectConfig initData)
            throws SharedObjectInitException {
        this.config = initData;
    }

    /**
     * Handles the all possible ECF Events that the shared object
     * may experience from its ECF communications container
     */
    public void handleEvent(Event event) {
    	// First handle info type events
        if (event instanceof ISharedObjectActivatedEvent) {
            System.out.println("HELLO WORLD from "+getID()+".  I'm activated!");
        } else if (event instanceof ISharedObjectDeactivatedEvent) {
            System.out.println("GOODBYE from "+getID()+".  I'm deactivated!");
        } else if (event instanceof IContainerConnectedEvent) {
            System.out.println("Remote "+((IContainerConnectedEvent)event).getTargetID()+" joined!");
        } else if (event instanceof IContainerDisconnectedEvent) {
            System.out.println("Remote "+((IContainerDisconnectedEvent)event).getTargetID()+" departed!");
        // Now handle TPTeam Events that need to be acted upon by observing objects
        } else if (event instanceof ISharedObjectMessageEvent) {
            setChanged();
            notifyObservers(event);
        }
    }

    /**
     * Handles many ECF Events at once
     */
    public void handleEvents(Event[] events) {
        for(int i=0; i < events.length; i++) {
            this.handleEvent(events[i]);
        }
    }

    /**
     * Dispose of this shared object
     */
    public void dispose(ID containerID) {
        this.config = null;
    }

    /**
     * Required ECF ISharedObject interface method, not used.
     */
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class clazz) {
        return null;
    }
}
