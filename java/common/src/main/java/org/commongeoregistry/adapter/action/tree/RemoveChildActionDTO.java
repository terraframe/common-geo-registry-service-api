package org.commongeoregistry.adapter.action.tree;

import org.commongeoregistry.adapter.action.AbstractActionDTO;
import org.commongeoregistry.adapter.constants.RegistryUrls;

import com.google.gson.JsonObject;

public class RemoveChildActionDTO extends AbstractActionDTO
{
    private String childId;
    
    private String childTypeCode;

    private String parentId;

    private String hierarchyCode;
    
    private String parentTypeCode;
    
    public RemoveChildActionDTO()
    {
      super(RegistryUrls.GEO_OBJECT_REMOVE_CHILD);
    }
    
    @Override
    protected void buildJson(JsonObject json)
    {
      super.buildJson(json);
      
      json.addProperty(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_CHILDID, this.childId);
      json.addProperty(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_CHILD_TYPE_CODE, this.childTypeCode);
      json.addProperty(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_PARENTID, this.parentId);
      json.addProperty(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_PARENT_TYPE_CODE, this.parentTypeCode);
      json.addProperty(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_HIERARCHY_CODE, this.hierarchyCode);
    }
    
    @Override
    protected void buildFromJson(JsonObject json)
    {
      super.buildFromJson(json);
      
      this.childId = json.get(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_CHILDID).getAsString();
      this.childTypeCode = json.get(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_CHILD_TYPE_CODE).getAsString();
      this.parentId = json.get(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_PARENTID).getAsString();
      this.parentTypeCode = json.get(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_PARENT_TYPE_CODE).getAsString();
      this.hierarchyCode = json.get(RegistryUrls.GEO_OBJECT_REMOVE_CHILD_PARAM_HIERARCHY_CODE).getAsString();
    }
    
    public void setChildId(String childId)
    {
      this.childId = childId;
    }

    public void setChildTypeCode(String childTypeCode)
    {
      this.childTypeCode = childTypeCode;
    }

    public void setParentId(String parentId)
    {
      this.parentId = parentId;
    }

    public void setHierarchyCode(String hierarchyCode)
    {
      this.hierarchyCode = hierarchyCode;
    }

    public void setParentTypeCode(String parentTypeCode)
    {
      this.parentTypeCode = parentTypeCode;
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
