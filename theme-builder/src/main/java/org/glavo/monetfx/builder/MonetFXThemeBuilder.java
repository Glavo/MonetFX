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
package org.glavo.monetfx.builder;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
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
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.glavo.monetfx.*;
import org.glavo.monetfx.beans.property.ColorSchemeProperty;
import org.glavo.monetfx.beans.property.SimpleColorSchemeProperty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import static org.glavo.monetfx.ColorRole.*;

public final class MonetFXThemeBuilder extends Application {

    private static final Color DEFAULT_COLOR = Color.web("#5C6BC0");
    private static final String STYLESHEET_PATH = MonetFXThemeBuilder.class.getResource("style.css").toExternalForm();
    private static final EnumSet<ColorStyle> SPEC_2025_STYLES = EnumSet.of(
            ColorStyle.EXPRESSIVE, ColorStyle.VIBRANT, ColorStyle.TONAL_SPOT, ColorStyle.NEUTRAL);
    private static final String RESET_SVG = "M12 20q-3.35 0-5.675-2.325T4 12Q4 8.65 6.325 6.325T12 4q1.725 0 3.3.7125T18 6.75V4h2v7H13V9h4.2q-.8-1.4-2.1875-2.2T12 6Q9.5 6 7.75 7.75T6 12t1.75 4.25T12 18q1.925 0 3.475-1.1T17.65 14h2.1q-.7 2.65-2.85 4.325T12 20Z";

    private static String toWeb(Color color) {
        return String.format("#%02X%02X%02X",
                Math.round(color.getRed() * 255),
                Math.round(color.getGreen() * 255),
                Math.round(color.getBlue() * 255));
    }

    private final FileChooser backgroundFileChooser = new FileChooser();

