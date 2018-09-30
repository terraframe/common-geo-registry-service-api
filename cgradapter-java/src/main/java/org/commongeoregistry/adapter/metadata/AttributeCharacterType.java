package org.commongeoregistry.adapter.metadata;

public class AttributeCharacterType extends AttributePrimitiveType
{
  public static String TYPE = "character";
  
  public AttributeCharacterType(String _name, String _localizedLabel, String _localizedDescription)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE);
  }
}
