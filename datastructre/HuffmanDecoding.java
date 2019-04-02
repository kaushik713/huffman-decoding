package datastructre;

/*
 *Huffman coding assigns variable length codewords to fixed length input characters based on their frequencies.
 *More frequent characters are assigned shorter codewords and less frequent characters are assigned longer codewords.
 *All edges along the path to a character contain a code digit.
 *If they are on the left side of the tree, they will be a 0 (zero).
 *If on the right, they'll be a 1 (one).
 *Only the leaves will contain a letter and its frequency count.
 *All other nodes will contain a null instead of a character, and the count of the frequency of all of it and its descendant characters.
 *To avoid ambiguity, Huffman encoding is a prefix free encoding technique.
 *No codeword appears as a prefix of any other codeword.
 *To decode the encoded string, follow the zeros and ones to a leaf and return the character there.
 *You are given pointer to the root of the Huffman tree and a binary coded string to decode.
 *You need to print the decoded string.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;


abstract class Node implements Comparable<Node> {
   public  int frequency; // the frequency of this tree
   public  char data;
   public  Node left, right; 
   public Node(int freq) { 
     frequency = freq; 
   }

   // compares on the frequency
   public int compareTo(Node tree) {
       return frequency - tree.frequency;
   }
}
/*
 * this class is used to encode the string in huffman encoding
 */

class HuffmanLeaf extends Node {
   

   public HuffmanLeaf(int freq, char val) {
       super(freq);
       data = val;
   }
}

class HuffmanNode extends Node {
   
   public HuffmanNode(Node l, Node r) {
       super(l.frequency + r.frequency);
       left = l;
       right = r;
   }

}

/*
 * this class is used to decode the huffman encoded string
 */
class Decoding {

   void decode(String S, Node root) {
       
  String decodedString="" ;
   Node node = root;
   for (int i = 0; i < S.length(); i++) {
       if(S.charAt(i)=='1') /* if the character is 1 then we move pointer to right side*/
         node=node.right;         
       else                 /*if the character is zero then we move string to left side*/
         node=node.left;
       if (node.left == null && node.right == null) { /* this condition indicate that the leaf node is reached */
           decodedString+=(node.data); /* here we append the character at leaf node to the decoded string*/
           node = root;                 /* after that we again move to the root node*/
       }
   }
   System.out.print(decodedString);   
   }



}


public class HuffmanDecoding {
 
   // input is an array of frequencies, indexed by character code
   public static Node buildTree(int[] charFreqs) {
     
       PriorityQueue<Node> trees = new PriorityQueue<Node>();
       // initially, we have a forest of leaves
       // one for each non-empty character
       for (int i = 0; i < charFreqs.length; i++)
           if (charFreqs[i] > 0)
               trees.offer(new HuffmanLeaf(charFreqs[i], (char)i));

       assert trees.size() > 0;
     
       // loop until there is only one tree left
       while (trees.size() > 1) {
           // two trees with least frequency
           Node a = trees.poll();
           Node b = trees.poll();

           // put into new node and re-insert into queue
           trees.offer(new HuffmanNode(a, b));
       }
     
       return trees.poll();
   }
 
   public static Map<Character,String> mapA=new HashMap<Character ,String>();
 
   public static void printCodes(Node tree, StringBuffer prefix) {
     
       assert tree != null;
     
       if (tree instanceof HuffmanLeaf) {
           HuffmanLeaf leaf = (HuffmanLeaf)tree;

           // print out character, frequency, and code for this leaf (which is just the prefix)

           mapA.put(leaf.data,prefix.toString());

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

   public static void main(String[] args) {
       Scanner input = new Scanner(System.in);
   
       String test= input.next();

       // we will assume that all our characters will have
       // code less than 256, for simplicity
       int[] charFreqs = new int[256];
     
       // read each character and record the frequencies
       for (char c : test.toCharArray())
           charFreqs[c]++;

       // build tree
       Node tree = buildTree(charFreqs);

       // print out results
       printCodes(tree, new StringBuffer());
       StringBuffer s = new StringBuffer();
     
       for(int i = 0; i < test.length(); i++) {
           char c = test.charAt(i);
           s.append(mapA.get(c));
       }
     

       Decoding d = new Decoding();
       d.decode(s.toString(), tree);

   }
}