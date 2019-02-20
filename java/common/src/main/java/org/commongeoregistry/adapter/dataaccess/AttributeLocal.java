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
package org.commongeoregistry.adapter.dataaccess;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.commongeoregistry.adapter.metadata.AttributeLocalType;

public class AttributeLocal extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = -506321096607959557L;

  private LocalizedValue    value;

  public AttributeLocal(String name)
  {
    super(name, AttributeLocalType.TYPE);
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof LocalizedValue)
    {
      this.value = (LocalizedValue) value;
    }
  }

  public void setValue(Locale locale, String value)
  {
    this.value.setValue(locale, value);
  }

  @Override
  public Object getValue()
  {
    return this.value;
  }

}
