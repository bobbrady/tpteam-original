/********************************************************************
 * 
 * File		:	update_proj.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used before the updating
 *				a Project
 *  
 ********************************************************************/
function validateForm (form)
{
	var name = form.projName.value;
	var desc = form.projDesc.value;
	var prod = form.prod;
	if( name == "")
	{
		alert("Please enter at least a project name.");
		return false;
	}
	else if(prod.options[prod.selectedIndex].value == "")
	{
		alert("Please select a product.");
		return false;
	}
	form.action = "updateProjectEntity";
	form.submit();
	return true;
}