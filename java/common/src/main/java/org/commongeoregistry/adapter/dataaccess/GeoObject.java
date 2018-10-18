package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GeoObject implements Serializable
{

  /**
   * 
   */
  private static final long      serialVersionUID          = 7686140708200106783L;
  
  public static final String     UID                       = DefaultAttribute.UID.getName();
  
  public static final String     CODE                      = DefaultAttribute.CODE.getName();
  
  public static final String     LOCALIZED_DISPLAY_LABEL   = DefaultAttribute.LOCALIZED_DISPLAY_LABEL.getName();
  
  public static final String     JSON_PROPERTIES           = "properties";
  
  public static final String     JSON_TYPE                 = "type";
  
  public static final String     JSON_GEOMETRY             = "geometry";
  
  public static final String     JSON_FEATURE              = "Feature";

  private GeoObjectType          geoObjectType;

  private GeometryType           geometryType;

  private Geometry               geometry;

  private Map<String, Attribute> attributeMap;

  /**
   * Use the factory method on the {@link RegistryAdapter} to create new instances of a {@link GeoObject}
   * 
   * @param _geoObjectType
   * @param _geometryType
   * @param _attributeMap
   */
  public GeoObject(GeoObjectType _geoObjectType, GeometryType _geometryType,
      Map<String, Attribute> _attributeMap)
  {
    this.geoObjectType = _geoObjectType;

    this.geometryType = _geometryType;

    this.geometry = null;

    this.attributeMap = _attributeMap;
    
    this.getAttribute(DefaultAttribute.TYPE.getName()).setValue(_geoObjectType.getCode());
  }
  
  /**
   * Returns a map of {@link Attribute} objects for a {@link GeoObject} of the given {@link GeoObjectType}. 
   * 
   * @param geoObjectType
   * 
   * @return map of {@link Attribute} objects for a {@link GeoObject} of the given {@link GeoObjectType}. 
   */
  public static Map<String, Attribute> buildAttributeMap(GeoObjectType geoObjectType)
  {
    Map<String, AttributeType> attributeTypeMap = geoObjectType.getAttributeMap();
    
    Map<String, Attribute> attributeMap = new ConcurrentHashMap<String, Attribute>();
    
    for (AttributeType attributeType : attributeTypeMap.values())
    {
      Attribute attribute = Attribute.attributeFactory(attributeType);
      
      attributeMap.put(attribute.getName(), attribute);
    }
    
    return attributeMap;
  }

  /**
   * Returns a reference to the {@link GeoObjectType} that defines this {@link GeoObject}.
   * 
   * @return a reference to the {@link GeoObjectType} that defines this {@link GeoObject}.
   */
  public GeoObjectType getType()
  {
    return this.geoObjectType;
  }

  /**
   * Returns the type of the geometry of this {@link GeoObject}
   * 
   * @return type of the geometry of this {@link GeoObject}
   */
  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }

  /**
   * Returns the geometry of this {@link GeoObject}
   * 
   * @return the geometry of this {@link GeoObject}
   */
  public Geometry getGeometry()
  {
    return this.geometry;
  }

  /**
   * Set the {@link Geometry} on this {@link GeoObject}
   * 
   * @param geometry
   */
  public void setGeometry(Geometry geometry)
  {
    this.geometry = geometry;
  }
  
  /**
   * Set the WKT geometry on this Geometry Type.
   * 
   * @param wkt
   */
  public void setWKTGeometry(String wkt)
  {
    Geometry wktObj = null;
    WKTReader wktReader = new WKTReader();
    try
    {
      wktObj = wktReader.read(wkt);
    }
    catch (ParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.setGeometry(wktObj);
  }

  /**
   * Returns the value of the attribute with the given name.
   * 
   * @pre attribute with the given name is defined on the {@link GeoObjectType}
   * that defines this {@link GeoObject}.
   * 
   * @param _attributeName
   * 
   * @return  value of the attribute with the given name.
   */
  public Object getValue(String _attributeName)
  {
    return this.attributeMap.get(_attributeName).getValue();
  }
  
  /**
   * Sets the value of the {@link attribute} object with the given name.
   * 
   * @param _attributeName
   * @param _value
   */
  public void setValue(String _attributeName, Object _value)
  {
    this.attributeMap.get(_attributeName).setValue(_value);
  }

  /**
   * Returns the {@link attribute} object with the given name.
   * 
   * @pre attribute with the given name is defined on the {@link GeoObjectType}
   * that defines this {@link GeoObject}.
   * 
   * @param _attributeName
   * 
   * @return
   */
  public Attribute getAttribute(String _attributeName)
  {
    return this.attributeMap.get(_attributeName);
  }
  
  /**
   * Sets the code of this {@link GeoObject}.
   * 
   * @param _code 
   */
  public void setCode(String _code)
  {
    this.attributeMap.get(CODE).setValue(_code);
  }
  
  /**
   * Returns the code id of this {@link GeoObject}
   * 
   * @return the code id of this {@link GeoObject}
   */
  public String getCode()
  {
    return (String) this.attributeMap.get(CODE).getValue();
  }
  
  /**
   * Sets the UID of this {@link GeoObject}.
   * 
   * @param _uid
   */
  public void setUid(String _uid)
  {
    this.attributeMap.get(UID).setValue(_uid);
  }
  
  /**
   * Returns the UID of this {@link GeoObject}.
   * 
   * @return
   */
  public String getUid()
  {
    return (String) this.attributeMap.get(UID).getValue();
  }
  
  /**
   * Returns the localized 
   * 
   * @return
   */
  public String getLocalizedDisplayLabel()
  {
    return (String) this.attributeMap.get(LOCALIZED_DISPLAY_LABEL).getValue();
  }
  
  public void setLocalizedDisplayLabel(String _displayLabel)
  {
    this.attributeMap.get(LOCALIZED_DISPLAY_LABEL).setValue(_displayLabel);
  } 
  
  /**
   * Returns the status 
   * 
   * @return
   */
  public Term getStatus()
  {
    return (Term) this.attributeMap.get(DefaultAttribute.STATUS).getValue();
  }
  
  public void setStatus(Term status)
  {
    this.attributeMap.get(DefaultAttribute.STATUS).setValue(status);
  }
  
  /**
   * Creates a {@link GeoObject} from the given JSON.
   * 
   * @pre assumes the attributes on the JSON are valid attributes defined by the {@link GeoObjectType}
   * 
   * @param _registry
   * @param _sJson
   * 
   * @return {@link GeoObject} from the given JSON.
   */
  public static GeoObject fromJSON(RegistryAdapter _registry, String _sJson)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(_sJson).getAsJsonObject();
    JsonObject oJsonProps = oJson.getAsJsonObject(JSON_PROPERTIES);
    
    GeoObject geoObj = _registry.newGeoObjectInstance(oJsonProps.get(JSON_TYPE).getAsString());
    
    JsonElement oGeom = oJson.get(JSON_GEOMETRY);
    if (oGeom != null)
    {
      GeoJSONReader reader = new GeoJSONReader();
      Geometry jtsGeom = reader.read(oGeom.toString());
      
      geoObj.setGeometry(jtsGeom);
    }
    
    for (String key : geoObj.attributeMap.keySet())
    {
      Attribute attr = geoObj.attributeMap.get(key);
      
      if (oJsonProps.has(key))
      {
        attr.fromJSON(oJsonProps.get(key), _registry);
      }
    }
    
    return geoObj;
  }
  
  /**
   * Return the JSON representation of this [@link GeoObject}
   * 
   * @return JSON representation of this [@link GeoObject}
   */
  public JsonObject toJSON()
  {
    JsonObject jsonObj = new JsonObject();

    // It's assumed that GeoObjects are simple features rather than
    // FeatureCollections.
    // Spec reference: https://tools.ietf.org/html/rfc7946#section-3.3
    jsonObj.addProperty(JSON_TYPE, JSON_FEATURE);

    if (this.getGeometry() != null)
    {
      GeoJSONWriter gw = new GeoJSONWriter();
      org.wololo.geojson.Geometry gJSON = gw.write(this.getGeometry());
      
      JsonParser parser = new JsonParser();
      JsonObject geomObj = parser.parse(gJSON.toString()).getAsJsonObject();
      
      jsonObj.add(JSON_GEOMETRY, geomObj);
    }
    
    JsonObject props = new JsonObject();
    for (String key : this.attributeMap.keySet())
    {
      Attribute attr = this.attributeMap.get(key);
      
      attr.toJSON(props);
      
//      if(attr instanceof AttributeTerm)
//      {
//        attrs.add(key, attr.toJSON());
//      }
//      else
//      {
//        
//        System.out.println(attr.toJSON());
//        
//        // TODO: All these attributes are required by the CGR spec. Adding an
//        // empty string is a temporary step for me to work on another area of 
//        // the adapter. Ensure that Values are always present and handle 
//        // NULLs as errors.
//        if(attr.getValue() == null )
//        {
//          attrs.addProperty(key, ""); 
//        }
//        else
//        {
//          attrs.addProperty(key, attr.getValue().toString() );
//        }
//      }
      
//      JsonParser attrParser = new JsonParser();
//      JsonObject geomObj = attrParser.parse(attr.toJSON().toString()).getAsJsonObject();
      
    }

    jsonObj.add(JSON_PROPERTIES, props);
    

    return jsonObj;
  }

  public void printAttributes()
  {
    for (Attribute attribute : attributeMap.values())
    {
      System.out.println(attribute.toString());
    }

    System.out.println("Geometry: " + this.geometry);
  }
}
