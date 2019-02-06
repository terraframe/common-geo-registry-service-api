package org.commongeoregistry.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.commongeoregistry.adapter.action.AbstractActionDTO;
import org.commongeoregistry.adapter.action.geoobject.CreateGeoObjectActionDTO;
import org.commongeoregistry.adapter.action.geoobject.UpdateGeoObjectActionDTO;
import org.commongeoregistry.adapter.action.tree.AddChildActionDTO;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.MetadataFactory;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;

public class SerializationTest
{
	
  @Test
  public void testTerm()
  {
    Term facilityType = new Term("FACILITY_TYPE", "Facility Type", "...");
	Term clinic = new Term("CLINIC", "Clinic", "...");
	Term matWard = new Term("MATERNITY_WARD", "Maternity Ward", "...");
	facilityType.addChild(clinic);
	facilityType.addChild(matWard);
	  
	JsonObject jsonObject = facilityType.toJSON();
	    
	Term facilityType2 = Term.fromJSON(jsonObject);
	  
	Assert.assertEquals(facilityType.getCode(), facilityType2.getCode());
	Assert.assertEquals(facilityType.getLocalizedLabel(), facilityType2.getLocalizedLabel());
	Assert.assertEquals(facilityType.getLocalizedDescription(), facilityType2.getLocalizedDescription());
	  
	Assert.assertEquals(facilityType.getChildren().size(), facilityType2.getChildren().size());
  }
	
  @Test
  public void testGeoObject()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", false, registry);

    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";

    GeoObject geoObject = registry.newGeoObjectInstance("State");

