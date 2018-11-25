package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultTerms;
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
  private RegistryAdapter adapter;
  
  public MetadataCache(RegistryAdapter adapter)
  {
    this.adapter = adapter;
  }
  
  /** 
   * Clears the metadata cache.
   */
  public void rebuild()
  {
    this.geoGeoObjectTypeMap = new ConcurrentHashMap<String, GeoObjectType>();
    this.hierarchyTypeMap = new ConcurrentHashMap<String, HierarchyType>();
    this.termMap = new ConcurrentHashMap<String, Term>();

    DefaultTerms.buildGeoObjectStatusTree(adapter);
  }
  
  public void addTerm(Term term) 
  {
    this.termMap.put(term.getCode(), term);
  }
    
  public Optional<Term> getTerm(String code) 
  {
    return Optional.of(this.termMap.get(code));
  }
  
  public void addGeoObjectType(GeoObjectType geoObjectType) 
  {
    this.geoGeoObjectTypeMap.put(geoObjectType.getCode(), geoObjectType);
  }
    
  public Optional<GeoObjectType> getGeoObjectType(String code) 
  {
    return Optional.of(this.geoGeoObjectTypeMap.get(code));
  }
  
  public void removeGeoObjectType(String code)
  {
    this.geoGeoObjectTypeMap.remove(code);
  }
  
  public void addHierarchyType(HierarchyType hierarchyType) 
  {
    this.hierarchyTypeMap.put(hierarchyType.getCode(), hierarchyType);
  }
  
  public Optional<HierarchyType> getHierachyType(String code) 
  {
    return Optional.of(this.hierarchyTypeMap.get(code));
  }
  
  public void removeHierarchyType(String code)
  {
    this.hierarchyTypeMap.remove(code);
  }

  public GeoObjectType[] getAllGeoObjectTypes()
  {
    return this.geoGeoObjectTypeMap.values().toArray(new GeoObjectType[this.geoGeoObjectTypeMap.values().size()]);
  }

  public HierarchyType[] getAllHierarchyTypes()
  {
    return this.hierarchyTypeMap.values().toArray(new HierarchyType[this.hierarchyTypeMap.values().size()]);
  }
}
