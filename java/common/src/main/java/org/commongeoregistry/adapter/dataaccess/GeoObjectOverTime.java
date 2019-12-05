package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeGeometryType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.DefaultSerializer;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vividsolutions.jts.geom.Geometry;

public class GeoObjectOverTime implements Serializable
{

  private static final long serialVersionUID = 6218261169426542019L;
  
  public static final String     JSON_ATTRIBUTES  = "attributes";
  
  private GeoObjectType          geoObjectType;
  
  /**
   * For attributes that do change over time, they will be stored here.
   */
  private Map<String, ValueOverTimeCollectionDTO> votAttributeMap;
  
  /**
   * Not all attributes are stored with change-over-time properties. You can check the AttributeType
   * to see if the attribute changes over time.
   */
  private Map<String, Attribute> attributeMap;
  
  private AttributeGeometryType geometryAttributeType;
  
  public GeoObjectOverTime(GeoObjectType geoObjectType, Map<String, ValueOverTimeCollectionDTO> votAttributeMap, Map<String, Attribute> attributeMap)
  {
    this.geoObjectType = geoObjectType;
    this.votAttributeMap = votAttributeMap;
    this.attributeMap = attributeMap;
    geometryAttributeType = (AttributeGeometryType) DefaultAttribute.GEOMETRY.createAttributeType();
    
    this.setValue(DefaultAttribute.TYPE.getName(), this.geoObjectType.getCode());
  }
  
  public static Map<String, ValueOverTimeCollectionDTO> buildVotAttributeMap(GeoObjectType geoObjectType)
  {
    Map<String, AttributeType> attributeTypeMap = geoObjectType.getAttributeMap();

    Map<String, ValueOverTimeCollectionDTO> attributeMap = new ConcurrentHashMap<String, ValueOverTimeCollectionDTO>();

    for (AttributeType attributeType : attributeTypeMap.values())
    {
      if (attributeType.isChangeOverTime())
      {
        ValueOverTimeCollectionDTO votc = new ValueOverTimeCollectionDTO(attributeType);
  
        attributeMap.put(attributeType.getName(), votc);
      }
    }
    
    AttributeGeometryType geometry = (AttributeGeometryType) DefaultAttribute.GEOMETRY.createAttributeType();
    ValueOverTimeCollectionDTO votc = new ValueOverTimeCollectionDTO(geometry);
    attributeMap.put(geometry.getName(), votc);

    return attributeMap;
  }
  
  public static Map<String, Attribute> buildAttributeMap(GeoObjectType geoObjectType)
  {
    Map<String, AttributeType> attributeTypeMap = geoObjectType.getAttributeMap();

    Map<String, Attribute> attributeMap = new ConcurrentHashMap<String, Attribute>();

    for (AttributeType attributeType : attributeTypeMap.values())
    {
      if (!attributeType.isChangeOverTime())
      {
        Attribute attribute = Attribute.attributeFactory(attributeType);

        attributeMap.put(attribute.getName(), attribute);
      }
    }

    return attributeMap;
  }
  
  public GeoObjectType getType()
  {
    return this.geoObjectType;
  }
  
  public Attribute getAttribute(String key, Date date)
  {
    return this.votAttributeMap.get(key).getAttribute(date);
  }
  
  public ValueOverTimeCollectionDTO getAllValues(String attributeName)
  {
    return this.votAttributeMap.get(attributeName);
  }
  
  /**
   * Returns the value of the non-change-over-time attribute with the given name.
   * 
   * @pre attribute with the given name is defined on the {@link GeoObjectType}
   *      that defines this {@link GeoObject}.
   * 
   * @param attributeName
   * 
   * @return value of the attribute with the given name.
   */
  public Object getValue(String attributeName)
  {
    return this.attributeMap.get(attributeName).getValue();
  }
  
  /**
   * Returns the value of the change-over-time attribute with the given name.
   * 
   * @pre attribute with the given name is defined on the {@link GeoObjectType}
   *      that defines this {@link GeoObject}.
   * 
   * @param attributeName
   * 
   * @return value of the attribute with the given name.
   */
  public Object getValue(String attributeName, Date date)
  {
    return this.votAttributeMap.get(attributeName).getValue(date);
  }
  
