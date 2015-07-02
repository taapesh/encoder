/**
 * Created by taapesh on 6/24/15.
 */
import java.util.*;
import java.io.*;
import java.lang.StringBuilder;
import java.lang.Math;

public class Encoder {
    private static final int ALPHA_LENGTH = 27;
    private static final String FNAME_TEST_TEXT = "testText";
    private static final String FNAME_ENC1 = "testText.enc1";
    private static final String FNAME_DEC1 = "testText.dec1";
    private static final String FNAME_ENC2 = "testText.enc2";
    private static final String FNAME_DEC2 = "testText.dec2";

    private static Map<String, String> prefixToSymbol = new HashMap<>();
    private static Map<String, String> symbolToPrefix  = new HashMap<>();
    private static ArrayList<String> prefixes = new ArrayList<>();

    private static int[] charFrequencies = new int[ALPHA_LENGTH];
    private static int[] twoCharSymbolFreqs = new int[ALPHA_LENGTH];
    private static Map<String, Integer> charToFrequencies  = new TreeMap<>();

    private static Map<Character, String> charMap = new HashMap<>();
    private static Map<String, Character> prefixMap = new HashMap<>();


    private static StringBuilder generateText(int numChars, int totalFreq) throws IOException {
        StringBuilder plainText = new StringBuilder();
        char[] dartboard = new char[totalFreq];
        Random rand = new Random();

        char letter = 'A';
        int dartIdx = 0;
        int frequencyIdx = 0;
        int charFreq = charFrequencies[frequencyIdx];

        while (dartIdx < totalFreq) {
            if (charFreq == 0) {
                letter++;
                frequencyIdx++;
                charFreq = charFrequencies[frequencyIdx];
                dartboard[dartIdx] = letter;
            }
            else {
                dartboard[dartIdx] = letter;
            }

            dartIdx++;
            charFreq--;
        }

        for(int i = 0; i < numChars; i++) {
            plainText.append(dartboard[rand.nextInt(totalFreq)]);
        }

        writeAndClose(FNAME_TEST_TEXT, plainText);
        return plainText;
    }

    private static void encodeText(StringBuilder plainText) throws IOException {
        StringBuilder encodedText = new StringBuilder();
        int textLength = plainText.length();

        for(int i = 0; i < textLength; i++) {
            char c = plainText.charAt(i);

            if(charMap.containsKey(c)) {
                encodedText.append(charMap.get(c));
            }
        }

        writeAndClose(FNAME_ENC1, encodedText);
    }

    private static void decodeText() throws IOException {
        StringBuilder decodedText = new StringBuilder();
        StringBuilder decodeThis = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader(FNAME_ENC1));

        String line = r.readLine();
        while (line != null) {
            decodeThis.append(line);
            line = r.readLine();
        }

        String s = "";
        int textLength = decodeThis.length();
        for(int i = 0; i < textLength; i++) {
            s += decodeThis.charAt(i);
            if (prefixMap.containsKey(s)) {
                decodedText.append(prefixMap.get(s));
                s = "";
            }
        }

