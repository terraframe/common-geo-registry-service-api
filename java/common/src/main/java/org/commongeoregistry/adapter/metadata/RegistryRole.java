package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RegistryRole implements Serializable
{

  public enum Type 
  {
    SRA(),
    
    RA(),

    RM(),
    
    RC(),
    
    AC();
   
    public static final String REGISTRY_ROLE_PREFIX             = "commongeoregistry";
    
    public static final String REGISTRY_ROOT_ORG_ROLE           = REGISTRY_ROLE_PREFIX + ".Org";

    /**
     * Returns the {@link RegistryRole} name for the Super Registry Administrator.
     * 
     * 
     * @return the {@link RegistryRole} name for the Super Registry Administrator.
     */
    public static String getSRA_RoleName()
    {
      return REGISTRY_ROLE_PREFIX + "." + Type.SRA.name();
    }

    /**
     * Constructs a role name for the {@link OrganizationDTO} with the given code.
     * 
     * @param organizationCode
     *          {@link OrganizationDTO} code.
     * 
     * @return role name for the {@link OrganizationDTO} with the given code.
     */
    public static String getRoleName(String organizationCode)
    {
      return REGISTRY_ROOT_ORG_ROLE + "." + organizationCode;
    }
    
    /**
     * Constructs a {@link RegistryRole} for the Registry Administrator for the
     * {@link OrganizationDTO} with the given code.
     * 
     * @param organizationCode
     *          {@link OrganizationDTO} code.
     * 
     * @return {@link RegistryRole} name for the Registry Administrator for the
     *         {@link OrganizationDTO} with the given code.
     */
    public static String getRA_RoleName(String organizationCode)
    {
      String organizationRoleName = RegistryRole.Type.getRoleName(organizationCode);

      return organizationRoleName + "." + Type.RA.name();
    }
    
    /**
     * Constructs a {@link RegistryRole} for the Registry Maintainer for the
     * {@link OrganizationDTO} with the given code and {@link GeoObjectType} with the given code.
     * 
     * @param organizationCode
     *          {@link OrganizationDTO} code.
     *          
     * @param geoObjectTypeCode
     *          {@link GeoObjectType} code.
     * 
     * @return {@link RegistryRole} name for the Registry Maintainer for the
     * {@link OrganizationDTO} with the given code and {@link GeoObjectType} with the given code.
     */
    public static String getRM_RoleName(String organizationCode, String geoObjectTypeCode)
    {
      String organizationRoleName = RegistryRole.Type.getRoleName(organizationCode);

      return organizationRoleName + "." + geoObjectTypeCode+"."+Type.RM.name();
    }
    
    /**
     * Constructs a {@link RegistryRole} for the Registry Contributor for the
     * {@link OrganizationDTO} with the given code and {@link GeoObjectType} with the given code.
     * 
     * @param organizationCode
     *          {@link OrganizationDTO} code.
     *          
     * @param geoObjectTypeCode
     *          {@link GeoObjectType} code.
     * 
     * @return {@link RegistryRole} name for the Registry Contributor for the
     * {@link OrganizationDTO} with the given code and {@link GeoObjectType} with the given code.
     */
    public static String getRC_RoleName(String organizationCode, String geoObjectTypeCode)
    {
      String organizationRoleName = RegistryRole.Type.getRoleName(organizationCode);

      return organizationRoleName + "." + geoObjectTypeCode+"."+Type.RC.name();
    }
    
    /**
     * Constructs a {@link RegistryRole} for the API Contributor for the
     * {@link OrganizationDTO} with the given code and {@link GeoObjectType} with the given code.
     * 
     * @param organizationCode
     *          {@link OrganizationDTO} code.
     *          
     * @param geoObjectTypeCode
     *          {@link GeoObjectType} code.
     * 
     * @return {@link RegistryRole} name for the API Contributor for the
     * {@link OrganizationDTO} with the given code and {@link GeoObjectType} with the given code.
     */
    public static String getAC_RoleName(String organizationCode, String geoObjectTypeCode)
    {
      String organizationRoleName = RegistryRole.Type.getRoleName(organizationCode);

      return organizationRoleName + "." + geoObjectTypeCode+"."+Type.AC.name();
    }
  }

  
  /**
   * 
   */
  private static final long serialVersionUID = -1535218580445132712L;

  public static final String         JSON_TYPE                   = "type";
  
  public static final String         JSON_NAME                   = "name";
  
  public static final String         JSON_LOCALIZED_LABEL        = "label";
  
  public static final String         JSON_ORG_CODE               = "orgCode";
  
  public static final String         JSON_GEO_OBJECT_TYPE_CODE   = "geoObjectTypeCode";
  
  private Type                       type;
  
  private String                     name;
  
  private LocalizedValue             label;
  
  private String                     organizationCode;
  
  private String                     geoObjectTypeCode;
  
  /**
   * Precondition: Parameters form a valid role with the following rules:
   * 
   * {@link RegistryRole.Type.RA}
   * 
   * @param type {@link Type} of the {@link RegistryRole}
   * @param name  name of the role
   * @param label localized display label of the role
   * @param organizationCode the organization the role belongs to (if any)
   * @param geoObjectTypeCode the {@link GeoObjectType} that the role is associated with (if any);
   */
  private RegistryRole(Type type, String name, LocalizedValue label, String organizationCode, String geoObjectTypeCode)
  {
    this.type              = type;
    this.label             = label;

    if (this.type.equals(Type.SRA))
    {
      this.organizationCode  = "";
      this.geoObjectTypeCode = "";
      this.name = Type.getSRA_RoleName();
    }
    else if (this.type.equals(Type.RA))
    {
      this.organizationCode  = organizationCode;
      this.geoObjectTypeCode = "";
      this.name              = Type.getRA_RoleName(this.organizationCode);
    }
    else if (this.type.equals(Type.RM))
    {
      this.organizationCode  = organizationCode;
      this.geoObjectTypeCode = geoObjectTypeCode;
      this.name              = Type.getRM_RoleName(this.organizationCode, this.geoObjectTypeCode);
    }
    else if (this.type.equals(Type.RC))
    {
      this.organizationCode  = organizationCode;
      this.geoObjectTypeCode = geoObjectTypeCode;
      this.name              = Type.getRC_RoleName(this.organizationCode, this.geoObjectTypeCode);
    }
    else if (this.type.equals(Type.AC))
    {
      this.organizationCode  = organizationCode;
      this.geoObjectTypeCode = geoObjectTypeCode;
      this.name              = Type.getAC_RoleName(this.organizationCode, this.geoObjectTypeCode);
    }
    else
    {
      this.name              = name;
      this.label             = label;
      this.organizationCode  = organizationCode;
      this.geoObjectTypeCode = geoObjectTypeCode;
    }
  }
  
  
  /**
   * Creates a {@link RegistryRole} object for the SRA role.
   * 
   * @param label localized display label.
   * 
   * @return a {@link RegistryRole} object for the SRA role.
   */
  public static RegistryRole createSRA(LocalizedValue label)
  {
    String roleName = RegistryRole.Type.getSRA_RoleName();
    
    return new RegistryRole(RegistryRole.Type.SRA, roleName, label, "", "");
  }
  
  /**
   * Creates a {@link RegistryRole} object for the RA role for the {@link OrganizationDTO} with the given code.
   * 
   * @param label
   * @param organizationCode
   * @return {@link RegistryRole} object for the RA role for the {@link OrganizationDTO} with the given code.
   */
  public static RegistryRole createRA(LocalizedValue label, String organizationCode)
  {
    String roleName = RegistryRole.Type.getRA_RoleName(organizationCode);
    
    return new RegistryRole(RegistryRole.Type.RA, roleName, label, organizationCode, "");
  }
  
  /**
   * Creates a {@link RegistryRole} object for the RM role for the {@link OrganizationDTO} with the given code
   * and {@link GeoObjectType} with the given code.
   * 
   * @param label
   * @param organizationCode
   * @return {@link RegistryRole} object for the RM role for the {@link OrganizationDTO} with the given code
   * and {@link GeoObjectType} with the given code.
   */
  public static RegistryRole createRM(LocalizedValue label, String organizationCode, String geoObjectTypeCode)
  {
    String roleName = RegistryRole.Type.getRM_RoleName(organizationCode, geoObjectTypeCode);
    
    return new RegistryRole(RegistryRole.Type.RM, roleName, label, organizationCode, geoObjectTypeCode);
  }
  
  /**
   * Creates a {@link RegistryRole} object for the RC role for the {@link OrganizationDTO} with the given code
   * and {@link GeoObjectType} with the given code.
   * 
   * @param label
   * @param organizationCode
   * @return {@link RegistryRole} object for the RC role for the {@link OrganizationDTO} with the given code
   * and {@link GeoObjectType} with the given code.
   */
  public static RegistryRole createRC(LocalizedValue label, String organizationCode, String geoObjectTypeCode)
  {
    String roleName = RegistryRole.Type.getRC_RoleName(organizationCode, geoObjectTypeCode);
    
    return new RegistryRole(RegistryRole.Type.RC, roleName, label, organizationCode, geoObjectTypeCode);
  }
  
  /**
   * Creates a {@link RegistryRole} object for the AC role for the {@link OrganizationDTO} with the given code
   * and {@link GeoObjectType} with the given code.
   * 
   * @param label
   * @param organizationCode
   * @return {@link RegistryRole} object for the AC role for the {@link OrganizationDTO} with the given code
   * and {@link GeoObjectType} with the given code.
   */
  public static RegistryRole createAC(LocalizedValue label, String organizationCode, String geoObjectTypeCode)
  {
    String roleName = RegistryRole.Type.getAC_RoleName(organizationCode, geoObjectTypeCode);
    
    return new RegistryRole(RegistryRole.Type.RC, roleName, label, organizationCode, geoObjectTypeCode);
  }
  
  /**
   * Returns the {@link Type} of the role.
   * 
   * @return the {@link Type} of the role.
   */
  public Type getType()
  {
    return this.type;
  }
  
  /**
   * Returns the name of this {@link RegistryRole}.
   * 
   * @return name of this {@link RegistryRole}.
   */
  public String getName()
  {
    return this.name;
  }
  
  /**
   * Returns the localized label of this {@link RegistryRole} used for the
   * presentation layer.
   * 
   * @return Localized label of this {@link RegistryRole}.
   */
  public LocalizedValue getLabel()
  {
    return this.label;
  }
  
  /**
   * Sets the localized display label of this {@link RegistryRole}.
   * 
   * Precondition: label may not be null
   * 
   * @param label
   */
  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }
  
  
  /**
   * Sets the localized display label of this {@link RegistryRole}.
   * 
   * Precondition: key may not be null
   * Precondition: key must represent a valid locale that has been defined on the back-end
   * 
   * @param key string of the locale name.
   * @param value value for the given locale.
   */
  public void setLabel(String key, String value)
  {
    this.label.setValue(key, value);
  }
  
  /**
   * Returns the {@link OrganizationDTO} code of this {@link RegistryRole} or an empty string if there is none.
   * 
   * @return the {@link OrganizationDTO} code of this {@link RegistryRole} or an empty string if there is none.
   */
  public String getOrganizationCode()
  {
    return this.organizationCode;
  }
  
  /**
   * Returns the {@link GeoObjectType} code of this {@link RegistryRole} or an empty string if there is none.
   * 
   * @return the {@link GeoObjectType} code of this {@link RegistryRole} or an empty string if there is none.
   */
  public String getGeoObjectTypeCode()
  {
    return this.geoObjectTypeCode;
  }
  
  /**
   * Creates a {@link RegistryRole} from the given JSON string.
   * 
   * Precondition: 
   * 
   * @param sJson
   *          JSON string that defines the {@link RegistryRole}.
   * @return
   */
  public static RegistryRole fromJSON(String sJson)
  {
    JsonParser parser = new JsonParser();

    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    
    String type = oJson.get(JSON_TYPE).getAsString().toUpperCase();
    
    String name = oJson.get(JSON_NAME).getAsString();

    LocalizedValue label = LocalizedValue.fromJSON(oJson.get(JSON_LOCALIZED_LABEL).getAsJsonObject());
    
    String organizationCode = oJson.get(JSON_ORG_CODE).getAsString();
    
    String geoObjectTypeCode = oJson.get(JSON_GEO_OBJECT_TYPE_CODE).getAsString();
    
    RegistryRole registryRole = new RegistryRole(Type.valueOf(type), name, label, organizationCode, geoObjectTypeCode);
    
    return registryRole;
  }
  
  
  /**
   * Return the JSON representation of this {@link RegistryRole}.
   * 
   * @return JSON representation of this {@link RegistryRole}.
   */
  public final JsonObject toJSON()
  {
    return toJSON(new DefaultSerializer());
  }
  
  /**
   * Return the JSON representation of this {@link RegistryRole}. Filters the
   * attributes to include in serialization.
   * 
   * @param filter
   *          Filter used to determine if an attribute is included
   * 
   * @return JSON representation of this {@link RegistryRole}.
   */
  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject json = new JsonObject();
    
    json.addProperty(JSON_TYPE, this.getType().name());
    
    json.addProperty(JSON_NAME, this.getName());
    
    json.add(JSON_LOCALIZED_LABEL, this.getLabel().toJSON(serializer));
    
    json.addProperty(JSON_ORG_CODE, this.getOrganizationCode());
    
    json.addProperty(JSON_GEO_OBJECT_TYPE_CODE, this.getGeoObjectTypeCode());

    return json;
  }
}
