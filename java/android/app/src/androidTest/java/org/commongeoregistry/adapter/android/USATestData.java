package org.commongeoregistry.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.junit.Assert;

/**
 * This class contains logic for creating and retrieving test objects in reusable, predictable ways.
 * It also contains logic for assertion of test data.
 */
public class USATestData
{
  public static final String TEST_DATA_KEY = "USATestData";
  
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
  
  public TestGeoObjectTypeInfo[] UNIVERSALS = new TestGeoObjectTypeInfo[]{COUNTRY, STATE, DISTRICT};
  
  public TestGeoObjectInfo[] GEOENTITIES = new TestGeoObjectInfo[]{USA, COLORADO, WASHINGTON, CO_D_ONE, CO_D_TWO, CO_D_THREE, WA_D_ONE, WA_D_TWO};
  
  private ArrayList<TestGeoObjectInfo> customGeoInfos = new ArrayList<TestGeoObjectInfo>();

  private ArrayList<TestGeoObjectTypeInfo> customUniInfos = new ArrayList<TestGeoObjectTypeInfo>();
  
  private RegistryAdapter adapter;
  
  public USATestData(RegistryAdapter adapter)
  {
    this.adapter = adapter;
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
      this.code = TEST_DATA_KEY + "-" + genKey + "Code";
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
    
    public void addChild(TestGeoObjectTypeInfo child, String relationshipType)
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
      return USATestData.this.adapter.getMetadataCache().getGeoObjectType(this.code).get();
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
    
    this.customGeoInfos.add(info);
    
    return info;
  }
  
  public TestGeoObjectInfo newTestGeoObjectInfo(String genKey, TestGeoObjectTypeInfo testUni, String wkt)
  {
    TestGeoObjectInfo info = new TestGeoObjectInfo(genKey, testUni, wkt);
    
    info.delete();
    
    this.customGeoInfos.add(info);
    
    return info;
  }
  
  public TestGeoObjectTypeInfo newTestGeoObjectTypeInfo(String genKey)
  {
    TestGeoObjectTypeInfo info = new TestGeoObjectTypeInfo(genKey);
    
    info.delete();
    
    this.customUniInfos.add(info);
    
    return info;
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
      this.geoId = TEST_DATA_KEY + "-" + genKey + "Code";
      this.displayLabel = TEST_DATA_KEY + " " + genKey + " Display Label";
      this.wkt = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";
      this.universal = testUni;
      this.children = new LinkedList<TestGeoObjectInfo>();
      this.parents = new LinkedList<TestGeoObjectInfo>();
    }

    public String getGeoId() {
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
      GeoObject geoObj = USATestData.this.adapter.newGeoObjectInstance(this.universal.getCode());
      
      geoObj.setWKTGeometry(this.getWkt());
      geoObj.setCode(this.getGeoId());
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
    
//    public void assertEquals(ChildTreeNode tn, String[] childrenTypes, boolean recursive)
//    {
//      this.assertEquals(tn.getGeoObject());
//      // TODO : HierarchyType?
//      
//      List<ChildTreeNode> tnChildren = tn.getChildren();
//      
//      // Check array size
//      int numChildren = 0;
//      for (TestGeoObjectInfo testChild : this.children)
//      {
//        if (ArrayUtils.contains(childrenTypes, testChild.getUniversal().getCode()))
//        {
//          numChildren++;
//        }
//      }
//      Assert.assertEquals(numChildren, tnChildren.size());
//      
//      // Check to make sure all the children match types in our type array
//      for (ChildTreeNode compareChild : tnChildren)
//      {
//        String code = compareChild.getGeoObject().getType().getCode();
//        
//        if (!ArrayUtils.contains(childrenTypes, code))
//        {
//          Assert.fail("Unexpected child with code [" + code + "]. Does not match expected childrenTypes array [" + StringUtils.join(childrenTypes, ", ") + "].");
//        }
//      }
//      
//      for (TestGeoObjectInfo testChild : this.children)
//      {
//        if (ArrayUtils.contains(childrenTypes, testChild.getGeoId()))
//        {
//          ChildTreeNode tnChild = null;
//          for (ChildTreeNode compareChild : tnChildren)
//          {
//            if (testChild.getGeoId().equals(compareChild.getGeoObject().getCode()))
//            {
//              tnChild = compareChild;
//            }
//          }
//          
//          if (tnChild == null)
//          {
//            Assert.fail("The ChildTreeNode did not contain a child that we expected to find.");
//          }
//          else if (recursive)
//          {
//            testChild.assertEquals(tnChild, childrenTypes, recursive);
//          }
//          else
//          {
//            testChild.assertEquals(tnChild.getGeoObject());
//            USATestData.assertEqualsHierarchyType(LocatedIn.CLASS, tnChild.getHierachyType());
//          }
//        }
//      }
//    }
    
//    public void assertEquals(ParentTreeNode tn, String[] parentTypes, boolean recursive)
//    {
//      this.assertEquals(tn.getGeoObject());
//      // TODO : HierarchyType?
//      
//      List<ParentTreeNode> tnParents = tn.getParents();
//      
//      // Check array size
//      int numParents = 0;
//      for (TestGeoObjectInfo testParent : this.parents)
//      {
//        if (ArrayUtils.contains(parentTypes, testParent.getUniversal().getCode()))
//        {
//          numParents++;
//        }
//      }
//      Assert.assertEquals(numParents, tnParents.size());
//      
//      // Check to make sure all the children match types in our type array
//      for (ParentTreeNode compareParent : tnParents)
//      {
//        String code = compareParent.getGeoObject().getType().getCode();
//        
//        if (!ArrayUtils.contains(parentTypes, code))
//        {
//          Assert.fail("Unexpected child with code [" + code + "]. Does not match expected childrenTypes array [" + StringUtils.join(parentTypes, ", ") + "].");
//        }
//      }
//      
//      for (TestGeoObjectInfo testParent : this.parents)
//      {
//        if (ArrayUtils.contains(parentTypes, testParent.getGeoId()))
//        {
//          ParentTreeNode tnParent = null;
//          for (ParentTreeNode compareParent : tnParents)
//          {
//            if (testParent.getGeoId().equals(compareParent.getGeoObject().getCode()))
//            {
//              tnParent = compareParent;
//            }
//          }
//          
//          if (tnParent == null)
//          {
//            Assert.fail("The ParentTreeNode did not contain a child that we expected to find.");
//          }
//          else if (recursive)
//          {
//            testParent.assertEquals(tnParent, parentTypes, recursive);
//          }
//          else
//          {
//            testParent.assertEquals(tnParent.getGeoObject());
//            USATestData.assertEqualsHierarchyType(LocatedIn.CLASS, tnParent.getHierachyType());
//          }
//        }
//      }
//    }
    
    public void assertEquals(GeoObject geoObj)
    {
      Assert.assertEquals(this.getUid(), geoObj.getUid());
      Assert.assertEquals(this.getGeoId(), geoObj.getCode());
//      Assert.assertEquals(StringUtils.deleteWhitespace(this.getWkt()), StringUtils.deleteWhitespace(geoObj.getGeometry().toText()));
      Assert.assertEquals(this.getDisplayLabel(), geoObj.getLocalizedDisplayLabel());
      this.getUniversal().assertEquals(geoObj.getType());
      // TODO : state?
    }
    
    public void addChild(TestGeoObjectInfo child, String relationshipType)
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
  }
}
