package storybuilder.graph.view;

import storybuilder.graph.model.LinkSwitchGraphData;
import storybuilder.section.model.Link;

/**
 *
 * @author Francesco Bertolino
 */
public class SwitchConnection extends Connection
{

    private final LinkSwitchGraphData data;

    public SwitchConnection(final Node origin, final Node destination, final LinkSwitchGraphData data)
    {
        super(origin, destination);
        this.data = data;
    }

    @Override
    protected String getText()
    {
        return data.getText();
    }

    @Override
    protected void setStroke()
    {
        setStrokeWidth(2);
        // TODO
    }

}
