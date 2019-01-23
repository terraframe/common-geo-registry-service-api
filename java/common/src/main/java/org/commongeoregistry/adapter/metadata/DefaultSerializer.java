package org.commongeoregistry.adapter.metadata;

import java.util.Collection;

import com.google.gson.JsonObject;

public class DefaultSerializer implements CustomSerializer
{

  @Override
  public Collection<AttributeType> attributes(GeoObjectType type)
  {
    return type.getAttributeMap().values();
  }

  @Override
  public void configure(AttributeType attributeType, JsonObject json)
  {
    // Do nothing
  }

}
