/********************************************************************
 * 
 * File		:	delete_proj.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form confirmation script used before the deleting
 *				a Project
 *  
 ********************************************************************/
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