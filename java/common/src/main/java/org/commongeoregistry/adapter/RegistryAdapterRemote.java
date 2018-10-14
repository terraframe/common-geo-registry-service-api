package org.commongeoregistry.adapter;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonObject;


/**
 * This class is used by remote systems wishing to interface with the Common Geo-Registry. This will run on the remote system 
 * and will pull over the metadata for each {@link GeoObjectType}.
 * 
 * @author nathan
 *
 */
public class RegistryAdapterRemote extends RegistryAdapter
{
  /**
   * 
   */
  private static final long    serialVersionUID            = -8311449977719450035L;
  
  private static final String  GET_GEO_OBJECT              = "getGeoObject";
  
  private static final String  CREATE_GEO_OBJECT           = "createGeoObject";
  
  private static final String  UPDATE_GEO_OBJECT           = "updateGeoObject"; 
  
  
  
  /** 
   * Relationships.
   */
  private static final String  GET_CHILDREN_GEO_OBJECTS    = "getChildGeoObjects"; 
  
  private static final String  GET_PARENT_GEO_OBJECTS      = "getParentGeoObjects"; 
  
  
  
  /** 
   * Metadata
   */
  private static final String  GET_GEO_OBJECT_UIDS         = "getGeoObjectUids"; 
  
  private static final String  GET_GEO_OBJECT_TYPES        = "getGeoObjectTypes"; 
  
  
 
  // The URL of the Common Geo-Registry
  private String cgrURL;
  
  /**
   * 
   * 
   * @param _cgrURL URL to the ommon geo-registry
   */
  public RegistryAdapterRemote(String _cgrURL)
  {
    this.cgrURL = _cgrURL;
  }
  
  
  /**
   * Clears the metadata cache and populates it with the metadata from the 
   * common geo-registry.
   * 
   */
  public void refreshMetadataCache()
  {
    String opURL = this.cgrURL+"/"+GET_GEO_OBJECT_TYPES;
    
    this.getMetadataCache().clear();
    
    // Make the RESTFul call
    
    // Populate the cache
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
    String opURL = this.cgrURL+"/"+GET_GEO_OBJECT;
    
    // make the restful URL call
    String geoJSON = "";
    
    GeoObject geoObject = GeoObject.fromJSON(this, geoJSON);
    
    return geoObject;
  }
  
  /**
   * Sends the given {@link GeoObject} to the common geo-registry
   * to be created.
   * 
   * @pre the status on the {@link GeoObject} is in the new state.
   * 
   * @param _geoObject
   */
  public void createGeoObject(GeoObject _geoObject)
  {
    String opURL = this.cgrURL+"/"+CREATE_GEO_OBJECT;
    
    JsonObject jsonObject = _geoObject.toJSON();
    
    String geoJSON = jsonObject.toString();
    
    // make the RESTful call.
  }
  
  
  /**
   * Sends the given {@link GeoObject} to the common geo-registry
   * to be updated.
   * 
   * @pre the status on the {@link GeoObject} is NOT in the new state.
   * 
   * @param _geoObject
   */
  public void updateGeoObject(GeoObject _geoObject)
  {
    String opURL = this.cgrURL+"/"+UPDATE_GEO_OBJECT;
    
    JsonObject jsonObject = _geoObject.toJSON();
    
    String geoJSON = jsonObject.toString();
    
    // make the RESTful call.
    
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
    String opURL = this.cgrURL+"/"+GET_CHILDREN_GEO_OBJECTS;
    
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
    String opURL = this.cgrURL+"/"+GET_PARENT_GEO_OBJECTS;
    
    return null;
  }

}
