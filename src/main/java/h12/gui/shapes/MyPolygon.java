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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class representing a polygon.
 *
 * @see MyShape
 */
public class MyPolygon extends MyShape {

    public static final ShapeType TYPE = ShapeType.POLYGON;

    private static int defaultEdgeAmount = 5;

    protected final List<Integer> x;
    protected final List<Integer> y;

    private final int edges;


    /**
     * Creates a new {@link MyPolygon}-Instance.
     *
     * @param x           The x-coordinates of the edges. Does not have to contain all coordinates.
     * @param y           The y-coordinates of the edges. Does not have to contain all coordinates.
     * @param fillColor   The {@link Color} used to fill the polygon.
     * @param borderColor The {@link Color} used to draw the border of the polygon.
     * @param edges       The amount of edges this polygon has.
     */
    public MyPolygon(List<Integer> x, List<Integer> y, Color fillColor, Color borderColor, int edges) {
        this.x = x;
        this.y = y;
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.edges = edges;
    }

    /**
     * Creates a new {@link MyPolygon}-Instance. The amount of edges will default to {@link MyPolygon#defaultEdgeAmount}.
     *
     * @param x           The x-coordinates of the edges. Does not have to contain all coordinates.
     * @param y           The y-coordinates of the edges. Does not have to contain all coordinates.
     * @param fillColor   The {@link Color} used to fill the polygon.
     * @param borderColor The {@link Color} used to draw the border of the polygon.
     */
    public MyPolygon(List<Integer> x, List<Integer> y, Color fillColor, Color borderColor) {
        this(x, y, fillColor, borderColor, defaultEdgeAmount);
    }

    /**
     * {@inheritDoc}
     *
     * @param g2d The {@link Graphics2D} object used to draw this {@link MyShape} object.
     */
    @Override
    public void draw(Graphics2D g2d) {
        Polygon polygon = new Polygon(x.stream().mapToInt(i -> i).toArray(), y.stream().mapToInt(i -> i).toArray(), x.size());

        g2d.setColor(fillColor);
        g2d.fillPolygon(polygon);

        g2d.setColor(borderColor);
        g2d.drawPolygon(polygon);

    }

    /**
     * Converts this {@link MyPolygon} to a {@link JSONObjectNode}. The {@link JSONObject} contains the following entries:
     * <p> name: The {@link ShapeType} as a {@link JSONStringNode}.
     * <p> edges: The amount of edges as a {@link JSONNumberNode}.
     * <p> x: The x-coordinates of the polygon as a {@link JSONArrayNode} of {@link JSONNumberNode}.
     * <p> y: The y-coordinates of the polygon as a {@link JSONArrayNode} of {@link JSONNumberNode}.
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

        map.put(new JSONStringNode("edges"), new JSONNumberNode(edges));

        map.put(new JSONStringNode("x"), new JSONArrayNode(x.stream().map(JSONNumberNode::new).collect(Collectors.toList())));
        map.put(new JSONStringNode("y"), new JSONArrayNode(y.stream().map(JSONNumberNode::new).collect(Collectors.toList())));

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
        if (this.x.size() > phase) {
            replaceLastX(x);
            replaceLastY(y);
        } else {
            this.x.add(x);
            this.y.add(y);
        }
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
        this.x.add(x);
        this.y.add(y);
        return phase >= edges;
    }

    /**
     * Sets the default edge amount used for creating new {@link MyPolygon} objects.
     *
     * @param defaultEdgeAmount The new default edge amount.
     */
    public static void setDefaultEdgeAmount(int defaultEdgeAmount) {
        MyPolygon.defaultEdgeAmount = defaultEdgeAmount;
    }

    private void replaceLastX(int newX) {
        if (x.size() == 0) x.add(newX);
        else x.set(x.size() - 1, newX);
    }


    private void replaceLastY(int newY) {
        if (y.size() == 0) y.add(newY);
        else y.set(y.size() - 1, newY);
    }

}
