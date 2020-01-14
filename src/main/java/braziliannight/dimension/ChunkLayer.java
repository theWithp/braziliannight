package braziliannight.dimension;

//container class for bnchunkgenerator
public class ChunkLayer
{
  private int y;
  private String[] arr;

  public ChunkLayer(int y, String[] arr)
    {
      // assert y > 0 y < 256
      this.y = y;
      // assert arr.len = 16
      this.arr = arr;
    }

  public int getY ()
    {
      return y;
    }

  public String get (int pos)
    {
      // assert pos >= 0 pos < 16
      return arr[pos];
    }
}
