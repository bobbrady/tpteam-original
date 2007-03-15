/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

public class TestContentProvider extends ArrayContentProvider implements
		ITreeContentProvider, ITreeNodeChangeListener {

	private TreeViewer mTreeViewer;

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		mTreeViewer = (TreeViewer) viewer;
		if (oldInput != null) {
			for (ITreeNode node : (TPEntity[]) oldInput)
				removeListenerFrom(node);
		}
		if (newInput != null) {
			for (ITreeNode node : (TPEntity[]) newInput)
				addListenerTo(node);
		}
	}

	/**
	 * Because the domain model does not have a richer listener model,
	 * recursively remove this listener from each child box of the given box.
	 */
	protected void removeListenerFrom(ITreeNode node) {
		node.removeChangeListener(this);
		for (ITreeNode child : node.getChildren()) {
			removeListenerFrom(child);
		}
	}

	/**
	 * Because the domain model does not have a richer listener model,
	 * recursively add this listener to each child box of the given box.
	 */
	protected void addListenerTo(ITreeNode node) {
		node.addChangeListener(this);
		for (ITreeNode child : node.getChildren()) {
			addListenerTo(child);
		}
	}

	public Object getParent(Object child) {
		return ((ITreeNode) child).getParent();
	}

	public Object[] getChildren(Object parent) {
		return ((ITreeNode) parent).getChildren().toArray(new ITreeNode[0]);
	}

	public boolean hasChildren(Object parent) {
		return ((ITreeNode) parent).getChildren().size() > 0;
	}

	public void updateNode(ITreeNode node) {
		mTreeViewer.refresh(node.getParent(), true);
	}

	public void addNode(ITreeNode node) {
		mTreeViewer.refresh(node.getParent(), false);
	}

	public void deleteNode(ITreeNode node) {
		System.out.println("TestContentProvider Got message to delete node: " + node.getName());
		mTreeViewer.refresh(node.getParent(), true);
	}

}