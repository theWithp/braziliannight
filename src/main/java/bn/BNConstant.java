package bn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import bn.magic.ConstantLoader;

public class BNConstant
{

  public final Map<String, Double> DOUBLES = new HashMap<>();
  public final Map<String, Integer> INTEGERS = new HashMap<>();
  public final Map<String, String> STRINGS = new HashMap<>();

  public BNConstant(String filename)
    {
      JsonObject raw = ConstantLoader.loadJsonResource(filename);
      for (Entry<String, JsonElement> elm : raw.entrySet())
        {
          String pos = elm.getKey();
          JsonElement subject = elm.getValue();
          try
            {
              if (subject.getAsString().contains("."))
                DOUBLES.put(pos, subject.getAsDouble());
              else
                INTEGERS.put(pos, subject.getAsInt());
            } catch (Exception e)
            {
              STRINGS.put(pos, subject.getAsString());
              System.out.println(subject.getAsString() + " is a string");
            }
        }
    }

  public int getInt (String key)
    {
      return INTEGERS.get(key);
    }

  public double getDouble (String key)
    {
      return DOUBLES.get(key);
    }

  public float getFloat (String key)
    {
      return DOUBLES.get(key).floatValue();
    }

  public String getString (String key)
    {
      return STRINGS.get(key);
    }
}
