package h12.gui.shapes;

import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.JSONString;
import h12.json.implementation.node.JSONArrayNode;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONObjectNode;
import h12.json.implementation.node.JSONStringNode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a rectangle.
 *
 * @see MyShape
 */
public class MyRectangle extends MyShape {

    public static final ShapeType TYPE = ShapeType.RECTANGLE;

    private final int x;
    private final int y;
    private int height;
    private int width;

    /**
     * Creates a new {@link MyRectangle}-Instance.
     *
     * @param x           The x-coordinate of the upper left corner.
     * @param y           The y-coordinate of the upper left corner.
     * @param height      the height of the rectangle.
     * @param width       the width of the rectangle
     * @param fillColor   The {@link Color} used to fill the rectangle.
     * @param borderColor The {@link Color} used to draw the border of the rectangle.
     */
    public MyRectangle(int x, int y, int height, int width, Color fillColor, Color borderColor) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    /**
     * {@inheritDoc}
     *
     * @param g2d The {@link Graphics2D} object used to draw this {@link MyShape} object.
     */
    @Override
    public void draw(Graphics2D g2d) {

        int actualX = x;
        int actualY = y;
        int actualWidth = width;
        int actualHeight = height;

        if (width < 0) {
            actualWidth = -width;
            actualX -= actualWidth;
        }

        if (height < 0) {
            actualHeight = -height;
            actualY -= actualHeight;
        }

        g2d.setColor(fillColor);
        g2d.fillRect(actualX, actualY, actualWidth, actualHeight);
        g2d.setColor(borderColor);
        g2d.drawRect(actualX, actualY, actualWidth, actualHeight);
    }

    /**
     * Converts this {@link MyRectangle} to a {@link JSONObjectNode}. The {@link JSONObject} contains the following entries:
     * <p> name: The {@link ShapeType} as a {@link JSONStringNode}.
     * <p> x: The x-coordinate of the upper left corner as a {@link JSONNumberNode}.
     * <p> y: The y-coordinate of the upper left corner as a {@link JSONNumberNode}.
     * <p> width: The width of the rectangle as a {@link JSONNumberNode}.
     * <p> height: The height of the rectangle as a {@link JSONNumberNode}.
     * <p> fillColor: The color used to fill the circle as a {@link JSONArrayNode}.
     * <p> borderColor: The color used to draw the border of the circle as a {@link JSONArrayNode}.
     *
     * @return A {@link JSONObjectNode} containing the entries listed above.
     * @see ColorHelper#toJSON(Color)
     * @see ShapeType#getSpelling()
     */
    @Override
    public JSONObject toJSON() {
        Map<JSONString, JSONElement> map = new HashMap<>();

        map.put(new JSONStringNode("name"), new JSONStringNode(TYPE.getSpelling()));

        map.put(new JSONStringNode("x"), new JSONNumberNode(x));
        map.put(new JSONStringNode("y"), new JSONNumberNode(y));
        map.put(new JSONStringNode("height"), new JSONNumberNode(height));
        map.put(new JSONStringNode("width"), new JSONNumberNode(width));

        map.put(new JSONStringNode("fillColor"), ColorHelper.toJSON(fillColor));
        map.put(new JSONStringNode("borderColor"), ColorHelper.toJSON(borderColor));

        return new JSONObjectNode(map);
    }

    /**
     * {@inheritDoc}
     *
     * @param x     The new x-coordinate.
     * @param y     The new y-coordinate.
     * @param phase The current creation phase.
     */
    @Override
    public void update(int x, int y, int phase) {
        width = x - this.x;
        height = y - this.y;
    }

    /**
     * {@inheritDoc}
     *
     * @param x     The new x-coordinate.
     * @param y     The new y-coordinate.
     * @param phase The next creation phase.
     * @return
     */
    @Override
    public boolean nextPhase(int x, int y, int phase) {
        update(x, y, phase);
        return true;
    }

}
