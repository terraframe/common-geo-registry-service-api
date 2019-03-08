package org.commongeoregistry.adapter.android;

import android.content.Context;

import com.google.gson.JsonObject;

import org.commongeoregistry.adapter.http.AbstractHttpConnector;
import org.commongeoregistry.adapter.http.Connector;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseProcessor;

import java.util.HashMap;

public class AndroidTestRegistryClient extends AndroidRegistryClient
{
    public AndroidTestRegistryClient(Connector connector, Context context)
    {
        super(connector, context);
    }

    public void testSetUp()
    {
        HttpResponse resp = ((AbstractHttpConnector)this.getConnector()).httpGetRaw("integrationtest/testSetUp", new HashMap<String, String>());
        ResponseProcessor.validateStatusCode(resp);
    }

    public void testCleanUp()
    {
        HttpResponse resp = ((AbstractHttpConnector)this.getConnector()).httpGetRaw("integrationtest/testCleanUp", new HashMap<String, String>());
        ResponseProcessor.validateStatusCode(resp);
    }
}
