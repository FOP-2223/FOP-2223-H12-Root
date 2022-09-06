package h12.gui.shapes;

import h12.json.JSONElement;
import h12.json.JSONObject;
import h12.json.JSONString;
import h12.json.implementation.node.JSONArrayNode;
import h12.json.implementation.node.JSONNumberNode;
import h12.json.implementation.node.JSONObjectNode;
import h12.json.implementation.node.JSONStringNode;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a circle.
 *
 * @see MyShape
 */
public class MyCircle extends MyShape {

    public static final ShapeType TYPE = ShapeType.CIRCLE;

    private int radius;
    private final int x;
    private final int y;

    /**
     * Creates a new {@link MyCircle}-Instance.
     *
     * @param x           The x-coordinate of the center.
     * @param y           The y-coordinate of the center.
     * @param radius      The radius of the circle.
     * @param fillColor   The {@link Color} used to fill the circle.
     * @param borderColor The {@link Color} used to draw the border of the circle.
     */
    public MyCircle(int x, int y, int radius, Color fillColor, Color borderColor) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.borderColor = borderColor;
        this.fillColor = fillColor;
    }

    /**
     * {@inheritDoc}
     *
     * @param g2d The {@link Graphics2D} object used to draw this {@link MyShape} object.
     */
    @Override
    public void draw(Graphics2D g2d) {
        Shape circle = new Ellipse2D.Double(x - radius, y - radius, 2.0 * radius, 2.0 * radius);
        g2d.setColor(fillColor);
        g2d.fill(circle);
        g2d.setColor(borderColor);
        g2d.draw(circle);
    }

    /**
     * Converts this {@link MyCircle} to a {@link JSONObjectNode}. The {@link JSONObject} contains the following entries:
     * <p> name: The {@link ShapeType} as a {@link JSONStringNode}.
     * <p> x: The x-coordinate of the center as a {@link JSONNumberNode}.
     * <p> y: The y-coordinate of the center as a {@link JSONNumberNode}.
     * <p> radius: The radius as a {@link JSONNumberNode}.
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
        map.put(new JSONStringNode("radius"), new JSONNumberNode(radius));

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
        radius = (int) Math.sqrt(Math.pow(Math.abs(this.x - x), 2) + Math.pow(Math.abs(this.y - y), 2));
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
