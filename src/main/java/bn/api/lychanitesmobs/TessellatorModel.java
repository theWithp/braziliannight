package bn.api.lychanitesmobs;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

/**
 * @author jglrxavpok
 */
public class TessellatorModel extends ObjModel
{

  public static final EventBus MODEL_RENDERING_BUS = new EventBus();

  public TessellatorModel(ResourceLocation resourceLocation)
    {
      super(resourceLocation.getResourcePath());
      String path = resourceLocation.toString();
      try
        {
          InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation)
              .getInputStream();
          String content = new String(read(inputStream), "UTF-8");
          String startPath = path.substring(0, path.lastIndexOf('/') + 1);
          HashMap<ObjObject, IndexedModel> map = new OBJLoader().loadModel(startPath, content);
          objObjects.clear();
          Set<ObjObject> keys = map.keySet();
          Iterator<ObjObject> it = keys.iterator();
          while (it.hasNext())
            {
              ObjObject object = it.next();
              Mesh mesh = new Mesh();
              object.mesh = mesh;
              objObjects.add(object);
              map.get(object).toMesh(mesh);
            }
        } catch (Exception e)
        {
          e.printStackTrace();
        }
    }

  @Override
  public void renderImpl ()
    {
      Collections.sort(objObjects, (a, b) ->
        {
          Vec3d v = Minecraft.getMinecraft().getRenderViewEntity().getPositionVector();
          double aDist = v.distanceTo(new Vec3d(a.center.x, a.center.y, a.center.z));
          double bDist = v.distanceTo(new Vec3d(b.center.x, b.center.y, b.center.z));
          return Double.compare(aDist, bDist);
        });
      for (ObjObject object : objObjects)
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
      bufferBuilder.finishDrawing();
      if (bufferBuilder.getVertexCount() > 0)
        {
          VertexFormat vertexformat = bufferBuilder.getVertexFormat();
          int i = vertexformat.getNextOffset();
          ByteBuffer bytebuffer = bufferBuilder.getByteBuffer();
          List<VertexFormatElement> list = vertexformat.getElements();

          for (int j = 0; j < list.size(); ++j)
            {
              VertexFormatElement vertexformatelement = list.get(j);
              bytebuffer.position(vertexformat.getOffset(j));
              vertexformatelement.getUsage().preDraw(vertexformat, j, i, bytebuffer);
            }

          GlStateManager.glDrawArrays(bufferBuilder.getDrawMode(), 0, bufferBuilder.getVertexCount());
          int i1 = 0;

          for (int j1 = list.size(); i1 < j1; ++i1)
            {
              VertexFormatElement vertexformatelement1 = list.get(i1);
              vertexformatelement1.getUsage().postDraw(vertexformat, i1, i, bytebuffer);
            }
        }
      bufferBuilder.reset();
    }

  @Override
  public boolean fireEvent (ObjEvent event)
    {
      Event evt = null;
      if (event.type == ObjEvent.EventType.PRE_RENDER_GROUP)
        {
          evt = new TessellatorModelEvent.RenderGroupEvent.Pre(((ObjObject) event.data[1]).getName(), this);
        } else if (event.type == ObjEvent.EventType.POST_RENDER_GROUP)
        {
          evt = new TessellatorModelEvent.RenderGroupEvent.Post(((ObjObject) event.data[1]).getName(), this);
        } else if (event.type == ObjEvent.EventType.PRE_RENDER_ALL)
        {
          evt = new TessellatorModelEvent.RenderPre(this);
        } else if (event.type == ObjEvent.EventType.POST_RENDER_ALL)
        {
          evt = new TessellatorModelEvent.RenderPost(this);
        }
      if (evt != null)
        return !MODEL_RENDERING_BUS.post(evt);
      return true;
    }

  public javax.vecmath.Vector3f getNormal (javax.vecmath.Vector3f p1, javax.vecmath.Vector3f p2,
      javax.vecmath.Vector3f p3)
    {
      javax.vecmath.Vector3f output = new javax.vecmath.Vector3f();

      // Calculate Edges:
      javax.vecmath.Vector3f calU = new javax.vecmath.Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
      javax.vecmath.Vector3f calV = new javax.vecmath.Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);

      // Cross Edges
      output.x = calU.y * calV.z - calU.z * calV.y;
      output.y = calU.z * calV.x - calU.x * calV.z;
      output.z = calU.x * calV.y - calU.y * calV.x;

      output.normalize();
      return output;
    }
}
