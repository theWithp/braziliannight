package braziliannight;

import static braziliannight.BN.MODID;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import braziliannight.block.BrazilianLight;
import braziliannight.block.Killplane;
import braziliannight.block.WarpDoor;
import braziliannight.dimension.BNBiome;
import braziliannight.dimension.BNBiomeProvider;
import braziliannight.dimension.BNChunkGenerator;
import braziliannight.dimension.BNDimension;
import braziliannight.entity.BNNimbus;
import braziliannight.entity.BNThunderhead;
import braziliannight.item.BNINimbus;
import braziliannight.item.CharmXyz;
import braziliannight.item.DimMirror;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
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

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(MODID)
public class BNRegistration
{
  public static final ModDimension PORTALLIS = null;
  public static final ChunkGeneratorType<GenerationSettings, BNChunkGenerator> CHUNK_GENERATOR = null;
  public static final BiomeProviderType<SingleBiomeProviderSettings, BNBiomeProvider> BIOME_PROVIDER = null;
  public static final Block DIM_DOOR = null;
  public static final Block WHITE_DOOR = null;
  public static final Block LIGHT = null;
  public static final Block KILLPLANE = null;
  public static final Biome PORTALLIS_HUB = null;
  public static final Item DIM_MIRROR = null;
  public static final Item I_NIMBUS = null;
  public static final Item CHARM_XYZ = null;

  public static final EntityType<BNNimbus> NIMBUS = setupEntityType("nimbus",
      EntityType.Builder.<BNNimbus>create(BNNimbus::new, EntityClassification.MISC).size(0.5f, 1));
  public static final EntityType<BNThunderhead> THUNDERHEAD = setupEntityType("thunderhead",
      EntityType.Builder.<BNThunderhead>create(BNThunderhead::new, EntityClassification.MISC).size(0.5f, 1));

  public static final Item THUNDERHEAD_EGG = null;

  @SubscribeEvent
  public static void onDimensionModRegistry (RegistryEvent.Register<ModDimension> event)
    {
      BN.LOG.info("BNR: ModDimension Registration Event");

      ModDimension dim = new ModDimension()
        {
          @Override
          public BiFunction<World, DimensionType, ? extends Dimension> getFactory ()
            {
              return BNDimension::new;
            }
        }.setRegistryName(BN.DIM_LOC);
      event.getRegistry().register(dim);
      DimensionManager.registerDimension(BN.DIM_LOC, dim, null, false);
    }

  @SubscribeEvent
  public static void onChunkGeneratorTypeRegistry (RegistryEvent.Register<ChunkGeneratorType<?, ?>> event)
    {
      BN.LOG.info("BNR: ChunkGeneratorType Registration Event");

      event.getRegistry().register(new ChunkGeneratorType<>(BNChunkGenerator::new, false, GenerationSettings::new)
          .setRegistryName(MODID, "chunk_generator"));
    }

  @SubscribeEvent
  public static void onBiomeProviderTypeRegistry (RegistryEvent.Register<BiomeProviderType<?, ?>> event)
    {
      BN.LOG.info("BNR: BiomeProviderType Registration Event");

      event.getRegistry().register(new BiomeProviderType<>(BNBiomeProvider::new, SingleBiomeProviderSettings::new)
          .setRegistryName(MODID, "biome_provider"));
    }

  @SubscribeEvent
  public static void onBlockRegistry (RegistryEvent.Register<Block> event)
    {
      BN.LOG.info("BNR: Block Registration Event");
      event.getRegistry().register((new WarpDoor()).setRegistryName(MODID, "dim_door"));
      event.getRegistry()
          .register((new WarpDoor(new BlockPos(0, 33, 0), new BlockPos(160, 33, 0),
              new ResourceLocation(BN.MODID, BN.DIMNAME), new ResourceLocation(BN.MODID, BN.DIMNAME)))
                  .setRegistryName(MODID, "white_door"));

      event.getRegistry().register((new BrazilianLight()).setRegistryName(MODID, "light"));
      event.getRegistry().register((new Killplane()).setRegistryName(MODID, "killplane"));

    }

  @SubscribeEvent
  public static void onBiomeRegistry (RegistryEvent.Register<Biome> ev)
    {
      BN.LOG.info("BNR: Biome Registration Event");

      Biome b = new BNBiome().setRegistryName(MODID, "portallis_hub");
      ForgeRegistries.BIOMES.register(b);
      BiomeDictionary.addTypes(b, BiomeDictionary.Type.VOID);
    }

  @SubscribeEvent
  public static void onItemRegistry (RegistryEvent.Register<Item> ev)
    {
      BN.LOG.info("BNR: Item Registration Event");
      //portals
      ev.getRegistry().register(
          new BlockItem(DIM_DOOR, new Item.Properties().group(BN.GROUP)).setRegistryName(DIM_DOOR.getRegistryName()));
      ev.getRegistry().register(new BlockItem(WHITE_DOOR, new Item.Properties().group(BN.GROUP))
          .setRegistryName(WHITE_DOOR.getRegistryName()));

      ev.getRegistry().register(setup(new DimMirror(), "dim_mirror"));

      //technical blocks
      ev.getRegistry().register(
          new BlockItem(LIGHT, new Item.Properties().group(BN.GROUP)).setRegistryName(LIGHT.getRegistryName()));
      ev.getRegistry().register(
          new BlockItem(KILLPLANE, new Item.Properties().group(BN.GROUP)).setRegistryName(KILLPLANE.getRegistryName()));

      //debug items
      ev.getRegistry().register(setup(new CharmXyz(), "charm_xyz"));

      //eggs
      ev.getRegistry().register(setup(new BNINimbus(), "i_nimbus"));
      ev.getRegistry().register(new SpawnEggItem(THUNDERHEAD, 0, 0, new Item.Properties().group(BN.GROUP))
          .setRegistryName(MODID, "thunderhead_egg"));
    }

  @SubscribeEvent
  public static void registerEntities (final RegistryEvent.Register<EntityType<?>> ev)
    {
      BN.LOG.info("BNR: EntityType Registration Event");

      ev.getRegistry().register(NIMBUS);
      ev.getRegistry().register(THUNDERHEAD);

    }

  @Nonnull
  private static <T extends IForgeRegistryEntry<T>> T setup (@Nonnull final T entry, @Nonnull final String name)
    {
      return setup(entry, new ResourceLocation(BN.MODID, name));
    }

  @Nonnull
  private static <T extends IForgeRegistryEntry<T>> T setup (@Nonnull final T entry,
      @Nonnull final ResourceLocation registryName)
    {
      entry.setRegistryName(registryName);
      return entry;
    }

  @SuppressWarnings("deprecation")
  private static <T extends Entity> EntityType<T> setupEntityType (String key, EntityType.Builder<T> builder)
    {
      return Registry.register(Registry.ENTITY_TYPE, MODID + ":" + key, builder.build(key));
    }
}
