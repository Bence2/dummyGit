public class Outcast {
    private  WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        int currentBiggestDistance = Integer.MIN_VALUE;
        String outcastNoun = "";
        for (String noun : nouns) {
            int nounsCompleteDistance = 0;
            for (String nounToCompareWith : nouns) {
                int partialDistance = wordNet.distance(noun, nounToCompareWith);
                nounsCompleteDistance += partialDistance;

            }
            if (nounsCompleteDistance > currentBiggestDistance) {
                outcastNoun = noun;
                currentBiggestDistance = nounsCompleteDistance;
            }
        }
        return outcastNoun;
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/synsets.txt", "/hypernyms.txt");
        Outcast outcast = new Outcast(wordNet);

        String outcastEntity = outcast.outcast(new String[] {"horse", "zebra", "cat", "bear", "table"});
        assert(outcastEntity.equals("table"));

        outcastEntity = outcast.outcast(new String[] {"water", "soda", "bed", "orange_juice", "milk", "apple_juice", "tea", "coffee"});
        assert(outcastEntity.equals("bed"));

        System.out.println("miau");
    }
}
