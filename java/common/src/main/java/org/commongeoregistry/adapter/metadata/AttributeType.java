package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;

import org.commongeoregistry.adapter.constants.DefaultAttribute;

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
  
  public static final String JSON_CODE                      = "code";
  
  public static final String JSON_LOCALIZED_LABEL           = "localizedLabel";
  
  public static final String JSON_LOCALIZED_DESCRIPTION     = "localizedDescription";
  
  public static final String JSON_TYPE                      = "type";
  
  public static final String JSON_IS_DEFAULT				= "isDefault";

  private String name;

  private String localizedLabel;

  private String localizedDescription;

  private String type;                // Attribute Type Constant
  
  private boolean isDefault;
  
  //TODO add a boolean for if the attribute is required or not

  public AttributeType(String _name, String _localizedLabel, String _localizedDescription, String _type, boolean _isDefault)
  {
    this.name = _name;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
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
  
  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }
  
  public void setLocalizedLabel(String localizedLabel)
  {
    this.localizedLabel = localizedLabel;
  }
  
  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }
  
  public void setLocalizedDescription(String localizedDescription)
  {
    this.localizedDescription = localizedDescription;
  }
  
  public boolean getIsDefault()
  {
	  return this.isDefault;
  }
  
  public static AttributeType factory(String _name, String _localizedLabel, String _localizedDescription, String _type)
  {
    AttributeType attributeType = null;
    
    boolean _isDefault = false;
    if(DefaultAttribute.UID.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.UID.getIsDefault();
    }
    else if(DefaultAttribute.CODE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.CODE.getIsDefault();
    }
    else if(DefaultAttribute.LOCALIZED_DISPLAY_LABEL.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.LOCALIZED_DISPLAY_LABEL.getIsDefault();
    }
    else if(DefaultAttribute.TYPE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.TYPE.getIsDefault();
    }
    else if(DefaultAttribute.STATUS.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.STATUS.getIsDefault();
    }
    else if(DefaultAttribute.SEQUENCE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.SEQUENCE.getIsDefault();
    }
    else if(DefaultAttribute.CREATE_DATE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.CREATE_DATE.getIsDefault();
    }
    else if(DefaultAttribute.LAST_UPDATE_DATE.getName().equals(_name))
    {
      _isDefault = DefaultAttribute.LAST_UPDATE_DATE.getIsDefault();
    }
    
    
    if (_type.equals(AttributeCharacterType.TYPE))
    {
      attributeType = new AttributeCharacterType(_name, _localizedLabel, _localizedDescription, _isDefault);
    }
    else if (_type.equals(AttributeDateType.TYPE))
    {
      attributeType = new AttributeDateType(_name, _localizedLabel, _localizedDescription, _isDefault);
    }
    else if (_type.equals(AttributeIntegerType.TYPE))
    {
      attributeType = new AttributeIntegerType(_name, _localizedLabel, _localizedDescription, _isDefault);
    }
    else if (_type.equals(AttributeFloatType.TYPE))
    {
      attributeType = new AttributeFloatType(_name, _localizedLabel, _localizedDescription, _isDefault);
    }
    else if (_type.equals(AttributeTermType.TYPE))
    {
      attributeType = new AttributeTermType(_name, _localizedLabel, _localizedDescription, _isDefault);
    }
    else if (_type.equals(AttributeBooleanType.TYPE))
    {
      attributeType = new AttributeBooleanType(_name, _localizedLabel, _localizedDescription, _isDefault);
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
    
    json.addProperty(JSON_LOCALIZED_LABEL, this.getLocalizedLabel());
    
    json.addProperty(JSON_LOCALIZED_DESCRIPTION, this.getLocalizedDescription());
    
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
  public void fromJSON(JsonObject attrObj) {}
}
