/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Common Geo Registry Adapter(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.metadata;

import java.util.Collection;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;

public class DefaultSerializer implements CustomSerializer
{

  @Override
  public Collection<AttributeType> attributes(GeoObjectType type)
  {
    return type.getAttributeMap().values();
  }

  @Override
  public void configure(AttributeType attributeType, JsonObject json)
  {
    // Do nothing
  }

  @Override
  public void configure(LocalizedValue localizedValue, JsonObject object)
  {
    // Do nothing
  }

  @Override
  public void configure(GeoObjectType type, JsonObject json)
  {
    // Do nothing
  }

  @Override
  public void configure(HierarchyType type, JsonObject json)
  {
    // Do nothing
  }

}
