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
package org.commongeoregistry.adapter.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.HttpRegistryClient;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.junit.Assert;

/**
 * This class contains logic for creating and retrieving test objects in reusable, predictable ways.
 * It also contains logic for assertion of test data.
 */
public class USATestData
{
  public static final String TEST_DATA_KEY = "USATestData";

  public final TestHierarchyTypeInfo LOCATED_IN = new TestHierarchyTypeInfo("LocatedIn");

  public final TestHierarchyTypeInfo ALLOWED_IN = new TestHierarchyTypeInfo("AllowedIn");
  
  public final TestGeoObjectTypeInfo COUNTRY = new TestGeoObjectTypeInfo("Country");
  
  public final TestGeoObjectInfo USA = new TestGeoObjectInfo("USA", COUNTRY);
  
  public final TestGeoObjectTypeInfo STATE = new TestGeoObjectTypeInfo("State");
  
  public final TestGeoObjectTypeInfo DISTRICT = new TestGeoObjectTypeInfo("District");
  
  public final TestGeoObjectInfo COLORADO = new TestGeoObjectInfo("Colorado", STATE);
  
  public final TestGeoObjectInfo CO_D_ONE = new TestGeoObjectInfo("ColoradoDistrictOne", DISTRICT);
  
  public final TestGeoObjectInfo CO_D_TWO = new TestGeoObjectInfo("ColoradoDistrictTwo", DISTRICT);
  
  public final TestGeoObjectInfo CO_D_THREE = new TestGeoObjectInfo("ColoradoDistrictThree", DISTRICT);
  
  public final TestGeoObjectInfo WASHINGTON = new TestGeoObjectInfo("Washington", STATE, "POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2))");
  
  public final TestGeoObjectInfo WA_D_ONE = new TestGeoObjectInfo("WashingtonDistrictOne", DISTRICT);
  
  public final TestGeoObjectInfo WA_D_TWO = new TestGeoObjectInfo("WashingtonDistrictTwo", DISTRICT);

  public ArrayList<TestGeoObjectTypeInfo> GEOOBJECTTYPES = new ArrayList<TestGeoObjectTypeInfo>(Arrays.asList(new TestGeoObjectTypeInfo[]{COUNTRY, STATE, DISTRICT}));
  
  public ArrayList<TestGeoObjectInfo> GEOOBJECTS = new ArrayList<TestGeoObjectInfo>(Arrays.asList(new TestGeoObjectInfo[]{USA, COLORADO, WASHINGTON, CO_D_ONE, CO_D_TWO, CO_D_THREE, WA_D_ONE, WA_D_TWO}));
  
  private HttpRegistryClient client;
  
  public USATestData(HttpRegistryClient client)
  {
    this.client = client;
  }

  public void setUp()
  {
    for (TestGeoObjectInfo geo : GEOOBJECTS)
    {
      geo.fetchUid();
    }

    COUNTRY.addChild(STATE);
    STATE.addChild(DISTRICT);

    USA.addChild(COLORADO);
    COLORADO.addChild(CO_D_ONE);
    COLORADO.addChild(CO_D_TWO);
    COLORADO.addChild(CO_D_THREE);

    USA.addChild(WASHINGTON);
    WASHINGTON.addChild(WA_D_ONE);
    WASHINGTON.addChild(WA_D_TWO);
  }

  public class TestHierarchyTypeInfo
  {
    private String code;

    private TestHierarchyTypeInfo(String code)
    {
      this.code = code;
    }

    public String getCode()
    {
      return code;
    }

    public void setCode(String code)
    {
      this.code = code;
    }
  }
  
  public class TestGeoObjectTypeInfo
  {
    private String code;
    
    private String displayLabel;
    
    private String description;
    
    private String uid;
    
    private List<TestGeoObjectTypeInfo> children;
    
    private TestGeoObjectTypeInfo(String genKey)
    {
      this.code = TEST_DATA_KEY + genKey + "Code";
      this.displayLabel = TEST_DATA_KEY + " " + genKey + " Display Label";
      this.description = TEST_DATA_KEY + " " + genKey + " Description";
      this.children = new LinkedList<TestGeoObjectTypeInfo>();
    }

    public String getCode() {
      return code;
    }

    public String getDisplayLabel() {
      return displayLabel;
    }

    public String getDescription() {
      return description;
    }

    public String getUid() {
      return uid;
    }

    public void setUid(String uid) {
      this.uid = uid;
    }
    
    public List<TestGeoObjectTypeInfo> getChildren()
    {
      return this.children;
    }
    
