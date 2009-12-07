<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"	
	xmlns:evnt="http://www.psylus.info/IWA/assignment3/events#"
    xmlns:artist="http://www.psylus.info/IWA/assignment3/artist#"
    xmlns:venue="http://www.psylus.info/IWA/assignment3/venue#"
>

<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" /> 
<xsl:template match="/">
 <rdf:RDF
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-shema#"
	xmlns:evnt="http://www.psylus.info/IWA/assignment3/events#"
    xmlns:artist="http://www.psylus.info/IWA/assignment3/artist#"
    xmlns:venue="http://www.psylus.info/IWA/assignment3/venue#"
 >	
 <xsl:apply-templates/>
 </rdf:RDF>
 </xsl:template>  

<xsl:template match="lfm/events">
	<xsl:for-each select="event">
	 <rdf:Description rdf:about="http://www.psylus.info/IWA/assignment3/events#{id}"> 
	 <evnt:url rdf:resource="{url}"/>
	 <evnt:title><xsl:value-of select="title"/></evnt:title>
	 <evnt:start_time><xsl:value-of select="startDate"/></evnt:start_time>
	 <xsl:if test="description!=''">
      <evnt:Description><xsl:value-of select="description"/></evnt:Description>
     </xsl:if> 
     <evnt:takesPlaceAt>
       <rdf:Description rdf:about="venue:{venue/id}">
        <venue:name><xsl:value-of select="venue/name"/></venue:name>
        <venue:address><xsl:value-of select="venue/location/street"/></venue:address>
        <venue:postal_code><xsl:value-of select="venue/location/postalcode"/></venue:postal_code>
        <venue:city><xsl:value-of select="venue/location/city"/></venue:city>
        <venue:country><xsl:value-of select="venue/location/country"/></venue:country>
        <xsl:if test="venue/website!=''">
         <venue:url rdf:resource="{venue/website}"/>
        </xsl:if>
       </rdf:Description>
     </evnt:takesPlaceAt>
     <xsl:if test="artists/artist">
       <xsl:for-each select="artists/artist">  
            <evnt:featuring><xsl:value-of select="."/></evnt:featuring>        
          </xsl:for-each>  
      </xsl:if>
     <xsl:if test="artists/headliner">
       <xsl:for-each select="artists/headliner">  
            <evnt:headliner><xsl:value-of select="."/></evnt:headliner>        
          </xsl:for-each>  
      </xsl:if>
     </rdf:Description>
   </xsl:for-each>    
 </xsl:template>
</xsl:stylesheet>
		
	
