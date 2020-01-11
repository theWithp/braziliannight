package bn.dimension.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import bn.blocks.BNBlocks;
import bn.dimension.BNInitWorldGen;
import bn.magic.ConstantLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//TODO: add doors (but that'll require a new block)
public class BNChunkGenerator implements IChunkGenerator
{
  private World world;
  // TODO: confirm that diamond is unbreakable
  private static final IBlockState FLOOR_BLOCK = BNBlocks.UNBREAKABLE_BLACK_CONCRETE;
  // TODO: confirm this wont spawn lycanites or whatever
  private static final List<Biome.SpawnListEntry> SPAWN_LIST = new ArrayList<>();

  private final Map<Pair<Integer, Integer>, Set<String>> worldMap = new HashMap<>();
  private final Map<String, ChunkLayer> worldChunkLayers = new HashMap<>();
  private final Map<String, String> worldRows = new HashMap<>();
  private Set<String> defaultLayer = new HashSet<>();
  private ChunkLegend legend;// legend is TODO

  public BNChunkGenerator(World w)
    {
      world = w;
      final JsonObject rawMap = ConstantLoader.loadJsonResource(ConstantLoader.dataLoc("chunk/portallisMap.json"));

      for (JsonElement elm : rawMap.get("chunkDefinitions").getAsJsonArray())
        {
          final JsonObject chunk = elm.getAsJsonObject();
          Pair<Integer, Integer> p = new ImmutablePair<>(chunk.get("x").getAsInt(), chunk.get("z").getAsInt());
          Set<String> s = new HashSet<>();
          for (JsonElement layer : chunk.get("layers").getAsJsonArray())
            s.add(layer.getAsString());
          worldMap.put(p, s);
        }

      for (JsonElement elm : rawMap.get("layerDefinitions").getAsJsonArray())
        {
          final JsonObject rawLayer = elm.getAsJsonObject();
          String id = rawLayer.get("id").getAsString();
          int y = rawLayer.get("baseYPos").getAsInt();
          String[] symbols = new String[16];
          JsonArray map = rawLayer.get("map").getAsJsonArray();
          for (int i = 0; i < 16; i++)
            symbols[i] = map.get(i).getAsString();
          worldChunkLayers.put(id, new ChunkLayer(y, symbols));
        }
      defaultLayer.add(rawMap.get("defaultLayer").getAsString());

      for (JsonElement elm : rawMap.get("rowDefinitions").getAsJsonArray())
        {
          final JsonObject row = elm.getAsJsonObject();
          // assert chars = 16
          worldRows.put(row.get("id").getAsString(), row.get("chars").getAsString());
        }

      legend = new ChunkLegend(rawMap.get("legend").getAsJsonArray());

      MinecraftForge.EVENT_BUS.register(this);
    }

  private void placeFromLegend (int x, int y, int z, ChunkPrimer primer, char sigil)
    {
      // TODO hardcoded for now (we ignore legend)
      if (sigil == '.')
        {
          primer.setBlockState(x, y, z, FLOOR_BLOCK);
          primer.setBlockState(x, 127, z, FLOOR_BLOCK);
        } else if (sigil == 'X')
        {
          for (int i = y; i < 128; i++)
            {
              primer.setBlockState(x, i, z, FLOOR_BLOCK);
            }
        }
    }

  @Override
  public Chunk generateChunk (int x, int z)
    {
      ChunkPrimer primer = new ChunkPrimer();
      Set<String> chunkRecipe;
      Pair<Integer, Integer> loc = new ImmutablePair<>(x, z);
      if (worldMap.containsKey(loc))
        chunkRecipe = worldMap.get(loc);
      else
        chunkRecipe = defaultLayer;
      for (String l : chunkRecipe)
        {
          ChunkLayer layer = worldChunkLayers.get(l);
          for (int i = 0; i < 16; i++)
            {
              String row = worldRows.get(layer.get(i));
              for (int j = 0; j < 16; j++)
                {
                  char sigil = row.charAt(j);
                  placeFromLegend(i, layer.getY(), j, primer, sigil);
                }
            }
        }

      Chunk ret = new Chunk(world, primer, x, z);
      Arrays.fill(ret.getBiomeArray(), (byte) Biome.getIdForBiome(BNInitWorldGen.PORT_BIOME));
      ret.generateSkylightMap();
      return ret;
    }

  @SubscribeEvent
  public void onChunkchange (EntityEvent.EnteringChunk ev)
    {
      if (world.isRemote)
        return;
      Entity ent = ev.getEntity();
      if (ent.dimension != BNInitWorldGen.DIM_ID)
        return;

      Pair<Integer, Integer> curPos = new ImmutablePair<>(ev.getNewChunkX(), ev.getNewChunkZ());
      double y = ent.posY;
      if (!worldMap.containsKey(curPos))
        ent.setPosition(0, y, 0);
      else
        {
          Set<String> chunkData = worldMap.get(curPos);
          for (String feature : chunkData)
            {
              System.out.println(feature);
              if (feature.contains("North") || feature.contains("South") || feature.contains("West")
                  || feature.contains("East"))
                ent.setPositionAndUpdate(-(ent.posX - 2 * Math.signum(ent.posX)), y,
                    -(ent.posZ - 2 * Math.signum(ent.posZ)));
            }
        }
    }

  @Override
  public void populate (int x, int z)
    {}

  @Override
  public boolean generateStructures (Chunk chunkIn, int x, int z)
    {
      return false;
    }

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures (EnumCreatureType creatureType, BlockPos pos)
    {
      return SPAWN_LIST;
    }

  @Override
  public void recreateStructures (Chunk chunkIn, int x, int z)
    {}

  @Override
  public boolean isInsideStructure (World worldIn, String structureName, BlockPos pos)
    {
      return false;
    }

  @Override
  public BlockPos getNearestStructurePos (World worldIn, String structureName, BlockPos position,
      boolean findUnexplored)
    {
      return null;
    }

}
