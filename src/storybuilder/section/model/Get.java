package storybuilder.section.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.event.model.Event;
import storybuilder.item.model.Item;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Get extends StoryElement
{

    /**
     * Item/event ids
     */
    private List<String> ids = new ArrayList<>();

    public Get(final String name, final boolean defaultElement, final String... ids)
    {
        super(name, defaultElement);
        this.ids.addAll(Arrays.asList(ids));
    }

    public Get(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] loadedIds = textContent.split(",");
        ids.addAll(Arrays.asList(loadedIds));
    }

    public Get(final Get another)
    {
        this(another.getName(), another.isDefault(), another.getIdsArray());
    }

    @Override
    public String getPrefix()
    {
        return "";
    }

    @Override
    public String getContent()
    {
        final StringBuilder sb = new StringBuilder();
        ids.stream().forEach(id -> sb.append(id).append(","));
        sb.delete(sb.length() - 1, sb.length()); // deletes last ','
        return sb.toString();
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final Get anotherParagraph = (Get) another;
        setName(anotherParagraph.getName());
        setIds(anotherParagraph.getIds());
        setDefault(anotherParagraph.isDefault());
    }

    public static Get load(final String fileName, final String sectionName) throws SBException
    {
        final Document doc = FileManager.openDocument(fileName);
        final Node element = FileManager.findElementNamed(sectionName + "_get", doc);
        if (element != null) {
            return new Get(element, false);
        }
        return null;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (ids.isEmpty()) {
            throw new ValidationFailed("A minimum of one item/event is required.");
        }
    }

    public List<String> getIds()
    {
        return ids;
    }

    public List<String> getItemIds()
    {
        return ids.stream().filter(id -> id.startsWith(Item.PREFIX)).collect(Collectors.toList());
    }

    public List<String> getEventIds()
    {
        return ids.stream().filter(id -> id.startsWith(Event.PREFIX)).collect(Collectors.toList());
    }

    public String[] getIdsArray()
    {
        return ids.toArray(new String[0]);
    }

    public void setIds(List<String> ids)
    {
        this.ids = ids;
    }

}
