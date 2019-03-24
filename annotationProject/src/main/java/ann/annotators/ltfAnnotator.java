package ann.annotators;

import ann.filereaders.*;
import com.google.gson.JsonArray;


import org.w3c.dom.Document;
import sensePredictors.SensePredictor;
import sensePredictors.verbnetSensePredictor;
import utils.FilenameFilter;
import writers.ltfWriter;

import javax.xml.transform.TransformerException;

import java.io.*;

public class ltfAnnotator {

    public static void main(String[] args) throws TransformerException {


        SensePredictor depParseObj = new verbnetSensePredictor();
        ltfWriter writer = new ltfWriter();

        File dir = new File("/home/prajnya/Desktop/WSD/data/ltf/HC000Q.ltf/");
        String outputDir = "/home/prajnya/Desktop/WSD/outFiles/ltf/HC000Q.ltf/";
        // list the files using FileFilter
        File[] files = dir.listFiles(new FilenameFilter(new String[]{"xml"}));
        for (File f : files) {
            System.out.println("file: " + f.getName());

            JsonArray resultsOut = new JsonArray();
            LTFFileReader docObject = new LTFFileReader();
            Document modifiedDoc = docObject.reader(f.getAbsolutePath(), depParseObj);
            String outpath = outputDir+String.valueOf(f.getName());
            writer.writeToDoc(outpath, modifiedDoc);

        }


    }
}
