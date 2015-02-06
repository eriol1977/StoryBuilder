package storybuilder.graph.view;

import javafx.scene.control.Label;
import storybuilder.graph.view.connection.ConnectionView;
import javafx.scene.layout.Pane;
import storybuilder.main.view.AbstractView;

/**
 *
 * @author Francesco Bertolino
 */
public class Graph extends AbstractView
{

    private final Pane canvas;

    public Graph(final double width, final double height)
    {
        canvas = new Pane();
        canvas.setPrefSize(width, height);
        add(canvas);
    }

    public void clear()
    {
        canvas.getChildren().clear();
    }

    public void drawNode(final NodeView nodeView)
    {
        canvas.getChildren().add(nodeView);
    }

    public void drawConnection(final ConnectionView connectionView)
    {
        canvas.getChildren().add(connectionView);
    }

    public void drawEmpty()
    {
        canvas.getChildren().add(new Label("No section 1 detected!"));
    }
}
