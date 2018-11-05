package org.commongeoregistry.adapter.metadata;

public class AttributeBooleanType extends AttributeType
{
  /**
   * 
   */
  private static final long serialVersionUID = -6889939609956215822L;
  
  public static String TYPE = "boolean";
  
  public AttributeBooleanType(String _name, String _localizedLabel, String _localizedDescription)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE);
  }
}
