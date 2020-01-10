package bn.dimension;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class BNBiomePortallis extends Biome
{
  private static BiomeProperties getProps ()
    {
      BiomeProperties props = new BiomeProperties(BNInitWorldGen.DIM_NAME);
      props.setBaseHeight(1);
      props.setHeightVariation(0f);
      props.setTemperature(0f);
      return props;
    }

  private void setSpawnables ()
    {
      spawnableCreatureList.clear();
      spawnableMonsterList.clear();
      spawnableWaterCreatureList.clear();
      spawnableCaveCreatureList.clear();
    }

  public BNBiomePortallis()
    {
      super(getProps());
      setSpawnables();
    }

  // making ChunkGenerator do everything, NOOP
  @Override
  public void genTerrainBlocks (World world, Random seed, ChunkPrimer primer, int x, int z, double noise)
    {}

  /*
   * If we cared about biome decorators we'd have to override CreateBiomeDecorator
   * but we do not. (TODO: ensure that we don't have to make a void biome
   * decorator for whatever reason)
   */

}
