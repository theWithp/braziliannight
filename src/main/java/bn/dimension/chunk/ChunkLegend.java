package bn.dimension.chunk;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.chunk.ChunkPrimer;

public class ChunkLegend
{
  private Map<Character, JsonArray> legend = new HashMap<>();
  private static final Map<String, IBlockState> BLOCK_LOOKUP = makeLookup();

  public ChunkLegend(JsonArray arr)
    {
      for (JsonElement raw : arr)
        {
          JsonObject elm = raw.getAsJsonObject();
          Character title = elm.get("char").getAsString().charAt(0);
          JsonArray actionsArr = elm.get("actions").getAsJsonArray();
          legend.put(title, actionsArr);
        }
    }

  private static Map<String, IBlockState> makeLookup ()
    {
      Map<String, IBlockState> lookup = new HashMap<>();

      // TODO still very not confident in the unbreakability here
      Block b = Blocks.CONCRETE.setBlockUnbreakable();
      lookup.put("BLACK_CONCRETE", b.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK));
      lookup.put("BEDROCK", Blocks.BEDROCK.getDefaultState());

      return lookup;
    }

  public void place (int x, int y, int z, ChunkPrimer primer, char sigil)
    {
      // note we ignore blockSpecial for now.

      if (!legend.containsKey(sigil))
        return;
      JsonArray actions = legend.get(sigil);
      for (JsonElement raw : actions)
        {
          JsonObject obj = raw.getAsJsonObject();
          String id = obj.get("blockId").getAsString();
          IBlockState targetBlock = BLOCK_LOOKUP.get(id);

          // could use ternary operator here but not now.
          int height = 0;
          if (obj.has("heightFromBase"))
            height = obj.get("heightFromBase").getAsInt();
          int curY = y;
          if (obj.has("yOffset"))
            curY += obj.get("yOffset").getAsInt();

          int i;
          for (i = 0; i <= height; i++)
            primer.setBlockState(x, curY + i, z, targetBlock);
        }
    }

}
