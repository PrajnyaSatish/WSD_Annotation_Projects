package writers;

import com.google.gson.*;

import java.util.List;

public class ltfWriter {

    public static JsonObject jsonConverter(String segmentId, String originalText, List<List<JsonObject>> parsedSentence) {
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
        sentenceInfo.addProperty("segmentId", segmentId);
        sentenceInfo.addProperty("Original_Text", originalText);
        sentenceInfo.add("WSD_verb_tags", vnInfoJson);
        return sentenceInfo;
    }
}