    public void addChild(TestGeoObjectTypeInfo child)
    {
      if (!this.children.contains(child))
      {
        this.children.add(child);
      }
    }
    
    public void assertEquals(GeoObjectType got)
    {
      Assert.assertEquals(code, got.getCode());
      Assert.assertEquals(displayLabel, got.getLocalizedLabel());
      Assert.assertEquals(description, got.getLocalizedDescription());
      // TOOD : check the uid
    }
    
    public GeoObjectType newGeoObjectType()
    {
      return USATestData.this.client.getMetadataCache().getGeoObjectType(this.code).get();
//      return RegistryService.getConversionService().universalToGeoObjectType(this.getUniversal());
    }

    public void delete()
    {
      
    }
  }
  
  public TestGeoObjectInfo newTestGeoObjectInfo(String genKey, TestGeoObjectTypeInfo testUni)
  {
    TestGeoObjectInfo info = new TestGeoObjectInfo(genKey, testUni);
    
    info.delete();
    
    this.GEOOBJECTS.add(info);
    
    return info;
  }
  
  public TestGeoObjectInfo newTestGeoObjectInfo(String genKey, TestGeoObjectTypeInfo testUni, String wkt)
  {
    TestGeoObjectInfo info = new TestGeoObjectInfo(genKey, testUni, wkt);
    
    info.delete();
    
    this.GEOOBJECTS.add(info);
    
    return info;
  }
  
  public TestGeoObjectTypeInfo newTestGeoObjectTypeInfo(String genKey)
  {
    TestGeoObjectTypeInfo info = new TestGeoObjectTypeInfo(genKey);

    info.delete();

    this.GEOOBJECTTYPES.add(info);

    return info;
  }

    public static void assertEqualsHierarchyType(String relationshipType, HierarchyType compare)
    {
        // TODO

//        MdRelationship allowedIn = MdRelationship.getMdRelationship(relationshipType);
//
//        Assert.assertEquals(allowedIn.getKey(), compare.getCode());
//        Assert.assertEquals(allowedIn.getDescription().getValue(), compare.getLocalizedDescription());
//        Assert.assertEquals(allowedIn.getDisplayLabel().getValue(), compare.getLocalizedLabel());

//    compare.getRootGeoObjectTypes() // TODO
    }
  
  public class TestGeoObjectInfo
  {
    private String geoId;
    
    private String displayLabel;
    
    private String wkt;
    
    private String uid = null;
    
    private TestGeoObjectTypeInfo universal;
    
    private List<TestGeoObjectInfo> children;
    
    private List<TestGeoObjectInfo> parents;
    
    private TestGeoObjectInfo(String genKey, TestGeoObjectTypeInfo testUni, String wkt)
    {
      initialize(genKey, testUni);
      this.wkt = wkt;
    }
    
    public void delete()
    {
      
    }

    public void setDisplayLabel(String label)
    {
      this.displayLabel = label;
    }

    private TestGeoObjectInfo(String genKey, TestGeoObjectTypeInfo testUni)
    {
      initialize(genKey, testUni);
    }
    
    private void initialize(String genKey, TestGeoObjectTypeInfo testUni)
    {
      this.geoId = TEST_DATA_KEY + genKey + "Code";
      this.displayLabel = TEST_DATA_KEY + " " + genKey + " Display Label";
      this.wkt = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
      this.universal = testUni;
      this.children = new LinkedList<TestGeoObjectInfo>();
      this.parents = new LinkedList<TestGeoObjectInfo>();
    }

    public String getCode() {
      return geoId;
    }

    public String getDisplayLabel() {
      return displayLabel;
    }

    public String getWkt() {
      return wkt;
    }

    public String getUid() {
      return uid;
    }

    public void setUid(String uid) {
      this.uid = uid;
    }
    
    public GeoObject newGeoObject()
    {
      GeoObject geoObj = USATestData.this.client.newGeoObjectInstance(this.universal.getCode());
      
      geoObj.setWKTGeometry(this.getWkt());
      geoObj.setCode(this.getCode());
      geoObj.setLocalizedDisplayLabel(this.getDisplayLabel());
      
      if (uid != null)
      {
        geoObj.setUid(uid);
      }
      
      return geoObj;
    }
    
    public List<TestGeoObjectInfo> getChildren()
    {
      return this.children;
    }
    
    public List<TestGeoObjectInfo> getParents()
    {
      return this.parents;
    }
    
