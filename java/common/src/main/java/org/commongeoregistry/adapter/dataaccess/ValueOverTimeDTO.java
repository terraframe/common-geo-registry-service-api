package org.commongeoregistry.adapter.dataaccess;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.DefaultSerializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ValueOverTimeDTO
{
  private Date   startDate;

  private Date   endDate;
  
  private Attribute attribute;

  private ValueOverTimeCollectionDTO collection;
  
  public ValueOverTimeDTO(Date startDate, Date endDate, ValueOverTimeCollectionDTO collection)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    this.collection = collection;
    this.attribute = Attribute.attributeFactory(collection.getAttributeType());
  }
  
  public boolean between(Date date)
  {
    return ( this.startDate.equals(date) || this.startDate.before(date) ) && ( this.endDate.equals(date) || this.endDate.after(date) );
  }
  
  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject ret = new JsonObject();
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(TimeZone.getTimeZone("GMT"));
    
    ret.addProperty("startDate", format.format(this.startDate));
    ret.addProperty("endDate", format.format(this.endDate.getTime()));
    
    JsonElement value = this.attribute.toJSON(serializer);
    ret.add("value", value);
    
    return ret;
  }
  
  public static ValueOverTimeDTO fromJSON(String json, ValueOverTimeCollectionDTO collection, RegistryAdapter registry)
  {
    JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(TimeZone.getTimeZone("GMT"));
    try
    {
      Date startDate = format.parse(jo.get("startDate").getAsString());
      Date endDate = format.parse(jo.get("endDate").getAsString());
      
      ValueOverTimeDTO ret = new ValueOverTimeDTO(startDate, endDate, collection);
      
      ret.attribute.fromJSON(jo.get("value"), registry);
      
      return ret;
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e); // TODO : Error handling
    }
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public Object getValue()
  {
    return this.attribute.getValue();
  }

  public void setValue(Object value)
  {
    this.attribute.setValue(value);
  }

  public Attribute getAttribute()
  {
    return attribute;
  }

  public void setAttribute(Attribute attribute)
  {
    this.attribute = attribute;
  }
  
  @Override
  public String toString()
  {
    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    String endDate = "null";
    if (this.endDate != null)
    {
      endDate = dateFormat.format(this.endDate);
    }

    return "value [" + this.attribute.getValue() + "] from " + dateFormat.format(this.startDate) + " to " + endDate;
  }
  
}
