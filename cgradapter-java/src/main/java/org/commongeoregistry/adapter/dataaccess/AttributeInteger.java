package org.commongeoregistry.adapter.dataaccess;

import java.util.Date;

import org.commongeoregistry.adapter.metadata.AttributeIntegerType;

public class AttributeInteger extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = -2116815892488790274L;
  
  private Integer integer;
  
  public AttributeInteger(String _name)
  {
    super(_name, AttributeIntegerType.TYPE);
    
    this.integer = null;
  }
  
  @Override
  public void setValue(Object _integer)
  {
    this.setInteger((Integer)_integer);
  }
  
  public void setInteger(Integer _integer)
  {
    this.integer = _integer;
  }
  
  @Override
  public Integer getValue()
  {
    return this.integer;
  }

}
