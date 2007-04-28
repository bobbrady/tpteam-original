package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestExecution;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

public class TestExecutionUtil {
	
	public static void insertTestExec(String testID, TPEvent tpEvent) throws Exception {
		// For standalone
		// Session s =
		// HibernateUtil.getSessionFactory().getCurrentSession();

		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
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