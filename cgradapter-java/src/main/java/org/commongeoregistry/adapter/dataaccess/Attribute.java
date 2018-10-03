package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;

import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.google.gson.JsonObject;

public abstract class Attribute implements Serializable
{  
  /**
   * 
   */
  private static final long serialVersionUID = -6682494916732516027L;
  
  private String name;
  
  private String type;
  

  public Attribute(String _name, String _type)
  {
    this.name = _name;
    this.type = _type;
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
  
  public abstract Object getValue();
  
  public abstract void setValue(Object _value);
  
  public static Attribute attributeFactory(AttributeType _attributeType)
  {
    Attribute attribute; 
    
    if (_attributeType instanceof AttributeDateType)
    {
      attribute = new AttributeDate(_attributeType.getName());
    }
    else if (_attributeType instanceof AttributeIntegerType)
    {
      attribute = new AttributeInteger(_attributeType.getName());
    }
    else if (_attributeType instanceof AttributeTermType)
    {
      attribute = new AttributeTerm(_attributeType.getName());
    }
    else 
    {
      attribute = new AttributeCharacter(_attributeType.getName());
    }
    
    return attribute;
  }
  
  public String toString()
  {
    return this.getName()+": "+this.getValue();
  }


  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.addProperty("name", this.name);
    
    json.addProperty("type", this.type);
    
    return json;
  }
  
}
