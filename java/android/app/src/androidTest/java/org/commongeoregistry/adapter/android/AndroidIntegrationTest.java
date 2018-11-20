package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests that run in Android and require a common geo registry server running.
 */
public class AndroidIntegrationTest
{
    private USATestData data;

    private AndroidRegistryClient client;

    private USATestData.TestGeoObjectInfo UTAH;

    private USATestData.TestGeoObjectInfo CALIFORNIA;

    private USATestData.TestGeoObjectInfo TEST_ADD_CHILD;

    @Before
    public void setUp()
    {
        Context context = InstrumentationRegistry.getTargetContext();

        // TODO : Not sure how to parameterize this
        AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector();
        connector.setCredentials("admin", "_nm8P4gfdWxGqNRQ#8");
        connector.setServerUrl("https://172.31.99.191:8443/georegistry");
        connector.initialize();

        client = new AndroidRegistryClient(connector, context);
        client.refreshMetadataCache();

        data = new USATestData(client);
        // These objects are predefined:
        TEST_ADD_CHILD = data.newTestGeoObjectInfo("TEST_ADD_CHILD", data.STATE);
        data.setUp();

        // These objects do not exist in the database yet:
        UTAH = data.newTestGeoObjectInfo("Utah", data.STATE);
        CALIFORNIA = data.newTestGeoObjectInfo("California", data.STATE);
    }

    @Test
    public void testCreateGetUpdateGeoObject()
    {
        // TODO : The AndroidRegistryCient seems to be logging in every time we make a request.
        // This may not be sustainable because we may run out of available sessions.
        // We should be managing the log in / log out state somehow.

        // 1. Create a Geo Object locally
        GeoObject goUtah = UTAH.newGeoObject();

        // 2. Send the new GeoObject to the server to be applied to the database
        GeoObject go2 = client.createGeoObject(goUtah);
        UTAH.setUid(go2.getUid());
        UTAH.assertEquals(go2);

        // 3. Retrieve the new GeoObject from the server
        GeoObject go3 = client.getGeoObject(go2.getUid());
        UTAH.assertEquals(go3);

        // 4. Update the GeoObject
        final String newLabel = "MODIFIED DISPLAY LABEL";
        go3.setLocalizedDisplayLabel(newLabel);
        UTAH.setDisplayLabel(newLabel);
        GeoObject go4 = client.updateGeoObject(go3);
        UTAH.assertEquals(go4);

        // 5. Fetch it one last time to make sure our update worked
        GeoObject go5 = client.getGeoObject(go4.getUid());
        UTAH.assertEquals(go5);
    }

//    @Test
//    public void testGetChildGeObjects() // TODO : This test doesn't work because of the LocatedIn / AllowedIn bug that Nate is working on.
//    {
//        String[] childrenTypes = new String[]{data.STATE.getCode(), data.DISTRICT.getCode()};
//
//        // Recursive
//        ChildTreeNode tn = client.getChildGeoObjects(data.USA.getUid(), childrenTypes, true);
//        data.USA.assertEquals(tn, childrenTypes, true);
//        Assert.assertEquals(tn.toJSON().toString(), ChildTreeNode.fromJSON(tn.toJSON().toString(), client).toJSON().toString());
//
//        // Not recursive
//        ChildTreeNode tn2 = client.getChildGeoObjects(data.USA.getUid(), childrenTypes, false);
//        data.USA.assertEquals(tn2, childrenTypes, false);
//        Assert.assertEquals(tn2.toJSON().toString(), ChildTreeNode.fromJSON(tn2.toJSON().toString(), client).toJSON().toString());
//
//        // Test only getting districts
//        String[] distArr = new String[]{data.DISTRICT.getCode()};
//        ChildTreeNode tn3 = client.getChildGeoObjects(data.USA.getUid(), distArr, true);
//        data.USA.assertEquals(tn3, distArr, true);
//        Assert.assertEquals(tn3.toJSON().toString(), ChildTreeNode.fromJSON(tn3.toJSON().toString(), client).toJSON().toString());
//    }

//    @Test
//    public void testExecuteActions()
//    {
//        // Create a new GeoObject locally
//        GeoObject goCali = CALIFORNIA.newGeoObject();
//        client.getLocalCache().createGeoObject(goCali); // TODO : This does not work because we don't have ids locally
//
//        // Update that GeoObject
//        final String newLabel = "MODIFIED DISPLAY LABEL";
//        goCali.setLocalizedDisplayLabel(newLabel);
//        client.getLocalCache().updateGeoObject(goCali);
//
//        client.pushObjectsToRegistry();
//
//        // Fetch California and make sure it has our new display label
//        GeoObject goCali2 = client.getGeoObjectByCode(CALIFORNIA.getCode());
//
//        CALIFORNIA.setUid(goCali2.getUid());
//        CALIFORNIA.setDisplayLabel(newLabel);
//        CALIFORNIA.assertEquals(goCali2);
//    }

    // TODO : This test doesn't work because of the LocatedIn / AllowedIn bug that Nate is working on.
//    @Test
//    public void testAddChild()
//    {
//        ParentTreeNode ptnTestState = client.addChild(data.USA.getUid(), TEST_ADD_CHILD.getUid(), data.LOCATED_IN.getCode());
//
//        boolean found = false;
//        for (ParentTreeNode ptnUSA : ptnTestState.getParents())
//        {
//            if (ptnUSA.getGeoObject().getCode().equals(data.USA.getCode()))
//            {
//                found = true;
//                break;
//            }
//        }
//        Assert.assertTrue("Did not find our test object in the list of returned children", found);
//        TEST_ADD_CHILD.assertEquals(ptnTestState.getGeoObject());
//
//        ChildTreeNode ctnUSA2 = client.getChildGeoObjects(data.USA.getUid(), new String[]{data.STATE.getCode()}, false);
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
    }
}
