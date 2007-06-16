/********************************************************************
 * 
 * File		:	TreeNodeModel.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A container for ITreeNode objects that provides
 * 				O(1) look-ups and listens for ITreeNode events for
 * 				automated updating of its model
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.util.HashMap;

/*******************************************************************************
* File 			:  	TreeNodeModel.java
* 
* Description 	: 	A container for ITreeNode objects that provides
* 					O(1) look-ups and listens for ITreeNode events for
* 					automated updating of its model
* 
* @author Bob Brady, rpbrady@gmail.com
* @version $Revision$
* @date $Date$ Copyright (c) 2007 Bob Brady
******************************************************************************/
public class TreeNodeModel extends HashMap<String, ITreeNode> implements
		ITreeNodeChangeListener {

	private static final long serialVersionUID = 1L;

	// Public accessors
	
	public ITreeNode put(String key, ITreeNode node) {
		node.addChangeListener(this);
		return super.put(key, node);
	}

	public ITreeNode remove(String key, ITreeNode node) {
		node.removeChangeListener(this);
		return super.remove(key);
	}

	public void addNode(ITreeNode node) {
		addListenerTo(node);
		put(String.valueOf(node.getID()), node);
		for (ITreeNode child : node.getChildren()) {
			addNode(child);
		}
	}

	public void deleteNode(ITreeNode node) {
		if (get(String.valueOf(node.getID())) != null) {
			removeListenerFrom(node);
			remove(String.valueOf(node.getID()));
			for (ITreeNode child : node.getChildren()) {
				deleteNode(child);
			}
		}
	}

	public void updateNode(ITreeNode node) {
		// NOOP
	}

	/**
	 * Removes this TreeNodeModel as a listener
	 * from all contained nodes, then clears its
	 * HashMap
	 */
	public void clear() {
		for (String key : keySet()) {
			removeListenerFrom(get(key));
		}
		super.clear();
	}

	/**
	 * Removes this TreeNodeModel as a listener 
	 * from the given ITreeNode
	 * 
	 * @param node the ITreeNode
	 */
	protected void removeListenerFrom(ITreeNode node) {
		node.removeChangeListener(this);
		for (ITreeNode child : node.getChildren()) {
			removeListenerFrom(child);
		}
	}

	/**
	 * Adds this TreeNodeModel as a listener 
	 * to the given ITreeNode
	 * 
	 * @param node the ITreeNode
	 */
	protected void addListenerTo(ITreeNode node) {
		node.addChangeListener(this);
		for (ITreeNode child : node.getChildren()) {
			addListenerTo(child);
		}
	}

}
