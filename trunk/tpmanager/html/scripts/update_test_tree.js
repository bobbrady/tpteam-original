function validateForm(form)
{
	if(selectedID == "")
	{ 
		alert("Please select a folder or test.");
		return false;
	}
	form.testID.value = selectedID;
	form.action = "updateTest3";
	form.submit();
	return true;
}