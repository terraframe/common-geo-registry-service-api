package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.RegistryAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Metadata that describes a hierarchy type, such as Geopolitical or health administrative.
 *
 */
public class HierarchyType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID               = -1947163248569170534L;
  
  public static final String         JSON_CODE                      = "code";
  
  public static final String         JSON_LOCALIZED_LABEL           = "localizedLabel";
  
  public static final String         JSON_LOCALIZED_DESCRIPTION     = "localizedDescription";
  
  public static final String         JSON_ROOT_GEOOBJECTTYPES       = "rootGeoObjectTypes";
  
  public static final String         JSON_GEOOBJECTTYPE             = "geoObjectType";
  
  public static final String         JSON_CHILDREN                  = "children";
  
  /**
   * Unique identifier but also human readable.
   */
  private String code;

  /**
   * The localized label of the hierarchy type for the presentation tier.
   */
  private String localizedLabel;

  /**
   * The localized description of the hierarchy type for the presentation tier.
   */
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

  public void setLocalizedLabel(String localizedLabel)
  {
    this.localizedLabel = localizedLabel;
  }
  
  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }
  
  public void setLocalizedDescription(String localizedDescription)
  {
    this.localizedDescription = localizedDescription;
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
  
  /**
   * Represents a node in a {@link HierarchyType} where the node value is a {@link GeoObjectType} and
   * the children are also {@link GeoObjectType}s in the {@link HierarchyType}.
   * 
   * @author nathan
   *
   */
  public static class HierarchyNode
  {
    /**
     * {@link GeoObjectType} in the hierarchies node.
     */
    private GeoObjectType       geoObjectType;

    /**
     * Children {@link GeoObjectType}s in the hierarchy.
     */
    private List<HierarchyNode> children;

    /**
     * 
     * @param _geoObjectType {@link GeoObjectType} in the hierarchies node.
     */
    public HierarchyNode(GeoObjectType _geoObjectType)
    {
      this.geoObjectType = _geoObjectType;
      this.children = Collections.synchronizedList(new LinkedList<HierarchyNode>());
    }

    /**
     * Returns the {@link GeoObjectType} defined on this node in the hierarchy.
     * 
     * @return the {@link GeoObjectType} defined on this node in the hierarchy.
     */
    public GeoObjectType getGeoObjectType()
    {
      return this.geoObjectType;
    }

    /**
     * Add the given child {@link GeoObjectType} to this node in the hierarchy.
     * 
     * @param _child Child {@link GeoObjectType} to add to the hierarchy. 
     */
    public void addChild(HierarchyNode _hierarchyNode)
    {
      this.children.add(_hierarchyNode);
    }
    
    /**
     * Returns the child nodes of this current node.
     * 
     * @return child nodes of this current node.
     */
    public List<HierarchyNode> getChildren()
    {
      return this.children;
    }
    
    /**
     * Generates JSON for this object.
     * 
     * @return JSON representation of this object.
     */
    public JsonObject toJSON()
    {
      JsonObject jsonObj = new JsonObject();

      jsonObj.addProperty(JSON_GEOOBJECTTYPE, geoObjectType.getCode());
      
      JsonArray jaChildren = new JsonArray();
      for (int i = 0; i < children.size(); ++i)
      {
        HierarchyNode hnode = children.get(i);
        
        jaChildren.add(hnode.toJSON());
      }
      jsonObj.add(JSON_CHILDREN, jaChildren);
      
      return jsonObj;
    }
    
    
    /**
     * Generates JSON for the Hierarchy Node.
     * 
     * @param sJson
     * @param registry
     * @return JSON for the Hierarchy Node.
     */
    protected static HierarchyNode fromJSON(String sJson, RegistryAdapter registry)
    {
      JsonParser parser = new JsonParser();
      
      JsonObject oJson = parser.parse(sJson).getAsJsonObject();
      
      GeoObjectType got = registry.getMetadataCache().getGeoObjectType(oJson.get(JSON_GEOOBJECTTYPE).getAsString()).get();
      
      HierarchyNode node = new HierarchyNode(got);
      
      JsonArray jaChildren = oJson.getAsJsonArray(JSON_CHILDREN);
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

    jsonObj.addProperty(JSON_CODE, this.getCode());

    jsonObj.addProperty(JSON_LOCALIZED_LABEL, this.getLocalizedLabel());
    
    jsonObj.addProperty(JSON_LOCALIZED_DESCRIPTION, this.getLocalizedDescription());
    
    JsonArray jaRoots = new JsonArray();
    for (int i = 0; i < rootGeoObjectTypes.size(); ++i)
    {
      HierarchyNode hnode = rootGeoObjectTypes.get(i);
      
      jaRoots.add(hnode.toJSON());
    }

    jsonObj.add(JSON_ROOT_GEOOBJECTTYPES, jaRoots);
    
    return jsonObj;
  }
  
  
  /**
   * Constructs a {@link HierarchyType} from the given JSON.
   * @param _sJson
   * @param _registry
   * @return
   */
  public static HierarchyType fromJSON(String _sJson, RegistryAdapter _registry)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(_sJson).getAsJsonObject();
    
    String code = oJson.get(JSON_CODE).getAsString();
    String localizedLabel = oJson.get(JSON_LOCALIZED_LABEL).getAsString();
    String localizedDescription = oJson.get(JSON_LOCALIZED_DESCRIPTION).getAsString();
    
    HierarchyType ht = new HierarchyType(code, localizedLabel, localizedDescription);
    
    JsonArray rootGeoObjectTypes = oJson.getAsJsonArray(JSON_ROOT_GEOOBJECTTYPES);
    for (int i = 0; i < rootGeoObjectTypes.size(); ++i)
    {
      HierarchyNode node = HierarchyNode.fromJSON(rootGeoObjectTypes.get(i).getAsJsonObject().toString(), _registry);
      
      ht.addRootGeoObjects(node);
    }
    
    return ht;
  }
}
