/********************************************************************
 * 
 * File		:	ITreeNode.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	The interface for TreeNode objects 
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.io.Serializable;
import java.util.List;

/*******************************************************************************
 * File 		:  	ITreeNode.java
 * 
 * Description 	: 	An O(1) look-up container for TPTeam ITreeNode 
 * 					objects
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public interface ITreeNode extends Serializable {
	// Propert accessors
	public String getID();
	public void setID(String id);
	public String getName();
	public void setName(String name);
	public List<ITreeNode> getChildren();
	public void setChildren(List<ITreeNode> children);
	public void addChild(ITreeNode child);
	public boolean removeChild(ITreeNode child);
	public boolean removeChild(int childID);
	public boolean hasChildren();
	public ITreeNode getParent();
	public void setParent(ITreeNode parent);
	public String toString();
	public void addChangeListener(ITreeNodeChangeListener listener);
	public void removeChangeListener(ITreeNodeChangeListener listener);
}
