<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns="http://www.opengis.net/wms"
                xmlns:xlink="http://www.w3.org/1999/xlink"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <xsl:param name="wms" select="'http://images.land.vic.gov.au/erdas-iws/ogc/wms?request=getcapabilities&amp;service=wms'"/>

	<xsl:output method="xml" indent="yes"/>

  <xsl:variable name="delwpwms" select="document($wms)"/>

	<!-- ================================================================= -->

  <xsl:template match="/">
    <layers>
		  <xsl:apply-templates select="$delwpwms/node()"/>
    </layers>
  </xsl:template>

	<!-- ================================================================= -->

  <xsl:template match="*[local-name()='Layer' and @queryable='1']" priority="50">
    <layer>
      <xsl:copy-of select="*[local-name()='Name']"/>
      <xsl:copy-of select="*[local-name()='EX_GeographicBoundingBox']"/>
    </layer>
  </xsl:template>

	<!-- ================================================================= -->

	<xsl:template match="node()">
		<xsl:apply-templates select="node()"/>
	</xsl:template>

	<!-- ================================================================= -->

</xsl:stylesheet>
