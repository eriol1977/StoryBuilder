package storybuilder.section.graph.view;

import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionNode extends StackPane
{

    final static double RADIUS = 30;

    private final SectionsGraph graph;

    private final Section section;

    private final double x;

    private final double y;

    SectionNode(final SectionsGraph graph, final Section section, final double x, final double y)
    {
        this.graph = graph;
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
        circle.setOnMouseClicked((MouseEvent event) -> {
            act();
        });
        text.setOnMouseClicked((MouseEvent event) -> {
            act();
        });
        getChildren().add(circle);
        getChildren().add(text);
        final StringBuilder sb = new StringBuilder();
        section.getParagraphs().stream().forEach(p -> sb.append(p.getText()).append("\n"));
        Tooltip t = new Tooltip(sb.toString());
        Tooltip.install(circle, t);

        relocate(x - RADIUS, y - RADIUS);
    }

    private void act()
    {
        graph.drawForSection(section);
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
