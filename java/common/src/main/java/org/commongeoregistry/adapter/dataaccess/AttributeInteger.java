package org.commongeoregistry.adapter.dataaccess;

import org.commongeoregistry.adapter.RegistryInterface;
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
  
  public void toJSON(JsonObject geoObjProps)
  {
    geoObjProps.addProperty(this.getName(), this.integer);
  }
  
  public void fromJSON(JsonElement jValue, RegistryInterface registry)
  {
    this.setValue(jValue.getAsInt());
  }

}
