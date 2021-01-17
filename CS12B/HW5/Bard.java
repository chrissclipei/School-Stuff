
// Emmanuel Huff 
// ehuff
// Chris Sclipei 
// csclipei
// HW 5
// 3/18/2017
// Filename: Bard.java
//
import java.io.*;
import java.util.*;
public class Bard
{

    private static BufferedWriter writer;

    public static void readShakespeareText() {
        int k;
        int l;
        int frequencyCount = 0;
        String[] storeWord;
        int[] storeCount;
        String[] readIn;
        String wordToFind;
        String[] inquire = null;
        boolean foundWord = false;
        Hashtable<Integer, Node> map = new Hashtable<Integer, Node>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("shakespeare.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String word;
                String wordToCheck; 

                StringTokenizer token = new StringTokenizer(line, "[]!?.,:;() "); //covers all cases with non-standard characters
                while (token.hasMoreTokens()) {
                    word = token.nextToken();
                    word = word.trim();
                    word = word.replaceAll("[^a-zA-Z0-9'-]", "");
                    wordToCheck = word;
                    if (!word.equals(wordToCheck.toUpperCase())){
                        word = word.toLowerCase();
                        if (map.containsKey(word.length())) {
                            Node wordList = (Node) map.get(word.length());
                            Node head = wordList;
                            foundWord = false;
                            while (wordList != null && !foundWord){
                                if(wordList.getWord().equalsIgnoreCase(word)){
                                    wordList.incrementCount();
                                    foundWord = true;
                                }
                                wordList = wordList.getNext();
                            }
                            if(!foundWord) {
                                Node newWord = new Node(word);
                                newWord.setNext(head);
                                map.put(word.length(), newWord);
                            }
                        } else {
                            Node newWord = new Node(word);
                            map.put(word.length(), newWord);
                        }
                    }
                }      
            }
            reader.close();
        }
        catch (Exception e) {
            System.out.println("Exception occurred trying to read file"); // throw a generic exception if failure to read occurs
        }

        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("input.txt"));
            String line;
            while((line = reader1.readLine()) != null){
                String[] args = line.split(" ");
                if (args.length == 1) {
                    wordToFind = args[0];
                    if (map.containsKey(wordToFind.length())) {
                        Node wordList = (Node) map.get(wordToFind.length());
                        foundWord = false;
                        frequencyCount = 0;
                        while (wordList != null && !foundWord) {
                            if (wordList.getWord().equals(wordToFind)) {
                                frequencyCount = wordList.getCount();
                                foundWord = true;
                            }
                            wordList = wordList.getNext();
                        }
                        System.out.println(frequencyCount);
                        writer.write(String.valueOf(frequencyCount));
                        writer.write("\n");
                    }
                    else {
                        System.out.println("Word not found");
                        writer.write("Word not found");
                    }
                }
                else {
                    l = Integer.parseInt(args[0]);
                    k = Integer.parseInt(args[1]);
                    storeWord = new String[k];
                    storeCount = new int[k];                    
                    for (int i = 0; i < k; i++) {
                        storeCount[i] = 0;
                    }
                    if (map.containsKey(l)) {
                        Node wordList = (Node) map.get(l);
                        while (wordList != null) {
                            int curr = wordList.getCount();
                            int idx;
                            for (idx = 0; idx < k && curr < storeCount[idx]; idx++){
                            }
                            if (idx < k){
                                for (int i = k - 1; i > idx; i--) {
                                    storeCount[i] = storeCount[i - 1];
                                    storeWord[i] = storeWord[i -1];
                                }
                                storeCount[idx] = curr;
                                storeWord[idx] = wordList.getWord();
                            }
                            wordList = wordList.getNext();
                        }
                        for (int i = 0; i < k; i++) {
                            System.out.print(storeWord[i] + " ");
                            writer.write(storeWord[i] + "  ");
                        }
                        System.out.println();
                        writer.write("\n");
                    }
                    else {
                        System.out.println("Word not found");
                        writer.write("Word not found");
                    }
                }
            }
        }

        catch (Exception e) {
            System.out.println("Exception occurred trying to read file");
        }

    }


    public static void main(String[] args) {
        try{
            writer = new BufferedWriter(new FileWriter("analysis.txt"));
            readShakespeareText();
            writer.close();
        }
        catch(Exception e) {
            System.err.println("Error while creating BufferedWriter");
        }
    }

}
