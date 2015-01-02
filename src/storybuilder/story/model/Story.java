package storybuilder.story.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Story
{
    
    private final static String FILE_PATH = "resources/stories";
    
    private final String title;
    
    private final String fileName;
    
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
        
        FileManager.saveDocument(doc, FileManager.getStoryFilenameWithAbsolutePath(this));
    }
    
    public static Story load(final File file)
    {
        Story story = null;
        try {
            final Document doc = FileManager.openDocument(file);
            final String filename = file.getName().substring(0, file.getName().length() - 4); // removes ".xml"
            final NodeList storyElements = doc.getElementsByTagName("string");
            Node element;
            for (int i = 0; i < storyElements.getLength(); i++) {
                element = storyElements.item(i);
                if (FileManager.isElementNamedAs(element, "l_title")) {
                    story = new Story(element.getTextContent(), filename);
                    Cache.getInstance().setStory(story);
                }
                // TODO continue loading
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
}
