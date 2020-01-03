package bn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BNConstant
{

  public final Map<String, Double> DOUBLES = new HashMap<>();
  public final Map<String, Integer> INTEGERS = new HashMap<>();
  public final Map<String, String> STRINGS = new HashMap<>();

  public BNConstant(String filename)
    {
      URL loc = Thread.currentThread().getContextClassLoader().getResource(filename);
      try
        {
          BufferedReader in = new BufferedReader(new InputStreamReader(loc.openStream()));
          String line;
          while ((line = in.readLine()) != null)
            {
              line = line.trim();
              if (line.startsWith("#") || !line.contains("="))
                continue;
              String[] parts = line.split("=", 2);
              parts[0] = parts[0].trim();
              parts[1] = parts[1].trim();
              if (parts[1].matches("\\A-?\\d+\\z"))
                {
                  INTEGERS.put(parts[0], Integer.parseInt(parts[1]));
                } else if (parts[1].matches("\\A-?\\d+\\.?\\d+D?\\z"))
                {
                  DOUBLES.put(parts[0], Double.parseDouble(parts[1]));
                } else
                {
                  STRINGS.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e)
        {
          System.err.println(BNConstants.MOD_NAME + ": error could not load " + loc.toString());
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
