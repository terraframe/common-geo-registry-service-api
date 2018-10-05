package org.commongeoregistry.adapter.metadata;

import org.commongeoregistry.adapter.RegistryInterface;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.GeometryType;

public class MetadataFactory
{
  public static Term newTerm(String _code, String _localizedLabel, String _localizedDescription, RegistryInterface registry)
  {
    Term t = new Term(_code, _localizedLabel, _localizedDescription, registry);
    
    registry.getMetadataCache().addTerm(t);
    
    return t;
  }
  
  public static HierarchyType newHierarchyType(String _code, String _localizedLabel, String _localizedDescription, RegistryInterface registry)
  {
    HierarchyType ht = new HierarchyType(_code, _localizedLabel, _localizedDescription);
    
    registry.getMetadataCache().addHierarchyType(ht);
    
    return ht;
  }
  
  public static GeoObjectType newGeoObjectType(String _code, GeometryType _geometryType, String _localizedLabel, String _localizedDescription, RegistryInterface registry)
  {
    GeoObjectType got = new GeoObjectType(_code, _geometryType, _localizedLabel, _localizedDescription, registry);
    
    registry.getMetadataCache().addGeoObjectType(got);
    
    return got;
  }
}
