package org.commongeoregistry.adapter.metadata;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.GeometryType;

public class MetadataFactory
{
  public static Term newTerm(String code, String localizedLabel, String localizedDescription, RegistryAdapter registry)
  {
    Term t = new Term(code, localizedLabel, localizedDescription);
    
    registry.getMetadataCache().addTerm(t);
    
    return t;
  }
  
  public static HierarchyType newHierarchyType(String code, String localizedLabel, String localizedDescription, RegistryAdapter registry)
  {
    HierarchyType ht = new HierarchyType(code, localizedLabel, localizedDescription);
    
    registry.getMetadataCache().addHierarchyType(ht);
    
    return ht;
  }
  
  public static GeoObjectType newGeoObjectType(String code, GeometryType geometryType, String localizedLabel, String localizedDescription, Boolean isLeaf, RegistryAdapter registry)
  {
    GeoObjectType got = new GeoObjectType(code, geometryType, localizedLabel, localizedDescription, isLeaf, registry);
    
    registry.getMetadataCache().addGeoObjectType(got);
    
    return got;
  }
}
