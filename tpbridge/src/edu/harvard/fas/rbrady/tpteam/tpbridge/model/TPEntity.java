package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

public class TPEntity extends AbstractTreeNode {

	private static final long serialVersionUID = 1L;

	public static final String FOLDER = "FOLDER";

	public static final String JUNIT_TEST = "JUNIT";

	public static final String PASS = "PASS";

	public static final String FAIL = "FAIL";

	public static final String INCONCLUSIVE = "INCONCLUSIVE";

	public static final String ERROR = "ERROR";

	public static final String EXEC_PASS = "EXEC_" + PASS;

	public static final String EXEC_FAIL = "EXEC_" + FAIL;

	public static final String EXEC_ERROR = "EXEC_" + ERROR;

	public static final String EXEC_INCONCLUSIVE = "EXEC_" + INCONCLUSIVE;

	public static final String EXEC = "EXEC";

	private String mDescription;

	private String mType;

	public TPEntity() {

	}

	public TPEntity(String id, String name, String description, String type) {
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
