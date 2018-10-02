package org.commongeoregistry.adapter;

import java.util.List;
import java.util.Optional;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms.GeoObjectStatusTerm;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.HierarchyType.HierarchyNode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class RegistryServerInterfaceTest
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
  
  
  private static RegistryServerInterface registryServerInterface;
  
  public RegistryServerInterfaceTest()
  {
    super();
  }

  @BeforeClass
  public static void classSetUp()
  {
    registryServerInterface = new RegistryServerInterface();
    
    // Define GeoObject Types
    GeoObjectType province = new GeoObjectType(PROVINCE, GeometryType.POLYGON, "Province", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(province);
    
    GeoObjectType district = new GeoObjectType(DISTRICT, GeometryType.POLYGON, "District", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(district);
    
    GeoObjectType commune = new GeoObjectType(COMMUNE, GeometryType.POLYGON, "Commune", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(commune);
    
    GeoObjectType village = new GeoObjectType(VILLAGE, GeometryType.POLYGON, "Village", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(village);
    
    GeoObjectType household = new GeoObjectType(HOUSEHOLD, GeometryType.POLYGON, "Household", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(household);
    
    GeoObjectType focusArea = new GeoObjectType(FOCUS_AREA, GeometryType.POLYGON, "Focus Area", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(focusArea);
    
    GeoObjectType healthFacility = new GeoObjectType(HEALTH_FACILITY, GeometryType.POLYGON, "Health Facility", "");    
    healthFacility.addAttribute(createHealthFacilityTypeAttribute());
    registryServerInterface.getMetadataCache().addGeoObjectType(healthFacility);
    
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
    
    // Define Health Administrative
    HierarchyType healthAdministrative = new HierarchyType(HEALTH_ADMINISTRATIVE, "Health Administrative", "Health Administrative Hierarchy");
    HierarchyNode healthProvinceNode = new HierarchyType.HierarchyNode(province);
    HierarchyNode healthDistrictNode = new HierarchyType.HierarchyNode(district);
    HierarchyNode healthCommuneNode = new HierarchyType.HierarchyNode(commune);
    HierarchyNode healthFacilityNode = new HierarchyType.HierarchyNode(healthFacility);

    healthProvinceNode.addChild(healthDistrictNode);
    healthDistrictNode.addChild(healthCommuneNode);
    healthCommuneNode.addChild(healthFacilityNode);
    
    healthAdministrative.addRootGeoObjects(healthProvinceNode);
  }
  
  @AfterClass
  public static void classTeardown()
  {
    registryServerInterface.getMetadataCache().clear();
  }
  
  private static AttributeTermType createHealthFacilityTypeAttribute()
  {
    AttributeTermType attrType = 
        (AttributeTermType)AttributeType.factory(HEALTH_FACILITY_ATTRIBUTE, "Health Facility Type", "The type of health facility", AttributeTermType.TYPE);

    Term rootTerm = createHealthFacilityTerms();
    
    attrType.setRootTerm(rootTerm);
    
    return attrType;
  }
  
  private static Term createHealthFacilityTerms()
  {
    Term rootTerm = new Term("CM:Health-Facility-Types", "Health Facility Types", "The types of health facilities within a country");
    Term dispensary = new Term("CM:Dispensary", "Dispensary", "");
    Term privateClinic = new Term("CM:Private-Clinic", "Private Clinic", "");
    Term publicClinic = new Term("CM:Public-Clinic", "Public Clinic", "");
    Term matWard = new Term("CM:Maternity-Ward", "Maternity Ward", "");
    Term nursing = new Term("CM:Nursing-Home", "Nursing Home", "");
    
    rootTerm.addChild(dispensary);
    rootTerm.addChild(privateClinic);
    rootTerm.addChild(publicClinic);
    rootTerm.addChild(matWard);
    rootTerm.addChild(nursing);
    
    return rootTerm;
  }
  
  
  @Test
  public void testDefinedGeoTypes()
  {
    Assert.assertNotNull(PROVINCE+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(PROVINCE).get());

    Assert.assertNotNull(DISTRICT+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(DISTRICT).get());
    
    Assert.assertNotNull(COMMUNE+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(COMMUNE).get());
    
    Assert.assertNotNull(VILLAGE+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(VILLAGE).get());
    
    Assert.assertNotNull(HOUSEHOLD+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(HOUSEHOLD).get());
    
    Assert.assertNotNull(FOCUS_AREA+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(FOCUS_AREA).get());
    
    Assert.assertNotNull(HEALTH_FACILITY+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(HEALTH_FACILITY).get());
  }
  
  @Test
  public void testDefaultAttributes()
  { 
    Optional<GeoObjectType> geoObjectType = registryServerInterface.getMetadataCache().getGeoObjectType(PROVINCE);
    
    Assert.assertNotNull(PROVINCE+" was not found in the cache", geoObjectType.get());
    
    GeoObjectType province = geoObjectType.get();
    
    for (DefaultAttribute defaultAttribute : DefaultAttribute.values())
    {
      AttributeType attributeType = province.getAttribute(defaultAttribute.getName()).get();
      
      Assert.assertNotNull(defaultAttribute.getName()+"  was not defined as a default attribute", attributeType);
      
      if (defaultAttribute.getName().equals(DefaultAttribute.STATUS.getName()))
      {
        AttributeTermType statusAttribute = (AttributeTermType)attributeType;
        
        List<Term> terms = statusAttribute.getTerms();
        
        Assert.assertTrue(GeoObjectStatusTerm.values().length - 1 == terms.size());        
      }
    }
  }
  
  
  @Test
  public void testCreateGeoObject()
  {
    GeoObject geoObject = registryServerInterface.createGeoObject(HEALTH_FACILITY);
    
    geoObject.printAttributes();
    
  }
  
//  @Test
//  public void testHealthFacilityTypes()
//  { 
//    GeoObjectType healthFacility = registryServerInterface.getMetadataCache().getGeoObjectType(HEALTH_FACILITY).get();
//    
//    AttributeTermType attributeTermType = (AttributeTermType)healthFacility.getAttribute(HEALTH_FACILITY_ATTRIBUTE).get();
//    
//    List<Term> terms = attributeTermType.getTerms();
//  }
  
}
