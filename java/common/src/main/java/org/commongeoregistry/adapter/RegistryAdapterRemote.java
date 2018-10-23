package org.commongeoregistry.adapter;

import org.apache.commons.httpclient.NameValuePair;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.http.AbstractConnector;
import org.commongeoregistry.adapter.http.HTTPResponse;
import org.commongeoregistry.adapter.http.ResponseProcessor;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * This class is used by remote systems wishing to interface with the Common Geo-Registry. This will run on the remote system 
 * and will pull over the metadata for each {@link GeoObjectType}.
 * 
 * @author nathan
 * @author rrowlands
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
  
  
  private AbstractConnector connector;
  
  /**
   * 
   * 
   * @param _cgrURL URL to the common geo-registry
   */
  public RegistryAdapterRemote(AbstractConnector connector)
  {
    this.connector = connector;
  }
  
  
  /**
   * Clears the metadata cache and populates it with the metadata from the 
   * common geo-registry.
   * 
   */
  public void refreshMetadataCache()
  {
    this.getMetadataCache().rebuild();
    
    HTTPResponse resp = this.connector.httpGet(GET_GEO_OBJECT_TYPES, new NameValuePair[]{});
    ResponseProcessor.validateStatusCode(resp);
    
    JsonArray jArr = resp.getAsJsonArray();
    
    for (int i = 0; i < jArr.size(); ++i)
    {
      JsonObject jObj = jArr.get(i).getAsJsonObject();
      GeoObjectType got = GeoObjectType.fromJSON(jObj.toString(), this);
      this.getMetadataCache().addGeoObjectType(got);
    }
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
    HTTPResponse resp = this.connector.httpGet(GET_GEO_OBJECT, new NameValuePair[]{});
    ResponseProcessor.validateStatusCode(resp);
    
    GeoObject geoObject = GeoObject.fromJSON(this, resp.getAsString());
    
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
    JsonObject jsonObject = _geoObject.toJSON();
    
    String geoJSON = jsonObject.toString();
    
    HTTPResponse resp = this.connector.httpPost(CREATE_GEO_OBJECT, geoJSON);
    ResponseProcessor.validateStatusCode(resp);
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
    JsonObject jsonObject = _geoObject.toJSON();
    
    String geoJSON = jsonObject.toString();
    
    HTTPResponse resp = this.connector.httpPost(UPDATE_GEO_OBJECT, geoJSON);
    ResponseProcessor.validateStatusCode(resp);
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
    HTTPResponse resp = this.connector.httpGet(GET_CHILDREN_GEO_OBJECTS, new NameValuePair[]{});
    ResponseProcessor.validateStatusCode(resp);
    
    ChildTreeNode tn = ChildTreeNode.fromJSON(resp.getAsString(), this);
    
    return tn;
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
    HTTPResponse resp = this.connector.httpGet(GET_PARENT_GEO_OBJECTS, new NameValuePair[]{});
    ResponseProcessor.validateStatusCode(resp);
    
    ParentTreeNode tn = ParentTreeNode.fromJSON(resp.getAsString(), this);
    
    return tn;
  }

}
