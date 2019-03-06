import com.google.gson.JsonElement;
import io.github.clearwsd.DefaultSensePredictor;
import io.github.clearwsd.WordSenseAnnotator;
import io.github.clearwsd.WordSenseClassifier;
import io.github.clearwsd.parser.NlpParser;
import io.github.clearwsd.parser.StanfordDependencyParser;
import io.github.clearwsd.type.DepNode;
import io.github.clearwsd.type.DepTree;
import io.github.clearwsd.verbnet.DefaultPredicateAnnotator;
import io.github.clearwsd.type.FeatureType;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.gson.JsonObject;

public class customSensePredictor extends SensePredictor {
    /* This class is similar to interactiveTestLoop.java class in that it uses a dependency tree to get the senses from a semlink model.
    It returns a parsed string. */
    private NlpParser parser;
    private WordSenseClassifier classifier;
    Function<DepTree, String> formatter;

    String modelPath = "../clearwsd/clearwsd-models/src/main/resources/models/nlp4j-verbnet-1.3.bin";

    public customSensePredictor() {
        super();
    }

    @Override
    protected NlpParser parser() {
        return new StanfordDependencyParser();
    }

    private NlpParser getParser() {
        if (parser == null) {
            parser = parser();
        }
        return parser;
    }

    private WordSenseClassifier loadClassifier() {
        //log.info("Loading saved classifier model from {}", modelPath);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath))) {
            return new WordSenseClassifier(ois);
        } catch (FileNotFoundException e) {
            String cwd = new File("").getAbsolutePath();
            System.out.println(cwd);
            throw new RuntimeException("Unable to locate model at path " + modelPath, e);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load word sense classifier model: " + e.getMessage(), e);
        }
    }

    private WordSenseAnnotator getAnnotator() {
        if (classifier == null) {
            classifier = loadClassifier();
        }
        return new WordSenseAnnotator(classifier, new DefaultPredicateAnnotator(classifier.predicateDictionary()));
    }

    public List<List<JsonObject>> parseString(String inputSentence) {
        NlpParser parser = new DefaultSensePredictor(getAnnotator(), getParser());
        List<List<JsonObject>> listOfVerbs = new ArrayList<List<JsonObject>>();
        for (String sentence : parser.segment(inputSentence)) {
            DepTree tree = parser.parse(parser.tokenize(sentence));
            listOfVerbs.add(applyDict(tree));
        }
        return listOfVerbs;
    }

    public List<JsonObject> applyDict(DepTree depParsedTree) {
        List<JsonObject> verbs_list = new ArrayList<JsonObject>();
        int index = 0;
        for (DepNode node : depParsedTree) {
            if (node.feature(FeatureType.Predicate) != null) {
                JsonObject verbLemmas = new JsonObject();
                verbLemmas.addProperty("TokenNum", index);
                verbLemmas.addProperty("Text", (String) node.feature(FeatureType.Text));
                verbLemmas.addProperty("Predicate", (String) node.feature(FeatureType.Predicate));
                verbLemmas.addProperty("Sense", (String) node.feature(FeatureType.Sense));
                if (node.head()!= null){
                    int headToken = node.head().index();
                    verbLemmas.addProperty("HeadToken", headToken);
                    verbLemmas.addProperty("HeadVal", depParsedTree.get(headToken).feature(FeatureType.Text).toString());
                }
                verbs_list.add(verbLemmas);
            }
            index++;
        }
        return verbs_list;
    }
}
