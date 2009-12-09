<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" 
    xmlns:foaf="http://xmlns.com/foaf/0.1/" 
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" 
    xmlns:owl="http://www.w3.org/2002/07/owl#" 
    xmlns:m="http://www.kanzaki.com/ns/music#" 
    xmlns:dbpedia2="http://dbpedia.org/property/" 
    xmlns:mindswap="http://www.mindswap.org/~glapizco/technical.owl#" 
    xmlns:xsd="http://www.w3.org/2001/XMLSchema#" 
    xmlns:dbpedia="http://dbpedia.org/resource/" 
    xmlns:vCard="http://www.w3.org/2001/vcard-rdf/3.0#" 
    xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" 
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:dbpedia-owl="http://dbpedia.org/ontology/"
    >
		
<xsl:output method="html" version='1.0' encoding='UTF-8' indent='yes'/>
<xsl:template match="/">
	<div style="padding:3px;">
		<xsl:apply-templates select="rdf:RDF/rdf:Description"/>
	</div>
</xsl:template>

<xsl:template match="rdf:RDF/rdf:Description">
	<xsl:if test="foaf:name">
		<strong><span id="artist-name"><xsl:value-of select="foaf:name"/></span></strong><br/>
	</xsl:if>
	<xsl:if test="dbpedia-owl:thumbnail">
		<div style="float:right">
			<img width="100" height="100">
				<xsl:attribute name="src"><xsl:value-of select="dbpedia-owl:thumbnail/@rdf:resource"/> </xsl:attribute>
			</img>
		</div>
	</xsl:if>
	<xsl:if test="dbpedia2:abstract">
		<span id="artist-description">
			<xsl:value-of select="dbpedia2:abstract"/>
		</span><br/><br/>
	</xsl:if>
	<xsl:if test="dbpedia2:yearsActive">
		<span id="artist-yearsactive">
			<strong>Active: </strong><xsl:value-of select="dbpedia2:yearsActive"/>
		</span>
		<br/><br/>
	</xsl:if>
	<xsl:if test="dbpedia2:currentMembers">
		<strong>Members:</strong><br/>
		<xsl:for-each select="dbpedia2:currentMembers">
			<a target="_blank">
				<xsl:attribute name="href"><xsl:value-of select="./@rdf:resource" /></xsl:attribute>
				<xsl:value-of select="substring-after(./@rdf:resource,'http://dbpedia.org/resource/')"/>
			</a><br/>
		</xsl:for-each>
		<br/>
	</xsl:if>
	<xsl:if test="dbpedia2:genre">
		<strong>Genres: </strong><br/>
		<xsl:for-each select="dbpedia2:genre">
			<a target="_blank">
				<xsl:attribute name="href"><xsl:value-of select="./@rdf:resource"/></xsl:attribute>
				<xsl:value-of select="substring-after(./@rdf:resource,'http://dbpedia.org/resource/')"/>
			</a><br/>
		</xsl:for-each>
		<br/>
	</xsl:if>
	<xsl:if test="dbpedia2:url">
		<strong>Web: </strong> 
		<a target="_blank">
			<xsl:attribute name="href"><xsl:value-of select="dbpedia2:url/@rdf:resource"/></xsl:attribute>
			<xsl:value-of select="dbpedia2:url/@rdf:resource" />
		</a><br/>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>
