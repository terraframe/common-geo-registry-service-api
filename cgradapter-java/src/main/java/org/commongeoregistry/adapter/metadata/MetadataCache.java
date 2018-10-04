package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.GeoObject;

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
  private Map<String, Term> termMap;
  
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
    this.termMap = new ConcurrentHashMap<String, Term>();
  }
  
  public void addTerm(Term _term) 
  {
    this.termMap.put(_term.getCode(), _term);
  }
    
  public Optional<Term> getTerm(String _code) 
  {
    return Optional.of(this.termMap.get(_code));
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
