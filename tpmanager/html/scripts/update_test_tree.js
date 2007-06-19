/********************************************************************
 * 
 * File		:	update_test_tree.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script to ensure a Test Tree node
 *				was selected before a Test update is done
 *  
 ********************************************************************/
function validateForm(form)
{
	if(selectedID == "")
	{ 
		alert("Please select a folder or test.");
		return false;
	}
	form.testID.value = selectedID;
	form.action = form.formAction.value;
	form.submit();
	return true;
}
