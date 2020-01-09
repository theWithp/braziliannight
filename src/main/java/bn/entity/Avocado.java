package bn.entity;

import bn.BNConstant;
import bn.BNConstants;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Avocado extends BNEntity
{

  private static final BNConstant AVOCADO = BNConstants.AVOCADO;

  private static final String NAME = AVOCADO.getString("NAME");
  private static final float WIDTH = AVOCADO.getFloat("WIDTH");
  private static final float HEIGHT = AVOCADO.getFloat("HEIGHT");

  public Avocado(World w)
    {
      super(w);
      this.setSize(WIDTH, HEIGHT);
    }

  @Override
  protected float modifyDamageAmount (DamageSource d, float amount)
    {
      return amount;
    }
}
