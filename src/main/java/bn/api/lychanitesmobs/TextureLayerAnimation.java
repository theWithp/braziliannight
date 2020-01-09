package bn.api.lychanitesmobs;

import com.google.gson.JsonObject;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

public class TextureLayerAnimation
{

  /**
   * The name of this layer. If set to "base" this will apply animations to the
   * base texture layer.
   **/
  public String name = "base";

  /**
   * The texture suffix for loading sub textures. If empty, the base texture is
   * used.
   **/
  public String textureSuffix = "";

  /**
   * If true, lighting is disabled when rendering this layer for glowing in the
   * dark, etc.
   **/
  public boolean glow = false;

  /**
   * The texture blending style to use. Can be "normal", "additive" or
   * "subtractive".
   **/
  public String blending = "normal";

  /**
   * The scrolling speeds to use, if 0 the texture isn't scrolled in that
   * direction.
   **/
  public Vector2f scrollSpeed = new Vector2f(0, 0);

  /** The color fading speeds to use, if 0 the color isn't faded. **/
  public Vector4f colorFadeSpeed;

  /**
   * Reads JSON data into this Animation Layer.
   * 
   * @param json The JSON data to read from.
   */
  public void loadFromJson (JsonObject json)
    {
      this.name = json.get("name").getAsString().toLowerCase();

      if (json.has("textureSuffix"))
        this.textureSuffix = json.get("textureSuffix").getAsString();

      if (json.has("glow"))
        this.glow = json.get("glow").getAsBoolean();

      if (json.has("blending"))
        this.blending = json.get("blending").getAsString().toLowerCase();

      float scrollSpeedX = 0;
      if (json.has("scrollSpeedX"))
        scrollSpeedX = json.get("scrollSpeedX").getAsFloat();
      float scrollSpeedY = 0;
      if (json.has("scrollSpeedY"))
        scrollSpeedY = json.get("scrollSpeedY").getAsFloat();
      this.scrollSpeed = new Vector2f(scrollSpeedX, scrollSpeedY);

      float colorFadeRed = 0;
      if (json.has("colorFadeRed"))
        colorFadeRed = json.get("colorFadeRed").getAsFloat();
      float colorFadeGreen = 0;
      if (json.has("colorFadeGreen"))
        colorFadeGreen = json.get("colorFadeGreen").getAsFloat();
      float colorFadeBlue = 0;
      if (json.has("colorFadeBlue"))
        colorFadeBlue = json.get("colorFadeBlue").getAsFloat();
      float colorFadeAlpha = 0;
      if (json.has("colorFadeAlpha"))
        colorFadeAlpha = json.get("colorFadeAlpha").getAsFloat();
      if (colorFadeRed != 0 || colorFadeGreen != 0 || colorFadeBlue != 0 || colorFadeAlpha != 0)
        this.colorFadeSpeed = new Vector4f(colorFadeRed, colorFadeGreen, colorFadeBlue, colorFadeAlpha);
    }
}
