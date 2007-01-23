	function validateForm (form)
	{
		var name = form.prodName.value;
		var desc = form.prodDesc.value;
		if( name == "")
		{
			alert("Please enter at least a product name.");
			return false;
		}
		form.action = "updateProductEntity";
		form.submit();
        return true;
	}