package org.commongeoregistry.adapter.metadata;

import java.util.Collection;

import com.google.gson.JsonObject;

public interface CustomSerializer
{
  public Collection<AttributeType> attributes(GeoObjectType type);

  public void configure(AttributeType attributeType, JsonObject json);
}
