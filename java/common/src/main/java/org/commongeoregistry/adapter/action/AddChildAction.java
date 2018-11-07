package org.commongeoregistry.adapter.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddChildAction extends AbstractAction
{
    public static final String JSON_KEY_CHILD_ID = "childoid";

    public static final String JSON_KEY_PARENT_ID = "parentoid";

    private static final String JSON_KEY_HIERARCHY_ID = "hiearchyoid";

    private final String childId;

    private final String parentId;

    private final String hierarchyId;

    public AddChildAction(String childId, String parentId, String hierarchyId)
    {
      this.childId = childId;
      this.parentId = parentId;
      this.hierarchyId = hierarchyId;
    }

    @Override
    public JsonObject toJSON()
    {
      JsonObject json = new JsonObject();

      json.addProperty(JSON_KEY_CHILD_ID, this.childId);

      json.addProperty(JSON_KEY_PARENT_ID, this.parentId);
      
      json.addProperty(JSON_KEY_HIERARCHY_ID, this.hierarchyId);
      
      json.addProperty(JSON_KEY_ACTION_TYPE, this.getClass().getName());

      return json;
    }

    public static AddChildAction fromJSON(String json)
    {
      JsonParser parser = new JsonParser();
      JsonObject oJson = parser.parse(json).getAsJsonObject();

      String childId = oJson.get(JSON_KEY_CHILD_ID).getAsString();
      String parentId = oJson.get(JSON_KEY_PARENT_ID).getAsString();
      String hierarchyId = oJson.get(JSON_KEY_HIERARCHY_ID).getAsString();

      AddChildAction aca = new AddChildAction(childId, parentId, hierarchyId);

      return aca;
    }

    public String getChildId()
    {
      return childId;
    }

    public String getParentId()
    {
      return parentId;
    }

    public String getHierarchyId()
    {
      return hierarchyId;
    }
}
