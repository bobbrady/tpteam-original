/********************************************************************
 * 
 * File		:	update_test_folder.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used before updating
 *				a Test folder
 *  
 ********************************************************************/
function validateForm(form)
{
	var name = form.testName.value;
	if( name == "")
	{ 
		alert("Please enter the test element name.");
		return false;
	}
	form.action = form.formAction.value;
	form.submit();
	return true;
}
