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
import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Section;
import storybuilder.section.view.SectionDetailView;

/**
 *
 * @author Francesco Bertolino
 */
public class Node extends StackPane
{

    final static double RADIUS = 30;

    private final Section section;

    private final double x;

    private final double y;

    Node(final Graph graph, final Section section, final double x, final double y)
    {
        this.section = section;
        this.x = x;
        this.y = y;

        Circle circle = new Circle(RADIUS);
        if (section.getNameWithoutPrefix().equals("1") || section.isEnding()) {
            circle.setFill(Color.LIGHTGRAY);
        } else {
            circle.setFill(Color.WHITE);
        }
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);
        Text text = new Text(section.getNameWithoutPrefix());
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        getChildren().add(circle);
        getChildren().add(text);
        final StringBuilder sb = new StringBuilder();
        section.getParagraphs().stream().forEach(p -> sb.append(p.getText()).append("\n"));

        Tooltip t = new Tooltip(sb.toString());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(circle, t);

        relocate(x - RADIUS, y - RADIUS);

        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.HAND);
        });

        final MenuItem addMenuItem = new MenuItem("Go to table entry");
        addMenuItem.setOnAction((ActionEvent event) -> {
            MainWindowController.getInstance().
                    switchToSection(section.getNameWithoutPrefix(), SectionDetailView.EXPAND_PARAGRAPHS);
        });
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(addMenuItem);

        setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                graph.drawForSection(section);
            } else {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public Section getSection()
    {
        return section;
    }

}
