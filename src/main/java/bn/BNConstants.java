package bn;

public class BNConstants
{
  private BNConstants()
    {}

  // tag for our mod for internal things
  public static final String MOD_ID = "braziliannight";
  // what we call our mod for users
  public static final String MOD_NAME = "Brazilian Night";

  public static final String MC_VERSIONS = "1.12.2";

  // set by gradle macro
  public static final String VERSION = "@VERSION@";

  public static final String CLIENT_PROXY_CLASS = "bn.proxy.ClientProxy";
  public static final String SERVER_PROXY_CLASS = "bn.proxy.CommonProxy";

  // this stuff could all go in files too, but we need to discuss filenames
  public static final float DEFAULT_STEP_HEIGHT = 1.02f;
  public static final double FOLLOW_RANGE = 48.0;
  public static final float BOSS_HURT_CAP = 7f;
  public static final int WHIRLWIND_PROBABILITY = 10;
  public static final int WHIRLWIND_COOLDOWN = 20;
  public static final double WHIRLWIND_SPEED = 0.2;
  public static final double WHIRLWIND_TARGET_RANGE = 32;
  public static final int WHIRLWIND_LIFETIME = 500;// I made a number up, need to fine tune
  public static final int BOSS_RESIST_TIME = 40;

  public static final String prefix = "data/" + MOD_ID + "/";
  public static final BNConstant WHITE_WITCH = new BNConstant(prefix + "whiteWitch.cfg");;

}