    geoObject.setWKTGeometry(geom);
    geoObject.setCode("Colorado");
    geoObject.setUid("CO");
    geoObject.setLocalizedDisplayLabel("Colorado Display Label");

    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registry, sJson);
    String sJson2 = geoObject2.toJSON().toString();

    Assert.assertEquals(sJson, sJson2);
    Assert.assertEquals("Colorado", geoObject2.getCode());
    Assert.assertEquals("CO", geoObject2.getUid());
    Assert.assertEquals("Colorado Display Label", geoObject2.getLocalizedDisplayLabel());
    Assert.assertEquals(geoObject.getStatus(), geoObject2.getStatus());
  }

  /**
   * Tests to make sure optional values are allowed and handled properly.
   */
  @Test
  public void testOptionalGeoObject()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", false, registry);

    GeoObject geoObject = registry.newGeoObjectInstance("State");

    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registry, sJson);
    String sJson2 = geoObject2.toJSON().toString();

    Assert.assertEquals(sJson, sJson2);
  }

  @Test
  public void testGeoObjectType()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", false, registry);

    String sJson = state.toJSON().toString();
    GeoObjectType state2 = GeoObjectType.fromJSON(sJson, registry);
    String sJson2 = state2.toJSON().toString();

    Assert.assertEquals(sJson, sJson2);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGeoObjectCustomAttributes()
  {
    RegistryAdapterServer registryServerInterface = new RegistryAdapterServer(new MockIdService());

    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", false, registryServerInterface);

    AttributeType testChar = AttributeType.factory("testChar", "testCharLocalName", "testCharLocalDescrip", AttributeCharacterType.TYPE);
    AttributeType testDate = AttributeType.factory("testDate", "testDateLocalName", "testDateLocalDescrip", AttributeDateType.TYPE);
    AttributeType testInteger = AttributeType.factory("testInteger", "testIntegerLocalName", "testIntegerLocalDescrip", AttributeIntegerType.TYPE);
    AttributeType testBoolean = AttributeType.factory("testBoolean", "testBooleanName", "testBooleanDescrip", AttributeBooleanType.TYPE);
    AttributeType testTerm = AttributeType.factory("testTerm", "testTermLocalName", "testTermLocalDescrip", AttributeTermType.TYPE);

    ( (AttributeTermType) testTerm ).setRootTerm(registryServerInterface.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.ROOT.code).get());

    state.addAttribute(testChar);
    state.addAttribute(testDate);
    state.addAttribute(testInteger);
    state.addAttribute(testBoolean);
    state.addAttribute(testTerm);

    String geom = "POLYGON ((10000 10000, 12300 40000, 16800 50000, 12354 60000, 13354 60000, 17800 50000, 13300 40000, 11000 10000, 10000 10000))";

    GeoObject geoObject = registryServerInterface.newGeoObjectInstance("State");

    geoObject.setWKTGeometry(geom);
    geoObject.setCode("Colorado");
    geoObject.setUid("CO");

    geoObject.setValue("testChar", "Test Character Value");
    geoObject.setValue("testDate", new Date());
    geoObject.setValue("testInteger", 3);
    geoObject.setValue("testBoolean", false);
    geoObject.setValue("testTerm", registryServerInterface.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.PENDING.code).get());

    String sJson = geoObject.toJSON().toString();
    GeoObject geoObject2 = GeoObject.fromJSON(registryServerInterface, sJson);
    String sJson2 = geoObject2.toJSON().toString();

    Assert.assertEquals(sJson, sJson2);
    Assert.assertEquals(geoObject.getValue("testChar"), geoObject2.getValue("testChar"));
    Assert.assertEquals(geoObject.getValue("testDate"), geoObject2.getValue("testDate"));
    Assert.assertEquals(geoObject.getValue("testInteger"), geoObject2.getValue("testInteger"));
    Assert.assertEquals(geoObject.getValue("testBoolean"), geoObject2.getValue("testBoolean"));
    
    Assert.assertEquals(((Iterator<String>)geoObject.getValue("testTerm")).next(), ((Iterator<String>)geoObject2.getValue("testTerm")).next());
  }

  /**
   * Tests to make sure that custom attributes can be added to GeoObjectTypes,
   * and also that they are serialized correctly.
   */
  @Test
  public void testGeoObjectTypeCustomAttributes()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    GeoObjectType state = MetadataFactory.newGeoObjectType("State", GeometryType.POLYGON, "State", "", false, registry);

    AttributeType testChar = AttributeType.factory("testChar", "testCharLocalName", "testCharLocalDescrip", AttributeCharacterType.TYPE);
    AttributeType testDate = AttributeType.factory("testDate", "testDateLocalName", "testDateLocalDescrip", AttributeDateType.TYPE);
    AttributeType testInteger = AttributeType.factory("testInteger", "testIntegerLocalName", "testDateLocalDescrip", AttributeIntegerType.TYPE);
    AttributeType testTerm = AttributeType.factory("testTerm", "testTermLocalName", "testTermLocalDescrip", AttributeTermType.TYPE);

    state.addAttribute(testChar);
    state.addAttribute(testDate);
    state.addAttribute(testInteger);
    state.addAttribute(testTerm);

    String sJson = state.toJSON().toString();
    GeoObjectType state2 = GeoObjectType.fromJSON(sJson, registry);
    String sJson2 = state2.toJSON().toString();

    Assert.assertEquals(sJson, sJson2);
    Assert.assertEquals(testChar.getName(), state2.getAttribute("testChar").get().getName());
    Assert.assertEquals(testDate.getName(), state2.getAttribute("testDate").get().getName());
    Assert.assertEquals(testInteger.getName(), state2.getAttribute("testInteger").get().getName());
    Assert.assertEquals(testTerm.getName(), state2.getAttribute("testTerm").get().getName());
  }

  @Test
  public void testHierarchyType()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    TestFixture.defineExampleHierarchies(registry);

    HierarchyType geoPolitical = registry.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();

    String geoPoliticalJson = geoPolitical.toJSON().toString();
    HierarchyType geoPolitical2 = HierarchyType.fromJSON(geoPoliticalJson, registry);
    String geoPoliticalJson2 = geoPolitical2.toJSON().toString();

    Assert.assertEquals(geoPoliticalJson, geoPoliticalJson2);
    Assert.assertEquals(geoPolitical.getCode(), geoPolitical2.getCode());
    Assert.assertEquals(geoPolitical.getLocalizedDescription(), geoPolitical2.getLocalizedDescription());
    Assert.assertEquals(geoPolitical.getLocalizedLabel(), geoPolitical2.getLocalizedLabel());
    Assert.assertEquals(geoPolitical.getRootGeoObjectTypes().size(), geoPolitical2.getRootGeoObjectTypes().size());
    Assert.assertEquals(geoPolitical.getRootGeoObjectTypes().get(0).getChildren().size(), geoPolitical2.getRootGeoObjectTypes().get(0).getChildren().size());
  }

  @Test
  public void testChildTreeNode()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    TestFixture.defineExampleHierarchies(registry);
    HierarchyType geoPolitical = registry.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();

    GeoObject pOne = registry.newGeoObjectInstance(TestFixture.PROVINCE);
    pOne.setCode("pOne");
    pOne.setUid("pOne");
    ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

    GeoObject dOne = registry.newGeoObjectInstance(TestFixture.DISTRICT);
    dOne.setCode("dOne");
    dOne.setUid("dOne");
    ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
    ptOne.addChild(dtOne);

    GeoObject cOne = registry.newGeoObjectInstance(TestFixture.COMMUNE);
    cOne.setCode("cOne");
    cOne.setUid("cOne");
    ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
    dtOne.addChild(ctOne);

    GeoObject dTwo = registry.newGeoObjectInstance(TestFixture.DISTRICT);
    dTwo.setCode("dTwo");
    dTwo.setUid("dTwo");
    ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
    ptOne.addChild(dtTwo);

    GeoObject cTwo = registry.newGeoObjectInstance(TestFixture.COMMUNE);
    cTwo.setCode("cTwo");
    cTwo.setUid("cTwo");
    ChildTreeNode ctTwo = new ChildTreeNode(cTwo, geoPolitical);
    ptOne.addChild(ctTwo);

    String ptOneJson = ptOne.toJSON().toString();
    ChildTreeNode ptOne2 = ChildTreeNode.fromJSON(ptOneJson, registry);

    String ptOne2Json = ptOne2.toJSON().toString();

    Assert.assertEquals(ptOneJson, ptOne2Json);
    Assert.assertEquals(ptOne.getChildren().size(), ptOne2.getChildren().size());
    Assert.assertEquals(ptOne.getChildren().get(0).getChildren().size(), ptOne2.getChildren().get(0).getChildren().size());
    Assert.assertEquals(ptOne.getChildren().get(0).getChildren().get(0).getChildren().size(), ptOne2.getChildren().get(0).getChildren().get(0).getChildren().size());
    Assert.assertEquals(ptOne.getHierachyType(), ptOne2.getHierachyType());
    Assert.assertEquals(ptOne.getChildren().get(0).getHierachyType(), ptOne2.getChildren().get(0).getHierachyType());
  }

  @Test
  public void testParentTreeNode()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());

    TestFixture.defineExampleHierarchies(registry);
    HierarchyType geoPolitical = registry.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();

    GeoObject cOne = registry.newGeoObjectInstance(TestFixture.COMMUNE);
    cOne.setCode("cOne");
    cOne.setUid("cOne");
    ParentTreeNode ctOne = new ParentTreeNode(cOne, geoPolitical);

    GeoObject dOne = registry.newGeoObjectInstance(TestFixture.DISTRICT);
    dOne.setCode("dOne");
    dOne.setUid("dOne");
    ParentTreeNode dtOne = new ParentTreeNode(dOne, geoPolitical);
    ctOne.addParent(dtOne);

    GeoObject dTwo = registry.newGeoObjectInstance(TestFixture.DISTRICT);
    dTwo.setCode("dTwo");
    dTwo.setUid("dTwo");
    ParentTreeNode dtTwo = new ParentTreeNode(dTwo, geoPolitical);
    ctOne.addParent(dtTwo);

    GeoObject pOne = registry.newGeoObjectInstance(TestFixture.PROVINCE);
    pOne.setCode("pOne");
    pOne.setUid("pOne");
    ParentTreeNode ptOne = new ParentTreeNode(pOne, geoPolitical);
    dtOne.addParent(ptOne);

    GeoObject pTwo = registry.newGeoObjectInstance(TestFixture.PROVINCE);
    pTwo.setCode("pTwo");
    pTwo.setUid("pTwo");
    ParentTreeNode ptTwo = new ParentTreeNode(pTwo, geoPolitical);
    dtTwo.addParent(ptTwo);

    String ctOneJson = ctOne.toJSON().toString();
    ParentTreeNode ctOne2 = ParentTreeNode.fromJSON(ctOneJson, registry);

    String ctOne2Json = ctOne2.toJSON().toString();

    Assert.assertEquals(ctOneJson, ctOne2Json);
    Assert.assertEquals(ctOne.getParents().size(), ctOne2.getParents().size());
    Assert.assertEquals(ctOne.getParents().get(0).getParents().size(), ctOne2.getParents().get(0).getParents().size());
    Assert.assertEquals(ctOne.getParents().get(0).getParents().get(0).getParents().size(), ctOne2.getParents().get(0).getParents().get(0).getParents().size());
    Assert.assertEquals(ctOne.getHierachyType(), ctOne2.getHierachyType());
    Assert.assertEquals(ctOne.getParents().get(0).getHierachyType(), ctOne2.getParents().get(0).getHierachyType());
  }

  @Test
  public void testActions()
  {
    RegistryAdapterServer registry = new RegistryAdapterServer(new MockIdService());
    TestFixture.defineExampleHierarchies(registry);
    GeoObject geoObj1 = TestFixture.createGeoObject(registry, "PROV_ONE", TestFixture.PROVINCE);
    GeoObject geoObj2 = TestFixture.createGeoObject(registry, "DIST_ONE", TestFixture.DISTRICT);
    GeoObjectType province = geoObj1.getType();
    HierarchyType geoPolitical = registry.getMetadataCache().getHierachyType(TestFixture.GEOPOLITICAL).get();

    List<AbstractActionDTO> actions = new ArrayList<AbstractActionDTO>();
    int i = 0;

    /*
     *  Add Child
     */
    AddChildActionDTO addChild = new AddChildActionDTO();
    addChild.setParentId(geoObj1.getUid());
    addChild.setParentTypeCode(geoObj1.getType().getCode());
    addChild.setChildId(geoObj2.getUid());
    addChild.setChildTypeCode(geoObj2.getType().getCode());
    addChild.setHierarchyCode(geoPolitical.getCode());
    
    String addChildJson = addChild.toJSON().toString();
    String addChildJson2 = AbstractActionDTO.parseAction(addChildJson).toJSON().toString();
    Assert.assertEquals(addChildJson, addChildJson2);
    actions.add(addChild);

    // Add Parent TODO
    
    // Remove Child ??
    // TODO

    /*
     *  Create a GeoObject
     */
    CreateGeoObjectActionDTO create = new CreateGeoObjectActionDTO();
    create.setGeoObject(geoObj1.toJSON());
    
    String createJson = create.toJSON().toString();
    String createJson2 = AbstractActionDTO.parseAction(createJson).toJSON().toString();
    Assert.assertEquals(createJson, createJson2);
    actions.add(create);

    /*
     *  Update a GeoObject
     */
    UpdateGeoObjectActionDTO update = new UpdateGeoObjectActionDTO();
    update.setGeoObject(geoObj1.toJSON());
    
    String updateJson = update.toJSON().toString();
    String updateJson2 = AbstractActionDTO.parseAction(updateJson).toJSON().toString();
    Assert.assertEquals(updateJson, updateJson2);
    actions.add(create);

    /*
     *  Update a GeoObjectType
     */
    UpdateGeoObjectActionDTO createGOT = new UpdateGeoObjectActionDTO();
    createGOT.setGeoObject(province.toJSON());
    
    String createGOTJson = createGOT.toJSON().toString();
    String createGOTJson2 = AbstractActionDTO.parseAction(createGOTJson).toJSON().toString();
    Assert.assertEquals(createGOTJson, createGOTJson2);
    actions.add(createGOT);

    /*
     *  Serialize the actions
     */
    String sActions = AbstractActionDTO.serializeActions(actions).toString();
    String sActions2 = AbstractActionDTO.serializeActions(AbstractActionDTO.parseActions(sActions)).toString();
    Assert.assertEquals(sActions, sActions2);
    
    System.out.println(sActions);
  }
}
