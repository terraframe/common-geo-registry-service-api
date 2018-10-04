package org.commongeoregistry.adapter.dataaccess;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.metadata.HierarchyType;

public class TreeNode
{
  private GeoObject geoObject;
  
  private HierarchyType hierachyType;
  
  private List<TreeNode> children;
  
  public TreeNode(GeoObject _geoObject, HierarchyType _hierarchyType)
  {
    this.geoObject = _geoObject;
    
    this.children = Collections.synchronizedList(new LinkedList<TreeNode>());
  }

  public GeoObject getGeoObject() {
    return geoObject;
  }

  public HierarchyType getHierachyType() {
    return hierachyType;
  }

  public List<TreeNode> getChildren() {
    return children;
  }
}
