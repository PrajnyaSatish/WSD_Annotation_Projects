package ann.filereaders;

import javax.xml.parsers.*;

import com.google.gson.JsonObject;
import org.w3c.dom.*;


import java.io.*;
import java.util.List;
import java.nio.charset.Charset;
import org.w3c.dom.Document;
import org.xml.sax.*;


import sensePredictors.SensePredictor;

public class LTFFileReader {

    private String docId;
    private String docLang;
    private static final Charset UTF_8 = Charset.forName("UTF-8");


    public String getDocId() {
        return this.docId;
    }

    public String getDocLang() {
        return this.docLang;
    }

    public Document reader(String filename, SensePredictor depParseObj) {

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
//
//            /* To ignore dtd schema in the document - https://stackoverflow.com/questions/155101/make-documentbuilder-parse-ignore-dtd-references
//             * To read XML files using DOM reader - https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm */

            doc.getDocumentElement().normalize();

            Node docDetails = doc.getElementsByTagName("DOC").item(0);
            Element docElements = (Element) docDetails;
            this.docLang = docElements.getAttribute("lang");
            this.docId  = docElements.getAttribute("id");

            NodeList nList = doc.getElementsByTagName("SEG");

            for (int node = 0; node < nList.getLength(); node++) {
                Node nNode = nList.item(node);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String segId = eElement.getAttribute("id").split("-")[1];
                    String sent = eElement.getElementsByTagName("ORIGINAL_TEXT").item(0).getTextContent();

                    List<List<JsonObject>> parsedSentence = depParseObj.parseString(sent);
                    for (List<JsonObject> listOfSenses : parsedSentence) {
                        for (JsonObject vnSense : listOfSenses) {
                            String senseNum = String.valueOf(vnSense.get("Sense"));
                            String vnSenseNumTORet = senseNum.substring(1, senseNum.length()-1); //TODO: To remove the non-UTF quotations. Need a more elegant way to deal with this.
                            String vnTokenNum = String.valueOf(vnSense.get("TokenNum"));
                            NodeList tokens = eElement.getElementsByTagName("TOKEN");

                            for (int j = 0; j < tokens.getLength(); j++) {
                                Node tok = tokens.item(j);
                                String tokenId = String.valueOf(tok.getAttributes().getNamedItem("id").getNodeValue());
                                if(tokenId.equals("token-"+segId+"-"+vnTokenNum)){
                                    ((Element)tok).setAttribute("vnSense", vnSenseNumTORet);
                                }

                            }

                        }
                    }
                }

            }
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
