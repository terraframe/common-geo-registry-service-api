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
package org.commongeoregistry.adapter.dataaccess;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.DefaultSerializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LocalizedValue
{
  public static final String  DEFAULT_LOCALE  = "defaultLocale";

  public static final String  LOCALIZED_VALUE = "localizedValue";

  public static final String  LOCALE_VALUES   = "localeValues";

  public static final String  LOCALE          = "locale";

  public static final String  VALUE           = "value";

  private String              localizedValue;

  private Map<String, String> localeValues;

  public LocalizedValue(String localizedValue)
  {
    this(localizedValue, new HashMap<String, String>());
  }

  public LocalizedValue(String localizedValue, Map<String, String> localeValues)
  {
    super();
    this.localizedValue = localizedValue;
    this.localeValues = localeValues;
  }

  public String getValue()
  {
    return localizedValue;
  }

  public void setValue(String value)
  {
    this.localizedValue = value;
  }

  public String getValue(Locale locale)
  {
    return this.getValue(locale.toString());
  }

  public String getValue(String key)
  {
    if (this.localeValues.containsKey(key))
    {
      return this.localeValues.get(key);
    }

    return localizedValue;
  }

  public void setValue(Locale locale, String value)
  {
    String key = locale.toString();

    this.localeValues.put(key, value);
  }

  public void setValue(String key, String value)
  {
    this.localeValues.put(key, value);
  }

  public boolean contains(Locale locale)
  {
    return this.localeValues.containsKey(locale.toString());
  }

  public JsonObject toJSON()
  {
    return toJSON(new DefaultSerializer());
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonArray array = new JsonArray();

    Set<Entry<String, String>> entries = this.localeValues.entrySet();

    for (Entry<String, String> entry : entries)
    {
      JsonObject locale = new JsonObject();
      locale.addProperty(LOCALE, entry.getKey());
      locale.addProperty(VALUE, entry.getValue());

      array.add(locale);
    }

    JsonObject object = new JsonObject();

    if (this.localizedValue != null)
    {
      object.addProperty(LOCALIZED_VALUE, this.localizedValue);
    }

    object.add(LOCALE_VALUES, array);

    serializer.configure(this, object);

    return object;
  }

  public static LocalizedValue fromJSON(JsonObject jObject)
  {
    String localizedValue = jObject.has(LOCALIZED_VALUE) ? jObject.get(LOCALIZED_VALUE).getAsString() : null;
    JsonArray locales = jObject.get(LOCALE_VALUES).getAsJsonArray();

    Map<String, String> map = new HashMap<String, String>();

    for (int i = 0; i < locales.size(); i++)
    {
      JsonObject locale = locales.get(i).getAsJsonObject();
      final JsonElement element = locale.get(VALUE);

      String key = locale.get(LOCALE).getAsString();
      String value = !element.isJsonNull() ? element.getAsString() : null;

      map.put(key, value);
    }

    return new LocalizedValue(localizedValue, map);
  }
}
