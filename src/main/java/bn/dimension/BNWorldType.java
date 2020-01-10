package bn.dimension;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class BNWorldType extends WorldType
{
  public BNWorldType()
    {
      super(BNInitWorldGen.DIM_NAME);
    }

  @Override
  public BiomeProvider getBiomeProvider (World w)
    {
      return new BNBiomeProvider();
    }

  @Override
  public IChunkGenerator getChunkGenerator (World w, String options)
    {
      return new BNChunkGenerator(w);
    }

  // no using it as overworld
  @Override
  public boolean canBeCreated ()
    {
      return false;
    }
}
