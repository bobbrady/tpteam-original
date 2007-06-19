/********************************************************************
 * 
 * File		:	delete_test_tree.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script to ensure a Test Tree 
 *				element was selected before a deletion request
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
	form.action = "deleteTestEntity";
	form.submit();
	return true;
}
