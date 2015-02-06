package storybuilder.graph.view;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import storybuilder.graph.controller.GraphController;
import storybuilder.graph.model.Node;
import storybuilder.main.view.SBDialog;

/**
 *
 * @author Francesco Bertolino
 */
public class NodeView extends StackPane
{

    public final static double RADIUS = 30;

    private final Node node;

    private final double x;

    private final double y;

    public NodeView(final GraphController controller, final Node node, final double x, final double y)
    {
        this.node = node;
        this.x = x;
        this.y = y;

        initShape();
        initTooltip();
        initEventListeners(controller);

        relocate(x - RADIUS, y - RADIUS);
    }

    private void initShape()
    {
        final Circle circle = new Circle(RADIUS);
        if (node.isEnding()) {
            circle.setFill(Color.LIGHTSALMON);
        } else if (node.getItem() != null) {
            circle.setFill(Color.LIGHTBLUE);
        } else {
            circle.setFill(Color.WHITE);
        }
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);
        Text text = new Text(node.getTitle());
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        getChildren().add(circle);
        getChildren().add(text);
    }

    private void initTooltip()
    {
        final Tooltip t = new Tooltip(node.getDescription());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(this, t);
    }

    private void initEventListeners(final GraphController controller)
    {
        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.HAND);
        });
        setOnMouseClicked((MouseEvent event) -> {
            if (event.isControlDown()) {
                final SBDialog dialog = new SBDialog();
                dialog.setTitle("Section");
                dialog.setHeight(300);
                dialog.add(new Label(getNode().getDescription()));
                final Button closeButton = new Button("Close");
                closeButton.setOnAction((ActionEvent event1) -> {
                    dialog.close();
                });
                closeButton.setMinWidth(670);
                dialog.add(closeButton);
                dialog.show();
            } else {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    controller.drawGraphForSection(node.getSection());
                } else {
                    controller.showContextMenuForNode(this, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }

    public Node getNode()
    {
        return node;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
}
