package storybuilder.graph.view;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import storybuilder.graph.model.Node;
import storybuilder.main.view.MainWindowController;
import storybuilder.section.view.SectionDetailView;

/**
 *
 * @author Francesco Bertolino
 */
public class NodeView extends StackPane
{

    final static double RADIUS = 30;

    private final Node node;

    private final double x;

    private final double y;

    NodeView(final Graph graph, final Node node, final double x, final double y)
    {
        this.node = node;
        this.x = x;
        this.y = y;

        Circle circle = new Circle(RADIUS);
        if (node.isStarting() || node.isEnding()) {
            circle.setFill(Color.LIGHTGRAY);
        } else {
            circle.setFill(Color.WHITE);
        }
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);
        Text text = new Text(node.getTitle());
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        getChildren().add(circle);
        getChildren().add(text);

        Tooltip t = new Tooltip(node.getDescription());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(circle, t);

        relocate(x - RADIUS, y - RADIUS);

        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.HAND);
        });

        final MenuItem addMenuItem = new MenuItem("Go to table entry");
        addMenuItem.setOnAction((ActionEvent event) -> {
            MainWindowController.getInstance().
                    switchToSection(node.getSection().getNameWithoutPrefix(), SectionDetailView.EXPAND_PARAGRAPHS);
        });
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(addMenuItem);

        setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                graph.drawForSection(node.getSection());
            } else {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
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
