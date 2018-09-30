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

  protected HierarchyType(String _code, String _localizedLabel, String _localizedDescription)
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

  public class HierarchyNode
  {
    private GeoObjectType       geoObjectType;

    private List<GeoObjectType> children;

    public HierarchyNode(GeoObjectType _geoObjectType)
    {
      this.geoObjectType = _geoObjectType;
      this.children = Collections.synchronizedList(new LinkedList<GeoObjectType>());
    }

    public GeoObjectType getGeoObjectType()
    {
      return this.geoObjectType;
    }

    public List<GeoObjectType> getChildren()
    {
      return this.children;
    }
  }
}
