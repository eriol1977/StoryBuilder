package storybuilder.section.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.story.model.Story;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Link extends StoryElement
{

    public final static String ITEM_YES = "yes";

    public final static String ITEM_NO = "no";

    private String sectionId;

    private List<String> commandIds = new ArrayList<>();

    private Map<String, List<String>> itemIds = new HashMap<>();

    public Link(final String name, final String sectionId, final List<String> commandIds, final List<String> itemIds, final List<String> noItemIds, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.sectionId = sectionId;
        this.commandIds.addAll(commandIds);
        this.itemIds.put(ITEM_YES, itemIds);
        this.itemIds.put(ITEM_NO, noItemIds);
    }

    public Link(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] linkInfo = textContent.split(":");
        this.sectionId = linkInfo[0];
        if (linkInfo.length > 1) {
            commandIds.addAll(Arrays.asList(linkInfo[1].split(",")));
            if (linkInfo.length > 2) {
                final String[] itemsAndNoItems = linkInfo[2].split(",");
                final List<String> items = new ArrayList<>();
                final List<String> noItems = new ArrayList<>();
                for (final String id : itemsAndNoItems) {
                    if (id.startsWith("no_")) {
                        noItems.add(id);
                    } else {
                        items.add(id);
                    }
                }
                this.itemIds.put(ITEM_YES, items);
                this.itemIds.put(ITEM_NO, noItems);
            }
        }
    }

    public Link(final Link another)
    {
        this(another.getName(), another.getSectionId(), another.getCommandIds(),
                another.getItemIds(), another.getNoItemIds(), another.isDefault());
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
        sb.append(getSectionId());
        // commands
        if (!getCommandIds().isEmpty()) {
            sb.append(":");
            getCommandIds().stream().forEach(id -> sb.append(id).append(","));
            sb.delete(sb.length() - 1, sb.length()); // deletes last ','

            // items and no_items
            if (!getItemIds().isEmpty() || !getNoItemIds().isEmpty()) {
                sb.append(":");
            }
            if (!getItemIds().isEmpty()) {
                getItemIds().stream().forEach(id -> sb.append(id).append(","));
                if (getNoItemIds().isEmpty()) {
                    sb.delete(sb.length() - 1, sb.length()); // deletes last ','
                }
            }
            if (!getNoItemIds().isEmpty()) {
                getNoItemIds().stream().forEach(id -> sb.append(id).append(","));
                sb.delete(sb.length() - 1, sb.length()); // deletes last ','
            }
        }
        return sb.toString();
    }

    public String getReadableContent()
    {
        final Story story = Cache.getInstance().getStory();
        final StringBuilder sb = new StringBuilder();
        sb.append("Next section: ").append(getSectionId());
        // commands
        if (!getCommandIds().isEmpty()) {
            sb.append(" - Commands: ");
            getCommandIds().stream().forEach(id -> sb.append(story.getCommand(id).getDescription()).append(", "));
            sb.delete(sb.length() - 2, sb.length()); // deletes last ', '

            // items and no_items
            if (!getItemIds().isEmpty() || !getNoItemIds().isEmpty()) {
                sb.append(" - ");
            }
            if (!getItemIds().isEmpty()) {
                sb.append("Items to have: ");
                getItemIds().stream().forEach(id -> sb.append(story.getItem(id).getItemName()).append(", "));
                sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
                if (!getNoItemIds().isEmpty()) {
                    sb.append(" - ");
                }
            }
            if (!getNoItemIds().isEmpty()) {
                sb.append("Items not to have: ");
                getNoItemIds().stream().forEach(id -> sb.append(story.getItem(id).getItemName()).append(", "));
                sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
            }
        }
        return sb.toString();
    }

    @Override
    public void copyData(final IStoryElement another)
    {
        final Link anotherLink = (Link) another;
        setName(anotherLink.getName());
        setSectionId(anotherLink.getSectionId());
        setCommandIds(anotherLink.getCommandIds());
        setItemIds(anotherLink.getAllItemIds());
        setDefault(anotherLink.isDefault());
    }

    public static List<Link> load(final String fileName, final boolean defaultElements, final String sectionName) throws SBException
    {
        final List<Link> links = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsMatching(sectionName + "_link_\\d+", doc);
        elements.stream().forEach((element) -> {
            links.add(new Link(element, defaultElements));
        });
        return links;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (sectionId == null) {
            throw new ValidationFailed("Next section is required.");
        }
    }

    public String getNumber()
    {
        int index = getName().lastIndexOf("_");
        return getName().substring(index + 1, getName().length());
    }

    public String getSectionId()
    {
        return sectionId;
    }

    public List<String> getCommandIds()
    {
        return commandIds;
    }

    public Map<String, List<String>> getAllItemIds()
    {
        return itemIds;
    }

    public List<String> getItemIds()
    {
        final List<String> items = itemIds.get(ITEM_YES);
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public List<String> getNoItemIds()
    {
        final List<String> noItems = itemIds.get(ITEM_NO);
        if (noItems == null) {
            return new ArrayList<>();
        }
        return noItems;
    }

    public void setSectionId(final String sectionId)
    {
        this.sectionId = sectionId;
    }

    public void setCommandIds(final List<String> commandIds)
    {
        this.commandIds = commandIds;
    }

    public void setItemIds(final Map<String, List<String>> itemIds)
    {
        this.itemIds = itemIds;
    }

}
