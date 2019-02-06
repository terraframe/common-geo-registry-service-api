package org.commongeoregistry.adapter.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.commongeoregistry.adapter.constants.CGRAdapterProperties;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

abstract public class AbstractActionDTO
{
  public static final String ACTION_TYPE = "actionType";
  
  public static final String API_VERSION = "apiVersion";
  
  public static final String CREATE_ACTION_DATE = "createActionDate";
  
  private String apiVersion;
  
  private Date createActionDate;

  private String actionType;
  
  public AbstractActionDTO(String actionType)
  {
    this.apiVersion = CGRAdapterProperties.getApiVersion();
    this.createActionDate = new Date();
    this.actionType = actionType;
  }
  
  public static JsonArray serializeActions(List<AbstractActionDTO> actions)
  {
    JsonArray ja = new JsonArray();
    
    for (int i = 0; i < actions.size(); ++i)
    {
      ja.add(actions.get(i).toJSON());
    }
    
    return ja;
  }
  
  public static List<AbstractActionDTO> parseActions(String jsonArray) {
    JsonParser parser = new JsonParser();

    JsonArray aJson = parser.parse(jsonArray).getAsJsonArray();

    List<AbstractActionDTO> actions = new ArrayList<AbstractActionDTO>(aJson.size());
    
    for (int i = 0; i < aJson.size(); ++i) {
      JsonObject oJson = aJson.get(i).getAsJsonObject();
      
      actions.add(parseAction(oJson.toString()));
    }
    
    return actions;
  }
  
  public static AbstractActionDTO parseAction(String jsonObject)
  {
    JsonParser parser = new JsonParser();
    JsonObject oJson = parser.parse(jsonObject).getAsJsonObject();
    String actionType = oJson.get(ACTION_TYPE).getAsString();
    
    AbstractActionDTO action = ActionDTOFactory.newAction(actionType);
    
    action.buildFromJson(oJson);
    
    return action;
  }
  
  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    this.buildJson(json);
    
    return json;
  }
  
  protected void buildFromJson(JsonObject json)
  {
    this.apiVersion = json.get(API_VERSION).getAsString();
    
    this.createActionDate = new Date(json.get(CREATE_ACTION_DATE).getAsLong());
  }
  
  protected void buildJson(JsonObject json)
  {
    json.addProperty(ACTION_TYPE, this.actionType);
    
    json.addProperty(API_VERSION, this.apiVersion);
    
    json.addProperty(CREATE_ACTION_DATE, this.createActionDate.getTime());
  }

  public String getApiVersion()
  {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion)
  {
    this.apiVersion = apiVersion;
  }

  public Date getCreateActionDate()
  {
    return createActionDate;
  }

  public void setCreateActionDate(Date createActionDate)
  {
    this.createActionDate = createActionDate;
  }

  public String getActionType()
  {
    return this.actionType;
  }
}
