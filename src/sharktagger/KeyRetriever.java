package sharktagger;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class to lazily load the keys XML file.
 */
public class KeyRetriever {
    private String keysFilename;

    public KeyRetriever(String filename) {
        keysFilename = filename;
    }

    private Element keysElement;
    private Element getKeysElement() {
        if (keysElement != null) {
            return keysElement;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(keysFilename));
            keysElement = document.getDocumentElement();
        } catch (Exception e) {
            // I really don't care.
            e.printStackTrace();
        }
        return keysElement;
    }

    public String getJawsPublicKey() {
        Element element = getKeysElement();
        Element jawsElement = (Element) element.getElementsByTagName("jaws").item(0);
        return jawsElement.getElementsByTagName("public").item(0).getTextContent();
    }
    public String getJawsPrivateKey() {
        Element element = getKeysElement();
        Element jawsElement = (Element) element.getElementsByTagName("jaws").item(0);
        return jawsElement.getElementsByTagName("private").item(0).getTextContent();
    }
}
