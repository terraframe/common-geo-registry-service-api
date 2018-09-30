package org.commongeoregistry.adapter.metadata;

public class AttributeIntegerType extends AttributeNumericType
{
  public static String TYPE = "integer";
  
  public AttributeIntegerType(String _name, String _localizedLabel, String _localizedDescription)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE);
  }
}
