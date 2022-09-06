package h12.gui.components;

import h12.gui.shapes.MyPolygon;
import h12.gui.shapes.ShapeType;
import h12.json.JSON;
import org.tudalgo.algoutils.student.CrashException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The control panel of the painting program.
 */
public class ControlPanel extends JPanel {

    private final MainFrame mf;

    private final ColorSelectorButtons colorSelectorButtons;

    /**
     * Creates a new {@link ControlPanel}-Instance.
     *
     * @param mf The {@link MainFrame} the created {@link ControlPanel} belongs to.
     */
    public ControlPanel(MainFrame mf) {
        this.mf = mf;

        this.colorSelectorButtons = new ColorSelectorButtons();

        add(new ControlButtons());
        add(colorSelectorButtons);
        add(new ShapeSelector());
    }

    /**
     * Creates a new {@link JButton} or {@link JToggleButton} depending on the last parameter with the given properties.
     *
     * @param name           The name of the button.
     * @param toolTipText    The tooltip text of the button.
     * @param actionListener The {@link ActionListener} for a handling the press of the button.
     * @param isToggleButton Whether the creates button is a {@link JToggleButton} or a {@link JButton}.
     * @return The created button.
     */
    private AbstractButton createButton(String name, String toolTipText, ActionListener actionListener, boolean isToggleButton) {
        AbstractButton jButton = isToggleButton ? new JToggleButton() : new JButton();
        jButton.setText(name);
        jButton.setToolTipText(toolTipText);
        jButton.addActionListener(actionListener);

        return jButton;
    }

    /**
     * Returns the {@link MainFrame} this {@link ControlPanel} belongs to.
     *
     * @return The {@link MainFrame} this {@link ControlPanel} belongs to.
     */
    public MainFrame getMainFrame() {
        return mf;
    }

    /**
     * An inner class for handling the color selection section of this  {@link ControlPanel}.
     */
    private class ColorSelectorButtons extends JToolBar {

        private final String SELECT_FILL_COLOR = "SELECT_FILL_COLOR";
        private final String SELECT_BORDER_COLOR = "SELECT_BORDEr_COLOR";
        private final String SELECT_BACKGROUND_COLOR = "SELECT_BACKGROUND_COLOR";

        private final JButton fillColorButton;
        private final JButton borderColorButton;
        private final JButton backgroundColorButton;

        /**
         * Creates a new {@link ColorSelectorButtons}-Instance.
         */
        public ColorSelectorButtons() {
            fillColorButton = createColorSelectorButton("Fill Color", "Choose a fill color for all Shapes", SELECT_FILL_COLOR, Color.WHITE);
            borderColorButton = createColorSelectorButton("Border Color", "Choose a border Color for all Lines and Outlines", SELECT_BORDER_COLOR, Color.BLACK);
            backgroundColorButton = createColorSelectorButton("Background Color", "Choose a background color", SELECT_BACKGROUND_COLOR, Color.WHITE);

            add(fillColorButton);
            add(borderColorButton);
            add(backgroundColorButton);
        }

