/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glavo.monetfx.demo;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.glavo.monetfx.Brightness;
import org.glavo.monetfx.ColorRole;
import org.glavo.monetfx.ColorScheme;
import org.glavo.monetfx.beans.property.ColorSchemeProperty;
import org.glavo.monetfx.beans.property.SimpleColorSchemeProperty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.glavo.monetfx.ColorRole.*;

public final class MonetFXDemo extends Application {

    @SuppressWarnings("DataFlowIssue")
    private static final String ROOT_CSS = MonetFXDemo.class.getResource("root.css").toExternalForm();

    private static final Color DEFAULT_COLOR = Color.web("#5C6BC0");
    private static final Color DARK_COLOR = Color.web("#141314");

    private final FileChooser fileChooser = new FileChooser();
    {
        fileChooser.setTitle("Choose background image");
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("Image File", "*.png", "*.jpg", "*.jpeg")
        );
    }

    private final BooleanProperty darkModeProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>(DEFAULT_COLOR);
    private final ObjectProperty<Image> backgroundImageProperty = new SimpleObjectProperty<>();

    private final ColorSchemeProperty scheme = new SimpleColorSchemeProperty();

    private boolean skipUpdateScheme = false;
    private final InvalidationListener listener = observable -> {
        if (skipUpdateScheme) {
            return;
        }

        if (observable == colorProperty) {
            skipUpdateScheme = true;
            backgroundImageProperty.set(null);
            skipUpdateScheme = false;
        }

        Brightness brightness = darkModeProperty.get() ? Brightness.DARK : Brightness.LIGHT;
        Image image = backgroundImageProperty.get();
        if (image == null) {
            Color color = colorProperty.get();
            if (color == null) {
                color = DEFAULT_COLOR;
            }

            scheme.set(ColorScheme.fromSeed(color, brightness));
        } else {
            scheme.set(ColorScheme.fromImage(image, brightness));
        }
    };

    {
        listener.invalidated(null);
        colorProperty.addListener(listener);
        backgroundImageProperty.addListener(listener);
        darkModeProperty.addListener(listener);
    }

    private Node createCard(ColorRole cardRole, ColorRole textRole) {
        StackPane card = new StackPane();
        card.setPrefSize(StackPane.USE_COMPUTED_SIZE, StackPane.USE_COMPUTED_SIZE);
        Label text = new Label(cardRole.toString());

        card.setStyle("-fx-background-color: " + cardRole.getVariableName());

        if (textRole != null) {
            text.setStyle("-fx-text-fill: " + textRole.getVariableName());
        } else {
            text.setTextFill(Color.WHITE);
        }

        card.getChildren().add(text);
        return card;
    }

    private HBox createSplitCard(ColorRole cardRole1, ColorRole textRole1, ColorRole cardRole2, ColorRole textRole2) {
        Node card1 = createCard(cardRole1, textRole1);
        Node card2 = createCard(cardRole2, textRole2);

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);

        return new HBox(card1, card2);
    }

    Path prevCssFile;
    private void updateCss(Scene scene) {
        String css = scheme.get().toStyleSheet();
        try {
            Path tempFile = Files.createTempFile("monetfx-", ".css");
            System.out.println("CSS File: " + tempFile.toAbsolutePath());
            tempFile.toFile().deleteOnExit();
            try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
                writer.write(css);
            }

            if (prevCssFile != null)
                Files.deleteIfExists(prevCssFile);
            prevCssFile = tempFile;
            scene.getStylesheets().setAll(tempFile.toUri().toString(), ROOT_CSS);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void updateBackgroundImage(Path file) {
        try (InputStream inputStream = Files.newInputStream(file)) {
            Image image = new Image(inputStream);
            backgroundImageProperty.set(image);

            skipUpdateScheme = true;
            colorProperty.set(scheme.get().getSourceColor());
            skipUpdateScheme = false;
        } catch (IOException e) {
            System.err.println("Failed to load background image from: " + file);
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        BorderPane settingsPane = new BorderPane();
        settingsPane.setPadding(new Insets(10));
        {
            settingsPane.setStyle("-fx-background-color: -monet-primary-container");

            StackPane titlePane = new StackPane();
            settingsPane.setTop(titlePane);
            {
                Label label = new Label("Settings");
                label.setStyle("-fx-text-fill: -monet-on-primary-container; -fx-font-size: 16");
                label.setPadding(new Insets(0, 0, 10, 0));
                titlePane.getChildren().add(label);
            }

            VBox content = new VBox(10);
            settingsPane.setCenter(content);
            {
                BorderPane darkModePane = new BorderPane();
                {
                    Label label = new Label("Dark Mode");
                    BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                    label.setStyle("-fx-text-fill: -monet-on-primary-container");
                    darkModePane.setLeft(label);

                    RadioButton toggleButton = new RadioButton();
                    toggleButton.selectedProperty().bindBidirectional(darkModeProperty);
                    darkModePane.setRight(toggleButton);
                }

                BorderPane colorPickerPane = new BorderPane();
                {
                    Label label = new Label("Color Picker");
                    BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                    label.setStyle("-fx-text-fill: -monet-on-primary-container");
                    colorPickerPane.setLeft(label);

                    ColorPicker colorPicker = new ColorPicker();
                    colorPicker.getCustomColors().setAll(
                            DEFAULT_COLOR,
                            Color.web("#b33b15"),
                            Color.web("#63a002"),
                            Color.web("#769cdf"),
                            Color.web("#ffde3f")
                    );
                    colorPicker.valueProperty().bindBidirectional(colorProperty);
                    colorPickerPane.setRight(colorPicker);
                }

                BorderPane backgroundChooserPane = new BorderPane();
                {
                    Label label = new Label("Background Image");
                    BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                    label.setStyle("-fx-text-fill: -monet-on-primary-container");
                    backgroundChooserPane.setLeft(label);


                    Button chooseButton = new Button("Choose");
                    chooseButton.setOnAction(event -> {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            updateBackgroundImage(file.toPath());
                            fileChooser.setInitialDirectory(file.getParentFile());
                        }
                    });
                    backgroundChooserPane.setRight(chooseButton);
                }

                content.getChildren().setAll(darkModePane, colorPickerPane, backgroundChooserPane);
            }
        }

        GridPane gridPane = new GridPane();
        root.setCenter(gridPane);
        {
            int defaultCardWidth = 260;
            int defaultCardHeight = 50;
            int smallGap = 5;
            int largeGap = 10;

            gridPane.getColumnConstraints().setAll(new ColumnConstraints(defaultCardWidth), new ColumnConstraints(smallGap), new ColumnConstraints(defaultCardWidth), new ColumnConstraints(smallGap), new ColumnConstraints(defaultCardWidth), new ColumnConstraints(largeGap), new ColumnConstraints(defaultCardWidth));

            gridPane.getRowConstraints().setAll(new RowConstraints(defaultCardHeight * 2), new RowConstraints(defaultCardHeight), new RowConstraints(smallGap), new RowConstraints(defaultCardHeight), new RowConstraints(defaultCardHeight), new RowConstraints(largeGap), new RowConstraints(defaultCardHeight * 2), new RowConstraints(defaultCardHeight), new RowConstraints(defaultCardHeight), new RowConstraints(largeGap), new RowConstraints(defaultCardHeight * 2), new RowConstraints(smallGap), new RowConstraints(defaultCardHeight * 2), new RowConstraints(smallGap), new RowConstraints(defaultCardHeight * 2));

            gridPane.add(createCard(PRIMARY, ON_PRIMARY), 0, 0);
            gridPane.add(createCard(ON_PRIMARY, PRIMARY), 0, 1);
            gridPane.add(createCard(PRIMARY_CONTAINER, ON_PRIMARY_CONTAINER), 0, 3);
            gridPane.add(createCard(ON_PRIMARY_CONTAINER, PRIMARY_CONTAINER), 0, 4);

            gridPane.add(createCard(SECONDARY, ON_SECONDARY), 2, 0);
            gridPane.add(createCard(ON_SECONDARY, SECONDARY), 2, 1);
            gridPane.add(createCard(SECONDARY_CONTAINER, ON_SECONDARY_CONTAINER), 2, 3);
            gridPane.add(createCard(ON_SECONDARY_CONTAINER, SECONDARY_CONTAINER), 2, 4);

            gridPane.add(createCard(TERTIARY, ON_TERTIARY), 4, 0);
            gridPane.add(createCard(ON_TERTIARY, TERTIARY), 4, 1);
            gridPane.add(createCard(TERTIARY_CONTAINER, ON_TERTIARY_CONTAINER), 4, 3);
            gridPane.add(createCard(ON_TERTIARY_CONTAINER, TERTIARY_CONTAINER), 4, 4);

            gridPane.add(createCard(ERROR, ON_ERROR), 6, 0);
            gridPane.add(createCard(ON_ERROR, ERROR), 6, 1);
            gridPane.add(createCard(ERROR_CONTAINER, ON_ERROR_CONTAINER), 6, 3);
            gridPane.add(createCard(ON_ERROR_CONTAINER, ERROR_CONTAINER), 6, 4);

            gridPane.add(createSplitCard(PRIMARY_FIXED, ON_PRIMARY_FIXED, PRIMARY_FIXED_DIM, ON_PRIMARY_FIXED_VARIANT), 0, 6);
            gridPane.add(createCard(ON_PRIMARY_FIXED, PRIMARY_FIXED), 0, 7);
            gridPane.add(createCard(ON_PRIMARY_FIXED_VARIANT, PRIMARY_FIXED_DIM), 0, 8);

            gridPane.add(createSplitCard(SECONDARY_FIXED, ON_SECONDARY_FIXED, SECONDARY_FIXED_DIM, ON_SECONDARY_FIXED_VARIANT), 2, 6);
            gridPane.add(createCard(ON_SECONDARY_FIXED, SECONDARY_FIXED), 2, 7);
            gridPane.add(createCard(ON_SECONDARY_FIXED_VARIANT, SECONDARY_FIXED_DIM), 2, 8);

            gridPane.add(createSplitCard(TERTIARY_FIXED, ON_TERTIARY_FIXED, TERTIARY_FIXED_DIM, ON_TERTIARY_FIXED_VARIANT), 4, 6);
            gridPane.add(createCard(ON_TERTIARY_FIXED, TERTIARY_FIXED), 4, 7);
            gridPane.add(createCard(ON_TERTIARY_FIXED_VARIANT, TERTIARY_FIXED_DIM), 4, 8);

            gridPane.add(createCard(SURFACE_DIM, ON_SURFACE), 0, 10);
            gridPane.add(createCard(SURFACE, ON_SURFACE), 2, 10);
            gridPane.add(createCard(SURFACE_BRIGHT, ON_SURFACE), 4, 10);

            HBox surfaceContainerBox = new HBox(createCard(SURFACE_CONTAINER_LOWEST, ON_SURFACE), createCard(SURFACE_CONTAINER_LOW, ON_SURFACE), createCard(SURFACE_CONTAINER, ON_SURFACE), createCard(SURFACE_CONTAINER_HIGH, ON_SURFACE), createCard(SURFACE_CONTAINER_HIGHEST, ON_SURFACE));
            surfaceContainerBox.getChildren().forEach(it -> HBox.setHgrow(it, Priority.ALWAYS));
            GridPane.setColumnSpan(surfaceContainerBox, 5);
            gridPane.add(surfaceContainerBox, 0, 12);

            HBox bottomBox = new HBox(createCard(ON_SURFACE, SURFACE), createCard(ON_SURFACE_VARIANT, SURFACE_VARIANT), createCard(OUTLINE, SURFACE), createCard(OUTLINE_VARIANT, ON_SURFACE));
            bottomBox.getChildren().forEach(it -> HBox.setHgrow(it, Priority.ALWAYS));
            GridPane.setColumnSpan(bottomBox, 5);
            gridPane.add(bottomBox, 0, 14);

            VBox inverseBox = new VBox(createCard(INVERSE_SURFACE, SURFACE), createCard(INVERSE_ON_SURFACE, ON_SURFACE), createCard(INVERSE_PRIMARY, PRIMARY));
            inverseBox.getChildren().forEach(it -> VBox.setVgrow(it, Priority.ALWAYS));
            GridPane.setRowSpan(inverseBox, 3);
            gridPane.add(inverseBox, 6, 10);

            HBox bottomRightBox = createSplitCard(SCRIM, null, SHADOW, null);
            bottomRightBox.setSpacing(largeGap);
            gridPane.add(bottomRightBox, 6, 14);


            GridPane.setRowSpan(settingsPane, 3);
            gridPane.add(settingsPane, 6, 6);
        }

        root.backgroundProperty().bind(Bindings.createObjectBinding(() -> {
            Image image = backgroundImageProperty.get();
            if (image != null) {
                return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)));
            } else {
                Color backgroundColor = darkModeProperty.get() ? DARK_COLOR : Color.WHITE;
                return new Background(new BackgroundFill(backgroundColor, null, null));
            }
        }, backgroundImageProperty, darkModeProperty));

        root.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                if (event.getDragboard().getFiles().stream().anyMatch(it -> {
                    String fileName = it.getName();
                    return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
                })) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }
            event.consume();
        });
        root.setOnDragDropped(event -> {
            List<File> files = event.getDragboard().getFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        updateBackgroundImage(file.toPath());
                    }
                }
            }
        });

        Scene scene = new Scene(root, 1080, 800);

        updateCss(scene);
        scheme.addListener((observable, oldValue, newValue) -> updateCss(scene));

        primaryStage.setTitle("MonetFX Demo");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        root.requestFocus();
    }


    public static void main(String[] args) {
        Application.launch(MonetFXDemo.class, args);
    }
}