  /**
   * Sets the value of the non-change-over-time attribute.
   * 
   * @param attributeName
   * @param _value
   */
  public void setValue(String attributeName, Object _value)
  {
    Attribute attribute = this.attributeMap.get(attributeName);
    
    Optional<AttributeType> optional = this.getType().getAttribute(attributeName);
    
    if (optional.isPresent())
    {
      optional.get().validate(_value);
    }
    
    attribute.setValue(_value);
  }
  
  /**
   * Sets the value of the change-over-time attribute.
   * 
   * @param attributeName
   * @param _value
   */
  public void setValue(String attributeName, Object _value, Date startDate, Date endDate)
  {
    ValueOverTimeCollectionDTO votc = this.votAttributeMap.get(attributeName);
    
    if (attributeName.equals(DefaultAttribute.GEOMETRY.getName()))
    {
      geometryAttributeType.validate(_value);
    }
    else
    {
      Optional<AttributeType> optional = this.getType().getAttribute(attributeName);
      
      if (optional.isPresent())
      {
        optional.get().validate(_value);
      }
    }
    
    Attribute attribute = votc.getAttribute(startDate);
    
    if (attribute == null)
    {
      ValueOverTimeDTO vot = new ValueOverTimeDTO(startDate, endDate, votc);
      vot.setValue(_value);
      votc.add(vot);
    }
    else
    {
      attribute.setValue(_value);
    }
  }

  /**
   * Returns the geometry of this {@link GeoObject}
   * 
   * @return the geometry of this {@link GeoObject}
   */
  public Geometry getGeometry(Date date)
  {
    return (Geometry) this.getValue(DefaultAttribute.GEOMETRY.getName(), date);
  }

  /**
   * Set the {@link Geometry} on this {@link GeoObject}
   * 
   * @param geometry
   */
  public void setGeometry(Geometry geometry, Date startDate, Date endDate)
  {
    this.setValue(DefaultAttribute.GEOMETRY.getName(), geometry, startDate, endDate);
  }
  
  /**
   * Sets the code of this {@link GeoObject}.
   * 
   * @param code
   */
  public void setCode(String code)
  {
    this.setValue(DefaultAttribute.CODE.getName(), code);
  }

  /**
   * Returns the code id of this {@link GeoObject}
   * 
   * @return the code id of this {@link GeoObject}
   */
  public String getCode()
  {
    return (String) this.getValue(DefaultAttribute.CODE.getName());
  }
  
  /**
   * Sets the display label of this {@link GeoObject}.
   * 
   * @param label
   */
  public void setDisplayLabel(LocalizedValue label, Date startDate, Date endDate)
  {
    this.setValue(DefaultAttribute.DISPLAY_LABEL.getName(), label, startDate, endDate);
  }

  /**
   * Returns the display label of this {@link GeoObject}
   * 
   * @return the display label of this {@link GeoObject}
   */
  public LocalizedValue getDisplayLabel(Date startDate)
  {
    return (LocalizedValue) this.getValue(DefaultAttribute.DISPLAY_LABEL.getName(), startDate);
  }

  /**
   * Sets the UID of this {@link GeoObject}.
   * 
   * @param uid
   */
  public void setUid(String uid)
  {
    this.setValue(DefaultAttribute.UID.getName(), uid);
  }

  /**
   * Returns the UID of this {@link GeoObject}.
   * 
   * @return
   */
  public String getUid()
  {
    return (String) this.getValue(DefaultAttribute.UID.getName());
  }
  
