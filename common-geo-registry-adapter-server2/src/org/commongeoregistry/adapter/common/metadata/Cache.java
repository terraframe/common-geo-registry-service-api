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
  private Map<String, HierarchyType> hierarchyTypeMap;
  
  private Cache()
  {
    this.geoGeoObjectTypeMap = new ConcurrentHashMap<String, GeoObjectType>();
    this.hierarchyTypeMap = new ConcurrentHashMap<String, HierarchyType>();
  }
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final Cache INSTANCE = new Cache();
  }
 
  /** 
   * Clears the metadata cache.
   */
  public static void clear()
  {
    Singleton.INSTANCE.geoGeoObjectTypeMap = new ConcurrentHashMap<String, GeoObjectType>();
    Singleton.INSTANCE.hierarchyTypeMap = new ConcurrentHashMap<String, HierarchyType>();
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
  
  public static void addHierarchyType(HierarchyType _hierarchyType) 
  {
    Singleton.INSTANCE.hierarchyTypeMap.put(_hierarchyType.getCode(), _hierarchyType);
  }
  
  public static Optional<HierarchyType> getHierachyType(String _code) 
  {
    return Optional.of(Singleton.INSTANCE.hierarchyTypeMap.get(_code));
  }
  
  public static void removeHierarchyType(String _code)
  {
    Singleton.INSTANCE.hierarchyTypeMap.remove(_code);
  }
}
