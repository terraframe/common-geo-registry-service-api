package org.commongeoregistry.adapter;


/**
 * This class is used by remote systems wishing to interface with the Common Geo-Registry. This will run on the remote system 
 * and will pull over the metadata for each {@link GeoObjectType}.
 * 
 * @author nathan
 *
 */
public class RegistryRemoteInterface extends RegistryInterface
{
  /**
   * 
   */
  private static final long serialVersionUID = -8311449977719450035L;
 
  // The URL of the Common Geo-Registry
  private String cgrURL;
  
  public RegistryRemoteInterface(String _cgrURL)
  {
    this.cgrURL = _cgrURL;
  }

}
