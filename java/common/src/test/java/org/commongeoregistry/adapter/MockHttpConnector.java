package org.commongeoregistry.adapter;

import org.apache.commons.httpclient.NameValuePair;
import org.commongeoregistry.adapter.http.AbstractHttpConnector;
import org.commongeoregistry.adapter.http.HttpResponse;

public class MockHttpConnector extends AbstractHttpConnector
{
  private String          url;

  private NameValuePair[] params;

  private String          body;

  private HttpResponse    response;

  public MockHttpConnector()
  {
    super();
  }

  public MockHttpConnector(HttpResponse response)
  {
    super();
    this.response = response;
  }

  public String getUrl()
  {
    return url;
  }

  public NameValuePair[] getParams()
  {
    return params;
  }

  public String getBody()
  {
    return body;
  }

  public HttpResponse getResponse()
  {
    return response;
  }

  public void setResponse(HttpResponse response)
  {
    this.response = response;
  }

  private void clear()
  {
    this.url = null;
    this.params = null;
    this.body = null;
  }

  @Override
  public HttpResponse httpGet(String url, NameValuePair[] params)
  {
    this.clear();

    this.url = url;
    this.params = params;

    return this.response;
  }

  @Override
  public HttpResponse httpPost(String url, String body)
  {
    this.clear();

    this.url = url;
    this.body = body;

    return this.response;
  }

}
