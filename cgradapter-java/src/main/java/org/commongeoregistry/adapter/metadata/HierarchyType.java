package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.RegistryInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Describes a hierarchy type.
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
    
    public static HierarchyNode fromJSON(String sJson, RegistryInterface registry)
    {
      JsonParser parser = new JsonParser();
      
      JsonObject oJson = parser.parse(sJson).getAsJsonObject();
      
      GeoObjectType got = registry.getMetadataCache().getGeoObjectType(oJson.get("geoObjectType").getAsString()).get();
      
      HierarchyNode node = new HierarchyNode(got);
      
      JsonArray jaChildren = oJson.getAsJsonArray("children");
      for (int i = 0; i < jaChildren.size(); ++i)
      {
        JsonObject joChild = jaChildren.get(i).getAsJsonObject();
        
        HierarchyNode hnChild = HierarchyNode.fromJSON(joChild.toString(), registry);
        
        node.addChild(hnChild);
      }
      
      return node;
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
  
  public static HierarchyType fromJSON(String sJson, RegistryInterface registry)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    
    String code = oJson.get("code").getAsString();
    String localizedLabel = oJson.get("localizedLabel").getAsString();
    String localizedDescription = oJson.get("localizedDescription").getAsString();
    
    HierarchyType ht = new HierarchyType(code, localizedLabel, localizedDescription);
    
    JsonArray rootGeoObjectTypes = oJson.getAsJsonArray("rootGeoObjectTypes");
    for (int i = 0; i < rootGeoObjectTypes.size(); ++i)
    {
      HierarchyNode node = HierarchyNode.fromJSON(rootGeoObjectTypes.get(i).getAsJsonObject().toString(), registry);
      
      ht.addRootGeoObjects(node);
    }
    
    return ht;
  }
}
