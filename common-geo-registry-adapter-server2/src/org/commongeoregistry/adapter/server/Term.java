package org.commongeoregistry.adapter.server;

import java.util.LinkedList;
import java.util.List;

public class Term
{
  private String code;
  
  private String localizedLabel;
  
  private String localizedDescription;
  
  private List<Term> children;
  
  public Term(String _code, String _localizedLabel, String _localizedDescription)
  {
    this.code = _code;
    this.localizedLabel = _localizedLabel;
    this.localizedDescription = _localizedDescription;
    
    this.children = new LinkedList<Term>();
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
    return this.getChildren();
  }
}
