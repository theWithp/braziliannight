package braziliannight.dimension;

import braziliannight.BN;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class BNBiome extends Biome
{
  // if we wanted mob spawns (say, for maze mobs) we would extend this again and
  // add them after the super (see for example any non TheVoidBiome vanilla biome
  // decl)
  public BNBiome()
    {
      super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG)
          .precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F)
          .downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String) null));
      BN.LOG.trace("BNBiome::new, OK!");

    }

}