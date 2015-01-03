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
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.MainWindowController;
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

    public void save() throws ValidationFailed
    {
        validate();
        try {
            updateStoriesFile();
            createStoryFile();
            loadCommands();
            loadEvents();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while saving story", ex);
        }
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
        FileManager.addElement(doc, "starting", "");
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
                story.loadCommands();
                story.loadEvents();
                Cache.getInstance().setStory(story);
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

    public boolean saveStoryElement(final IStoryElement element)
    {
        try {
            if (validateStoryElement(element)) {
                final Document doc = getXmlDoc();
                final Element newElement = element.build(doc);
                doc.getDocumentElement().appendChild(newElement);
                saveXmlDoc(doc);
                return true;
            }
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
            if (validateStoryElement(element)) {
                final Document doc = getXmlDoc();
                final Node node = FileManager.findElementNamed(element.getName(), doc);
                node.setTextContent(element.getContent());
                saveXmlDoc(doc);
                return true;
            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Story.class, "Error while updating story element", ex);
        }
        return false;
    }

    private boolean validateStoryElement(final IStoryElement element)
    {
        try {
            element.validate();
            return true;
        } catch (ValidationFailed ex) {
            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
        }
        return false;
    }

    private void saveXmlDoc(final Document doc) throws TransformerException
    {
        FileManager.saveDocument(doc, FileManager.getStoryFilenameWithAbsolutePath(this));
    }

    private Document getXmlDoc() throws IOException, SAXException, ParserConfigurationException
    {
        return FileManager.openDocument(FileManager.getStoryFilenameWithAbsolutePath(this));
    }

}
