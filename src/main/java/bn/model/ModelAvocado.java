package bn.model;

import bn.BNConstants;
import bn.api.lychanitesmobs.ModelCreatureObj;
import net.minecraft.entity.EntityLiving;

public class ModelAvocado extends ModelCreatureObj
{
  public ModelAvocado()
    {
      this.initModel("obj/avocado");
    }

  @Override
  public void animatePart (String partName, EntityLiving entity, float time, float distance, float loop, float lookY,
      float lookX, float scale)
    {
      // TODO: animation code go here
    }
}