  /**
   * Returns the status code
   * 
   * @return
   */
  public Term getStatus(Date date)
  {
    Term term = null;

    Optional<AttributeType> optionalAttributeType = this.getType().getAttribute(DefaultAttribute.STATUS.getName());

    if (optionalAttributeType.isPresent())
    {
      AttributeTermType attributeTermType = (AttributeTermType) optionalAttributeType.get();

      @SuppressWarnings("unchecked")
      String termCode = ( (Iterator<String>) this.getValue(DefaultAttribute.STATUS.getName(), date) ).next();
      Optional<Term> optionalTerm = attributeTermType.getTermByCode(termCode);

      if (optionalTerm.isPresent())
      {
        term = optionalTerm.get();
      }
    }

    return term;
  }

  public void setStatus(Term status, Date startDate, Date endDate)
  {
    this.setValue(DefaultAttribute.STATUS.getName(), status.getCode(), startDate, endDate);
  }

  public void setStatus(String statusCode, Date startDate, Date endDate)
  {
    this.setValue(DefaultAttribute.STATUS.getName(), statusCode, startDate, endDate);
  }
  
  /**
   * Creates a {@link GeoObjectOverTime} from the given JSON.
   * 
   * @pre assumes the attributes on the JSON are valid attributes defined by the
   *      {@link GeoObjectType}
   * 
   * @param _registry
   * @param _sJson
   * 
   * @return {@link GeoObjectOverTime} from the given JSON.
   */
  public static GeoObjectOverTime fromJSON(RegistryAdapter registry, String sJson)
  {
    JsonParser parser = new JsonParser();

    JsonObject joGO = parser.parse(sJson).getAsJsonObject();
    JsonObject joAttrs = joGO.getAsJsonObject(JSON_ATTRIBUTES);
    
    String type = joAttrs.get(DefaultAttribute.TYPE.getName()).getAsString();

    GeoObjectOverTime geoObj;
    if (joAttrs.has("uid"))
    {
      geoObj = registry.newGeoObjectOverTimeInstance(type, false);
    }
    else
    {
      geoObj = registry.newGeoObjectOverTimeInstance(type, true);
    }

    for (String key : geoObj.votAttributeMap.keySet())
    {
      ValueOverTimeCollectionDTO votc = geoObj.votAttributeMap.get(key);
      votc.clear();

      if (joAttrs.has(key) && !joAttrs.get(key).isJsonNull())
      {
        JsonObject attributeOverTime = joAttrs.get(key).getAsJsonObject();
        
        JsonArray jaValues = attributeOverTime.get("values").getAsJsonArray();
        
        for (int i = 0; i < jaValues.size(); ++i)
        {
          ValueOverTimeDTO vot = ValueOverTimeDTO.fromJSON(jaValues.get(i).toString(), votc, registry);
          
          votc.add(vot);
        }
      }
    }
    
    for (String key : geoObj.attributeMap.keySet())
    {
      Attribute attr = geoObj.attributeMap.get(key);

      if (joAttrs.has(key) && !joAttrs.get(key).isJsonNull())
      {
        attr.fromJSON(joAttrs.get(key), registry);
      }
    }
    
    return geoObj;
  }

  public JsonObject toJSON()
  {
    return toJSON(new DefaultSerializer());
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject jsonObj = new JsonObject();
    
    JsonObject attrs = new JsonObject();
    for (String key : this.votAttributeMap.keySet())
    {
      ValueOverTimeCollectionDTO votc = this.votAttributeMap.get(key);
      AttributeType type = votc.getAttributeType();
      
      JsonObject attributeOverTime = new JsonObject();
      attributeOverTime.addProperty("name", type.getName());
      attributeOverTime.addProperty("type", type.getType());
      
      JsonArray values = new JsonArray();
      
      for (ValueOverTimeDTO vot : votc)
      {
        values.add(vot.toJSON(serializer));
      }
      
      attributeOverTime.add("values", values);
      
      attrs.add(type.getName(), attributeOverTime);
    }
    
    for (String key : this.attributeMap.keySet())
    {
      Attribute attr = this.attributeMap.get(key);
      
      JsonElement value = attr.toJSON(serializer);
      if (!value.isJsonNull())
      {
        attrs.add(attr.getName(), value);
      }
    }

    jsonObj.add(JSON_ATTRIBUTES, attrs);

    return jsonObj;
  }
  
}
