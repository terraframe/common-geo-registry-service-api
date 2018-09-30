package org.commongeoregistry.adapter.metadata;

public abstract class AttributeType
{
  private String name;

  private String localizedLabel;

  private String localizedDescription;

  private String type;                // Attribute Type Constant

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
  
  public String localizedDescription()
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
}
