package org.commongeoregistry.adapter.metadata;

import java.util.List;

import org.commongeoregistry.adapter.Term;

public class AttributeTermType extends AttributeType
{
  public static String TYPE = "term";
  
  private Term rootTerm     = null;
  
  public AttributeTermType(String _name, String _localizedLabel, String _localizedDescription)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE);
  }
  
  public Term getRootTerm()
  {
    return this.rootTerm;
  }
  
  public void setRootTerm(Term _rootTerm)
  {
    this.rootTerm = _rootTerm;
  }
  
  public List<Term> getTerms()
  {
    return this.rootTerm.getChildren();
  }
}
