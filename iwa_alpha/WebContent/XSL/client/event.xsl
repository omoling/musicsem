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
	<ul class="eventlist">
		<xsl:apply-templates select="rdf:RDF/rdf:Description"/>
	</ul>
</xsl:template>

<xsl:template match="rdf:RDF/rdf:Description">
	<xsl:if test="vCard:URL">
		<li class="eventlist">
			<strong>
			<xsl:value-of select="dc:date"/><br /></strong>
		
			<a target="_blank">
				<xsl:attribute name="href"><xsl:value-of select="./@rdf:resource" /></xsl:attribute>
				Website
			</a>
			<!-- 
			<a href="javascript:void(0);" onclick="updateMap(40.2, 40.2, 'Venue address');">London, United Kingdom</a>
			<a href="javascript:void(0);" onclick="newArtist('U2');">Moby</a>
			 -->
		</li>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>
