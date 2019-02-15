package org.commongeoregistry.adapter.metadata;

public class AttributeIntegerType extends AttributeNumericType
{
  /**
   * 
   */
  private static final long serialVersionUID = -8395438752409839660L;
  
  public static String TYPE = "integer";
  
  public AttributeIntegerType(String _name, String _localizedLabel, String _localizedDescription, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE, _isDefault);
  }
}
