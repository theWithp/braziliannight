package braziliannight;

import static braziliannight.BrazilianNight.MODID;

import javax.annotation.Nonnull;

import braziliannight.item.DimMirror;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BNEventHandler
{
  @SubscribeEvent
  public static void onDimensionModRegistry (RegistryEvent.Register<ModDimension> event)
    {
      event.getRegistry().register(BrazilianNight.DIMENSION);
      DimensionManager.registerDimension(BrazilianNight.DIM_LOC, BrazilianNight.DIMENSION, null, false);
    }

  @SubscribeEvent
  public static void onChunkGeneratorTypeRegistry (RegistryEvent.Register<ChunkGeneratorType<?, ?>> event)
    {
      event.getRegistry().register(BrazilianNight.CHUNK_GENERATOR_TYPE.setRegistryName(MODID, "generator"));
    }

  @SubscribeEvent
  public static void onBiomeProviderTypeRegistry (RegistryEvent.Register<BiomeProviderType<?, ?>> event)
    {
      event.getRegistry().register(BrazilianNight.BIOME_PROVIDER_TYPE.setRegistryName(MODID, "generator"));
    }

  @SubscribeEvent
  public static void onBlockRegistry (RegistryEvent.Register<Block> event)
    {
      event.getRegistry().register(BrazilianNight.DIM_DOOR.setRegistryName(MODID, "dim_door"));
    }

  @SubscribeEvent
  public static void onBiomeRegistry (RegistryEvent.Register<Biome> ev)
    {
      ForgeRegistries.BIOMES.register(BrazilianNight.PORT_BIOME);
      BiomeDictionary.addTypes(BrazilianNight.PORT_BIOME, BiomeDictionary.Type.VOID);
    }

  @SubscribeEvent
  public static void onItemRegistry (RegistryEvent.Register<Item> ev)
    {
      ev.getRegistry()
          .register(new BlockItem(BrazilianNight.DIM_DOOR, new Item.Properties().group(BrazilianNight.GROUP))
              .setRegistryName(BrazilianNight.DIM_DOOR.getRegistryName()));
      ev.getRegistry().register(setup(new DimMirror(), "dim_mirror"));
    }

  @Nonnull
  private static <T extends IForgeRegistryEntry<T>> T setup (@Nonnull final T entry, @Nonnull final String name)
    {
      return setup(entry, new ResourceLocation(BrazilianNight.MODID, name));
    }

  @Nonnull
  private static <T extends IForgeRegistryEntry<T>> T setup (@Nonnull final T entry,
      @Nonnull final ResourceLocation registryName)
    {
      entry.setRegistryName(registryName);
      return entry;
    }
}
