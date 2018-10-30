package org.commongeoregistry.adapter.android;

import java.util.Map;
import java.util.Optional;

import org.commongeoregistry.adapter.HttpRegistryClient;
import org.commongeoregistry.adapter.RegistryAdapterServer;
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

public class AndroidRegistryClientTest {

//    @Test
//    public void testRefreshMetadataCache() {
//        /*
//         * Setup mock objects
//         */
//        RegistryAdapterServer registry = new RegistryAdapterServer();
//        GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", registry);
//
//        JsonArray array = new JsonArray();
//        array.add(state.toJSON());
//
//        HttpResponse response = new HttpResponse(array.toString(), 200);
//        MockHttpConnector connector = new MockHttpConnector(response);
//
//        /*
//         * Invoke method
//         */
//        AndroidRegistryClient client = new AndroidRegistryClient(connector);
//        client.refreshMetadataCache();
//
//        /*
//         * Validate request
//         */
//        Assert.assertEquals(HttpRegistryClient.GET_GEO_OBJECT_TYPES, connector.getUrl());
//        Assert.assertEquals(0, connector.getParams().size());
//
//        /*
//         * Validate response
//         */
//        MetadataCache cache = client.getMetadataCache();
//        Optional<GeoObjectType> optional = cache.getGeoObjectType(state.getCode());
//
//        Assert.assertTrue(optional.isPresent());
//
//        GeoObjectType test = optional.get();
//
//        Assert.assertEquals(state.getCode(), test.getCode());
//        Assert.assertEquals(state.getLocalizedDescription(), test.getLocalizedDescription());
//        Assert.assertEquals(state.getLocalizedLabel(), test.getLocalizedLabel());
//    }
//
//    @Test
//    public void testGetGeoObject() {
//        /*
//         * Setup mock objects
//         */
//        MockHttpConnector connector = new MockHttpConnector();
//        AndroidRegistryClient client = new AndroidRegistryClient(connector);
//
//        MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", client);
//
//        GeoObject geoObject = client.newGeoObjectInstance("State");
//        geoObject.setCode("Test");
//        geoObject.setUid("blarg");
//
//        connector.setResponse(new HttpResponse(geoObject.toJSON().toString(), 200));
//
//        /*
//         * Invoke method
//         */
//        GeoObject test = client.getGeoObject(geoObject.getUid());
//
//        /*
//         * Validate response
//         */
//        Assert.assertEquals(geoObject.getCode(), test.getCode());
//        Assert.assertEquals(geoObject.getUid(), test.getUid());
//
//        /*
//         * Validate request
//         */
//        Assert.assertEquals(HttpRegistryClient.GET_GEO_OBJECT, connector.getUrl());
//
//        Map<String, String> params = connector.getParams();
//
//        Assert.assertNotNull(params);
//        Assert.assertEquals(1, params.size());
//
//        Assert.assertTrue(params.containsKey("uid"));
//        Assert.assertEquals(geoObject.getUid(), params.get("uid"));
//
//        /*
//         * Validate cache
//         */
//        LocalObjectCache cache = client.getLocalCache();
//        test = cache.getGeoObject(geoObject.getUid());
//
//        Assert.assertEquals(geoObject.getCode(), test.getCode());
//        Assert.assertEquals(geoObject.getUid(), test.getUid());
//    }
//
}
