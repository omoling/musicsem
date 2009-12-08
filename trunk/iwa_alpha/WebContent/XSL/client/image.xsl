<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:foaf="http://xmlns.com/foaf/0.1/">
		
<xsl:output method="html" version='1.0' encoding='UTF-8' indent='yes'/>
<xsl:template match="/">
	<xsl:apply-templates select="rdf:RDF/rdf:Description"/>
</xsl:template>

<xsl:template match="rdf:RDF/rdf:Description">
	<xsl:if test="foaf:img">
		<xsl:for-each select="foaf:img">
			<xsl:value-of select="."/>||
		</xsl:for-each>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>
