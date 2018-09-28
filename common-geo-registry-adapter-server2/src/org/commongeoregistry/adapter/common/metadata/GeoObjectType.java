package org.commongeoregistry.adapter.common.metadata;

import java.util.HashMap;
import java.util.Map;

public class GeoObjectType
{
  private String code;
  private String localizedLabel;
  private String localizedDescription;
  private Map<String, AttributeType> attributeMap;
  
  protected GeoObjectType(String _code, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    
    this.attributeMap = new HashMap<String, AttributeType>();
  }
  
}
