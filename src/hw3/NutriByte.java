//Shiven Shashidhar; sshashid
package hw3;

import hw3.Product.ProductNutrient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author Shiven
 * The main class to run the Application
 */
public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers 

	/**Uncomment the following three lines if you want to try out the full-size data files */
		static final String PRODUCT_FILE = "data/Products.csv";
		static final String NUTRIENT_FILE = "data/Nutrients.csv";
		static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
//	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image 

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	ObjectBinding<String> personBinding = new ObjectBinding<String>() {
		{
			super.bind(view.genderComboBox.valueProperty(), view.ageTextField.textProperty(), view.weightTextField.textProperty(), view.heightTextField.textProperty(), view.physicalActivityComboBox.valueProperty());
		}
		@Override
		protected String computeValue() {
			// TODO Auto-generated method stub
			float ageFloat=0;
			float weightFloat=0;
			float heightFloat=0;
			TextField textField = view.ageTextField;
			if(view.genderComboBox.getSelectionModel().getSelectedIndex() >= 0) {
				try {
					textField.setStyle("-fx-text-inner-color: black;");
					ageFloat = Float.parseFloat(textField.getText().trim());

					textField = view.weightTextField;
					textField.setStyle("-fx-text-inner-color: black;");
					weightFloat = Float.parseFloat(textField.getText().trim());

					textField = view.heightTextField;
					textField.setStyle("-fx-text-inner-color: black;");
					heightFloat = Float.parseFloat(textField.getText().trim());

					return view.genderComboBox.getSelectionModel().getSelectedItem() + view.ageTextField.getText() + view.weightTextField.getText() + view.heightTextField.getText() + view.physicalActivityComboBox.getSelectionModel().getSelectedItem();
				}
				catch (NumberFormatException e) {
					textField.setStyle("-fx-text-inner-color: red;");
					return null;
				}
			}
			return null;
		}
	};


	ObjectBinding<Product> prodBinding = new ObjectBinding<Product>() {
		{
			super.bind(view.productsComboBox.valueProperty());
		}
		@Override
		protected Product computeValue() {
			// TODO Auto-generated method stub
			if(view.productsComboBox.getSelectionModel().getSelectedIndex() >= 0) {
				return view.productsComboBox.getSelectionModel().getSelectedItem();
			}
			return new Product();
		}
	};

	/*
	 * The starting point of the application
	 */
	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE );
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible
		setupBindings();
		stage.setTitle("NutriByte 2.0");
		stage.setScene(scene);

		personBinding.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if(NutriByte.view.genderComboBox.getSelectionModel().getSelectedIndex()>=0 && !NutriByte.view.ageTextField.getText().trim().isEmpty() && !NutriByte.view.weightTextField.getText().trim().isEmpty() && !NutriByte.view.heightTextField.getText().trim().isEmpty())
					controller.new RecommendNutrientsButtonHandler().handle(new ActionEvent());
			}
		});

		prodBinding.addListener(controller.new ProductsComboBoxListener());

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * defines various bindings
	 */
	void setupBindings() {
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());

		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());

		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);

		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuHandler());
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());

		view.dietProductNameColumn.setCellValueFactory(dietProductNameCallback);
		view.dietServingSizeColumn.setCellValueFactory(dietServingSizeCallback);
		view.dietServingUomColumn.setCellValueFactory(dietServingUomCallback);
		view.dietHouseholdSizeColumn.setCellValueFactory(dietHouseholdSizeCallback);
		view.dietHouseholdUomColumn.setCellValueFactory(dietHouseholdUomCallback);

		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());


	}
	/*
	 * Three Callbacks bound to three columns in recommendedNutrientsTableView
	 */
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			return new SimpleStringProperty(String.format("%.2f",arg0.getValue().getNutrientQuantity()));
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>(){

		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> param) {
			return new SimpleStringProperty(Model.nutrientsMap.get(param.getValue().getNutrientCode()).getNutrientName());
		}

	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>(){

		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> param) {
			return new SimpleStringProperty(String.format("%.2f", param.getValue().getNutrientQuantity()));
		}

	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>(){

		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> param) {
			return new SimpleStringProperty(Model.nutrientsMap.get(param.getValue().getNutrientCode()).getNutrientUom());
		}

	};

	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietProductNameCallback = new Callback<TableColumn.CellDataFeatures<Product,String>, ObservableValue<String>>() {

		@Override
		public ObservableValue<String> call(CellDataFeatures<Product, String> param) {
			// TODO Auto-generated method stub
			return new SimpleStringProperty(Model.productsMap.get(param.getValue().getNdbNumber()).getProductName());
		}

	};

	Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> dietServingSizeCallback = new Callback<TableColumn.CellDataFeatures<Product,Float>, ObservableValue<Float>>() {

		@Override
		public ObservableValue<Float> call(CellDataFeatures<Product, Float> param) {
			// TODO Auto-generated method stub
			return new SimpleFloatProperty(param.getValue().getServingSize()).asObject();
		}

	};

	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietServingUomCallback = new Callback<TableColumn.CellDataFeatures<Product,String>, ObservableValue<String>>() {

		@Override
		public ObservableValue<String> call(CellDataFeatures<Product, String> param) {
			// TODO Auto-generated method stub
			return new SimpleStringProperty(Model.productsMap.get(param.getValue().getNdbNumber()).getServingUom());
		}

	};

	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietHouseholdUomCallback = new Callback<TableColumn.CellDataFeatures<Product,String>, ObservableValue<String>>() {

		@Override
		public ObservableValue<String> call(CellDataFeatures<Product, String> param) {
			// TODO Auto-generated method stub
			return new SimpleStringProperty(Model.productsMap.get(param.getValue().getNdbNumber()).getHouseholdUom());
		}

	};

	Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> dietHouseholdSizeCallback = new Callback<TableColumn.CellDataFeatures<Product,Float>, ObservableValue<Float>>() {

		@Override
		public ObservableValue<Float> call(CellDataFeatures<Product, Float> param) {
			// TODO Auto-generated method stub
			return new SimpleFloatProperty(param.getValue().getHouseholdSize()).asObject();
		}
	};


}
