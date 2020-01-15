package braziliannight.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

//big TODO
public abstract class BNEntity extends MobEntity
{

  protected BNEntity(EntityType<? extends MobEntity> type, World worldIn)
    {
      super(type, worldIn);
    }

  public float onlyRenderTicks = 0; // if we wanted to only render every so often this would be set for that... I
                                    // think?
}
