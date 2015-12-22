package app;

import analysis.*;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class App extends Application {

    //File Chooser
    final FileChooser fileChooser = new FileChooser();
	
    Stage window;
    TableView<TargetToGuess> table;
    TextField targetInput;
    private PronunciationGuesser guesser;
    
    private Set<String> alreadyGuessed = new HashSet<String>();

    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Speech Predictor");

        //Name column
        TableColumn<TargetToGuess, String> targetColumn = new TableColumn<>("Target");
        targetColumn.setMinWidth(200);
        targetColumn.setCellValueFactory(new PropertyValueFactory<>("target"));

        //Price column
        TableColumn<TargetToGuess, Double> guessColumn = new TableColumn<>("Guess");
        guessColumn.setMinWidth(100);
        guessColumn.setCellValueFactory(new PropertyValueFactory<>("guess"));

        // Info Button
        Button instructionsButton = new Button("Instructions");
        instructionsButton.setOnAction(e -> instructionsButtonClicked());
        
        // Notes Button
        Button notesButton = new Button("Notes/Credit");
        notesButton.setOnAction(e -> notesButtonClicked());
        
        // Top Horizontal
        HBox infoBox = new HBox();
        infoBox.setPadding(new Insets(10,10,10,10));
        infoBox.setSpacing(10);
        infoBox.getChildren().addAll(instructionsButton, notesButton);

        // Load Button
        Button loadFileButton = new Button("Load Training Set From File");
        loadFileButton.setOnAction(e -> loadButtonClicked(primaryStage));
        HBox loadingBox = new HBox();
        loadingBox.setPadding(new Insets(10,10,10,10));
        loadingBox.setSpacing(10);
        loadingBox.getChildren().addAll(loadFileButton);
        
        //Target input
        targetInput = new TextField();
        targetInput.setPromptText("Target Word");
        targetInput.setMinWidth(100);

        //Guess Button
        Button guessButton = new Button("Guess");
        guessButton.setOnAction(e -> guessButtonClicked());

        HBox guessBox = new HBox();
        guessBox.setPadding(new Insets(10,10,10,10));
        guessBox.setSpacing(10);
        guessBox.getChildren().addAll(targetInput, guessButton);

        table = new TableView<>();
        table.setItems(getProduct());
        table.getColumns().addAll(targetColumn, guessColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(infoBox, table, guessBox, loadingBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    private void notesButtonClicked() {
		popMessage("\u00a9 Parker Stakoff and Carissa Redfield"
				+ "\nPlease read the paper that we wrote in order to "
				+ "learn more."
				+ "\n\nNotes about the program:"
				+ "\n1. Our next project will implement insertion/deletion "
				+ "recognition."
				+ "\n2. Phonemes that have a space, according to the Arpabet, must "
				+ "be inputted with an underscore, replacing the space. For example, "
				+ "'EH R' must be written as 'EH_R'."
				+ "\n3. We only include consonants that are phonemic in English. "
				+ "Therefore, we have removed some symbols from the Arpabet. We have removed:"
				+ "\nEM, EN, ENG, EL, NX, Q"
				, AlertType.INFORMATION);
	}

	private void instructionsButtonClicked() {
		popMessage("Load a data/training set from a file on your computer."
				+ "\nThe file must be of the following format:\n"
				+ "\n1. All phonemes are represented by the arpabet (en.wikipedia.org/wiki/Arpabet)"
				+ "\n	**Please read our Notes to know which phonemes from "
				+ "the Arpabet are valid in this program.**"
				+ "\n2. Phonemes must be separated by dashes (-)"
				+ "\n3. Syllables must be separated by single spaces"
				+ "\n4. Each word should be on its own line, where "
				+ "the target word comes a line before how the child pronounced the word."
				+ "\nFor example, a file could look like:\n"
				+ "\nS-IH L-IY"
				+ "\nTH-IH L-IY"
				+ "\nL-IH-S-P"
				+ "\nL-IH-TH-P"
				+ "\nD-AA-G-Z"
				+ "\nD-AA-G-DH"
				+ "\nSH-IH-P"
				+ "\nS-IH-P"
				+ "\nW-AA-CH"
				+ "\nW-AA-SH", 
				AlertType.INFORMATION);
	}

	private Object loadButtonClicked(Stage stage) {
    	File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
				loadFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		return null;
	}
    
    private void loadFile(File file) throws FileNotFoundException, IOException {
    	SpecificRuleFormer rp = new SpecificRuleFormer(file);
    	RuleGeneralizer rg = new RuleGeneralizer(rp.getRules());
		Collection<GeneralizedRule> genRules = rg.getGeneralizedRules();
		guesser = new PronunciationGuesser(genRules, rp.getPhonemeToSpecificRules());
		for (Entry<String, String> e : rp.getInputtedData().entrySet()) {
			String target = e.getKey();
			if (!alreadyGuessed.contains(target)) {
				addTargetGuessToTable(target, e.getValue());
	    		alreadyGuessed.add(target);
			}
		}
    }

	//Add button clicked
    public void guessButtonClicked(){
    	if (alreadyGuessed.size() == 0) {
    		popMessage("Please upload data from a file before making guesses.", AlertType.ERROR);
    		return;
    	}
    	String target = targetInput.getText().toUpperCase(Locale.ENGLISH);
    	if (target.isEmpty()) {
    		popMessage("Please enter a target word.", AlertType.ERROR);
    		return;
    	}
    	if (alreadyGuessed.contains(target)) {
    		return;
    	}
    	try {
    		String guess = guesser.guessPronunciationOfTargetWord(target);
    		alreadyGuessed.add(target);
        	addTargetGuessToTable(target, guess);
            targetInput.clear();
    	} catch (Exception ex) {
    		 popMessage(target + " contains an invalid "
	        			+ "phoneme according to the Arpabet."
	    		 		+ "\nPlease make sure to add a '-' after every phoneme."
	    		 		+ "\nOptionally, "
	    		 		+ "add a space after every syllable. For example:\n"
	    		 		+ "B-EY K-ER", AlertType.ERROR);
    		 return;
    	}
    }
    
    private void popMessage(String s, AlertType alertType) {
    	Alert alert = new Alert(alertType);
    	alert.setTitle("");
    	alert.setHeaderText("");
    	alert.setContentText(s);
    	alert.showAndWait();
    }
    
    private void addTargetGuessToTable(String t, String g) {
    	table.getItems().add(new TargetToGuess(t, g));
    }

    //Get all of the products
    public ObservableList<TargetToGuess> getProduct(){
        ObservableList<TargetToGuess> guesses = FXCollections.observableArrayList();
        return guesses;
    }


}