/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HTTPResponse
{
  private String response;
  
  private int statusCode;
  
  public HTTPResponse(String sResp, int statusCode)
  {
    this.response = sResp;
    this.statusCode = statusCode;
  }

  public JsonObject getAsJsonObject()
  {
    JsonParser parser = new JsonParser();
    
    return parser.parse(this.response).getAsJsonObject();
  }
  
  public JsonArray getAsJsonArray()
  {
    JsonParser parser = new JsonParser();
    
    return parser.parse(this.response).getAsJsonArray();
  }
  
  public String getAsString()
  {
    return this.response;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode)
  {
    this.statusCode = statusCode;
  }
}
