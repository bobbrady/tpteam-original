/********************************************************************
 * 
 * File		:	exec_test_tree.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script to ensure a Test Tree 
 *				element was selected before an execution request
 *  
 ********************************************************************/
function validateForm(form)
{
	if(selectedID == "")
	{ 
		alert("Please select a folder or test.");
		return false;
	}
	if(selectedID == "0")
	{
		alert("Please select a folder or test under the root node.");
		return false;
	}
	
	form.testID.value = selectedID;
	form.action = "processTestExec";
	form.submit();
	return true;
}
