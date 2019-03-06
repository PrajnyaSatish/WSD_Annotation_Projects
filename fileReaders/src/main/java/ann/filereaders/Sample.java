package ann.filereaders;

public class Sample {
    String segmentId;
    String originalSentence;

    public Sample(String segmentId, String originalSentence) {
        this.segmentId = segmentId;
        this.originalSentence = originalSentence;
    }

    public String getId() {
        return segmentId;
    }

    public String getOrigSent() {
        return originalSentence;
    }
}
