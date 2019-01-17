package org.commongeoregistry.adapter.metadata;

import java.util.List;

import org.commongeoregistry.adapter.Term;

public class AttributeTermType extends AttributeType
{
  /**
   * 
   */
  private static final long serialVersionUID = 6431580798592645011L;

  public static String TYPE = "term";
  
  private Term rootTerm     = null;
  
  public AttributeTermType(String _name, String _localizedLabel, String _localizedDescription, boolean _isDefault)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE, _isDefault);
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
