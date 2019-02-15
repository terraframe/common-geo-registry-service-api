package org.commongeoregistry.adapter.metadata;

public class AttributeFloatType extends AttributeNumericType
{
  /**
   * 
   */
  private static final long serialVersionUID = -2000724524967535694L;

  public static String TYPE = "float";
  
  public AttributeFloatType(String _name, String _localizedLabel, String _localizedDescription, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE, _isDefault);
  }
}
