package org.commongeoregistry.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.commongeoregistry.adapter.constants.RegistryUrls;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.http.Connector;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseProcessor;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;

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
  private static final long  serialVersionUID         = -8311449977719450035L;

  private Connector          connector;

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
   * Returns the HTTP connector used for making custom requests to the geo registry server.
   * 
   */
  public Connector getConnector()
  {
    return this.connector;
  }

  /**
   * Clears the metadata cache and populates it with the metadata from the
   * common geo-registry.
   * 
   */
  public void refreshMetadataCache()
  {
    this.getMetadataCache().rebuild();
    
    GeoObjectType[] gots = this.getGeoObjectTypes(new String[]{});

    for (GeoObjectType got : gots)
    {
      this.getMetadataCache().addGeoObjectType(got);
    }
    
    HierarchyType[] hts = this.getHierarchyTypes(new String[]{});
    
    for (HierarchyType ht : hts)
    {
      this.getMetadataCache().addHierarchyType(ht);
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
    if (_uid == null || _uid.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET, "uid");
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("uid", _uid);

    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_GET, params);
    ResponseProcessor.validateStatusCode(resp);

    GeoObject geoObject = GeoObject.fromJSON(this, resp.getAsString());

    return geoObject;
  }
  
  /**
   * Returns the GeoObject with the given code.
   * 
   * @param code
   *          code of the GeoObject.
   * 
   * @return GeoObject with the given code.
   */
  public GeoObject getGeoObjectByCode(String code)
  {
    if (code == null || code.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CODE, "code");
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("code", code);

    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_GET_CODE, params);
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
  public GeoObject createGeoObject(GeoObject _geoObject)
  {
    if (_geoObject == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_CREATE, "geoObject");
    }

    JsonObject jsonObject = _geoObject.toJSON();

    JsonObject params = new JsonObject();
    params.add("geoObject", jsonObject);

    HttpResponse resp = this.connector.httpPost(RegistryUrls.GEO_OBJECT_CREATE, params.toString());
    ResponseProcessor.validateStatusCode(resp);
    
    GeoObject retGeo = GeoObject.fromJSON(this, resp.getAsString());
    return retGeo;
  }
  
  /**
   * Creates a relationship between @parentUid and @childUid.
   *
   * @pre Both the parent and child have already been persisted / applied
   * @post A relationship will exist between @parent and @child
   *
   * @returns ParentTreeNode The new node which was created with the provided parent.
   */
  public ParentTreeNode addChild(String childUid, String parentUid, String hierarchyCode)
  {
    if (childUid == null || childUid.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, "childUid");
    }
    if (parentUid == null || childUid.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, "parentUid");
    }
    if (hierarchyCode == null || childUid.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, "hierarchyCode");
    }
    
    JsonObject params = new JsonObject();
    params.addProperty("childUid", childUid);
    params.addProperty("parentUid", parentUid);
    params.addProperty("hierarchyCode", hierarchyCode);
    
    HttpResponse resp = this.connector.httpPost(RegistryUrls.GEO_OBJECT_ADD_CHILD, params.toString());
    ResponseProcessor.validateStatusCode(resp);
    
    ParentTreeNode ret = ParentTreeNode.fromJSON(resp.getAsString(), this);
    return ret;
  }

  /**
   * Sends the given {@link GeoObject} to the common geo-registry to be updated.
   * 
   * @pre the status on the {@link GeoObject} is NOT in the new state.
   * 
   * @param _geoObject
   */
  public GeoObject updateGeoObject(GeoObject _geoObject)
  {
    if (_geoObject == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_UPDATE, "geoObject");
    }

    JsonObject jsonObject = _geoObject.toJSON();

    JsonObject params = new JsonObject();
    params.add("geoObject", jsonObject);

    HttpResponse resp = this.connector.httpPost(RegistryUrls.GEO_OBJECT_UPDATE, params.toString());
    ResponseProcessor.validateStatusCode(resp);
    
    GeoObject retGeo = GeoObject.fromJSON(this, resp.getAsString());
    return retGeo;
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
    if (parentUid == null || parentUid.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, "parentUid");
    }

    if (childrenTypes == null || childrenTypes.length == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, "childrenTypes");
    }

    if (recursive == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, "recursive");
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

    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_GET_CHILDREN, params);
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
    if (childUid == null || childUid.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, "childUid");
    }

    if (parentTypes == null || parentTypes.length == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, "parentTypes");
    }

    if (recursive == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, "recursive");
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

    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_GET_PARENTS, params);
    ResponseProcessor.validateStatusCode(resp);

    ParentTreeNode tn = ParentTreeNode.fromJSON(resp.getAsString(), this);

    return tn;
  }

  /**
   * Get list of valid UIDs for use in creating new GeoObjects. The Common
   * Geo-Registry will only accept newly created GeoObjects with a UID that was
   * issued from the Common GeoRegistry.
   * 
   * @param numberOfUids
   * 
   * @return An array of UIDs.
   */
  public List<String> getGeoObjectUids(Integer numberOfUids)
  {
    if (numberOfUids == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_UIDS, "numberOfUids");
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put("numberOfUids", numberOfUids.toString());

    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_GET_UIDS, params);
    ResponseProcessor.validateStatusCode(resp);

    JsonArray values = resp.getAsJsonArray();

    List<String> list = new ArrayList<String>(values.size());

    for (int i = 0; i < values.size(); i++)
    {
      list.add(values.get(i).getAsString());
    }

    return list;
  }
  
  /**
   * Sends the given {@link GeoObjectType} to the common geo-registry to be created.
   * 
   * @param geoObjectType
   */
  public void createGeoObjectType(GeoObjectType geoObjectType)
  {
    if (geoObjectType == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_TYPE_CREATE, "geoObjectType");
    }
    
    JsonObject jsonObject = geoObjectType.toJSON();

    JsonObject params = new JsonObject();
    params.add("gtJSON", jsonObject);

    HttpResponse resp = this.connector.httpPost(RegistryUrls.GEO_OBJECT_TYPE_CREATE, params.toString());
    ResponseProcessor.validateStatusCode(resp);
  }
  
  /**
   * Returns an array of {@link GeoOjectType} objects that define the given list of types.
   *
    * @pre 
   * @post 
   *
   * @param types An array of GeoObjectType codes. If blank then all GeoObjectType objects are returned.
   *
    * @returns
   * @throws
   **/
  public GeoObjectType[] getGeoObjectTypes(String[] codes)
  {
    if (codes == null)
    {
      codes = new String[]{};
    }
    
    JsonArray types = new JsonArray();
    for (String code : codes)
    {
      types.add(code);
    }
    
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("types", types.toString());
    
    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_TYPE_GET_ALL, params);
    ResponseProcessor.validateStatusCode(resp);
    
    JsonArray jaGots = resp.getAsJsonArray();
    GeoObjectType[] gots = new GeoObjectType[jaGots.size()];
    for (int i = 0; i < jaGots.size(); ++i)
    {
      GeoObjectType got = GeoObjectType.fromJSON(jaGots.get(i).toString(), this);
      gots[i] = got;
    }
    
    return gots;
  }
  
  /**
   * Returns an array of {@link HierarchyType} that define the given list of types. If no types are provided then all will be returned.
   * 
   * @param types An array of HierarchyType codes that will be retrieved.
   */
  private HierarchyType[] getHierarchyTypes(String[] types)
  {
    if (types == null)
    {
      types = new String[]{};
    }
    
    JsonArray jaTypes = new JsonArray();
    for (String type : types)
    {
      jaTypes.add(type);
    }
    
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("types", jaTypes.toString());
    
    HttpResponse resp = this.connector.httpGet(RegistryUrls.HIERARCHY_TYPE_GET_ALL, params);
    ResponseProcessor.validateStatusCode(resp);
    
    JsonArray jaHts = resp.getAsJsonArray();
    HierarchyType[] hts = new HierarchyType[jaHts.size()];
    for (int i = 0; i < jaHts.size(); ++i)
    {
      HierarchyType got = HierarchyType.fromJSON(jaHts.get(i).toString(), this);
      hts[i] = got;
    }
    
    return hts;
  }
}
