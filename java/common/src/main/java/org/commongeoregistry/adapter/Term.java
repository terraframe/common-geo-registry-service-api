/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Term implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID                = 8658638930185089125L;
  
  public static final String JSON_CODE			            = "code";
  
  public static final String JSON_LOCALIZED_LABEL           = "localizedLabel";
  
  public static final String JSON_LOCALIZED_DESCRIPTION     = "localizedDescription";
  
  public static final String JSON_CHILDREN                  = "children";

  private String            code;

  private String            localizedLabel;

  private String            localizedDescription;

  private List<Term>        children;

  public Term(String code, String localizedLabel, String localizedDescription)
  {
    this.code = code;
    this.localizedLabel = localizedLabel;
    this.localizedDescription = localizedDescription;

    this.children = Collections.synchronizedList(new LinkedList<Term>());
  }
  
  public String getCode()
  {
    return this.code;
  }

  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }

  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }

  public void addChild(Term childTerm)
  {
    this.children.add(childTerm);
  }

  public List<Term> getChildren()
  {
    return this.children;
  }
  
  public static JsonArray toJSON(Term[] terms)
  {
    JsonArray json = new JsonArray();
	for(Term term : terms)
    {
      json.add(term.toJSON());
	}
	  
	return json;
  }

  public JsonObject toJSON()
  {
    JsonObject obj = new JsonObject();
    obj.addProperty(JSON_CODE, this.getCode());
    obj.addProperty(JSON_LOCALIZED_LABEL, this.getLocalizedLabel());
    obj.addProperty(JSON_LOCALIZED_DESCRIPTION, this.getLocalizedDescription());
    
    // Child Terms are not stored in a hierarchy structure. They are flattened in an array. 
    JsonArray childTerms = new JsonArray();
    for(int i=0; i<this.getChildren().size(); i++)
    {
      Term child = this.getChildren().get(i);
      childTerms.add(child.toJSON());
    }
    obj.add(JSON_CHILDREN, childTerms);

    return obj;
  }

  
  /**
   * Creates a {@link Term} object including references to child terms.
   * 
   * @param termObj
   * @return
   */
  public static Term fromJSON(JsonObject termObj)
  {
    String code = termObj.get(Term.JSON_CODE).getAsString();
	String localizedLabel = termObj.get(Term.JSON_LOCALIZED_LABEL).getAsString();
	String localizedDescription = termObj.get(Term.JSON_LOCALIZED_DESCRIPTION).getAsString();
	
	Term term = new Term(code, localizedLabel, localizedDescription);
	
	JsonElement children = termObj.get(Term.JSON_CHILDREN);
	
	if (children != null && !children.isJsonNull() && children.isJsonArray())
	{
	  JsonArray childrenArray = children.getAsJsonArray();
	  
	  for (JsonElement jsonElement : childrenArray)
	  {
	    if (jsonElement.isJsonObject())
	    {
	      JsonObject childTermObj = jsonElement.getAsJsonObject();
	      
	      Term childTerm = Term.fromJSON(childTermObj);
	      term.addChild(childTerm);
	    }
	  }
	}
	
	return term;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    return (obj instanceof Term) && ((Term)obj).getCode().equals(this.getCode());
  }
  
  @Override
  public String toString()
  {
    String toString = "Term: " + this.code + " ";

    boolean firstIteration = true;
    for (Term child : getChildren())
    {
      if (firstIteration)
      {
        toString += "Children:{";
        firstIteration = false;
      }
      else
      {
        toString += ", ";
      }

      toString += child.toString();
    }

    if (!firstIteration)
    {
      toString += "}";
    }

    return toString;
  }
}
