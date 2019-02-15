package org.commongeoregistry.adapter.metadata;

public abstract class AttributeNumericType extends AttributePrimitiveType
{
  /**
   * 
   */
  private static final long serialVersionUID = 5572144593795191683L;

  public AttributeNumericType(String _name, String _localizedLabel, String _localizedDescription, String _type, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, _type, _isDefault);
  }
}
