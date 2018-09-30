package com.cgr.adapter.server;

/**
 * This class is used to manage the metadata and the {@link GeoObject}s that are managed by an implementation of 
 * the Common Geo-Registry. The {@link MetadataCache} is populated with the {@link GeoObjectType}s and the 
 * {@link HierarchyType}s implemented on the local instance. When remote systems that want to interface with this 
 * implementation of the CommonGeoRegistry
 * 
 * @author nathan
 *
 */
public class RegistryServer extends Registry
{
  /**
   * 
   */
  private static final long serialVersionUID = -3343727858910300438L;

  public RegistryServer()
  {
    super();
  }
}
