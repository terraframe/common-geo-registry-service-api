package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Attribute implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -6682494916732516027L;

  private String            name;

  private String            type;

  public Attribute(String name, String type)
  {
    this.name = name;
    this.type = type;
  }

  public String getName()
  {
    return this.name;
  }

  /**
   * 
   * @return
   */
  public String getType()
  {
    return this.type;
  }

  public void validate(AttributeType attributeType, Object _value)
  {
    // Stub method for optional validation
  }

  public abstract Object getValue();

  public abstract void setValue(Object value);

  public static Attribute attributeFactory(AttributeType attributeType)
  {
    Attribute attribute;

    if (attributeType instanceof AttributeDateType)
    {
      attribute = new AttributeDate(attributeType.getName());
    }
    else if (attributeType instanceof AttributeIntegerType)
    {
      attribute = new AttributeInteger(attributeType.getName());
    }
    else if (attributeType instanceof AttributeFloatType)
    {
      attribute = new AttributeFloat(attributeType.getName());
    }
    else if (attributeType instanceof AttributeTermType)
    {
      attribute = new AttributeTerm(attributeType.getName());
    }
    else if (attributeType instanceof AttributeBooleanType)
    {
      attribute = new AttributeBoolean(attributeType.getName());
    }
    else
    {
      attribute = new AttributeCharacter(attributeType.getName());
    }

    return attribute;
  }

  public String toString()
  {
    return this.getName() + ": " + this.getValue();
  }

  public void toJSON(JsonObject geoObjProps)
  {
    Object value = this.getValue();
    if (value == null)
    {
      value = "";
    }

    geoObjProps.addProperty(this.getName(), value.toString());
  }

  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    this.setValue(jValue.getAsString());
  }

}
