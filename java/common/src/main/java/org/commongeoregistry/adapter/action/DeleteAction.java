package org.commongeoregistry.adapter.action;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeleteAction extends AbstractAction
{
    private static final String JSON_KEY_OBJ_TYPE = "objtype";

    private static final String JSON_KEY_OBJ_OID = "objoid";

    private final String objType;

    private final String objOid;

    public DeleteAction(GeoObject go)
    {
      this.objType = go.getClass().getName();
      this.objOid = go.getUid();
    }
    
    public DeleteAction(GeoObjectType type)
    {
      this.objType = type.getClass().getName();
      this.objOid = type.getCode();
    }
    
    private DeleteAction(String objType, String objOid)
    {
      this.objType = objType;
      this.objOid = objOid;
    }

    @Override
    public JsonObject toJSON()
    {
        JsonObject json = new JsonObject();

        json.addProperty(JSON_KEY_OBJ_TYPE, this.objType);

        json.addProperty(JSON_KEY_OBJ_OID, this.objOid);
        
        json.addProperty(JSON_KEY_ACTION_TYPE, this.getClass().getName());

        return json;
    }

    public static DeleteAction fromJSON(String json)
    {
        JsonParser parser = new JsonParser();
        JsonObject oJson = parser.parse(json).getAsJsonObject();

        String objType = oJson.get(JSON_KEY_OBJ_TYPE).getAsString();
        String objOid = oJson.get(JSON_KEY_OBJ_OID).getAsString();

        DeleteAction da = new DeleteAction(objType, objOid);

        return da;
    }

    public String getObjType() {
      return objType;
    }

    public String getObjOid() {
      return objOid;
    }
}
