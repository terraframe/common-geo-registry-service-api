package org.commongeoregistry.adapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.commongeoregistry.adapter.constants.RegistryUrls;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.http.Connector;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseProcessor;
import org.commongeoregistry.adapter.id.AdapterIdServiceIF;
import org.commongeoregistry.adapter.id.MemoryOnlyIdService;
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
    super(new MemoryOnlyIdService());
    this.connector = connector;
    ((MemoryOnlyIdService)this.getIdService()).setClient(this);
  }
  
  public HttpRegistryClient(Connector connector, AdapterIdServiceIF idService)
  {
    super(idService);
    this.connector = connector;
    
    if (idService instanceof MemoryOnlyIdService)
    {
      ( (MemoryOnlyIdService) idService ).setClient(this);
    }
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
  public GeoObject getGeoObject(String id, String typeCode)
  {
    if (id == null || id.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET, RegistryUrls.GEO_OBJECT_GET_PARAM_ID);
    }
    if (typeCode == null || typeCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET, RegistryUrls.GEO_OBJECT_GET_PARAM_TYPE_CODE);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put(RegistryUrls.GEO_OBJECT_GET_PARAM_ID, id);
    params.put(RegistryUrls.GEO_OBJECT_GET_PARAM_TYPE_CODE, typeCode);

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
  public GeoObject getGeoObjectByCode(String code, String typeCode)
  {
    if (code == null || code.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CODE, RegistryUrls.GEO_OBJECT_GET_CODE_PARAM_CODE);
    }
    if (typeCode == null || typeCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CODE, RegistryUrls.GEO_OBJECT_GET_CODE_PARAM_TYPE_CODE);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put(RegistryUrls.GEO_OBJECT_GET_CODE_PARAM_CODE, code);
    params.put(RegistryUrls.GEO_OBJECT_GET_CODE_PARAM_TYPE_CODE, typeCode);

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
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_CREATE, RegistryUrls.GEO_OBJECT_CREATE_PARAM_GEOOBJECT);
    }

    JsonObject jsonObject = _geoObject.toJSON();

    JsonObject params = new JsonObject();
    params.add(RegistryUrls.GEO_OBJECT_CREATE_PARAM_GEOOBJECT, jsonObject);

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
  public ParentTreeNode addChild(String parentId, String parentTypeCode, String childId, String childTypeCode, String hierarchyCode)
  {
    if (childId == null || childId.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_CHILDID);
    }
    if (childTypeCode == null || childTypeCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_CHILD_TYPE_CODE);
    }
    if (parentId == null || parentId.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_PARENTID);
    }
    if (parentTypeCode == null || parentTypeCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_PARENT_TYPE_CODE);
    }
    if (hierarchyCode == null || hierarchyCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_ADD_CHILD, RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_HIERARCHY_CODE);
    }
    
    JsonObject params = new JsonObject();
    params.addProperty(RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_CHILDID, childId);
    params.addProperty(RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_CHILD_TYPE_CODE, childTypeCode);
    params.addProperty(RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_PARENTID, parentId);
    params.addProperty(RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_PARENT_TYPE_CODE, parentTypeCode);
    params.addProperty(RegistryUrls.GEO_OBJECT_ADD_CHILD_PARAM_HIERARCHY_CODE, hierarchyCode);
    
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
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_UPDATE, RegistryUrls.GEO_OBJECT_UPDATE_PARAM_GEOOBJECT);
    }

    JsonObject jsonObject = _geoObject.toJSON();

    JsonObject params = new JsonObject();
    params.add(RegistryUrls.GEO_OBJECT_UPDATE_PARAM_GEOOBJECT, jsonObject);

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
  public ChildTreeNode getChildGeoObjects(String parentId, String parentTypeCode, String[] childrenTypes, Boolean recursive)
  {
    if (parentId == null || parentId.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_PARENTID);
    }
    
    if (parentTypeCode == null || parentTypeCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_PARENT_TYPE_CODE);
    }

    if (childrenTypes == null || childrenTypes.length == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_CHILDREN_TYPES);
    }

    if (recursive == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_CHILDREN, RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_RECURSIVE);
    }

    JsonArray serialized = new JsonArray();

    for (String childType : childrenTypes)
    {
      serialized.add(childType);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put(RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_PARENTID, parentId);
    params.put(RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_PARENT_TYPE_CODE, parentTypeCode);
    params.put(RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_CHILDREN_TYPES, serialized.toString());
    params.put(RegistryUrls.GEO_OBJECT_GET_CHILDREN_PARAM_RECURSIVE, recursive.toString());

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
   * @param childId
   *          UID of the child {@link GeoObject}
   * @param childTypeCode
   *          The code of the child {@link GeoObjectType}
   * @param parentTypes
   *          an array of object types.
   * @param recursive
   *          true if all recursive parents should be fetched, or false if only
   *          immediate recursive should be fetched.
   * 
   * @return {@link ParentTreeNode} containing the {@link GeoObject} with the
   *         given UID and its children of the given types.
   */
  public ParentTreeNode getParentGeoObjects(String childId, String childTypeCode, String[] parentTypes, Boolean recursive)
  {
    if (childId == null || childId.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_CHILDID);
    }
    
    if (childTypeCode == null || childTypeCode.length() == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_CHILD_TYPE_CODE);
    }

    if (parentTypes == null || parentTypes.length == 0)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_PARENT_TYPES);
    }

    if (recursive == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_PARENTS, RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_RECURSIVE);
    }

    JsonArray serialized = new JsonArray();

    for (String parentType : parentTypes)
    {
      serialized.add(parentType);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put(RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_CHILDID, childId);
    params.put(RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_CHILD_TYPE_CODE, childTypeCode);
    params.put(RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_PARENT_TYPES, serialized.toString());
    params.put(RegistryUrls.GEO_OBJECT_GET_PARENTS_PARAM_RECURSIVE, recursive.toString());

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
  public Set<String> getGeoObjectUids(Integer numberOfUids)
  {
    if (numberOfUids == null)
    {
      throw new RequiredParameterException(RegistryUrls.GEO_OBJECT_GET_UIDS, RegistryUrls.GEO_OBJECT_GET_UIDS_PARAM_AMOUNT);
    }

    HashMap<String, String> params = new HashMap<String, String>();
    params.put(RegistryUrls.GEO_OBJECT_GET_UIDS_PARAM_AMOUNT, numberOfUids.toString());

    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_GET_UIDS, params);
    ResponseProcessor.validateStatusCode(resp);

    JsonArray values = resp.getAsJsonArray();

    Set<String> set = new HashSet<String>(values.size());

    for (int i = 0; i < values.size(); i++)
    {
      set.add(values.get(i).getAsString());
    }

    return set;
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
    params.put(RegistryUrls.GEO_OBJECT_TYPE_GET_ALL_PARAM_TYPES, types.toString());
    
    HttpResponse resp = this.connector.httpGet(RegistryUrls.GEO_OBJECT_TYPE_GET_ALL, params);
    ResponseProcessor.validateStatusCode(resp);
    
    GeoObjectType[] gots = GeoObjectType.fromJSONArray(resp.getAsString(), this);
    
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
    
    HierarchyType[] hts = HierarchyType.fromJSONArray(resp.getAsString(), this);
    
    return hts;
  }
}
