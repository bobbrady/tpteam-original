/********************************************************************
 * 
 * File		:	update_user.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used before updating
 *				a TPTeam user
 *  
 ********************************************************************/
function validateForm (form)
{
	var firstName = form.firstName.value;
	var lastName = form.lastName.value;
	var userName = form.userName.value;
	var password = form.password.value;
	var passwordConfirm = form.passwordConfirm.value;
	var email = form.email.value;
	var phone = form.phone.value;
	var ecfID = form.ecfID.value;
				
	if( firstName == "")
	{ 
		alert("Please enter a first name.");
		return false;
	}
	else if( lastName == "")
	{ 
		alert("Please enter a last name.");
		return false;
	}
	else if(userName == "")
	{
		alert("Please enter a userName.");
		return false;
	}
	else if(password == "" || passwordConfirm == "")
	{ 
		alert("Please enter a password.");
		return false;
	} 
	else if(password != passwordConfirm)
	{ 
		alert("Password not confirmed.  Please re-enter passwords.");
		return false;
	} 
	else if(phone == "" || !isPhoneValid(phone))
	{
		alert("Please enter a phone number in ddd-ddd-dddd format.");
		return false;
	} 
	else if(email == "" || !isEmailValid(email))
	{
		alert("Please enter an email address.");
		return false;
	} 
	else if(ecfID == "" || !isEmailValid(ecfID))
	{
		alert("Please enter a valid ECF ID.");
		return false;
	}
		
	form.action = form.formAction.value;
	form.submit();
	return true;
}

function isEmailValid(email) 
{
     var emailRegEx = "^[\\w-_\.]*[\\w-_\.]\@[\\w]\.+[\\w]+[\\w]$";
     var regex = new RegExp(emailRegEx);
     return regex.test(email);
  }
  
  function isPhoneValid(phone) 
  {
     var phoneRegEx = "^\\d{3}-\\d{3}-\\d{4}$";
     var regex = new RegExp(phoneRegEx);
     return regex.test(phone);
  }
