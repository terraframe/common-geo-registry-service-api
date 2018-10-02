package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;

public class GeoObjectType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID          = 2857923921744440744L;

  private String                     code;
  
  private GeometryType               geometryType;

  private String                     localizedLabel;

  private String                     localizedDescription;
  
  private Map<String, AttributeType> attributeMap;

  public GeoObjectType(String _code, GeometryType _geometryType, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    
    this.geometryType = _geometryType;

    this.attributeMap = buildDefaultAttributes();
  }

  public String getCode()
  {
    return this.code;
  }

  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }
  
  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }

  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }

  public Optional<AttributeType> getAttribute(String name)
  {
    return Optional.of(this.attributeMap.get(name));
  }
  
  public void addAttribute(AttributeType attributeType)
  {
    this.attributeMap.put(attributeType.getName(), attributeType);
  }
  
  public Map<String, AttributeType> getAttributeMap()
  {
    return this.attributeMap;
  }
  
  /**
   * All {@link GeoObjectType}s have a standard set of attributes
   * @return
   */
  private Map<String, AttributeType> buildDefaultAttributes()
  {
    Map<String, AttributeType> defaultAttributeMap = new ConcurrentHashMap<String, AttributeType>();
        
    AttributeCharacterType uid = (AttributeCharacterType)DefaultAttribute.UID.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.UID.getName(), uid);
    
    AttributeCharacterType code = (AttributeCharacterType)DefaultAttribute.CODE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.CODE.getName(), code);
    
    AttributeCharacterType type = (AttributeCharacterType)DefaultAttribute.TYPE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.TYPE.getName(), type);
    
    AttributeTermType status = (AttributeTermType)DefaultAttribute.STATUS.createAttributeType();
    
    Term rootStatusTerm = DefaultTerms.buildGeoObjectStatusTree();
    
    status.setRootTerm(rootStatusTerm);
    
    defaultAttributeMap.put(DefaultAttribute.STATUS.getName(), status);
    
    return defaultAttributeMap;
  }
  
  /**
   * Return the JSON representation of this metadata
   * 
   * @return
   */
  public JsonObjectBuilder toJSON()
  {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    
    builder.add("code", code);
    
    builder.add("localizedLabel", localizedLabel);
    
    builder.add("localizedDescription", localizedDescription);
    
    JsonArrayBuilder attrs = Json.createArrayBuilder();
    
    for (String key : this.attributeMap.keySet())
    {
      AttributeType attrType = this.attributeMap.get(key);
      
      attrs.add(attrType.toJSON());
    }
    
    builder.add("attributes", attrs);
    
    return builder;
  }
  
}
