package com.cgr.adapter.common.metadata;

import java.util.List;

import com.cgr.adapter.server.Term;

public class AttributeTermType extends AttributePrimitiveType
{
  private Term rootTerm;
  
  public AttributeTermType(String _name, String _localizedLabel, String _localizedDescription, String _type, Term _rootTerm)
  {
    super(_name, _localizedLabel, _localizedDescription, _type);
    
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
