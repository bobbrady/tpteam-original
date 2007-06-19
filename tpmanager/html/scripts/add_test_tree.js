/********************************************************************
 * 
 * File		:	add_test_tree.js
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A script used to render a test tree when adding tests
 *  
 ********************************************************************/
var openImg = new Image();
openImg.src = "/bridge/tpteam/images/open.gif";
var closedImg = new Image();
closedImg.src = "/bridge/tpteam/images/closed.gif";
var selectedID = "";

function showBranch(branch){
	var objBranch = document.getElementById(branch).style;
	if(objBranch.display=="block")
		objBranch.display="none";
	else
      {
		objBranch.display="block";
      }
}

function swapFolder(img){
	objImg = document.getElementById(img);
	if(objImg.src.indexOf('closed.gif')>-1)
		objImg.src = openImg.src;
	else
		objImg.src = closedImg.src;
}

function makeBold(id){
	if(selectedID != "")
      {
 		document.getElementById(selectedID).style.fontWeight = 'normal';
	}
 	document.getElementById(id).style.fontWeight = 'bold';
 	selectedID = id;
}

