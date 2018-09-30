package com.cgr.adapter.common.metadata;

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
}
