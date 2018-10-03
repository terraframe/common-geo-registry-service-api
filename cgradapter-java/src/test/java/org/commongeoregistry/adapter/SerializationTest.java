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
    
    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
    
    GeoObject geoObject = registryServerInterface.createGeoObject("Province");
    geoObject.setWKTGeometry(geom);
    
    System.out.println(geoObject.toJSON().toString());
    
//    Assert.assertNotNull(PROVINCE+" was not found in the cache", registryServerInterface.getMetadataCache().getGeoObjectType(PROVINCE).get());
  }
}
