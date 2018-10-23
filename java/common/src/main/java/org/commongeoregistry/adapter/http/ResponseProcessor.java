package org.commongeoregistry.adapter.http;

import org.apache.commons.httpclient.HttpStatus;

public class ResponseProcessor
{
  public static void validateStatusCode(HTTPResponse resp)
  {
    int statusCode = resp.getStatusCode();
    
    if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED)
    {
      throw new RuntimeException("Invalid status code [" + statusCode + "].");
    }
  }
}
