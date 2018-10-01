package org.commongeoregistry.adapter.dataaccess;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.metadata.AttributeTermType;

public class AttributeTerm extends Attribute
{
  
  private List<Term> terms;

  /**
   * 
   */
  private static final long serialVersionUID = -7912192621951141119L;
  
  public AttributeTerm(String _name)
  {
    super(_name, AttributeTermType.TYPE);
    
    this.terms = Collections.synchronizedList(new LinkedList<Term>()); ;
  }

  @Override
  public void setValue(Object _integer)
  {
    this.terms.clear();
    this.addTerm((Term)_integer);
  }
  
  public void addTerm(Term _term)
  {
    // TODO add validation to ensure that the provided term is one of the allowed terms on this type
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

  public String toString()
  {
    String toString = this.getName()+": ";
    
    toString += " Terms: ";
    
    for (Term term : this.terms)
    {
      toString += term.toString();
    }
    
    return toString;
  }
  
}
