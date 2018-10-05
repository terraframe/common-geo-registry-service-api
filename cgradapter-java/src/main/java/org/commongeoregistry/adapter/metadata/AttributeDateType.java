package org.commongeoregistry.adapter.metadata;


public class AttributeDateType extends AttributePrimitiveType
{
  /**
   * 
   */
  private static final long serialVersionUID = 1543071656686171731L;
  public static String TYPE = "date";
  
  public AttributeDateType(String _name, String _localizedLabel, String _localizedDescription)
  {
    super(_name, _localizedLabel, _localizedDescription, TYPE);
  }
}
