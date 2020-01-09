package bn.dimension;

import javax.annotation.Nullable;

import bn.BNConstants;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BNInitWorldGen
{
  public static final String PORT_NAME = "Portallis";
  public static final int PORT_ID = findFreeDimID();
  public static final DimensionType PORT_DIM_TYPE = DimensionType.register(PORT_NAME, "_" + PORT_NAME, PORT_ID,
      BNWorldProvider.class, false);
  public static final WorldType PORT_WORLD_TYPE = new BNWorldType();
  public static final Biome PORT_BIOME = new BNBiomePortallis();
  public static final int DEFAULT_FLOOR_POS = 32;

  public static void onInit ()
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
            return i * 11;
        }
      return null;
    }

  @Mod.EventBusSubscriber(modid = BNConstants.MOD_ID)
  public static class RegistrationHandler
  {

    @SubscribeEvent
    public static void onEvent (RegistryEvent.Register<Biome> event)
      {
        final IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(PORT_BIOME.setRegistryName(BNConstants.MOD_ID, PORT_NAME));
      }
  }
}
