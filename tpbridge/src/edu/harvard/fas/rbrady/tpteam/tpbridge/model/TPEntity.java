package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

public class TPEntity extends AbstractTreeNode {

	private static final long serialVersionUID = 1L;

	public static final String FOLDER = "FOLDER";

	public static final String JUNIT_TEST = "JUNIT";

	public static final String EXEC_PASS = "EXEC_PASS";

	public static final String EXEC_FAIL = "EXEC_FAIL";

	private String mDescription;

	private String mType;

	public TPEntity() {

	}

	public TPEntity(int id, String name, String description, String type) {
		setID(id);
		setName(name);
		mDescription = description;
		mType = type;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
		fireNodeUpdated();
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

}
