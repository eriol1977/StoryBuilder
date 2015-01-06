package storybuilder.event.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Event extends StoryElement
{

    public final static String PREFIX = "e_";

    private final SimpleStringProperty description;

    public Event(final String name, final String description, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.description = new SimpleStringProperty(description);
    }

    public Event(Node node, final boolean defaultElement)
    {
        super(node, defaultElement);
        description = new SimpleStringProperty(textContent);
    }

    public Event(final Event another)
    {
        this(another.getName(), another.getDescription(), another.isDefault());
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

    @Override
    public String getContent()
    {
        return getDescription();
    }

    @Override
    public void copyData(final IStoryElement another)
    {
        final Event anotherEvent = (Event) another;
        setName(anotherEvent.getName());
        setDescription(anotherEvent.getDescription());
        setDefault(anotherEvent.isDefault());
    }

    public String getDescription()
    {
        return description.get();
    }

    public void setDescription(final String description)
    {
        this.description.set(description);
    }

    public static List<Event> load(final String fileName, final boolean defaultElements) throws SBException
    {
        final List<Event> events = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsStartingWith(PREFIX, doc);
        elements.stream().forEach((element) -> {
            events.add(new Event(element, defaultElements));
        });
        return events;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (description == null || description.get().isEmpty()) {
            throw new ValidationFailed("Description must be at least one character long");
        }
    }

}
