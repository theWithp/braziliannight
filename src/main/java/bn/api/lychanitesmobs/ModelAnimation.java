package bn.api.lychanitesmobs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

public class ModelAnimation
{

  /** A list of model texture layer definitions. **/
  public Map<String, TextureLayerAnimation> textureLayers = new HashMap<>();

  /** A list of part animations. **/
  public List<ModelPartAnimation> partAnimations = new ArrayList<>();

  /**
   * Reads JSON data into this ObjPart.
   * 
   * @param json The JSON data to read from.
   */
  public void loadFromJson (JsonObject json)
    {
      // Texture Layers:
      if (json.has("textureLayers"))
        {
          Iterator<JsonElement> textureLayers = json.get("textureLayers").getAsJsonArray().iterator();
          while (textureLayers.hasNext())
            {
              JsonObject jsonObject = textureLayers.next().getAsJsonObject();
              TextureLayerAnimation animationLayer = new TextureLayerAnimation();
              animationLayer.loadFromJson(jsonObject);
              this.textureLayers.put(animationLayer.name, animationLayer);
            }
        }

      // Part Animations:
      if (json.has("partAnimations"))
        {
          Iterator<JsonElement> partAnimations = json.get("partAnimations").getAsJsonArray().iterator();
          while (partAnimations.hasNext())
            {
              JsonObject jsonObject = partAnimations.next().getAsJsonObject();
              ModelPartAnimation partAnimation = new ModelPartAnimation();
              partAnimation.loadFromJson(jsonObject);
              this.partAnimations.add(partAnimation);
            }
        }
    }
}
