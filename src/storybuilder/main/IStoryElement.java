/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storybuilder.main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import storybuilder.validation.ValidationFailed;

/**
 * Any story element writable to the XML file must implement this interface.
 * 
 * @author Francesco
 */
public interface IStoryElement
{

    /**
     * @return name, written as an attribute in the XML element
     */
    String getName();

    /**
     * @return prefix used to form the element name
     */
    String getPrefix();

    /**
     * @param doc XML file
     * @return XML element corresponding to the object
     */
    Element build(final Document doc);

    /**
     * @return Text content of the XML element corresponding to the object
     */
    String getContent();

    /**
     * Validates the element data
     * 
     * @throws ValidationFailed 
     */
    void validate() throws ValidationFailed;
}
