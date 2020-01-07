package bn.dimension;

import javax.annotation.Nullable;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class BNInitWorldGen
{
  public static final String PORT_NAME = "Land of Doors: Portallis";
  public static final int PORT_ID = findFreeDimID();
  public static final DimensionType PORT_DIM_TYPE = DimensionType.register(PORT_NAME, "_" + PORT_NAME, PORT_ID,
      BNWorldProvider.class, false);
  public static final WorldType PORT_WORLD_TYPE = new BNWorldType();
  public static final Biome PORT_BIOME = new BNBiomePortallis();

  @EventHandler
  public static void onInit (FMLInitializationEvent ev)
    {
      BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(PORT_BIOME, 0));
      BiomeDictionary.addTypes(PORT_BIOME, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.SPOOKY);
      DimensionManager.registerDimension(PORT_ID, PORT_DIM_TYPE);
    }

  @Nullable
  private static Integer findFreeDimID ()
    {
      for (int i = -2; i > Integer.MIN_VALUE; i--)
        {
          if (!DimensionManager.isDimensionRegistered(i * 11))
            return i;
        }
      return null;
    }
}
