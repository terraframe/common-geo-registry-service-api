package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.Map;

import org.commongeoregistry.adapter.RegistryInterface;
import org.commongeoregistry.adapter.constants.GeometryType;
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
  private static final long      serialVersionUID = 7686140708200106783L;

  private GeoObjectType          geoObjectType;

  private GeometryType           geometryType;

  private Geometry               geometry;

  private Map<String, Attribute> attributeMap;

  public GeoObject(GeoObjectType _geoObjectType, GeometryType _geometryType,
      Map<String, Attribute> _attributeMap)
  {
    this.geoObjectType = _geoObjectType;

    this.geometryType = _geometryType;

    this.geometry = null;

    this.attributeMap = _attributeMap;
  }

  public GeoObjectType getType()
  {
    return this.geoObjectType;
  }

  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }

  public Geometry getGeometry()
  {
    return this.geometry;
  }

  public void setGeometry(Geometry geometry)
  {
    this.geometry = geometry;
  }
  
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

  public Object getValue(String attributeName)
  {
    return this.attributeMap.get(attributeName).getValue();
  }

  public Attribute getAttribute(String attributeName)
  {
    return this.attributeMap.get(attributeName);
  }
  
  public void setCode(String code)
  {
    this.attributeMap.get("code").setValue(code);
  }
  
  public String getCode()
  {
    return (String) this.attributeMap.get("code").getValue();
  }
  
  public void setUid(String uid)
  {
    this.attributeMap.get("uid").setValue(uid);
  }
  
  public String getUid()
  {
    return (String) this.attributeMap.get("uid").getValue();
  }
  
  public static GeoObject fromJSON(RegistryInterface registry, String sJson)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    JsonObject oJsonProps = oJson.getAsJsonObject("properties");
    
    GeoObject geoObj = registry.createGeoObject(oJsonProps.get("type").getAsString());
    
    JsonElement oGeom = oJson.get("geometry");
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
        attr.fromJSON(oJsonProps.get(key));
      }
    }
    
    return geoObj;
  }
  
  /**
   * Return the JSON representation of this metadata
   * 
   * @return
   */
  public JsonObject toJSON()
  {
    JsonObject jsonObj = new JsonObject();

    // It's assumed that GeoObjects are simple features rather than
    // FeatureCollections.
    // Spec reference: https://tools.ietf.org/html/rfc7946#section-3.3
    jsonObj.addProperty("type", "Feature");

    if (this.getGeometry() != null)
    {
      GeoJSONWriter gw = new GeoJSONWriter();
      org.wololo.geojson.Geometry gJSON = gw.write(this.getGeometry());
      
      JsonParser parser = new JsonParser();
      JsonObject geomObj = parser.parse(gJSON.toString()).getAsJsonObject();
      
      jsonObj.add("geometry", geomObj);
    }
    
    JsonObject props = new JsonObject();
    for (String key : this.attributeMap.keySet())
    {
      Attribute attr = this.attributeMap.get(key);
      
      attr.toJSON(props);
    }

    jsonObj.add("properties", props);
    

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
