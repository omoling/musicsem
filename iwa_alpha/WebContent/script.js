//GLOBAL VARS
//max z-index
var _zIndex = 0;
//google map
var map;
//google map central marker
var centerMarker;

//current content in containers
var current_artist;
var current_event;
var current_map_center;

//XSL FILENAMES
var xsl_artist = "artist.xsl";
var xsl_event = "event.xsl";
var xsl_image = "image.xsl";
var xsl_video = "video.xsl";

//VIDEO
var current_video_id;
var video_array = new Array();
var video_array_pos = -1;

//IMAGES
var current_image;
var image_array = new Array();
var image_array_pos = -1;

function initializePage(){

	//position containers
	positionContainers();
	
	//Youtube
	initializeYoutube();

	//Google Maps
	initializeMap();
	
	//initialize 2 test images
	var testimg1 = new image("http://farm4.static.flickr.com/3630/3681432106_a0366e4674.jpg","test1",100,100,"1 title");
	var testimg2 = new image("http://farm3.static.flickr.com/2603/4048205561_d7d39c82fe.jpg","test2",100,100,"2 title");
	image_array[0] = testimg1;
	image_array[1] = testimg2;
	image_array_pos = 0;
	loadImage(image_array_pos);
	
	//initialize 2 test video
	var testvideo1 = new video("u1zgFlCw8Aw");
	var testvideo2 = new video("uDkBzkA9L4s");
	video_array[0] = testvideo1;
	video_array[1] = testvideo2;
	video_array_pos = 0;
	loadVideo(video_array_pos);
}

function positionContainers(){
	//get the float position and reset is as abolute positioned
	//oder must be reversed as displayed
	positionContainer("test-container");
	positionContainer("image-container");
	positionContainer("video-container");
	positionContainer("map-container");
	positionContainer("event-container");
	positionContainer("artist-container");
}

function positionContainer(id, top, left){
	var container = document.getElementById(id);
	var top = container.offsetTop;
	var left = container.offsetLeft;
	container.style.float = "none";
	container.style.position = "absolute";
	container.style.top = top+"px";
	container.style.left = left+"px";
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

var xmlhttp = new getXMLObject();

function ajaxSearch() {
	containersLoading();
	if(xmlhttp){
		var searchString = document.getElementById("search").value;
		xmlhttp.open("GET","MainServlet?type=artist&search="+searchString, true);
		xmlhttp.onreadystatechange = handleSearchResponse;
		xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xmlhttp.send(null);
	} else {
		alert("AJAX error!");
	}
}

function ajaxEvents(location) {
	if(xmlhttp){
		xmlhttp.open("GET","MainServlet?type=eventsnearby&search="+location, true);
		xmlhttp.onreadystatechange = handleEventsResponse;
		xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xmlhttp.send(null);
	} else {
		alert("AJAX error!");
	}
}

function loadXSL(filename){
	// Load the xsl file using synchronous XMLHttpRequest
	var xmlhttp = getXMLObject();
	xmlhttp.open("GET", "http://localhost:8080/iwa_alpha/XSL/client/"+filename, false);
	xmlhttp.send(null);
	//alert(xmlhttp.responseText);
	var xsl = xmlhttp.responseXML;
	return xsl;
}

function handleEventsResponse(){
	if (xmlhttp.readyState == 4) {
		if(xmlhttp.status == 200) {
			//alert(xmlhttp.responseText);
		}
	}
}

function handleSearchResponse(){
	if (xmlhttp.readyState == 4) {
		if(xmlhttp.status == 200) {
			
			//TODO: parse response for each container, check for artist-events status
			//get XSL
			var xsl = loadXSL(xsl_artist);
			var parsedContent = "";
			
			/*// code for IE
			if (window.ActiveXObject)
			{
				parsedContent = xml.transformNode(xsl);
				ex=xml.transformNode(xsl);
				document.getElementById("example").innerHTML=ex;
			}
			// code for Mozilla, Firefox, Opera, etc.
			else*/ if (document.implementation && document.implementation.createDocument)
			{
				var xsltProcessor = new XSLTProcessor();
				xsltProcessor.importStylesheet(xsl);
				var xmlRef = document.implementation.createDocument("", "", null);
				
				result = xsltProcessor.transformToFragment(xmlhttp.responseXML, document);
				parsedContent = result;
				updateContent('artist-content', parsedContent);
			} else {
				alert("Error during XSL transformation (are you using Firefox? You should..)");
			}
		}
		else {
			alert("Error during AJAX call. Please try again. (" + xmlhttp.status + ")");
		}
	}
	containersLoaded();
}

//TODO: check if needed!!
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
	updateContent('artist', fragment);

}

