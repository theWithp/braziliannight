package braziliannight;

import static braziliannight.BrazilianNight.MODID;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import braziliannight.block.DimDoor;
import braziliannight.dimension.BNBiome;
import braziliannight.dimension.BNBiomeProvider;
import braziliannight.dimension.BNChunkGenerator;
import braziliannight.dimension.BNDimension;
import braziliannight.item.DimMirror;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(BrazilianNight.MODID)
public class BNRegistration
{
  public static final ModDimension PORTALLIS = null;
  public static final ChunkGeneratorType<GenerationSettings, BNChunkGenerator> CHUNK_GENERATOR = null;
  public static final BiomeProviderType<SingleBiomeProviderSettings, BNBiomeProvider> BIOME_PROVIDER = null;
  public static final Block DIM_DOOR = null;
  public static final Biome PORTALLIS_HUB = null;
  public static final Item DIM_MIRROR = null;

  @SubscribeEvent
  public static void onDimensionModRegistry (RegistryEvent.Register<ModDimension> event)
    {
      ModDimension dim = new ModDimension()
        {
          @Override
          public BiFunction<World, DimensionType, ? extends Dimension> getFactory ()
            {
              return BNDimension::new;
            }
        }.setRegistryName(BrazilianNight.DIM_LOC);
      event.getRegistry().register(dim);
      DimensionManager.registerDimension(BrazilianNight.DIM_LOC, dim, null, false);
    }

  @SubscribeEvent
  public static void onChunkGeneratorTypeRegistry (RegistryEvent.Register<ChunkGeneratorType<?, ?>> event)
    {
      event.getRegistry().register(new ChunkGeneratorType<>(BNChunkGenerator::new, false, GenerationSettings::new)
          .setRegistryName(MODID, "chunk_generator"));
    }

  @SubscribeEvent
  public static void onBiomeProviderTypeRegistry (RegistryEvent.Register<BiomeProviderType<?, ?>> event)
    {
      event.getRegistry().register(new BiomeProviderType<>(BNBiomeProvider::new, SingleBiomeProviderSettings::new)
          .setRegistryName(MODID, "biome_provider"));
    }

  @SubscribeEvent
  public static void onBlockRegistry (RegistryEvent.Register<Block> event)
    {
      event.getRegistry().register((new DimDoor()).setRegistryName(MODID, "dim_door"));
    }

  @SubscribeEvent
  public static void onBiomeRegistry (RegistryEvent.Register<Biome> ev)
    {
      Biome b = new BNBiome().setRegistryName(MODID, "portallis_hub");
      ForgeRegistries.BIOMES.register(b);
      BiomeDictionary.addTypes(b, BiomeDictionary.Type.VOID);
    }

  @SubscribeEvent
  public static void onItemRegistry (RegistryEvent.Register<Item> ev)
    {
      ev.getRegistry().register(new BlockItem(DIM_DOOR, new Item.Properties().group(BrazilianNight.modInstance.GROUP))
          .setRegistryName(DIM_DOOR.getRegistryName()));
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