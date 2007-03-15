package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

public interface ITreeNodeChangeListener {
	
	void updateNode(ITreeNode node);
	
	void addNode(ITreeNode node);
	
	void deleteNode(ITreeNode node);

}
