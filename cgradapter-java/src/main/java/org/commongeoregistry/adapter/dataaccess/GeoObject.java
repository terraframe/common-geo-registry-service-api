package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.Map;

import org.commongeoregistry.adapter.constants.GeometryType;

public class GeoObject implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7686140708200106783L;

  
  private String typeCode;
  
  private GeometryType geometryType;
  
  private String geoJSON;
  
  private Map<String, Attribute> attributeMap;
  
  
  public GeoObject(String _typeCode, GeometryType _geometryType, Map<String, Attribute> _attributeMap)
  {
    this.typeCode = _typeCode;
    
    this.geometryType = _geometryType;
    
    this.geoJSON = new String();
    
    this.attributeMap = _attributeMap;
  }
  
  public String getTypeCode()
  {
    return this.typeCode;
  }
  
  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }
  
  
  public String getGeoJSON()
  {
    return this.geoJSON;
  }
  
  public void setGeoJSON(String _geoJSON)
  {
    this.geoJSON = _geoJSON;
  }
  
  public Object getValue(String attributeName)
  {
    return this.attributeMap.get(attributeName).getValue();
  }
  
  public Attribute getAttribute(String attributeName)
  {
    return this.attributeMap.get(attributeName);
  }
  
  public void printAttributes()
  {
    for (Attribute attribute : attributeMap.values())
    {
      System.out.println(attribute.toString());
    }
    
    System.out.println("GeoJSON: "+this.geoJSON);
  }
}
