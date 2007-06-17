/********************************************************************
 * 
 * File		:	TestExecutionUtil.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A utility class for TPTeam TestExecution operations
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestExecution;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

/*******************************************************************************
 * File 		: 	TestExecutionUtil.java
 * 
 * Description 	: 	A utility class for TPTeam TestExecution operations
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TestExecutionUtil {
	
	/**
	 * Inserts a test execution into the TPTeam database
	 * 
	 * @param testID the ID of the test
	 * @param tpEvent the TPEvent containing the request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void insertTestExec(String testID, TPEvent tpEvent) throws Exception {
		// Use plugin activator if in OSGi runtime
		Session s = null;
		if(Activator.getDefault() != null)
		{
			s = Activator.getDefault().getHiberSessionFactory()
			.getCurrentSession();
		}
		else
		{
			s = HibernateUtil.getSessionFactory().getCurrentSession();
		}

		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", tpEvent.getDictionary().get(TPEvent.ECFID_KEY));
			List users = query.list();
			Test test = (Test) s.load(Test.class, Integer.parseInt(testID));
			TestExecution testExec = new TestExecution();
			testExec.setTest(test);
			testExec.setStatus(tpEvent.getDictionary().get(TPEvent.VERDICT_KEY).toUpperCase().charAt(0));
			testExec.setExecDate(new Date());
			testExec.setTpteamUser((TpteamUser) users.get(0));
			s.save(testExec);
			tpEvent.getDictionary().put(TPEvent.PARENT_ID_KEY, testID);
			tpEvent.getDictionary().put(TPEvent.ID_KEY,  TPEntity.EXEC + "_" + String.valueOf(testExec.getId()));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}


}
