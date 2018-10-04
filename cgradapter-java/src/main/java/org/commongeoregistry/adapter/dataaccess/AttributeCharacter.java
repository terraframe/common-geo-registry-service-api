package org.commongeoregistry.adapter.dataaccess;

import org.commongeoregistry.adapter.metadata.AttributeCharacterType;

public class AttributeCharacter extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = -506321096607959557L;
  
  private String value;
  
  public AttributeCharacter(String _name)
  {
    super(_name, AttributeCharacterType.TYPE);
    
    this.value = null;
  }
  
  @Override
  public void setValue(Object _value)
  {
    this.setText((String)_value);
  }
  
  public void setText(String _value)
  {
    this.value = _value;
  }
  
  @Override
  public String getValue()
  {
    return this.value;
  }

}
