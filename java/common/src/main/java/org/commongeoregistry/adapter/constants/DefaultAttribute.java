package org.commongeoregistry.adapter.constants;

import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

public enum DefaultAttribute 
{
  UID("uid",       "UID",    "The internal globally unique identifier ID.", AttributeCharacterType.TYPE),

  CODE("code",     "Code",   "Human readable unique identifier.", AttributeCharacterType.TYPE),
  
  TYPE("type",     "Type",   "The type of the GeoObject.", AttributeCharacterType.TYPE),
  
  STATUS("status", "Status", "The status of the GeoObject", AttributeTermType.TYPE);

  
  private String name;
  
  private String defaultLocalizedLabel;
  
  private String defaultLocalizedDescription;
  
  private String type;

  private DefaultAttribute(String _name, String _defaultLocalizedLabel, String _defaultLocalizedDescription, String _type)
  {
    this.name                        = _name;
    
    this.defaultLocalizedLabel       = _defaultLocalizedLabel;
    
    this.defaultLocalizedDescription = _defaultLocalizedDescription;
    
    this.type                        = _type;
    
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDefaultLocalizedName()
  {
    return this.defaultLocalizedLabel;
  }
  
  public String getDefaultLocalizedDescription()
  {
    return this.defaultLocalizedDescription;
  }
  
  public String getType()
  {
    return this.type;
  }
  
 public AttributeType createAttributeType()
 {
   return AttributeType.factory(this.getName(), this.getDefaultLocalizedName(), this.getDefaultLocalizedDescription(), this.getType());
 }
}
