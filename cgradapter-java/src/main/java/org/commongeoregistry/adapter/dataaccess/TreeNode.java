package org.commongeoregistry.adapter.dataaccess;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryServerInterface;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TreeNode
{
  private GeoObject geoObject;
  
  private HierarchyType hierarchyType;
  
  private List<TreeNode> children;
  
  private List<TreeNode> parents;
  
  public TreeNode(GeoObject _geoObject, HierarchyType _hierarchyType)
  {
    this.geoObject = _geoObject;
    
    this.hierarchyType = _hierarchyType;
    
    this.children = Collections.synchronizedList(new LinkedList<TreeNode>());
    
    this.parents = Collections.synchronizedList(new LinkedList<TreeNode>());
  }

  public GeoObject getGeoObject() {
    return geoObject;
  }

  public HierarchyType getHierachyType() {
    return hierarchyType;
  }

  public List<TreeNode> getChildren() {
    return children;
  }
  
  public void addChild(TreeNode child)
  {
    this.children.add(child);
  }
  
  public void addParent(TreeNode parent)
  {
    this.parents.add(parent);
  }

  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.add("geoObject", this.geoObject.toJSON());
    
    json.addProperty("hierarchyType", this.hierarchyType.getCode());
    
    JsonArray jaChildren = new JsonArray();
    for (int i = 0; i < this.children.size(); ++i)
    {
      TreeNode child = this.children.get(i);
      
      jaChildren.add(child.toJSON());
    }
    json.add("children", jaChildren);
    
    JsonArray jaParents = new JsonArray();
    for (int i = 0; i < this.parents.size(); ++i)
    {
      TreeNode parent = this.parents.get(i);
      
      jaParents.add(parent.toJSON());
    }
    json.add("parents", jaParents);
    
    return json;
  }

  public static TreeNode fromJSON(String sJson, RegistryServerInterface registry)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    
    GeoObject geoObj = GeoObject.fromJSON(registry, oJson.get("geoObject").getAsJsonObject().toString());
    
    HierarchyType hierarchyType = registry.getMetadataCache().getHierachyType(oJson.get("hierarchyType").getAsString()).get();
    
    TreeNode tn = new TreeNode(geoObj, hierarchyType);
    
    JsonArray jaChildren = oJson.get("children").getAsJsonArray();
    for (int i = 0; i < jaChildren.size(); ++i)
    {
      JsonObject joChild = jaChildren.get(i).getAsJsonObject();
      
      TreeNode tnChild = TreeNode.fromJSON(joChild.toString(), registry);
      
      tn.addChild(tnChild);
    }
    
    JsonArray jaParents = oJson.get("parents").getAsJsonArray();
    for (int i = 0; i < jaParents.size(); ++i)
    {
      JsonObject joParent = jaParents.get(i).getAsJsonObject();
      
      TreeNode tnParent = TreeNode.fromJSON(joParent.toString(), registry);
      
      tn.addParent(tnParent);
    }
    
    return tn;
  }
}
