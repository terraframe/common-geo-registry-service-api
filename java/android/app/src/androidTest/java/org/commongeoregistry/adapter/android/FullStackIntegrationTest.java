package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.commongeoregistry.adapter.USATestData;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests that run in Android and require a common geo registry server running.
 */
public class FullStackIntegrationTest
{
    private USATestData data;

    private AndroidRegistryClient client;

    private USATestData.TestGeoObjectInfo UTAH;

    @Before
    public void setUp()
    {
        Context context = InstrumentationRegistry.getTargetContext();

        // TODO : Not sure how to parameterize this
        AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector();
        connector.setCredentials("admin", "_nm8P4gfdWxGqNRQ#8");
        connector.setServerUrl("https://192.168.0.23:8443/georegistry");
        connector.initialize();

        client = new AndroidRegistryClient(connector, context);
        client.refreshMetadataCache();

        data = new USATestData(client);

        UTAH = data.newTestGeoObjectInfo("Utah", data.STATE);
    }

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
}
