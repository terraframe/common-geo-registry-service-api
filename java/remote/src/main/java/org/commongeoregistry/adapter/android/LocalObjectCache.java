package org.commongeoregistry.adapter.android;

import java.io.Serializable;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.dataaccess.TreeNode;

import com.google.gson.JsonObject;


/**
 * This is a local persisted cache of {@link GeoObject}s and how they relate to each other through relationships
 * for offline use.
 * 
 * @author nathan
 *
 */
public class LocalObjectCache implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 4759560897184243298L;
  
  public LocalObjectCache()
  {
    
  }

  
  /**
   * Adds the given {@link TreeNode} object to the local cache.
   * 
   * @param treeNode
   */
  public void cache(TreeNode treeNode)
  {
    
  }
  
  /**
   * Add the given {@link GeoObject} to the local cache.
   * 
   * @param geoObject
   */
  public void cache(GeoObject geoObject)
  {
    
  }
  
  /**
   * Returns the GeoObject with the given UID.
   * 
   * @param _uid UID of the GeoObject.
   * 
   * @return GeoObject with the given UID.
   */
  public GeoObject getGeoObject(String _uid)
  {
    return null;
  }
   
  
  /**
   * Returns the {@link GeoObject} with the given UID and its children of the given types.
   * 
   * Shall we include the hierarchy types as a parameter as well?
   * 
   * @param parentUid UID of the parent {@link GeoObject}
   * @param childrenTypes an array of object types.
   * @param recursive true if all recursive children should be fetched, or false if only immediate
   * children should be fetched.
   * 
   * @return {@link ChildTreeNode} containing the {@link GeoObject} with the given UID and its 
   * children of the given types.
   */
  public ChildTreeNode getChildGeoObjects(String parentUid, String[] childrenTypes, Boolean recursive)
  {    
    return null;
  }

  /**
   * Returns the {@link GeoObject} with the given UID and its parent of the given types.
   * 
   * Shall we include the hierarchy types as a parameter as well?
   * 
   * @param childUid UID of the child {@link GeoObject}
   * @param parentTypes an array of object types.
   * @param recursive true if all recursive parents should be fetched, or false if only immediate
   * recursive should be fetched.
   * 
   * @return {@link ParentTreeNode} containing the {@link GeoObject} with the given UID and its 
   * children of the given types.
   */
  public ParentTreeNode getParentGeoObjects(String childUid, String[] parentTypes, Boolean recursive)
  { 
    return null;
  }
  
}
