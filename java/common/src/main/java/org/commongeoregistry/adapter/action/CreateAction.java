package org.commongeoregistry.adapter.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CreateAction extends AbstractAction
{
    private static final String JSON_KEY_OBJ_JSON = "objjson";

    private static final String JSON_KEY_OBJ_TYPE = "objtype";

    private final JsonObject objJson;

    private final String objType;

    public CreateAction(String objType, JsonObject objJson)
    {
        this.objJson = objJson;
        this.objType = objType;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();

        json.add(JSON_KEY_OBJ_JSON, this.objJson);

        json.addProperty(JSON_KEY_OBJ_JSON, this.objType);
        
        json.addProperty(JSON_KEY_ACTION_TYPE, this.getClass().getName());

        return json;
    }

    public static CreateAction fromJSON(String json)
    {
        JsonParser parser = new JsonParser();
        JsonObject oJson = parser.parse(json).getAsJsonObject();

        String objType = oJson.get(JSON_KEY_OBJ_TYPE).getAsString();
        JsonObject objJson = oJson.get(JSON_KEY_OBJ_JSON).getAsJsonObject();

        CreateAction ca = new CreateAction(objType, objJson);

        return ca;
    }

    public JsonObject getObjJson() {
      return objJson;
    }

    public String getObjType() {
      return objType;
    }
}
