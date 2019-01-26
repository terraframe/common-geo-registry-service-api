package org.commongeoregistry.adapter.dataaccess;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.metadata.AttributeTermType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeTerm extends Attribute
{ 
  private Set<String>      termCodes;

  /**
   * 
   */
  private static final long serialVersionUID = -7912192621951141119L;

  public AttributeTerm(String name)
  {
    super(name, AttributeTermType.TYPE);

    this.termCodes = Collections.synchronizedSet(new HashSet<String>());
  }

  /**
   * Clears any existing term references and sets it to the given reference
   * 
   */
  @Override
  public void setValue(Object value)
  {
	if (value instanceof Term)
	{
      this.setValue((Term)value);
	}
	else
	{
      this.setTerm((String)value);
	}
  }
  
  /**
   * Clears any existing term references and sets it to the given reference
   * 
   */
  public void setValue(Term term)
  {
    this.setTerm(term.getCode());
  }


  public void setTerm(String termCode)
  {
    this.termCodes.clear();
	this.addTerm((String) termCode);
  }
  
  
  public void addTerm(String termCode)
  {
    // TODO add validation to ensure that the provided term is one of the
    // allowed terms on this type
    this.termCodes.add(termCode);
  }
  
  /**
   * 
   * 
   * @param termCode
   */
  public void removeTerm(String termCode)
  {
    this.termCodes.remove(termCode);
  }

  public void clearTerms()
  {
    this.termCodes.clear();
  }

  @Override
  public Iterator<String> getValue()
  {
    return this.termCodes.iterator();
  }

  @Override
  public void toJSON(JsonObject geoObjProps)
  {    
    JsonArray termCodesJson = new JsonArray();

    if (this.termCodes.size() > 0)
    {      
      for (String termCode : this.termCodes)
      {
    	termCodesJson.add(termCode);
      }
    }
    
    geoObjProps.add(this.getName(), termCodesJson);
  }
  
  
  @Override
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    if (!jValue.isJsonArray()) // They may have passed us a JsonNull
    {
      this.clearTerms();
      return;
    }
    
    JsonArray termCodesJson = jValue.getAsJsonArray();
    
    for (JsonElement jsonElement : termCodesJson)
    {
      this.addTerm(jsonElement.getAsString());
    }

//    JsonObject jTerm = jValue.getAsJsonObject();
//    String code = jTerm.get("code").getAsString();
//    
//    Optional<Term> opTerm = registry.getMetadataCache().getTerm(code);
//    
//    if (opTerm.isPresent())
//    {
//      this.terms = Collections.synchronizedList(new LinkedList<Term>());
//      
//      this.terms.add(opTerm.get());
//    }
//    else
//    {
//      throw new RuntimeException("Unable to find term with code [" + code + "].");
//    }
  }

  @Override
  public String toString()
  {
    String toString = this.getName() + ": ";

    toString += " Terms: ";

    for (String termCode : this.termCodes)
    {
      toString += termCode.toString();
    }

    return toString;
  }

}
