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
package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.dataaccess.GeoObject;

/**
 * This is a singleton instance that caches {@link GeoObjectType} objects for creating {@link GeoObject}s and 
 * that caches {@link HierarchyType}.
 * 
 * @author nathan
 *
 */
public class MetadataCache implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -8829469298178067536L;
  
  private Map<String, OrganizationDTO> organizationMap;
  private Map<String, GeoObjectType> geoGeoObjectTypeMap;
  private Map<String, HierarchyType> hierarchyTypeMap;
  private Map<String, Term> termMap;
  private RegistryAdapter adapter;
  
  public MetadataCache(RegistryAdapter adapter)
  {
    this.adapter = adapter;
  }
  
  /** 
   * Clears the metadata cache.
   */
  public void rebuild()
  {
    this.organizationMap = new ConcurrentHashMap<String, OrganizationDTO>();
    this.geoGeoObjectTypeMap = new ConcurrentHashMap<String, GeoObjectType>();
    this.hierarchyTypeMap = new ConcurrentHashMap<String, HierarchyType>();
    this.termMap = new ConcurrentHashMap<String, Term>();

    DefaultTerms.buildGeoObjectStatusTree(adapter);
  }
  
  public void addTerm(Term term) 
  {
    this.termMap.put(term.getCode(), term);
  }
    
  public Optional<Term> getTerm(String code) 
  {
    return Optional.of(this.termMap.get(code));
  }

  public void addOrganization(OrganizationDTO organization) 
  {
    this.organizationMap.put(organization.getCode(), organization);
  }
    
  public Optional<OrganizationDTO> getOrganization(String code) 
  {
    return Optional.of(this.organizationMap.get(code));
  }
  
  public OrganizationDTO[] getAllOrganizations()
  {
    return this.organizationMap.values().toArray(new OrganizationDTO[this.organizationMap.values().size()]);
  }
  
  public void removeOrganization(String code)
  {
    this.organizationMap.remove(code);
  }
  
  public void addGeoObjectType(GeoObjectType geoObjectType) 
  {
    this.geoGeoObjectTypeMap.put(geoObjectType.getCode(), geoObjectType);
  }
    
  public Optional<GeoObjectType> getGeoObjectType(String code) 
  {
    return Optional.of(this.geoGeoObjectTypeMap.get(code));
  }
  
  public void removeGeoObjectType(String code)
  {
    this.geoGeoObjectTypeMap.remove(code);
  }
  
  public void addHierarchyType(HierarchyType hierarchyType) 
  {
    this.hierarchyTypeMap.put(hierarchyType.getCode(), hierarchyType);
  }
  
  public Optional<HierarchyType> getHierachyType(String code) 
  {
    return Optional.of(this.hierarchyTypeMap.get(code));
  }
  
  public void removeHierarchyType(String code)
  {
    this.hierarchyTypeMap.remove(code);
  }

  public OrganizationDTO[] getAllOrganizationsTypes()
  {
    return this.organizationMap.values().toArray(new OrganizationDTO[this.organizationMap.values().size()]);
  }
  
  public String[] getAllOrganizationCodes()
  {
    OrganizationDTO[] organizations = this.getAllOrganizationsTypes();
    String[] codes = new String[organizations.length];
    
    for (int i = 0; i < organizations.length; ++i)
    {
      codes[i] = organizations[i].getCode();
    }
 
    return codes;
  }
  
  public GeoObjectType[] getAllGeoObjectTypes()
  {
    return this.geoGeoObjectTypeMap.values().toArray(new GeoObjectType[this.geoGeoObjectTypeMap.values().size()]);
  }
  
  public String[] getAllGeoObjectTypeCodes()
  {
    GeoObjectType[] gots = this.getAllGeoObjectTypes();
    String[] codes = new String[gots.length];
    
    for (int i = 0; i < gots.length; ++i)
    {
      codes[i] = gots[i].getCode();
    }
    
    return codes;
  }

  public HierarchyType[] getAllHierarchyTypes()
  {
    return this.hierarchyTypeMap.values().toArray(new HierarchyType[this.hierarchyTypeMap.values().size()]);
  }
}
