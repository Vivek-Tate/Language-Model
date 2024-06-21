/**
 * VocabularyList created by Vivek Shravan Tate
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;


public class VocabularyList extends JFrame {

    // USER INTERFACE INSTANCE VARIABLES

    private JTextArea textArea;
    private DefaultTableModel vocabularyTableModel;
    private DefaultTableModel predictionTableModel;
    private JSplitPane mainSplitPane;
    private JSplitPane secondarySplitPane;
    private JPanel mainStatsPanel;
    private JPanel secondaryStatsPanel;
    

    // INSTANCE VARIABLES

    private MyHashTable hashTable;
    private MyHashTable bigramHashTable;
    private MyHashTable trigramHashTable;
    private String documentContent;
    private String hashFucntionString = "FirstLetterHashFucntion";
    private Boolean isMainPageActive = true;
    private String lMString = "BigramModel";

    // CONSTRUCTOR

    // Default Constructor
    public VocabularyList() {
        InitializeUI();
    }

    // PRIVATE METHODS

    // Initialize the User Interface
    private void InitializeUI() {

        // Setup the title bar
        setTitle("Language Model");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setupComponents();
    }
    

    // Setup the UI Components and thier layouts
    private void setupComponents() {

        // Set the layout constraints
        setLayout(new BorderLayout());

        // Render the button panel
        renderButtonPanel();

        // Renders the split pane
        renderSplitPane();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Setup and renders the button panel
    private void renderButtonPanel() {

        int buttonHeight = 40;

        // Create a String array for sorting options
        String[] sortingOptions = {"Sort Lexicographically" ,"Sort By Count (Ascending)", "Sort By Count (Descending)"};

        // Create a String array for hash function options
        String[] hashFunctionOptions = {"First Letter Hash Function" ,"Division Hash Fucntion"};

        // BUTTONS
        // Button for loading of new document
        JButton loadDocumentButton = new JButton("Load Document");

        // Button to process the uploaded document
        JButton processDocumentButton = new JButton("Process Document");

        // Sort vocabulary button
        // Create a JComboBox and set its options
        JComboBox<String> sortOptionsComboBox = new JComboBox<>(sortingOptions);

        // Create a JComboBox  and set its options
        JComboBox<String> hashFunctionOptionsComboBox = new JComboBox<>(hashFunctionOptions);

        // Display statistics button
        JButton showStatisticsButton = new JButton("Show Statistics");

        // Display statistics button
        JButton runLMAnalysis = new JButton("Run N-Gram Analysis");

        // Set the preferred height for the buttons
        loadDocumentButton.setPreferredSize(new Dimension(loadDocumentButton.getPreferredSize().width, buttonHeight));
        processDocumentButton.setPreferredSize(new Dimension(processDocumentButton.getPreferredSize().width, buttonHeight));
        hashFunctionOptionsComboBox.setPreferredSize(new Dimension(hashFunctionOptionsComboBox.getPreferredSize().width, 80));
        sortOptionsComboBox.setPreferredSize(new Dimension(sortOptionsComboBox.getPreferredSize().width, 80));
        showStatisticsButton.setPreferredSize(new Dimension(showStatisticsButton.getPreferredSize().width, buttonHeight));
        runLMAnalysis.setPreferredSize(new Dimension(runLMAnalysis.getPreferredSize().width, buttonHeight));


        // BUTTON PANEL
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(loadDocumentButton);
        buttonsPanel.add(processDocumentButton);
        buttonsPanel.add(hashFunctionOptionsComboBox);
        buttonsPanel.add(sortOptionsComboBox);
        buttonsPanel.add(showStatisticsButton);
        buttonsPanel.add(runLMAnalysis);
        add(buttonsPanel, BorderLayout.NORTH);

        // DIABLE BUTTONS
        processDocumentButton.setEnabled(false);
        hashFunctionOptionsComboBox.setEnabled(false);
        sortOptionsComboBox.setEnabled(false);
        showStatisticsButton.setEnabled(false);
        runLMAnalysis.setEnabled(false);

        // EVENT LISTENERS
        // Load the document from the file system
        loadDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // If the document is loaded successfully, the enable the buttons
                if(loadDocument()) {

                processDocumentButton.setEnabled(true);
                hashFunctionOptionsComboBox.setEnabled(true);
                }
            }
        });

        // Post Load processing of documents
        processDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Enable Buttons
                sortOptionsComboBox.setEnabled(true);
                showStatisticsButton.setEnabled(true);
                runLMAnalysis.setEnabled(true);

                processDocument();
            }
        });

        // Sort the vocabulary list alphabetically ascending and descending order
        sortOptionsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedOption = (String) sortOptionsComboBox.getSelectedItem();
                switch (selectedOption) {
                    case "Sort Lexicographically":

                        showVocabularyListTable(hashTable.getHashTableDictionary());
                        break;
                
                    case "Sort By Count (Ascending)":

                        showVocabularyListTable(sortVocabularyListASCE());
                        break;

                    case "Sort By Count (Descending)":
                    
                        showVocabularyListTable(sortVocabularyListDESC());
                        break;

                    default:
                        break;
                }
            }
        });

        // Choose hash function
        hashFunctionOptionsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedOption = (String) hashFunctionOptionsComboBox.getSelectedItem();
                switch (selectedOption) {
                    case "First Letter Hash Function":

                        hashFucntionString = "FirstLetterHashFucntion";
                        processDocument();
                        break;
                
                    case "Division Hash Fucntion":

                        hashFucntionString = "Division Hash Fucntion";
                        processDocument();
                        break;

                    default:
                        break;
                }
            }
        });

        // Show Statistics for the vocabulary list
        showStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isMainPageActive) {
                    showStatistics(hashTable);
                } else {

                    if(lMString == "BigramModel") {

                        showStatistics(bigramHashTable);
                    } else {

                        showStatistics(trigramHashTable);
                    }
                }
            }
        });

        runLMAnalysis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Check if user is on main page
                if (isMainPageActive) {

                    // Change the stored properties
                    isMainPageActive = false;
                    
                    runLMAnalysis.setText("Main Page");

                    // Disable the buttons
                    loadDocumentButton.setEnabled(false);
                    processDocumentButton.setEnabled(false);
                    hashFunctionOptionsComboBox.setEnabled(false);
                    sortOptionsComboBox.setEnabled(false);

                    remove(mainSplitPane);
                    renderSplitPaneForNGramModel();

                    runLanguageModelAnalysis();

                } else { // When user shifts to secondary page

                    // Change the stored properties
                    isMainPageActive = true;
                    lMString = "BigramModel";
                    runLMAnalysis.setText("Run N-Gram Analysis");

                    // Enable the buttons
                    loadDocumentButton.setEnabled(true);
                    processDocumentButton.setEnabled(true);
                    hashFunctionOptionsComboBox.setEnabled(true);
                    sortOptionsComboBox.setEnabled(true);

                    remove(secondarySplitPane);
                    add(mainSplitPane);

                    invalidate();
                    repaint();

                }

            }
        });
    }

    // Setup and renders the split pane
    private void renderSplitPane(){
        
        // Divide the screen into three parts
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, renderTextArea(), renderVocabularyTable());
        splitPane.setResizeWeight(0.5); // Initial division ratio

        // Add the additional table to the right of the vocabulary table
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, renderMainStatisticView());
        mainSplitPane.setResizeWeight(0.5); // Initial division ratio

        // Add the main split pane to the CENTER
        add(mainSplitPane, BorderLayout.CENTER);
    }

    private void renderSplitPaneForNGramModel(){

        // Divide the screen into three parts
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, renderLanguageModelPane(), renderPredictionAnalysisTable());
        splitPane.setResizeWeight(0.5); // Initial division ratio

        // Add the additional table to the right of the vocabulary table
        secondarySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, renderLMStatisticView());
        secondarySplitPane.setResizeWeight(0.5); // Initial division ratio

        // Add the main split pane to the CENTER
        add(secondarySplitPane, BorderLayout.CENTER); 
    }

    // Setup and renders the text area
    private JScrollPane renderTextArea() {

        // Initialise the text area
        textArea = new JTextArea();

        if(documentContent != null) {
            textArea.setText(documentContent);
        }

        // Initialise the scroll pane and assign the text area
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);

        // Customise the scroll pane
        textAreaScrollPane.setColumnHeaderView(createCenteredHeader("Document Content"));
        textAreaScrollPane.setPreferredSize(new Dimension(400, 700));

        return textAreaScrollPane;
    }

    // Setup and renders the vocabulary list table
    private JScrollPane renderVocabularyTable() {
        // VOCABULARY TABLE
        JTable vocabularyListTable = new JTable();
        vocabularyTableModel = new DefaultTableModel(new Object[]{"Words", "Count"}, 45);
        vocabularyListTable.setRowHeight(20);
        vocabularyListTable.setModel(vocabularyTableModel);
    
        // Set font size
        Font tableFont = new Font(vocabularyListTable.getFont().getName(), Font.PLAIN, 16); // Adjust font size as needed
        vocabularyListTable.setFont(tableFont);
        vocabularyListTable.getTableHeader().setFont(tableFont);
    
        // Use a renderer for cell font and border
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(tableFont);
    
                // Add border to rows
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(Color.WHITE);
                }
    
                return c;
            }
        };
    
        // Access columns using TableColumnModel
        TableColumnModel columnModel = vocabularyListTable.getColumnModel();
        columnModel.getColumn(0).setHeaderValue("Words");
        columnModel.getColumn(1).setHeaderValue("Count");
        columnModel.getColumn(0).setCellRenderer(renderer);
        columnModel.getColumn(1).setCellRenderer(renderer);
    
        // Increase column widths
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(80);
    
        JScrollPane vocabularyScrollPane = new JScrollPane(vocabularyListTable);
        vocabularyScrollPane.setColumnHeaderView(createCenteredHeader("Vocabulary List"));
        vocabularyScrollPane.setPreferredSize(new Dimension(400, 700));
    
        return vocabularyScrollPane;
    }
    
    private JScrollPane renderLanguageModelPane() {

        // UI COMPNENTS
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Bigram Analysis", "Trigram Analysis"});
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 15, 10),
                comboBox.getBorder()));

        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(300, 100));

        JButton predictButton = new JButton("Predict Words");
        predictButton.setPreferredSize(new Dimension(300, 40));
        predictButton.setAlignmentX(Component.CENTER_ALIGNMENT);       // Add action listener to the button



        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Fetching the string from text area
                String str = textArea.getText();

                if(lMString == "BigramModel") {
                    predictBigrams(str);
                
                } else {
                    predictTrigrams(str);
                }
            }
        });

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String selectedOption = (String) comboBox.getSelectedItem(); 

                if (selectedOption == "Bigram Model") {

                    lMString = "BigramModel";
                    processBigramData();

                } else {

                    lMString = "TrigramModel";
                    processTrigramData();
                }
            }
        });
    
        // Create a JPanel with a vertical BoxLayout to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(comboBox);
        panel.add(textArea);
        panel.add(predictButton);
    
        // Create a JScrollPane and set the panel as its view
        JScrollPane scrollPane = new JScrollPane(panel);

        // Customise the scroll pane
        scrollPane.setColumnHeaderView(createCenteredHeader("N-Gram Language Model Analysis"));
        scrollPane.setPreferredSize(new Dimension(350, 700));
    
        return scrollPane;
    }
    
    private JScrollPane renderMainStatisticView() {

        mainStatsPanel = new JPanel(new BorderLayout());

        // Initialise the scroll pane and assign the view
        JScrollPane statisticScrollPane = new JScrollPane(mainStatsPanel);

        // Customise the scroll pane
        statisticScrollPane.setColumnHeaderView(createCenteredHeader("Statistics View"));
        statisticScrollPane.setPreferredSize(new Dimension(400, 700));

        return statisticScrollPane;
    }

    private JScrollPane renderLMStatisticView() {

        secondaryStatsPanel = new JPanel(new BorderLayout());

        // Initialise the scroll pane and assign the view
        JScrollPane statisticScrollPane = new JScrollPane(secondaryStatsPanel);

        // Customise the scroll pane
        statisticScrollPane.setColumnHeaderView(createCenteredHeader("Statistics View"));
        statisticScrollPane.setPreferredSize(new Dimension(400, 700));

        return statisticScrollPane;
    }
    
    // Setup the predction analysis table
    private JScrollPane renderPredictionAnalysisTable(){

        // ANALYSIS TABLE
        JTable predictionListTable = new JTable();
        predictionTableModel = new DefaultTableModel(new Object[]{"Words", "Count"}, 45);
        predictionListTable.setRowHeight(20);
        predictionListTable.setModel(predictionTableModel);
    
        // Set font size
        Font tableFont = new Font(predictionListTable.getFont().getName(), Font.PLAIN, 16); // Adjust font size as needed
        predictionListTable.setFont(tableFont);
        predictionListTable.getTableHeader().setFont(tableFont);
    
        // Use a renderer for cell font and border
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(tableFont);
    
                // Add border to rows
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(Color.WHITE);
                }
    
                return c;
            }
        };
    
        // Access columns using TableColumnModel
        TableColumnModel columnModel = predictionListTable.getColumnModel();
        columnModel.getColumn(0).setHeaderValue("Words");
        columnModel.getColumn(1).setHeaderValue("Count");
        columnModel.getColumn(0).setCellRenderer(renderer);
        columnModel.getColumn(1).setCellRenderer(renderer);
    
        // Increase column widths
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(80);
    
        JScrollPane predictionListScrollPane = new JScrollPane(predictionListTable);
        predictionListScrollPane.setColumnHeaderView(createCenteredHeader("Prediction List"));
        predictionListScrollPane.setPreferredSize(new Dimension(400, 700));
    
        return predictionListScrollPane;
    }

    // Load the document from the file system
    private Boolean loadDocument() {
        // Create the instance of File Chooser
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        // Checks if file is loaded, The read the file and display it in text area
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                return true;

            } catch (IOException exception) {

                exception.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // Process the document and load the hash table
    private void processDocument() {

        // Creating the instance of hash table
        hashTable = new MyHashTable(hashFucntionString);

        // Clearing the data in vocabularyTableModel
        vocabularyTableModel.setRowCount(0);

        // Fetching the document content from text area
        documentContent = textArea.getText();

        // Regular expression to match any character except lower case letters, full stop, apostrophe
        String regex = "[^a-z.'\\s]";

        // Holds the mis-processed items
        StringBuilder restrictedWords = new StringBuilder();

        // Process the document contents
        for (String word: documentContent.split("\\s+")) {

            // Check if entire word contains unwanted char
            if (word.matches(".*" + regex + ".*")) {

                restrictedWords.append(word).append(", ");
            } else {

                // Add word to hashTable
                hashTable.addWord(word);
            }
        }

        // Check if misProcessedItems exist and show a warning
        if (restrictedWords.length() > 0) {
            
            restrictedWords.setLength(restrictedWords.length() - 2); // Remove trailing comma and space
            showWarningMessage("Misprocessed Items: " + restrictedWords.toString() + "\nThese items contain characters not allowed in the vocabulary and will be excluded.");
        }

        showVocabularyListTable(hashTable.getHashTableDictionary());

        // FIXME
        System.out.println("Total word count" + hashTable.getTotalWordCount());
    }

    private void showVocabularyListTable(ArrayList<Pair<String, Integer>> vocabularyList) {

        // Clear existing data in the table
        vocabularyTableModel.setRowCount(0);

        for (int i = 0; i < vocabularyList.size(); i++) {
            if (vocabularyList.get(i) != null) {
                vocabularyTableModel.addRow(new Object[]{vocabularyList.get(i).getKey(), vocabularyList.get(i).getValue()});
            }
        }
    }

    private void showLMListTable(ArrayList<Pair<String, Integer>> lmList) {

        // Clear existing data in the table
        predictionTableModel.setRowCount(0);

        for (int i = 0; i < lmList.size(); i++) {
            if (lmList.get(i) != null) {
                predictionTableModel.addRow(new Object[]{lmList.get(i).getKey(), lmList.get(i).getValue()});
            }
        }
    }

    public ArrayList<Pair<String, Integer>> sortVocabularyListASCE() {

        ArrayList<Pair<String, Integer>> arrayList = hashTable.getHashTableDictionary();

        int n = arrayList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare counts and swap if necessary
                if (arrayList.get(j).getValue() > arrayList.get(j + 1).getValue()) {
                    Pair<String, Integer> temp = arrayList.get(j);
                    arrayList.set(j, arrayList.get(j + 1));
                    arrayList.set(j + 1, temp);
                }
            }
        }

        return arrayList;
    }

    private ArrayList<Pair<String, Integer>> sortVocabularyListDESC() {

        ArrayList<Pair<String, Integer>> arrayList = hashTable.getHashTableDictionary();
    
        int n = arrayList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare counts and swap if necessary
                if (arrayList.get(j).getValue() < arrayList.get(j + 1).getValue()) {
                    Pair<String, Integer> temp = arrayList.get(j);
                    arrayList.set(j, arrayList.get(j + 1));
                    arrayList.set(j + 1, temp);
                }
            }
        }
    
        return arrayList;
    }
    

    // Display the vocabulary list statistics
    private void showStatistics(MyHashTable hashTable) {

        // Initialise the view
        JTable statsTable = new JTable(new Object[][]{{"  Total Word Count: ", hashTable.getTotalWordCount()}, {"  Unique Word Count: ", hashTable.getTotalDifferentWords()}}, new Object[]{"Column 1", "Column 2"});
        statsTable.setRowHeight(30);
        Font cellFont = new Font(statsTable.getFont().getName(), Font.PLAIN, 16);
        statsTable.setFont(cellFont);

        BarChart barChart = new BarChart();
        int[] initialData = {hashTable.getMinValueInLinkedList(), hashTable.getMaxValueInLinkedList(), (int) hashTable.getStandardDeviation(), hashTable.getTotalNumberOfLinkedList(), hashTable.getAvreageLinkedListLength()};
        barChart.renderChart(initialData);

        if(isMainPageActive) {

            mainStatsPanel.add(barChart, BorderLayout.CENTER);
            mainStatsPanel.add(statsTable, BorderLayout.NORTH);
            pack();
            validate();
            repaint();
        } else {

            secondaryStatsPanel.add(barChart, BorderLayout.CENTER);
            secondaryStatsPanel.add(statsTable, BorderLayout.NORTH);

            pack();
            validate();
            repaint();
        }
    }

    /**
     * Show warning message if any miss-processed occured.
     * 
     * @param message String message to be displayed in warning
    */
    private void showWarningMessage(String message) {

        JOptionPane.showMessageDialog(this, message, "Misprocessed Words Warning", JOptionPane.WARNING_MESSAGE);
    }

    private JPanel createCenteredHeader(String labelText) {

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.CENTER);
    
        // Set font size and preferred height
        Font headerFont = label.getFont().deriveFont(Font.BOLD, 14); // Adjust font size as needed
        label.setFont(headerFont);
        label.setPreferredSize(new Dimension(label.getWidth(), 30)); // Adjust height as needed
    
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(label, BorderLayout.CENTER);
    
        return headerPanel;
    }


    // N-GRAM HELPER METHODS
    private void runLanguageModelAnalysis() {

        if(lMString == "BigramModel") {

            processBigramData();
        } else {

            processTrigramData();
        }
    }

    private void processBigramData() {

        this.bigramHashTable = new MyHashTable();

        // Regular expression to match any character except lower case letters, full stop, apostrophe
        String regex = "[^a-z.'\\s]";

        String words[] = documentContent.split("\\s+");

        for (int i = 0; i < words.length - 1; i++) {

            String wordString = words[i] + " " + words[i+1];

            // Check if entire word contains unwanted char
            if (!wordString.matches(".*" + regex + ".*")) {

                // Add word to hashTable
                bigramHashTable.addWord(wordString);
            }
  
        }

        showLMListTable(bigramHashTable.getHashTableDictionary());

        // DEBUGGING PURPOSE
        System.out.println("Total word count for bigram" + bigramHashTable.getTotalWordCount());
    }

    private void processTrigramData() {

        this.trigramHashTable = new MyHashTable();
    
        // Regular expression to match any character except lower case letters, full stop, apostrophe
        String regex = "[^a-z.'\\s]";
    
        // Split the documentContent into an array of words
        String[] words = documentContent.split("\\s+");
    
        // Process the document contents
        for (int i = 0; i < words.length - 2; i++) {
            // Check if any of the three consecutive words contain unwanted characters
            if (!words[i].matches(".*" + regex + ".*") &&
                !words[i + 1].matches(".*" + regex + ".*") &&
                !words[i + 2].matches(".*" + regex + ".*")) {
                // Concatenate three consecutive words to form a trigram
                String trigram = words[i] + " " + words[i + 1] + " " + words[i + 2];
                
                // Add trigram to trigramHashTable
                trigramHashTable.addWord(trigram);
            }
        }

        showLMListTable(trigramHashTable.getHashTableDictionary());

        // DEBUGGING PURPOSE
        System.out.println("Total word count for trigram" + trigramHashTable.getTotalWordCount());
    }

    private void showToast(String message) {
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.getContentPane().setBackground(new Color(255, 223, 186)); // Set background color
        dialog.setSize(200, 80);
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        dialog.add(label, BorderLayout.CENTER);

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        timer.setRepeats(false); // Only run once
        timer.start();

        dialog.setVisible(true);
    }
    // NESTED CLASSES
    public class BarChart extends JPanel {

        private int[] data;
    
        public BarChart() {
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    
        public void renderChart(int[] data) {
            this.data = data;
            repaint();
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            Graphics2D graphics2d = (Graphics2D) g;
            int barWidth = 30;
            int barSpacing = 50;
            int maxHeight = getHeight() - 50;
    
            if (data != null) {
                int totalBarsWidth = data.length * (barWidth + barSpacing) - barSpacing;
                int startX = (getWidth() - totalBarsWidth) / 2;
    
                graphics2d.drawString("Min Len", startX, getHeight() - 5);
                graphics2d.drawString("Max Len", startX + barWidth + barSpacing, getHeight() - 5);
                graphics2d.drawString("Std Devi", startX + 2 * (barWidth + barSpacing), getHeight() - 5);
                graphics2d.drawString("Total Len", startX + 3 * (barWidth + barSpacing), getHeight() - 5);
                graphics2d.drawString("Avg Len", startX + 4 * (barWidth + barSpacing), getHeight() - 5);
    
                for (int i = 0; i < data.length; i++) {
                    int barHeight = (int) (((double) data[i] / data[3]) * maxHeight);
    
                    graphics2d.setColor(Color.gray);
                    graphics2d.fillRect(startX, getHeight() - barHeight - 20, barWidth, barHeight);
    
                    graphics2d.setColor(Color.black);
                    graphics2d.drawRect(startX, getHeight() - barHeight - 20, barWidth, barHeight);
    
                    // Adjust the position for the text above the bar
                    int textYPosition = getHeight() - barHeight - 20 - 5;
                    graphics2d.drawString(String.valueOf(data[i]), startX + barWidth / 2 - 10, textYPosition);
    
                    startX += barWidth + barSpacing;
                }
            }
        }
    }
    
    private void predictBigrams(String string) {
        String inputSentence = string.trim();

        if (inputSentence.isEmpty()) {
            System.out.println("Please enter a sentence to predict the next word.");
            return;
        }

        String[] words = inputSentence.split(" ");
        List<String> wordList = Arrays.asList(words);
        String lastWord = wordList.get(wordList.size() - 1);
        Map<String, Double> predictedWords = new HashMap<>();

        int numIterations = 20;

        for (int iteration = 0; iteration < numIterations; iteration++) {
            System.out.println("Iteration " + (iteration + 1));

            // Get bigrams directly from the processed text
            List<Map.Entry<String, Integer>> bigramEntries = bigramHashTable.getHashTableDictionaryInMap();
            for (Map.Entry<String, Integer> bigramEntry : bigramEntries) {
                String[] bigramWords = bigramEntry.getKey().split(" ");
                String firstWord = bigramWords[0];
                String currentWord = bigramWords[1];

                if (firstWord.equals(lastWord)) {
                    int bigramCount = bigramEntry.getValue();
                    int unigramCount = bigramHashTable.getWordCount(currentWord);
                    double conditionalProbability = (double) bigramCount / unigramCount;
                    predictedWords.put(currentWord, conditionalProbability);
                }
            }

            // Select the most probable word
            Optional<Map.Entry<String, Double>> maxEntry = predictedWords.entrySet().stream()
                    .max(Map.Entry.comparingByValue());

            System.out.println(maxEntry);
            if (maxEntry.isPresent()) {
                String nextWord = maxEntry.get().getKey();
                inputSentence += " " + nextWord;
                lastWord = nextWord;
                System.out.println(inputSentence);
                predictedWords.clear();
            } 
        }
    }
    
    private void predictTrigrams(String string) {

        System.out.println("Trigram Model");
        String inputSentence = string.trim();

        if (inputSentence.isEmpty()) {
            System.out.println("Please enter a sentence to predict the next word.");
            return;
        }

        String[] words = inputSentence.split(" ");
        List<String> wordList = Arrays.asList(words);

        String lastWord = wordList.get(wordList.size() - 1);
        String secLastWords = "";
        if (wordList.size() >= 2) {
            secLastWords = wordList.get(wordList.size() - 2);
        } else {
            System.out.println("Error List in to small");
        }
        Map<String, Double> predictedWords = new HashMap<>();

        int numIterations = 20;

        for (int iteration = 0; iteration < numIterations; iteration++) {
            System.out.println("Iteration " + (iteration + 1));

            // Get trigrams directly from the processed text
            List<Map.Entry<String, Integer>> trigramsEntries = trigramHashTable.getHashTableDictionaryInMap();

            for (Map.Entry<String, Integer> trigramEntry : trigramsEntries) {
                String[] triwords = trigramEntry.getKey().split(" ");

                if (triwords.length == 3) {
                    String firstWord = triwords[0];
                    String secondWord = triwords[1];
                    String currentWord = triwords[2];

                    if (firstWord.equals(secLastWords) && secondWord.equals(lastWord)) {
                        int trigramCount = trigramEntry.getValue();
                        int bigramCount = bigramHashTable.getWordCount(currentWord);
                        double conditionalProbability = (double) trigramCount / bigramCount;

                        predictedWords.put(currentWord, conditionalProbability);
                    }
                }
            }

            // Select the most probable word
            Optional<Map.Entry<String, Double>> maxEntry = predictedWords.entrySet().stream()
                    .max(Map.Entry.comparingByValue());

            if (maxEntry.isPresent()) {
                String nextWord = maxEntry.get().getKey();
                inputSentence += " " + nextWord;
                secLastWords = lastWord;
                lastWord = nextWord;
                System.out.println(inputSentence);
                predictedWords.clear();
            } 
        }
    }
}