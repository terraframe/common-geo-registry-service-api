package org.commongeoregistry.adapter.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddChildAction extends AbstractAction
{
    public static final String JSON_KEY_CHILD_ID = "childoid";

    public static final String JSON_KEY_PARENT_ID = "parentoid";

    private final String childId;

    private final String parentId;

    public AddChildAction(String childId, String parentId)
    {
        this.childId = childId;
        this.parentId = parentId;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();

        json.addProperty(JSON_KEY_CHILD_ID, this.childId);

        json.addProperty(JSON_KEY_PARENT_ID, this.parentId);
        
        json.addProperty(JSON_KEY_ACTION_TYPE, this.getClass().getName());

        return json;
    }

    public static AddChildAction fromJSON(String json)
    {
        JsonParser parser = new JsonParser();
        JsonObject oJson = parser.parse(json).getAsJsonObject();

        String childId = oJson.get(JSON_KEY_CHILD_ID).getAsString();
        String parentId = oJson.get(JSON_KEY_PARENT_ID).getAsString();

        AddChildAction aca = new AddChildAction(childId, parentId);

        return aca;
    }

    public String getChildId() {
      return childId;
    }

    public String getParentId() {
      return parentId;
    }
}
