package app;

import analysis.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Frame1 extends Application {

    //File Chooser
    final FileChooser fileChooser = new FileChooser();
	
    Stage window;
    TableView<TargetToGuess> table;
    TextField targetInput;
    private PronunciationGuesser guesser;

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
        
        Button loadFileButton = new Button("Load Data From File");
        loadFileButton.setOnAction(e -> loadButtonClicked(primaryStage));
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(10,10,10,10));
        topHBox.setSpacing(10);
        topHBox.getChildren().addAll(loadFileButton);
        
        //Target input
        targetInput = new TextField();
        targetInput.setPromptText("Target");
        targetInput.setMinWidth(100);

        //Guess Button
        Button guessButton = new Button("Guess");
        guessButton.setOnAction(e -> guessButtonClicked());

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(targetInput, guessButton);

        table = new TableView<>();
        table.setItems(getProduct());
        table.getColumns().addAll(targetColumn, guessColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(topHBox, table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
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
			addTargetGuessToTable(e.getKey(), e.getValue());
		}
    }

	//Add button clicked
    public void guessButtonClicked(){
    	String target = targetInput.getText();
    	String guess = guesser.guessPronunciationOfTargetWord(target);
    	addTargetGuessToTable(target, guess);
        targetInput.clear();
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