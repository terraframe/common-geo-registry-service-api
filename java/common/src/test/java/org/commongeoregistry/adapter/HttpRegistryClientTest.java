package org.commongeoregistry.adapter;

import java.util.Optional;

import org.apache.commons.httpclient.NameValuePair;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseException;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.MetadataCache;
import org.commongeoregistry.adapter.metadata.MetadataFactory;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonArray;

public class HttpRegistryClientTest
{

  @Test
  public void testRefreshMetadataCache()
  {
    /*
     * Setup mock objects
     */
    RegistryAdapterServer registry = new RegistryAdapterServer();
    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registry);

    JsonArray array = new JsonArray();
    array.add(state.toJSON());

    HttpResponse response = new HttpResponse(array.toString(), 200);
    MockHttpConnector connector = new MockHttpConnector(response);

    /*
     * Invoke method
     */
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.refreshMetadataCache();

    /*
     * Validate request
     */
    Assert.assertEquals(HttpRegistryClient.GET_GEO_OBJECT_TYPES, connector.getUrl());
    Assert.assertEquals(0, connector.getParams().length);

    /*
     * Validate response
     */
    MetadataCache cache = client.getMetadataCache();
    Optional<GeoObjectType> optional = cache.getGeoObjectType(state.getCode());

    Assert.assertTrue(optional.isPresent());

    GeoObjectType test = optional.get();