function updateContent(id, newContent){
	document.getElementById(id).innerHTML = "";
	document.getElementById(id).appendChild(newContent);
	//document.getElementById(id).innerHTML = newContent;
	loaded(id);
}

function containersLoading(){
	//for now: all
	isloading('artist');
	isloading('event');
	isloading('map');
	isloading('video');
	isloading('image');
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

function updateMap(lat, lng, title){
	isloading('map');
	//set new center
	var newCenter = new google.maps.LatLng(parseInt(lat),parseInt(lng));
	map.setCenter(newCenter);
	//set new center marker
	centerMarker = null;
	centerMarker = new google.maps.Marker({
     		position: newCenter, 
      		map: map,
      		title: title
		});
	loaded('map');
}

function newArtist(name){
	
	//update artist container
	
	//update event container for given new artist
	
	//update video container for given new artist
	
	alert("not impelemnted yet!");
	
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

/*function updateHTML(elmId, value) {
	document.getElementById(elmId).innerHTML = value;
}

function setytplayerState(newState) {
	updateHTML("playerstate", newState);
}*/

function onYouTubePlayerReady(playerId) {
	ytplayer = document.getElementById("myytplayer");
	setInterval(updateytplayerInfo, 250);
	updateytplayerInfo();
	ytplayer.addEventListener("onStateChange", "onytplayerStateChange");
	ytplayer.addEventListener("onError", "onPlayerError");
}

function onytplayerStateChange(newState) {
	setytplayerState(newState);
}

function onPlayerError(errorCode) {
	alert("An error occured: " + errorCode);
}

/*function updateytplayerInfo() {
	updateHTML("bytesloaded", getBytesLoaded());
	updateHTML("bytestotal", getBytesTotal());
	updateHTML("videoduration", getDuration());
	updateHTML("videotime", getCurrentTime());
	updateHTML("startbytes", getStartBytes());
	updateHTML("volume", getVolume());
}*/

// functions for the api calls
function loadNewVideo(id, startSeconds) {
	if (ytplayer) {
		ytplayer.loadVideoById(id, parseInt(startSeconds));
	}
	
	//document.getElementById("videoid").value = id;
}

function cueNewVideo(id, startSeconds) {
	if (ytplayer) {
		ytplayer.cueVideoById(id, startSeconds);
	}
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

function getPlayerState() {
	if (ytplayer) {
		return ytplayer.getPlayerState();
	}
}

function seekTo(seconds) {
	if (ytplayer) {
		ytplayer.seekTo(seconds, true);
	}
}

function getBytesLoaded() {
	if (ytplayer) {
		return ytplayer.getVideoBytesLoaded();
	}
}

function getBytesTotal() {
	if (ytplayer) {
		return ytplayer.getVideoBytesTotal();
	}
}

function getCurrentTime() {
	if (ytplayer) {
		return ytplayer.getCurrentTime();
	}
}

function getDuration() {
	if (ytplayer) {
		return ytplayer.getDuration();
	}
}

function getStartBytes() {
	if (ytplayer) {
		return ytplayer.getVideoStartBytes();
	}
}

function mute() {
	if (ytplayer) {
		ytplayer.mute();
	}
}

function unMute() {
	if (ytplayer) {
		ytplayer.unMute();
	}
}

function getEmbedCode() {
	alert(ytplayer.getVideoEmbedCode());
}

function getVideoUrl() {
	alert(ytplayer.getVideoUrl());
}

function setVolume(newVolume) {
	if (ytplayer) {
		ytplayer.setVolume(newVolume);
	}
}

function getVolume() {
	if (ytplayer) {
		return ytplayer.getVolume();
	}
}

function clearVideo() {
	if (ytplayer) {
		ytplayer.clearVideo();
	}
}

//increase and assign global z-index
function increaseZ(id){
	_zIndex += 1;
	document.getElementById(id).style.zIndex = _zIndex;
}


function eventsInArea(){
	
	//get name from reverse-geocoding
	var geocoder = new google.maps.Geocoder();
	var center = map.getCenter();
	var location = "";
	
	if (geocoder) {
	      geocoder.geocode({'latLng': center}, function(results, status) {
	        if (status == google.maps.GeocoderStatus.OK) {
	          if (results[1]) {
	        	  for(i=0; i < results[1].address_components.length; i++)
	        	  {
	        		  if(results[1].address_components[i].types.indexOf("locality") >= 0){
	        			  location = results[1].address_components[i].long_name;
	        			  break;
	        		  }
	        	  }
	        	  ajaxEvents(location);
	            } else {
	            	alert("Could not find the location!");
	            }
	          } else {
	            alert("Geocoder failed due to: " + status);
	          }
	      });
	}

}

//VIDEO FUNCTIONS
function loadVideo(pos){
	if(pos >= video_array.length){
		if(video_array.length > 0){
			pos = 0;
		} else {
			//no image
			pos = -1;
		}
	}
	if (pos >= 0){
		var videotoload = video_array[pos];
		loadNewVideo(videotoload.id, 0);
		var position_element = document.getElementById("videoposition");
		var total_element = document.getElementById("videototal");
		position_element.innerHTML = pos+1 + " / ";
		total_element.innerHTML = video_array.length;
	} else {
		position_element.innerHTML = "0 / ";
		total_element.innerHTML = "0 [no video]";
	}
}

function getNextVideoPos(){
	video_array_pos++;
	//check to loop forward
	if(video_array_pos >= video_array.length){
		video_array_pos = 0;
	}
	return video_array_pos;
}

function getPrevVideoPos(){
	video_array_pos--;
	//check to loop backwards
	if(video_array_pos < 0){
		video_array_pos = video_array.length - 1;
	}
	return video_array_pos;
}

function nextVideo(){
	isloading('video');
	loadVideo(getNextVideoPos());
	loaded('video');
}

function previousVideo(){
	isloading('image');
	loadVideo(getPrevVideoPos());
	loaded('image');
}

//IMAGES FUNCTIONS
function resizeImage(w, h){
	var holder = document.getElementById("image-holder");
	holder.style.width=w+"px";
	holder.style.height=h+"px";
}

function loadImage(pos) {
	if(pos >= image_array.length){
		if(image_array.length > 0){
			pos = 0;
		} else {
			//no image
			pos = -1;
		}
	}
	var image_element = document.getElementById("currentimage");
	var link_element = document.getElementById("imagelink");
	var title_element = document.getElementById("imagetitle");
	var position_element = document.getElementById("imageposition");
	var total_element = document.getElementById("imagetotal");
	if (pos >= 0){
		var imagetoload = image_array[pos];
		image_element.src = imagetoload.src;
		image_element.alt = imagetoload.alt;
		link_element.href = imagetoload.src;
		title_element.innerHTML = imagetoload.title;
		position_element.innerHTML = pos+1 + " / ";
		total_element.innerHTML = image_array.length;
	} else {
		image_element.src = "";
		image_element.alt = "no image";
		link_element.href = "javascript:void(0);";
		title_element.innerHTML = "no image";
		position_element.innerHTML = "0 / ";
		total_element.innerHTML = "0";
	}
}

function getNextImgPos(){
	image_array_pos++;
	//check to loop forward
	if(image_array_pos >= image_array.length){
		image_array_pos = 0;
	}
	return image_array_pos;
}

function getPrevImgPos(){
	image_array_pos--;
	//check to loop backwards
	if(image_array_pos < 0){
		image_array_pos = image_array.length - 1;
	}
	return image_array_pos;
}

function previousImage(){
	isloading('image');
	loadImage(getPrevImgPos());
	loaded('image');
}

function nextImage(){
	isloading('image');
	loadImage(getNextImgPos());
	loaded('image');
}

function preventReloading(){
	alert("You are going to reload every content!");
}

//***************************************************************
// OBJECTS

function testObjects(){
	var myobject = new image("src");
	alert(myobject.src);
}

function image(src,alt,width,height,title){
	this.src = src;
	this.alt = alt;
	this.width = width;
	this.height = height;
	this.title = title;
}

function video(id){
	this.id = id;
}

function marker(marker){
	this.marker = marker;
}


//***************************************************************
//Testing functions

function testXslLoading(){
	//alert("going to try");
	var xsl = loadXSL("test.xsl");
	updateContent("artist", xsl);
	alert("done?");
}

