package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.commongeoregistry.adapter.HttpRegistryClient;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.MetadataFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LocalObjectCacheTest {
    public static String PROVINCE = "PROVINCE";

    public static String DISTRICT = "DISTRICT";

    public static String COMMUNE = "COMMUNE";

    public static String VILLAGE = "VILLAGE";

    public static String HOUSEHOLD = "HOUSEHOLD";

    public static String FOCUS_AREA = "FOCUS_AREA";

    public static String HEALTH_FACILITY = "HEALTH_FACILITY";

    public static String HEALTH_FACILITY_ATTRIBUTE = "healthFacilityType";

    public static String GEOPOLITICAL = "GEOPOLITICAL";

    public static String HEALTH_ADMINISTRATIVE = "HEALTH_ADMINISTRATIVE";

    private HttpRegistryClient client;

    private LocalObjectCache cache;
    private HierarchyType geoPolitical;

    @Before
    public void setup() {
        /*
         * Setup mock objects
         */
        MockHttpConnector connector = new MockHttpConnector();
        this.client = new HttpRegistryClient(connector);

        Context context = InstrumentationRegistry.getTargetContext();

        this.cache = new LocalObjectCache(context, this.client);
        cache.clear();

        // Define GeoObject Types
        GeoObjectType province = MetadataFactory.newGeoObjectType(PROVINCE, GeometryType.POLYGON, "Province", "", client);

        GeoObjectType district = MetadataFactory.newGeoObjectType(DISTRICT, GeometryType.POLYGON, "District", "", client);

        GeoObjectType commune = MetadataFactory.newGeoObjectType(COMMUNE, GeometryType.POLYGON, "Commune", "", client);

        GeoObjectType village = MetadataFactory.newGeoObjectType(VILLAGE, GeometryType.POLYGON, "Village", "", client);

        GeoObjectType household = MetadataFactory.newGeoObjectType(HOUSEHOLD, GeometryType.POLYGON, "Household", "", client);

        GeoObjectType focusArea = MetadataFactory.newGeoObjectType(FOCUS_AREA, GeometryType.POLYGON, "Focus Area", "", client);

        GeoObjectType healthFacility = MetadataFactory.newGeoObjectType(HEALTH_FACILITY, GeometryType.POLYGON, "Health Facility", "", client);
        healthFacility.addAttribute(createHealthFacilityTypeAttribute(client));

        // Define Geopolitical Hierarchy Type
        this.geoPolitical = MetadataFactory.newHierarchyType(GEOPOLITICAL, "Geopolitical", "Geopolitical Hierarchy", client);
        HierarchyType.HierarchyNode geoProvinceNode = new HierarchyType.HierarchyNode(province);
        HierarchyType.HierarchyNode geoDistrictNode = new HierarchyType.HierarchyNode(district);
        HierarchyType.HierarchyNode geoCommuneNode = new HierarchyType.HierarchyNode(commune);
        HierarchyType.HierarchyNode geoVillageNode = new HierarchyType.HierarchyNode(village);
        HierarchyType.HierarchyNode geoHouseholdNode = new HierarchyType.HierarchyNode(household);

        geoProvinceNode.addChild(geoDistrictNode);
        geoDistrictNode.addChild(geoCommuneNode);
        geoCommuneNode.addChild(geoVillageNode);
        geoVillageNode.addChild(geoHouseholdNode);

        geoPolitical.addRootGeoObjects(geoProvinceNode);
    }

    private AttributeTermType createHealthFacilityTypeAttribute(HttpRegistryClient client) {
        AttributeTermType attrType =
                (AttributeTermType) AttributeType.factory(HEALTH_FACILITY_ATTRIBUTE, "Health Facility Type", "The type of health facility", AttributeTermType.TYPE);

        Term rootTerm = createHealthFacilityTerms(client);

        attrType.setRootTerm(rootTerm);

        return attrType;
    }

    private static Term createHealthFacilityTerms(HttpRegistryClient registry) {
        Term rootTerm = MetadataFactory.newTerm("CM:Health-Facility-Types", "Health Facility Types", "The types of health facilities within a country", registry);
        Term dispensary = MetadataFactory.newTerm("CM:Dispensary", "Dispensary", "", registry);
        Term privateClinic = MetadataFactory.newTerm("CM:Private-Clinic", "Private Clinic", "", registry);
        Term publicClinic = MetadataFactory.newTerm("CM:Public-Clinic", "Public Clinic", "", registry);
        Term matWard = MetadataFactory.newTerm("CM:Maternity-Ward", "Maternity Ward", "", registry);
        Term nursing = MetadataFactory.newTerm("CM:Nursing-Home", "Nursing Home", "", registry);

        rootTerm.addChild(dispensary);
        rootTerm.addChild(privateClinic);
        rootTerm.addChild(publicClinic);
        rootTerm.addChild(matWard);
        rootTerm.addChild(nursing);

        return rootTerm;
    }


    @Test
    public void testCacheAndGetGeoObject() {

        /*
         * Setup mock objects
         */
        GeoObject geoObject = client.newGeoObjectInstance(DISTRICT);
        geoObject.setCode("Test");
        geoObject.setUid("state1");

        try {
            cache.cache(geoObject);

            GeoObject test = cache.getGeoObject(geoObject.getUid());

            Assert.assertNotNull(test);
        } finally {
            cache.close();
        }
    }

    @Test
    public void testCacheAndGetChildGeoObjects() {

        /*
         * Setup mock objects
         */
        GeoObject pOne = client.newGeoObjectInstance(PROVINCE);
        pOne.setCode("pOne");
        pOne.setUid("pOne");
        ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

        GeoObject dOne = client.newGeoObjectInstance(DISTRICT);
        dOne.setCode("dOne");
        dOne.setUid("dOne");
        ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
        ptOne.addChild(dtOne);

        GeoObject cOne = client.newGeoObjectInstance(COMMUNE);
        cOne.setCode("cOne");
        cOne.setUid("cOne");
        ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
        dtOne.addChild(ctOne);

        GeoObject dTwo = client.newGeoObjectInstance(DISTRICT);
        dTwo.setCode("dTwo");
        dTwo.setUid("dTwo");
        ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
        ptOne.addChild(dtTwo);

        try {
            cache.cache(ptOne);

            ChildTreeNode test = cache.getChildGeoObjects(pOne.getUid(), new String[]{geoPolitical.getCode()}, false);

            Assert.assertNotNull(test);

            Assert.assertEquals(pOne.getUid(), test.getGeoObject().getUid());

            List<ChildTreeNode> children = test.getChildren();

            Assert.assertEquals(2, children.size());

            ChildTreeNode tPtOne = children.get(0);

            Assert.assertEquals(dOne.getUid(), tPtOne.getGeoObject().getUid());

            children = tPtOne.getChildren();

            Assert.assertEquals(0, children.size());
        } finally {
            cache.close();
        }
    }

    @Test
    public void testGetChildGeoObjectsBadHierarchy() {

        /*
         * Setup mock objects
         */
        GeoObject pOne = client.newGeoObjectInstance(PROVINCE);
        pOne.setCode("pOne");
        pOne.setUid("pOne");
        ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

        GeoObject dOne = client.newGeoObjectInstance(DISTRICT);
        dOne.setCode("dOne");
        dOne.setUid("dOne");
        ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
        ptOne.addChild(dtOne);

        GeoObject cOne = client.newGeoObjectInstance(COMMUNE);
        cOne.setCode("cOne");
        cOne.setUid("cOne");
        ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
        dtOne.addChild(ctOne);

        GeoObject dTwo = client.newGeoObjectInstance(DISTRICT);
        dTwo.setCode("dTwo");
        dTwo.setUid("dTwo");
        ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
        ptOne.addChild(dtTwo);

        try {
            cache.cache(ptOne);

            ChildTreeNode test = cache.getChildGeoObjects(pOne.getUid(), new String[]{"NONE"}, false);

            Assert.assertNotNull(test);

            Assert.assertEquals(pOne.getUid(), test.getGeoObject().getUid());

            List<ChildTreeNode> children = test.getChildren();

            Assert.assertEquals(0, children.size());
        } finally {
            cache.close();
        }
    }


    @Test
    public void testGetChildGeoObjectsRecursive() {

        /*
         * Setup mock objects
         */
        GeoObject pOne = client.newGeoObjectInstance(PROVINCE);
        pOne.setCode("pOne");
        pOne.setUid("pOne");
        ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

        GeoObject dOne = client.newGeoObjectInstance(DISTRICT);
        dOne.setCode("dOne");
        dOne.setUid("dOne");
        ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
        ptOne.addChild(dtOne);

        GeoObject cOne = client.newGeoObjectInstance(COMMUNE);
        cOne.setCode("cOne");
        cOne.setUid("cOne");
        ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
        dtOne.addChild(ctOne);

        GeoObject dTwo = client.newGeoObjectInstance(DISTRICT);
        dTwo.setCode("dTwo");
        dTwo.setUid("dTwo");
        ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
        ptOne.addChild(dtTwo);

        try {
            cache.cache(ptOne);

            ChildTreeNode test = cache.getChildGeoObjects(pOne.getUid(), new String[]{geoPolitical.getCode()}, true);

            Assert.assertNotNull(test);

            Assert.assertEquals(pOne.getUid(), test.getGeoObject().getUid());

            List<ChildTreeNode> children = test.getChildren();

            Assert.assertEquals(2, children.size());

            ChildTreeNode tPtOne = children.get(0);

            Assert.assertEquals(dOne.getUid(), tPtOne.getGeoObject().getUid());

            children = tPtOne.getChildren();

            Assert.assertEquals(1, children.size());

            ChildTreeNode tDtOne = children.get(0);

            Assert.assertEquals(cOne.getUid(), tDtOne.getGeoObject().getUid());
        } finally {
            cache.close();
        }
    }

    @Test
    public void testGetParentGeoObjects() {

        /*
         * Setup mock objects
         */
        GeoObject pOne = client.newGeoObjectInstance(PROVINCE);
        pOne.setCode("pOne");
        pOne.setUid("pOne");
        ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

        GeoObject dOne = client.newGeoObjectInstance(DISTRICT);
        dOne.setCode("dOne");
        dOne.setUid("dOne");
        ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
        ptOne.addChild(dtOne);

        GeoObject cOne = client.newGeoObjectInstance(COMMUNE);
        cOne.setCode("cOne");
        cOne.setUid("cOne");
        ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
        dtOne.addChild(ctOne);

        GeoObject dTwo = client.newGeoObjectInstance(DISTRICT);
        dTwo.setCode("dTwo");
        dTwo.setUid("dTwo");
        ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
        ptOne.addChild(dtTwo);

        try {
            cache.cache(ptOne);

            ParentTreeNode test = cache.getParentGeoObjects(cOne.getUid(), new String[]{geoPolitical.getCode()}, false);

            Assert.assertNotNull(test);

            Assert.assertEquals(cOne.getUid(), test.getGeoObject().getUid());

            List<ParentTreeNode> parents = test.getParents();

            Assert.assertEquals(1, parents.size());

            ParentTreeNode tDtOne = parents.get(0);

            Assert.assertEquals(dOne.getUid(), tDtOne.getGeoObject().getUid());

            parents = tDtOne.getParents();

            Assert.assertEquals(0, parents.size());
        } finally {
            cache.close();
        }
    }

    @Test
    public void testGetParentGeoObjectsBadHierarchy() {

        /*
         * Setup mock objects
         */
        GeoObject pOne = client.newGeoObjectInstance(PROVINCE);
        pOne.setCode("pOne");
        pOne.setUid("pOne");
        ChildTreeNode ptOne = new ChildTreeNode(pOne, geoPolitical);

        GeoObject dOne = client.newGeoObjectInstance(DISTRICT);
        dOne.setCode("dOne");
        dOne.setUid("dOne");
        ChildTreeNode dtOne = new ChildTreeNode(dOne, geoPolitical);
        ptOne.addChild(dtOne);

        GeoObject cOne = client.newGeoObjectInstance(COMMUNE);
        cOne.setCode("cOne");
        cOne.setUid("cOne");
        ChildTreeNode ctOne = new ChildTreeNode(cOne, geoPolitical);
        dtOne.addChild(ctOne);

        GeoObject dTwo = client.newGeoObjectInstance(DISTRICT);
        dTwo.setCode("dTwo");
        dTwo.setUid("dTwo");
        ChildTreeNode dtTwo = new ChildTreeNode(dTwo, geoPolitical);
        ptOne.addChild(dtTwo);


        // Context of the app under test.

        try {
            cache.cache(ptOne);

            ParentTreeNode test = cache.getParentGeoObjects(dTwo.getUid(), new String[]{"NONE"}, false);

            Assert.assertNotNull(test);

            Assert.assertEquals(dTwo.getUid(), test.getGeoObject().getUid());

            List<ParentTreeNode> parents = test.getParents();

            Assert.assertEquals(0, parents.size());
        } finally {
            cache.close();
        }
    }


    @Test
    public void testCacheAndGetParentGeoObjectsRecursive() {

        /*
         * Setup mock objects
         */
        GeoObject pOne = client.newGeoObjectInstance(PROVINCE);
        pOne.setCode("pOne");
        pOne.setUid("pOne");
        ParentTreeNode ptOne = new ParentTreeNode(pOne, geoPolitical);

        GeoObject dOne = client.newGeoObjectInstance(DISTRICT);
        dOne.setCode("dOne");
        dOne.setUid("dOne");
        ParentTreeNode dtOne = new ParentTreeNode(dOne, geoPolitical);
        dtOne.addParent(ptOne);

        GeoObject cOne = client.newGeoObjectInstance(COMMUNE);
        cOne.setCode("cOne");
        cOne.setUid("cOne");
        ParentTreeNode ctOne = new ParentTreeNode(cOne, geoPolitical);
        ctOne.addParent(dtOne);

        try {
            cache.cache(ctOne);

            ParentTreeNode test = cache.getParentGeoObjects(cOne.getUid(), new String[]{geoPolitical.getCode()}, true);

            Assert.assertNotNull(test);

            Assert.assertEquals(cOne.getUid(), test.getGeoObject().getUid());

            List<ParentTreeNode> parents = test.getParents();

            Assert.assertEquals(1, parents.size());

            ParentTreeNode tDtOne = parents.get(0);

            Assert.assertEquals(dOne.getUid(), tDtOne.getGeoObject().getUid());

            parents = tDtOne.getParents();

            Assert.assertEquals(1, parents.size());

            ParentTreeNode tPtOne = parents.get(0);

            Assert.assertEquals(pOne.getUid(), tPtOne.getGeoObject().getUid());
        } finally {
            cache.close();
        }
    }


}
