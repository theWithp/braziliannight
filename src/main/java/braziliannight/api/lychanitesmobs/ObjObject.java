package braziliannight.api.lychanitesmobs;

import javax.vecmath.Vector3f;


public class ObjObject {

    private String name;
    public Mesh mesh;
    public Material material;
    public Vector3f center;

    public ObjObject(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}