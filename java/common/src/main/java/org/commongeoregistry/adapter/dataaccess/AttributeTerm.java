package org.commongeoregistry.adapter.dataaccess;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.metadata.AttributeTermType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeTerm extends Attribute
{

  private List<Term>        terms;

  /**
   * 
   */
  private static final long serialVersionUID = -7912192621951141119L;

  public AttributeTerm(String _name)
  {
    super(_name, AttributeTermType.TYPE);

    this.terms = Collections.synchronizedList(new LinkedList<Term>());
  }

  public List<Term> getTerms()
  {
    return this.terms;
  }

  @Override
  public void setValue(Object _integer)
  {
    this.terms.clear();
    this.addTerm((Term) _integer);
  }

  public void addTerm(Term _term)
  {
    // TODO add validation to ensure that the provided term is one of the
    // allowed terms on this type
    this.terms.add(_term);
  }

  public void clearTerms()
  {
    this.terms.clear();
  }

  @Override
  public List<Term> getValue()
  {
    return this.terms;
  }

  @Override
  public void toJSON(JsonObject geoObjProps)
  {
    List<Term> terms = this.getTerms();
    
    JsonObject joTerm = null;
    if (terms.size() > 0)
    {
      joTerm = this.getTerms().get(0).toJSON();
    }
    
    geoObjProps.add(this.getName(), joTerm);
  }
  
  @Override
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    if (!jValue.isJsonObject()) // They may have passed us a JsonNull
    {
      this.clearTerms();
      return;
    }
    
    JsonObject jTerm = jValue.getAsJsonObject();
    String code = jTerm.get("code").getAsString();
    
    Optional<Term> opTerm = registry.getMetadataCache().getTerm(code);
    
    if (opTerm.isPresent())
    {
      this.terms = Collections.synchronizedList(new LinkedList<Term>());
      
      this.terms.add(opTerm.get());
    }
    else
    {
      throw new RuntimeException("Unable to find term with code [" + code + "].");
    }
  }

  @Override
  public String toString()
  {
    String toString = this.getName() + ": ";

    toString += " Terms: ";

    for (Term term : this.terms)
    {
      toString += term.toString();
    }

    return toString;
  }

}
