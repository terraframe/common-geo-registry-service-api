package org.commongeoregistry.adapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Term implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 8658638930185089125L;

  private String code;
  
  private String localizedLabel;
  
  private String localizedDescription;
  
  private List<Term> children;
  
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
}
