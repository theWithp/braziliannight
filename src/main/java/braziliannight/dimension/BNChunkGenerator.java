package braziliannight.dimension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import braziliannight.BNRegistration;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BNChunkGenerator extends ChunkGenerator<GenerationSettings>
{

  private static final Map<Pair<Integer, Integer>, Set<String>> worldMap = new HashMap<>();
  private static final Map<String, ChunkLayer> worldChunkLayers = new HashMap<>();
  private static final Map<String, String> worldRows = new HashMap<>();
  private static final Set<String> defaultLayer = new HashSet<>();
  private static ChunkLegend legend;
  // if we had multiple dimensions/maps we'd have to do this in the constructor
  // and not at static time
    {
      final JsonObject rawMap = ConstantLoader.loadJsonResource(ConstantLoader.dataLoc("chunk/portallis_map.json"));

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
    }

  public BNChunkGenerator(IWorld world, BiomeProvider biomeProvider, GenerationSettings settings)
    {
      super(world, biomeProvider, settings);
    }

  @Override
  public void decorate (WorldGenRegion region)
    {}

  @Override
  public void generateSurface (IChunk chunk)
    {
      int x = chunk.getPos().x;
      int z = chunk.getPos().z;

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
                  legend.place(i, layer.getY(), j, chunk, sigil);
                }
            }
        }

    }

  @Override
  public int getGroundHeight ()
    {
      return this.world.getSeaLevel() + 1;
    }

  @Override
  public int func_222529_a (int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_)
    {
      return 0;
    }

  @Override
  public void carve (IChunk p_222538_1_, GenerationStage.Carving p_222538_2_)
    {}

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures (EntityClassification creatureType, BlockPos pos)
    {
      return BNRegistration.PORTALLIS_HUB.getSpawns(creatureType);
    }

  @Nullable
  @Override
  public BlockPos findNearestStructure (World worldIn, String name, BlockPos pos, int radius, boolean p_211403_5_)
    {
      return null;
    }

  @Override
  public boolean hasStructure (Biome biomeIn, Structure<? extends IFeatureConfig> structureIn)
    {
      return false;
    }

  @Override
  public void initStructureStarts (IChunk p_222533_1_, ChunkGenerator<?> p_222533_2_, TemplateManager p_222533_3_)
    {}

  @Override
  public void makeBase (IWorld iWorld, IChunk iChunk)
    {}

  @Nullable
  @Override
  public <C extends IFeatureConfig> C getStructureConfig (Biome biomeIn, Structure<C> structureIn)
    {
      return null;
    }
}
