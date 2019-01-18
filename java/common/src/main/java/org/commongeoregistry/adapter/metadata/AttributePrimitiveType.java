package org.commongeoregistry.adapter.metadata;

public abstract class AttributePrimitiveType extends AttributeType
{
  /**
   * 
   */
  private static final long serialVersionUID = 7553432124777528154L;

  public AttributePrimitiveType(String _name, String _localizedLabel, String _localizedDescription, String _type, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, _type, _isDefault);
  }
}
