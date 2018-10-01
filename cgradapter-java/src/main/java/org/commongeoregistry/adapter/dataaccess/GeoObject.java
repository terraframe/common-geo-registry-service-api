package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.Map;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

public class GeoObject implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 7686140708200106783L;

  
  private GeoObjectType geoObjectType;
  
  private GeometryType geometryType;
  
  private String geometry;
  
  private Map<String, Attribute> attributeMap;
  
  
  public GeoObject(GeoObjectType _geoObjectType, GeometryType _geometryType, Map<String, Attribute> _attributeMap)
  {
    this.geoObjectType = _geoObjectType;
    
    this.geometryType = _geometryType;
    
    this.geometry = new String();
    
    this.attributeMap = _attributeMap;
  }
  
  public GeoObjectType getTypeCode()
  {
    return this.geoObjectType;
  }
  
  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }
  
  
  public String getGeometry()
  {
    return this.geometry;
  }
  
  public void setGeometry(String _geometry)
  {
    this.geometry = _geometry;
  }
  
  public Object getValue(String attributeName)
  {
    return this.attributeMap.get(attributeName).getValue();
  }
  
  public Attribute getAttribute(String attributeName)
  {
    return this.attributeMap.get(attributeName);
  }
  
  // TODO
  /**
   * Return the GeoJSON representation of this metadata
   * 
   * @return
   */
  public String toGeoJSON()
  {
    return new String();
  }
  
  public void printAttributes()
  {
    for (Attribute attribute : attributeMap.values())
    {
      System.out.println(attribute.toString());
    }
    
    System.out.println("Geometry: "+this.geometry);
  }
}
