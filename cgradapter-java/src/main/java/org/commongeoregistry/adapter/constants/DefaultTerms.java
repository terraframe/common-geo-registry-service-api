package org.commongeoregistry.adapter.constants;

import org.commongeoregistry.adapter.Term;


/**
 * Builds a term tree that defines the terms that define the status of a GeoObject. 
 * 
 * @author nathan
 *
 */
public class DefaultTerms
{

  public static Term buildGeoObjectStatusTree()
  {
    Term root = GeoObjectStatusTerm.ROOT.getTerm();
    Term neww = GeoObjectStatusTerm.NEW.getTerm();
    Term active = GeoObjectStatusTerm.ACTIVE.getTerm();
    Term pending = GeoObjectStatusTerm.PENDING.getTerm();
    Term inactive = GeoObjectStatusTerm.INACTIVE.getTerm();
    
    root.addChild(neww);
    root.addChild(active);
    root.addChild(pending);
    root.addChild(inactive);
    
    return root;
  }
  
  public enum GeoObjectStatusTerm 
  {
    ROOT("CGR:Status-Root", "GeoObject Status", "The status of a GeoObject changes during the governance lifecycle."),
    
    NEW("CGR:Status-New", "New", "A newly created GeoObject that has not been submitted for approval."),
    
    ACTIVE("CGR:Status-Active", "Active", "The GeoObject is a part of the master list."),
    
    PENDING("CGR:Status-Pending", "Pending", "Edits to the GeoObject are pending approval"),
    
    INACTIVE("CGR:Status-Inactive", "Inactive", "The object is not considered a source of truth");
    
    private Term term;
    
    private GeoObjectStatusTerm(String _code, String _localizedLabel, String _localizedDescription)
    {
      this.term = new Term(_code, _localizedLabel, _localizedDescription);
    }
    
    public Term getTerm()
    {
      return new Term(this.term.getCode(), this.term.getLocalizedLabel(), this.term.getLocalizedDescription());
    }
   
  }
}
