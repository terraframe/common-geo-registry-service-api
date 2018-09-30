package org.commongeoregistry.adapter.server;

import java.io.Serializable;

import org.commongeoregistry.adapter.common.metadata.MetadataCache;

public abstract class Registry implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5085432383838987882L;
  
  private MetadataCache metadataCache;
  
  public Registry()
  {
    this.metadataCache = new MetadataCache();
  }
  
  public MetadataCache getMetadataCache()
  {
    return this.metadataCache;
  }
}
