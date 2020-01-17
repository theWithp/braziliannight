package braziliannight.dimension;

import javax.annotation.Nullable;

import braziliannight.BN;
import braziliannight.BNRegistration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BNDimension extends Dimension
{
  public BNDimension(World world, DimensionType type)
    {
      super(world, type);
      BN.LOG.trace("BNDimension::new, OK!");

    }

  @Override
  public ChunkGenerator<?> createChunkGenerator ()
    {
      return BNRegistration.CHUNK_GENERATOR.create(this.world,
          BNRegistration.BIOME_PROVIDER
              .create(BNRegistration.BIOME_PROVIDER.createSettings().setBiome(BNRegistration.PORTALLIS_HUB)),
          BNRegistration.CHUNK_GENERATOR.createSettings());
    }

  @Nullable
  @Override
  public BlockPos findSpawn (ChunkPos chunkPos, boolean checkValid)
    {
      return null;
    }

  @Nullable
  @Override
  public BlockPos findSpawn (int x, int z, boolean checkValid)
    {
      return null;
    }

  @Override
  public float calculateCelestialAngle (long worldTime, float partialTicks)
    {
      return 0;
    }

  @Override
  public boolean isSurfaceWorld ()
    {
      return false;
    }

  @Override
  @OnlyIn(Dist.CLIENT)
  public Vec3d getFogColor (float p_76562_1_, float p_76562_2_)
    {
      float f = MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
      f = MathHelper.clamp(f, 0.0F, 1.0F);
      float f1 = 0.7529412F;
      float f2 = 0.84705883F;
      float f3 = 1.0F;
      f1 = f1 * (f * 0.94F + 0.06F);
      f2 = f2 * (f * 0.94F + 0.06F);
      f3 = f3 * (f * 0.91F + 0.09F);
      return new Vec3d(f1, f2, f3);
    }

  @Override
  public boolean canRespawnHere ()
    {
      return false;
    }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean doesXZShowFog (int x, int z)
    {
      return false;
    }

  @Override
  public long getWorldTime ()
    {
      long ret = super.getWorldTime();
      return ret;
    }

  @Override
  public boolean hasSkyLight ()
    {
      return false;
    }
}
