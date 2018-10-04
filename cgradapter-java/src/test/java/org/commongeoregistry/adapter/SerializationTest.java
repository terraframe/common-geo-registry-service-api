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
import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class SerializationTest
{
  @Test
  public void testGeoObject()
  {
    RegistryServerInterface registryServerInterface = new RegistryServerInterface();
    
    GeoObjectType province = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registryServerInterface);
    registryServerInterface.getMetadataCache().addGeoObjectType(province);
    
    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
    
    GeoObject geoObject = registryServerInterface.createGeoObject("State");
    
    geoObject.setWKTGeometry(geom);
    geoObject.setCode("Colorado");
    geoObject.setUid("CO");
    
    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registryServerInterface, sJson);
    String sJson2 = geoObject2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
  }
  
  /**
   * Tests to make sure optional values are allowed and handled properly.
   */
  @Test
  public void testOptionalGeoObject()
  {
    RegistryServerInterface registryServerInterface = new RegistryServerInterface();
    
    GeoObjectType province = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registryServerInterface);
    registryServerInterface.getMetadataCache().addGeoObjectType(province);
    
    GeoObject geoObject = registryServerInterface.createGeoObject("State");
    
    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registryServerInterface, sJson);
    String sJson2 = geoObject2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
  }
  
  @Test
  public void testGeoObjectType()
  {
    RegistryServerInterface registryServerInterface = new RegistryServerInterface();
    
    GeoObjectType state = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registryServerInterface);
    registryServerInterface.getMetadataCache().addGeoObjectType(state);
    
    String sJson = state.toJSON().toString();
    GeoObjectType state2 = GeoObjectType.fromJSON(sJson, registryServerInterface);
    String sJson2 = state2.toJSON().toString();
    
    Assert.equals(sJson, sJson2);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testGeoObjectCustomAttributes()
  {
    RegistryServerInterface registryServerInterface = new RegistryServerInterface();
    
    GeoObjectType state = new GeoObjectType("State", GeometryType.POLYGON, "State", "", registryServerInterface);
    registryServerInterface.getMetadataCache().addGeoObjectType(state);
    
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
}
