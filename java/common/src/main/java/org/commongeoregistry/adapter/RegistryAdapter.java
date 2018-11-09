package org.commongeoregistry.adapter;

import java.io.Serializable;
import java.util.Map;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms.GeoObjectStatusTerm;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.MetadataCache;

public abstract class RegistryAdapter implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5085432383838987882L;
  
  private MetadataCache metadataCache;
  
  public RegistryAdapter()
  {
    this.metadataCache = new MetadataCache(this);
    this.metadataCache.rebuild();
  }
  
  public MetadataCache getMetadataCache()
  {
    return this.metadataCache;
  }
  
  // TODO - Add support for a supplier provided exception.
  /**
   * Creates a new local {@link GeoObject} instance of the given type.
   * 
   * @param geoObjectTypeCode
   * @return a new local {@link GeoObject} instance of the given type.
   */
  public GeoObject newGeoObjectInstance(String geoObjectTypeCode)
  {
    GeoObjectType geoObjectType = this.getMetadataCache().getGeoObjectType(geoObjectTypeCode).get();
    
    Map<String, Attribute> attributeMap = GeoObject.buildAttributeMap(geoObjectType);
    
    GeoObject geoObject = new GeoObject(geoObjectType, geoObjectType.getGeometryType(), attributeMap);
    
    // Set some default values
//    geoObject.getAttribute(DefaultAttribute.TYPE.getName()).setValue(geoObjectTypeCode);
    
    Term newStatus = this.getMetadataCache().getTerm(GeoObjectStatusTerm.NEW.code).get();
    geoObject.getAttribute(DefaultAttribute.STATUS.getName()).setValue(newStatus);
    
    return geoObject;
    
  }
}
