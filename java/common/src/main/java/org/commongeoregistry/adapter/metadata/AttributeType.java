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

import java.io.Serializable;
import java.util.Locale;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;

/**
 * Primary abstraction for attribute metadata on {@link GeoObjectType}.
 * 
 * @author nathan
 *
 */
public abstract class AttributeType implements Serializable
{
  /**
   * 
   */
  private static final long  serialVersionUID           = -2037233821367602621L;

  public static final String JSON_CODE                  = "code";

  public static final String JSON_LOCALIZED_LABEL       = "label";

  public static final String JSON_LOCALIZED_DESCRIPTION = "description";

  public static final String JSON_TYPE                  = "type";

  public static final String JSON_IS_DEFAULT            = "isDefault";

  private String             name;

  private LocalizedValue              label;

  private LocalizedValue              description;

  private String             type;                                               // Attribute
                                                                                 // Type
                                                                                 // Constant

  private boolean            isDefault;

  // TODO add a boolean for if the attribute is required or not

  public AttributeType(String _name, LocalizedValue _label, LocalizedValue _description, String _type, boolean _isDefault)
  {
    this.name = _name;
    this.label = _label;
    this.description = _description;
    this.type = _type;
    this.isDefault = _isDefault;
  }

  public String getName()
  {
    return this.name;
  }

  public String getType()
  {
    return this.type;
  }

  public LocalizedValue getLabel()
  {
    return this.label;
  }

  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }

  public void setLabel(String label)
  {
    this.label.setValue(label);
  }

  public void setLabel(Locale locale, String label)
  {
    this.label.setValue(locale, label);
  }

  public LocalizedValue getDescription()
  {
    return this.description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public void setDescription(String description)
  {
    this.description.setValue(description);
  }

  public void setDescription(Locale locale, String description)
  {
    this.description.setValue(locale, description);
  }

  public boolean getIsDefault()
  {
    return this.isDefault;
  }

  public void validate(Object _value)
  {
    // Stub method used to validate the value according to the metadata of the
    // AttributeType
  }

  public static AttributeType factory(String _name, LocalizedValue _label, LocalizedValue _description, String _type)
  {
    AttributeType attributeType = null;

    boolean _isDefault = false;
    if (DefaultAttribute.UID.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.UID.getIsDefault();
    }
    else if (DefaultAttribute.CODE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.CODE.getIsDefault();
    }
    else if (DefaultAttribute.DISPLAY_LABEL.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.DISPLAY_LABEL.getIsDefault();
    }
    else if (DefaultAttribute.TYPE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.TYPE.getIsDefault();
    }
    else if (DefaultAttribute.STATUS.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.STATUS.getIsDefault();
    }
    else if (DefaultAttribute.SEQUENCE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.SEQUENCE.getIsDefault();
    }
    else if (DefaultAttribute.CREATE_DATE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.CREATE_DATE.getIsDefault();
    }
    else if (DefaultAttribute.LAST_UPDATE_DATE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.LAST_UPDATE_DATE.getIsDefault();
    }

    if (_type.equals(AttributeCharacterType.TYPE))
    {
      attributeType = new AttributeCharacterType(_name, _label, _description, _isDefault);
    }
    else if (_type.equals(AttributeLocalType.TYPE))
    {
      attributeType = new AttributeLocalType(_name, _label, _description, _isDefault);
    }
    else if (_type.equals(AttributeDateType.TYPE))
    {
      attributeType = new AttributeDateType(_name, _label, _description, _isDefault);
    }
    else if (_type.equals(AttributeIntegerType.TYPE))
    {
      attributeType = new AttributeIntegerType(_name, _label, _description, _isDefault);
    }
    else if (_type.equals(AttributeFloatType.TYPE))
    {
      attributeType = new AttributeFloatType(_name, _label, _description, _isDefault);
    }
    else if (_type.equals(AttributeTermType.TYPE))
    {
      attributeType = new AttributeTermType(_name, _label, _description, _isDefault);
    }
    else if (_type.equals(AttributeBooleanType.TYPE))
    {
      attributeType = new AttributeBooleanType(_name, _label, _description, _isDefault);
    }

    return attributeType;
  }

  public JsonObject toJSON()
  {
    return this.toJSON(new DefaultSerializer());
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject json = new JsonObject();

    json.addProperty(JSON_CODE, this.getName());

    json.addProperty(JSON_TYPE, this.getType());

    json.add(JSON_LOCALIZED_LABEL, this.getLabel().toJSON());

    json.add(JSON_LOCALIZED_DESCRIPTION, this.getDescription().toJSON());

    json.addProperty(JSON_IS_DEFAULT, this.getIsDefault());

    serializer.configure(this, json);

    return json;
  }

  /**
   * Populates any additional attributes from JSON that were not populated in
   * {@link GeoObjectType#fromJSON(String, org.commongeoregistry.adapter.RegistryAdapter)}
   * 
   * @param attrObj
   * @return {@link AttributeType}
   */
  public void fromJSON(JsonObject attrObj)
  {
  }
}
