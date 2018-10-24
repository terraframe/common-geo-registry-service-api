package org.commongeoregistry.adapter;

import java.util.HashMap;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.http.Connector;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseProcessor;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This class is used by remote systems wishing to interface with the Common
 * Geo-Registry. This will run on the remote system and will pull over the
 * metadata for each {@link GeoObjectType}.
 * 
 * @author nathan
 * @author rrowlands
 *
 */
public class HttpRegistryClient extends RegistryAdapter
{
  /**
   * 
   */
  private static final long serialVersionUID         = -8311449977719450035L;

  static final String       GET_GEO_OBJECT           = "getGeoObject";

  static final String       CREATE_GEO_OBJECT        = "createGeoObject";

  static final String       UPDATE_GEO_OBJECT        = "updateGeoObject";

  /**
   * Relationships.
   */
  static final String       GET_CHILDREN_GEO_OBJECTS = "getChildGeoObjects";

  static final String       GET_PARENT_GEO_OBJECTS   = "getParentGeoObjects";

  /**
   * Metadata
   */
  static final String       GET_GEO_OBJECT_UIDS      = "getGeoObjectUids";

  static final String       GET_GEO_OBJECT_TYPES     = "getGeoObjectTypes";

  private Connector         connector;

  /**
   * 
   * 
   * @param _cgrURL
   *          URL to the common geo-registry
   */
  public HttpRegistryClient(Connector connector)
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

    HttpResponse resp = this.connector.httpGet(GET_GEO_OBJECT_TYPES, new HashMap<String, String>());
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
   * @param _uid
   *          UID of the GeoObject.
   * 
   * @return GeoObject with the given UID.
   */
  public GeoObject getGeoObject(String _uid)
  {
    if (_uid == null)
    {
      throw new RequiredParameterException("Method [getGeoObject] requires a parameter value for the parameter named [uid]", "uid");
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("uid", _uid);

    HttpResponse resp = this.connector.httpGet(GET_GEO_OBJECT, params);
    ResponseProcessor.validateStatusCode(resp);

    GeoObject geoObject = GeoObject.fromJSON(this, resp.getAsString());

    return geoObject;
  }

  /**
   * Sends the given {@link GeoObject} to the common geo-registry to be created.
   * 
   * @pre the status on the {@link GeoObject} is in the new state.
   * 
   * @param _geoObject
   */
  public void createGeoObject(GeoObject _geoObject)
  {
    if (_geoObject == null)
    {
      throw new RequiredParameterException("Method [createGeoObject] requires a parameter value for the parameter named [geoObject]", "geoObject");
    }

    JsonObject jsonObject = _geoObject.toJSON();

    String geoJSON = jsonObject.toString();

    HttpResponse resp = this.connector.httpPost(CREATE_GEO_OBJECT, geoJSON);
    ResponseProcessor.validateStatusCode(resp);
  }

  /**
   * Sends the given {@link GeoObject} to the common geo-registry to be updated.
   * 
   * @pre the status on the {@link GeoObject} is NOT in the new state.
   * 
   * @param _geoObject
   */
  public void updateGeoObject(GeoObject _geoObject)
  {
    if (_geoObject == null)
    {
      throw new RequiredParameterException("Method [updateGeoObject] requires a parameter value for the parameter named [geoObject]", "geoObject");
    }

    JsonObject jsonObject = _geoObject.toJSON();

    String geoJSON = jsonObject.toString();

    HttpResponse resp = this.connector.httpPost(UPDATE_GEO_OBJECT, geoJSON);
    ResponseProcessor.validateStatusCode(resp);
  }

  /**
   * Returns the {@link GeoObject} with the given UID and its children of the
   * given types.
   * 
   * Shall we include the hierarchy types as a parameter as well?
   * 
   * @param parentUid
   *          UID of the parent {@link GeoObject}
   * @param childrenTypes
   *          an array of object types.
   * @param recursive
   *          true if all recursive children should be fetched, or false if only
   *          immediate children should be fetched.
   * 
   * @return {@link ChildTreeNode} containing the {@link GeoObject} with the
   *         given UID and its children of the given types.
   */
  public ChildTreeNode getChildGeoObjects(String parentUid, String[] childrenTypes, Boolean recursive)
  {
    if (parentUid == null)
    {
      throw new RequiredParameterException("Method [getChildGeoObjects] requires a parameter value for the parameter named [parentUid]", "parentUid");
    }

    if (childrenTypes == null || childrenTypes.length == 0)
    {
      throw new RequiredParameterException("Method [getChildGeoObjects] requires a parameter value for the parameter named [childrenTypes]", "childrenTypes");
    }

    if (recursive == null)
    {
      throw new RequiredParameterException("Method [getChildGeoObjects] requires a parameter value for the parameter named [geoObject]", "recursive");
    }

    JsonArray serialized = new JsonArray();

    for (String childType : childrenTypes)
    {
      serialized.add(childType);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("parentUid", parentUid);
    params.put("childrenTypes", serialized.toString());
    params.put("recursive", recursive.toString());

    HttpResponse resp = this.connector.httpGet(GET_CHILDREN_GEO_OBJECTS, params);
    ResponseProcessor.validateStatusCode(resp);

    ChildTreeNode tn = ChildTreeNode.fromJSON(resp.getAsString(), this);

    return tn;
  }

  /**
   * Returns the {@link GeoObject} with the given UID and its parent of the
   * given types.
   * 
   * Shall we include the hierarchy types as a parameter as well?
   * 
   * @param childUid
   *          UID of the child {@link GeoObject}
   * @param parentTypes
   *          an array of object types.
   * @param recursive
   *          true if all recursive parents should be fetched, or false if only
   *          immediate recursive should be fetched.
   * 
   * @return {@link ParentTreeNode} containing the {@link GeoObject} with the
   *         given UID and its children of the given types.
   */
  public ParentTreeNode getParentGeoObjects(String childUid, String[] parentTypes, Boolean recursive)
  {
    if (childUid == null)
    {
      throw new RequiredParameterException("Method [getParentGeoObjects] requires a parameter value for the parameter named [childUid]", "childUid");
    }

    if (parentTypes == null || parentTypes.length == 0)
    {
      throw new RequiredParameterException("Method [getParentGeoObjects] requires a parameter value for the parameter named [parentTypes]", "parentTypes");
    }

    if (recursive == null)
    {
      throw new RequiredParameterException("Method [getParentGeoObjects] requires a parameter value for the parameter named [geoObject]", "recursive");
    }

    JsonArray serialized = new JsonArray();

    for (String parentType : parentTypes)
    {
      serialized.add(parentType);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("childUid", childUid);
    params.put("parentTypes", serialized.toString());
    params.put("recursive", recursive.toString());

    HttpResponse resp = this.connector.httpGet(GET_PARENT_GEO_OBJECTS, params);
    ResponseProcessor.validateStatusCode(resp);

    ParentTreeNode tn = ParentTreeNode.fromJSON(resp.getAsString(), this);

    return tn;
  }
}
