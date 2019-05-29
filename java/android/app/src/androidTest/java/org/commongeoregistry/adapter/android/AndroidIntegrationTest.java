package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests that run in Android and require a common geo registry server running.
 */
public class AndroidIntegrationTest
{
    private static final String serverUrl = "https://192.168.0.23:8443/georegistry";

    private static final String user = "admin";

    private static final String pass = "_nm8P4gfdWxGqNRQ#8";

    private USATestData data;

    private AndroidTestRegistryClient client;

    private USATestData.TestGeoObjectInfo UTAH;

    private USATestData.TestGeoObjectInfo CALIFORNIA;

    private USATestData.TestGeoObjectInfo TEST_ADD_CHILD;

    @Before
    public void setUp()
    {
        Context context = InstrumentationRegistry.getTargetContext();

        AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector();
        connector.setCredentials(user, pass);
        connector.setServerUrl(serverUrl);
        connector.initialize();

        client = new AndroidTestRegistryClient(connector, context);
        client.getLocalCache().clear();
        client.testSetUp();

        client.refreshMetadataCache();
        client.getIdService().populate(500);

        data = new USATestData(client);
        // These objects are predefined:
        TEST_ADD_CHILD = data.newTestGeoObjectInfo("TEST_ADD_CHILD", data.DISTRICT);
        data.setUp();

        // These objects do not exist in the database yet:
        UTAH = data.newTestGeoObjectInfo("Utah", data.STATE);
        CALIFORNIA = data.newTestGeoObjectInfo("California", data.STATE);
    }

    @After
    public void cleanUp()
    {
        client.testCleanUp();
    }

//    @Test(expected = InvalidLoginException.class)
//    public void testInvalidLoginException()
//    {
//        AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector();
//        connector.setCredentials("admin", "bad");
//        connector.setServerUrl("https://192.168.0.23:8443/georegistry");
//        connector.initialize();
//
//        AndroidRegistryClient client = new AndroidRegistryClient(connector, InstrumentationRegistry.getTargetContext());
//        client.refreshMetadataCache();
//    }

    @Test
    public void testCreateGetUpdateGeoObject()
    {
        // 1. Create a Geo Object locally
        GeoObject goUtah = UTAH.newGeoObject();

        // 2. Send the new GeoObject to the server to be applied to the database
        GeoObject go2 = client.createGeoObject(goUtah);
        UTAH.setUid(go2.getUid());
        UTAH.assertEquals(go2);

        // 3. Retrieve the new GeoObject from the server
        int numRegistryIds = client.getLocalCache().countNumberRegistryIds();
        GeoObject go3 = client.getGeoObject(go2.getUid(), go2.getType().getCode());
        UTAH.assertEquals(go3);
        Assert.assertEquals(numRegistryIds, client.getLocalCache().countNumberRegistryIds());

        // 4. Update the GeoObject
        final String newLabel = "MODIFIED DISPLAY LABEL";
        go3.setDisplayLabel(LocalizedValue.DEFAULT_LOCALE, newLabel);
        UTAH.setDisplayLabel(newLabel);
        GeoObject go4 = client.updateGeoObject(go3);
        UTAH.assertEquals(go4);

        // 5. Fetch it one last time to make sure our update worked
        GeoObject go5 = client.getGeoObject(go4.getUid(), go4.getType().getCode());
        UTAH.assertEquals(go5);
    }

    @Test
    public void testGetParentGeoObjects()
    {
        int numRegistryIds = client.getLocalCache().countNumberRegistryIds();
      
        String childId = data.CO_D_TWO.getUid();
        String childTypeCode = data.CO_D_TWO.getUniversal().getCode();
        String[] childrenTypes = new String[]{data.COUNTRY.getCode(), data.STATE.getCode()};

        // Recursive
        ParentTreeNode tn = client.getParentGeoObjects(childId, childTypeCode, childrenTypes, true);
        data.CO_D_TWO.assertEquals(tn, childrenTypes, true);
        Assert.assertEquals(tn.toJSON().toString(), ParentTreeNode.fromJSON(tn.toJSON().toString(), client).toJSON().toString());

        // Not recursive
        ParentTreeNode tn2 = client.getParentGeoObjects(childId, childTypeCode, childrenTypes, false);
        data.CO_D_TWO.assertEquals(tn2, childrenTypes, false);
        Assert.assertEquals(tn2.toJSON().toString(), ParentTreeNode.fromJSON(tn2.toJSON().toString(), client).toJSON().toString());

        // Test only getting countries
        String[] countryArr = new String[]{data.COUNTRY.getCode()};
        ParentTreeNode tn3 = client.getParentGeoObjects(childId, childTypeCode, countryArr, true);
        data.CO_D_TWO.assertEquals(tn3, countryArr, true);
        Assert.assertEquals(tn3.toJSON().toString(), ParentTreeNode.fromJSON(tn3.toJSON().toString(), client).toJSON().toString());
        
        Assert.assertEquals(numRegistryIds, client.getLocalCache().countNumberRegistryIds());
    }

