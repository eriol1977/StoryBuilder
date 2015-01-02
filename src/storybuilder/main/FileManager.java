package storybuilder.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class FileManager
{

    public static Document openDocument(final String fileName) throws ParserConfigurationException, SAXException, IOException
    {
        final File file = new File(fileName + ".xml");
        return openDocument(file);
    }

    public static Document openDocument(final File file) throws ParserConfigurationException, SAXException, IOException
    {
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        final Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public static boolean isElementNamedAs(final Node element, final String name)
    {
        return element.getAttributes().getNamedItem("name").getTextContent().equals(name);
    }

    public static void saveDocument(final Document doc, final String fileName) throws TransformerConfigurationException, TransformerException
    {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult file = new StreamResult(new File(fileName + ".xml"));
        transformer.transform(source, file);
    }

    public static Element addElement(final Document doc, final String name, final String value)
    {
        final Element el = doc.createElement("string");
        el.setAttribute("name", name);
        el.setTextContent(value);
        doc.getDocumentElement().appendChild(el);
        return el;
    }

    public static String getStoryFilenameWithAbsolutePath(final Story story)
    {
        return Cache.getInstance().getPreferences().getDirectoryPath() + "\\" + story.getFileName();
    }

    /**
     * Code taken from:
     * http://stackoverflow.com/questions/2357133/identifying-os-dependent-invalid-filename-characters-in-java-6-not-7
     *
     * @param filename
     * @return indexes of illegal character inside filename
     */
    public static List<Integer> checkFilenameForIllegalChars(final String filename)
    {
        List<Integer> invalidIndices = new LinkedList<>();
        String osName = System.getProperty("os.name");
        String invalidChars;
        if (osName.contains("win") || osName.contains("Win")) {
            invalidChars = "\\/:*?\"<>|";
        } else if (osName.contains("mac") || osName.contains("Mac")) {
            invalidChars = "/:";
        } else { // assume Unix/Linux
            invalidChars = "/";
        }

        char[] chars = filename.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((invalidChars.indexOf(chars[i]) >= 0) // OS-invalid
                    || (chars[i] < '\u0020') // ctrls
                    || (chars[i] > '\u007e' && chars[i] < '\u00a0') // ctrls
                    ) {
                invalidIndices.add(i);
            }
        }

        return invalidIndices;
    }
}
