package storybuilder.section.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.event.model.Event;
import storybuilder.item.model.Item;
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

    public final static String YES = "yes";

    public final static String NO = "no";

    private String sectionId;

    private List<String> commandIds = new ArrayList<>();

    private Map<String, List<String>> itemIds = new HashMap<>();

    private Map<String, List<String>> eventIds = new HashMap<>();

    public Link(final String name, final String sectionId, final List<String> commandIds,
            final List<String> itemIds, final List<String> noItemIds,
            final List<String> eventIds, final List<String> noEventIds,
            final boolean defaultElement)
    {
        super(name, defaultElement);
        this.sectionId = sectionId;
        this.commandIds.addAll(commandIds);
        this.itemIds.put(YES, itemIds);
        this.itemIds.put(NO, noItemIds);
        this.eventIds.put(YES, eventIds);
        this.eventIds.put(NO, noEventIds);
    }

    public Link(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] linkInfo = textContent.split(":");
        buildLink(linkInfo);
    }

    /**
     * Used to create a LinkSwitch
     *
     * @param linkInfo
     */
    Link(final String[] linkInfo)
    {
        super("", false);
        buildLink(linkInfo);
    }

    private void buildLink(final String[] linkInfo)
    {
        this.sectionId = linkInfo[0];
        if (linkInfo.length > 1) {
            commandIds.addAll(Arrays.asList(linkInfo[1].split(",")));
            if (linkInfo.length > 2) {
                final String[] stuff = linkInfo[2].split(",");
                final List<String> items = new ArrayList<>();
                final List<String> noItems = new ArrayList<>();
                final List<String> events = new ArrayList<>();
                final List<String> noEvents = new ArrayList<>();
                for (final String id : stuff) {
                    if (id.startsWith("no_" + Item.PREFIX)) {
                        noItems.add(id.substring(3, id.length()));
                    } else if (id.startsWith("no_" + Event.PREFIX)) {
                        noEvents.add(id.substring(3, id.length()));
                    } else if (id.startsWith(Item.PREFIX)) {
                        items.add(id);
                    } else if (id.startsWith(Event.PREFIX)) {
                        events.add(id);
                    }
                }
                this.itemIds.put(YES, items);
                this.itemIds.put(NO, noItems);
                this.eventIds.put(YES, events);
                this.eventIds.put(NO, noEvents);
            }
        }
    }

    public Link(final Link another)
    {
        this(another.getName(), another.getSectionId(), another.getCommandIds(),
                another.getItemIds(), another.getNoItemIds(), another.getEventIds(), another.getNoEventIds(), another.isDefault());
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

            // items, no_items, events and no_events
            if (!getItemIds().isEmpty() || !getNoItemIds().isEmpty() || !getEventIds().isEmpty() || !getNoEventIds().isEmpty()) {
                sb.append(":");
            }
            if (!getItemIds().isEmpty()) {
                getItemIds().stream().forEach(id -> sb.append(id).append(","));
                if (getNoItemIds().isEmpty() && getAllEventIds().isEmpty()) {
                    sb.delete(sb.length() - 1, sb.length()); // deletes last ','
                }
            }
            if (!getNoItemIds().isEmpty()) {
                getNoItemIds().stream().forEach(id -> sb.append("no_").append(id).append(","));
                if (getAllEventIds().isEmpty()) {
                    sb.delete(sb.length() - 1, sb.length()); // deletes last ','
                }
            }
            if (!getEventIds().isEmpty()) {
                getEventIds().stream().forEach(id -> sb.append(id).append(","));
                if (getNoEventIds().isEmpty()) {
                    sb.delete(sb.length() - 1, sb.length()); // deletes last ','
                }
            }
            if (!getNoEventIds().isEmpty()) {
                getNoEventIds().stream().forEach(id -> sb.append("no_").append(id).append(","));
                sb.delete(sb.length() - 1, sb.length()); // deletes last ','
            }
        }
        return sb.toString();
    }

    public String getReadableContent()
    {
        final Story story = Cache.getInstance().getStory();
        final StringBuilder sb = new StringBuilder();
        sb.append("[Next section: ").append(getSectionId()).append("] ");
        // commands
        if (!getCommandIds().isEmpty()) {
            sb.append("[Commands: ");
            getCommandIds().stream().forEach(id -> sb.append(story.getCommand(id).getDescription()).append(", "));
            sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
            sb.append("] ");

            // items, no_items, events and no_events
            if (!getItemIds().isEmpty()) {
                sb.append("[Items to have: ");
                getItemIds().stream().forEach(id -> sb.append(story.getItem(id).getItemName()).append(", "));
                sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
                sb.append("] ");
            }
            if (!getNoItemIds().isEmpty()) {
                sb.append("[Items not to have: ");
                getNoItemIds().stream().forEach(id -> sb.append(story.getItem(id).getItemName()).append(", "));
                sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
                sb.append("] ");
            }
            if (!getEventIds().isEmpty()) {
                sb.append("[Events to have: ");
                getEventIds().stream().forEach(id -> sb.append(story.getEvent(id).getDescription()).append(", "));
                sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
                sb.append("] ");
            }
            if (!getNoEventIds().isEmpty()) {
                sb.append("[Events not to have: ");
                getNoEventIds().stream().forEach(id -> sb.append(story.getEvent(id).getDescription()).append(", "));
                sb.delete(sb.length() - 2, sb.length()); // deletes last ', '
                sb.append("] ");
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
        setAllItemIds(anotherLink.getAllItemIds());
        setAllEventIds(anotherLink.getAllEventIds());
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
        final List<String> items = itemIds.get(YES);
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public List<String> getNoItemIds()
    {
        final List<String> noItems = itemIds.get(NO);
        if (noItems == null) {
            return new ArrayList<>();
        }
        return noItems;
    }

    public Map<String, List<String>> getAllEventIds()
    {
        return eventIds;
    }

    public List<String> getEventIds()
    {
        final List<String> events = eventIds.get(YES);
        if (events == null) {
            return new ArrayList<>();
        }
        return events;
    }

    public List<String> getNoEventIds()
    {
        final List<String> noEvents = eventIds.get(NO);
        if (noEvents == null) {
            return new ArrayList<>();
        }
        return noEvents;
    }

    public void setSectionId(final String sectionId)
    {
        this.sectionId = sectionId;
    }

    public void setCommandIds(final List<String> commandIds)
    {
        this.commandIds = commandIds;
    }

    public void setAllItemIds(final Map<String, List<String>> itemIds)
    {
        this.itemIds = itemIds;
    }

    public void setItemIds(final List<String> itemIds)
    {
        this.itemIds.put(YES, itemIds);
    }

    public void setNoItemIds(final List<String> noItemIds)
    {
        this.itemIds.put(NO, noItemIds);
    }

    public void setAllEventIds(final Map<String, List<String>> eventIds)
    {
        this.eventIds = eventIds;
    }

    public void setEventIds(final List<String> eventIds)
    {
        this.eventIds.put(YES, eventIds);
    }

    public void setNoEventIds(final List<String> noEventIds)
    {
        this.eventIds.put(NO, noEventIds);
    }

}
