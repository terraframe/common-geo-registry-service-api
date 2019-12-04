package org.commongeoregistry.adapter.dataaccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    ret.addProperty("startDate", this.startDate.getTime());
    ret.addProperty("endDate", this.endDate.getTime());
    
    JsonElement value = this.attribute.toJSON(serializer);
    ret.add("value", value);
    
    return ret;
  }
  
  public static ValueOverTimeDTO fromJSON(String json, ValueOverTimeCollectionDTO collection, RegistryAdapter registry)
  {
    JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
    
    Date startDate = new Date(jo.get("startDate").getAsLong());
    Date endDate = new Date(jo.get("endDate").getAsLong());
    
    ValueOverTimeDTO ret = new ValueOverTimeDTO(startDate, endDate, collection);
    
    ret.attribute.fromJSON(jo.get("value"), registry);
    
    return ret;
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
