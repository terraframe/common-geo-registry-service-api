package org.commongeoregistry.adapter.http;

import java.util.Map;

public interface Connector
{

  HttpResponse httpGet(String url, Map<String, String> params);

  HttpResponse httpPost(String url, String body);

}