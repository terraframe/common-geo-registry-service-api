package org.commongeoregistry.adapter.http;

import java.net.HttpURLConnection;

public class ResponseProcessor
{
  public static void validateStatusCode(HttpResponse resp)
  {
    int statusCode = resp.getStatusCode();

    if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED)
    {
      throw new ResponseException("Invalid status code [" + statusCode + "] [" + resp.getAsString() + "].", statusCode);
    }
  }
}
