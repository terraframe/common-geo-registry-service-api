package org.commongeoregistry.adapter.constants;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.metadata.MetadataFactory;


/**
 * Builds a term tree that defines the terms that define the status of a GeoObject. 
 */
public class DefaultTerms
{

  public static Term buildGeoObjectStatusTree(RegistryAdapter registry)
  {
    Term root = GeoObjectStatusTerm.ROOT.construct(registry);
    Term neww = GeoObjectStatusTerm.NEW.construct(registry);
    Term active = GeoObjectStatusTerm.ACTIVE.construct(registry);
    Term pending = GeoObjectStatusTerm.PENDING.construct(registry);
    Term inactive = GeoObjectStatusTerm.INACTIVE.construct(registry);
    
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
    
    public String code;
    
    public String localizedLabel;
    
    public String localizedDescription;
    
    private GeoObjectStatusTerm(String _code, String _localizedLabel, String _localizedDescription)
    {
      this.code = _code;
      this.localizedLabel = _localizedLabel;
      this.localizedDescription = _localizedDescription;
    }
    
    public Term construct(RegistryAdapter registry)
    {
      return MetadataFactory.newTerm(this.code, this.localizedLabel, this.localizedDescription, registry);
    }
  }
}
