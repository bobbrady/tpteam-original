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