/********************************************************************
 * 
 * File		:	delete_prod.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form confirmation script used before the deleting
 *				a Product
 *  
 ********************************************************************/
	function validateForm (form)
	{
		var name = form.prodName.value;
		form.action = "deleteProductEntity";
		if(confirm("Do you really want to delete product \"" + name + "\"?"))
		{
			form.submit();
        	return true;
       	}
       	else
       	{
       		return false;
       	}
	}