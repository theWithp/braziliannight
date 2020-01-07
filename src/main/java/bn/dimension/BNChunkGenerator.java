package bn.dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

//TODO: add doors (but that'll require a new block)
//TODO: possibly weirder floor?
//TODO: contemplate extra vertical layers
public class BNChunkGenerator implements IChunkGenerator
{
  private ChunkPrimer primer = new ChunkPrimer();
  private World world;
  private static final int BASE_HEIGHT = 30;
  // TODO: confirm that diamond is unbreakable
  private static final IBlockState FLOOR_BLOCK = Blocks.BLACK_GLAZED_TERRACOTTA.setBlockUnbreakable().getDefaultState();
  // TODO: confirm this wont spawn lycanites or whatever
  private static final List<Biome.SpawnListEntry> SPAWN_LIST = new ArrayList<>();

  public BNChunkGenerator(World w)
    {
      world = w;
    }

  @Override
  public Chunk generateChunk (int x, int z)
    {
      for (int i = 0; i < 16; i++)
        {
          for (int j = 0; j < 16; j++)
            {
              primer.setBlockState(i, BASE_HEIGHT, j, FLOOR_BLOCK);
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
