/********************************************************************
 * 
 * File		:	TPEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A generic TPTeam entity use in XML serializaion and
 * 				GUI displays
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

/*******************************************************************************
* File 			:  	TPEntity.java
* 
* Description 	: 	A generic TPTeam entity use in XML serializaion and
* 					GUI displays
* 
* @author Bob Brady, rpbrady@gmail.com
* @version $Revision$
* @date $Date$ Copyright (c) 2007 Bob Brady
******************************************************************************/
public class TPEntity extends AbstractTreeNode {

	private static final long serialVersionUID = 1L;

	/** TPTeam folder test type */
	public static final String FOLDER = "FOLDER";
	/** TPTeam JUnit test type */
	public static final String JUNIT_TEST = "JUNIT";
	/** TPTeam test execution type */
	public static final String EXEC = "EXEC";
	/** TPTeam test execution passed verdict */
	public static final String PASS = "PASS";
	/** TPTeam test execution failed verdict */
	public static final String FAIL = "FAIL";
	/** TPTeam test execution inconclusive verdict */
	public static final String INCONCLUSIVE = "INCONCLUSIVE";
	/** TPTeam test execution error verdict */
	public static final String ERROR = "ERROR";
	/** TPTeam test execution passed verdict for execution entity */
	public static final String EXEC_PASS = "EXEC_" + PASS;
	/** TPTeam test execution failed verdict for execution entity */
	public static final String EXEC_FAIL = "EXEC_" + FAIL;
	/** TPTeam test execution error verdict for execution entity */
	public static final String EXEC_ERROR = "EXEC_" + ERROR;
	/** TPTeam test execution inconclusive verdict for execution entity */
	public static final String EXEC_INCONCLUSIVE = "EXEC_" + INCONCLUSIVE;
	/** Description of entity from TPTeam database */
	private String mDescription;
	/** Type of TPTeam entity: test folder, JUnit, test execution */
	private String mType;

	/**
	 * Default constructor
	 */
	public TPEntity() {

	}

	/**
	 * Constructor
	 * 
	 * @param id The TPTeam ID
	 * @param name The TPTeam name
	 * @param description The TPTeam description
	 * @param type The TPTeam test type
	 */
	public TPEntity(String id, String name, String description, String type) {
		setID(id);
		setName(name);
		mDescription = description;
		mType = type;
	}

	// Property accessors
	
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
