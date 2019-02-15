/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter;

import java.io.Serializable;
import java.util.Map;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms.GeoObjectStatusTerm;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.id.AdapterIdServiceIF;
import org.commongeoregistry.adapter.id.EmptyIdCacheException;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.MetadataCache;

public abstract class RegistryAdapter implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5085432383838987882L;
  
  private MetadataCache metadataCache;
  
  private AdapterIdServiceIF idService;
  
  public RegistryAdapter(AdapterIdServiceIF idService)
  {
    this.metadataCache = new MetadataCache(this);
    this.metadataCache.rebuild();
    this.idService = idService;
  }
  
  public MetadataCache getMetadataCache()
  {
    return this.metadataCache;
  }
  
  public AdapterIdServiceIF getIdService()
  {
    return this.idService;
  }
  
  // TODO - Add support for a supplier provided exception.
  /**
   * Creates a new local {@link GeoObject} instance of the given type. If the local id cache is empty, an EmptyIdCacheException is thrown.
   * 
   * @param geoObjectTypeCode
   * @return a new local {@link GeoObject} instance of the given type.
   */
  public GeoObject newGeoObjectInstance(String geoObjectTypeCode) throws EmptyIdCacheException
  {
    GeoObjectType geoObjectType = this.getMetadataCache().getGeoObjectType(geoObjectTypeCode).get();
    
    Map<String, Attribute> attributeMap = GeoObject.buildAttributeMap(geoObjectType);
    
    GeoObject geoObject = new GeoObject(geoObjectType, geoObjectType.getGeometryType(), attributeMap);
    
    // Set some default values
//    geoObject.getAttribute(DefaultAttribute.TYPE.getName()).setValue(geoObjectTypeCode);
    
    Term newStatus = this.getMetadataCache().getTerm(GeoObjectStatusTerm.NEW.code).get();
    geoObject.getAttribute(DefaultAttribute.STATUS.getName()).setValue(newStatus);
    
    String uid = this.idService.next();
    geoObject.setUid(uid);
    
    return geoObject;
    
  }
}