    Assert.assertEquals(state.getCode(), test.getCode());
    Assert.assertEquals(state.getLocalizedDescription(), test.getLocalizedDescription());
    Assert.assertEquals(state.getLocalizedLabel(), test.getLocalizedLabel());
  }

  @Test(expected = ResponseException.class)
  public void testRefreshMetadataCacheBadStatus()
  {
    HttpResponse response = new HttpResponse(new JsonArray().toString(), 400);
    MockHttpConnector connector = new MockHttpConnector(response);

    /*
     * Invoke method
     */
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.refreshMetadataCache();
  }

  @Test
  public void testGetGeoObject()
  {
    /*
     * Setup mock objects
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);

    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", client);

    GeoObject geoObject = client.newGeoObjectInstance("State");
    geoObject.setCode("Test");
    geoObject.setUid("blarg");

    connector.setResponse(new HttpResponse(geoObject.toJSON().toString(), 200));

    /*
     * Invoke method
     */
    GeoObject test = client.getGeoObject(geoObject.getUid());

    /*
     * Validate response
     */
    Assert.assertEquals(geoObject.getCode(), test.getCode());
    Assert.assertEquals(geoObject.getUid(), test.getUid());

    /*
     * Validate request
     */
    Assert.assertEquals(HttpRegistryClient.GET_GEO_OBJECT, connector.getUrl());

    NameValuePair[] params = connector.getParams();

    Assert.assertNotNull(params);
    Assert.assertEquals(1, params.length);

    NameValuePair param = params[0];

    Assert.assertEquals("uid", param.getName());
    Assert.assertEquals(geoObject.getUid(), param.getValue());
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetGeoObjectMissingUID()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getGeoObject(null);
  }

  @Test(expected = ResponseException.class)
  public void testGetGeoObjectBadStatus()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector(new HttpResponse("", 400));
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getGeoObject("23");
  }

  @Test
  public void testCreateGeoObject()
  {
    /*
     * Setup mock objects
     */
    MockHttpConnector connector = new MockHttpConnector(new HttpResponse("", 201));
    HttpRegistryClient client = new HttpRegistryClient(connector);

    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", client);

    GeoObject geoObject = client.newGeoObjectInstance("State");
    geoObject.setCode("Test");
    geoObject.setUid("blarg");

    /*
     * Invoke method
     */
    client.createGeoObject(geoObject);

    /*
     * Validate request
     */
    Assert.assertEquals(HttpRegistryClient.CREATE_GEO_OBJECT, connector.getUrl());

    String body = connector.getBody();

    Assert.assertNotNull(body);

    GeoObject test = GeoObject.fromJSON(client, body);

    Assert.assertEquals(geoObject.getUid(), test.getUid());
  }

  @Test(expected = RequiredParameterException.class)
  public void testCreateGeoObjectMissingUID()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.createGeoObject(null);
  }

  @Test
  public void testUpdateGeoObject()
  {
    /*
     * Setup mock objects
     */
    MockHttpConnector connector = new MockHttpConnector(new HttpResponse("", 201));
    HttpRegistryClient client = new HttpRegistryClient(connector);

    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", client);

    GeoObject geoObject = client.newGeoObjectInstance("State");
    geoObject.setCode("Test");
    geoObject.setUid("blarg");

    /*
     * Invoke method
     */
    client.updateGeoObject(geoObject);

    /*
     * Validate request
     */
    Assert.assertEquals(HttpRegistryClient.UPDATE_GEO_OBJECT, connector.getUrl());

    String body = connector.getBody();

    Assert.assertNotNull(body);

    GeoObject test = GeoObject.fromJSON(client, body);

    Assert.assertEquals(geoObject.getUid(), test.getUid());
  }

  @Test(expected = RequiredParameterException.class)
  public void testUpdateGeoObjectMissingUID()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.updateGeoObject(null);
  }

  @Test
  public void testGetChildGeoObjects()
  {
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);

    /*
     * Setup mock objects
     */
    TestFixture.defineExampleHierarchies(client);
    HierarchyType geoPolitical = client.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();

    GeoObject pOne = client.newGeoObjectInstance(TestFixture.PROVINCE);
    pOne.setCode("pOne");
    pOne.setUid("pOne");
    ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

    GeoObject dOne = client.newGeoObjectInstance(TestFixture.DISTRICT);
    dOne.setCode("dOne");
    dOne.setUid("dOne");
    ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
    ptOne.addChild(dtOne);

    GeoObject cOne = client.newGeoObjectInstance(TestFixture.COMMUNE);
    cOne.setCode("cOne");
    cOne.setUid("cOne");
    ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
    dtOne.addChild(ctOne);

    GeoObject dTwo = client.newGeoObjectInstance(TestFixture.DISTRICT);
    dTwo.setCode("dTwo");
    dTwo.setUid("dTwo");
    ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
    ptOne.addChild(dtTwo);

    connector.setResponse(new HttpResponse(dtOne.toJSON().toString(), 200));

    /*
     * Invoke method
     */
    ChildTreeNode node = client.getChildGeoObjects(pOne.getUid(), new String[] { TestFixture.DISTRICT }, false);

    /*
     * Validate response
     */
    Assert.assertEquals(node.getGeoObject().getType().getCode(), dOne.getType().getCode());

    /*
     * Validate request
     */
    Assert.assertEquals(HttpRegistryClient.GET_CHILDREN_GEO_OBJECTS, connector.getUrl());

    NameValuePair[] params = connector.getParams();

    Assert.assertNotNull(params);
    Assert.assertEquals(3, params.length);

    NameValuePair param = params[0];

    Assert.assertEquals("parentUid", param.getName());
    Assert.assertEquals(pOne.getUid(), param.getValue());

    param = params[1];

    Assert.assertEquals("childrenTypes", param.getName());
    Assert.assertEquals("[\"" + TestFixture.DISTRICT + "\"]", param.getValue());

    param = params[2];

    Assert.assertEquals("recursive", param.getName());
    Assert.assertEquals(Boolean.FALSE.toString(), param.getValue());
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetChildGeoObjectsMissingParentUID()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getChildGeoObjects(null, new String[] { "Test" }, true);
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetChildGeoObjectsMissingChildTypes()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getChildGeoObjects("Abc", null, true);
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetChildGeoObjectsEmptyChildTypes()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getChildGeoObjects("Abc", new String[] {}, true);
  }

  @Test
  public void testGetParentGeoObjects()
  {
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);

    /*
     * Setup mock objects
     */
    TestFixture.defineExampleHierarchies(client);
    HierarchyType geoPolitical = client.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();

    GeoObject pOne = client.newGeoObjectInstance(TestFixture.PROVINCE);
    pOne.setCode("pOne");
    pOne.setUid("pOne");
    ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

    GeoObject dOne = client.newGeoObjectInstance(TestFixture.DISTRICT);
    dOne.setCode("dOne");
    dOne.setUid("dOne");
    ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
    ptOne.addChild(dtOne);

    GeoObject cOne = client.newGeoObjectInstance(TestFixture.COMMUNE);
    cOne.setCode("cOne");
    cOne.setUid("cOne");
    ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
    dtOne.addChild(ctOne);

    GeoObject dTwo = client.newGeoObjectInstance(TestFixture.DISTRICT);
    dTwo.setCode("dTwo");
    dTwo.setUid("dTwo");
    ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
    ptOne.addChild(dtTwo);

    connector.setResponse(new HttpResponse(dtOne.toJSON().toString(), 200));

    /*
     * Invoke method
     */
    ParentTreeNode node = client.getParentGeoObjects(cOne.getUid(), new String[] { TestFixture.DISTRICT }, false);

    /*
     * Validate response
     */
    Assert.assertEquals(node.getGeoObject().getType().getCode(), dOne.getType().getCode());

    /*
     * Validate request
     */
    Assert.assertEquals(HttpRegistryClient.GET_PARENT_GEO_OBJECTS, connector.getUrl());

    NameValuePair[] params = connector.getParams();

    Assert.assertNotNull(params);
    Assert.assertEquals(3, params.length);

    NameValuePair param = params[0];

    Assert.assertEquals("childUid", param.getName());
    Assert.assertEquals(cOne.getUid(), param.getValue());

    param = params[1];

    Assert.assertEquals("parentTypes", param.getName());
    Assert.assertEquals("[\"" + TestFixture.DISTRICT + "\"]", param.getValue());

    param = params[2];

    Assert.assertEquals("recursive", param.getName());
    Assert.assertEquals(Boolean.FALSE.toString(), param.getValue());
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetParentGeoObjectsMissingParentUID()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getParentGeoObjects(null, new String[] { "Test" }, true);
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetParentGeoObjectsMissingParentTypes()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getParentGeoObjects("Abc", null, true);
  }

  @Test(expected = RequiredParameterException.class)
  public void testGetParentGeoObjectsEmptyParentTypes()
  {
    /*
     * Invoke method
     */
    MockHttpConnector connector = new MockHttpConnector();
    HttpRegistryClient client = new HttpRegistryClient(connector);
    client.getParentGeoObjects("Abc", new String[] {}, true);
  }

}
