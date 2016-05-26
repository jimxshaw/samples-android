package me.jimmyshaw.realtutorialopengles;

public class TextObject
{
    public String text;
    public float x;
    public float y;
    public float[] color;

    public TextObject()
    {
        text = "default";
        x = 0f;
        y = 0f;
        color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    }

    public TextObject(String text, float xcoord, float ycoord)
    {
        this.text = text;
        x = xcoord;
        y = ycoord;
        color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    }
}
