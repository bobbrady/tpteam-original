/********************************************************************
 * 
 * File		:	delete_user.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form confirmation script used before the deleting
 *				a TPTeam user
 *  
 ********************************************************************/
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