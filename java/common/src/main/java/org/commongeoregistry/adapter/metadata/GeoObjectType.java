/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A {@link GeoObjectType} represents the definition of a location type, such as
 * state, county, province, district, village household, or health facility. A
 * {@link GeoObjectType} specifies the geometry type stored and the feature
 * attributes on the {@link GeoObject}s that are instances of the
 * {@link GeoObjectType}. {@link GeoObjectType} objects are used to define
 * integrity constraints on the user
 * 
 * @author nathan
 * @author rrowlands
 *
 */
public class GeoObjectType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID           = 2857923921744440744L;

  public static final String         JSON_ATTRIBUTES            = "attributes";

  public static final String         JSON_CODE                  = "code";

  public static final String         JSON_LOCALIZED_LABEL       = "label";

  public static final String         JSON_LOCALIZED_DESCRIPTION = "description";

  public static final String         JSON_GEOMETRY_TYPE         = "geometryType";

  public static final String         JSON_IS_LEAF               = "isLeaf";

  public static final String         JSON_IS_GEOMETRY_EDITABLE  = "isGeometryEditable";

  public static final String         JSON_FREQUENCY             = "frequency";

  public static final String         JSON_IS_DEFAULT            = "isDefault";
  
  public static final String         JSON_ORGANIZARION_CODE     = "organizationCode";

  /**
   * Unique but human readable identifier. It could be VILLAGE or HOUSEHOLD.
   */
  private String                     code;

  /**
   * Type of geometry used for instances of this {@link GeoObjectType}, such as
   * point, line, polygon, etc.
   */
  private GeometryType               geometryType;

  /**
   * The localized label of this type, such as Village or Household. Used for
   * display in the presentation tier.
   */
  private LocalizedValue             label;

  /**
   * The localized description of this type, used for display in the
   * presentation tier.
   */
  private LocalizedValue             description;

  /**
   * Indicates whether the type that can only be added as a leaf to a hierarchy.
   * Certain types, like households and structures, if added to a tree would
   * cause the tree to grow to a very large size. Rather, instances of these
   * types in the back-end will reference parent nodes in the tre.
   */
  // #Refactor - remove 
  private Boolean                    isLeaf;

  /**
   * Indicates if geometries can be modified through the web interface.
   */
  private Boolean                    isGeometryEditable;

  /**
   * Frequency of time range for change over time values
   */
  // #Refactor - remove 
  private FrequencyType              frequency;
  
  /**
   * The organization responsible for this {@link GeoObjectType}. This can be null.
   */
  private String                     organizationCode;

  /**
   * Collection of {@link AttributeType} metadata attributes.
   * 
   * key: {@code AttributeType#getName()}
   * 
   * value: {@code AttributeType}
   * 
   */
  private Map<String, AttributeType> attributeMap;

  /**
   * 
   * Precondition: The organization code is valid.
   * 
   * @param code
   *          unique identifier that his human readable.
   * @param geometryType
   *          type of geometry for the {@link GeoObjectType} such as point,
   *          line, etc.
   * @param label
   *          localized label of the {@link GeoObjectType}.
   * @param description
   *          localized description of the {@link GeoObjectType}.
   * @param isLeaf
   *          True if the type is a leaf, false otherwise.
   * @param frequency
   *          TODO
   * @param registry
   *          {@link RegistryAdapter} from which this {@link GeoObjectType} is
   *          defined.
   */
  public GeoObjectType(String code, GeometryType geometryType, LocalizedValue label, LocalizedValue description, Boolean isLeaf, Boolean isGeometryEditable, FrequencyType frequency, String organizationCode, RegistryAdapter registry)
  {
    this.init(code, geometryType, label, description, isLeaf, isGeometryEditable, frequency, organizationCode);

    this.attributeMap = buildDefaultAttributes(registry);
  }

  /**
   * 
   * Precondition: The organization code is valid.
   * 
   * @param code
   *          unique identifier that his human readable.
   * @param geometryType
   *          type of geometry for the {@link GeoObjectType} such as point,
   *          line, etc.
   * @param label
   *          localized label of the {@link GeoObjectType}.
   * @param description
   *          localized description of the {@link GeoObjectType}.
   * @param isLeaf
   *          True if the type is a leaf, false otherwise.
   * @param isGeometryEditable
   *          True if geometries can be modified through the web interface
   * @param frequency
   *          TODO
   * @param attributeMap
   *          attribute map.
   */
  private GeoObjectType(String code, GeometryType geometryType, LocalizedValue label, LocalizedValue description, Boolean isLeaf, Boolean isGeometryEditable, FrequencyType frequency, String organizationCode, Map<String, AttributeType> attributeMap)
  {
    this.init(code, geometryType, label, description, isLeaf, isGeometryEditable, frequency, organizationCode);

    this.attributeMap = attributeMap;
  }

  /**
   * Initializes member variables.
   * 
   * Precondition: The organization code is valid.
   * 
   * @param code
   * @param geometryType
   * @param label
   * @param description
   * @param isLeaf
   * @param isGeometryEditable
   * @param frequency
   *          TODO
   * 
   */
  private void init(String code, GeometryType geometryType, LocalizedValue label, LocalizedValue description, Boolean isLeaf, Boolean isGeometryEditable, FrequencyType frequency, String organizationCode)
  {
    this.code = code;
    this.label = label;
    this.description = description;

    this.geometryType = geometryType;

    this.isLeaf = isLeaf;
    this.isGeometryEditable = isGeometryEditable;
    this.frequency = frequency;
    
    this.organizationCode = organizationCode;
  }

  /**
   * Creates a new instance of the current object and copies the attributes
   * from the given {@link GeoObject} into this object.
   * 
   * @param gotSource
   *          {@link GeoObject} with attributes to copy into this attribute.
   * 
   * @return this {@link GeoObject}
   */
  public GeoObjectType copy(GeoObjectType gotSource)
  {
    GeoObjectType newGeoObjt = new GeoObjectType(this.code, this.geometryType, this.label, this.description, this.isLeaf, this.isGeometryEditable, frequency, this.organizationCode, this.attributeMap);

    newGeoObjt.code = gotSource.getCode();
    newGeoObjt.label = gotSource.getLabel();
    newGeoObjt.description = gotSource.getDescription();

    newGeoObjt.geometryType = gotSource.getGeometryType();

    newGeoObjt.isLeaf = gotSource.isLeaf();
    newGeoObjt.isGeometryEditable = gotSource.isGeometryEditable();
    
    newGeoObjt.organizationCode = gotSource.getOrganizationCode();

    return newGeoObjt;
  }

  /**
   * Returns the code which is the human readable unique identifier.
   * 
   * @return Code value.
   */
  public String getCode()
  {
    return this.code;
  }

  /**
   * Returns the {@link GeometryType} supported for instances of this
   * {@link GeoObjectType}.
   * 
   * @return {@link GeometryType}.
   */
  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }

  /**
   * @return Change over time frequency
   */
  public FrequencyType getFrequency()
  {
    return frequency;
  }

  /**
   * Returns the localized label of this {@link GeoObjectType} used for the
   * presentation layer.
   * 
   * @return Localized label of this {@link GeoObjectType}.
   */
  public LocalizedValue getLabel()
  {
    return this.label;
  }

  /**
   * Sets the localized display label of this {@link GeoObjectType}.
   * 
   * @param label
   */
  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }

  /**
   * Sets the localized display label of this {@link GeoObjectType}.
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
   * Returns true if the type is a leaf node, false otherwise.
   * 
   * @return true if the type is a leaf node, false otherwise.
   */
  public Boolean isLeaf()
  {
    return this.isLeaf;
  }

  /**
   * True if the type is a leaf node, false otherwise.
   * 
   * @param isLeaf
   *          True if the type is a leaf node, false otherwise.
   */
  public void isLeaf(Boolean isLeaf)
  {
    this.isLeaf = isLeaf;
  }

  /**
   * @return True if geometries can be edited through the web interface
   */
  public Boolean isGeometryEditable()
  {
    if (this.isGeometryEditable != null)
    {
      return isGeometryEditable;
    }

    return Boolean.TRUE;
  }

  /**
   * @param isGeometryEditable
   */
  public void setIsGeometryEditable(Boolean isGeometryEditable)
  {
    this.isGeometryEditable = isGeometryEditable;
  }

  /**
   * Returns the localized description of this {@link GeoObjectType} used for
   * the presentation layer.
   * 
   * @return Localized description of this {@link GeoObjectType}.
   */
  public LocalizedValue getDescription()
  {
    return this.description;
  }

  /**
   * Sets the localized display label of this {@link GeoObjectType}.
   * 
   * @param description
   */
  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  /**
   * Sets the localized display label of this {@link GeoObjectType}.
   * 
   * @param description
   */
  public void setDescription(String key, String value)
  {
    this.description.setValue(key, value);
  }

  /**
   * 
   * @return the code of the {@link OrganizationDTO} (Optional) that manages this {@link GeoObjectType}, 
   * or NULL if not managed by an {@link OrganizationDTO}.
   */
  public String getOrganizationCode()
  {
    return this.organizationCode;
  } 
  
  /**
   * Sets the {@link OrganizationDTO} (Optional) that manages this {@link GeoObjectType.
   * 
   * Precondition: The organization code is valid
   * 
   * @param organizationCode code of the {@link OrganizationDTO} that manages this {@link GeoObjectType, 
   * or NULL if none.
   */
  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
  }
  
  /**
   * Returns the {@link AttributeType} defined on this {@link GeoObjectType}
   * with the given name.
   * 
   * @param name
   *          Name of the attribute {@code AttributeType#getName()}.
   * 
   * Precondition: Attribute with the given name is defined on this
   *      {@link GeoObjectType}.
   * 
   * @return Name of the attributes.
   */
  public Optional<AttributeType> getAttribute(String name)
  {
    return Optional.of(this.attributeMap.get(name));
  }

  /**
   * Adds the given {@link AttributeType} as an attribute defined on this
   * {@link GeoObjectType}.
   * 
   * @param attributeType
   *          {@link AttributeType} to add to this {@link GeoObjectType}.
   */
  public void addAttribute(AttributeType attributeType)
  {
    this.attributeMap.put(attributeType.getName(), attributeType);
  }

  /**
   * Removes the given {@link AttributeType} as an attribute defined on this
   * {@link GeoObjectType}.
   * 
   * @param attributeName
   *          {@link AttributeType} to remove from this {@link GeoObjectType}.
   */
  public void removeAttribute(String attributeName)
  {
    this.attributeMap.remove(attributeName);
  }

  /**
   * Returns the {@link AttributeType} objects of the attributes defined on
   * this @link GeoObjectType}.
   * 
   * @return {@link AttributeType} objects of the attributes defined on
   *         this @link GeoObjectType}.
   */
  public Map<String, AttributeType> getAttributeMap()
  {
    return this.attributeMap;
  }

  /**
   * Defines the standard set of {@link AttributeType} defined on
   * all{@link GeoObjectType}s.
   * 
   * @return the standard set of {@link AttributeType} defined on
   *         all{@link GeoObjectType}s.
   */
  private static Map<String, AttributeType> buildDefaultAttributes(RegistryAdapter registry)
  {
    Map<String, AttributeType> defaultAttributeMap = new ConcurrentHashMap<String, AttributeType>();

    AttributeCharacterType uid = (AttributeCharacterType) DefaultAttribute.UID.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.UID.getName(), uid);

    AttributeCharacterType code = (AttributeCharacterType) DefaultAttribute.CODE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.CODE.getName(), code);

    AttributeLocalType displayLabel = (AttributeLocalType) DefaultAttribute.DISPLAY_LABEL.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.DISPLAY_LABEL.getName(), displayLabel);

    AttributeCharacterType type = (AttributeCharacterType) DefaultAttribute.TYPE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.TYPE.getName(), type);

    AttributeIntegerType sequence = (AttributeIntegerType) DefaultAttribute.SEQUENCE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.SEQUENCE.getName(), sequence);

    AttributeDateType createdDate = (AttributeDateType) DefaultAttribute.CREATE_DATE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.CREATE_DATE.getName(), createdDate);

    AttributeDateType updatedDate = (AttributeDateType) DefaultAttribute.LAST_UPDATE_DATE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.LAST_UPDATE_DATE.getName(), updatedDate);

    AttributeTermType status = (AttributeTermType) DefaultAttribute.STATUS.createAttributeType();
    Term rootStatusTerm = registry.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.ROOT.code).get();
    status.setRootTerm(rootStatusTerm);
    defaultAttributeMap.put(DefaultAttribute.STATUS.getName(), status);
    
