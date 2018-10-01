package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttributes;
import org.commongeoregistry.adapter.constants.DefaultTerms;

public class GeoObjectType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID          = 2857923921744440744L;

  private String                     code;

  private String                     localizedLabel;

  private String                     localizedDescription;
  
  private Map<String, AttributeType> attributeMap;

  public GeoObjectType(String _code, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;

    this.attributeMap = buildDefaultAttributes();
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

  public Optional<AttributeType> getAttribute(String name)
  {
    return Optional.of(this.attributeMap.get(name));
  }
  
  public void addAttribute(AttributeType attributeType)
  {
    this.attributeMap.put(attributeType.getName(), attributeType);
  }
  
  /**
   * All {@link GeoObjectType}s have a standard set of attributes
   * @return
   */
  private Map<String, AttributeType> buildDefaultAttributes()
  {
    Map<String, AttributeType> defaultAttributeMap = new ConcurrentHashMap<String, AttributeType>();
        
    AttributeCharacterType uid = (AttributeCharacterType)DefaultAttributes.UID.createAttributeType();
    defaultAttributeMap.put(DefaultAttributes.UID.getName(), uid);
    
    AttributeCharacterType code = (AttributeCharacterType)DefaultAttributes.CODE.createAttributeType();
    defaultAttributeMap.put(DefaultAttributes.CODE.getName(), code);
    
    AttributeCharacterType type = (AttributeCharacterType)DefaultAttributes.TYPE.createAttributeType();
    defaultAttributeMap.put(DefaultAttributes.TYPE.getName(), type);
    
    AttributeTermType status = (AttributeTermType)DefaultAttributes.STATUS.createAttributeType();
    
    Term rootStatusTerm = DefaultTerms.buildGeoObjectStatusTree();
    
    status.setRootTerm(rootStatusTerm);
    
    defaultAttributeMap.put(DefaultAttributes.STATUS.getName(), status);
    
    return defaultAttributeMap;
    
  }
}
