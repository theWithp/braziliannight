package braziliannight;

import java.util.function.BiFunction;

import braziliannight.block.DimDoor;
import braziliannight.dimension.BNBiome;
import braziliannight.dimension.BNBiomeProvider;
import braziliannight.dimension.BNChunkGenerator;
import braziliannight.dimension.BNDimension;
import braziliannight.item.DimMirror;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.common.Mod;

@Mod(BrazilianNight.MODID)
public class BrazilianNight
{

  public static final String MODID = "braziliannight";
  public static final ResourceLocation DIM_LOC = new ResourceLocation(MODID, "portallis");
  public static final ModDimension DIMENSION = new ModDimension()
    {
      @Override
      public BiFunction<World, DimensionType, ? extends Dimension> getFactory ()
        {
          return BNDimension::new;
        }
    }.setRegistryName(DIM_LOC);

  public static final ChunkGeneratorType<GenerationSettings, BNChunkGenerator> CHUNK_GENERATOR_TYPE = new ChunkGeneratorType<>(
      BNChunkGenerator::new, false, GenerationSettings::new);
  public static final BiomeProviderType<SingleBiomeProviderSettings, BNBiomeProvider> BIOME_PROVIDER_TYPE = new BiomeProviderType<>(
      BNBiomeProvider::new, SingleBiomeProviderSettings::new);
  public static final Biome PORT_BIOME = new BNBiome();
  public static final Block DIM_DOOR = new DimDoor();
  public static final Item DIM_MIRROR = new DimMirror();
  public static final ItemGroup GROUP = new ItemGroup(MODID)
    {
      @Override
      public ItemStack createIcon ()
        {
          return new ItemStack(DIM_MIRROR);
        }
    };

  public BrazilianNight()
    {}
}
