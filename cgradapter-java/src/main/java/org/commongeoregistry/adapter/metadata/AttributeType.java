package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;

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
  private static final long serialVersionUID                = -2037233821367602621L;
  
  public static final String JSON_NAME                      = "name";
  
  public static final String JSON_LOCALIZED_LABEL           = "localizedLabel";
  
  public static final String JSON_LOCALIZED_DESCRIPTION     = "localizedDescription";
  
  public static final String JSON_TYPE                      = "type";

  private String name;

  private String localizedLabel;

  private String localizedDescription;

  private String type;                // Attribute Type Constant
  
  //TODO add a boolean for if the attribute is required or not

  public AttributeType(String _name, String _localizedLabel, String _localizedDescription, String _type)
  {
    this.name = _name;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    this.type = _type;
  }

  public String getName()
  {
    return this.name;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }
  
  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }
  
  public static AttributeType factory(String _name, String _localizedLabel, String _localizedDescription, String _type)
  {
    AttributeType attributeType = null;
    
    if (_type.equals(AttributeCharacterType.TYPE))
    {
      attributeType = new AttributeCharacterType(_name, _localizedLabel, _localizedDescription);
    }
    else if (_type.equals(AttributeDateType.TYPE))
    {
      attributeType = new AttributeDateType(_name, _localizedLabel, _localizedDescription);
    }
    else if (_type.equals(AttributeIntegerType.TYPE))
    {
      attributeType = new AttributeIntegerType(_name, _localizedLabel, _localizedDescription);
    }
    else if (_type.equals(AttributeTermType.TYPE))
    {
      attributeType = new AttributeTermType(_name, _localizedLabel, _localizedDescription);
    }
    
    return attributeType;
  }

  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.addProperty(JSON_NAME, this.getName());
    
    json.addProperty(JSON_TYPE, this.getType());
    
    json.addProperty(JSON_LOCALIZED_LABEL, this.getLocalizedLabel());
    
    json.addProperty(JSON_LOCALIZED_DESCRIPTION, this.getLocalizedDescription());
    
    return json;
  }
}
