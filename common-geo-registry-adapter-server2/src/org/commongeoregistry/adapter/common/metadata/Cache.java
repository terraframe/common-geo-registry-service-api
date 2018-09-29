package org.commongeoregistry.adapter.common.metadata;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.server.GeoObject;

/**
 * This is a singleton instance that caches {@link GeoObjectType} objects for creating {@link GeoObject}s and 
 * that caches {@link HierarchyType}.
 * 
 * @author nathan
 *
 */
public class Cache
{
  private Map<String, GeoObjectType> geoGeoObjectTypeMap;
  
  private Cache()
  {
    this.geoGeoObjectTypeMap = new ConcurrentHashMap<String, GeoObjectType>();
  }
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final Cache INSTANCE = new Cache();
  }
  
  public static void addGeoObjectType(GeoObjectType _geoObjectType) 
  {
    Singleton.INSTANCE.geoGeoObjectTypeMap.put(_geoObjectType.getCode(), _geoObjectType);
  }
    
  public static Optional<GeoObjectType> getGeoObjectType(String _code) 
  {
    return Optional.of(Singleton.INSTANCE.geoGeoObjectTypeMap.get(_code));
  }
  
  public static void removeGeoObjectType(String _code)
  {
    Singleton.INSTANCE.geoGeoObjectTypeMap.remove(_code);
  }
  
  
  
  
}
