function validateForm(form)
{
	var name = form.testName.value;
	if( name == "")
	{ 
		alert("Please enter the test element name.");
		return false;
	}
	form.action = "updateTestEntity";
	form.submit();
	return true;
}
