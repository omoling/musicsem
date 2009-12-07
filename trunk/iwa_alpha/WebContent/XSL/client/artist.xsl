<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:foaf="http://xmlns.com/foaf/0.1/"
	xmlns:dbpprop="http://dbpedia.org/property/" 
    xmlns:dbpedia-owl="http://dbpedia.org/ontology/">
		
<xsl:output method="html" version='1.0' encoding='UTF-8' indent='yes'/>
<xsl:template match="/">
	<div style="padding:3px;">
		<xsl:apply-templates select="rdf:RDF/rdf:Description"/>
	</div>
</xsl:template>

<xsl:template match="rdf:RDF/rdf:Description">
	<strong><span id="artist-name"><xsl:value-of select="foaf:name"/></span></strong><br/>
	<xsl:if test="dbpedia-owl:thumbnail">
		<div style="float:right">
			<img width="100" height="100">
				<xsl:attribute name="src"><xsl:value-of select="dbpedia-owl:thumbnail/@rdf:resource"/> </xsl:attribute>
			</img>
		</div>
	</xsl:if>
	<span id="artist-description">
		<xsl:value-of select="dbpprop:abstract"/>
	</span><br/><br/>
	<span id="artist-yearsactive"><strong>Active: </strong><xsl:value-of select="dbpprop:yearsActive"/></span>
	<br/><br/>
	<xsl:if test="dbpprop:currentMembers">
		<strong>Members:</strong><br/>
		<xsl:for-each select="dbpprop:currentMembers">
			<a target="_blank">
				<xsl:attribute name="href"><xsl:value-of select="./@rdf:resource" /></xsl:attribute>
				<xsl:value-of select="substring-after(./@rdf:resource,'http://dbpedia.org/resource/')"/>
			</a><br/>
		</xsl:for-each>
		<br/>
	</xsl:if>
	<xsl:if test="dbpprop:genre">
		<strong>Genres: </strong><br/>
		<xsl:for-each select="dbpprop:genre">
			<a target="_blank">
				<xsl:attribute name="href"><xsl:value-of select="./@rdf:resource"/></xsl:attribute>
				<xsl:value-of select="substring-after(./@rdf:resource,'http://dbpedia.org/resource/')"/>
			</a><br/>
		</xsl:for-each>
		<br/>
	</xsl:if>
	<strong>Web: </strong> 
	<a target="_blank">
		<xsl:attribute name="href"><xsl:value-of select="dbpprop:url/@rdf:resource"/></xsl:attribute>
		<xsl:value-of select="dbpprop:url/@rdf:resource" />
	</a><br/>
</xsl:template>

</xsl:stylesheet>
