package org.commongeoregistry.adapter.dataaccess;

import java.util.LinkedList;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.CustomSerializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class ValueOverTimeCollectionDTO
{
  private LinkedList<ValueOverTimeDTO> valuesOverTime;
  
  public ValueOverTimeCollectionDTO()
  {
    this.valuesOverTime = new LinkedList<ValueOverTimeDTO>();
  }
  
  public void add(ValueOverTimeDTO dto)
  {
    this.valuesOverTime.add(dto);
  }
  
  public ValueOverTimeDTO get(int i)
  {
    return this.valuesOverTime.get(i);
  }
  
  public int size()
  {
    return this.valuesOverTime.size();
  }
  
  public JsonArray toJSON(CustomSerializer serializer)
  {
    JsonArray ret = new JsonArray();
    
    for (ValueOverTimeDTO vot : this.valuesOverTime)
    {
      ret.add(vot.toJSON(serializer));
    }
    
    return ret;
  }
  
  public static ValueOverTimeCollectionDTO fromJSON(String json, AttributeType type, RegistryAdapter adapter)
  {
    ValueOverTimeCollectionDTO ret = new ValueOverTimeCollectionDTO();
    JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
    
    for (int i = 0; i < ja.size(); ++i)
    {
      ValueOverTimeDTO vot = ValueOverTimeDTO.fromJSON(ja.get(i).toString(), type, adapter);
      
      ret.add(vot);
    }
    
    return ret;
  }
}
