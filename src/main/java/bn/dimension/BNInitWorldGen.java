package bn.dimension;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import bn.BNConstants;
import bn.magic.ConstantLoader;
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
  private BNInitWorldGen()
    {}

  public static final JsonObject DIMENSIONAL_CONSTANTS = ConstantLoader
      .loadJsonResource(ConstantLoader.dataLoc("dimension.json"));
  public static final String DIM_NAME = DIMENSIONAL_CONSTANTS.get("DIM_NAME").getAsString();
  public static final int DEFAULT_FLOOR_POS = DIMENSIONAL_CONSTANTS.get("DEFAULT_FLOOR_POS").getAsInt();

  public static final int DIM_ID = findFreeDimID();
  public static final DimensionType DIM_TYPE = DimensionType.register(DIM_NAME, "_" + DIM_NAME, DIM_ID,
      BNWorldProvider.class, false);
  public static final WorldType WORLD_TYPE = new BNWorldType();
  public static final Biome PORT_BIOME = new BNBiomePortallis();

  public static void onInit ()
    {
      BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(PORT_BIOME, 0));
      BiomeDictionary.addTypes(PORT_BIOME, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.SPOOKY);
      DimensionManager.registerDimension(DIM_ID, DIM_TYPE);
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
        registry.register(PORT_BIOME.setRegistryName(BNConstants.MOD_ID, DIM_NAME));
      }
  }
}
