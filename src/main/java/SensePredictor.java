import com.google.gson.JsonObject;
import io.github.clearwsd.parser.NlpParser;

import java.util.List;

public abstract class SensePredictor {

    public SensePredictor() { }

    protected abstract NlpParser parser();

    public abstract List<List<JsonObject>> parseString(String inputSentence);
}
