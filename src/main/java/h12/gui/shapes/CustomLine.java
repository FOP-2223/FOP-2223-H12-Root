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
 * A class representing a custom line.
 *
 * @see MyShape
 */
public class CustomLine extends MyShape {

    public static final ShapeType TYPE = ShapeType.CUSTOM_LINE;

    List<Integer> x;
    List<Integer> y;

    /**
     * Creates a new {@link CustomLine}-Instance.
     *
     * @param x     A list containing all x-coordinates.
     * @param y     A list containing all y-coordinates.
     * @param color The {@link Color} of the line.
     */
    public CustomLine(List<Integer> x, List<Integer> y, Color color) {
        this.x = x;
        this.y = y;
        this.borderColor = color;
    }

    /**
     * {@inheritDoc}
     *
     * @param g2d The {@link Graphics2D} object used to draw this {@link MyShape} object.
     */
    @Override
    public void draw(Graphics2D g2d) {

        g2d.setColor(borderColor);
        g2d.drawPolyline(x.stream().mapToInt(i -> i).toArray(), y.stream().mapToInt(i -> i).toArray(), x.size());

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
        this.x.add(x);
        this.y.add(y);
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

    /**
     * Converts this {@link CustomLine} to a {@link JSONObjectNode}. The {@link JSONObject} contains the following entries:
     * <p> name: The {@link ShapeType} as a {@link JSONStringNode}.
     * <p> x: The x-coordinates of the line as a {@link JSONArrayNode} of {@link JSONNumberNode}.
     * <p> y: The y-coordinates of the line as a {@link JSONArrayNode} of {@link JSONNumberNode}.
     * <p> color: The color used to Draw the line as a {@link JSONArrayNode}.
     *
     * @return A {@link JSONObjectNode} containing the entries listed above.
     * @see ColorHelper#toJSON(Color)
     * @see ShapeType#getSpelling()
     */
    @Override
    public JSONElement toJSON() {
        Map<JSONString, JSONElement> map = new HashMap<>();

        map.put(new JSONStringNode("name"), new JSONStringNode(TYPE.getSpelling()));

        map.put(new JSONStringNode("color"), ColorHelper.toJSON(borderColor));

        map.put(new JSONStringNode("x"), new JSONArrayNode(x.stream().map(JSONNumberNode::new).collect(Collectors.toList())));
        map.put(new JSONStringNode("y"), new JSONArrayNode(y.stream().map(JSONNumberNode::new).collect(Collectors.toList())));

        return new JSONObjectNode(map);
    }

}
