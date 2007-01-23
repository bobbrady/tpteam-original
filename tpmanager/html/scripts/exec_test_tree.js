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
	form.action = "adminProcessTestExec";
	form.submit();
	return true;
}
