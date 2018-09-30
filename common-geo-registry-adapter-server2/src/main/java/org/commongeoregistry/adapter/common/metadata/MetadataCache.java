package org.commongeoregistry.adapter.common.metadata;

import java.io.Serializable;
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
public class MetadataCache implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -8829469298178067536L;
  private Map<String, GeoObjectType> geoGeoObjectTypeMap;
  private Map<String, HierarchyType> hierarchyTypeMap;
  
  public MetadataCache()
  {
    this.clear();
  }
  
  /** 
   * Clears the metadata cache.
   */
  public void clear()
  {
    this.geoGeoObjectTypeMap = new ConcurrentHashMap<String, GeoObjectType>();
    this.hierarchyTypeMap = new ConcurrentHashMap<String, HierarchyType>();
  }
  
  public void addGeoObjectType(GeoObjectType _geoObjectType) 
  {
    this.geoGeoObjectTypeMap.put(_geoObjectType.getCode(), _geoObjectType);
  }
    
  public Optional<GeoObjectType> getGeoObjectType(String _code) 
  {
    return Optional.of(this.geoGeoObjectTypeMap.get(_code));
  }
  
  public void removeGeoObjectType(String _code)
  {
    this.geoGeoObjectTypeMap.remove(_code);
  }
  
  public void addHierarchyType(HierarchyType _hierarchyType) 
  {
    this.hierarchyTypeMap.put(_hierarchyType.getCode(), _hierarchyType);
  }
  
  public Optional<HierarchyType> getHierachyType(String _code) 
  {
    return Optional.of(this.hierarchyTypeMap.get(_code));
  }
  
  public void removeHierarchyType(String _code)
  {
    this.hierarchyTypeMap.remove(_code);
  }
}