    {
        backgroundFileChooser.setTitle("Choose background image");
        backgroundFileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("Image File", "*.png", "*.jpg", "*.jpeg")
        );
    }

    private final FileChooser exportFileChooser = new FileChooser();

    private final BooleanProperty darkModeProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<Color> primaryColorProperty = new SimpleObjectProperty<>(DEFAULT_COLOR);
    private final ObjectProperty<Color> secondaryColorProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> tertiaryColorProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> neutralColorProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> neutralVariantColorProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> errorColorProperty = new SimpleObjectProperty<>();

    private final ObjectProperty<Image> backgroundImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<ColorStyle> colorStyleProperty = new SimpleObjectProperty<>(ColorStyle.TONAL_SPOT);
    private final DoubleProperty contrastProperty = new SimpleDoubleProperty(0.0);
    private final ObjectProperty<TargetPlatform> platformProperty = new SimpleObjectProperty<>(TargetPlatform.PHONE);
    private final ObjectProperty<ColorSpecVersion> specVersionProperty = new SimpleObjectProperty<>(ColorSpecVersion.SPEC_2021);

    private final ColorSchemeProperty scheme = new SimpleColorSchemeProperty();

    /// Flag to skip updating scheme in listener to avoid infinite recursion.
    private boolean skipUpdateScheme = false;
    private final InvalidationListener listener = observable -> {
        if (skipUpdateScheme) {
            return;
        }

        skipUpdateScheme = true;

        if (observable == primaryColorProperty) {
            backgroundImageProperty.set(null);
        }

        if (observable == colorStyleProperty && !SPEC_2025_STYLES.contains(colorStyleProperty.get())) {
            platformProperty.set(TargetPlatform.PHONE);
            specVersionProperty.set(ColorSpecVersion.SPEC_2021);
        }

        if (observable == platformProperty) {
            darkModeProperty.set(true);
            specVersionProperty.set(ColorSpecVersion.SPEC_2025);
        }

        skipUpdateScheme = false;

        Brightness brightness = darkModeProperty.get() ? Brightness.DARK : Brightness.LIGHT;
        Image image = backgroundImageProperty.get();

        ColorSchemeBuilder builder = ColorScheme.newBuilder();
        if (image == null || observable == primaryColorProperty) {
            Color color = primaryColorProperty.get();
            if (color == null) {
                color = DEFAULT_COLOR;
            }

            builder.setPrimaryColorSeed(color);
        } else {
            builder.setWallpaper(image, DEFAULT_COLOR);
        }

        scheme.set(builder
                .setSecondaryColorSeed(secondaryColorProperty.get())
                .setTertiaryColorSeed(tertiaryColorProperty.get())
                .setNeutralColorSeed(neutralColorProperty.get())
                .setNeutralVariantColorSeed(neutralVariantColorProperty.get())
                .setErrorColorSeed(errorColorProperty.get())
                .setBrightness(brightness)
                .setColorStyle(colorStyleProperty.get())
                .setContrast(Contrast.of(contrastProperty.get()))
                .setPlatform(platformProperty.get())
                .setSpecVersion(specVersionProperty.get())
                .build());
    };

    {
        listener.invalidated(primaryColorProperty);
        primaryColorProperty.addListener(listener);
        secondaryColorProperty.addListener(listener);
        tertiaryColorProperty.addListener(listener);
        neutralColorProperty.addListener(listener);
        neutralVariantColorProperty.addListener(listener);
        errorColorProperty.addListener(listener);
        backgroundImageProperty.addListener(listener);
        darkModeProperty.addListener(listener);
        colorStyleProperty.addListener(listener);
        platformProperty.addListener(listener);
        specVersionProperty.addListener(listener);
        contrastProperty.addListener(listener);
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
            scene.getStylesheets().setAll(tempFile.toUri().toString(), STYLESHEET_PATH);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void updateBackgroundImage(Path file) {
        try (InputStream inputStream = Files.newInputStream(file)) {
            Image image = new Image(inputStream);
            backgroundImageProperty.set(image);

            skipUpdateScheme = true;
            primaryColorProperty.set(scheme.get().getPrimaryColorSeed());
            skipUpdateScheme = false;
        } catch (IOException e) {
            System.err.println("Failed to load background image from: " + file);
            e.printStackTrace(System.err);
        }
    }

    private Node createCard(ColorRole cardRole, ColorRole textRole) {
        BorderPane card = new BorderPane();
        card.setPrefSize(StackPane.USE_COMPUTED_SIZE, StackPane.USE_COMPUTED_SIZE);
        card.setStyle("-fx-background-color: " + cardRole.getVariableName());

        Label nameLabel = new Label(cardRole.toString());
        BorderPane.setAlignment(nameLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(nameLabel, new Insets(5));
        card.setTop(nameLabel);

        Label colorLabel = new Label();
        BorderPane.setAlignment(colorLabel, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(colorLabel, new Insets(5));
        card.setBottom(colorLabel);

        ObjectBinding<Color> colorBinding = scheme.getColor(cardRole);
        colorLabel.textProperty().bind(Bindings.createStringBinding(() -> toWeb(colorBinding.get()), colorBinding));

        if (textRole != null) {
            nameLabel.textFillProperty().bind(scheme.getColor(textRole));
            colorLabel.textFillProperty().bind(scheme.getColor(textRole));
        } else {
            nameLabel.setTextFill(Color.WHITE);
            colorLabel.setTextFill(Color.WHITE);
        }

        card.setCursor(Cursor.CROSSHAIR);
        card.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                String color = toWeb(colorBinding.get());
                Dragboard.getSystemClipboard().setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, color));
                System.out.println("Copy color " + color + " to clipboard");
                event.consume();
            }
        });

        return card;
    }

    private HBox createSplitCard(ColorRole cardRole1, ColorRole textRole1, ColorRole cardRole2, ColorRole textRole2) {
        Node card1 = createCard(cardRole1, textRole1);
        Node card2 = createCard(cardRole2, textRole2);

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);

        return new HBox(card1, card2);
    }

    private BorderPane createColorSettingsPane(String name, ObjectProperty<Color> property, Color... customColors) {
        BorderPane colorPane = new BorderPane();

        Label label = new Label(name);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        label.setStyle("-fx-text-fill: -monet-on-surface");
        colorPane.setLeft(label);

        ColorPicker colorPicker = new ColorPicker();
        BorderPane.setAlignment(colorPicker, Pos.CENTER_RIGHT);
        colorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        colorPicker.setPrefSize(90, 25);
        if (property != primaryColorProperty) {
            ChangeListener<Color> changeListener = (observable, oldValue, newValue) -> {
                if (newValue == null) {
                    colorPicker.getStyleClass().add("color-picker-null");
                } else {
                    colorPicker.getStyleClass().remove("color-picker-null");
                }
            };
            colorPicker.valueProperty().addListener(changeListener);
        }
        colorPicker.valueProperty().bindBidirectional(property);
        colorPane.setCenter(colorPicker);

        SVGPath resetIcon = new SVGPath();
        resetIcon.setContent(RESET_SVG);
        resetIcon.getStyleClass().add("icon");
        resetIcon.fillProperty().bind(scheme.getOnSurface());
        Button resetButton = new Button(null, resetIcon);
        BorderPane.setAlignment(resetButton, Pos.BOTTOM_CENTER);
        resetButton.getStyleClass().add("reset-button");
        colorPane.setRight(resetButton);

        Color defaultColor;
        if (customColors.length > 0) {
            colorPicker.getCustomColors().setAll(customColors);
            defaultColor = customColors[0];
        } else {
            defaultColor = null;
        }
        resetButton.disableProperty().bind(property.isEqualTo(defaultColor));
        resetButton.setOnAction(event -> colorPicker.setValue(defaultColor));

        return colorPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox leftPane = new VBox(20);
        leftPane.setPrefWidth(280);
        BorderPane.setMargin(leftPane, new Insets(20, 20, 20, 10));
        root.setLeft(leftPane);
        {
            BorderPane settingsPane = new BorderPane();
            settingsPane.getStyleClass().add("settings-card");
            {
                StackPane titlePane = new StackPane();
                settingsPane.setTop(titlePane);
                {
                    Label label = new Label("Theme Settings");
                    label.setStyle("-fx-text-fill: -monet-on-surface; -fx-font-size: 15");
                    label.setPadding(new Insets(0, 0, 8, 0));
                    titlePane.getChildren().add(label);
                }

                VBox content = new VBox(8);
                settingsPane.setCenter(content);
                {
                    BorderPane darkModePane = new BorderPane();
                    {
                        Label label = new Label("Dark Mode");
                        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                        label.setStyle("-fx-text-fill: -monet-on-surface");
                        darkModePane.setLeft(label);

                        CheckBox checkBox = new CheckBox();
                        checkBox.selectedProperty().bindBidirectional(darkModeProperty);
                        darkModePane.setRight(checkBox);

                        checkBox.disableProperty().bind(platformProperty.isEqualTo(TargetPlatform.WATCH));
                    }

                    BorderPane backgroundChooserPane = new BorderPane();
                    {
                        Label label = new Label("Background Image");
                        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                        label.setStyle("-fx-text-fill: -monet-on-surface");
                        backgroundChooserPane.setLeft(label);

                        Button chooseButton = new Button("Choose");
                        chooseButton.setOnAction(event -> {
                            File file = backgroundFileChooser.showOpenDialog(primaryStage);
                            if (file != null) {
                                updateBackgroundImage(file.toPath());
                                backgroundFileChooser.setInitialDirectory(file.getParentFile());
                            }
                        });
                        backgroundChooserPane.setRight(chooseButton);
                    }

                    BorderPane primaryColorPane = createColorSettingsPane("Primary Color", primaryColorProperty,
                            DEFAULT_COLOR,
                            Color.web("#b33b15"),
                            Color.web("#63a002"),
                            Color.web("#769cdf"),
                            Color.web("#ffde3f"));
                    BorderPane secondaryColorPane = createColorSettingsPane("Secondary Color", secondaryColorProperty);
                    BorderPane tertiaryColorPane = createColorSettingsPane("Tertiary Color", tertiaryColorProperty);
                    BorderPane neutralColorPane = createColorSettingsPane("Neutral Color", neutralColorProperty);
                    BorderPane neutralVariantColorPane = createColorSettingsPane("Neutral Variant Color", neutralVariantColorProperty);
                    BorderPane errorColorPane = createColorSettingsPane("Error Color", errorColorProperty);

                    BorderPane colorStylePane = new BorderPane();
                    {
                        Label label = new Label("Color Style");
                        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                        label.setStyle("-fx-text-fill: -monet-on-surface");
                        colorStylePane.setLeft(label);

                        ComboBox<ColorStyle> comboBox = new ComboBox<>();
                        comboBox.getItems().setAll(ColorStyle.values());
                        comboBox.valueProperty().bindBidirectional(this.colorStyleProperty);
                        colorStylePane.setRight(comboBox);
                    }

                    BorderPane contrastPane = new BorderPane();
                    {
                        Label label = new Label("Contrast");
                        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                        label.setStyle("-fx-text-fill: -monet-on-surface");
                        contrastPane.setLeft(label);

                        Slider slider = new Slider(-1.0, 1.0, 0.0);
                        slider.getStyleClass().add("contrast-slider");
                        slider.setShowTickMarks(true);
                        slider.setShowTickLabels(true);
                        slider.setSnapToTicks(true);
                        slider.setMajorTickUnit(0.5);
                        slider.valueProperty().bindBidirectional(contrastProperty);
                        contrastPane.setRight(slider);
                    }

                    BorderPane platformPane = new BorderPane();
                    {
                        Label label = new Label("Platform");
                        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                        label.setStyle("-fx-text-fill: -monet-on-surface");
                        platformPane.setLeft(label);

                        ComboBox<TargetPlatform> comboBox = new ComboBox<>();
                        comboBox.getItems().addAll(TargetPlatform.values());
                        comboBox.valueProperty().bindBidirectional(platformProperty);
                        comboBox.setConverter(new StringConverter<TargetPlatform>() {
                            @Override
                            public String toString(TargetPlatform platform) {
                                String name = platform.name();
                                return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase(Locale.ROOT);
                            }

                            @Override
                            public TargetPlatform fromString(String string) {
                                return TargetPlatform.valueOf(string);
                            }
                        });
                        platformPane.setRight(comboBox);

                        comboBox.disableProperty().bind(Bindings.createObjectBinding(
                                () -> !SPEC_2025_STYLES.contains(colorStyleProperty.get()),
                                colorStyleProperty
                        ));
                    }

                    BorderPane specVersionPane = new BorderPane();
                    {
                        Label label = new Label("Spec Version");
                        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
                        label.setStyle("-fx-text-fill: -monet-on-surface");
                        specVersionPane.setLeft(label);

                        ComboBox<ColorSpecVersion> comboBox = new ComboBox<>();
                        comboBox.getItems().addAll(ColorSpecVersion.values());
                        comboBox.valueProperty().bindBidirectional(this.specVersionProperty);
                        comboBox.setConverter(new StringConverter<ColorSpecVersion>() {
                            @Override
                            public String toString(ColorSpecVersion object) {
                                return object.toString().substring("SPEC_".length());
                            }

                            @Override
                            public ColorSpecVersion fromString(String string) {
                                return ColorSpecVersion.valueOf("SPEC_" + string);
                            }
                        });
                        specVersionPane.setRight(comboBox);

                        comboBox.disableProperty().bind(Bindings.createObjectBinding(
                                () -> platformProperty.get() == TargetPlatform.WATCH || !SPEC_2025_STYLES.contains(colorStyleProperty.get()),
                                platformProperty, colorStyleProperty
                        ));
                    }

                    BorderPane exportPane = new BorderPane();
                    {
                        HBox buttonsBar = new HBox();
                        exportPane.setRight(buttonsBar);
                        {
                            Button exportButton = new Button("Export");
                            exportButton.getStyleClass().add("primary-button");
                            {
                                ContextMenu contextMenu = new ContextMenu();
                                exportButton.setOnAction(e -> contextMenu.show(exportButton, Side.BOTTOM, 0, 0));

                                for (ThemeExporter exporter : ThemeExporter.values()) {
                                    MenuItem item = new MenuItem(exporter.name);
                                    item.setOnAction(e -> {
                                        exportFileChooser.getExtensionFilters().setAll(exporter.extensionFilter);
                                        File file = exportFileChooser.showSaveDialog(primaryStage);
                                        if (file != null) {
                                            exportFileChooser.setInitialDirectory(file.getParentFile());

                                            if (!exporter.export(file.toPath(), scheme.get())) {
                                                Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to export theme.");
                                                alert.show();
                                            }
                                        }
                                    });
                                    contextMenu.getItems().add(item);
                                }
                            }

                            buttonsBar.getChildren().add(exportButton);
                        }
                    }

                    content.getChildren().setAll(darkModePane, backgroundChooserPane,
                            primaryColorPane, secondaryColorPane, tertiaryColorPane,
                            neutralColorPane, neutralVariantColorPane, errorColorPane,
                            colorStylePane, contrastPane, platformPane, specVersionPane,
                            exportPane);
                }
            }

            BorderPane aboutPane = new BorderPane();
            aboutPane.getStyleClass().add("settings-card");
            {
                StackPane titlePane = new StackPane();
                aboutPane.setTop(titlePane);
                {
                    Label label = new Label("About");
                    label.setStyle("-fx-text-fill: -monet-on-surface; -fx-font-size: 15");
                    label.setPadding(new Insets(0, 0, 8, 0));
                    titlePane.getChildren().add(label);
                }


                TextFlow content = new TextFlow();
                aboutPane.setCenter(content);

                // TODO
            }

            leftPane.getChildren().setAll(settingsPane);
        }

        GridPane gridPane = new GridPane();
        root.setCenter(gridPane);
        {
            int defaultCardWidth = 280;
            int defaultCardHeight = 55;
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
        }

        root.backgroundProperty().bind(Bindings.createObjectBinding(() -> {
            Image image = backgroundImageProperty.get();
            if (image != null) {
                return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)));
            } else {
                return new Background(new BackgroundFill(scheme.get().getSurface(), null, null));
            }
        }, backgroundImageProperty, scheme));

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

        Scene scene = new Scene(root);

        updateCss(scene);
        scheme.addListener((observable, oldValue, newValue) -> updateCss(scene));

        primaryStage.setTitle("MonetFX Theme Builder");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        root.requestFocus();
    }


    public static void main(String[] args) {
        Application.launch(MonetFXThemeBuilder.class, args);
    }
}
