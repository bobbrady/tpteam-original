/********************************************************************
 * 
 * File		:	add_test_type.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used when adding test 
 *				types
 *  
 ********************************************************************/
function validatePage2Form ()
{
	var workspace = document.addTestPage2.eclipseWorkspace.value;
	var home = document.addTestPage2.eclipseHome.value;
	var proj = document.addTestPage2.eclipseProj.value;
	var dir = document.addTestPage2.reportDir.value;
	var conn = document.addTestPage2.tptpConn.value;
	var suite = document.addTestPage2.testSuite.value;
	document.addTestPage2.parentID.value = selectedID;
				
	if( workspace == "")
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
	else if(document.addTestPage2.parentID.value == "")
	{ 
		alert("Please select a parent folder.");
		return false;
	}
	document.addTestPage2.action = "addTestEntity";
	document.addTestPage2.submit();
	return true;
}
