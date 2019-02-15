/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.HierarchyType.HierarchyNode;
import org.commongeoregistry.adapter.metadata.MetadataFactory;

public class TestFixture
{
  public static String                  PROVINCE                   = "PROVINCE";
  
  public static String                  DISTRICT                   = "DISTRICT";
  
  public static String                  COMMUNE                    = "COMMUNE";
  
  public static String                  VILLAGE                    = "VILLAGE";
  
  public static String                  HOUSEHOLD                  = "HOUSEHOLD";
  
  public static String                  FOCUS_AREA                 = "FOCUS_AREA";
  
  public static String                  HEALTH_FACILITY            = "HEALTH_FACILITY";
  
  public static String                  HEALTH_FACILITY_ATTRIBUTE  = "healthFacilityType";

  public static String                  GEOPOLITICAL               = "GEOPOLITICAL";

  public static String                  HEALTH_ADMINISTRATIVE      = "HEALTH_ADMINISTRATIVE";
  
  public static void defineExampleHierarchies(RegistryAdapter registry)
  {
    // Define GeoObject Types
    GeoObjectType province = MetadataFactory.newGeoObjectType(PROVINCE, GeometryType.POLYGON, "Province", "", false, registry);
    
    GeoObjectType district = MetadataFactory.newGeoObjectType(DISTRICT, GeometryType.POLYGON, "District", "", false, registry);
    
    GeoObjectType commune = MetadataFactory.newGeoObjectType(COMMUNE, GeometryType.POLYGON, "Commune", "", false, registry);
    
    GeoObjectType village = MetadataFactory.newGeoObjectType(VILLAGE, GeometryType.POLYGON, "Village", "", false, registry);
    
    GeoObjectType household = MetadataFactory.newGeoObjectType(HOUSEHOLD, GeometryType.POLYGON, "Household", "", false, registry);
    
//    GeoObjectType focusArea = MetadataFactory.newGeoObjectType(FOCUS_AREA, GeometryType.POLYGON, "Focus Area", "", false, registry);
    
    GeoObjectType healthFacility = MetadataFactory.newGeoObjectType(HEALTH_FACILITY, GeometryType.POLYGON, "Health Facility", "", false, registry);    
    healthFacility.addAttribute(createHealthFacilityTypeAttribute(registry));
    
    // Define Geopolitical Hierarchy Type
    HierarchyType geoPolitical = MetadataFactory.newHierarchyType(GEOPOLITICAL, "Geopolitical", "Geopolitical Hierarchy", registry);   
    HierarchyNode geoProvinceNode = new HierarchyType.HierarchyNode(province);
    HierarchyNode geoDistrictNode = new HierarchyType.HierarchyNode(district);
    HierarchyNode geoCommuneNode = new HierarchyType.HierarchyNode(commune);
    HierarchyNode geoVillageNode = new HierarchyType.HierarchyNode(village);
    HierarchyNode geoHouseholdNode = new HierarchyType.HierarchyNode(household);
    
    geoProvinceNode.addChild(geoDistrictNode);
    geoDistrictNode.addChild(geoCommuneNode);
    geoCommuneNode.addChild(geoVillageNode);
    geoVillageNode.addChild(geoHouseholdNode);
    
    geoPolitical.addRootGeoObjects(geoProvinceNode);
    
    // Define Health Administrative
    HierarchyType healthAdministrative = MetadataFactory.newHierarchyType(TestFixture.HEALTH_ADMINISTRATIVE, "Health Administrative", "Health Administrative Hierarchy", registry);
    HierarchyNode healthProvinceNode = new HierarchyType.HierarchyNode(province);
    HierarchyNode healthDistrictNode = new HierarchyType.HierarchyNode(district);
    HierarchyNode healthCommuneNode = new HierarchyType.HierarchyNode(commune);
    HierarchyNode healthFacilityNode = new HierarchyType.HierarchyNode(healthFacility);

    healthProvinceNode.addChild(healthDistrictNode);
    healthDistrictNode.addChild(healthCommuneNode);
    healthCommuneNode.addChild(healthFacilityNode);
    
    healthAdministrative.addRootGeoObjects(healthProvinceNode);
  }
  
  public static GeoObject createGeoObject(RegistryAdapter registry, String genKey, String typeCode)
  {
    GeoObject geoObject = registry.newGeoObjectInstance(typeCode);
    
    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
    
    geoObject.setWKTGeometry(geom);
    geoObject.setCode(genKey + "_CODE");
    geoObject.setUid(genKey + "_UID");
    geoObject.setLocalizedDisplayLabel(genKey + " Display Label");
    
    return geoObject;
  }
  
  private static AttributeTermType createHealthFacilityTypeAttribute(RegistryAdapter registry)
  {
    AttributeTermType attrType = 
        (AttributeTermType)AttributeType.factory(HEALTH_FACILITY_ATTRIBUTE, "Health Facility Type", "The type of health facility", AttributeTermType.TYPE);

    Term rootTerm = createHealthFacilityTerms(registry);
    
    attrType.setRootTerm(rootTerm);
    
    return attrType;
  }

  private static Term createHealthFacilityTerms(RegistryAdapter registry)
  {
    Term rootTerm = MetadataFactory.newTerm("CM:Health-Facility-Types", "Health Facility Types", "The types of health facilities within a country", registry);
    Term dispensary = MetadataFactory.newTerm("CM:Dispensary", "Dispensary", "", registry);
    Term privateClinic = MetadataFactory.newTerm("CM:Private-Clinic", "Private Clinic", "", registry);
    Term publicClinic = MetadataFactory.newTerm("CM:Public-Clinic", "Public Clinic", "", registry);
    Term matWard = MetadataFactory.newTerm("CM:Maternity-Ward", "Maternity Ward", "", registry);
    Term nursing = MetadataFactory.newTerm("CM:Nursing-Home", "Nursing Home", "", registry);
    
    rootTerm.addChild(dispensary);
    rootTerm.addChild(privateClinic);
    rootTerm.addChild(publicClinic);
    rootTerm.addChild(matWard);
    rootTerm.addChild(nursing);
    
    return rootTerm;
  }
}
