package org.commongeoregistry.adapter;

import java.util.Date;
import java.util.List;

import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.MetadataFactory;
import org.commongeoregistry.adapter.metadata.HierarchyType.HierarchyNode;
import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class SerializationTest
{
  @Test
  public void testGeoObject()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    
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
    
    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    
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
    
    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    
    String sJson = state.toJSON().toString();
    GeoObjectType state2 = GeoObjectType.fromJSON(sJson, registry);
    String sJson2 = state2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testGeoObjectCustomAttributes()
  {
    RegistryServerInterface registryServerInterface = new RegistryServerInterface();
    
    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registryServerInterface);
    
    AttributeType testChar = AttributeType.factory("testChar",  "testCharLocalName", "testCharLocalDescrip", AttributeCharacterType.TYPE);
    AttributeType testDate = AttributeType.factory("testDate",  "testDateLocalName", "testDateLocalDescrip", AttributeDateType.TYPE);
    AttributeType testInteger = AttributeType.factory("testInteger",  "testIntegerLocalName", "testIntegerLocalDescrip", AttributeIntegerType.TYPE);
    AttributeType testTerm = AttributeType.factory("testTerm",  "testTermLocalName", "testTermLocalDescrip", AttributeTermType.TYPE);
    
    ((AttributeTermType)testTerm).setRootTerm(registryServerInterface.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.ROOT.code).get());
    
    state.addAttribute(testChar);
    state.addAttribute(testDate);
    state.addAttribute(testInteger);
    state.addAttribute(testTerm);
    
    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
    
    GeoObject geoObject = registryServerInterface.createGeoObject("State");
    
    geoObject.setWKTGeometry(geom);
    geoObject.setCode("Colorado");
    geoObject.setUid("CO");
    
    geoObject.setValue("testChar", "Test Character Value");
    geoObject.setValue("testDate", new Date());
    geoObject.setValue("testInteger", 3);
    geoObject.setValue("testTerm", registryServerInterface.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.PENDING.code).get());
    
    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registryServerInterface, sJson);
    String sJson2 = geoObject2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
    Assert.equals(geoObject.getValue("testChar"), geoObject2.getValue("testChar"));
    Assert.equals(geoObject.getValue("testDate"), geoObject2.getValue("testDate"));
    Assert.equals(geoObject.getValue("testInteger"), geoObject2.getValue("testInteger"));
    Assert.equals(geoObject.getValue("testTerm"), geoObject2.getValue("testTerm"));
    Assert.equals(((List<Term>)geoObject.getValue("testTerm")).get(0).getCode(), ((List<Term>)geoObject2.getValue("testTerm")).get(0).getCode());
  }
    
  /**
   * Tests to make sure that custom attributes can be added to GeoObjectTypes, and also that they are serialized correctly.
   */
  @Test
  public void testGeoObjectTypeCustomAttributes()
  {
    RegistryServerInterface registry = new RegistryServerInterface();
    
    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
    
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
    
    TestFixture.defineExampleHierarchies(registry);
    
    HierarchyType geoPolitical = registry.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();
    
    String geoPoliticalJson = geoPolitical.toJSON().toString();
    System.out.println(geoPoliticalJson);
//    HierarchyType HierarchyType.fromJSON(geoPoliticalJson);
  }
}
