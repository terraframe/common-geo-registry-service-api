package org.commongeoregistry.adapter;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.HierarchyType.HierarchyNode;
import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class SerializationTest
{
  private static String                  PROVINCE                   = "PROVINCE";
  
  private static String                  DISTRICT                   = "DISTRICT";
  
  private static String                  COMMUNE                    = "COMMUNE";
  
  private static String                  VILLAGE                    = "VILLAGE";
  
  private static String                  HOUSEHOLD                  = "HOUSEHOLD";
  
  private static String                  FOCUS_AREA                 = "FOCUS_AREA";
  
  private static String                  HEALTH_FACILITY            = "HEALTH_FACILITY";
  
  private static String                  HEALTH_FACILITY_ATTRIBUTE  = "healthFacilityType";

  private static String                  GEOPOLITICAL               = "GEOPOLITICAL";

  private static String                  HEALTH_ADMINISTRATIVE      = "HEALTH_ADMINISTRATIVE";
  
  @Test
  public void testGeoObject()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    GeoObjectType province = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    registry.getMetadataCache().addGeoObjectType(province);
    
    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
    
    GeoObject geoObject = registry.createGeoObject("State");
    
    geoObject.setWKTGeometry(geom);
    geoObject.setCode("Colorado");
    geoObject.setUid("CO");
    
    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registry, sJson);
    String sJson2 = geoObject2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
    Assert.equals("Colorado", geoObject2.getCode());
    Assert.equals("CO", geoObject2.getUid());
  }
  
  /**
   * Tests to make sure optional values are allowed and handled properly.
   */
  @Test
  public void testOptionalGeoObject()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    GeoObjectType province = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    registry.getMetadataCache().addGeoObjectType(province);
    
    GeoObject geoObject = registry.createGeoObject("State");
    
    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registry, sJson);
    String sJson2 = geoObject2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
  }
  
  @Test
  public void testGeoObjectType()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    GeoObjectType state = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    registry.getMetadataCache().addGeoObjectType(state);
    
    String sJson = state.toJSON().toString();
    GeoObjectType state2 = GeoObjectType.fromJSON(sJson, registry);
    String sJson2 = state2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
  }
  
  /**
   * Tests to make sure that custom attributes can be added to GeoObjectTypes, and also that they are serialized correctly.
   */
  @Test
  public void testGeoObjectTypeCustomAttributes()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    GeoObjectType state = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    registry.getMetadataCache().addGeoObjectType(state);
    
    AttributeType testChar = AttributeType.factory("testChar", "testCharLocalName", "testCharLocalDescrip", AttributeCharacterType.TYPE);
    AttributeType testDate = AttributeType.factory("testDate", "testDateLocalName", "testDateLocalDescrip", AttributeDateType.TYPE);
    AttributeType testInteger = AttributeType.factory("testInteger", "testIntegerLocalName", "testDateLocalDescrip", AttributeIntegerType.TYPE);
    AttributeType testTerm = AttributeType.factory("testTerm", "testTermLocalName", "testTermLocalDescrip", AttributeTermType.TYPE);
    
    state.addAttribute(testChar);
    state.addAttribute(testDate);
    state.addAttribute(testInteger);
    state.addAttribute(testTerm);
    
    String sJson = state.toJSON().toString();
    GeoObjectType state2 = GeoObjectType.fromJSON(sJson, registry);
    String sJson2 = state2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
    Assert.equals(testChar.getName(), state2.getAttribute("testChar").get().getName());
    Assert.equals(testDate.getName(), state2.getAttribute("testDate").get().getName());
    Assert.equals(testInteger.getName(), state2.getAttribute("testInteger").get().getName());
    Assert.equals(testTerm.getName(), state2.getAttribute("testTerm").get().getName());
  }
  
  @Test
  public void testHierarchyType()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    // Define GeoObject Types
    GeoObjectType province = new GeoObjectType(PROVINCE, GeometryType.POLYGON, "Province", "", registry);
    registry.getMetadataCache().addGeoObjectType(province);
    
    GeoObjectType district = new GeoObjectType(DISTRICT, GeometryType.POLYGON, "District", "", registry);
    registry.getMetadataCache().addGeoObjectType(district);
    
    GeoObjectType commune = new GeoObjectType(COMMUNE, GeometryType.POLYGON, "Commune", "", registry);
    registry.getMetadataCache().addGeoObjectType(commune);
    
    GeoObjectType village = new GeoObjectType(VILLAGE, GeometryType.POLYGON, "Village", "", registry);
    registry.getMetadataCache().addGeoObjectType(village);
    
    GeoObjectType household = new GeoObjectType(HOUSEHOLD, GeometryType.POLYGON, "Household", "", registry);
    registry.getMetadataCache().addGeoObjectType(household);
    
    GeoObjectType focusArea = new GeoObjectType(FOCUS_AREA, GeometryType.POLYGON, "Focus Area", "", registry);
    registry.getMetadataCache().addGeoObjectType(focusArea);
    
    GeoObjectType healthFacility = new GeoObjectType(HEALTH_FACILITY, GeometryType.POLYGON, "Health Facility", "", registry);    
    registry.getMetadataCache().addGeoObjectType(healthFacility);
    
    // Define Geopolitical Hierarchy Type
    HierarchyType geoPolitical = new HierarchyType(GEOPOLITICAL, "Geopolitical", "Geopolitical Hierarchy");   
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
    
    
    String geoPoliticalJson = geoPolitical.toJSON().toString();
    System.out.println(geoPoliticalJson);
//    HierarchyType HierarchyType.fromJSON(geoPoliticalJson);
  }
}
