function getXMLObject()  //XML OBJECT
{
   var xmlHttp = false;
   try {
     xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");  // For Old Microsoft Browsers
   }
   catch (e) {
     try {
       xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");  // For Microsoft IE 6.0+
     }
     catch (e2) {
       xmlHttp = false;   // No Browser accepts the XMLHTTP Object then false
     }
   }
   if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
     xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
   }
   return xmlHttp;  // Mandatory Statement returning the ajax object created
}
 
var xmlhttp = new getXMLObject();	//xmlhttp holds the ajax object
 
function ajaxFunction() {
  var getdate = new Date();  //Used to prevent caching during ajax call
  if(xmlhttp) { 
	var searchString = document.getElementById("search").value;
	alert(searchString);
    xmlhttp.open("GET","MainServlet?search=" + searchString, true);
    xmlhttp.onreadystatechange  = handleServerResponse;
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xmlhttp.send(null);
  }
}
 
function handleServerResponse() {
   if (xmlhttp.readyState == 4) {
     if(xmlhttp.status == 200) {
       //update content
       document.getElementById("artist").innerHtml = xmlhttp.responseXml;
     }
     else {
        //alert("Error during AJAX call. Please try again");
     }
   }
}

function isloading(id){
	document.getElementById(id+'-hover').style.display = 'block';
}

function loaded(id) {
	document.getElementById(id+'-hover').style.display = 'none';
}
