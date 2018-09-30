package org.commongeoregistry.adapter;

import java.io.Serializable;

import org.commongeoregistry.adapter.metadata.MetadataCache;

public abstract class RegistryInterface implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5085432383838987882L;
  
  private MetadataCache metadataCache;
  
  public RegistryInterface()
  {
    this.metadataCache = new MetadataCache();
  }
  
  public MetadataCache getMetadataCache()
  {
    return this.metadataCache;
  }
}
