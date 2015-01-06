package storybuilder.story.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import storybuilder.command.model.Command;
import storybuilder.event.model.Event;
import storybuilder.item.model.Item;
import storybuilder.join.model.Join;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.section.model.Link;
import storybuilder.section.model.Paragraph;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;
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

    public void create() throws ValidationFailed, SBException
    {
        validate();
        updateStoriesFile();
        createStoryFile();
        loadStoryElements();
    }

    private void loadStoryElements() throws SBException
    {
        // even if the story is new, it must load elements from the default.xml file
        loadCommands();
        loadEvents();
        loadSections();
        loadItems();
        loadJoins();
    }

    private void updateStoriesFile() throws SBException
    {
        final Document storiesDoc = FileManager.openDocument(FILE_PATH);
        final Element storyElement = storiesDoc.createElement("story");
        storyElement.setAttribute("filename", fileName);
        storyElement.setTextContent(title);
        storiesDoc.getDocumentElement().appendChild(storyElement);
        FileManager.saveDocument(storiesDoc, FILE_PATH);
    }

    private void createStoryFile() throws SBException
    {
        try {
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
        } catch (ParserConfigurationException ex) {
            throw new SBException("Error while building story document.");
        }
    }

    public static Story load(final File file) throws SBException
    {
        Story story = null;
        final Document doc = FileManager.openDocument(file);
        final Node titleElement = FileManager.findElementNamed("l_title", doc);
        if (titleElement != null) {
            story = new Story(titleElement.getTextContent(), file.getName());
            Cache.getInstance().setStory(story);
            story.loadStoryElements();
        } else {
            throw new SBException("Story title not found.");
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
    private void loadCommands() throws SBException
    {
        commands.addAll(Command.load("resources/default.xml", true));
        commands.addAll(Command.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Command> getCommands()
    {
        return commands;
    }

    public Command getCommand(final String id)
    {
        return commands.stream().filter(item -> item.getName().equals(id)).collect(Collectors.toList()).get(0);
    }

    public void addCommand(final Command command) throws SBException
    {
        saveStoryElement(command);
        commands.add(command);
    }

    public void removeCommand(final Command command) throws SBException
    {
        removeStoryElement(command);
        commands.remove(command);
    }

    public void updateCommand(final Command command) throws SBException
    {
        updateStoryElement(command);
    }

    /////////// EVENTS
    private void loadEvents() throws SBException
    {
        events.addAll(Event.load("resources/default.xml", true));
        events.addAll(Event.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Event> getEvents()
    {
        return events;
    }

    public List<String> getEventIds()
    {
        return events.stream().map(e -> e.getName()).collect(Collectors.toList());
    }

    public void addEvent(final Event event) throws SBException
    {
        saveStoryElement(event);
        events.add(event);
    }

    public void removeEvent(final Event event) throws SBException
    {
        removeStoryElement(event);
        events.remove(event);
    }

    public void updateEvent(final Event event) throws SBException
    {
        updateStoryElement(event);
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

    private void loadSections() throws SBException
    {
        sections.addAll(Section.loadDefault());
        sections.addAll(Section.load(this));
    }

    public void addSection(final Section section) throws SBException
    {
        if (section.isEnding()) {
            addEnding(section.getNameWithoutPrefix());
        }
        saveSectionElements(section);
        sections.add(section);
        incrementLastSectionId();
    }

    private void saveSectionElements(final Section section) throws SBException
    {
        for (final Paragraph paragraph : section.getParagraphs()) {
            saveStoryElement(paragraph);
        }
        for (final Link link : section.getLinks()) {
            saveStoryElement(link);
        }
        if (section.getGet() != null) {
            saveStoryElement(section.getGet());
        }
        if (section.getDrop() != null) {
            saveStoryElement(section.getDrop());
        }
    }

    public void updateSection(final Section oldSection, final Section newSection) throws SBException
    {
        updateEndings(newSection.getNameWithoutPrefix(), newSection.isEnding());
        deleteSectionElements(oldSection);
        saveSectionElements(newSection);
    }

    public void deleteSection(final Section section) throws SBException
    {
        checkRelatedElement(section);

        if (section.isEnding()) {
            removeEnding(section.getNameWithoutPrefix());
        }
        deleteSectionElements(section);
        sections.remove(section);
    }

    private void checkRelatedElement(final Section section) throws SBException
    {
        for (final Item item : items) {
            if (item.getSectionId().equals(section.getNameWithoutPrefix())) {
                throw new SBException("Cannot delete section, because it's related to item \"" + item.getNameWithoutPrefix() + "\"");
            }
        }
        for (final Join join : joins) {
            if (join.getSectionId().equals(section.getNameWithoutPrefix())) {
                throw new SBException("Cannot delete section, because it's related to join \"" + join.getNameWithoutPrefix() + "\"");
            }
        }
    }

    private void deleteSectionElements(final Section section) throws SBException
    {
        final Document doc = getXmlDoc();
        final List<Node> sectionElements = FileManager.findElementsStartingWith(section.getName(), doc);
        sectionElements.stream().forEach(node -> doc.getDocumentElement().removeChild(node));
        saveXmlDoc(doc);
    }

    public int getLastSectionId() throws SBException
    {
        final Document doc = getXmlDoc();
        final Node sectionsElement = FileManager.findElementNamed("sections", doc);
        return Integer.valueOf(sectionsElement.getTextContent());
    }

    public void incrementLastSectionId() throws SBException
    {
        final Document doc = getXmlDoc();
        final Node sectionsElement = FileManager.findElementNamed("sections", doc);
        int lastSectionId = Integer.valueOf(sectionsElement.getTextContent());
        sectionsElement.setTextContent(String.valueOf(++lastSectionId));
        saveXmlDoc(doc);
    }

    public void addEnding(final String sectionId) throws SBException
    {
        final Document doc = getXmlDoc();
        final Node endingElement = FileManager.findElementNamed("ending", doc);
        final String oldValue = endingElement.getTextContent();
        final String newValue = oldValue.isEmpty() ? sectionId : oldValue + "," + sectionId;
        endingElement.setTextContent(newValue);
        saveXmlDoc(doc);
    }

    public void removeEnding(final String sectionId) throws SBException
    {
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
    }

    public void updateEndings(final String sectionId, final boolean isEnding) throws SBException
    {
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
    }

    ////////// ITEMS
    private void loadItems() throws SBException
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
        return items.stream().map(i -> i.getName()).collect(Collectors.toList());
    }

    public Item getItem(final String id)
    {
        return items.stream().filter(item -> item.getName().equals(id)).collect(Collectors.toList()).get(0);
    }

    public void addItem(final Item item) throws SBException
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
        saveStoryElement(item);
        items.add(item);
    }

    public void removeItem(final Item item) throws SBException
    {
        if (!item.getSectionId().isEmpty()) {
            final Section section = getSection(item.getSectionId());
            if (section != null) {
                deleteSection(section);
            }
        }
        removeStoryElement(item);
        items.remove(item);
    }

    public void updateItem(final Item item) throws SBException
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
        updateStoryElement(item);
    }

    ////////// JOINS
    private void loadJoins() throws SBException
    {
        joins.addAll(Join.load("resources/default.xml", true));
        joins.addAll(Join.load(FileManager.getStoryFilenameWithAbsolutePath(this), false));
    }

    public List<Join> getJoins()
    {
        return joins;
    }

    public void addJoin(final Join join) throws SBException
    {
        final String description = join.getTemporarySectionText();
        final int newSectionId = getLastSectionId() + 1;
        final Section section = new Section(Section.PREFIX + newSectionId, false);
        final List<Paragraph> paragraphs = new ArrayList<>(1);
        paragraphs.add(new Paragraph(section.getName() + "_1", description, false));
        section.setParagraphs(paragraphs);
        addSection(section);

        join.setSectionId(String.valueOf(newSectionId));
        saveStoryElement(join);
        joins.add(join);
    }

    public void removeJoin(final Join join) throws SBException
    {
        if (!join.getSectionId().isEmpty()) {
            final Section section = getSection(join.getSectionId());
            if (section != null) {
                deleteSection(section);
            }
        }
        removeStoryElement(join);
        joins.remove(join);
    }

    public void updateJoin(final Join join) throws SBException
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
        updateStoryElement(join);
    }

    ////////// XML
    public void saveStoryElement(final IStoryElement element) throws SBException
    {
        final Document doc = getXmlDoc();
        final Element newElement = element.build(doc);
        doc.getDocumentElement().appendChild(newElement);
        saveXmlDoc(doc);
    }

    private void removeStoryElement(final IStoryElement element) throws SBException
    {
        final Document doc = getXmlDoc();
        final Node node = FileManager.findElementNamed(element.getName(), doc);
        doc.getDocumentElement().removeChild(node);
        saveXmlDoc(doc);
    }

    private void updateStoryElement(final IStoryElement element) throws SBException
    {
        final Document doc = getXmlDoc();
        final Node node = FileManager.findElementNamed(element.getName(), doc);
        node.setTextContent(element.getContent());
        saveXmlDoc(doc);
    }

    public void saveXmlDoc(final Document doc) throws SBException
    {
        FileManager.saveDocument(doc, FileManager.getStoryFilenameWithAbsolutePath(this));
    }

    public Document getXmlDoc() throws SBException
    {
        return FileManager.openDocument(FileManager.getStoryFilenameWithAbsolutePath(this));
    }

}
