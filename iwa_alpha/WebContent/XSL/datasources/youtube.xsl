<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:vCard = "http://www.w3.org/2001/vcard-rdf/3.0#"
    xmlns:mindswap="http://www.mindswap.org/~glapizco/technical.owl#"
    xmlns:media="http://search.yahoo.com/mrss/" 
    xmlns:gd="http://schemas.google.com/g/2005" 
    xmlns:yt="http://gdata.youtube.com/schemas/2007" 

>

<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" /> 
<xsl:template match="/">
 <rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:vCard = "http://www.w3.org/2001/vcard-rdf/3.0#"
    xmlns:mindswap="http://www.mindswap.org/~glapizco/technical.owl#" 
    xmlns:media="http://search.yahoo.com/mrss/" 
    xmlns:gd="http://schemas.google.com/g/2005" 
    xmlns:yt="http://gdata.youtube.com/schemas/2007" 
 >	
 <xsl:apply-templates/>
 </rdf:RDF>
 </xsl:template>  

<xsl:template match="feed">
	<xsl:for-each select="entry">
     <mindswap:Video>
	   <dc:title><xsl:value-of select="title"/></dc:title>
	   <mindswap:hasDurationSeconds><xsl:value-of select="media:group/yt:duration"/></mindswap:hasDurationSeconds>
	   <vCard:URL rdf:resource="{media:group/media:player}"/>
      </mindswap:Video>
   </xsl:for-each>    
 </xsl:template>
</xsl:stylesheet>
		
	
