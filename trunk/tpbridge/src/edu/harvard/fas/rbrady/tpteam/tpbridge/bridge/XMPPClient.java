/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IDInstantiationException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IMessageListener;
import org.eclipse.ecf.presence.IMessageSender;
import org.eclipse.ecf.presence.IPresenceContainer;
import org.eclipse.ecf.presence.IPresenceListener;
import org.eclipse.ecf.provider.xmpp.container.XMPPClientSOContainer;

public class XMPPClient {
	
	protected static String CONTAINER_TYPE = "ecf.xmpp.smack";
	
	Namespace namespace = null;
	XMPPClientSOContainer container = null;
	IPresenceContainer presence = null;
	IMessageSender sender = null;
	ID userID = null;
	
	// Interface for receiving messages
	IMessageReceiver receiver = null;
	IPresenceListener presenceListener = null;
	
	public XMPPClient() {
		this(null);
	}
	
	public XMPPClient(IMessageReceiver receiver) {
		super();
		setMessageReceiver(receiver);
	}
	public XMPPClient(IMessageReceiver receiver, IPresenceListener presenceListener) {
		this(receiver);
		setPresenceListener(presenceListener);
	}
	protected void setMessageReceiver(IMessageReceiver receiver) {
		this.receiver = receiver;
	}
	protected void setPresenceListener(IPresenceListener listener) {
		this.presenceListener = listener;
	}
	protected XMPPClientSOContainer setupContainer() throws ECFException {
		try
		{
		if (container == null) {
			//container = ContainerFactory.getDefault().createContainer(CONTAINER_TYPE);
			container = new XMPPClientSOContainer();
			namespace = container.getConnectNamespace();
		}
		}
		catch(Exception e)
		{
			if(container != null)
				container.dispose();
		}
		return container;
	}
	protected XMPPClientSOContainer getContainer() {
		return container;
	}
	protected Namespace getConnectNamespace() {
		return namespace;
	}
	protected void setupPresence() throws ECFException {
		if (presence == null) {
			presence = (IPresenceContainer) container
			.getAdapter(IPresenceContainer.class);
			sender = presence.getMessageSender();
			presence.addMessageListener(new IMessageListener() {
				public void handleMessage(ID fromID, ID toID, Type type,
						String subject, String messageBody) {
					if (receiver != null) {
						receiver.handleMessage(fromID.getName(), messageBody);
					}
				}
			});
			if (presenceListener != null) {
				presence.addPresenceListener(presenceListener);
			}
		}
	}
	public void connect(String account, String password) throws ECFException {
		setupContainer();
		setupPresence();
		doConnect(account,password);
	}
	
	protected void doConnect(String account, String password) throws ECFException  {
		// Now connect
		ID targetID = IDFactory.getDefault().createID(namespace, account);
		container.connect(targetID,ConnectContextFactory.createPasswordConnectContext(password));
		userID = getID(account);
	}
	public ID getID(String name) {
		try {
			return IDFactory.getDefault().createID(namespace, name);
		} catch (IDInstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void sendMessage(String jid, String msg) {
		if (sender != null) {
			sender.sendMessage(userID, getID(jid),
					IMessageListener.Type.NORMAL, "", msg);
		}
	}
	public synchronized boolean isConnected() {
		if (container == null) return false;
		
		return (container.getConnectedID() != null);
	}
	public synchronized void close() {
		if (container != null) {
			container.dispose();
			container = null;
			presence = null;
			sender = null;
			receiver = null;
			userID = null;
		}
	}
	

}
