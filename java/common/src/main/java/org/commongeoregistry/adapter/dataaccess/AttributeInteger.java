package org.commongeoregistry.adapter.dataaccess;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeInteger extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = -2116815892488790274L;
  
  private Integer integer;
  
  public AttributeInteger(String name)
  {
    super(name, AttributeIntegerType.TYPE);
    
    this.integer = null;
  }
  
  @Override
  public void setValue(Object integer)
  {
    this.setInteger((Integer)integer);
  }
  
  public void setInteger(Integer integer)
  {
    this.integer = integer;
  }
  
  @Override
  public Integer getValue()
  {
    return this.integer;
  }
  
  public void toJSON(JsonObject geoObjProps)
  {
    geoObjProps.addProperty(this.getName(), this.integer);
  }
  
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    this.setValue(jValue.getAsInt());
  }

}
