package braziliannight.entity;

import braziliannight.BN;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

public class BNTheSun extends MobEntity implements BNEntity
{

  public BNTheSun(EntityType<BNTheSun> type, World w)
    {
      super(type, w);
      BN.LOG.trace("BNTheSun::new, OK!");
    }
}
