package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.constants.DefaultAttributes;

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
  
  /**
   * All {@link GeoObjectType}s have a standard set of attributes
   * @return
   */
  public static Map<String, AttributeType> buildDefaultAttributes()
  {
    Map<String, AttributeType> defaultAttributeMap = new ConcurrentHashMap<String, AttributeType>();
        
    AttributeCharacterType uid = (AttributeCharacterType)DefaultAttributes.UID.createAttributeType();
    defaultAttributeMap.put(DefaultAttributes.UID.getName(), uid);
    
    AttributeCharacterType code = (AttributeCharacterType)DefaultAttributes.CODE.createAttributeType();
    defaultAttributeMap.put(DefaultAttributes.CODE.getName(), code);
    
    AttributeCharacterType type = (AttributeCharacterType)DefaultAttributes.TYPE.createAttributeType();
    defaultAttributeMap.put(DefaultAttributes.TYPE.getName(), type);
    
    AttributeTermType status = (AttributeTermType)DefaultAttributes.STATUS.createAttributeType();
    
    // NEED TO DEFINE THE TERM!
    
    
    defaultAttributeMap.put(DefaultAttributes.STATUS.getName(), status);
    
    
    
    
    return defaultAttributeMap;
    
  }
}
