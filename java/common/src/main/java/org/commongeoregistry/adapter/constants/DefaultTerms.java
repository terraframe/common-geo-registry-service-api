/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
