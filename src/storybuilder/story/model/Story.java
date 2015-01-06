package storybuilder.story.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import storybuilder.command.model.Command;
import storybuilder.event.model.Event;
import storybuilder.item.model.Item;
import storybuilder.join.model.Join;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.section.model.Paragraph;
import storybuilder.section.model.Section;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Story
{

    private final static String FILE_PATH = "resources/stories.xml";

    private final String title;

    private final String fileName;

    private final List<Command> commands = new ArrayList<>();

    private final List<Event> events = new ArrayList<>();

    private final List<Item> items = new ArrayList<>();

    private final List<Join> joins = new ArrayList<>();

    private final List<Section> sections = new ArrayList<>();

    public Story(String title, String fileName)
    {
        this.title = title;
        this.fileName = fileName;
    }

    public String getTitle()
    {
        return title;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void create() throws ValidationFailed
    {
        validate();
        try {
            updateStoriesFile();
            createStoryFile();
            loadStoryElements();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while saving story", ex);
        }
    }

    private void loadStoryElements()
    {
        // even if the story is new, it must load elements from the default.xml file
        loadCommands();
        loadEvents();
        loadItems();
        loadJoins();
        loadSections();
    }

    private void updateStoriesFile() throws ParserConfigurationException, TransformerException, DOMException, SAXException, IOException
    {
        final Document storiesDoc = FileManager.openDocument(FILE_PATH);
        final Element storyElement = storiesDoc.createElement("story");
        storyElement.setAttribute("filename", fileName);
        storyElement.setTextContent(title);
        storiesDoc.getDocumentElement().appendChild(storyElement);
        FileManager.saveDocument(storiesDoc, FILE_PATH);
    }

    private void createStoryFile() throws ParserConfigurationException, TransformerException
    {
        final DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder icBuilder = icFactory.newDocumentBuilder();
        final Document doc = icBuilder.newDocument();
        final Element mainRootElement = doc.createElement("resources");
        doc.appendChild(mainRootElement);

        FileManager.addElement(doc, "l_title", title);
        FileManager.addElement(doc, "sections", String.valueOf(0));
        FileManager.addElement(doc, "starting", String.valueOf(1));
        FileManager.addElement(doc, "ending", "");

        saveXmlDoc(doc);
    }

    public static Story load(final File file)
    {
        Story story = null;
        try {
            final Document doc = FileManager.openDocument(file);
            final Node titleElement = FileManager.findElementNamed("l_title", doc);
            if (titleElement != null) {
                story = new Story(titleElement.getTextContent(), file.getName());
                Cache.getInstance().setStory(story);
                story.loadStoryElements();
            } else {
                ErrorManager.showErrorMessage(Story.class, "Error while loading story");
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while loading story", ex);
        }
        return story;
    }

    public static void delete(final File file)
    {
        file.delete();
        Cache.getInstance().setStory(new NullStory());
    }

    private void validate() throws ValidationFailed
    {
        if (title == null || title.isEmpty()) {
            throw new ValidationFailed("Title must be at least one character long");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new ValidationFailed("Filename must be at least one character long");
        }
        if (fileName.contains(" ")) {
            throw new ValidationFailed("Filename cannot contain empty spaces");
        }
        final List<Integer> indexes = FileManager.checkFilenameForIllegalChars(fileName);
        if (!indexes.isEmpty()) {
            throw new ValidationFailed("Filename cannot contain illegal characters");
        }
    }

    /////////// COMMANDS
    private void loadCommands()
    {
        commands.addAll(Command.load("resources/default.xml", true));
        commands.addAll(Command.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Command> getCommands()
    {
        return commands;
    }

    public boolean addCommand(final Command command)
    {
        final boolean result = saveStoryElement(command);
        if (result) {
            commands.add(command);
        }
        return result;
    }

    public boolean removeCommand(final Command command)
    {
        final boolean result = removeStoryElement(command);
        if (result) {
            commands.remove(command);
        }
        return result;
    }

    public boolean updateCommand(final Command command)
    {
        return updateStoryElement(command);
    }

    /////////// EVENTS
    private void loadEvents()
    {
        events.addAll(Event.load("resources/default.xml", true));
        events.addAll(Event.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Event> getEvents()
    {
        return events;
    }

    public boolean addEvent(final Event event)
    {
        final boolean result = saveStoryElement(event);
        if (result) {
            events.add(event);
        }
        return result;
    }

    public boolean removeEvent(final Event event)
    {
        final boolean result = removeStoryElement(event);
        if (result) {
            events.remove(event);
        }
        return result;
    }

    public boolean updateEvent(final Event event)
    {
        return updateStoryElement(event);
    }

    /////////// SECTIONS
    public List<Section> getSections()
    {
        return sections;
    }

    public Section getSection(final String id)
    {
        for (final Section section : sections) {
            if (section.getNameWithoutPrefix().equals(id)) {
                return section;
            }
        }
        return null;
    }

    private void loadSections()
    {
        sections.addAll(Section.loadDefault());
        sections.addAll(Section.load(this));
    }

    public boolean addSection(final Section section)
    {
        boolean result = true;
        if (section.isEnding()) {
            result = addEnding(section.getNameWithoutPrefix());
        }
        if (result) {
            result = saveSectionElements(section);
            if (result) {
                sections.add(section);
                incrementLastSectionId();
            }
        }
        return result;
    }

    private boolean saveSectionElements(final Section section)
    {
        boolean result = true;
        for (final Paragraph paragraph : section.getParagraphs()) {
            result = saveStoryElement(paragraph);
            if (!result) {
                break;
            }
        }
        return result;
    }

    public boolean updateSection(final Section oldSection, final Section newSection)
    {
        boolean result = updateEndings(newSection.getNameWithoutPrefix(), newSection.isEnding());
        if (result) {
            return deleteSectionElements(oldSection) && saveSectionElements(newSection);
        }
        return result;
    }

    public boolean deleteSection(final Section section)
    {
        boolean result = true;
        if (section.isEnding()) {
            result = removeEnding(section.getNameWithoutPrefix());
        }
        if (result) {
            result = deleteSectionElements(section);
            if (result) {
                sections.remove(section);
            }
        }
        return result;
    }

    private boolean deleteSectionElements(final Section section)
    {
        boolean result = true;
        if (section.isEnding()) {
            result = removeEnding(section.getNameWithoutPrefix());
        }
        for (final Paragraph paragraph : section.getParagraphs()) {
            result = removeStoryElement(paragraph);
            if (!result) {
                break;
            }
        }
        return result;
    }

    public int getLastSectionId()
    {
        int lastSectionId = -1;
        try {
            final Document doc = getXmlDoc();
            final Node sectionsElement = FileManager.findElementNamed("sections", doc);
            lastSectionId = Integer.valueOf(sectionsElement.getTextContent());
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while getting next section id", ex);
        }
        return lastSectionId;
    }

    public void incrementLastSectionId()
    {
        try {
            final Document doc = getXmlDoc();
            final Node sectionsElement = FileManager.findElementNamed("sections", doc);
            int lastSectionId = Integer.valueOf(sectionsElement.getTextContent());
            sectionsElement.setTextContent(String.valueOf(++lastSectionId));
            saveXmlDoc(doc);
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while incrementing section id", ex);
        }
    }

    public boolean addEnding(final String sectionId)
    {
        try {
            final Document doc = getXmlDoc();
            final Node endingElement = FileManager.findElementNamed("ending", doc);
            final String oldValue = endingElement.getTextContent();
            final String newValue = oldValue.isEmpty() ? sectionId : oldValue + "," + sectionId;
            endingElement.setTextContent(newValue);
            saveXmlDoc(doc);
            return true;
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while adding ending", ex);
        }
        return false;
    }

    public boolean removeEnding(final String sectionId)
    {
        try {
            final Document doc = getXmlDoc();
            final Node endingElement = FileManager.findElementNamed("ending", doc);
            final StringBuilder sb = new StringBuilder();
            final String[] endingSectionsIds = endingElement.getTextContent().split(",");
            for (final String id : endingSectionsIds) {
                if (!id.equals(sectionId)) {
                    sb.append(id).append(",");
                }
            }
            sb.delete(sb.length() - 1, sb.length());
            endingElement.setTextContent(sb.toString());
            saveXmlDoc(doc);
            return true;
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while removing ending", ex);
        }
        return false;
    }

    public boolean updateEndings(final String sectionId, final boolean isEnding)
    {
        try {
            final Document doc = getXmlDoc();
            final Node endingElement = FileManager.findElementNamed("ending", doc);
            final String[] endingSectionsIds = endingElement.getTextContent().split(",");
            boolean found = false;
            for (final String id : endingSectionsIds) {
                if (id.equals(sectionId)) {
                    found = true;
                    break;
                }
            }
            if (isEnding && !found) {
                addEnding(sectionId);
            } else if (!isEnding && found) {
                removeEnding(sectionId);
            }
            return true;
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while updating endings", ex);
        }
        return false;
    }

    ////////// ITEMS
    private void loadItems()
    {
        items.addAll(Item.load("resources/default.xml", true));
        items.addAll(Item.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Item> getItems()
    {
        return items;
    }

    public List<String> getItemIds()
    {
        final List<String> itemIds = new ArrayList<>(items.size());
        items.stream().forEach(i -> itemIds.add(i.getName()));
        return itemIds;
    }

    public boolean addItem(final Item item)
    {
        final String description = item.getTemporarySectionText();
        if (!description.isEmpty()) {
            final int newSectionId = getLastSectionId() + 1;
            final Section section = new Section(Section.PREFIX + newSectionId, false);
            final List<Paragraph> paragraphs = new ArrayList<>(1);
            paragraphs.add(new Paragraph(section.getName() + "_1", description, false));
            section.setParagraphs(paragraphs);
            addSection(section);
            item.setSectionId(String.valueOf(newSectionId));
        }
        final boolean result = saveStoryElement(item);
        if (result) {
            items.add(item);
        }
        return result;
    }

    public boolean removeItem(final Item item)
    {
        if (!item.getSectionId().isEmpty()) {
            final Section section = getSection(item.getSectionId());
            if (section != null) {
                deleteSection(section);
            }
        }

        final boolean result = removeStoryElement(item);
        if (result) {
            items.remove(item);
        }
        return result;
    }

    public boolean updateItem(final Item item)
    {
        if (!item.getSectionId().isEmpty()) {
            final Section section = getSection(item.getSectionId());
            if (section != null) {
                final String description = item.getTemporarySectionText();
                final Paragraph paragraph = section.getParagraphs().get(0);
                if (!paragraph.getText().equals(description)) {
                    paragraph.setText(description);
                    updateStoryElement(paragraph);
                }
            }
        }
        return updateStoryElement(item);
    }

    ////////// JOINS
    private void loadJoins()
    {
        joins.addAll(Join.load("resources/default.xml", true));
        joins.addAll(Join.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Join> getJoins()
    {
        return joins;
    }

    public boolean addJoin(final Join join)
    {
        final String description = join.getTemporarySectionText();
        final int newSectionId = getLastSectionId() + 1;
        final Section section = new Section(Section.PREFIX + newSectionId, false);
        final List<Paragraph> paragraphs = new ArrayList<>(1);
        paragraphs.add(new Paragraph(section.getName() + "_1", description, false));
        section.setParagraphs(paragraphs);
        addSection(section);

        join.setSectionId(String.valueOf(newSectionId));
        final boolean result = saveStoryElement(join);
        if (result) {
            joins.add(join);
        }
        return result;
    }

    public boolean removeJoin(final Join join)
    {
        if (!join.getSectionId().isEmpty()) {
            final Section section = getSection(join.getSectionId());
            if (section != null) {
                deleteSection(section);
            }
        }

        final boolean result = removeStoryElement(join);
        if (result) {
            joins.remove(join);
        }
        return result;
    }

    public boolean updateJoin(final Join join)
    {
        if (!join.getSectionId().isEmpty()) {
            final Section section = getSection(join.getSectionId());
            if (section != null) {
                final String description = join.getTemporarySectionText();
                final Paragraph paragraph = section.getParagraphs().get(0);
                if (!paragraph.getText().equals(description)) {
                    paragraph.setText(description);
                    updateStoryElement(paragraph);
                }
            }
        }
        return updateStoryElement(join);
    }

    ////////// XML
    public boolean saveStoryElement(final IStoryElement element)
    {
        try {
            final Document doc = getXmlDoc();
            final Element newElement = element.build(doc);
            doc.getDocumentElement().appendChild(newElement);
            saveXmlDoc(doc);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while saving new story element", ex);
        }
        return false;
    }

    private boolean removeStoryElement(final IStoryElement element)
    {
        try {
            final Document doc = getXmlDoc();
            final Node node = FileManager.findElementNamed(element.getName(), doc);
            doc.getDocumentElement().removeChild(node);
            saveXmlDoc(doc);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while deleting story element", ex);
        }
        return false;
    }

    private boolean updateStoryElement(final IStoryElement element)
    {
        try {
            final Document doc = getXmlDoc();
            final Node node = FileManager.findElementNamed(element.getName(), doc);
            node.setTextContent(element.getContent());
            saveXmlDoc(doc);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while updating story element", ex);
        }
        return false;
    }

    public void saveXmlDoc(final Document doc) throws TransformerException
    {
        FileManager.saveDocument(doc, FileManager.getStoryFilenameWithAbsolutePath(this));
    }

    public Document getXmlDoc() throws IOException, SAXException, ParserConfigurationException
    {
        return FileManager.openDocument(FileManager.getStoryFilenameWithAbsolutePath(this));
    }

}
