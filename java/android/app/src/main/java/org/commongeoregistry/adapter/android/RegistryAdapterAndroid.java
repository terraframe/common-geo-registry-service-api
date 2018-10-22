package org.commongeoregistry.adapter.android;

import org.commongeoregistry.adapter.RegistryAdapterRemote;


public class RegistryAdapterAndroid extends RegistryAdapterRemote
{
  /**
   * 
   */
  private static final long serialVersionUID = 2367836756416546643L;
  
  private LocalObjectCache localObjectCache;

  /**
   * 
   * 
   * @param _cgrURL URL to the common geo-registry
   */
  public RegistryAdapterAndroid(String _cgrURL)
  {
    super(_cgrURL);
    
    this.localObjectCache = new LocalObjectCache();
  }
  
  /**
   * Returns a reference to the object that is managing the local persisted
   * cache on the Android device.
   * 
   * @return a reference to the object that is managing the local persisted
   * cache on the Android device.
   */
  public LocalObjectCache getLocalCache()
  {
    return this.localObjectCache;
  }
  
  /**
   * All modified objects that have been persisted will be pushed to the
   * 
   * common geo-registry.
   */
  public void pushObjectsToRegistry()
  {
    
  }

}
