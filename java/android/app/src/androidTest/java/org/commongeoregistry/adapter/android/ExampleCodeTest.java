package org.commongeoregistry.adapter.android;

import android.support.test.InstrumentationRegistry;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.http.AuthenticationException;
import org.commongeoregistry.adapter.http.ServerResponseException;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ExampleCodeTest
{
    @Test
    public void testExampleCode() throws AuthenticationException, ServerResponseException, IOException, InvocationTargetException {
        // Configure our connection with the remote registry server
        AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector();
        connector.setCredentials("admin", "admin");
        connector.setServerUrl("https://georegistry.geoprism.net");
        connector.initialize();

        // Synchronize with the Registry server (requires an internet connection)
        AndroidRegistryClient client = new AndroidRegistryClient(connector, InstrumentationRegistry.getTargetContext()); // The second parameter here is the Android context
        client.refreshMetadataCache(); // Pull our metadata objects which we'll use later
        client.getIdService().populate(500); // Fetch 500 ids from the GeoRegistry which we'll use later to create GeoObjects. These ids are persisted in an offline database on the android phone.

        // Create a new GeoObject locally
        GeoObject newGo = client.newGeoObjectInstance("Cambodia_Commune");
        newGo.setCode("855 30");
        newGo.setDisplayLabel(LocalizedValue.DEFAULT_LOCALE, "New Cambodia Commune (Android)");
        newGo.setWKTGeometry("MULTIPOLYGON (((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000)))");

        client.getLocalCache().createGeoObject(newGo); // Save the 'create action' of our GeoObject to the offline database so that we can send it to the server later

        // Update that GeoObject
        newGo.setDisplayLabel(LocalizedValue.DEFAULT_LOCALE, "New Cambodia Commune (Modified)");
        client.getLocalCache().updateGeoObject(newGo); // Save the 'update action' of our GeoObject to the offline database so that we can send it to the server later

        // Push all our updates to the GeoRegistry (requires an internet connection)
        client.pushObjectsToRegistry();

        // TODO : You won't be able to fetch our new GeoObject here because the admin hasn't approved it yet
        // Fetch California and make sure it has our new display label (requires an internet connection)
        GeoObject newGo2 = client.getGeoObjectByCode("855 30", "Cambodia_Commune");
        System.out.println(newGo2.getLocalizedDisplayLabel()); // Prints "New Cambodia Commune (Modified)"

        // Gets the type codes of all GeoObjects in the system
        String[] typeCodes = client.getMetadataCache().getAllGeoObjectTypeCodes();

        // Get all children of California (of all type codes) (requires an internet connection)
        ChildTreeNode ctn = client.getChildGeoObjects(newGo2.getUid(), newGo2.getType().getCode(), typeCodes, true);

        // Cache these children for later retrieval
        client.getLocalCache().cache(ctn);

        // Fetch the children (offline)
        client.getLocalCache().getChildGeoObjects(newGo2.getUid(), typeCodes, true);

        // TODO : Fetching children of the root GeoObject
    }
}