        /**
         * Creates a new Button for selecting a {@link Color}.
         *
         * @param name          The name of the button.
         * @param toolTipText   The tooltip text of the button.
         * @param actionCommand The description of the handled action.
         * @param color         The default {@link Color} of this button.
         * @return The created button.
         */
        private JButton createColorSelectorButton(String name, String toolTipText, String actionCommand, Color color) {
            JButton jButton = new JButton();
            jButton.setActionCommand(actionCommand);
            jButton.setToolTipText(toolTipText);
            jButton.addActionListener(createColorSelectorAction(jButton));
            jButton.setText(name);
            jButton.setBackground(color);
            jButton.setForeground((299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000 >= 128 ? Color.black : Color.white);

            return jButton;
        }

        /**
         * Creates an {@link ActionListener} that lets the user select a {@link Color}.
         *
         * @param jButton The corresponding {@link JButton}.
         * @return The created {@link ActionListener}.
         */
        private ActionListener createColorSelectorAction(JButton jButton) {
            return e -> {
                Color color = JColorChooser.showDialog(this, "Select a color", Color.BLACK);
                if (color != null) {
                    jButton.setBackground(color);
                    jButton.setForeground((299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000 >= 128 ? Color.black : Color.white);

                    if (e.getActionCommand().equals(SELECT_FILL_COLOR)) {
                        mf.getInteraction().setFillColor(color);
                    } else if (e.getActionCommand().equals(SELECT_BORDER_COLOR)) {
                        mf.getInteraction().setBorderColor(color);
                    } else if (e.getActionCommand().equals(SELECT_BACKGROUND_COLOR)) {
                        mf.getContentPanel().setBackground(color);
                    }
                }
            };
        }

        public void resetColorButtons() {
            fillColorButton.setBackground(Color.WHITE);
            fillColorButton.setForeground(Color.BLACK);
            mf.getInteraction().setFillColor(Color.WHITE);

            borderColorButton.setBackground(Color.BLACK);
            borderColorButton.setForeground(Color.WHITE);
            mf.getInteraction().setBorderColor(Color.BLACK);

            backgroundColorButton.setBackground(Color.WHITE);
            backgroundColorButton.setForeground(Color.BLACK);
        }
    }

    /**
     * An inner class for handling the shape selection section of this {@link ControlPanel}.
     */
    private class ShapeSelector extends JToolBar {

        /**
         * Creates a new {@link ShapeSelector}-Instance.
         */
        public ShapeSelector() {
            ButtonGroup group = new ButtonGroup();

            for (ShapeType type : ShapeType.values()) {
                String normalized = type.getSpelling() == null ? "" : type.getSpelling().replace("_", " ");
                String capitalized = type.getSpelling() == null || type.getSpelling().length() < 1 ? "" :
                    type.getSpelling().substring(0, 1).toUpperCase() + type.getSpelling().substring(1);

                AbstractButton button = createButton(capitalized, "Create a new " + normalized, e -> mf.getInteraction().setType(type), true);
                group.add(button);
                add(button);
            }

            JSpinner polygonEdgeSpinner = new JSpinner(new SpinnerNumberModel(5, 2, Integer.MAX_VALUE, 1));
            polygonEdgeSpinner.setToolTipText("Sets the edge count of the next created Polygon");
            polygonEdgeSpinner.addChangeListener(e -> MyPolygon.setDefaultEdgeAmount((Integer) polygonEdgeSpinner.getValue()));

            add(polygonEdgeSpinner);
        }

    }

    /**
     * An inner class for handling the control section of this {@link ControlPanel}.
     */
    private class ControlButtons extends JToolBar {

        /**
         * Creates a new {@link ControlButtons}-Instance.
         */
        public ControlButtons() {

            AbstractButton saveButton = createButton("save", "Save the current canvas as JSON",
                e -> {
                    try {
                        new SaveCanvasHandler(ControlPanel.this).save();
                    } catch (CrashException exc) {
                        JOptionPane.showMessageDialog(getMainFrame(), ("This feature is not yet implemented. " +
                            "Have you completed task H5 and removed all calls to crash()?"), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }, false);

            AbstractButton loadButton = createButton("load", "Load a canvas from a JSON",
                e -> {
                    try {
                        new LoadCanvasHandler(ControlPanel.this, new JSON()).load();
                    } catch (CrashException exc) {
                        JOptionPane.showMessageDialog(getMainFrame(), ("This feature is not yet implemented. " +
                            "Have you completed task H5 and removed all calls to crash()?"), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }, false);

            AbstractButton clearButton = createButton("clear", "clears the whole canvas", e -> {
                colorSelectorButtons.resetColorButtons();
                mf.getContentPanel().clear();
            }, false);

            AbstractButton undoButton = createButton("undo", "remove the last created shape", e -> {
                mf.getContentPanel().removeLastShape();
                mf.getInteraction().stop();
            }, false);

            add(saveButton);
            add(loadButton);
            add(clearButton);
            add(undoButton);
        }

    }

}


