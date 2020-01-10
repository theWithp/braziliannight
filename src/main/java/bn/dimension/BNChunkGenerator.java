package bn.dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bn.blocks.BNBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

//TODO: add doors (but that'll require a new block)
public class BNChunkGenerator implements IChunkGenerator
{
  private ChunkPrimer primer = new ChunkPrimer();
  private World world;
  private static final int BASE_HEIGHT = BNInitWorldGen.DEFAULT_FLOOR_POS;
  // TODO: confirm that diamond is unbreakable
  private static final IBlockState FLOOR_BLOCK = BNBlocks.UNBREAKABLE_BLACK_CONCRETE;
  // TODO: confirm this wont spawn lycanites or whatever
  private static final List<Biome.SpawnListEntry> SPAWN_LIST = new ArrayList<>();
  private static final int RADIUS = BNInitWorldGen.DIMENSIONAL_CONSTANTS.get("HUB_RADIUS").getAsInt();
  private static final int MAX_HEIGHT = BNInitWorldGen.DIMENSIONAL_CONSTANTS.get("MAX_HEIGHT").getAsInt();

  public BNChunkGenerator(World w)
    {
      world = w;
    }

  public static PortallisChunkType getType (int x, int z)
    {
      if (Math.abs(x / 4) < RADIUS && Math.abs(z / 4) < RADIUS)
        return PortallisChunkType.HUB;
      return PortallisChunkType.FILLER;
    }

  private void fill (ChunkPrimer p, int x, int z)
    {
      for (int y = BASE_HEIGHT + 1; y <= MAX_HEIGHT; y++)
        primer.setBlockState(x, y, z, FLOOR_BLOCK);
    }

  @Override
  public Chunk generateChunk (int x, int z)
    {
      for (int i = 0; i < 16; i++)
        {
          for (int j = 0; j < 16; j++)
            {
              primer.setBlockState(i, BASE_HEIGHT, j, FLOOR_BLOCK);
              primer.setBlockState(i, MAX_HEIGHT, j, FLOOR_BLOCK);
              switch (getType(x, z))
                {
                case HUB:
                  break;
                case FILLER:
                  fill(primer, i, j);
                  break;
                }
            }
        }
      Chunk ret = new Chunk(world, primer, x, z);
      Arrays.fill(ret.getBiomeArray(), (byte) Biome.getIdForBiome(BNInitWorldGen.PORT_BIOME));
      ret.generateSkylightMap();
      return ret;
    }

  @Override
  public void populate (int x, int z)
    {}

  @Override
  public boolean generateStructures (Chunk chunkIn, int x, int z)
    {
      return false;
    }

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures (EnumCreatureType creatureType, BlockPos pos)
    {
      return SPAWN_LIST;
    }

  @Override
  public void recreateStructures (Chunk chunkIn, int x, int z)
    {}

  @Override
  public boolean isInsideStructure (World worldIn, String structureName, BlockPos pos)
    {
      return false;
    }

  @Override
  public BlockPos getNearestStructurePos (World worldIn, String structureName, BlockPos position,
      boolean findUnexplored)
    {
      return null;
    }

}
