package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.google.gson.JsonObject;

public abstract class TreeNode implements Serializable
{
  public static final String JSON_GEO_OBJECT            = "geoObject";
  
  public static final String JSON_HIERARCHY_TYPE        = "hierarchyType";
  
  /**
   * 
   */
  private static final long serialVersionUID = 6548238839268982437L;

  private GeoObject geoObject;
  
  private HierarchyType hierarchyType;
  
  private List<TreeNode> parents;
  
  public TreeNode(GeoObject _geoObject, HierarchyType _hierarchyType)
  {
    this.geoObject = _geoObject;
    
    this.hierarchyType = _hierarchyType;
    
    this.parents = Collections.synchronizedList(new LinkedList<TreeNode>());
  }

  public GeoObject getGeoObject() 
  {
    return this.geoObject;
  }

  public HierarchyType getHierachyType() 
  {
    return this.hierarchyType;
  }
  
  public void addParent(TreeNode parent)
  {
    this.parents.add(parent);
  }

  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.add(JSON_GEO_OBJECT, this.geoObject.toJSON());
    
    json.addProperty(JSON_HIERARCHY_TYPE, this.hierarchyType.getCode());
    
    json = this.relationshipsToJSON(json);
    
//    JsonArray jaParents = new JsonArray();
//    for (int i = 0; i < this.parents.size(); ++i)
//    {
//      TreeNode parent = this.parents.get(i);
//      
//      jaParents.add(parent.toJSON());
//    }
//    json.add("parents", jaParents);
    
    return json;
  }
  
  /**
   * Returns the relationships of the {@link TreeNode}.
   * 
   * @param _json the JSON being constructed.
   * 
   * @return JSON being constructed
   */
  protected abstract JsonObject relationshipsToJSON(JsonObject _json);
  

//  public static TreeNode fromJSON(String sJson, RegistryAdapterServer registry)
//  {
//    JsonParser parser = new JsonParser();
//    
//    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
//    
//    GeoObject geoObj = GeoObject.fromJSON(registry, oJson.get("geoObject").getAsJsonObject().toString());
//    
//    HierarchyType hierarchyType = registry.getMetadataCache().getHierachyType(oJson.get("hierarchyType").getAsString()).get();
//    
//    TreeNode tn = new TreeNode(geoObj, hierarchyType);
//    
//    JsonArray jaChildren = oJson.get("children").getAsJsonArray();
//    for (int i = 0; i < jaChildren.size(); ++i)
//    {
//      JsonObject joChild = jaChildren.get(i).getAsJsonObject();
//      
//      TreeNode tnChild = TreeNode.fromJSON(joChild.toString(), registry);
//      
//      tn.addChild(tnChild);
//    }
//    
//    JsonArray jaParents = oJson.get("parents").getAsJsonArray();
//    for (int i = 0; i < jaParents.size(); ++i)
//    {
//      JsonObject joParent = jaParents.get(i).getAsJsonObject();
//      
//      TreeNode tnParent = TreeNode.fromJSON(joParent.toString(), registry);
//      
//      tn.addParent(tnParent);
//    }
//    
//    return tn;
//  }

}
