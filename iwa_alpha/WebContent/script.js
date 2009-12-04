//global vars
var _zIndex = 0;
var map;

function initializePage(){
	
	//Youtube
	initializeYoutube();
	
	//Google Maps
	initializeMap();
}

function keyNotEmpty()
{
	if ( document.getElementById("search").value.length==0  ){
		alert("No keyword was entered!")
		return false;
	} else {
		return true;
	}
}

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
 
function ajaxSearch() {
	//put containers in loading state
	containersLoading();
	
  if(xmlhttp) { 
	var searchString = document.getElementById("search").value;
    xmlhttp.open("GET","MainServlet?search=" + searchString, true);
    xmlhttp.onreadystatechange  = handleServerResponse;
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xmlhttp.send(null);
  } else {
	  alert("AJAX error!");
	  containersLoaded();  
  }
}
 
function handleServerResponse() {
   if (xmlhttp.readyState == 4) {
     if(xmlhttp.status == 200) {
    	 //call content dispatcher
    	 contentHandler(xmlHttp.responseXml);
     }
     else {
    	 //alert("Error during AJAX call. Please try again");
     }
   }
}

function contentHandler(xml){
	
	//load XSL
	//TODO: extend for all XSL
	var xsl=loadXMLFile("test.xsl");
	
	//html content var
	//TODO: extend for all containers
	var htmlContent = "";
	
	//TODO: load all XSL needed
	var xsl = loadXsl('test.xsl');
	
	//TODO: run for all containers-xsl
	
	if (window.ActiveXObject)
	   {
	     htmlContent = xml.transformNode(xsl);
	   }
	   // code for Mozilla, Firefox, Opera, etc.
	   else if (document.implementation && document.implementation.createDocument)
	   {
	     xsltProcessor = new XSLTProcessor();
	     xsltProcessor.importStylesheet(xsl);
	     
	     var xmlRef = document.implementation.createDocument("", "", null);
         var myNode = document.getElementById(xmlIsland);
         var clonedNode = xmlRef.importNode(myNode.childNodes.item(1), true);
         xmlRef.appendChild(clonedNode);

         // do the transform
         var fragment = xsltProcessor.transformToFragment(xmlRef, document);
	   }	
	
	//update containers
	//TODO: update all containers
	updateContainer('artist', fragment);
	
}

function loadXSL(filename)
{
	// Load the xsl file using synchronous XMLHttpRequest
    var myXMLHTTPRequest = getXMLObject();
    myXMLHTTPRequest.open("GET", "http://localhost:8080/iwa_alpha/XSL/"+filename, false);
    myXMLHTTPRequest.send(null);
    var xsl = myXMLHTTPRequest.responseXML;
	return xsl;
	/*
	var xmlDoc;
	// code for IE
	if (window.ActiveXObject)
	{
		xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	}
	// code for Mozilla, Firefox, Opera, etc.
	else if (document.implementation
			&& document.implementation.createDocument)
	{
		xmlDoc=document.implementation.createDocument("","",null);
	}
	else
	{
		alert('Your browser cannot handle this script');
	}
	xmlDoc.async=false;
	xmlDoc.load("http://localhost:8080/iwa_alpha/XSL/" + filename);
	//xmlDoc.load("/iwa_alpha/XSL/" + filename);
	return(xmlDoc);
	*/
}

function updateContainer(id, newContent){
	document.getElementById(id).innerHTML = "";
	document.getElementById(id).appendChild(newContent);
	loaded(id);
}

function containersLoading(){
	//for now: all
	isLoading('artist');
	isLoading('event');
	isLoading('map');
	isLoading('video');
	isLoading('image');
}

function containersLoaded(){
	//for now: all
	loaded('artist');
	loaded('event');
	loaded('map');
	loaded('video');
	loaded('image');
}

function isloading(id){
	document.getElementById(id+'-hover').style.display = 'block';
}

function loaded(id) {
	document.getElementById(id+'-hover').style.display = 'none';
}

//Google Maps
function initializeMap() {
    var latlng = new google.maps.LatLng(52.3833, 4.9);
    var myOptions = {
      zoom: 10,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);    
}

//Youtube
var params;
var atts;
function initializeYoutube(){
	
	// allowScriptAccess must be set to allow the Javascript from one 
	// domain to access the swf on the youtube domain
	params = { allowScriptAccess: "always", bgcolor: "#cccccc" };
	// this sets the id of the object or embed tag to 'myytplayer'.
	// You then use this id to access the swf and make calls to the player's API
	atts = { id: "myytplayer" };
	swfobject.embedSWF("http://www.youtube.com/apiplayer?enablejsapi=1&playerapiid=ytplayer", 
	                   "ytapiplayer", "327", "283", "8", null, null, params, atts);
	
	/*
	var params = { allowScriptAccess: "always" };
	swfobject.embedSWF(
		"http://www.youtube.com/v/OGg8A2zfWKg&enablejsapi=1&playerapiid=ytplayer", 
		"ytplayer", "327", "283", "8", null, null, params);
		//"ytplayer", "307", "270", "8", null, null, params);
	*/
}

function resizeSWF(width, height){
	var videoid = document.getElementById("videoid").value;
	var state = getPlayerState();
	clearVideo();
	swfobject.embedSWF("http://www.youtube.com/apiplayer?enablejsapi=1&playerapiid=ytplayer", 
            "ytapiplayer", ""+width, ""+height, "8", null, null, params, atts);
	loadNewVideo(videoid, 0);
	setytplayerstate(state);
}

function play() {
	  if (ytplayer) {
		ytplayer.playVideo();
	  }
	}

	function pause() {
	  if (ytplayer) {
		ytplayer.pauseVideo();
	  }
	}

	function stop() {
	  if (ytplayer) {
		ytplayer.stopVideo();
	  }
	}

// increase and assign global z-index
function increaseZ(id){
	_zIndex += 1;
	document.getElementById(id).style.zIndex = _zIndex;
}


function eventsInArea(){
	var bounds = map.getBounds();
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	//alert(sw.lat() + " " + sw.lng() + " " + ne.lat() + " " + ne.lng());
	
	//lat SW, lng SW, lat NE, lng NE
	alert(bounds.toUrlValue());
}

//***************************************************************
//Testing functions

function testXslLoading(){
	alert("going to try");
	var xsl = loadXSL("test.xsl");
	updateContainer("artist", xsl);
	alert("done?");
	alert(xsl);
}

