package org.commongeoregistry.adapter.dataaccess;

import java.text.ParseException;
import java.util.Date;

import org.commongeoregistry.adapter.RegistryInterface;
import org.commongeoregistry.adapter.metadata.AttributeDateType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeDate extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 5532076653984789765L;
  
  private Date date;
  
  private static final String FORMAT = "yyyy-MM-dd G HH-mm-ss-SS Z";
  
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
  
  @Override
  public void toJSON(JsonObject geoObjProps)
  {
    String sDate = new java.text.SimpleDateFormat(FORMAT).format(this.date);
    
    geoObjProps.addProperty(this.getName(), sDate);
  }
  
  @Override
  public void fromJSON(JsonElement jValue, RegistryInterface registry)
  {
    try
    {
      this.setValue(new java.text.SimpleDateFormat(FORMAT).parse(jValue.getAsString()));
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
}
