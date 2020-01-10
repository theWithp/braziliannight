package bn.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class BNWorldProvider extends WorldProvider
{
  @Override
  protected void init ()
    {
      hasSkyLight = false;
      biomeProvider = BNInitWorldGen.PORT_BIOME_PROVIDER;
    }

  @Override
  public DimensionType getDimensionType ()
    {
      return BNInitWorldGen.DIM_TYPE;
    }

  @Override
  public boolean isSurfaceWorld ()
    {
      return false;
    }

  @Override
  public boolean canDoLightning (Chunk chunk)
    {
      return false;
    }

  @Override
  public boolean canDoRainSnowIce (Chunk chunk)
    {
      return false;
    }

  @Override
  public boolean canSnowAt (BlockPos pos, boolean light)
    {
      return false;
    }

  @Override
  public IChunkGenerator createChunkGenerator ()
    {
      return new BNChunkGenerator(world);
    }

}
