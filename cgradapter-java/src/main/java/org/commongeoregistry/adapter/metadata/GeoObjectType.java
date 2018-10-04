package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.locationtech.jts.geom.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
  
  public static GeoObjectType fromJSON(String sJson)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    JsonArray oJsonAttrs = oJson.getAsJsonArray("attributes");
    
    String code = oJson.get("code").getAsString();
    String localizedLabel = oJson.get("localizedLabel").getAsString();
    String localizedDescription = oJson.get("localizedDescription").getAsString();
    GeometryType geometryType = GeometryType.valueOf(oJson.get("geometryType").getAsString());
    
    GeoObjectType geoObjType = new GeoObjectType(code, geometryType, localizedLabel, localizedDescription);
    
    for (String key : geoObjType.attributeMap.keySet())
    {
      AttributeType attr = geoObjType.attributeMap.get(key);
      
      for (int i = 0; i < oJsonAttrs.size(); ++i)
      {
        JsonObject joAttr = oJsonAttrs.get(i).getAsJsonObject();
        
        if (attr.getName().equals(joAttr.get("name").getAsString()))
        {
          // TODO : Attribute factory
        }
      }
    }
    
    return geoObjType;
  }
  
  /**
   * Return the JSON representation of this metadata
   * 
   * @return
   */
  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.addProperty("code", code);
    
    json.addProperty("localizedLabel", localizedLabel);
    
    json.addProperty("localizedDescription", localizedDescription);
    
    json.addProperty("geometryType", this.geometryType.name());
    
    JsonArray attrs = new JsonArray();
    
    for (String key : this.attributeMap.keySet())
    {
      AttributeType attrType = this.attributeMap.get(key);
      
      attrs.add(attrType.toJSON());
    }
    
    json.add("attributes", attrs);
    
    return json;
  }
  
}
