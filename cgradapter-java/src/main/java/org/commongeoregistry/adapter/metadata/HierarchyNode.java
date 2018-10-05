package org.commongeoregistry.adapter.metadata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HierarchyNode
{
  private GeoObjectType geoObjectType;
  
  private List<HierarchyNode> children;
  
  public HierarchyNode(GeoObjectType _geoObjectType)
  {
    this.geoObjectType = _geoObjectType;
    
    this.children = Collections.synchronizedList(new LinkedList<HierarchyNode>());
  }
  
  public void addChild(HierarchyNode child)
  {
    children.add(child);
  }
  
  public GeoObjectType getGeoObjectType() {
    return geoObjectType;
  }

  public List<HierarchyNode> getChildren() {
    return children;
  }
}
