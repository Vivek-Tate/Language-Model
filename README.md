# Language Model Project

## Overview

This project is a GUI-based application for language model analysis, designed with object-oriented programming principles for efficient document processing, vocabulary management, and language model prediction.

## Features

### Main Screen
- **Document Upload**: Load and display a document.
- **Processing Document**: Enable processing with selected hash functions.
- **Vocabulary Display**: Show and sort word counts.
- **Statistics Display**: View detailed statistics.
- **Language Model Analysis**: Access advanced analysis features.

### Language Model Analysis Screen
- **Prediction**: Predict the next 20 words based on input.
- **Statistics**: View model statistics.
- **Navigation**: Return to the Main Screen retaining data.

## Key Concepts

### Data Abstraction and Encapsulation
- **Interfaces**: For HashFunction and LinkedListObject.
- **Encapsulation**: Private variables with public methods.
- **Inheritance**: Hierarchical structure for HashFunction implementations.
- **Polymorphism**: Interchangeable hash functions in MyHashTable.

## Tasks

### MyLinkedObject
- **Constructor**: Initializes with word and count.
- **Methods**: `setWord(String w)`, `getWordCount()`.

### MyHashFunction
- **First Letter Hash**: Uses Unicode value of the first letter.
- **Division Hash**: Sums Unicode values of all characters.

### MyHashTable
- **Encapsulation**: Hides internal classes.

### Vocabulary List
- **Sorting**: By descending frequency.
- **Statistics**: Unique words and word counts.

### N-grams
- **Unigrams, Bigrams, Trigrams**: Probability calculations.
- **Predictions**: Based on different n-gram models.
- **Issues with Larger N-grams**: Increased complexity and memory requirements.

## Usage

1. **Main Screen**:
   - Click "Load Document" to upload a document.
   - Click "Process Document" to analyze the document.
   - Use sorting and statistics options as needed.
   - Click "Run Language Model Analysis" for advanced analysis.

2. **Language Model Analysis Screen**:
   - Enter text for prediction and select a language model.
   - Click "Predict" to see predicted words.
   - Click "Show Statistics" for detailed analysis.
   - Click "Main Page" to return to the main screen.

### License
See the [LICENSE](LICENSE) file for details.

---

Feel free to reach out via the project's GitHub repository for any issues or contributions. Enjoy exploring Language Model Project!
