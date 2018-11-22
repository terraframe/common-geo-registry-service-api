package org.commongeoregistry.adapter.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddChildAction extends AbstractAction
{
    public static final String JSON_KEY_CHILD_ID = "childid";
    
    public static final String JSON_KEY_CHILD_TYPE_CODE = "childtypecode";

    public static final String JSON_KEY_PARENT_ID = "parentid";
    
    public static final String JSON_KEY_PARENT_TYPE_CODE = "parenttypecode";

    private static final String JSON_KEY_HIERARCHY_CODE = "hiearchycode";

    private final String childId;
    
    private final String childTypeCode;

    private final String parentId;

    private final String hierarchyCode;
    
    private final String parentTypeCode;

    public AddChildAction(String childId, String childTypeCode, String parentId, String parentTypeCode, String hierarchyId)
    {
      this.childId = childId;
      this.childTypeCode = childTypeCode;
      this.parentId = parentId;
      this.parentTypeCode = parentTypeCode;
      this.hierarchyCode = hierarchyId;
    }

    @Override
    public JsonObject toJSON()
    {
      JsonObject json = new JsonObject();

      json.addProperty(JSON_KEY_CHILD_ID, this.childId);
      
      json.addProperty(JSON_KEY_CHILD_TYPE_CODE, this.childTypeCode);

      json.addProperty(JSON_KEY_PARENT_ID, this.parentId);
      
      json.addProperty(JSON_KEY_PARENT_TYPE_CODE, this.parentTypeCode);
      
      json.addProperty(JSON_KEY_HIERARCHY_CODE, this.hierarchyCode);
      
      json.addProperty(JSON_KEY_ACTION_TYPE, this.getClass().getName());

      return json;
    }

    public static AddChildAction fromJSON(String json)
    {
      JsonParser parser = new JsonParser();
      JsonObject oJson = parser.parse(json).getAsJsonObject();

      String childId = oJson.get(JSON_KEY_CHILD_ID).getAsString();
      String childTypeCode = oJson.get(JSON_KEY_CHILD_TYPE_CODE).getAsString();
      String parentId = oJson.get(JSON_KEY_PARENT_ID).getAsString();
      String parentTypeCode = oJson.get(JSON_KEY_PARENT_TYPE_CODE).getAsString();
      String hierarchyCode = oJson.get(JSON_KEY_HIERARCHY_CODE).getAsString();

      AddChildAction aca = new AddChildAction(childId, childTypeCode, parentId, parentTypeCode, hierarchyCode);

      return aca;
    }

    public String getChildId()
    {
      return childId;
    }
    
    public String getChildTypeCode()
    {
      return childTypeCode;
    }

    public String getParentId()
    {
      return parentId;
    }
    
    public String getParentTypeCode()
    {
      return parentTypeCode;
    }

    public String getHierarchyCode()
    {
      return hierarchyCode;
    }
}
