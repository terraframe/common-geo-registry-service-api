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
package org.commongeoregistry.adapter.metadata;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

public class MetadataFactory
{
  public static Term newTerm(String code, LocalizedValue localizedLabel, LocalizedValue localizedDescription, RegistryAdapter registry)
  {
    Term t = new Term(code, localizedLabel, localizedDescription);
    
    registry.getMetadataCache().addTerm(t);
    
    return t;
  }
  
  public static HierarchyType newHierarchyType(String code, LocalizedValue localizedLabel, LocalizedValue localizedDescription, RegistryAdapter registry)
  {
    HierarchyType ht = new HierarchyType(code, localizedLabel, localizedDescription);
    
    registry.getMetadataCache().addHierarchyType(ht);
    
    return ht;
  }
  
  public static GeoObjectType newGeoObjectType(String code, GeometryType geometryType, LocalizedValue localizedLabel, LocalizedValue localizedDescription, Boolean isLeaf, RegistryAdapter registry)
  {
    GeoObjectType got = new GeoObjectType(code, geometryType, localizedLabel, localizedDescription, isLeaf, registry);
    
    registry.getMetadataCache().addGeoObjectType(got);
    
    return got;
  }
}
