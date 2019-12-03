package org.commongeoregistry.adapter.dataaccess;

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

  private Object value;
  
  private AttributeType type;
  
  public ValueOverTimeDTO(Date startDate, Date endDate, Object value, AttributeType type)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    this.value = value;
    this.type = type;
  }
  
  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject ret = new JsonObject();
    
    ret.addProperty("startDate", this.startDate.getTime());
    ret.addProperty("endDate", this.endDate.getTime());
    
    Attribute attr = Attribute.attributeFactory(this.type);
    JsonElement value = attr.toJSON(serializer);
    ret.add("value", value);
    
    return ret;
  }
  
  public static ValueOverTimeDTO fromJSON(String json, AttributeType type, RegistryAdapter registry)
  {
    JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
    
    Date startDate = new Date(jo.get("startDate").getAsLong());
    Date endDate = new Date(jo.get("endDate").getAsLong());
    
    Attribute attr = Attribute.attributeFactory(type);
    attr.fromJSON(jo.get("value"), registry);
    
    ValueOverTimeDTO ret = new ValueOverTimeDTO(startDate, endDate, attr.getValue(), type);
    
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
    return value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }
  
}
