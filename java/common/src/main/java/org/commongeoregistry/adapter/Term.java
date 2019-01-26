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
