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
package org.commongeoregistry.adapter.dataaccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.CustomSerializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

public class AttributeDate extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 5532076653984789765L;

  private Date              date;

  public AttributeDate(String name)
  {
    super(name, AttributeDateType.TYPE);

    this.date = null;
  }

  @Override
  public void setValue(Object date)
  {
    this.setDate((Date) date);
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  @Override
  public Date getValue()
  {
    return this.date;
  }
  
  private boolean isChangeOverTime()
  {
    boolean isChangeOverTime = true;
    DefaultAttribute defaultAttr = DefaultAttribute.getByAttributeName(this.getName());
    if (defaultAttr != null)
    {
      isChangeOverTime = defaultAttr.isChangeOverTime();
    }
    
    return isChangeOverTime;
  }

  @Override
  public JsonElement toJSON(CustomSerializer serializer)
  {
    if (this.date == null)
    {
      return JsonNull.INSTANCE;
    }
    
    if (!this.isChangeOverTime())
    {
      return new JsonPrimitive(this.date.getTime());
    }
    else
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.setTimeZone(TimeZone.getTimeZone("GMT"));
      return new JsonPrimitive(format.format(this.date));
    }
  }

  @Override
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    if (!this.isChangeOverTime())
    {
      long epoch = jValue.getAsLong();
      this.setValue(new Date(epoch));
    }
    else
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.setTimeZone(TimeZone.getTimeZone("GMT"));
      try
      {
        this.setValue(format.parse(jValue.getAsString()));
      }
      catch (ParseException e)
      {
        throw new RuntimeException(e); // TODO : Error handling
      }
    }
  }
}