        writeAndClose(FNAME_DEC1, decodedText);
    }

    private static void encodeTextTwoChar(StringBuilder plainText) throws IOException {
        StringBuilder encodedText = new StringBuilder();

        int length = plainText.length() - 1;
        for(int i = 0; i < length; i += 2) {
            StringBuilder symbol = new StringBuilder();
            symbol.append(plainText.charAt(i));
            symbol.append(plainText.charAt(i+1));

            if(symbolToPrefix.containsKey(symbol.toString())) {
                encodedText.append(symbolToPrefix.get(symbol.toString()));
            }
        }

        writeAndClose(FNAME_ENC2, encodedText);
    }

    private static void decodeTextTwoChar() throws IOException {
        StringBuilder text = new StringBuilder();
        StringBuilder decodeThis = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader(FNAME_ENC2));

        String line = r.readLine();
        while (line != null) {
            decodeThis.append(line);
            line = r.readLine();
        }

        String s = "";
        int textLength = decodeThis.length();
        for(int i = 0; i < textLength; i++) {
            s += decodeThis.charAt(i);
            if (prefixToSymbol.containsKey(s)) {
                text.append(prefixToSymbol.get(s));
                s = "";
            }
        }

        writeAndClose(FNAME_DEC2, text);
    }

    private static void writeAndClose(String fname, StringBuilder text) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(new File(fname)));
        w.append(text);
        w.flush();
        w.close();
    }

    public static void main(String[] args)
    {
        if(args.length < 2) {
            System.out.println("Program usage: java Encoder frequenciesFile k");
        } else {
            String frequenciesFile = args[0];
            int numChars = 0;
            try {
                numChars = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.println("Second argument must be an integer");
            }


            /*
            // TESTING:
            String frequenciesFile = "frequenciesFile";
            int numChars = 10;
            */


            BufferedReader r;
            try {
                r = new BufferedReader(new FileReader(frequenciesFile));

                double entropy = 0.0;
                int totalFreq = 0;

                String line = r.readLine();
                int k = 0;
                while (line != null) {
                    charFrequencies[k] = Integer.parseInt(line);
                    totalFreq += charFrequencies[k];
                    k++;
                    line = r.readLine();
                }

                for(int i = 0; charFrequencies[i] != 0; i++) {
                    double probability = ((double)charFrequencies[i] / totalFreq);
                    // System.out.println("probability: " + probability);
                    entropy += probability * (Math.log(probability) / Math.log(2));
                }
                entropy = -entropy;

                HuffmanTree tree = HuffmanCode.buildTree(charFrequencies);
                HuffmanCode.getSymbolEncoding(tree, new StringBuffer(), charMap, prefixMap, null, true);

                // Generate text, encode and decode and write results to file
                StringBuilder plainText = generateText(numChars, totalFreq);
                encodeText(plainText);
                decodeText();

                // Calculate average bits per symbol
                r = new BufferedReader(new InputStreamReader(new FileInputStream(new File(FNAME_ENC1))));
                int numBits = 0;
                while(r.read() != -1) {
                    numBits++;
                }
                //System.out.println("Total bits: " + numBits);

                double avgBitsPerSymbol = (double) numBits / numChars;

                // Display encoding stats
                System.out.println("ONE CHAR SYMBOL RESULTS");
                System.out.println();
                System.out.println("LETTER\tWEIGHT\tENCODING");
                HuffmanCode.printCodes(tree, new StringBuffer());

                double entropyDiff = (avgBitsPerSymbol/entropy-1)*100;

                System.out.println();
                System.out.println("Entropy: " + entropy);
                System.out.println("Average bits per symbol: " + avgBitsPerSymbol);
                System.out.println("Difference compared to entropy: " + entropyDiff + " %");

                // Perform the process for two char symbols
                for(int i = 0; charFrequencies[i] != 0 && i < ALPHA_LENGTH; i++) {
                    for(int j=0; charFrequencies[j] != 0 && j < ALPHA_LENGTH; j++) {
                        String symbol = String.valueOf((char)(i+65)) + String.valueOf((char)(j+65));
                        charToFrequencies.put(symbol, charFrequencies[i] * charFrequencies[j]);
                    }
                }

                k = 0;
                for(String key : charToFrequencies.keySet()) {
                    twoCharSymbolFreqs[k++] = charToFrequencies.get(key);
                }

                HuffmanTree twoCharTree = HuffmanCode.buildTree(twoCharSymbolFreqs);
                HuffmanCode.getSymbolEncoding(twoCharTree, new StringBuffer(), null, null, prefixes, false);

                int i = 0;
                for(String s: charToFrequencies.keySet()) {
                    String prefix = prefixes.get(i);
                    symbolToPrefix.put(s, prefix);
                    prefixToSymbol.put(prefix, s);
                    i++;
                }

                System.out.println("\nTWO CHAR SYMBOL RESULTS");
                System.out.println();
                System.out.println("LETTER\tWEIGHT\tENCODING");
                for(String symbol : charToFrequencies.keySet()) {
                    System.out.println(symbol + "\t" + charToFrequencies.get(symbol) + "\t" + symbolToPrefix.get(symbol));
                }

                encodeTextTwoChar(plainText);
                decodeTextTwoChar();

                // Calculate average bits per symbol
                r = new BufferedReader(new InputStreamReader(new FileInputStream(new File(FNAME_ENC2))));
                int numBitsTwoChar = 0;
                while(r.read() != -1) {
                    numBitsTwoChar++;
                }
                // System.out.println("Total bits two char: " + numBitsTwoChar);

                double avgBitsPerSymbolTwoChar = (double) numBitsTwoChar / numChars;
                double entropyDiffTwoChar = (avgBitsPerSymbolTwoChar/entropy-1)*100;
                double singleCharComparison = (avgBitsPerSymbolTwoChar/avgBitsPerSymbol-1)*100;

                // Display results of two char encoding
                System.out.println("\nAverage bits per symbol: " + avgBitsPerSymbolTwoChar);
                System.out.println("Difference compared to entropy: " + entropyDiffTwoChar + "%");
                System.out.println("Difference compared to single char encoding: " + singleCharComparison + "%\n");

            } catch (IOException e) {
                System.out.println("Exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
