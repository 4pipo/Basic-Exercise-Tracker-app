
//220408044 Doğukan Akmaz CENG



package com.example.demo16;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class GymExerciseTracker extends Application {

    private ObservableList<Exercise> exerciseList = FXCollections.observableArrayList();
    private ListView<Exercise> exerciseListView = new ListView<>(exerciseList);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gym Hareket Takibi");

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        HBox exerciseInputBox = createExerciseInputBox();
        HBox deleteButtonBox = createDeleteButtonBox();
        HBox saveButtonBox = createSaveButtonBox();

        vbox.getChildren().addAll(new Label("Hareket Listesi"), exerciseInputBox, exerciseListView, deleteButtonBox, saveButtonBox);

        Scene scene = new Scene(vbox, 1200, 600);
        primaryStage.setScene(scene);
        String appIconUrl = "https://t3.ftcdn.net/jpg/04/95/71/32/360_F_495713270_ezHIzc7fH7oJWj0MYAAftoGdZum07y49.jpg";
        Image appIcon = new Image(appIconUrl);
        primaryStage.getIcons().add(appIcon);
        String backgroundImageUrl = "https://t4.ftcdn.net/jpg/01/70/28/43/360_F_170284384_ubAtEvvSxgO89aLnfhMw9XlFykZA4QFp.jpg";
        final BackgroundImage[] backgroundImage = {new BackgroundImage(
                new Image(backgroundImageUrl, 1470, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)};
        vbox.setBackground(new Background(backgroundImage[0]));

        vbox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            backgroundImage[0] = new BackgroundImage(
                    new Image(backgroundImageUrl, newWidth.doubleValue(), vbox.getHeight(), false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            vbox.setBackground(new Background(backgroundImage[0]));
        });
        vbox.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            backgroundImage[0] = new BackgroundImage(
                    new Image(backgroundImageUrl, vbox.getWidth(), newHeight.doubleValue(), false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            vbox.setBackground(new Background(backgroundImage[0]));
        });


        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            vbox.setPrefWidth(newWidth.doubleValue());
        });
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            vbox.setPrefHeight(newHeight.doubleValue());
            exerciseListView.setPrefHeight(newHeight.doubleValue() * 0.5);
        });

        primaryStage.show();
    }

    private HBox createExerciseInputBox() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);

        DatePicker datePicker = new DatePicker();

        ComboBox<String> regionComboBox = new ComboBox<>();
        regionComboBox.getItems().addAll("Omuz", "Sırt", "Göğüs", "Kol", "Bacak");

        ComboBox<String> exerciseComboBox = new ComboBox<>();

        regionComboBox.setOnAction(event -> {
            String selectedRegion = regionComboBox.getValue();
            if (selectedRegion != null) {
                setExerciseOptions(selectedRegion, exerciseComboBox);
            }
        });

        ComboBox<Integer> setsComboBox = new ComboBox<>();
        setsComboBox.getItems().addAll(1, 2, 3, 4, 5);

        ComboBox<String> repsComboBox = new ComboBox<>();
        repsComboBox.getItems().addAll("5", "6-8", "8-12", "12-15", "15-20");

        TextField weightInputField = new TextField();

        ComboBox<Integer> workoutTimeComboBox = new ComboBox<>();
        workoutTimeComboBox.getItems().addAll(3,4,5,6,7,8,9,10,11,12,13);

        Button addButton = new Button("Ekle");
        addButton.setOnAction(event -> {
            String exerciseName = exerciseComboBox.getValue();
            String selectedRegion = regionComboBox.getValue();
            int sets = setsComboBox.getValue();
            String reps = repsComboBox.getValue();
            LocalDate date = datePicker.getValue();
            double weight = Double.parseDouble(weightInputField.getText());
            Integer workoutTime = workoutTimeComboBox.getValue();

            if (exerciseName != null && !exerciseName.isEmpty() && selectedRegion != null && date != null && workoutTime != null) {
                Exercise exercise = new Exercise(date, exerciseName, selectedRegion, sets, reps, weight, workoutTime);
                exerciseList.add(exercise);
                exerciseComboBox.getSelectionModel().clearSelection();
                regionComboBox.getSelectionModel().clearSelection();
                setsComboBox.getSelectionModel().clearSelection();
                repsComboBox.getSelectionModel().clearSelection();
                datePicker.setValue(null);
                weightInputField.clear();
                workoutTimeComboBox.getSelectionModel().clearSelection();
            }
        });

        hbox.getChildren().addAll(
                new Label("Tarih:"),
                datePicker,
                new Label("Bölge:"),
                regionComboBox,
                new Label("Hareket Adı:"),
                exerciseComboBox,
                new Label("Set Sayısı:"),
                setsComboBox,
                new Label("Tekrar Sayısı:"),
                repsComboBox,
                new Label("Ağırlık Miktarı:"),
                weightInputField,
                new Label("Antrenman Süresi:"),
                workoutTimeComboBox,
                addButton
        );

        return hbox;
    }

    private HBox createDeleteButtonBox() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);

        Button deleteButton = new Button("Sil");
        deleteButton.setOnAction(event -> {
            Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
            if (selectedExercise != null) {
                exerciseList.remove(selectedExercise);
            }
        });

        hbox.getChildren().add(deleteButton);

        return hbox;
    }

    private HBox createSaveButtonBox() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);

        Button saveButton = new Button("Kaydet");
        saveButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Dosya Kaydet");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Metin Dosyası", "*.txt"));
            File file = fileChooser.showSaveDialog(hbox.getScene().getWindow());

            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    for (Exercise exercise : exerciseList) {
                        writer.write(exercise.toString() + "\n");
                    }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        hbox.getChildren().add(saveButton);

        return hbox;
    }

    private void setExerciseOptions(String region, ComboBox<String> exerciseComboBox) {
        exerciseComboBox.getItems().clear();
        switch (region) {
            case "Omuz":
                exerciseComboBox.getItems().addAll(
                        "Dumbell Press", "Overhead Press", "Arnold Press",
                        "Lateral Raise", "FacePull", "Upright Row");
                break;
            case "Göğüs":
                exerciseComboBox.getItems().addAll(
                        "Dumbell Press", "Bench Press", "İncline Press",
                        "Dumbell Fly", "Cable Fly", "Cable Crossover", "Push Up");
                break;
            case "Sırt":
                exerciseComboBox.getItems().addAll(
                        "Barbell Row", "Lat Pull Down", "Dumbell Row",
                        "Cable Row", "Pull Up");
                break;
            case "Kol":
                exerciseComboBox.getItems().addAll(
                        "Dumbell Curl", "Arnold Curl", "Barbell Curl",
                        "Skull Crusher", "Rope Pushdown");
                break;
            case "Bacak":
                exerciseComboBox.getItems().addAll(
                        "Squat", "Legpress", "Leg Extension",
                        "Romanian Deadlift", "Leg Curl");
                break;
        }
    }

    private static class Exercise {
        private LocalDate date;
        private String name;
        private String region;
        private int sets;
        private String reps;
        private double weight;
        private int workoutTime;

        public Exercise(LocalDate date, String name, String region, int sets, String reps, double weight, int workoutTime) {
            this.date = date;
            this.name = name;
            this.region = region;
            this.sets = sets;
            this.reps = reps;
            this.weight = weight;
            this.workoutTime = workoutTime;
        }

        @Override
        public String toString() {
            return date + ", " + region + ", " + name + ", " + sets + " set, " + reps + " tekrar, " + weight + " kg, " + workoutTime + " mins";
        }
    }
}