<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
 xmlns:evnt="http://www.psylus.info/IWA/assignment2/events#"
 xmlns:artist="http://www.psylus.info/IWA/assignment2/artist#"
 xmlns:venue="http://www.psylus.info/IWA/assignment2/venue#">
 
<xsl:output method="html" version='1.0' encoding='UTF-8' indent='yes'/>
<xsl:template match="/">
 <html>
  <body>
    <head>Event Search Results</head> 
    <xsl:apply-templates select="/rdf:RDF/rdf:Description"/>
  </body>
 </html>
</xsl:template>



<xsl:template match="/rdf:RDF/rdf:Description">
<h3><a name="{substring-after(@rdf:about,'#')}">Event Title: <xsl:value-of select="evnt:title"/></a></h3>
Event ID: <xsl:value-of select="substring-after(@rdf:about,'#')"/><br />
Event URL: <a href="{substring-after(evnt:URL/@rdf:resource,'')}"> <xsl:value-of select="substring-after(evnt:URL/@rdf:resource,'')"/> </a><br />
Start Time: <xsl:value-of select="evnt:start_time"/><br />
Description: <xsl:value-of select="evnt:Description"/><br />
<xsl:apply-templates select="evnt:takesPlaceAt/rdf:Description"/>
<xsl:if test="evnt:artist">
  <xsl:for-each select="evnt:artist">  
    <xsl:apply-templates select="rdf:Description"/>
  </xsl:for-each>
</xsl:if>    
</xsl:template>


<xsl:template match="evnt:takesPlaceAt/rdf:Description">
Venue Name: <xsl:value-of select="venue:name"/><br />
Venue ID: <xsl:value-of select="substring-after(@rdf:about,'#')"/><br />
Venue URL: <a href="{substring-after(venue:URL/@rdf:resource,'')}"> <xsl:value-of select="substring-after(venue:URL/@rdf:resource,'')"/> </a><br /> 
Address: <xsl:value-of select="venue:address"/><br />
City: <xsl:value-of select="venue:city"/><br />
Country: <xsl:value-of select="venue:country"/><br />
</xsl:template>

<xsl:template match="rdf:Description">
Artist Name: <xsl:value-of select="artist:name"/><br />
Artist ID: <xsl:value-of select="substring-after(@rdf:about,'#')"/><br />
Artist URL: <a href="{substring-after(artist:URL/@rdf:resource,'')}"> <xsl:value-of select="substring-after(artist:URL/@rdf:resource,'')"/> </a><br /> 
Short Bio: <xsl:value-of select="artist:bio"/><br />
</xsl:template>

</xsl:stylesheet> 