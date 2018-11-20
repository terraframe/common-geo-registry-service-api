package org.commongeoregistry.adapter.constants;

public class RegistryUrls
{
  public static final String REGISTRY_CONTROLLER_URL  = "cgr";
  
  /**
   * Geo Objects
   */
  
  public static final String GEO_OBJECT_GET           = "geoobject/get";

  public static final String GEO_OBJECT_UPDATE        = "geoobject/update";
  
  public static final String GEO_OBJECT_CREATE        = GEO_OBJECT_UPDATE;
  
  public static final String GEO_OBJECT_GET_CHILDREN = "geoobject/getchildren";

  public static final String GEO_OBJECT_GET_PARENTS   = "geoobject/get-parent-geoobjects";
  
  public static final String GEO_OBJECT_ADD_CHILD     = "geoobject/addchild";

  public static final String GEO_OBJECT_GET_UIDS      = "geoobject/get-uids";
  
  /**
   * Geo Object Types
   */
  
  public static final String GEO_OBJECT_TYPE_GET_ALL  = "geoobjecttype/get-all";
  
  public static final String GEO_OBJECT_TYPE_CREATE   = "geoobjecttype/create";
  
  public static final String GEO_OBJECT_TYPE_UPDATE   = "geoobjecttype/update";
  
  public static final String GEO_OBJECT_TYPE_DELETE   = "geoobjecttype/delete";
  
  /**
   * Hierarchy Types
   */
  
  public static final String HIERARCHY_TYPE_GET_ALL   = "hierarchytype/get-all";
  
  public static final String HIERARCHY_TYPE_CREATE   = "hierarchytype/create";
  
  public static final String HIERARCHY_TYPE_UPDATE   = "hierarchytype/update";
  
  public static final String HIERARCHY_TYPE_DELETE   = "hierarchytype/delete";
  
  public static final String HIERARCHY_TYPE_ADD      = "hierarchytype/add";
  
  public static final String HIERARCHY_TYPE_REMOVE   = "hierarchytype/remove";
  
  /**
   * Miscellaneous
   */
  
  public static final String EXECUTE_ACTIONS         = "executeActions";
}
