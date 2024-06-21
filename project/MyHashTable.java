import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MyHashTable created by Vivek Shravan Tate
 */
public class MyHashTable {

    // STATIC VARIABLES
    // Setting the default has table size to 10
    private static final int DEFAULT_HASH_TABLE_SIZE = 9999999;

    // INSTANCE VARIABLES
    private MyLinkedObject[] hashTable;
    private MyHashFunction hashFunction;
    private int tableSize;

    // CONSTRUCTORS

    // Default Constructor
    public MyHashTable() {

        this("FirstLetterHashFucntion");
    }

    // Custom Constructor
    public MyHashTable(String hashFunction) {

        this.tableSize = DEFAULT_HASH_TABLE_SIZE;
        this.hashTable = new MyLinkedObject[DEFAULT_HASH_TABLE_SIZE];
        switch (hashFunction) {
            case "FirstLetterHashFucntion":
                this.hashFunction = new FirstLetterHashFucntion();
                break;
        
            case "Division Hash Fucntion":
                this.hashFunction = new DivisionHashFunction();
                break;

            default:
                this.hashFunction = new FirstLetterHashFucntion();
                break;
        }

        this.hashFunction = new FirstLetterHashFucntion();
    }


    // PUBLIC HELPER METHODS
    /**
     * Add word to the hash table
     * 
     * @param word String to be added
    */
    public void addWord(String word) {

        // Holds the value foe the index of the word
        int wordIndex = hashFunction.hash(word, tableSize);

        // Checking whether Linked List exists in hash table for specific index
        if (hashTable[wordIndex] == null) {

            // If not, Create linked list at the given index
            hashTable[wordIndex] = new MyLinkedObject(word);
        } else {

        // If yes, Add word to the linked list chain. 
        hashTable[wordIndex].setWord(word);

        }
    }

    /**
     * Get the count of the word
     * 
     * @param word String
     * @return Count of the word
    */
    public int getWordCount(String word) {
        // Holds the value foe the index of the word
        int wordIndex = hashFunction.hash(word, tableSize);
        
        return (hashTable[wordIndex] == null) ? 0 : hashTable[wordIndex].getWordCount();
    }

    // Fetch the total word count
    public int getTotalWordCount() {
        int count = 0;
        for (MyLinkedObject list : hashTable) {
            while (list != null) {
                count += list.count; // Count elements within linked lists
                list = list.next;
            }
        }
        return count;
    }

    public int getTotalDifferentWords() {
        int uniqueWordCount = 0;
    
        for (MyLinkedObject list : hashTable) {
            if (list != null) {
                uniqueWordCount++;  // Count each unique word at the head of the linked lists
    
                MyLinkedObject current = list.next;
                while (current != null) {
                    if (!current.word.equals(list.word)) {  // Check for duplicates within lists
                        uniqueWordCount++;
                    }
                    current = current.next;
                }
            }
        }
    
        return uniqueWordCount;
    }    
     
    // Converts the hash table data inot array list
    public ArrayList<Pair<String, Integer>> getHashTableDictionary(){
        ArrayList<Pair<String, Integer>> wordCountPairs = new ArrayList<>();
        for (MyLinkedObject list : hashTable) {
            while (list != null) {
                wordCountPairs.add(new Pair<>(list.word, list.count));
                list = list.next;
            }
        }

        return wordCountPairs;
    }

        public List<Map.Entry<String, Integer>> getHashTableDictionaryInMap() {
        List<Map.Entry<String, Integer>> wordCountEntries = new ArrayList<>();
        for (MyLinkedObject list : hashTable) {
            while (list != null) {
                wordCountEntries.add(new AbstractMap.SimpleEntry<>(list.word, list.count));
                list = list.next;
            }
        }

        return wordCountEntries;
    }

    // Get all linked list lengths
    public int[] getLinkedListLengths() {
        int[] linkedListLengths = new int[hashTable.length];
    
        for (int i = 0; i < hashTable.length; i++) {
            MyLinkedObject current = hashTable[i];
            int length = 0;
    
            while (current != null) {
                length++;
                current = current.next;
            }
    
            linkedListLengths[i] = length;
        }
    
        return linkedListLengths;
    }
    
    // Fetches the total number of linked list
    public int getTotalNumberOfLinkedList() {
        List<Integer> lengths = new ArrayList<>();

        for (MyLinkedObject list: hashTable){

            if(list != null && list.next != null ) {
                MyLinkedObject current = list.next;

                int length = 0;

                while (current != null) {
                    length++;
                    current = current.next;
                }

                if (length > 0) {
                    lengths.add(length);
                }
            }
        }

        int arraySize = lengths.size();

        if (arraySize == 0) {
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < arraySize; i++) {
            sum += lengths.get(i);
        }

        return sum;
    }

