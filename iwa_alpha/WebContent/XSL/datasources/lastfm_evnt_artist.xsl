<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:dcterms="http://purl.org/dc/terms/" 
    xmlns:foaf="http://xmlns.com/foaf/0.1/" 
    xmlns:m="http://www.kanzaki.com/ns/music#" 
    xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#"   
    xmlns:vCard = "http://www.w3.org/2001/vcard-rdf/3.0#"

>

<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" /> 
<xsl:template match="/">
 <rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:dcterms="http://purl.org/dc/terms/" 
    xmlns:foaf="http://xmlns.com/foaf/0.1/" 
    xmlns:m="http://www.kanzaki.com/ns/music#" 
    xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#"
    xmlns:vCard = "http://www.w3.org/2001/vcard-rdf/3.0#"
 >	
 <xsl:apply-templates/>
 </rdf:RDF>
 </xsl:template>  

<xsl:template match="lfm/events">
	<xsl:for-each select="event">
     <m:Concert>
	   <dc:title><xsl:value-of select="title"/></dc:title>
	   <dc:date><xsl:value-of select="startDate"/></dc:date>
	   <vCard:URL rdf:resource="{url}"/>
	  <xsl:if test="description!=''">
       <dc:description><xsl:value-of select="description"/></dc:description>
      </xsl:if>
      <xsl:if test="artists/artist">
       <xsl:for-each select="artists/artist">  
       </xsl:for-each>  
      </xsl:if>
    
     <xsl:if test="artists/headliner">
       <xsl:for-each select="artists/headliner">     
             <m:performerName><xsl:value-of select="."/></m:performerName>      
       </xsl:for-each>  
      </xsl:if>
        <m:event_description>
       <m:Venue>
          <dc:title><xsl:value-of select="venue/name"/></dc:title>
        <xsl:if test="venue/website!=''">
          <vCard:URL rdf:resource="{venue/website}"/>
        </xsl:if>
         <vCard:ADRTYPES>
         <vCard:Street><xsl:value-of select="venue/location/street"/></vCard:Street>
         <vCard:Locality><xsl:value-of select="venue/location/city"/></vCard:Locality>
         <vCard:Pcode> <xsl:value-of select="venue/location/postalcode"/></vCard:Pcode>  
         <vCard:Country> <xsl:value-of select="venue/location/country"/></vCard:Country> 
        </vCard:ADRTYPES>
        <geo:Point>
         <geo:lat><xsl:value-of select="venue/location/geo:point/geo:lat"/></geo:lat>
         <geo:long><xsl:value-of select="venue/location/geo:point/geo:long"/></geo:long>
        </geo:Point>
      </m:Venue>
      </m:event_description>
     </m:Concert>
   </xsl:for-each>    
 </xsl:template>
</xsl:stylesheet>
		
	
