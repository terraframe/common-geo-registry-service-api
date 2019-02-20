package org.commongeoregistry.adapter.dataaccess;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LocalizedValue
{
  private static final String LOCALIZED_VALUE = "localizedValue";

  private static final String LOCALE_MAP      = "localeMap";

  private static final String LOCALE          = "locale";

  private static final String VALUE           = "value";

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
    JsonArray array = new JsonArray();

    this.localeValues.forEach((key, value) -> {
      JsonObject locale = new JsonObject();
      locale.addProperty(LOCALE, key);
      locale.addProperty(VALUE, value);

      array.add(locale);
    });

    JsonObject object = new JsonObject();
    object.addProperty(LOCALIZED_VALUE, this.localizedValue);
    object.add(LOCALE_MAP, array);
    return object;
  }

  public static LocalizedValue fromJSON(JsonObject jObject)
  {
    String localizedValue = jObject.get(LOCALIZED_VALUE).getAsString();
    JsonArray locales = jObject.get(LOCALE_MAP).getAsJsonArray();

    Map<String, String> map = new HashMap<String, String>();

    for (int i = 0; i < locales.size(); i++)
    {
      JsonObject locale = locales.get(i).getAsJsonObject();
      String key = locale.get(LOCALE).getAsString();
      String value = locale.get(VALUE).getAsString();

      map.put(key, value);
    }

    return new LocalizedValue(localizedValue, map);
  }
}
