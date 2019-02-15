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
import java.util.Date;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.constants.AdapterConstants;
import org.commongeoregistry.adapter.metadata.AttributeDateType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeDate extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 5532076653984789765L;
  
  private Date date;
  
  public AttributeDate(String name)
  {
    super(name, AttributeDateType.TYPE);
    
    this.date = null;
  }
  
  @Override
  public void setValue(Object date)
  {
    this.setDate((Date)date);
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
  
  @Override
  public void toJSON(JsonObject geoObjProps)
  {  
    if (this.date != null)
    {
      String sDate = new java.text.SimpleDateFormat(AdapterConstants.DATE_FORMAT).format(this.date);
      
      geoObjProps.addProperty(this.getName(), sDate);
    }
  }
  
  @Override
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    try
    {
      this.setValue(new java.text.SimpleDateFormat(AdapterConstants.DATE_FORMAT).parse(jValue.getAsString()));
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
}
