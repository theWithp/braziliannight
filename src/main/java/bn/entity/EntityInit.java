package bn.entity;

import bn.BNConstants;
import bn.entity.boss.WhiteWitch;
import bn.entity.projectile.Whirlwind;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

//this class registers our entities at static time. anytime you add a new entity to the mod it MUST be placed in the listing here.
@Mod.EventBusSubscriber(modid = BNConstants.MOD_ID)
public class EntityInit
{
  private EntityInit()
    {}

  private static int stamp = 0;

  private static final EntityEntry[] ENTITY_ENTRIES = { createEntry("WhiteWitch", WhiteWitch.class, 0xFF00FF, 0x0),
      createEntry("BNWhirlwind", Whirlwind.class), createEntry("Avocado", Avocado.class, 0xFF00FF, 0xFFFFFF) };

  private static <T extends Entity> EntityEntry createEntry (String name, Class<T> cls, boolean canMove,
      boolean makeEgg, int colorA, int colorB)
    {
      EntityEntryBuilder<T> bldr = EntityEntryBuilder.create();
      bldr.entity(cls);
      bldr.name(BNConstants.MOD_ID + '.' + name);
      bldr.id(new ResourceLocation(BNConstants.MOD_ID, name), stamp++);
      bldr.tracker(64, 3, canMove); // put this in the constants file
      if (makeEgg)
        bldr.egg(colorA, colorB);
      return bldr.build();
    }

  private static <T extends Entity> EntityEntry createEntry (String name, Class<T> cls, int colorA, int colorB)
    {
      return createEntry(name, cls, true, true, colorA, colorB);
    }

  private static <T extends Entity> EntityEntry createEntry (String name, Class<T> cls)
    {
      return createEntry(name, cls, true, false, 0, 0);
    }

  @SubscribeEvent
  public static void registerEntities (final RegistryEvent.Register<EntityEntry> ev)
    {
      IForgeRegistry<EntityEntry> reg = ev.getRegistry();
      for (EntityEntry e : ENTITY_ENTRIES)
        reg.register(e);
    }
}
