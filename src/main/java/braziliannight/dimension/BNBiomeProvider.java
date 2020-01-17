package braziliannight.dimension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import braziliannight.BN;
import braziliannight.BNRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.gen.feature.structure.Structure;

public class BNBiomeProvider extends BiomeProvider
{
  private static final List<Biome> SPAWN = Collections.singletonList(BNRegistration.PORTALLIS_HUB);
  private final Biome biome;

  public BNBiomeProvider(SingleBiomeProviderSettings settings)
    {
      this.biome = settings.getBiome();
      BN.LOG.trace("BNBiomeProvider::new, OK!");
    }

  @Override
  public List<Biome> getBiomesToSpawnIn ()
    {
      return SPAWN;
    }

  @Override
  public Biome getBiome (int x, int y)
    {
      return this.biome;
    }

  @Override
  public Biome[] getBiomes (int x, int z, int width, int length, boolean cacheFlag)
    {
      Biome[] abiome = new Biome[width * length];
      Arrays.fill(abiome, 0, width * length, this.biome);
      return abiome;
    }

  @Override
  @Nullable
  public BlockPos findBiomePosition (int x, int z, int range, List<Biome> biomes, Random random)
    {
      return biomes.contains(this.biome)
          ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1))
          : null;
    }

  @Override
  public boolean hasStructure (Structure<?> structureIn)
    {
      return false;
    }

  @Override
  public Set<BlockState> getSurfaceBlocks ()
    {
      if (this.topBlocksCache.isEmpty())
        {
          this.topBlocksCache.add(this.biome.getSurfaceBuilderConfig().getTop());
        }

      return this.topBlocksCache;
    }

  @Override
  public Set<Biome> getBiomesInSquare (int centerX, int centerZ, int sideLength)
    {
      return Sets.newHashSet(this.biome);
    }
}
