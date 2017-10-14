package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Main extends Application {

    JSONArray albums = new JSONArray();
    private int WINDOW_WIDTH = 1000;
    private int WINDOW_HEIGHT = 325;
    GridPane container = new GridPane();
    boolean showURLs = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("");
        BorderPane root = new BorderPane();
        root.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        root = buildTitle(root);

        //build bottom area with submit button etc.
        AnchorPane helpPane = new AnchorPane();
        helpPane = buildHelpText(helpPane);


        //right side of bottom section with submit button and text area
        final Button submit = new Button("Submit");
        TextField userTextField = new TextField();
        userTextField.setPromptText("1-100 Here");
        HBox helpBox = new HBox();
        helpBox.setSpacing(30.0);
        helpBox.getChildren().addAll(userTextField, submit);

        //toggle for showing urls
        ToggleButton urlToggle = new ToggleButton("Show URLs");
        urlToggle.setId("url-toggle");
        ToggleGroup group = new ToggleGroup();
        urlToggle.setSelected(true);
        urlToggle.setToggleGroup(group);

        //not entirely useful currently but would come in handy if thoroughly styling
        VBox toggleWrapper = new VBox();
        toggleWrapper.getChildren().add(urlToggle);

        //build out Anchorpane in bottom section of root
        helpPane.getChildren().addAll(helpBox, toggleWrapper);
        helpPane.setRightAnchor(helpBox, 5.0);
        helpPane.setLeftAnchor(toggleWrapper, 500.0);
        root.setBottom(helpPane);
        Insets helpPaneInsets = new Insets(5,0,0,0);
        root.setMargin(helpPane, helpPaneInsets);

        //central section with info
        ScrollPane albumsContainer = new ScrollPane();
        albumsContainer.setFitToWidth(true);

        //create container for info
        VBox albumsDisplay = new VBox();
        albumsDisplay.setSpacing(20);
        albumsDisplay.setAlignment(Pos.TOP_CENTER);
        albumsContainer.setContent(albumsDisplay);
        root.setCenter(albumsContainer);

        // HANDLERS & SUCH ***********************************************************

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                //if toggled, build out albums with urls
                if(new_toggle != null){
                    showURLs = true;
                    submit.fire();
                } else{
                    showURLs = false;
                    submit.fire();
                }
            }
        });
        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                //if the value in the text area is unusable display warning text
                if(checkParsInt(userTextField.getText()) > 100 ||
                        checkParsInt(userTextField.getText()) <= 0) {
                    Text warning = new Text("YOU FAILED");
                    warning.setId("warning-text");
                    albumsDisplay.getChildren().clear();
                    albumsDisplay.getChildren().addAll(warning);
                } else {
                    AlbumMiner miner = new AlbumMiner(userTextField.getText());

                    //attempt to mine information from url
                    try {
                        albums = miner.mine();
                    } catch (IOException ex) {
                        System.out.println("All the things broke");
                    }
                    //clear out albums container
                    albumsDisplay.getChildren().clear();

                    //build album grid entries
                    for (int i = 0; i < albums.length(); i++) {

                        //build id and title container

                        container = new GridPane();
                        container.setId("album-container");
                        container.setHgap(15);

                        // Setting columns size in percent
                        ColumnConstraints column = new ColumnConstraints();
                        column.setPercentWidth(15);
                        column.setHalignment(HPos.RIGHT);
                        container.getColumnConstraints().add(column);

                        column = new ColumnConstraints();
                        column.setPercentWidth(70);
                        column.setHalignment(HPos.RIGHT);
                        container.getColumnConstraints().add(column);

                        container.setMaxWidth(800);
                        container.setAlignment(Pos.TOP_CENTER);

                        //build default labels
                        Text idLabel = new Text("ID:");
                        Text titleLabel = new Text("Title:");
                        titleLabel.setId("album-title-label");
                        idLabel.setId("album-id-label");
                        container.add(idLabel, 0, 0);
                        container.add(titleLabel, 0, 1);
                        JSONObject album = albums.getJSONObject(i);
                        //build url labels
                        if(showURLs) {
                            Text urlLabel = new Text("URL:");
                            Text thumbnailUrlLabel = new Text("Thumbnail URL:");
                            container.add(urlLabel, 0, 2);
                            container.add(thumbnailUrlLabel, 0, 3);
                        }

                        //build default info
                        Text id = new Text(Integer.toString(album.getInt("id")));
                        id.setId("album-id");
                        Text title = new Text(album.getString("title"));
                        title.setId("album-title");
                        container.add(id, 1, 0);
                        container.add(title, 1, 1);

                        //build url info
                        if(showURLs) {
                            Hyperlink urlLink = new Hyperlink();
                            urlLink.setText(album.getString("url"));
                            Hyperlink thumbnailUrlLink = new Hyperlink();
                            thumbnailUrlLink.setText(album.getString("thumbnailUrl"));
                            container.add(urlLink, 1, 2);
                            container.add(thumbnailUrlLink, 1, 3);
                        }
                        //add the newest entry
                        albumsDisplay.getChildren().add(container);
                    }
                }
            }
        });
        //*****************************************************************************

        //Finalize and render scene
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private AnchorPane buildHelpText(AnchorPane helpPane){
        BorderPane.setAlignment(helpPane, Pos.CENTER);
        helpPane.setId("help-pane");
        Text helpText = new Text("Type a whole number from 1-100 and then hit submit\n" +
                "Note: this will break if you fail to keep it 1-100");
        HBox helpTextWrapper = new HBox();
        helpTextWrapper.getChildren().addAll(helpText);
        helpPane.getChildren().addAll(helpTextWrapper);
        helpPane.setLeftAnchor(helpTextWrapper, 5.0);
        return helpPane;
    }
    private BorderPane buildBottom(BorderPane root){

        return root;
    }
    private BorderPane buildTitle(BorderPane root){
        //  build title
        Text scenetitle = new Text("The Albumonizer");
        scenetitle.setId("scene-title");
        root.setTop(scenetitle);
        BorderPane.setAlignment(scenetitle, Pos.TOP_CENTER);
        return root;
    }

    // make sure input is actually an integer
    private static int checkParsInt(String input){
        try{
            return Integer.parseInt(input);
        } catch(NumberFormatException e){
            return 0;
        }
    }

}
