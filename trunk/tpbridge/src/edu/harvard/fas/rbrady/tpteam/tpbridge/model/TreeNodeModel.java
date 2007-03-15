package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.util.HashMap;

public class TreeNodeModel extends HashMap<String, ITreeNode> implements ITreeNodeChangeListener {

	private static final long serialVersionUID = 1L;
	
	public ITreeNode put(String key, ITreeNode node)
	{
		node.addChangeListener(this);
		return super.put(key, node);
	}
	
	public ITreeNode remove(String key, ITreeNode node)
	{
		node.removeChangeListener(this);
		return super.remove(key);
	}

	public void addNode(ITreeNode node) {
		addListenerTo(node);
		put(String.valueOf(((TPEntity)node).getID()), node);
	}

	public void deleteNode(ITreeNode node) {
		if(get(String.valueOf(((TPEntity)node).getID())) != null)
		{
			System.out.println("TreeNodeModel Got message to delete node: " + node.getName());
			removeListenerFrom(node);
			remove(String.valueOf(((TPEntity)node).getID()));
			for(ITreeNode child : node.getChildren())
			{
				deleteNode(child);
			}
		}
	}

	public void updateNode(ITreeNode node) {
		//NOOP
	}
	
	public void clear()
	{
		for(String key : keySet())
		{
			removeListenerFrom(get(key));
		}
		super.clear();
	}
	
	protected void removeListenerFrom(ITreeNode node) {
		node.removeChangeListener(this);
		for (ITreeNode child : node.getChildren()) {
			removeListenerFrom(child);
		}
	}
	
	
	protected void addListenerTo(ITreeNode node) {
		node.addChangeListener(this);
		for (ITreeNode child : node.getChildren()) {
			addListenerTo(child);
		}
	}

}
