package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Describes a hierarchy type.
 * 
 * @author nathan
 *
 */
public class HierarchyType implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -1947163248569170534L;

  private String code;

  private String localizedLabel;

  private String localizedDescription;
  
  private List<HierarchyNode> rootGeoObjectTypes;

  public HierarchyType(String _code, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    this.rootGeoObjectTypes = Collections.synchronizedList(new LinkedList<HierarchyNode>());
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
  
  public List<HierarchyNode> getRootGeoObjectTypes()
  {
    return this.rootGeoObjectTypes;
  }

  /** 
   * Adds root {@link GeoObjectType} objects to the root of the hierarchy type.
   * 
   * @param hierarchyNode
   */
  public void addRootGeoObjects(HierarchyNode hierarchyNode)
  {
    this.rootGeoObjectTypes.add(hierarchyNode);
  }
  
  
  public static class HierarchyNode
  {
    private GeoObjectType       geoObjectType;

    private List<HierarchyNode> children;

    public HierarchyNode(GeoObjectType _geoObjectType)
    {
      this.geoObjectType = _geoObjectType;
      this.children = Collections.synchronizedList(new LinkedList<HierarchyNode>());
    }

    public GeoObjectType getGeoObjectType()
    {
      return this.geoObjectType;
    }

    public void addChild(HierarchyNode _hierarchyNode)
    {
      this.children.add(_hierarchyNode);
    }
    
    public List<HierarchyNode> getChildren()
    {
      return this.children;
    }

    public JsonObject toJSON()
    {
      JsonObject jsonObj = new JsonObject();

      jsonObj.addProperty("geoObjectType", geoObjectType.getCode());
      
      JsonArray jaChildren = new JsonArray();
      for (int i = 0; i < children.size(); ++i)
      {
        HierarchyNode hnode = children.get(i);
        
        jaChildren.add(hnode.toJSON());
      }
      jsonObj.add("children", jaChildren);
      
      return jsonObj;
    }
  }
  
  /**
   * Return the JSON representation of this metadata
   * 
   * @return
   */
  public JsonObject toJSON()
  {
    JsonObject jsonObj = new JsonObject();

    jsonObj.addProperty("code", code);

    jsonObj.addProperty("localizedLabel", localizedLabel);
    
    jsonObj.addProperty("localizedDescription", localizedDescription);
    
    JsonArray jaRoots = new JsonArray();
    for (int i = 0; i < rootGeoObjectTypes.size(); ++i)
    {
      HierarchyNode hnode = rootGeoObjectTypes.get(i);
      
      jaRoots.add(hnode.toJSON());
    }

    jsonObj.add("rootGeoObjectTypes", jaRoots);
    
    return jsonObj;
  }
}
