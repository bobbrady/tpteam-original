	function validateForm (form)
	{
		form.action = "deleteUserEntity";
		if(confirm("Do you really want to delete this user?"))
		{
			form.submit();
        	return true;
       	}
       	else
       	{
       		return false;
       	}
	}