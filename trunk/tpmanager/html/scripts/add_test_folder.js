function validatePage2Form ()
{
	if(selectedID == "")
	{ 
		alert("Please select a parent folder.");
		return false;
	}
	document.addTestPage2.parentID.value = selectedID;
	document.addTestPage2.action = "addTestEntity";
	document.addTestPage2.submit();
	return true;
}
