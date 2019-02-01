//Shiven Shashidhar; sshashid
package hw3;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Map;

import hw3.NutriProfiler.NutriEnum;
import hw3.Product.ProductNutrient;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * @author Shiven
 * The class holds all event handlers (the 'C' in MVC)
 */
public class Controller {

	/*
	 * Event handling for recommend nutrients button
	 */
	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Person aPerson = null;
			float ageFloat;
			TextField textField;
			if(NutriByte.person != null) {
				NutriByte.person.recommendedNutrientsList.clear();
			}
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();
			try {
				if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedIndex()>=0) {

					String age;
					if(NutriByte.view.ageTextField.getText().trim().isEmpty()) {
						throw new InvalidProfileException("Missing age information");
					}
					else {
						textField = NutriByte.view.ageTextField;
						if(textField.getStyle().equals("-fx-text-inner-color: red;")){
							throw new InvalidProfileException("Incorrect age input. Must be a number");
						}
						age = NutriByte.view.ageTextField.getText().trim();
						if(Float.parseFloat(age)<0) {
							throw new InvalidProfileException("Age must be a positive number");
						}
					}

					String weight;
					if(NutriByte.view.weightTextField.getText().trim().isEmpty()) {
						throw new InvalidProfileException("Missing weight information");
					}
					else {
						textField = NutriByte.view.weightTextField;
						if(textField.getStyle().equals("-fx-text-inner-color: red;")){
							throw new InvalidProfileException("Incorrect weight input. Must be a number");
						}
						weight = NutriByte.view.weightTextField.getText().trim();
						if(Float.parseFloat(weight)<0) {
							throw new InvalidProfileException("Weight must be a positive number");
						}
					}
					String height;

					if(NutriByte.view.heightTextField.getText().trim().isEmpty()) {
						throw new InvalidProfileException("Missing height information");
					}
					else {
						textField = NutriByte.view.heightTextField;
						if(textField.getStyle().equals("-fx-text-inner-color: red;")){
							throw new InvalidProfileException("Incorrect height input. Must be a number");
						}
						height = NutriByte.view.heightTextField.getText().trim();
						if(Float.parseFloat(height)<0) {
							throw new InvalidProfileException("Height must be a positive number");
						}
					}

					String gender = NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem();
					float physicalActivityLevel = NutriProfiler.PhysicalActivityEnum.SEDENTARY.getPhysicalActivityLevel();
					if(NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedIndex()>=0) {

						String physicalActivityLevelName = NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().trim();
						for(NutriProfiler.PhysicalActivityEnum pa: NutriProfiler.PhysicalActivityEnum.values()) {
							if(physicalActivityLevelName == pa.getName()) {
								physicalActivityLevel = pa.getPhysicalActivityLevel();
							}
						}
					}
					String ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getText().trim();
					if(gender.equals("Female")) {
						aPerson = new Female(Float.parseFloat(age), Float.parseFloat(weight) , Float.parseFloat(height), physicalActivityLevel, ingredientsToWatch );
					}
					else if(gender.equals("Male")) {
						aPerson = new Male(Float.parseFloat(age), Float.parseFloat(weight) , Float.parseFloat(height), physicalActivityLevel, ingredientsToWatch );
					}

					if(aPerson !=null) {
						NutriByte.person = aPerson;
						NutriProfiler.createNutriProfile(aPerson);
						NutriByte.view.recommendedNutrientsTableView.setItems(aPerson.recommendedNutrientsList);
						NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
					}
				}
				else {
					throw new InvalidProfileException("Missing gender information");
				}
			}
			catch(InvalidProfileException e1) {
				return;
			}
			catch(Exception e3) {
				return;
			}
		}
	}

	/*
	 * Event handling for open menu item for opening a profile file
	 */
	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			boolean flgProfile = false;
			fileChooser.setTitle("Select Profile");
			fileChooser.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
			File selectedFile = fileChooser.showOpenDialog(new Stage());
			Person ps = null;
			ObservableList<RecommendedNutrient> recmdNutrList = FXCollections.observableArrayList();
			if(selectedFile != null) {
				flgProfile = NutriByte.model.readProfiles(selectedFile.getAbsolutePath());
				if(flgProfile) {

					if(NutriByte.person instanceof Male) {
						NutriByte.view.genderComboBox.getSelectionModel().select(1);
					}
					else if(NutriByte.person instanceof Female) {
						NutriByte.view.genderComboBox.getSelectionModel().select(0);
					}

					ps = NutriByte.person;

					NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);
					NutriByte.view.ageTextField.setText(Float.toString(NutriByte.person.age));
					NutriByte.view.weightTextField.setText(Float.toString(NutriByte.person.weight));
					NutriByte.view.heightTextField.setText(Float.toString(NutriByte.person.height));

					for(NutriProfiler.PhysicalActivityEnum pa: NutriProfiler.PhysicalActivityEnum.values()) {
						if(ps.physicalActivityLevel == pa.getPhysicalActivityLevel() ) {
							NutriByte.view.physicalActivityComboBox.getSelectionModel().select(pa.getName());
						}
					}

					recmdNutrList = NutriByte.person.recommendedNutrientsList;

					NutriByte.person = ps;
					ps.recommendedNutrientsList = recmdNutrList;

					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

					if(!(NutriByte.person.dietProductsList.isEmpty() || NutriByte.person.dietProductsList == null)) {
						NutriByte.view.productsComboBox.setItems(NutriByte.person.dietProductsList);
						NutriByte.view.productsComboBox.getSelectionModel().select(0);
						NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients().values()));
						NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
						NutriByte.view.nutriChart.updateChart();
						NutriByte.view.searchResultSizeLabel.setText(NutriByte.person.dietProductsList.size() + " product(s) found");
						NutriByte.view.servingSizeLabel.setText(String.format("%.2f", Model.productsMap.get(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber()).getServingSize()) + " " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
						NutriByte.view.householdSizeLabel.setText(String.format("%.2f", Model.productsMap.get(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber()).getHouseholdSize()) + " " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
						NutriByte.view.dietServingUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
						NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
					}
					NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
					NutriProfiler.createNutriProfile(NutriByte.person);				
				}
				else {
					NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
					NutriByte.view.ingredientsToWatchTextArea.setText("");
					NutriByte.view.ageTextField.setText("");
					NutriByte.view.weightTextField.setText("");
					NutriByte.view.heightTextField.setText("");
					NutriByte.view.physicalActivityComboBox.getSelectionModel().clearSelection();
					NutriByte.view.recommendedNutrientsTableView.getItems().clear();
					NutriByte.view.productNutrientsTableView.getItems().clear();
					NutriByte.view.productsComboBox.getItems().clear();
					NutriByte.view.productIngredientsTextArea.clear();
					NutriByte.view.servingSizeLabel.setText("");
					NutriByte.view.householdSizeLabel.setText("");
					NutriByte.view.servingUom.setText("");
					NutriByte.view.householdServingUom.setText("");
					NutriByte.view.searchResultSizeLabel.setText("");
					NutriByte.view.dietHouseholdUomLabel.setText("");
					NutriByte.view.dietServingUomLabel.setText("");
					NutriByte.view.dietProductsTableView.getItems().clear();
					NutriByte.view.nutriChart.clearChart();
					NutriByte.view.productSearchTextField.clear();
					NutriByte.view.nutrientSearchTextField.clear();
					NutriByte.view.ingredientSearchTextField.clear();
					NutriByte.view.dietServingSizeTextField.clear();
					NutriByte.view.dietHouseholdSizeTextField.clear();
				}
			}
		}
	}

	/*
	 * Event handling for new menu item
	 */

	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productsComboBox.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.servingSizeLabel.setText("");
			NutriByte.view.householdSizeLabel.setText("");
			NutriByte.view.servingUom.setText("");
			NutriByte.view.householdServingUom.setText("");
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietProductsTableView.getItems().clear();
			NutriByte.view.nutriChart.clearChart();
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.dietServingSizeTextField.clear();
			NutriByte.view.dietHouseholdSizeTextField.clear();
		}
	}

	/*
	 * Event handling for about menu item
	 */
	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText("Version 2.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}

	class CloseMenuHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
			Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
			NutriByte.view.root.setBackground(b);
			NutriByte.view.root.requestFocus();
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();
			NutriByte.view.productNutrientsTableView.getItems().clear();
			NutriByte.view.productsComboBox.getItems().clear();
			NutriByte.view.productIngredientsTextArea.clear();
			NutriByte.view.servingSizeLabel.setText("");
			NutriByte.view.householdSizeLabel.setText("");
			NutriByte.view.servingUom.setText("");
			NutriByte.view.householdServingUom.setText("");
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietProductsTableView.getItems().clear();
			NutriByte.view.nutriChart.clearChart();
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.dietServingSizeTextField.clear();
			NutriByte.view.dietHouseholdSizeTextField.clear();
		}

	}

	class SaveMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			float ageFloat;
			TextField textField;
			try {
				if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedIndex()>=0) {

					String age;
					if(NutriByte.view.ageTextField.getText().trim().isEmpty()) {
						throw new InvalidProfileException("Missing age information");
					}
					else {
						textField = NutriByte.view.ageTextField;
						if(textField.getStyle().equals("-fx-text-inner-color: red;")){
							throw new InvalidProfileException("Incorrect age input. Must be a number");
						}
						age = NutriByte.view.ageTextField.getText().trim();
						if(Float.parseFloat(age)<0) {
							throw new InvalidProfileException("Age must be a positive number");
						}
					}

					String weight;
					if(NutriByte.view.weightTextField.getText().trim().isEmpty()) {
						throw new InvalidProfileException("Missing weight information");
					}
					else {
						textField = NutriByte.view.weightTextField;
						if(textField.getStyle().equals("-fx-text-inner-color: red;")){
							throw new InvalidProfileException("Incorrect weight input. Must be a number");
						}
						weight = NutriByte.view.weightTextField.getText().trim();
						if(Float.parseFloat(weight)<0) {
							throw new InvalidProfileException("Weight must be a positive number");
						}
					}
					String height;

					if(NutriByte.view.heightTextField.getText().trim().isEmpty()) {
						throw new InvalidProfileException("Missing height information");
					}
					else {
						textField = NutriByte.view.heightTextField;
						if(textField.getStyle().equals("-fx-text-inner-color: red;")){
							throw new InvalidProfileException("Incorrect height input. Must be a number");
						}
						height = NutriByte.view.heightTextField.getText().trim();
						if(Float.parseFloat(height)<0) {
							throw new InvalidProfileException("Height must be a positive number");
						}
					}

					FileChooser fileChooser = new FileChooser();
					boolean flgProfile = false;
					fileChooser.setTitle("Select file");
					fileChooser.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
					FileChooser.ExtensionFilter extFilter = 
							new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
					fileChooser.getExtensionFilters().add(extFilter);
					File savedFile = fileChooser.showSaveDialog(new Stage());

					if(savedFile != null) {
						NutriByte.model.writeProfile(savedFile.getAbsolutePath());
					}

				}
				else {
					throw new InvalidProfileException("Missing gender information");
				}

			}
			catch(InvalidProfileException ipe) {
				return;
			}

		}
	}

		class SearchButtonHandler implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				String productSearch = NutriByte.view.productSearchTextField.getText();
				String nutrientSearch = NutriByte.view.nutrientSearchTextField.getText();
				String ingredientSearch = NutriByte.view.ingredientSearchTextField.getText();
				String nutriCode = null;
				NutriByte.model.searchResultsList.clear();

				if(productSearch.isEmpty() && nutrientSearch.isEmpty() && ingredientSearch.isEmpty()) {
					for(Map.Entry<String, Product> prod: NutriByte.model.productsMap.entrySet()) {
						NutriByte.model.searchResultsList.add(prod.getValue());
					}
				}
				else {
					if(!nutrientSearch.isEmpty()) {
						for(Map.Entry<String, Nutrient> nutriRecord: NutriByte.model.nutrientsMap.entrySet()) {
							if(nutriRecord.getValue().getNutrientName().trim().toUpperCase().contains(nutrientSearch.trim().toUpperCase())) {
								nutriCode = nutriRecord.getValue().getNutrientCode();
								break;
							}
						}
					}

					for(Map.Entry<String, Product> prod: NutriByte.model.productsMap.entrySet()) {

						if(!productSearch.trim().isEmpty()) {
							if(prod.getValue().getProductName().trim().toLowerCase().contains(productSearch.trim().toLowerCase())) {
								if(nutrientSearch.isEmpty() && ingredientSearch.isEmpty()) {
									NutriByte.model.searchResultsList.add(prod.getValue());
								}
								else if(nutrientSearch.isEmpty()) {
									if(prod.getValue().getIngredients().trim().toLowerCase().contains(ingredientSearch.trim().toLowerCase())) {
										NutriByte.model.searchResultsList.add(prod.getValue());
									}
								}
								else if(ingredientSearch.isEmpty()) {
									ObservableMap<String, ProductNutrient> prodNutr = prod.getValue().getProductNutrients();

									if(prodNutr.containsKey(nutriCode)) {
										if(prodNutr.get(nutriCode).getNutrientQuantity() > 0){
											NutriByte.model.searchResultsList.add(prod.getValue());
										}
									}
								}
								else {
									if(prod.getValue().getIngredients().trim().toLowerCase().contains(ingredientSearch.trim().toLowerCase())) {
										ObservableMap<String, ProductNutrient> prodNutr2 = prod.getValue().getProductNutrients();

										if(prodNutr2.containsKey(nutriCode)) {
											if(prodNutr2.get(nutriCode).getNutrientQuantity() > 0){
												NutriByte.model.searchResultsList.add(prod.getValue());
											}
										}

									}

								}
							}
						}
						else if(!nutrientSearch.trim().isEmpty()) {
							ObservableMap<String, ProductNutrient> prodNutr3 = prod.getValue().getProductNutrients();
							if(ingredientSearch.isEmpty()) {
								if(prodNutr3.containsKey(nutriCode)) {
									if(prodNutr3.get(nutriCode).getNutrientQuantity() > 0){
										NutriByte.model.searchResultsList.add(prod.getValue());
									}
								}
							}
							else {
								if(prod.getValue().getIngredients().trim().toLowerCase().contains(ingredientSearch.trim().toLowerCase())) {
									if(prodNutr3.containsKey(nutriCode)) {
										if(prodNutr3.get(nutriCode).getNutrientQuantity() > 0){
											NutriByte.model.searchResultsList.add(prod.getValue());
										}
									}
								}
							}
						}
						else if(!ingredientSearch.trim().isEmpty()) {
							if(prod.getValue().getIngredients().trim().toLowerCase().contains(ingredientSearch.trim().toLowerCase())) {
								NutriByte.model.searchResultsList.add(prod.getValue());
							}
						}


					}
				}

				NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
				NutriByte.view.productsComboBox.getSelectionModel().select(0);
				NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultsList.size() + " product(s) found");
			}

		}

		class ClearButtonHandler implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				NutriByte.view.productSearchTextField.clear();
				NutriByte.view.nutrientSearchTextField.clear();
				NutriByte.view.ingredientSearchTextField.clear();

				NutriByte.view.productIngredientsTextArea.clear();

				if(NutriByte.view.productsComboBox.getItems() != null) {
					NutriByte.view.productsComboBox.getItems().clear();
				}

				NutriByte.view.productNutrientsTableView.getItems().clear();

				//Should i also clear the products count label?
				NutriByte.view.searchResultSizeLabel.setText("");
				NutriByte.view.servingSizeLabel.setText("0.00");
				NutriByte.view.householdSizeLabel.setText("0.00");
				NutriByte.view.dietServingUomLabel.setText("");
				NutriByte.view.dietHouseholdUomLabel.setText("");
			}

		}

		class ProductsComboBoxListener implements ChangeListener<Product>{

			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				// TODO Auto-generated method stub
				if (newValue == null) {
					NutriByte.view.productIngredientsTextArea.setText("");
					NutriByte.view.productIngredientsTextArea.setPromptText("Product ingredients");
				}
				if (newValue != null) {
					if(NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex()>=0 ) { 
						NutriByte.view.productIngredientsTextArea.setText("Product ingredients: " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getIngredients());
						//NutriByte.view.servingSizeLabel.setText(String.format("%.2f", NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingSize()) + " " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
						//NutriByte.view.householdSizeLabel.setText(String.format("%.2f",NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdSize()) + " " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
						NutriByte.view.servingSizeLabel.setText(String.format("%.2f", Model.productsMap.get(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber()).getServingSize()) + " " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
						NutriByte.view.householdSizeLabel.setText(String.format("%.2f", Model.productsMap.get(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getNdbNumber()).getHouseholdSize()) + " " + NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
						NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getProductNutrients().values()));
						NutriByte.view.dietServingUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getServingUom());
						NutriByte.view.dietHouseholdUomLabel.setText(NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem().getHouseholdUom());
					}
				}
			}

		}

		class AddDietButtonHandler implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex() >= 0) {
					Product prod1 = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
					Product prod2 = new Product(prod1.getNdbNumber(), prod1.getProductName(), prod1.getManufacturer(), prod1.getIngredients());
					prod2.setHouseholdUom(prod1.getHouseholdUom());
					prod2.setServingUom(prod1.getServingUom());
					prod2.setProductNutrients(prod1.getProductNutrients());

					if(NutriByte.view.dietServingSizeTextField.getText().isEmpty() && NutriByte.view.dietHouseholdSizeTextField.getText().isEmpty()) {
						prod2.setHouseholdSize(Model.productsMap.get(prod1.getNdbNumber()).getHouseholdSize());
						prod2.setServingSize(Model.productsMap.get(prod1.getNdbNumber()).getServingSize());
					}
					else if(NutriByte.view.dietHouseholdSizeTextField.getText().isEmpty()) {

						prod2.setServingSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim()));
						prod2.setHouseholdSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim()) * Model.productsMap.get(prod1.getNdbNumber()).getHouseholdSize()/Model.productsMap.get(prod1.getNdbNumber()).getServingSize());
					}
					else if(NutriByte.view.dietServingSizeTextField.getText().isEmpty()) {

						prod2.setHouseholdSize(Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText().trim()));
						prod2.setServingSize(Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText().trim()) * Model.productsMap.get(prod1.getNdbNumber()).getServingSize()/Model.productsMap.get(prod1.getNdbNumber()).getHouseholdSize());
					}
					else {

						prod2.setServingSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim()));
						prod2.setHouseholdSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim()) * Model.productsMap.get(prod1.getNdbNumber()).getHouseholdSize()/Model.productsMap.get(prod1.getNdbNumber()).getServingSize());
					}

					NutriByte.person.dietProductsList.add(prod2);
					NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
					NutriByte.person.populateDietNutrientMap();
					NutriByte.view.nutriChart.updateChart();
				}
			}

		}

		class RemoveDietButtonHandler implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent arg0) {
				if(NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedIndex() >= 0) {
					Product selectedProd = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem();
					NutriByte.person.dietProductsList.remove(selectedProd);
					NutriByte.person.populateDietNutrientMap();
					NutriByte.view.nutriChart.updateChart();
				}
			}

		}
	}	

