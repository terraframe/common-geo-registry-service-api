package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
  }
  
  // TODO
  /**
   * Return the JSON representation of this metadata
   * 
   * @return
   */
  public String toJSON()
  {
    return new String();
  }
}
