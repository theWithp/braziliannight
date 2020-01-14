package braziliannight.dimension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConstantLoader
{
  private ConstantLoader()
    {}

  public static JsonObject loadJsonResource (String filename)
    {
      URL loc = Thread.currentThread().getContextClassLoader().getResource(filename);
      BufferedReader reader;
      try
        {
          reader = new BufferedReader(new InputStreamReader(loc.openStream()));
        } catch (IOException e)
        {
          reader = null;
        }
      JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
      return obj;
    }

  public static String dataLoc (String filename)
    {
      return "data/" + "braziliannight" + "/" + filename;
    }
}
