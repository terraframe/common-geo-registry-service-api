package org.commongeoregistry.adapter.metadata;

public class AttributeCharacterType extends AttributePrimitiveType
{
  /**
   * 
   */
  private static final long serialVersionUID = -4241500416669749156L;
  
  public static String TYPE = "character";
  
  public AttributeCharacterType(String _name, String _localizedLabel, String _localizedDescription, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE, _isDefault);
  }
}
