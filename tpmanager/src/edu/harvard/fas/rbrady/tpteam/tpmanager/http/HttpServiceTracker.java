/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddProductEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddProject;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddProjectEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddTestEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddUserEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteProduct;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteProductEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteProject;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteProjectEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteTest2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteTestEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteUserEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec.AdminProcessTestExec;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec.ExecTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec.ExecTest2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateProduct;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateProductEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateProject;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateProject2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateProjectEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest3;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTestEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateUser2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateUserEntity;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProduct;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProject;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProject2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewTest2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewTest3;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewUser2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.add.AddTestByUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.add.AddTestEntityByUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update.UpdateTestByUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update.UpdateTestByUser2;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update.UpdateTestByUser3;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update.UpdateTestEntityByUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update.UpdateUserProfile;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update.UpdateUserProfileEntity;


	public class HttpServiceTracker extends ServiceTracker {

		public HttpServiceTracker(BundleContext context) {
			super(context, HttpService.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			HttpService httpService = (HttpService) context.getService(reference);
			try {			
				// Security HTML Pages
				httpService.registerResources("/tpteam/login.html", "/html/security/login.html", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/error.html", "/html/security/error.html", null); //$NON-NLS-1$ //$NON-NLS-2$
				// Admin HTML Pages
				httpService.registerResources("/tpteam/admin/index.html", "/html/admin/index.html", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/admin/add/addProduct.html", "/html/admin/add/addProduct.html", null); //$NON-NLS-1$ //$NON-NLS-2$
				/**********************************************************************
				 * Admin Servlets
				 **********************************************************************/
				httpService.registerServlet("/tpteam/admin/add/addProductEntity", new AddProductEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/add/addProject", new AddProject(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/add/addProjectEntity", new AddProjectEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/add/addTest", new AddTest(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/add/addTestEntity", new AddTestEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/add/addUser", new AddUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/add/addUserEntity", new AddUserEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateProduct", new UpdateProduct(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateProductEntity", new UpdateProductEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateProject", new UpdateProject(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateProject2", new UpdateProject2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateProjectEntity", new UpdateProjectEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateTest", new UpdateTest(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateTest2", new UpdateTest2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateTest3", new UpdateTest3(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateTestEntity", new UpdateTestEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateUser", new UpdateUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateUser2", new UpdateUser2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/update/updateUserEntity", new UpdateUserEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteProduct", new DeleteProduct(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteProductEntity", new DeleteProductEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteUser", new DeleteUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteUserEntity", new DeleteUserEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteProject", new DeleteProject(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteProjectEntity", new DeleteProjectEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteTest", new DeleteTest(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteTest2", new DeleteTest2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/delete/deleteTestEntity", new DeleteTestEntity(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewProduct", new ViewProduct(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewUser", new ViewUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewUser2", new ViewUser2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewProject", new ViewProject(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewProject2", new ViewProject2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewTest", new ViewTest(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewTest2", new ViewTest2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/view/viewTest3", new ViewTest3(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/exec/execTest", new ExecTest(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/exec/execTest2", new ExecTest2(), null, null); //$NON-NLS-1$
				/**********************************************************************
				 * User Servlets
				 **********************************************************************/
				httpService.registerServlet("/tpteam/user/add/addTest", new AddTestByUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/add/addTestEntity", new AddTestEntityByUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/update/updateTest", new UpdateTestByUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/update/updateTest2", new UpdateTestByUser2(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/update/updateTest3", new UpdateTestByUser3(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/update/updateTestEntity", new UpdateTestEntityByUser(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/update/updateUser", new UpdateUserProfile(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/user/update/updateUserEntity", new UpdateUserProfileEntity(), null, null); //$NON-NLS-1$

				// User HTML Pages
				httpService.registerResources("/tpteam/user/index.html", "/html/user/index.html", null); //$NON-NLS-1$ //$NON-NLS-2$
				// Images
				httpService.registerResources("/tpteam/images/open.gif", "/html/images/open.gif", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/images/closed.gif", "/html/images/closed.gif", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/images/doc.gif", "/html/images/doc.gif", null); //$NON-NLS-1$ //$NON-NLS-2$
				/***********************************************************************************
				 * Scripts
				 ***********************************************************************************/
				httpService.registerResources("/tpteam/scripts/add_test.js", "/html/scripts/add_test.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/add_test_tree.css", "/html/scripts/add_test_tree.css", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/add_test_tree.js", "/html/scripts/add_test_tree.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/add_test_type.js", "/html/scripts/add_test_type.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/add_test_folder.js", "/html/scripts/add_test_folder.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/add_user.js", "/html/scripts/add_user.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/update_prod.js", "/html/scripts/update_prod.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/update_proj.js", "/html/scripts/update_proj.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/update_test_tree.js", "/html/scripts/update_test_tree.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/update_test_folder.js", "/html/scripts/update_test_folder.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/update_test_def.js", "/html/scripts/update_test_def.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/update_user.js", "/html/scripts/update_user.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/delete_prod.js", "/html/scripts/delete_prod.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/delete_user.js", "/html/scripts/delete_user.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/delete_proj.js", "/html/scripts/delete_proj.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/delete_test_tree.js", "/html/scripts/delete_test_tree.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/view_test_tree.js", "/html/scripts/view_test_tree.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				httpService.registerResources("/tpteam/scripts/exec_test_tree.js", "/html/scripts/exec_test_tree.js", null); //$NON-NLS-1$ //$NON-NLS-2$
				// TPTP Resources
				//httpService.registerServlet("/tpteam/processTestExec", new ProcessTestExecServlet(), null, null); //$NON-NLS-1$
				httpService.registerServlet("/tpteam/admin/exec/adminProcessTestExec", new AdminProcessTestExec(), null, null); //$NON-NLS-1$
			} catch (Exception e) {
				e.printStackTrace();
			}
			return httpService;
		}		
		
		public void removedService(ServiceReference reference, Object service) {
			HttpService httpService = (HttpService) service;
			httpService.unregister("/index.html"); //$NON-NLS-1$
			httpService.unregister("/processTestExec"); //$NON-NLS-1$
			super.removedService(reference, service);
		}
	}


