/********************************************************************
 * 
 * File		:	ITreeNodeChangeListener.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	The interface for listeners of ITreeNode 
 * 				implementations 
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

/*******************************************************************************
 * File 		:  	ITreeNodeChangeListener.java
 * 
 * Description 	: 	The interface for listeners of ITreeNode 
 * 					implementations 
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public interface ITreeNodeChangeListener {
	
	/**
	 * Performs actions when an ITreeNode
	 * updates on of its properties
	 * 
	 * @param node the ITreeNode that was updated
	 */
	void updateNode(ITreeNode node);
	
	/**
	 * Performs actions when an ITreeNode
	 * adds a child node
	 * 
	 * @param node the child node that was added
	 */
	void addNode(ITreeNode node);

	/**
	 * Performs actions when an ITreeNode
	 * deletes a child node
	 * @param node the child node that was deleted
	 */
	void deleteNode(ITreeNode node);
}
