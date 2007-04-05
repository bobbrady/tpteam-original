package edu.harvard.fas.rbrady.tpteam.tpmanager.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

public class ServletUtil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String ADD_TEST_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/add_test.js\">\n";

	public static final String ADD_TEST_TREE_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/add_test_tree.js\">\n";

	public static final String ADD_TEST_TYPE_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/add_test_type.js\">\n";

	public static final String ADD_TEST_FOLDER_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/add_test_folder.js\">\n";

	public static final String ADD_USER_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/add_user.js\">\n";

	public static final String UPDATE_PROD_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/update_prod.js\">\n";

	public static final String UPDATE_PROJ_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/update_proj.js\">\n";

	public static final String UPDATE_TEST_TREE_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/update_test_tree.js\">\n";

	public static final String UPDATE_TEST_FOLDER_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/update_test_folder.js\">\n";

	public static final String UPDATE_TEST_DEF_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/update_test_def.js\">\n";

	public static final String UPDATE_USER_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/update_user.js\">\n";

	public static final String ADD_TEST_TREE_CSS = "<link href=\"/bridge/tpteam/scripts/add_test_tree.css\" rel=\"stylesheet\" type=\"text/css\">\n";

	public static final String DELETE_PROD_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/delete_prod.js\">\n";

	public static final String DELETE_USER_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/delete_user.js\">\n";

	public static final String DELETE_PROJ_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/delete_proj.js\">\n";

	public static final String DELETE_TEST_TREE_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/delete_test_tree.js\">\n";

	public static final String VIEW_TEST_TREE_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/view_test_tree.js\">\n";
	
	public static final String EXEC_TEST_TREE_JS = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/bridge/tpteam/scripts/exec_test_tree.js\">\n";

	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
			"dd-MMM-yyyy HH:mm:ss.SSS zzz");

	public void adminHeader(HttpServletRequest request,
			HttpServletResponse response, String javaScript)
			throws ServletException, IOException {
		if (javaScript == null)
			javaScript = "";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String header = "<html><head><title>TPTeam Admin Pages</title>\n"
				+ javaScript
				+ "</head>\n"
				+ "<body><div align=\"center\"><h3>TPTeam Admin Pages</h3>\n"
				+ "<a href=\"/bridge/tpteam/admin/index.html\">Home</a><hr size=\"2\">\n";
		out.println(header);
	}
	
	public void userHeader(HttpServletRequest request,
			HttpServletResponse response, String javaScript)
			throws ServletException, IOException {
		if (javaScript == null)
			javaScript = "";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String header = "<html><head><title>TPTeam Pages</title>\n"
				+ javaScript
				+ "</head>\n"
				+ "<body><div align=\"center\"><h3>TPTeam</h3>\n"
				+ "<a href=\"/bridge/tpteam/user/index.html\">Home</a><hr size=\"2\">\n";
		out.println(header);
	}

	public void adminFooter(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String footer = "</div><hr size=\"2\"></body></html>\n";
		out.println(footer);
		out.close();
	}
	
	public void userFooter(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		adminFooter(request, response);
	}

	public void adminReply(HttpServletRequest request,
			HttpServletResponse response, String reply)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println(reply);
	}
	
	public void userReply(HttpServletRequest request,
			HttpServletResponse response, String reply)
			throws ServletException, IOException {
		adminReply(request, response, reply);
	}

	public void adminError(HttpServletRequest request,
			HttpServletResponse response, String error)
			throws ServletException, IOException {
		adminHeader(request, response, null);
		error += "<p/><a href=\"javascript:history.back(1)\">Try Again</a><p/>";
		adminReply(request, response, error);
		adminFooter(request, response);
	}
	
	public void userError(HttpServletRequest request,
			HttpServletResponse response, String error)
			throws ServletException, IOException {
		userHeader(request, response, null);
		error += "<p/><a href=\"javascript:history.back(1)\">Try Again</a><p/>";
		userReply(request, response, error);
		userFooter(request, response);
	}
	
	protected void throwError(HttpServletRequest req, HttpServletResponse resp,
			StringBuffer error, HttpServlet servlet) throws ServletException, IOException {
		if(servlet instanceof UserServlet)
			userError(req, resp, error.toString());
		else
			adminError(req, resp, error.toString());
	}

	protected void showPage(HttpServletRequest req, HttpServletResponse resp,
			StringBuffer reply, String javaScript, HttpServlet servlet) throws ServletException,
			IOException, Exception {
		if(servlet instanceof UserServlet)
		{
			userHeader(req, resp, javaScript);
			userReply(req, resp, reply.toString());
			userFooter(req, resp);
		}
		else
		{
			adminHeader(req, resp, javaScript);
			adminReply(req, resp, reply.toString());
			adminFooter(req, resp);

		}
	}
		public String getTreeJavaScript() {
		String javaScript = ""
				+ " var openImg = new Image();\n"
				+ " openImg.src = \"open.gif\";\n"
				+ " var closedImg = new Image();\n"
				+ " closedImg.src = \"closed.gif\";\n"
				+ " var selectedID = \"\";\n"
				+

				" function showBranch(branch){\n"
				+ "\t var objBranch = document.getElementById(branch).style;\n"
				+ "\t if(objBranch.display==\"block\")\n"
				+ "\t\t objBranch.display=\"none\";\n"
				+ "\t else\n"
				+ "\t {\n"
				+ "\t\t objBranch.display=\"block\";\n"
				+ "\t }\n"
				+ "}\n"
				+

				"function swapFolder(img){\n"
				+ "\t objImg = document.getElementById(img);\n"
				+ "\t if(objImg.src.indexOf('closed.gif')>-1)\n"
				+ "\t\t objImg.src = openImg.src;\n"
				+ "\t else\n"
				+ "\t\t objImg.src = closedImg.src;\n"
				+ "}\n"
				+

				"function makeBold(id){\n"
				+ "\t if(selectedID != \"\")\n"
				+ "\t {\n"
				+ "\t\t document.getElementById(selectedID).style.fontWeight = 'normal';\n"
				+ "\t}\n"
				+ "\t document.getElementById(id).style.fontWeight = 'bold';\n"
				+ "\tselectedID = id;\n" + "}\n";
		return javaScript;
	}

	public static String getTestTreeFolders(String projID) throws Exception {

		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();

		// For standalone debug
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		StringBuffer tree = new StringBuffer();

		try {
			tx = s.beginTransaction();

			// Order by desc since we're pushing on stack: alpha first on top
			List<Test> tests = s
					.createQuery(
							"from Test as test where test.project.id = "
									+ projID
									+ " and test.parent is null order by test.path desc")
					.list();

			tree.append(getRootHeader());
			tree.append(getTree(tests, true));
			tree.append("</SPAN>");

			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

		return tree.toString();
	}

	public static int getDepth(String path) {
		String pathNoDots = path.replaceAll("\\.", "");
		return path.length() - pathNoDots.length();
	}

	public static String getTree(List<Test> tests, boolean onlyFolders) {
		Stack<Test> nodes = new Stack<Test>();
		nodes.addAll(tests);
		// Initialize node level to top level
		int currLevel = 0;
		StringBuffer tree = new StringBuffer();

		/**
		 * While node stack is not empty, add header String, push child nodes
		 * repeat
		 */
		while (!nodes.empty()) {
			Test test = nodes.pop();

			/**
			 * If depth in tree decreased, print closing </SPAN> tag for
			 * previous parent
			 */
			int depth = getDepth(test.getPath());
			if (depth < currLevel)
				tree.append(getNodeFooter(depth, currLevel));
			currLevel = depth;

			tree.append(getNodeHeader(test, depth, onlyFolders));

			// push children onto stack
			if (test.getIsFolder() == 'Y' && test.getChildren().size() > 0)
				nodes.addAll(test.getChildren());
			else if (test.getIsFolder() == 'Y')
				tree.append(getNodeFooter(depth));
		}
		// Add final closing </SPAN> tag
		tree.append(getNodeFooter(0));
		return tree.toString();
	}

	private static String getNodeHeader(Test test, int depth,
			boolean onlyFolders) {
		StringBuffer header = new StringBuffer();
		String testId = String.valueOf(test.getId());
		String testName = test.getName();
		if (test.getIsFolder() == 'Y') {
			header.append(getPad(depth));
			header
					.append("<div class=\"trigger\" onClick=\"showBranch('branch_"
							+ testId
							+ "');swapFolder('folder_"
							+ testId
							+ "');makeBold('"
							+ testId
							+ "');\" id=\""
							+ testId
							+ "\">\n");
			header
					.append("<img src=\"/bridge/tpteam/images/closed.gif\" border=\"0\" id=\"folder_"
							+ testId + "\">\n");
			header.append(test.getName() + "\n");
			header.append("</div>\n");
			header.append("<span class=\"branch\" id=\"branch_" + testId
					+ "\">\n");
		} else if (onlyFolders == false) {
			header.append(getPad(depth)
					+ "<img src=\"/bridge/tpteam/images/doc.gif\"> "
					+ "<a href=\"#\" id=\"" + testId
					+ "\" onClick=\"makeBold('" + testId
					+ "'); return false;\">" + testName + "</a><br>\n");
		}
		return header.toString();
	}

	private static String getRootHeader() {
		StringBuffer header = new StringBuffer();
		header
				.append("<div class=\"trigger\" onClick=\"showBranch('branch_0');");
		header.append("swapFolder('folder_0');makeBold('0');\" id=\"0\">\n");
		header
				.append("<img src=\"/bridge/tpteam/images/closed.gif\" border=\"0\" id=\"folder_0\">\n");
		header.append("Project Root\n");
		header.append("</div>\n");
		header.append("<span class=\"branch\" id=\"branch_0\">\n");
		return header.toString();
	}

	private static String getNodeFooter(int depth, int currLevel) {
		StringBuffer footer = new StringBuffer();
		while (currLevel > depth)
			footer.append(getPad(--currLevel) + "</SPAN>\n");
		return footer.toString();
	}

	private static String getNodeFooter(int depth) {
		return getPad(depth) + "</SPAN>\n";
	}

	private static String getPad(int depth) {
		StringBuffer pad = new StringBuffer();
		for (int idx = 0; idx < depth; idx++)
			pad.append("\t");
		return pad.toString();
	}

	public static String getSHA1Hash(String plainText)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(plainText.getBytes());
		byte[] mdbytes = md.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			String hex = Integer.toHexString(0xFF & mdbytes[i]);
			if (hex.length() == 1)
				hexString.append('0');

			hexString.append(hex);
		}
		return hexString.toString().toUpperCase();
	}

	public static void main(String[] args) {
		try {
			ServletUtil serv = new ServletUtil();
			// System.out.println(ServletUtil.getTestTreeFolders("1"));
			// System.out.println(ServletUtil.getSHA1Hash("tpteam"));
			System.out.println("userId: "
					+ ServletUtil.getRemoteUserID("tpmgr"));
			System.out.println(ServletUtil.getTestTree("1", false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTestTree(String projID, boolean onlyFolders)
			throws Exception {

		Session s = Activator.getDefault().getHiberSessionFactory()
			.getCurrentSession();

		// For standalone debug
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		StringBuffer tree = new StringBuffer();

		try {
			tx = s.beginTransaction();

			// Order by desc since we're pushing on stack: alpha first on top
			List<Test> tests = s
					.createQuery(
							"from Test as test where test.project.id = "
									+ projID
									+ " and test.parent is null order by test.path desc")
					.list();

			tree.append(getRootHeader());
			tree.append(getTree(tests, onlyFolders));
			tree.append("</SPAN>");

			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return tree.toString();
	}

	public static int getRemoteUserID(String userName) throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone debug
		 //Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		int userId;
		try {
			tx = s.beginTransaction();
			List<TpteamUser> users = s.createQuery(
					"from TpteamUser as user where user.userName = '"
							+ userName + "'").list();
			userId = users.get(0).getId();
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return userId;
	}
	
}