//GLOBAL VARS
//max z-index
var _zIndex = 0;
//google map
var map;
//google map central marker
var centerMarker;
//mode 1:artist 2:events 3:otherthanevent
var mode = 1;

//current content in containers
var current_artist;
var current_event;
var current_map_center;

//XSL FILENAMES
var xsl_artist = "artist.xsl";
var xsl_areaevent = "areaevent.xsl";
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
}

function positionContainers(){
	//get the float position and reset is as abolute positioned
	//oder must be reversed as displayed
	//positionContainer("test-container");
	positionContainer("image-container");
	positionContainer("video-container");
	positionContainer("map-container");
	positionContainer("event-container");
	positionContainer("artist-container");
}

function positionContainer(id){
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

function ajaxSearch(m) {
	mode = m;
	var searchString = document.getElementById("search").value;
	current_artist = searchString;
	//containersLoading();
	//isloading('artist');
	divloading();
	if(xmlhttp){
		xmlhttp.open("GET","MainServlet?type=artist&search="+searchString, true);
		xmlhttp.onreadystatechange = handleSearchResponse;
		xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xmlhttp.send(null);
	} else {
		alert("AJAX error!");
	}
}

function ajaxEvents(location) {
	mode = 2;
	if(xmlhttp){
		xmlhttp.open("GET","MainServlet?type=eventsnearby&search="+location, true);
		xmlhttp.onreadystatechange = handleSearchResponse;
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

function divloading(){
	//document.getElementById('loadingdiv').style.display = "block";
	document.getElementById('loadingdiv').className = "loading";
}

function divloaded(){
	//document.getElementById('loadingdiv').style.display = "none";
	document.getElementById('loadingdiv').className = "not-loading";
}

/*
function handleEventsResponse(){
	if (xmlhttp.readyState == 4) {
		if(xmlhttp.status == 200) {
			//alert(xmlhttp.responseText);
			//updateContent('page', xmlhttp.responseText);
			alert("got response, not implemented yet");
		}
	}
}
*/

function handleSearchResponse(){
	if (xmlhttp.readyState == 4) {
		if(xmlhttp.status == 200) {
			
			//prepare variables
			var xsl;
			var parsedContent = "";
			var xsltProcessor;
			
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
			
			if(mode == 1 || mode == 3){
				//***************************************** ARTIST
				xsl = loadXSL(xsl_artist);
				xsltProcessor = new XSLTProcessor();
				xsltProcessor.importStylesheet(xsl);
				var xmlRef = document.implementation.createDocument("", "", null);
				result = xsltProcessor.transformToFragment(xmlhttp.responseXML, document);
				parsedContent = result;
				updateContent('artist-content', parsedContent);
				
				//***************************************** VIDEOS
				xsl = loadXSL(xsl_video);
				xsltProcessor = new XSLTProcessor();
				xsltProcessor.importStylesheet(xsl);
				var xmlRef = document.implementation.createDocument("", "", null);
				result = xsltProcessor.transformToFragment(xmlhttp.responseXML, document);
				parsedContent = result;
				var contentasstring = parsedContent.toString();
				try {
					updateContent("videoholder", parsedContent);
					setNewVideos(document.getElementById("videoholder").innerHTML);
				} catch (e) {
					alert(e);
				}
				
				//***************************************** IMAGE
				xsl = loadXSL(xsl_image);
				xsltProcessor = new XSLTProcessor();
				xsltProcessor.importStylesheet(xsl);
				var xmlRef = document.implementation.createDocument("", "", null);
				result = xsltProcessor.transformToFragment(xmlhttp.responseXML, document);
				parsedContent = result;
				var contentasstring = parsedContent.toString();
				try {
					updateContent("imageholder", parsedContent);
					setNewImages(document.getElementById("imageholder").innerHTML);
				} catch (e) {
					alert(e);
				}
			
				if(mode == 1){
					//***************************************** EVENTS mode 1
					xsl = loadXSL(xsl_event);
					xsltProcessor = new XSLTProcessor();
					xsltProcessor.importStylesheet(xsl);
					var xmlRef = document.implementation.createDocument("", "", null);
					result = xsltProcessor.transformToFragment(xmlhttp.responseXML, document);
					parsedContent = result;
					updateContent('event-content', parsedContent);
				}
			}
			if(mode == 2) {
				//***************************************** EVENTS mode 2
				xsl = loadXSL(xsl_areaevent);
				xsltProcessor = new XSLTProcessor();
				xsltProcessor.importStylesheet(xsl);
				var xmlRef = document.implementation.createDocument("", "", null);
				result = xsltProcessor.transformToFragment(xmlhttp.responseXML, document);
				parsedContent = result;
				updateContent('event-content', parsedContent);
			}
				
			} else {
				alert("Error during XSL transformation (are you using Firefox? You should ;)");
			}
		}
		else {
			alert("Error during AJAX call. Please try again. (" + xmlhttp.status + ")");
		}
	}
	//containersLoaded();
	//loaded('artist');
	divloaded();
}

function updateContent(id, newContent){
	document.getElementById(id).innerHTML = "";
	document.getElementById(id).appendChild(newContent);
	//document.getElementById(id).innerHTML = newContent;
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

function newArtist(name){
	document.getElementById("search").value = name;		
	ajaxSearch(3);
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

/*
function resizeSWF(width, height){
	var videoid = document.getElementById("videoid").value;
	var state = getPlayerState();
	clearVideo();
	swfobject.embedSWF("http://www.youtube.com/apiplayer?enablejsapi=1&playerapiid=ytplayer", 
			"ytapiplayer", ""+width, ""+height, "8", null, null, params, atts);
	loadNewVideo(videoid, 0);
	setytplayerstate(state);
}
*/

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

function updateMap(address){
	
	/*
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
	*/
	
	var geocoder = new google.maps.Geocoder();
	var center = map.getCenter();
	
	if (geocoder) {
	      geocoder.geocode( { 'address': address}, function(results, status) {
	        if (status == google.maps.GeocoderStatus.OK) {
	          map.setCenter(results[0].geometry.location);
	          	  centerMarker = new google.maps.Marker({
	              map: map, 
	              position: results[0].geometry.location
	          });
	        } else {
	          alert("Geocode was not successful for the following reason: " + status);
	        }
	      });
	    }

	
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
	        	  //document.getElementById("search").value = location;
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
	
	var position_element = document.getElementById("videoposition");
	var total_element = document.getElementById("videototal");
	var duration_element = document.getElementById("videoduration");
	
	if (pos >= 0){
		var videotoload = video_array[pos];
		loadNewVideo(videotoload.id, 0);	
		position_element.innerHTML = pos+1 + " / ";
		total_element.innerHTML = video_array.length;
		duration_element.innerHTML = "Duration: " + videotoload.duration + " ";
	} else {
		position_element.innerHTML = "0 / ";
		total_element.innerHTML = "0 [no video]";
		duration_element.innerHTML = "Duration: / ";
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

function setNewVideos(raw){
	var newvideoarray = raw.split("||");
	//alert("videos #: "+ newvideoarray.length);
	video_array = null;
	video_array = new Array();
	if(newvideoarray.length > 0){
		var videoinfo;
		for(i = 0; i < newvideoarray.length; i++){
			if("" != newvideoarray[i]){
				videoinfo = newvideoarray[i].split("|");
				if("" != videoinfo[0]){
					video_array[i] = new video(videoinfo[0],videoinfo[1]);
				}
			}
		}
		video_array_pos = 0;
		loadVideo(video_array_pos);
	} else {
		alert("no image found");
	}
}

function setNewImages(raw){
	var newimagearray = raw.split("||");
	//alert("images #: "+ newimagearray.length);
	image_array = null;
	image_array = new Array();
	if(newimagearray.length > 0){
		for(i = 0; i < newimagearray.length; i++){
			if("" != newimagearray[i]){
				//alert(newimagearray[i]);
				image_array[i] = new image(newimagearray[i],"",100,100,"");
			}
		}
		image_array_pos = 0;
		loadImage(image_array_pos);
	} else {
		alert("no image found");
	}
}

//

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

function video(id, duration){
	this.id = id;
	this.duration = duration;
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

