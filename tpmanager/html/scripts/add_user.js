/********************************************************************
 * 
 * File		:	add_user.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A form validation script used when adding new TPTeam
 *				users
 *  
 ********************************************************************/
function validateForm ()
{
	var firstName = document.addUser.firstName.value;
	var lastName = document.addUser.lastName.value;
	var userName = document.addUser.userName.value;
	var password = document.addUser.password.value;
	var email = document.addUser.email.value;
	var phone = document.addUser.phone.value;
	var ecfID = document.addUser.ecfID.value;
	var roleList = document.addUser.role;
	var projList = document.addUser.projects;
				
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
	else if(password == "")
	{ 
		alert("Please enter a password.");
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
	else if(roleList.selectedIndex == 0)
	{ 
		alert("Please select a role.");
		return false;
	}
	else if(projList.selectedIndex == 0)
	{ 
		alert("Please select one or more projects.");
		return false;
	}
	
	document.addUser.action = "addUserEntity";
	document.addUser.submit();
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
