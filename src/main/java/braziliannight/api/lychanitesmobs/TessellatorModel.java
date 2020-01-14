package braziliannight.api.lychanitesmobs;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

/**
 * @author jglrxavpok
 */
public class TessellatorModel extends ObjModel
{


  public TessellatorModel(ResourceLocation resourceLocation)
    {
      super(resourceLocation.getPath());
      String path = resourceLocation.toString();
      try
      {
		  InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation).getInputStream();
          String content = new String(read(inputStream), "UTF-8");
          String startPath = path.substring(0, path.lastIndexOf('/') + 1);
          HashMap<ObjObject, IndexedModel> map = new OBJLoader().loadModel(startPath, content);
          objObjects.clear();
          Set<ObjObject> keys = map.keySet();
          Iterator<ObjObject> it = keys.iterator();
          while(it.hasNext())
          {
              ObjObject object = it.next();
              Mesh mesh = new Mesh();
              object.mesh = mesh;
              objObjects.add(object);
              map.get(object).toMesh(mesh);
          }
      }
      catch(Exception e)
      {
         //TODO log this somewhere when we do logging
      }
  }
  
  @Override
  public void renderImpl()
  {
      Collections.sort(objObjects, (a, b) -> {
			Vec3d v = Minecraft.getInstance().getRenderViewEntity().getPositionVector();
			double aDist = v.distanceTo(new Vec3d(a.center.x, a.center.y, a.center.z));
			double bDist = v.distanceTo(new Vec3d(b.center.x, b.center.y, b.center.z));
			return Double.compare(aDist, bDist);
		});
      for(ObjObject object : objObjects)
      {
          renderGroup(object);
      }
  }

  @Override
  public void renderGroupsImpl (String group)
    {
      for (ObjObject object : objObjects)
        {
          if (object.getName().equals(group))
            {
              renderGroup(object);
            }
        }
    }

  @Override
  public void renderGroupImpl (ObjObject obj, Vector4f color, Vector2f textureOffset)
    {
      Tessellator tess = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tess.getBuffer();
      if (obj.mesh == null)
        {
          return;
        }
      int[] indices = obj.mesh.indices;
      Vertex[] vertices = obj.mesh.vertices;

      // Get/Create Normals:
      if (obj.mesh.normals == null)
        {
          obj.mesh.normals = new javax.vecmath.Vector3f[indices.length];
        }

      // Build Buffer:
      bufferBuilder.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
      for (int i = 0; i < indices.length; i += 3)
        {

          // Normal:
          javax.vecmath.Vector3f normal = obj.mesh.normals[i];
          if (normal == null)
            {
              normal = this.getNormal(vertices[indices[i]].getPos(), vertices[indices[i + 1]].getPos(),
                  vertices[indices[i + 2]].getPos());
              obj.mesh.normals[i] = normal;
            }

          for (int iv = 0; iv < 3; iv++)
            {
              Vertex v = obj.mesh.vertices[indices[i + iv]];
              bufferBuilder.pos(v.getPos().x, v.getPos().y, v.getPos().z)
                  .tex(v.getTexCoords().x + (textureOffset.getX() * 0.01f),
                      1f - (v.getTexCoords().y + (textureOffset.getY() * 0.01f)))
                  .color(color.x, color.y, color.z, color.w)
                  // .normal(v.getNormal().x, v.getNormal().y, v.getNormal().z)
                  .normal(normal.x, normal.y, normal.z).endVertex();
            }
        }

      // Draw Buffer:
		tess.draw();
    }

  @Override
  public boolean fireEvent (ObjEvent event)
    {
	  return true; //?????
    }

	public Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
		Vector3f output = new Vector3f();

		// Calculate Edges:
		Vector3f calU = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Vector3f calV = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);

		// Cross Edges
		output.x = calU.y * calV.z - calU.z * calV.y;
		output.y = calU.z * calV.x - calU.x * calV.z;
		output.z = calU.x * calV.y - calU.y * calV.x;

		output.normalize();
		return output;
	}
}