//    AttributeGeometryType geometry = (AttributeGeometryType) DefaultAttribute.GEOMETRY.createAttributeType();
//    defaultAttributeMap.put(DefaultAttribute.GEOMETRY.getName(), geometry);

    return defaultAttributeMap;
  }

  public static GeoObjectType[] fromJSONArray(String saJson, RegistryAdapter adapter)
  {
    JsonParser parser = new JsonParser();

    JsonArray jaGots = parser.parse(saJson).getAsJsonArray();
    GeoObjectType[] gots = new GeoObjectType[jaGots.size()];
    for (int i = 0; i < jaGots.size(); ++i)
    {
      GeoObjectType got = GeoObjectType.fromJSON(jaGots.get(i).toString(), adapter);
      gots[i] = got;
    }

    return gots;
  }

  /**
   * Creates a {@link GeoObjectType} from the given JSON string.
   * 
   * @param sJson
   *          JSON string that defines the {@link GeoObjectType}.
   * @param registry
   *          {@link RegistryAdapter} from which this {@link GeoObjectType}
   *          object comes.
   * @return
   */
  public static GeoObjectType fromJSON(String sJson, RegistryAdapter registry)
  {
    JsonParser parser = new JsonParser();

    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    JsonArray oJsonAttrs = oJson.getAsJsonArray(JSON_ATTRIBUTES);

    String code = oJson.get(JSON_CODE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(oJson.get(JSON_LOCALIZED_LABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(oJson.get(JSON_LOCALIZED_DESCRIPTION).getAsJsonObject());
    GeometryType geometryType = GeometryType.valueOf(oJson.get(JSON_GEOMETRY_TYPE).getAsString());
    Boolean isLeaf = Boolean.valueOf(oJson.get(JSON_IS_LEAF).getAsString());
    Boolean isGeometryEditable = new Boolean(oJson.get(JSON_IS_GEOMETRY_EDITABLE).getAsBoolean());
    FrequencyType frequency = FrequencyType.valueOf(oJson.get(JSON_FREQUENCY).getAsString());

    String organizationCode = null;

    JsonElement jsonOrganization = oJson.get(JSON_ORGANIZARION_CODE);

    if (jsonOrganization != null)
    {
      organizationCode = jsonOrganization.getAsString();
    }

    Map<String, AttributeType> attributeMap = buildDefaultAttributes(registry);

    for (int i = 0; i < oJsonAttrs.size(); ++i)
    {
      JsonObject joAttr = oJsonAttrs.get(i).getAsJsonObject();
      AttributeType attrType = AttributeType.parse(joAttr);

      attributeMap.put(attrType.getName(), attrType);
    }

    // TODO Need to validate that the default attributes are still defined.
    GeoObjectType geoObjType = new GeoObjectType(code, geometryType, label, description, isLeaf, isGeometryEditable, frequency, organizationCode, attributeMap);

    return geoObjType;
  }

  /**
   * Return the JSON representation of this {@link GeoObjectType}.
   * 
   * @return JSON representation of this {@link GeoObjectType}.
   */
  public final JsonObject toJSON()
  {
    return toJSON(new DefaultSerializer());
  }

  /**
   * Return the JSON representation of this {@link GeoObjectType}. Filters the
   * attributes to include in serialization.
   * 
   * @param filter
   *          Filter used to determine if an attribute is included
   * 
   * @return JSON representation of this {@link GeoObjectType}.
   */
  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject json = new JsonObject();

    json.addProperty(JSON_CODE, this.getCode());

    json.add(JSON_LOCALIZED_LABEL, this.getLabel().toJSON(serializer));

    json.add(JSON_LOCALIZED_DESCRIPTION, this.getDescription().toJSON(serializer));

    // TODO: PROPOSED but not yet approved. Required for fromJSON
    // reconstruction.
    json.addProperty(JSON_GEOMETRY_TYPE, this.geometryType.name());

    json.addProperty(JSON_IS_LEAF, this.isLeaf().toString());
    json.addProperty(JSON_IS_GEOMETRY_EDITABLE, this.isGeometryEditable());
    json.addProperty(JSON_FREQUENCY, this.frequency.name());
    
    String organizationString;
    if (this.organizationCode == null)
    {
      organizationString = "";
    }
    else
    {
      organizationString = this.organizationCode;
    }
    json.addProperty(JSON_ORGANIZARION_CODE, organizationString);

    Collection<AttributeType> attributes = serializer.attributes(this);

    JsonArray attrs = serializer.serialize(this, attributes);

    json.add(JSON_ATTRIBUTES, attrs);

    serializer.configure(this, json);

    return json;
  }

}
