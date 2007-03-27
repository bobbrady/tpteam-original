package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate;

// Generated Nov 10, 2006 5:22:58 PM by Hibernate Tools 3.2.0.beta8

import java.util.HashSet;
import java.util.Set;

/**
 * Project generated by hbm2java
 */
public class Project implements java.io.Serializable {

	// Fields    

	private static final long serialVersionUID = 1L;

	private int id;

	private Product product;

	private String name;

	private String description;

	private Set<TpteamUser> tpteamUsers = new HashSet<TpteamUser>(0);

	private Set<Test> tests = new HashSet<Test>(0);

	// Constructors

	/** default constructor */
	public Project() {
	}

	/** minimal constructor */
	public Project(int id) {
		this.id = id;
	}

	/** full constructor */
	public Project(int id, Product product, String name, String description,
			Set<TpteamUser> tpteamUsers, Set<Test> tests) {
		this.id = id;
		this.product = product;
		this.name = name;
		this.description = description;
		this.tpteamUsers = tpteamUsers;
		this.tests = tests;
	}

	// Property accessors
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<TpteamUser> getTpteamUsers() {
		return this.tpteamUsers;
	}

	public void setTpteamUsers(Set<TpteamUser> tpteamUsers) {
		this.tpteamUsers = tpteamUsers;
	}
	
	public void addToTpteamUsers(TpteamUser tpTeamUser) {
		this.getTpteamUsers().add(tpTeamUser);
		tpTeamUser.getProjects().add(this);
	}

	public void removeFromTpteamUsers(TpteamUser tpTeamUser) {
		this.getTpteamUsers().remove(tpTeamUser);
		tpTeamUser.getProjects().remove(this);
	}


	public Set<Test> getTests() {
		return this.tests;
	}

	public void setTests(Set<Test> tests) {
		this.tests = tests;
	}
	
	/**
	 * Initializes Project skeleton values
	 * once Project object is loaded by Hibernate.
	 * 
	 * For use outside of Hibernate session.
	 */
	public void initSkeleton()
	{
		getId();
		getName();
		getDescription();
		getProduct().getId();
		getProduct().getName();
	}

}