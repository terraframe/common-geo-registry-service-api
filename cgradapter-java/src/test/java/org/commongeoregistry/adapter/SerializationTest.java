package org.commongeoregistry.adapter;

import org.commongeoregistry.adapter.RegistryServerInterface;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.junit.Test;

public class SerializationTest
{
  @Test
  public void testGeoObjectToJSON()
  {
    RegistryServerInterface registryServerInterface = new RegistryServerInterface();
    
    GeoObjectType province = new GeoObjectType("Province", GeometryType.POLYGON, "Province", "");
    registryServerInterface.getMetadataCache().addGeoObjectType(province);
    
    GeoObject geoObject = registryServerInterface.createGeoObject("Province");
    
    System.out.println(geoObject.toJSON().build().toString());
    
//    Assert.assertNotNull(PROVINCE+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(PROVINCE).get());
  }
}
