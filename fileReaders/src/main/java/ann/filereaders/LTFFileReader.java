package ann.filereaders;

import javax.xml.parsers.*;
import org.w3c.dom.*;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.xml.sax.*;

public class LTFFileReader {

    public static List<Sample> reader(String filename) {

        try {

            InputStream inputStream= new FileInputStream(filename);
            Reader reader = new InputStreamReader(inputStream,"UTF-8");
            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            /* To ignore dtd schema in the document - https://stackoverflow.com/questions/155101/make-documentbuilder-parse-ignore-dtd-references
             * To read XML files using DOM reader - https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm */

            dbFactory.setValidating(false);
            dbFactory.setNamespaceAware(true);
            dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            dbFactory.setFeature("http://xml.org/sax/features/validation", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

//            File xmlFile = new File(filename);
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//
//            /* To ignore dtd schema in the document - https://stackoverflow.com/questions/155101/make-documentbuilder-parse-ignore-dtd-references
//             * To read XML files using DOM reader - https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm */
//
//            dbFactory.setValidating(false);
//            dbFactory.setNamespaceAware(true);
//            dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
//            dbFactory.setFeature("http://xml.org/sax/features/validation", false);
//            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
//            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//
//            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("SEG");

            List<Sample> samples = new ArrayList<Sample>();


            for (int node = 0; node < nList.getLength(); node++) {
                Node nNode = nList.item(node);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String segId = eElement.getAttribute("id");
                    String sent = eElement.getElementsByTagName("ORIGINAL_TEXT").item(0).getTextContent();
                    samples.add(new Sample(segId, sent));
                }

            }
            return samples;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Sample>();
    }

}
