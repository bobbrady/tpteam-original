	function validateForm (form)
	{
		form.action = "deleteProjectEntity";
		if(confirm("Do you really want to delete the project and its entire test tree?"))
		{
			form.submit();
        	return true;
       	}
       	else
       	{
       		return false;
       	}
	}