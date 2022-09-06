package h12.gui.shapes;

import h12.exceptions.JSONParseException;
import h12.gui.components.ContentPanel;
import h12.json.JSONElement;
import h12.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An abstract class for a shape that can be interactively drawn in a {@link ContentPanel}.
 */
public abstract class MyShape {

    protected Color fillColor;
    protected Color borderColor;

    /**
     * Draws this {@link MyShape} using the given {@link Graphics2D} object.
     *
     * @param g2d The {@link Graphics2D} object used to draw this {@link MyShape} object.
     */
    public abstract void draw(Graphics2D g2d);

    /**
     * Updates the current position of the object during a given creation phase.
     *
     * @param x     The new x-coordinate.
     * @param y     The new y-coordinate.
     * @param phase The current creation phase.
     */
    public abstract void update(int x, int y, int phase);

    /**
     * Continues with the next creation phase at the given position.
     *
     * @param x     The new x-coordinate.
     * @param y     The new y-coordinate.
     * @param phase The next creation phase.
     * @return {@code true} if the creation process is finished. Otherwise, false.
     */
    public abstract boolean nextPhase(int x, int y, int phase);

    /**
     * Converts this {@link MyShape} object to a JSONElement.
     *
     * @return The converted {@link JSONElement}.
     */
    public abstract JSONElement toJSON();

    /**
     * Converts the content of the given {@link JSONObject} to a {@link MyShape}.
     *
     * @param element The {@link JSONElement} to convert.
     * @return The {@link MyShape} represented by the given {@link JSONObject}.
     * @throws JSONParseException If the given {@link JSONObject} does not represent a valid shape.
     */
    public static MyShape fromJSON(JSONElement element) throws JSONParseException {

        try {
            String name = element.getEntry("name").getString();
            ShapeType type = ShapeType.fromString(name);

            if (type == null) {
                throw new JSONParseException("Invalid shape type: %s!".formatted(name));
            }

            return switch (type) {
                case RECTANGLE -> {
                    int x = element.getEntry("x").getInteger();
                    int y = element.getEntry("y").getInteger();
                    int height = element.getEntry("height").getInteger();
                    int width = element.getEntry("width").getInteger();

                    Color borderColor = ColorHelper.fromJSON(element.getEntry("borderColor"));
                    Color fillColor = ColorHelper.fromJSON(element.getEntry("fillColor"));

                    yield new MyRectangle(x, y, height, width, fillColor, borderColor);
                }

                case CIRCLE -> {
                    int x = element.getEntry("x").getInteger();
                    int y = element.getEntry("y").getInteger();
                    int radius = element.getEntry("radius").getInteger();

                    Color borderColor = ColorHelper.fromJSON(element.getEntry("borderColor"));
                    Color fillColor = ColorHelper.fromJSON(element.getEntry("fillColor"));

                    yield new MyCircle(x, y, radius, fillColor, borderColor);
                }

                case CUSTOM_LINE -> {
                    Color color = ColorHelper.fromJSON(element.getEntry("color"));

                    List<Integer> x = Arrays.stream(element.getEntry("x").getArray()).map(JSONElement::getInteger).toList();
                    List<Integer> y = Arrays.stream(element.getEntry("y").getArray()).map(JSONElement::getInteger).toList();

                    yield new CustomLine(x, y, color);
                }

                case POLYGON -> {
                    int edges = element.getEntry("edges").getInteger();

                    List<Integer> x = Arrays.stream(element.getEntry("x").getArray()).map(JSONElement::getInteger).toList();
                    List<Integer> y = Arrays.stream(element.getEntry("y").getArray()).map(JSONElement::getInteger).toList();

                    Color borderColor = ColorHelper.fromJSON(element.getEntry("borderColor"));
                    Color fillColor = ColorHelper.fromJSON(element.getEntry("fillColor"));

                    yield new MyPolygon(x, y, fillColor, borderColor, edges);
                }

                default -> throw new JSONParseException("Invalid shape type: %s!".formatted(name));
            };
        } catch (UnsupportedOperationException exc) {
            throw new JSONParseException("Invalid MyShape format!");
        }
    }

    /**
     * Creates a new {@link MyShape} object of the given {@link ShapeType}.
     *
     * @param type        The type of shape to create.
     * @param x           The x-coordinate of the shape.
     * @param y           The y-coordinate of the shape.
     * @param fillColor   The {@link Color} to fill the shape with.
     * @param borderColor The {@link Color} to draw the border of the shape with.
     * @return The created {@link MyShape}.
     */
    public static MyShape of(ShapeType type, int x, int y, Color fillColor, Color borderColor) {
        return switch (type) {
            case RECTANGLE -> new MyRectangle(x, y, 0, 0, fillColor, borderColor);
            case CIRCLE -> new MyCircle(x, y, 0, fillColor, borderColor);
            case TRIANGLE -> new MyTriangle(x, y, x, y, x, y, fillColor, borderColor);
            case CUSTOM_LINE -> new CustomLine(new ArrayList<>(List.of(x)), new ArrayList<>(List.of(y)), borderColor);
            case POLYGON -> new MyPolygon(new ArrayList<>(List.of(x)), new ArrayList<>(List.of(y)), fillColor, borderColor);
            case STRAIGHT_LINE -> new StraightLine(x, y, x, y, borderColor);
        };
    }

    /**
     * Sets the {@link Color} to fill this {@link MyShape} with.
     *
     * @param fillColor The {@link Color} to fill this {@link MyShape} with.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Sets the {@link Color} to draw the border of this {@link MyShape} with.
     *
     * @param borderColor The {@link Color} to draw the border of this {@link MyShape} with.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}

