package ann.annotators;

import ann.filereaders.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.*;

import sensePredictors.SensePredictor;
import sensePredictors.verbnetSensePredictor;
import utils.FilePermissions;
import utils.FilenameFilter;
import writers.ltfWriter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


import java.io.*;

public class ltfAnnotator {

    public static void main(String[] args) {


        SensePredictor depParseObj = new verbnetSensePredictor();

        File dir = new File("/home/prajnya/Desktop/WSD/data/ltf/ltf");
        // list the files using FileFilter
        File[] files = dir.listFiles(new FilenameFilter(new String[]{"xml"}));
        for (File f : files) {
            System.out.println("file: " + f.getName());

            JsonArray resultsOut = new JsonArray();
            List<Sample> listOfsents = LTFFileReader.reader(f.getAbsolutePath());

            for (Sample segment : listOfsents) {
                // Pass the entire string as input to the dependency parser to get the predicates in the sentence and
                // other info like token number, lemma, predicate and verbnet sense.
                System.out.println(segment.getOrigSent());
                List<List<JsonObject>> parsedSentence = depParseObj.parseString(segment.getOrigSent());
                JsonObject jsonifiedParsedSentence = ltfWriter.jsonConverter(segment.getId(), segment.getOrigSent(), parsedSentence);
                resultsOut.add(jsonifiedParsedSentence);

            }
            //Write JSON file

            try (FileWriter writer = new FileWriter("../outFiles/ltf/"+f.getName()+".json")) {
                FilePermissions fp = new FilePermissions();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(resultsOut, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
