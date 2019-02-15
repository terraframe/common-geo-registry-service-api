/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.commongeoregistry.adapter.constants.RegistryUrls;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public abstract class AbstractHttpConnector implements Connector
{
  private String serverurl;

  public String getServerUrl()
  {
    return serverurl;
  }

  public void setServerUrl(String url)
  {
    if (!url.endsWith("/"))
    {
      url = url + "/";
    }

    this.serverurl = url;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.commongeoregistry.adapter.http.Connector#httpGet(java.lang.String,
   * java.util.Map)
   */
  @Override
  public HttpResponse httpGet(String url, Map<String, String> params)
  {
    try
    {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getServerUrl());
      builder.append(RegistryUrls.REGISTRY_CONTROLLER_URL);
      builder.append("/");
      builder.append(url);
      
      if (params.size() > 0)
      {
        Set<Entry<String, String>> entries = params.entrySet();
        
        int count = 0;
        for (Entry<String, String> entry : entries)
        {
          String value = entry.getValue();
          
//          if (value.startsWith("[") && value.endsWith("]"))
//          {
//            JsonArray array = new JsonParser().parse(value).getAsJsonArray();
//            String key = entry.getKey();
//            
//            for (int i = 0; i < array.size(); ++i)
//            {
//              builder.append( ( count == 0 ? "?" : "&" ));
//              builder.append(URLEncoder.encode(key, "utf-8"));
//              builder.append("=");
//              builder.append(URLEncoder.encode(array.get(i).getAsString(), "utf-8"));
//              
//              count++;
//            }
//          }
//          else
//          {
            builder.append( ( count == 0 ? "?" : "&" ));
            builder.append(URLEncoder.encode(entry.getKey(), "utf-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(value, "utf-8"));
//          }
          
          count++;
        }
      }
      
      System.out.println("Sending HTTP GET request to [" + builder.toString() + "].");
      
      URL obj = new URL(builder.toString());
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      try
      {
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        
        this.configureHttpUrlConnectionPost(con);

        con.connect();
        
        int status = con.getResponseCode();
        
        InputStream is;
        if (status != HttpURLConnection.HTTP_OK)
        {
          is = con.getErrorStream();
        }
        else
        {
          is = con.getInputStream();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(is)))
        {
          String inputLine;
          StringBuffer response = new StringBuffer();

          while ( ( inputLine = in.readLine() ) != null)
          {
            response.append(inputLine);
          }

          
          HttpResponse resp = new HttpResponse(response.toString(), con.getResponseCode());
          
          System.out.println("Receieved response [" + resp + "].");

          return resp;
        }
      }
      finally
      {
        con.disconnect();
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void configureHttpUrlConnectionPost(HttpURLConnection con)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.commongeoregistry.adapter.http.Connector#httpPost(java.lang.String,
   * java.lang.String)
   */
  @Override
  public HttpResponse httpPost(String url, String body)
  {
    try
    {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getServerUrl());
      builder.append(RegistryUrls.REGISTRY_CONTROLLER_URL);
      builder.append("/");
      builder.append(url);
      
      System.out.println("Sending HTTP POST request to [" + builder.toString() + "].");

      URL obj = new URL(builder.toString());
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      try
      {
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setChunkedStreamingMode(0);
        
        this.configureHttpUrlConnectionPost(con);

        con.connect();

        /*
         * Post the data
         */
        OutputStream out = new BufferedOutputStream(con.getOutputStream());
        out.write(body.getBytes("utf-8"));
        out.close();
        
        int status = con.getResponseCode();
        
        InputStream is;
        if (status != HttpURLConnection.HTTP_OK)
        {
          is = con.getErrorStream();
        }
        else
        {
          is = con.getInputStream();
        }
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is)))
        {
          String inputLine;
          StringBuffer response = new StringBuffer();

          while ( ( inputLine = in.readLine() ) != null)
          {
            response.append(inputLine);
          }
          
          HttpResponse resp = new HttpResponse(response.toString(), con.getResponseCode());
          
          System.out.println("Receieved response [" + resp + "].");

          return resp;
        }
      }
      finally
      {
        con.disconnect();
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
