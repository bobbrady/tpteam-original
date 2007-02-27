package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

public class TPEntity {
	
	public enum TPEntityType  {FOLDER, JUNIT_TEST, EXEC_PASS, EXEC_FAIL};
	
	private int mID;
	
	private String mName;
	
	private String mDescription;
	
	private TPEntityType mType;
	
	private TPEntity[] mChildren = new TPEntity[0];

	private TPEntity mParent = null;
	
	public TPEntity(int id, String name, String description, TPEntityType type)
	{
		mID = id;
		mName = name;
		mDescription = description;
		mType = type;
	}
	
	public int getID()
	{
		return mID;
	}
	
	public void setID(int id)
	{
		mID = id;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public void setName(String name)
	{
		mName = name;
	}
	
	public String getDescription()
	{
		return mDescription;
	}
	
	public void setDescription(String description)
	{
		mDescription = description;
	}
	
	public TPEntityType getType()
	{
		return mType;
	}
	
	public void setType(TPEntityType type)
	{
		mType = type;
	}
	
	public TPEntity[] getChildren()
	{
		return mChildren;
	}
	
	public void setChildren(TPEntity[] children)
	{
		mChildren = children;
	}
	
	public TPEntity getParent()
	{
		return mParent;
	}
	
	public void setParent(TPEntity parent)
	{
		mParent = parent;
	}

	public boolean hasChildren()
	{
		if(mChildren == null || mChildren.length == 0)
			return false;
		return true;
	}
	
	public String toString()
	{
		return mName;
	}

}
