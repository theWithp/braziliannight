package bn;

import java.util.HashMap;
import java.util.Map;

public class BNConstants {
    private BNConstants() {
    }

    // tag for our mod for internal things
    public static final String MOD_ID = "braziliannight";
    // what we call our mod for users
    public static final String MOD_NAME = "Brazilian Night";

    public static final String MC_VERSIONS = "1.12.2";

    // set by gradle macro
    public static final String VERSION = "@VERSION@";

    public static final String CLIENT_PROXY_CLASS = "bn.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "bn.proxy.CommonProxy";

    public static final float DEFAULT_STEP_HEIGHT = 1.02f;
    public static final double FOLLOW_RANGE = 48.0;
    public static final float BOSS_HURT_CAP = 7f;
    public static final int WHIRLWIND_PROBABILITY = 10;
    public static final int WHIRLWIND_COOLDOWN = 20;
    public static final double WHIRLWIND_SPEED = 0.5;
    public static final double WHIRLWIND_TARGET_RANGE = 32;
    public static final int WHIRLWIND_LIFETIME = 500;// I made a number up, need to fine tune
    // ideally this should be loaded out of a file
    public static final Map<String, Float> WHITE_WITCHF = new HashMap<>();
    public static final Map<String, Double> WHITE_WITCHD = new HashMap<>();
    public static final Map<String, Integer> WHITE_WITCHI = new HashMap<>();

    static {
	WHITE_WITCHF.put("WIDTH", 0.6f);
	WHITE_WITCHF.put("HEIGHT", 2.5f);
	WHITE_WITCHD.put("MAX_HEALTH", 220.0);
	WHITE_WITCHI.put("ARMOR", 14);
	WHITE_WITCHI.put("SHOT_CLOCK", 10);
	WHITE_WITCHF.put("ATTACK_RANGE", 64.0f);
	WHITE_WITCHF.put("MAGIC_DAMAGE_MULT", 0.8f);
	WHITE_WITCHD.put("MOVE_SPEED", 0.5);
	WHITE_WITCHI.put("WHIRLWIND_RATE", 4);
	WHITE_WITCHI.put("HURRICANE_RATE", 2);
	WHITE_WITCHD.put("HURRICANE_SIZE", 2.0);
	WHITE_WITCHF.put("HURRICANE_FACTOR", 0.18f);
    }
}
