package com.cgr.adapter.common.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GeoObjectType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID = 2857923921744440744L;

  private String                     code;

  private String                     localizedLabel;

  private String                     localizedDescription;

  private Map<String, AttributeType> attributeMap;

  protected GeoObjectType(String _code, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;

    this.attributeMap = new ConcurrentHashMap<String, AttributeType>();
  }

  public String getCode()
  {
    return this.code;
  }

  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }

  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }

  public Optional<AttributeType> getAttributeType(String name)
  {
    return Optional.of(this.attributeMap.get(name));
  }
}
