<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:gml="http://www.opengis.net/gml/3.2"
  xmlns:gex="http://standards.iso.org/iso/19115/-3/gex/1.0"
  xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
  xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
  xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
  xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
  xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:wms="http://www.opengis.net/wms"
  xmlns:fme="http://www.safe.com/gml/fme"
  xmlns:mf="http://github.com/geonetwork-delwp/rastermeta"
  exclude-result-prefixes="fme xs wms mf">

  <xsl:param name="uuid"/>
  <!-- <xsl:param name="iwslayersfile"/> -->

  <xsl:output method="xml" indent="yes"/>

  <xsl:variable name="gml32" select="document(concat('https://services.land.vic.gov.au/catalogue/publicproxy/guest/dv_geoserver/wfs?request=getFeature&amp;version=2.0.0&amp;typename=RESOURCEFOOTPRINT&amp;cql_filter=MD_UUID%3d%27',$uuid,'%27'))"/>
  <!-- <xsl:variable name="iwsLayers" select="document($iwslayersfile)"/> -->

  <xsl:template match="mri:MD_DataIdentification">
    <!-- match and copy everything before mri:extent with geographicExtent, which must
         be replaced by the new gml32 content n the variable $gml32 --> 
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates select="mri:citation"/>
      <xsl:apply-templates select="mri:abstract"/>
      <xsl:apply-templates select="mri:purpose"/>
      <xsl:apply-templates select="mri:credit"/>
      <xsl:apply-templates select="mri:status"/>
      <xsl:apply-templates select="mri:pointOfContact"/>
      <xsl:apply-templates select="mri:spatialRepresentationType"/>
      <xsl:apply-templates select="mri:spatialResolution"/>
      <xsl:apply-templates select="mri:temporalResolution"/>
      <xsl:apply-templates select="mri:topicCategory"/>

      <xsl:choose>
        <xsl:when test="count($gml32//gml:Polygon) > 0">
          <xsl:apply-templates select="mri:extent[count(descendant::gex:geographicElement) = 0]"/>
          <!-- Now copy in the new gml -->
          <xsl:call-template name="fill"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="mri:extent"/>
        </xsl:otherwise>
      </xsl:choose>
          
      <xsl:apply-templates select="mri:additionalDocumentation"/>
      <xsl:apply-templates select="mri:processingLevel"/>
      <xsl:apply-templates select="mri:resourceMaintenance"/>

      <xsl:call-template name="doGraphicOverview"/>

      <xsl:apply-templates select="mri:resourceFormat"/>
      <xsl:apply-templates select="mri:descriptiveKeywords"/>
      <xsl:apply-templates select="mri:resourceSpecificUsage"/>
      <xsl:apply-templates select="mri:resourceConstraints"/>
      <xsl:apply-templates select="mri:associatedResource"/>

      <xsl:apply-templates select="mri:defaultLocale"/>
      <xsl:apply-templates select="mri:otherLocale"/>
      <xsl:apply-templates select="mri:environmentDescription"/>
      <xsl:apply-templates select="mri:supplementalInformation"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template name="fill">
      <xsl:message><xsl:copy-of select="$gml32"/></xsl:message>
      <mri:extent>
        <gex:EX_Extent>
          <gex:geographicElement>
            <gex:EX_BoundingPolygon>
    <xsl:for-each select="$gml32//gml:PolygonPatch|$gml32//gml:Polygon">
              <gex:polygon>
                <!-- Note we use the following srsName because the coord order
                     coming out of fme is lat,long - if that changes to long,lat
                     then this will need to be changed -->
                <gml:Polygon gml:id="{generate-id()}" srsName="urn:x-ogc:def:crs:EPSG:4326">
                  <xsl:copy-of select="./*"/>
                </gml:Polygon>
              </gex:polygon>
    </xsl:for-each>
            </gex:EX_BoundingPolygon>
          </gex:geographicElement>
        </gex:EX_Extent>
      </mri:extent>
  </xsl:template>

  <!-- this is a problem as the filename is no longer part of the wfs returned -->
  <xsl:template name="doGraphicOverview">
    <!--
    <xsl:choose>
      <xsl:when test="count($filename)>0">
          <xsl:variable name="fixedfilenames" select="mf:buildfixedfilenames(distinct-values($filename))"/>
          <xsl:message>USING FILENAMES: <xsl:sequence select="$fixedfilenames"/></xsl:message>
          <xsl:variable name="layers" select="string-join($iwsLayers/wms:layers/wms:layer[mf:containsF(wms:Name,$fixedfilenames)!='']/wms:Name,',')"/>
          <xsl:message>FOUND LAYERS: <xsl:value-of select="$layers"/></xsl:message>

          <xsl:variable name="wB" select="min($iwsLayers/wms:layers/wms:layer[mf:containsF(wms:Name,$fixedfilenames)!='']/wms:EX_GeographicBoundingBox/wms:westBoundLongitude)"/>
          <xsl:variable name="sB" select="min($iwsLayers/wms:layers/wms:layer[mf:containsF(wms:Name,$fixedfilenames)!='']/wms:EX_GeographicBoundingBox/wms:southBoundLatitude)"/>
          <xsl:variable name="eB" select="max($iwsLayers/wms:layers/wms:layer[mf:containsF(wms:Name,$fixedfilenames)!='']/wms:EX_GeographicBoundingBox/wms:eastBoundLongitude)"/>
          <xsl:variable name="nB" select="max($iwsLayers/wms:layers/wms:layer[mf:containsF(wms:Name,$fixedfilenames)!='']/wms:EX_GeographicBoundingBox/wms:northBoundLatitude)"/>
          <xsl:choose>
            <xsl:when test="normalize-space($layers)=''">
              <xsl:message>NO LAYERS FOUND</xsl:message>
              <xsl:apply-templates select="mri:graphicOverview"/>
            </xsl:when>
            <xsl:otherwise>
              <!- - <xsl:message>FOUND WMS Layer(s) <xsl:value-of select="$layers"/></xsl:message> - ->
              <xsl:variable name="bbox" select="concat($wB,',',$sB,',',$eB,',',$nB)"/>
              <mri:graphicOverview>
                <mcc:MD_BrowseGraphic>
                  <mcc:fileName gco:nilReason="inapplicable" />
                  <mcc:linkage>
                    <cit:CI_OnlineResource>
                      <cit:linkage>
                        <gco:CharacterString><xsl:value-of select="
