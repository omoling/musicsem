<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<link rel="stylesheet" href="style.css" type="text/css" /> 
<script type="text/javascript" src="script.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>MusicSem</title>
</head>
<body>
<form action="MainServlet" method="post" >
		Artist:
		<input type="text" id="search" name="search" /><br />
		<!-- <input type="submit" value="Search" /><br /> -->
</form>		
<br /><br />
<input type="button" onclick="javascript:ajaxFunction();" value="Search" />
<br /><br />
<input type="button" onclick="javascript:isloading('event');" value="TestJS" />
<input type="button" onclick="javascript:loaded('event');" value="TestJS2" />
<br /><br />

<div id="artist-container" class="content-container">
<div id="artist" class="content-box">artist</div>
<div id="artist-hover" class="content-hover"><img src="images/loading.gif" alt="loading..." /></div>
</div>

<div id="event-container" class="content-container">
<div id="event" class="content-box">event</div>
<div id="event-hover" class="content-hover"><img src="images/loading.gif" alt="loading..." /></div>
</div>


<f:view></f:view>
</body>
</html>