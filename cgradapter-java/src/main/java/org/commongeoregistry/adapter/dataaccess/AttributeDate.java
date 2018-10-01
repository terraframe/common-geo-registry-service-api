package org.commongeoregistry.adapter.dataaccess;

import java.util.Date;

import org.commongeoregistry.adapter.metadata.AttributeDateType;

public class AttributeDate extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 5532076653984789765L;
  
  private Date date;
  
  public AttributeDate(String _name)
  {
    super(_name, AttributeDateType.TYPE);
    
    this.date = null;
  }
  
  @Override
  public void setValue(Object _date)
  {
    this.setDate((Date)_date);
  }
  

  public void setDate(Date _date)
  {
    this.date = _date;
  }
  
  @Override
  public Date getValue()
  {
    return this.date;
  }


}
