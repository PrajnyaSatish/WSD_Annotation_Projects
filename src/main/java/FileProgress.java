public class FileProgress {

    public String checkFileProgress(int lineNo, int numSentences) {
        if(lineNo+1!=numSentences) {
            if((lineNo+1)%10==0) {
                String percentDone = new Integer(((lineNo+1)/numSentences)*100).toString();
                return "At "+ percentDone + "%";
            }
        }
        return "Done";
    }
}
