import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
// Spelling Bee by Carter Techel
/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [Carter Techel]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateHelper("", letters);
    }

    public void generateHelper(String str, String letters)
    {
        // if the array list of words doesn't have the given str add it
        if(!words.contains(str))
        {
            words.add(str);
        }
        for (int lettersLeft = 0; lettersLeft < letters.length(); lettersLeft++)
        {
            generateHelper(str + letters.charAt(lettersLeft), letters.substring(0, lettersLeft) + letters.substring(lettersLeft+1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        // run merge sort on the words you have in the array list of words
        mergeSort(words, 0, words.size() - 1);
    }

    public ArrayList<String> mergeSort(ArrayList<String> merged, int low, int high)
    {
        // if the high and low are the same
        if (high <= low)
        {
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(merged.get(low));
            return newArr;
        }
        // find midpoint of array
        int mid = (low + (high - low) / 2);
        ArrayList<String> temp1 = mergeSort(merged, low, mid);
        ArrayList<String> temp2 = mergeSort(merged, mid + 1, high);
        return merge(temp1, temp2);
    }

    // merge the 2 arrays together
    public ArrayList<String> merge(ArrayList<String> array1, ArrayList<String> array2)
    {
        ArrayList<String> merged = new ArrayList<String>();
        int idx1 = 0;
        int idx2 = 0;
        // While neither are empty
        while (idx1 < array1.size() && idx2 < array2.size())
        {
            if (array1.get(idx1).compareTo(array2.get(idx2)) < 0)
            {
                merged.add(array1.get(idx1));
                idx1++;
            }
            else
            {
                merged.add(array2.get(idx2));
                idx2++;
            }
        }
        // add the rest to the end of the merged array
        while (idx1 < array1.size())
        {
            merged.add(array1.get(idx1));
            idx1++;
        }
        while (idx2 < array2.size())
        {
            merged.add(array2.get(idx2));
            idx2++;
        }
        return merged;
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++)
        {
            if (!checkWordHelper(words.get(i), 0, DICTIONARY_SIZE - 1))
            {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean checkWordHelper(String word, int start, int end)
    {
        int mid = (start + (end - start) / 2);
        if (word.equals(DICTIONARY[mid]))
        {
            return true;
        }
        if (end == start)
        {
            return false;
        }
        // word comes before
        if (word.compareTo(DICTIONARY[mid]) < 0)
        {
            return checkWordHelper(word, start, mid);
        }
        // word comes after
        else
        {
            return checkWordHelper(word, mid + 1, end);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