    public void assertEquals(ChildTreeNode tn, String[] childrenTypes, boolean recursive)
    {
      this.assertEquals(tn.getGeoObject());
      // TODO : HierarchyType?

      List<ChildTreeNode> tnChildren = tn.getChildren();

      // Check array size
      int numChildren = 0;
      for (TestGeoObjectInfo testChild : this.children)
      {
        if (ArrayUtils.contains(childrenTypes, testChild.getUniversal().getCode()))
        {
          numChildren++;
        }
      }
      Assert.assertEquals(numChildren, tnChildren.size());

      // Check to make sure all the children match types in our type array
      for (ChildTreeNode compareChild : tnChildren)
      {
        String code = compareChild.getGeoObject().getType().getCode();

        if (!ArrayUtils.contains(childrenTypes, code))
        {
          Assert.fail("Unexpected child with code [" + code + "]. Does not match expected childrenTypes array [" + StringUtils.join(childrenTypes, ", ") + "].");
        }
      }

      for (TestGeoObjectInfo testChild : this.children)
      {
        if (ArrayUtils.contains(childrenTypes, testChild.getCode()))
        {
          ChildTreeNode tnChild = null;
          for (ChildTreeNode compareChild : tnChildren)
          {
            if (testChild.getCode().equals(compareChild.getGeoObject().getCode()))
            {
              tnChild = compareChild;
            }
          }

          if (tnChild == null)
          {
            Assert.fail("The ChildTreeNode did not contain a child that we expected to find.");
          }
          else if (recursive)
          {
            testChild.assertEquals(tnChild, childrenTypes, recursive);
          }
          else
          {
            testChild.assertEquals(tnChild.getGeoObject());
//            USATestData.assertEqualsHierarchyTyp, tnChild.getHierachyType()); // TODO
          }
        }
      }
    }
    
    public void assertEquals(ParentTreeNode tn, String[] parentTypes, boolean recursive)
    {
      this.assertEquals(tn.getGeoObject());
      // TODO : HierarchyType?

      List<ParentTreeNode> tnParents = tn.getParents();

      // Check array size
      int numParents = 0;
      for (TestGeoObjectInfo testParent : this.parents)
      {
        if (ArrayUtils.contains(parentTypes, testParent.getUniversal().getCode()))
        {
          numParents++;
        }
      }
      Assert.assertEquals(numParents, tnParents.size());

      // Check to make sure all the children match types in our type array
      for (ParentTreeNode compareParent : tnParents)
      {
        String code = compareParent.getGeoObject().getType().getCode();

        if (!ArrayUtils.contains(parentTypes, code))
        {
          Assert.fail("Unexpected child with code [" + code + "]. Does not match expected childrenTypes array [" + StringUtils.join(parentTypes, ", ") + "].");
        }
      }

      for (TestGeoObjectInfo testParent : this.parents)
      {
        if (ArrayUtils.contains(parentTypes, testParent.getCode()))
        {
          ParentTreeNode tnParent = null;
          for (ParentTreeNode compareParent : tnParents)
          {
            if (testParent.getCode().equals(compareParent.getGeoObject().getCode()))
            {
              tnParent = compareParent;
            }
          }

          if (tnParent == null)
          {
            Assert.fail("The ParentTreeNode did not contain a child that we expected to find.");
          }
          else if (recursive)
          {
            testParent.assertEquals(tnParent, parentTypes, recursive);
          }
          else
          {
            testParent.assertEquals(tnParent.getGeoObject());
//            USATestData.assertEqualsHierarchyTyp, tnParent.getHierachyType()); // TODO
          }
        }
      }
    }
    
    public void assertEquals(GeoObject geoObj)
    {
      Assert.assertEquals(this.getUid(), geoObj.getUid());
      Assert.assertEquals(this.getCode(), geoObj.getCode());
//      Assert.assertEquals(StringUtils.deleteWhitespace(this.getWkt()), StringUtils.deleteWhitespace(geoObj.getGeometry().toText()));
      Assert.assertEquals(this.getDisplayLabel(), geoObj.getLocalizedDisplayLabel());
      this.getUniversal().assertEquals(geoObj.getType());
      // TODO : state?
    }
    
    public void addChild(TestGeoObjectInfo child)
    {
      if (!this.children.contains(child))
      {
        children.add(child);
      }
      child.addParent(this);
    }
    
    private void addParent(TestGeoObjectInfo parent)
    {
      if (!this.parents.contains(parent))
      {
        this.parents.add(parent);
      }
    }
    
    public TestGeoObjectTypeInfo getUniversal()
    {
      return universal;
    }

    public void fetchUid()
    {
      this.setUid(client.getGeoObjectByCode(this.getCode(), this.getUniversal().getCode()).getUid());
    }
  }
}