    @Test
    public void testGetChildGeObjects()
    {
        int numRegistryIds = client.getLocalCache().countNumberRegistryIds();
      
        String[] childrenTypes = new String[]{data.STATE.getCode(), data.DISTRICT.getCode()};

        // Recursive
        ChildTreeNode tn = client.getChildGeoObjects(data.USA.getUid(), data.USA.getUniversal().getCode(), childrenTypes, true);
        data.USA.assertEquals(tn, childrenTypes, true);
        Assert.assertEquals(tn.toJSON().toString(), ChildTreeNode.fromJSON(tn.toJSON().toString(), client).toJSON().toString());

        // Not recursive
        ChildTreeNode tn2 = client.getChildGeoObjects(data.USA.getUid(), data.USA.getUniversal().getCode(), childrenTypes, false);
        data.USA.assertEquals(tn2, childrenTypes, false);
        Assert.assertEquals(tn2.toJSON().toString(), ChildTreeNode.fromJSON(tn2.toJSON().toString(), client).toJSON().toString());

        // Test only getting districts
        String[] distArr = new String[]{data.DISTRICT.getCode()};
        ChildTreeNode tn3 = client.getChildGeoObjects(data.USA.getUid(), data.USA.getUniversal().getCode(), distArr, true);
        data.USA.assertEquals(tn3, distArr, true);
        Assert.assertEquals(tn3.toJSON().toString(), ChildTreeNode.fromJSON(tn3.toJSON().toString(), client).toJSON().toString());
        
        Assert.assertEquals(numRegistryIds, client.getLocalCache().countNumberRegistryIds());
    }

    @Test
    public void testExecuteActions()
    {
        // Create a new GeoObject locally
        GeoObject goCali = CALIFORNIA.newGeoObject();
        client.getLocalCache().createGeoObject(goCali);

        // Update that GeoObject
        final String newLabel = "MODIFIED DISPLAY LABEL";
        goCali.setDisplayLabel(LocalizedValue.DEFAULT_LOCALE, newLabel);
        client.getLocalCache().updateGeoObject(goCali);

        Assert.assertEquals(2, client.getLocalCache().getAllActionHistory().size());
        client.pushObjectsToRegistry();

        // TODO : This test isn't even possible anymore. Actions are not executed immediately when
        // they are received.
//        // Fetch California and make sure it has our new display label
//        GeoObject goCali2 = client.getGeoObjectByCode(CALIFORNIA.getCode(), CALIFORNIA.getUniversal().getCode());
//
//        CALIFORNIA.setUid(goCali2.getUid());
//        CALIFORNIA.setDisplayLabel(newLabel);
//        CALIFORNIA.assertEquals(goCali2);
//
//        // Update that GeoObject again
//        final String newLabel2 = "MODIFIED DISPLAY LABEL2";
//        goCali.setLocalizedDisplayLabel(newLabel2);
//        client.getLocalCache().updateGeoObject(goCali);
//
//        // Make sure that when we push it only pushes our new update and not the old ones again
//        Assert.assertEquals(1, client.getLocalCache().getUnpushedActionHistory().size());
    }

    @Test
    public void testAddChild()
    {
        ParentTreeNode ptnTestState = client.addChild(data.WASHINGTON.getUid(), data.WASHINGTON.getUniversal().getCode(), TEST_ADD_CHILD.getUid(), TEST_ADD_CHILD.getUniversal().getCode(), data.LOCATED_IN.getCode());

        boolean found = false;
        for (ParentTreeNode ptnUSA : ptnTestState.getParents())
        {
            if (ptnUSA.getGeoObject().getCode().equals(data.WASHINGTON.getCode()))
            {
                found = true;
                break;
            }
        }
        Assert.assertTrue("Did not find our test object in the list of returned children", found);
        TEST_ADD_CHILD.assertEquals(ptnTestState.getGeoObject());

        ChildTreeNode ctnUSA2 = client.getChildGeoObjects(data.WASHINGTON.getUid(), data.WASHINGTON.getUniversal().getCode(), new String[]{data.DISTRICT.getCode()}, false);

        found = false;
        for (ChildTreeNode ctnState : ctnUSA2.getChildren())
        {
            if (ctnState.getGeoObject().getCode().equals(TEST_ADD_CHILD.getCode()))
            {
                found = true;
                break;
            }
        }
        Assert.assertTrue("Did not find our test object in the list of returned children", found);
    }

    // TODO
//    @Test
//    public void testRemoveChild()
//    {
//        ParentTreeNode ptnTestState = client.addChild(data.WASHINGTON.getUid(), data.WASHINGTON.getUniversal().getCode(), TEST_ADD_CHILD.getUid(), TEST_ADD_CHILD.getUniversal().getCode(), data.LOCATED_IN.getCode());
//
//        boolean found = false;
//        for (ParentTreeNode ptnUSA : ptnTestState.getParents())
//        {
//            if (ptnUSA.getGeoObject().getCode().equals(data.WASHINGTON.getCode()))
//            {
//                found = true;
//                break;
//            }
//        }
//        Assert.assertTrue("Did not find our test object in the list of returned children", found);
//        TEST_ADD_CHILD.assertEquals(ptnTestState.getGeoObject());
//
//        ChildTreeNode ctnUSA2 = client.getChildGeoObjects(data.WASHINGTON.getUid(), data.WASHINGTON.getUniversal().getCode(), new String[]{data.DISTRICT.getCode()}, false);
//
//        found = false;
//        for (ChildTreeNode ctnState : ctnUSA2.getChildren())
//        {
//            if (ctnState.getGeoObject().getCode().equals(TEST_ADD_CHILD.getCode()))
//            {
//                found = true;
//                break;
//            }
//        }
//        Assert.assertTrue("Did not find our test object in the list of returned children", found);
//    }
}
