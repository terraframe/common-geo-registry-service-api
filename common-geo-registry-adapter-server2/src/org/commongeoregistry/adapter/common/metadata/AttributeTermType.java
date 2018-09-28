package org.commongeoregistry.adapter.common.metadata;

import java.util.List;

import org.commongeoregistry.adapter.server.Term;

public class AttributeTermType extends AttributePrimitiveType
{
  private Term rootTerm;
  
  public AttributeTermType(String _code, String _localizedLabel, String _localizedDescription, String _type, Term _rootTerm)
  {
    super(_code, _localizedLabel, _localizedDescription, _type);
    
    this.rootTerm = _rootTerm;
  }
  
  public Term getRootTerm()
  {
    return this.rootTerm;
  }
  
  public List<Term> getTerms()
  {
    return this.rootTerm.getChildren();
  }
  
}
