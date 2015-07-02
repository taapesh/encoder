/**
 * Created by taapesh on 6/24/15.
 */
import java.util.*;

abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }

    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends HuffmanTree {
    public final char value; // the character this leaf represents

    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees

    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

public class HuffmanCode {
    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        int length = charFreqs.length + 65;
        int idx = 0;
        for (int i = 65; i < length; i++)
            if (charFreqs[idx] > 0) {
                // System.out.println("Char: " + (char)(i));
                trees.offer(new HuffmanLeaf(charFreqs[idx], (char)(i)));
                idx++;
            }

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }

    public static void getSymbolEncoding(HuffmanTree tree, StringBuffer prefix, Map<Character, String> charMap, Map<String, Character> prefixMap, List<String> allPrefixes, boolean isSingleChar) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;

            if (isSingleChar) {
                assert charMap != null;
                charMap.put(leaf.value, prefix.toString());
                prefixMap.put(prefix.toString(), leaf.value);
            } else {
                assert allPrefixes != null;
                allPrefixes.add(prefix.toString());
            }
        }
        else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;

            if (isSingleChar) {
                // traverse left
                prefix.append('0');
                getSymbolEncoding(node.left, prefix, charMap, prefixMap, null, true);
                prefix.deleteCharAt(prefix.length()-1);

                // traverse right
                prefix.append('1');
                getSymbolEncoding(node.right, prefix, charMap, prefixMap, null, true);
                prefix.deleteCharAt(prefix.length()-1);
            } else {
                // traverse left
                prefix.append('0');
                getSymbolEncoding(node.left, prefix, null, null, allPrefixes, false);
                prefix.deleteCharAt(prefix.length()-1);

                // traverse right
                prefix.append('1');
                getSymbolEncoding(node.right, prefix, null, null, allPrefixes, false);
                prefix.deleteCharAt(prefix.length()-1);
            }

        }
    }

    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;

            // print out character, frequency, and code for this leaf (which is just the prefix)
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length()-1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }

    /*
    public static void main(String[] args) {
        String test = "this is an example for huffman encoding";

        // we will assume that all our characters will have
        // code less than 256, for simplicity
        int[] charFreqs = new int[256];
        // read each character and record the frequencies
        for (char c : test.toCharArray())
            charFreqs[c]++;

        // build tree
        HuffmanTree tree = buildTree(charFreqs);

        // print out results
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(tree, new StringBuffer());
    }
    */
}