concat('https://images.land.vic.gov.au/erdas-iws/ogc/wms?request=getmap&amp;service=wms&amp;layers=',$layers,'&amp;width=400&amp;height=200&amp;version=1.1.1&amp;format=image/jpeg&amp;styles=&amp;srs=epsg:4326&amp;bbox=',$bbox)
                        "/></gco:CharacterString>
                      </cit:linkage>
                      <cit:protocol>
                        <gco:CharacterString>OGC:WMS</gco:CharacterString>
                      </cit:protocol>
                      <cit:description>
                        <gco:CharacterString><xsl:value-of select="concat('Graphic Overview of Data Footprint from WMS ',$layers)"/></gco:CharacterString>
                      </cit:description>
                    </cit:CI_OnlineResource>
                  </mcc:linkage>
                </mcc:MD_BrowseGraphic>
              </mri:graphicOverview>
            </xsl:otherwise>
          </xsl:choose>
        
      </xsl:when>
      <xsl:otherwise>
        -->
        <xsl:apply-templates select="mri:graphicOverview"/>
        <!--
      </xsl:otherwise>
    </xsl:choose>
        -->
  </xsl:template>

  <xsl:function name="mf:buildfixedfilenames">
    <xsl:param name="fnames"/>
    <xsl:if test="count($fnames)>0">
      <xsl:variable name="fixed" select="if (contains($fnames[0],'.')) then
       mf:buildfixedfilenames( (remove($fnames,1), substring-before($fnames[0],'.')) )
                                         else
       mf:buildfixedfilenames( (remove($fnames,1), $fnames[0]) )"/>
    </xsl:if>
    <xsl:sequence select="$fnames"/>
  </xsl:function>

  <xsl:function name="mf:containsF" as="xs:string?">
    <xsl:param name="layer" as="xs:string"/>
    <xsl:param name="fixedfilenames"/>

    <xsl:for-each select="$fixedfilenames">
      <xsl:if test="contains($layer,.)">
        <xsl:copy-of select="."/>
      </xsl:if>
    </xsl:for-each>
  </xsl:function>

  <!-- copy everything else as is -->

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
