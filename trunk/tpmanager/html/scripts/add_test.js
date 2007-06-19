/********************************************************************
 * 
 * File		:	add_test.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used when adding tests,
 *              to verify core test data
 *  
 ********************************************************************/
function validatePage1Form ()
{ 
	var name = document.addTestPage1.testName.value;
	var desc = document.addTestPage1.testDesc.value;
	var proj = document.addTestPage1.proj;
	var testType = document.addTestPage1.testType;
				
	if( name == "")
	{ 
		alert("Please enter a test name.");
		return false;
	} 
	else if(proj.selectedIndex == 0)
	{ 
		alert("Please select a project.");
		return false;
	}
	else if(testType.selectedIndex == 0)
	{ 
		alert("Please select a testType.");
		return false;
	}
	document.addTestPage1.action = "addTest";
	document.addTestPage1.submit();
	return true;
}
