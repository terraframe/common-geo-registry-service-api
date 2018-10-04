package org.commongeoregistry.adapter;

import org.commongeoregistry.adapter.RegistryServerInterface;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
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
    
    System.out.println(sJson);
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
}
