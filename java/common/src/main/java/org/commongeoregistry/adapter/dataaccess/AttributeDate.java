package org.commongeoregistry.adapter.dataaccess;

import java.text.ParseException;
import java.util.Date;

import org.commongeoregistry.adapter.RegistryAdapter;
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
  
  public AttributeDate(String name)
  {
    super(name, AttributeDateType.TYPE);
    
    this.date = null;
  }
  
  @Override
  public void setValue(Object date)
  {
    this.setDate((Date)date);
  }
  

  public void setDate(Date date)
  {
    this.date = date;
  }
  
  @Override
  public Date getValue()
  {
    return this.date;
  }
  
  @Override
  public void toJSON(JsonObject geoObjProps)
  {
    if (this.date != null)
    {
      String sDate = new java.text.SimpleDateFormat(FORMAT).format(this.date);
      
      geoObjProps.addProperty(this.getName(), sDate);
    }
  }
  
  @Override
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
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
