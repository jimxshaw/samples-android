package me.jimmyshaw.realtutorialopengles;

import org.w3c.dom.Text;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class TextManager
{
    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    private static final float RI_TEXT_WIDTH = 32.0f;
    private static final float RI_TEXT_SPACESIZE = 20.0f;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer drawListBuffer;

    private float[] vecs;
    private float[] uvs;
    private float[] colors;
    private short[] indices;

    private int index_vecs;
    private int index_uvs;
    private int index_colors;
    private int index_indices;

    private int texturenr;

    private float uniformscale;

    public static int[] l_size =
            {
                    36, 29, 30, 34, 25, 25, 34, 33,
                    11, 20, 31, 24, 48, 35, 39, 29,
                    42, 31, 27, 31, 34, 35, 46, 35,
                    31, 27, 30, 26, 28, 26, 31, 28,
                    28, 28, 29, 29, 14, 24, 30, 18,
                    26, 14, 14, 14, 25, 28, 31, 0,
                    0, 38, 39, 12, 36, 34, 0, 0,
                    0, 38, 0, 0, 0, 0, 0, 0
            };

    public Vector<TextObject> textCollection;

    public TextManager()
    {
        // Create our container.
        textCollection = new Vector<TextObject>();

        // Create the arrays.
        vecs = new float[3 * 10];
        uvs = new float[2 * 10];
        colors = new float[4 * 10];
        indices = new short[10];

        // Init 0 as default.
        texturenr = 0;
    }

    public void addText(TextObject obj)
    {
        // Add text object to our collection.
        textCollection.add(obj);
    }

    public void setTextureID(int value)
    {
        texturenr = value;
    }
}
