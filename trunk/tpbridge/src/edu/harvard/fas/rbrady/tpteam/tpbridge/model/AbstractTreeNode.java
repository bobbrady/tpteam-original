/********************************************************************
 * 
 * File		:	AbstractTreeNode.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Base class for tree nodes
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*******************************************************************************
 * File 		:  	AbstractTreeNode.java
 * 
 * Description 	: 	Base class for tree nodes
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public abstract class AbstractTreeNode implements ITreeNode {

	/** The TPTeam ID of the tree node */
	private String mID;
	/** The TPTeam name of the tree node */
	private String mName;
	/** The parent of this tree node */
	private ITreeNode mParent = null;
	/** List of all listeners of this tree node */
	private List<ITreeNodeChangeListener> mChangeListeners;
	/** The children of this tree node */
	private List<ITreeNode> mChildren = new ArrayList<ITreeNode>();
	
	// Property accessors
	
	public List<ITreeNode> getChildren() {
		return mChildren;
	}

	public void setChildren(List<ITreeNode> children) {
		for (ITreeNode child : children)
			mChildren.add(child);
	}

	public boolean hasChildren() {
		if (mChildren == null || mChildren.size() < 1)
			return false;
		return true;
	}

	public void addChild(ITreeNode child) {
		mChildren.add(child);
		fireNodeAdded(child);
	}

	public boolean removeChild(ITreeNode child) {
		boolean isRemoved = mChildren.remove(child);
		if (isRemoved)
			fireNodeDeleted(child);
		return isRemoved;
	}

	public boolean removeChild(int childID) {
		for (ITreeNode childEnt : mChildren) {
			if (((TPEntity) childEnt).getID().equals(childID)) {
				boolean isRemoved = removeChild(childEnt);
				if (isRemoved) {
					fireNodeDeleted(childEnt);
					return isRemoved;
				}
			}
		}
		return false;
	}


	public String getID() {
		return mID;
	}

	public void setID(String id) {
		mID = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
		fireNodeUpdated();
	}

	public ITreeNode getParent() {
		return mParent;
	}

	public void setParent(ITreeNode parent) {
		mParent = parent;
	}

	public String toString() {
		return mName;
	}

	public void addChangeListener(ITreeNodeChangeListener listener) {
		if (mChangeListeners == null)
			mChangeListeners = new ArrayList<ITreeNodeChangeListener>();

		/* if listener already exists, then do not add */
		if (mChangeListeners.contains(listener))
			return;

		mChangeListeners.add(listener);
	}

	public void removeChangeListener(ITreeNodeChangeListener listener) {
		if (mChangeListeners == null)
			return;

		mChangeListeners.remove(listener);
	}

	protected List<ITreeNodeChangeListener> getChangedListeners() {
		if (mChangeListeners == null) {
			return Collections.emptyList();
		}
		return mChangeListeners;
	}

	/**
	 * Notifies all listeners when this node has had
	 * one of its properties updated
	 */
	protected void fireNodeUpdated() {
		for (ITreeNodeChangeListener listener : getChangedListeners())
			listener.updateNode(this);
	}

	/**
	 * Notifies all listeners when this node adds 
	 * a child node
	 * 
	 * @param child the child tree node to be added
	 */
	protected void fireNodeAdded(ITreeNode child) {
		for (ITreeNodeChangeListener listener : getChangedListeners())
			listener.addNode(child);
	}

	/**
	 * Notifies all listeners when this node deletes
	 * a child node
	 * 
	 * @param child the child tree node to be deleted
	 */
	protected void fireNodeDeleted(ITreeNode child) {
		for (ITreeNodeChangeListener listener : getChangedListeners()) {
			System.out.println("Node " + this.getName() + " fireNodeDeleted");
			listener.deleteNode(child);
		}
	}

}
