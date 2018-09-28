package org.commongeoregistry.adapter.common.metadata;

public abstract class AttributeType
{
  private String code;

  private String localizedLabel;

  private String localizedDescription;

  private String type;                // Attribute Type Constant

  public AttributeType(String _code, String _localizedLabel, String _localizedDescription, String _type)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    this.type = _type;
  }

  public String getName()
  {
    return this.code;
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
