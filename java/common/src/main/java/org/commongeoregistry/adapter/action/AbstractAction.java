package org.commongeoregistry.adapter.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

abstract public class AbstractAction
{
  public static final String JSON_KEY_ACTION_TYPE = "actiontype";
  
  abstract public JsonObject toJSON();
  
  public static JsonArray serializeActions(AbstractAction[] actions)
  {
    JsonArray ja = new JsonArray();
    
    for (int i = 0; i < actions.length; ++i)
    {
      ja.add(actions[i].toJSON());
    }
    
    return ja;
  }
  
  public static AbstractAction[] parseActions(String json) {
    JsonParser parser = new JsonParser();

    JsonArray aJson = parser.parse(json).getAsJsonArray();

    AbstractAction[] actions = new AbstractAction[aJson.size()];
    
    for (int i = 0; i < aJson.size(); ++i) {
      JsonObject oJson = aJson.get(i).getAsJsonObject();
      
      String actionType = oJson.get(JSON_KEY_ACTION_TYPE).getAsString();
      
      if (actionType.equals(AddChildAction.class.getName()))
      {
        actions[i] = AddChildAction.fromJSON(oJson.toString());
      }
      else if (actionType.equals(UpdateAction.class.getName()))
      {
        actions[i] = UpdateAction.fromJSON(oJson.toString());
      }
      else if (actionType.equals(CreateAction.class.getName()))
      {
        actions[i] = CreateAction.fromJSON(oJson.toString());
      }
      else if (actionType.equals(DeleteAction.class.getName()))
      {
        actions[i] = DeleteAction.fromJSON(oJson.toString());
      }
      else
      {
        throw new UnsupportedOperationException(actionType);
      }
    }
    
    return actions;
  }
}
