package braziliannight.api.lychanitesmobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JSONHelper
{
  public static List<String> getJsonStrings (JsonArray jsonArray)
    {
      List<String> strings = new ArrayList<>();
      Iterator<JsonElement> jsonIterator = jsonArray.iterator();
      while (jsonIterator.hasNext())
        {
          String string = jsonIterator.next().getAsString();
          strings.add(string);
        }
      return strings;
    }
}
