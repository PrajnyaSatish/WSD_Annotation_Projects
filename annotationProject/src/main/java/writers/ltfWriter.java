package writers;

import com.google.gson.*;

import java.io.File;
import java.util.List;
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


public class ltfWriter {

    public static void writeToDoc(String outputFilePath, Document modifiedDoc) throws TransformerException {
        /* Write Document object which contains modified XML file to new XML file in output directory */
        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(modifiedDoc);
        StreamResult streamResult = new StreamResult(new File(outputFilePath));

        transformer.transform(domSource, streamResult);

    }
}
