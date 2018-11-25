package org.commongeoregistry.adapter.action;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CreateAction extends AbstractAction
{
    private static final String JSON_KEY_OBJ_JSON = "objjson";

    private static final String JSON_KEY_OBJ_TYPE = "objtype";

    private final JsonObject objJson;

    private final String objType;

    public CreateAction(GeoObject go)
    {
      this.objType = go.getClass().getName();
      this.objJson = go.toJSON();
    }
    
    public CreateAction(GeoObjectType type)
    {
      this.objType = type.getClass().getName();
      this.objJson = type.toJSON();
    }
    
    private CreateAction(String objType, JsonObject objJson)
    {
      this.objType = objType;
      this.objJson = objJson;
    }

    @Override
    public JsonObject toJSON()
    {
      JsonObject json = new JsonObject();

      json.add(JSON_KEY_OBJ_JSON, this.objJson);

      json.addProperty(JSON_KEY_OBJ_TYPE, this.objType);
      
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
