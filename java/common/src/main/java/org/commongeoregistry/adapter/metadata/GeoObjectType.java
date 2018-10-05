package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryInterface;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A {@link GeoObjectType} represents the definition of a location type, such as state, county, province, district, village
 * household, or health facility.  A {@link GeoObjectType} specifies the geometry type stored and the feature attributes on
 * the {@link GeoObject}s that are instances of the {@link GeoObjectType}. {@link GeoObjectType} objects are used to define 
 * integrity constraints on the user 
 * 
 * @author nathan
 *
 */
public class GeoObjectType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID               = 2857923921744440744L;

  
  public static final String         JSON_ATTRIBUTES                = "attributes";
  
  public static final String         JSON_CODE                      = "code";
  
  public static final String         JSON_LOCALIZED_LABEL           = "localizedLabel";
  
  public static final String         JSON_LOCALIZED_DESCRIPTION     = "localizedDescription";
  
  public static final String         JSON_GEOMETRY_TYPE             = "geometryType";
 
  
  /**
   * Unique but human readable identifier. It could be VILLAGE or HOUSEHOLD.
   */
  private String                     code;
  
  /**
   * Type of geometry used for instances of this {@link GeoObjectType}, such as point, line, polygon, etc.
   */
  private GeometryType               geometryType;
  

  /**
   * The localized label of this type, such as Village or Household. Used for display in the presentation tier.
   */
  private String                     localizedLabel;
  

  /**
   * The localized description of this type, used for display in the presentation tier.
   */
  private String                     localizedDescription;
  
  /**
   * Collection of {@link AttributeType} metadata attributes.
   * 
   * key: {@code AttributeType#getName()}
   * 
   * value: {@code AttributeType}
   * 
   */
  private Map<String, AttributeType> attributeMap;

  /**
   * 
   * 
   * 
   * @param _code unique identifier that his human readable.
   * @param _geometryType type of geometry for the {@link GeoObjectType} such as point, line, etc.
   * @param _localizedLabel localized label of the {@link GeoObjectType}.
   * @param _localizedDescription localized description of the {@link GeoObjectType}.
   * @param _registry {@link RegistryInterface} from which this {@link GeoObjectType} is defined. 
   */
  public GeoObjectType(String _code, GeometryType _geometryType, String _localizedLabel, String _localizedDescription, RegistryInterface _registry)
  {
    this.init(_code, _geometryType, _localizedLabel, _localizedDescription);

    this.attributeMap = buildDefaultAttributes(_registry);
  }

  /**
   * 
   * 
   * 
   * @param _code unique identifier that his human readable.
   * @param _geometryType type of geometry for the {@link GeoObjectType} such as point, line, etc.
   * @param _localizedLabel localized label of the {@link GeoObjectType}.
   * @param _localizedDescription localized description of the {@link GeoObjectType}.
   * @param _registry {@link RegistryInterface} from which this {@link GeoObjectType} is defined. 
   */
  private GeoObjectType(String _code, GeometryType _geometryType, String _localizedLabel, String _localizedDescription, RegistryInterface _registry, Map<String, AttributeType> _attributeMap)
  {
    this.init(_code, _geometryType, _localizedLabel, _localizedDescription);

    this.attributeMap = _attributeMap;
  }
  

  /**
   * Initializes member variables.
   * 
   * @param _code
   * @param _geometryType
   * @param _localizedLabel
   * @param _localizedDescription
   */
  private void init(String _code, GeometryType _geometryType, String _localizedLabel,
      String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    
    this.geometryType = _geometryType;
  }
  

  /**
   * Returns the unique identifier that his human readable.
   * 
   * @return Code value.
   */
  public String getCode()
  {
    return this.code;
  }

  /**
   * Returns the {@link GeometryType} supported for instances of this {@link GeoObjectType}.
   * 
   * @return {@link GeometryType}.
   */
  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }
  
  /**
   * Returns the localized label of this {@link GeoObjectType} used for the presentation layer.
   * 
   * @return Localized label of this {@link GeoObjectType}.
   */
  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }

  /**
   * Returns the localized description of this {@link GeoObjectType} used for the presentation layer.
   * 
   * @return Localized description of this {@link GeoObjectType}.
   */
  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }

  /**
   * Returns the {@link AttributeType} defined on this {@link GeoObjectType} with the given name.
   * 
   * @param _name Name of the attribute {@code AttributeType#getName()}.
   * 
   * @pre Attribute with the given name is defined on this {@link GeoObjectType}.
   * 
   * @return Name of the attribute.s
   */
  public Optional<AttributeType> getAttribute(String _name)
  {
    return Optional.of(this.attributeMap.get(_name));
  }
  
  /**
   * Adds the given {@link AttributeType} as an attribute defined on this {@link GeoObjectType}s.
   * 
   * @param _attributeType {@link AttributeType} to add to this {@link GeoObjectType}.
   */
  public void addAttribute(AttributeType _attributeType)
  {
    this.attributeMap.put(_attributeType.getName(), _attributeType);
  }
  
  /**
   * Returns the {@link AttributeType} objects of the attributes defined on this @link GeoObjectType}.
   * 
   * @return {@link AttributeType} objects of the attributes defined on this @link GeoObjectType}.
   */
  public Map<String, AttributeType> getAttributeMap()
  {
    return this.attributeMap;
  }
  
  /**
   * Defines the standard set of {@link AttributeType} defined on all{@link GeoObjectType}s.
   * 
   * @return the standard set of {@link AttributeType} defined on all{@link GeoObjectType}s.
   */
  private Map<String, AttributeType> buildDefaultAttributes(RegistryInterface registry)
  {
    Map<String, AttributeType> defaultAttributeMap = new ConcurrentHashMap<String, AttributeType>();
        
    AttributeCharacterType uid = (AttributeCharacterType)DefaultAttribute.UID.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.UID.getName(), uid);
    
    AttributeCharacterType code = (AttributeCharacterType)DefaultAttribute.CODE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.CODE.getName(), code);
    
    AttributeCharacterType type = (AttributeCharacterType)DefaultAttribute.TYPE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.TYPE.getName(), type);
    
    AttributeTermType status = (AttributeTermType)DefaultAttribute.STATUS.createAttributeType();
    Term rootStatusTerm = registry.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.ROOT.code).get();
    status.setRootTerm(rootStatusTerm);
    
    defaultAttributeMap.put(DefaultAttribute.STATUS.getName(), status);
    
    return defaultAttributeMap;
  }
  
  
  /**
   * Creates a {@link GeoObjectType} from the given JSON string.
   * 
   * @param _sJson JSON string that defines the {@link GeoObjectType}.
   * @param _registry {@link RegistryInterface} from which this {@link GeoObjectType} object comes. 
   * @return
   */
  public static GeoObjectType fromJSON(String _sJson, RegistryInterface _registry)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(_sJson).getAsJsonObject();
    JsonArray oJsonAttrs = oJson.getAsJsonArray(JSON_ATTRIBUTES);
    
    String code = oJson.get(JSON_CODE).getAsString();
    String localizedLabel = oJson.get(JSON_LOCALIZED_LABEL).getAsString();
    String localizedDescription = oJson.get(JSON_LOCALIZED_DESCRIPTION).getAsString();
    GeometryType geometryType = GeometryType.valueOf(oJson.get(JSON_GEOMETRY_TYPE).getAsString());
        
    Map<String, AttributeType> attributeMap = new ConcurrentHashMap<String, AttributeType>();
    for (int i = 0; i < oJsonAttrs.size(); ++i)
    {
      JsonObject joAttr = oJsonAttrs.get(i).getAsJsonObject();
      String name = joAttr.get(AttributeType.JSON_NAME).getAsString();
      
      AttributeType attrType = AttributeType.factory(name, joAttr.get(AttributeType.JSON_LOCALIZED_LABEL).getAsString(), joAttr.get(AttributeType.JSON_LOCALIZED_DESCRIPTION).getAsString(), joAttr.get(AttributeType.JSON_TYPE).getAsString());
      attributeMap.put(name, attrType);
    }
    
    // TODO Need to validate that the default attributes are still defined.
    GeoObjectType geoObjType = new GeoObjectType(code, geometryType, localizedLabel, localizedDescription, _registry, attributeMap);
    
    return geoObjType;
  }
  
  /**
   * Return the JSON representation of this {@link GeoObjectType}.
   * 
   * @return JSON representation of this {@link GeoObjectType}.
   */
  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.addProperty(JSON_CODE, this.getCode());
    
    json.addProperty(JSON_LOCALIZED_LABEL, this.getLocalizedLabel());
    
    json.addProperty(JSON_LOCALIZED_DESCRIPTION, this.getLocalizedDescription());
    
    json.addProperty(JSON_GEOMETRY_TYPE, this.geometryType.name()); // TODO: PROPOSED but not yet approved. Required for fromJSON reconstruction.
    
    JsonArray attrs = new JsonArray();
    
    for (String key : this.attributeMap.keySet())
    {
      AttributeType attrType = this.attributeMap.get(key);
      
      attrs.add(attrType.toJSON());
    }
    
    json.add(JSON_ATTRIBUTES, attrs);
    
    return json;
  }
  
}
