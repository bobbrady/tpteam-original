/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateUser2;

public class UpdateUserProfile extends UpdateUser2 implements UserServlet{
	private static final long serialVersionUID = 1L;

	protected String mRemoteUser = null;
	
	protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateUserEntity\">\n";
}