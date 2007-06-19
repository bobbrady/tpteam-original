/********************************************************************
 * 
 * File		:	update_test_def.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used before updating
 *				a Test definition
 *  
 ********************************************************************/
function validateForm(form)
{
	var name = form.testName.value;
	var workspace = form.eclipseWorkspace.value;
	var home = form.eclipseHome.value;
	var proj = form.eclipseProj.value;
	var dir = form.reportDir.value;
	var conn = form.tptpConn.value;
	var suite = form.testSuite.value;
				
	if( name == "")
	{ 
		alert("Please enter a name for the test element.");
		return false;
	}			
	else if( workspace == "")
	{ 
		alert("Please enter the eclipse workspace.");
		return false;
	}
	else if( home == "")
	{ 
		alert("Please enter the home directory of the eclipse installation.");
		return false;
	}
	else if(proj == "")
	{
		alert("Please enter the name of the eclipse project containing the test.");
		return false;
	}
	else if(dir == "")
	{ 
		alert("Please enter the directory where tptp results will be stored.");
		return false;
	} 
	else if(conn == "")
	{
		alert("Please enter the TPTP connection URL.");
		return false;
	} 
	else if(suite == "")
	{ 
		alert("Please enter the test suite name.");
		return false;
	}
	form.action = form.formAction.value;
	form.submit();
	return true;
}
