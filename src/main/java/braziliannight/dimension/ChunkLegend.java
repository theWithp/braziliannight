package braziliannight.dimension;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import braziliannight.BNRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;

//follows json rules to fill in a x/z column with BlockStates at chunkgen time
public class ChunkLegend
{
  private Map<Character, JsonArray> legend = new HashMap<>();
  private static final Map<String, BlockState> BLOCK_LOOKUP = makeLookup();

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

  private static Map<String, BlockState> makeLookup ()
    {
      Map<String, BlockState> lookup = new HashMap<>();

      // TODO definitely not unbreakable
      lookup.put("BLACK_CONCRETE", Blocks.BLACK_CONCRETE.getDefaultState());
      lookup.put("BEDROCK", Blocks.BEDROCK.getDefaultState());
      lookup.put("KILLPLANE", BNRegistration.KILLPLANE.getDefaultState());
      lookup.put("LIGHT", BNRegistration.LIGHT.getDefaultState());

      return lookup;
    }

  public void place (int x, int y, int z, IChunk primer, char sigil)
    {
      // note we ignore blockSpecial for now.

      if (!legend.containsKey(sigil))
        return;
      JsonArray actions = legend.get(sigil);
      for (JsonElement raw : actions)
        {
          JsonObject obj = raw.getAsJsonObject();
          String id = obj.get("blockId").getAsString();
          BlockState targetBlock = BLOCK_LOOKUP.get(id);

          // could use ternary operator here but not now.
          int height = 0;
          if (obj.has("heightFromBase"))
            height = obj.get("heightFromBase").getAsInt();
          int curY = y;
          if (obj.has("yOffset"))
            curY += obj.get("yOffset").getAsInt();

          int i;
          for (i = 0; i <= height; i++)
            primer.setBlockState(new BlockPos(x, curY + i, z), targetBlock, false);
        }
    }

}