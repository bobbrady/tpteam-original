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