    // Fetches the minimum value of linked list chain
    public int getMinValueInLinkedList() {
        List<Integer> lengths = new ArrayList<>();

        for (MyLinkedObject list: hashTable){

            if(list != null && list.next != null ) {
                MyLinkedObject current = list.next;

                int length = 0;

                while (current != null) {
                    length++;
                    current = current.next;
                }

                if (length > 0) {
                    lengths.add(length);
                }
            }
        }

        int arraySize = lengths.size();

        if (arraySize == 0) {
            return 0;
        }

        int [] lengthsArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            lengthsArray[i] = lengths.get(i);
        }

        Arrays.sort(lengthsArray);

        return lengthsArray[0];
    }

    public int getMaxValueInLinkedList() {

        int[] array = getLinkedListLengths();

        // Check if the array is not empty
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        // Initialize max with the first element of the array
        int max = array[0];

        // Iterate through the array to find the maximum value
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    public int getAvreageLinkedListLength() {
        List<Integer> lengths = new ArrayList<>();

        for (MyLinkedObject list: hashTable){

            if(list != null && list.next != null ) {
                MyLinkedObject current = list.next;

                int length = 0;

                while (current != null) {
                    length++;
                    current = current.next;
                }

                if (length > 0) {
                    lengths.add(length);
                }
            }
        }

        int arraySize = lengths.size();

        if (arraySize == 0) {
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < arraySize; i++) {
            sum += lengths.get(i);
        }

        return sum/arraySize;
    }

    public double getStandardDeviation(){
        List<Integer> lengths = new ArrayList<>();

        for (MyLinkedObject list: hashTable){

            if(list != null && list.next != null ) {
                MyLinkedObject current = list.next;

                int length = 0;

                while (current != null) {
                    length++;
                    current = current.next;
                }

                if (length > 0) {
                    lengths.add(length);
                }
            }
        }
        int arraySize = lengths.size();

        if (arraySize == 0) {
            return 0;
        }        

        int [] lengthsArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            lengthsArray[i] = lengths.get(i);
        }

        // Calculate the mean
        double mean = 0.0;
        for (int length: lengthsArray){
            mean += length;
        }

        mean /= arraySize;

        // Calculate the sum of squred difference from the mean
        double sumSquaredDifferences = 0.0;

        for (int length : lengthsArray) {
            sumSquaredDifferences += Math.pow(length - mean,2);
        }

        // Calculate the variance
        double variance = sumSquaredDifferences / arraySize;

        // Calculate the standard deviation
        return Math. sqrt(variance);
    }

    // DEBUGGING METHOD: Display the hash table
    public void showHashTable() {
        System.out.println("LENGTH: " + hashTable.length);
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                System.out.println("Index " + i + ": Word: " + hashTable[i].word + " Count: " + hashTable[i].count);
            }
        }
    }

    // PRIVATE METHODS
  

    // NESTED CLASSES

    // Linked List Object class
    private static class MyLinkedObject {
    
        // INSTANCE VARIABLES
        private String word;
        private int count;
        private MyLinkedObject next;
    
        // CONSTRUCTOR

        /**
         * Initializes a new `MyLinkedObject` with the given words
         * 
         * @param w The words to be added
        */
        private MyLinkedObject(String w) {
            this.word = w;
            this.count = 1;
            this.next = null;
        }
    

        // PUBLIC HELLPER METHODS

        /**
         * Updates the linked list structure based on the input words
         * 
         * @param w The words to be added or updated
        */
        public void setWord(String w) {
			if (w.equals(word)) {
				this.count++;
			} else {
				if (w.compareTo(word) > 0 && next == null) {
					MyLinkedObject node = new MyLinkedObject(w);
					this.next = node;
				}
				else if (next != null && w.compareTo(next.word) < 0) {
					MyLinkedObject node = new MyLinkedObject(w);
					node.next = this.next;
					this.next = node;
				} else {

                    if (next == null) {
					    MyLinkedObject node = new MyLinkedObject(w);
					    this.next = node;
				    } else {
                        next.setWord(w);
                    }
				}
            }
		}
        /**
         * Fetches the count for the given word
         * 
         * @param word String
         * @return Count of the word
        */
        private int getWordCount() {

            return count;
        }
    }
    
    // Nested Abstract class for hash function
    private static abstract class MyHashFunction {

        // ABSTRACT METHODS
        public abstract int hash(String word, int hashTableSize);
    }
    
    // Hash function based on the First Letter
    private static class FirstLetterHashFucntion extends MyHashFunction {

        @Override
        public int hash(String word, int hashTableSize) {
    
            // Returns the Unicode value of the first letter modulo the hash table size
            // Returns 0, if word is empty
            return word.codePointAt(0) % hashTableSize;
        }
    }
    
    // Hash function based on Division Method
    private static class DivisionHashFunction extends MyHashFunction {

        @Override
        public int hash(String word, int hashTableSize) {
    
            // Calculate the sum of Unicode values for all characters in the word
            // Returns the sum modulo the hash table size
            return word.isEmpty() ? 0 : (word.chars().sum()) % hashTableSize;     
        }
    }
}
