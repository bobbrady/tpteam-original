package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TPEntity {
	
	public static final String FOLDER = "FOLDER";
	
	public static final String JUNIT_TEST = "JUNIT";
	
	public static final String EXEC_PASS = "EXEC_PASS";
	
	public static final String EXEC_FAIL = "EXEC_FAIL";
	
	private int mID;
	
	private String mName;
	
	private String mDescription;
	
	private String mType;
	
	private List<TPEntity> mChildren = new ArrayList<TPEntity>();

	private TPEntity mParent = null;
	
	public TPEntity()
	{
		
	}
	
	public TPEntity(int id, String name, String description, String type)
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
	
	public String getType()
	{
		return mType;
	}
	
	public void setType(String type)
	{
		mType = type;
	}
	
	public TPEntity[] getChildren()
	{
		return mChildren.toArray(new TPEntity[0]);
	}
	
	public void setChildren(TPEntity[] children)
	{
		for(TPEntity child : children)
			mChildren.add(child);
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
		if(mChildren == null || mChildren.size() < 1)
			return false;
		return true;
	}
	
	public void addChild(TPEntity child)
	{
		mChildren.add(child);
	}
	
	public boolean removeChild(TPEntity child)
	{
		return mChildren.remove(child);
	}
	
	public boolean removeChild(int childID)
	{
		for(TPEntity childEnt : mChildren)
		{
			if(childEnt.getID() == childID)
				return removeChild(childEnt);
		}
		return false;
	}
	
	public String toString()
	{
		return mName;
	}

}
