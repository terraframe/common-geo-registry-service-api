package org.commongeoregistry.adapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Term implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 8658638930185089125L;

  private String            code;

  private String            localizedLabel;

  private String            localizedDescription;

  private List<Term>        children;

  public Term(String _code, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;

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
    obj.addProperty("code", this.getCode());
    obj.addProperty("localizedLabel", this.getLocalizedLabel());
    
    // Child Terms are not stored in a hierarchy structure. They are flattened in an array. 
    JsonArray childTerms = new JsonArray();
    for(int i=0; i<this.getChildren().size(); i++)
    {
      Term child = this.getChildren().get(i);
      childTerms.add(child.toJSON());
    }
    obj.add("children", childTerms);

    return obj;
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
