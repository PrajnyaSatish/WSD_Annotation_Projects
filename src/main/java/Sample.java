import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.io.*;
import java.util.ArrayList;

class Sample {

    public static boolean checkList(ArrayList<String> arr, String targetValue) {
        final Set<String> arrSet = new HashSet<>(arr);
        return arrSet.contains(targetValue);
    }

    public static void main(String[] args)
    {
        SensePredictor semlinkDepParser = new SemlinkSensePredictor();
        JsonArray resultsOut = new JsonArray();

        File dir = new File("/home/prajnya/Desktop/SRL_VN_NLP/data/bolt-annotation/word");

        // list the files using FileFilter
        File[] files = dir.listFiles(new FilenameFilter());
        for (File f : files) {
            System.out.println("file: " + f.getName());
            ArrayList<String> completedStrings = new ArrayList<>();
            try {
                BufferedReader b = new BufferedReader(new FileReader(f));
                String readLine;
                int sentence_index = 0;
                // Read from file using a buffer line by line
                while ((readLine = b.readLine()) != null) {
                    String sentence = readLine;
                    if(!checkList(completedStrings, sentence)) {
                        completedStrings.add(sentence);
                        // TODO: Check progress
                        // Remove unwanted tags and info like the * tags in bolt data and rejoin sentences.
                        String[] tokens = sentence.split("\\s+");
                        List<String> mtokens = new ArrayList<>();
                        for(int j = 0; j < tokens.length; j++){
                            if(tokens[j].contains("*")){ continue;
                            }else{ mtokens.add(tokens[j]); }
                        }
                        String modifiedSentence = String.join(" ", mtokens);
                        // Pass the entire string as input to the dependency parser to get the predicates in the sentence and
                        // other info like token number, lemma, predicate and verbnet sense.
                        List<List<JsonObject>> parsedSentence = semlinkDepParser.parseString(modifiedSentence);

                        // Create a json object to store filename, sentence_id, sentence(orig), sentence(mod), and the returned
                        // parse information.
                        JsonObject sentenceInfo = new JsonObject();

                        // The parsed returned string is an arrayList. Convert it to a json readable string "parsedGsonString"
                        // and then into a json array.
                        //TODO: See if the arraylist to JsonArray can be done in fewer steps.

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.setPrettyPrinting().create();
                        String parsedGsonString = gson.toJson(parsedSentence);
                        JsonParser jsonparser = new JsonParser();
                        JsonElement jsonElement = jsonparser.parse(parsedGsonString);
                        JsonArray vnInfoJson = jsonElement.getAsJsonArray();

                        // Add all the elements to the "sentenceInfo" JsonObject.
                        sentenceInfo.addProperty("filename", f.getName());
                        sentenceInfo.addProperty("sentence_num", sentence_index);
                        sentenceInfo.addProperty("Original_Sentence", sentence);
                        sentenceInfo.addProperty("sentence", modifiedSentence);
                        sentenceInfo.add("WSD_verb_tags", vnInfoJson);
                        resultsOut.add(sentenceInfo);
                        sentence_index++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Write JSON file
        try (FileWriter writer = new FileWriter("../WSD_verbnet_1014.json")) {
            FilePermissions fp = new FilePermissions();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(resultsOut, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
