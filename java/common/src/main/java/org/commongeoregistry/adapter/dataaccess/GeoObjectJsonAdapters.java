package org.commongeoregistry.adapter.dataaccess;

import java.lang.reflect.Type;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.DefaultSerializer;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vividsolutions.jts.geom.Geometry;

public class GeoObjectJsonAdapters
{
  public static final String     JSON_PROPERTIES  = "properties";

  public static final String     JSON_TYPE        = "type";

  public static final String     JSON_GEOMETRY    = "geometry";

  public static final String     JSON_FEATURE     = "Feature";
  
  public static class GeoObjectDeserializer implements JsonDeserializer<GeoObject>
  {
    private RegistryAdapter registry;
    
    public GeoObjectDeserializer(RegistryAdapter registry)
    {
      this.registry = registry;
    }

    @Override
    public GeoObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject oJson = json.getAsJsonObject();
      JsonObject oJsonProps = oJson.getAsJsonObject(JSON_PROPERTIES);

      GeoObject geoObj;
      if (oJsonProps.has("uid"))
      {
        geoObj = registry.newGeoObjectInstance(oJsonProps.get(JSON_TYPE).getAsString(), false);
      }
      else
      {
        geoObj = registry.newGeoObjectInstance(oJsonProps.get(JSON_TYPE).getAsString(), true);
      }

      JsonElement oGeom = oJson.get(JSON_GEOMETRY);
      if (oGeom != null)
      {
        GeoJSONReader reader = new GeoJSONReader();
        Geometry jtsGeom = reader.read(oGeom.toString());

        geoObj.setGeometry(jtsGeom);
      }

      for (String key : geoObj.attributeMap.keySet())
      {
        Attribute attr = geoObj.attributeMap.get(key);

        if (oJsonProps.has(key) && !oJsonProps.get(key).isJsonNull())
        {
          attr.fromJSON(oJsonProps.get(key), registry);
        }
      }

      return geoObj;
    }
  }
  
  public static class GeoObjectSerializer implements JsonSerializer<GeoObject>
  {
    private CustomSerializer serializer;
    
    public GeoObjectSerializer(CustomSerializer serializer)
    {
      this.serializer = serializer;
    }
    
    public GeoObjectSerializer()
    {
      this.serializer = new DefaultSerializer();
    }
    
    @Override
    public JsonElement serialize(GeoObject go, Type typeOfSrc, JsonSerializationContext context)
    {
      JsonObject jsonObj = new JsonObject();

      // It's assumed that GeoObjects are simple features rather than
      // FeatureCollections.
      // Spec reference: https://tools.ietf.org/html/rfc7946#section-3.3
      jsonObj.addProperty(JSON_TYPE, JSON_FEATURE);

      if (go.getGeometry() != null)
      {
        GeoJSONWriter gw = new GeoJSONWriter();
        org.wololo.geojson.Geometry gJSON = gw.write(go.getGeometry());

        JsonParser parser = new JsonParser();
        JsonObject geomObj = parser.parse(gJSON.toString()).getAsJsonObject();

        jsonObj.add(JSON_GEOMETRY, geomObj);
      }

      JsonObject props = new JsonObject();
      for (String key : go.attributeMap.keySet())
      {
        Attribute attr = go.attributeMap.get(key);

        JsonElement value = attr.toJSON(serializer);
        if (!value.isJsonNull())
        {
          props.add(attr.getName(), value);
        }

        // if(attr instanceof AttributeTerm)
        // {
        // attrs.add(key, attr.toJSON());
        // }
        // else
        // {
        //
        // System.out.println(attr.toJSON());
        //
        // // TODO: All these attributes are required by the CGR spec. Adding an
        // // empty string is a temporary step for me to work on another area of
        // // the adapter. Ensure that Values are always present and handle
        // // NULLs as errors.
        // if(attr.getValue() == null )
        // {
        // attrs.addProperty(key, "");
        // }
        // else
        // {
        // attrs.addProperty(key, attr.getValue().toString() );
        // }
        // }

        // JsonParser attrParser = new JsonParser();
        // JsonObject geomObj =
        // attrParser.parse(attr.toJSON().toString()).getAsJsonObject();

      }

      jsonObj.add(JSON_PROPERTIES, props);

      return jsonObj;
    }
  }

}
