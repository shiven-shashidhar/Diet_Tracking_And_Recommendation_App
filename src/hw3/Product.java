//Shiven Shashidhar; sshashid
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * @author Shiven
 * A Java bean for capturing product details.
 */
public class Product {
	private StringProperty ndbNumber = new SimpleStringProperty();
	private StringProperty productName = new SimpleStringProperty();
	private StringProperty manufacturer = new SimpleStringProperty();
	private StringProperty ingredients = new SimpleStringProperty();
	private FloatProperty servingSize = new SimpleFloatProperty();
	private StringProperty servingUom = new SimpleStringProperty();
	private FloatProperty householdSize = new SimpleFloatProperty();
	private StringProperty householdUom = new SimpleStringProperty();
	/*Hashmap to store nutrients in the product. Key is nutrientCode and the value is of type ProductNutrient */
	private ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();
	
	/*initializes ndbNumber, productName, manufacturer, and ingredients to empty strings */
	Product() {
		ndbNumber.set("");
		productName.set("");
		manufacturer.set("");
		ingredients.set("");
		servingUom.set("");
		householdUom.set("");
	}
	
	/* initializes ndbNumber, productName, manufacturer, and ingredients to respective args */
	Product(String ndbNumber, String productName, String manufacturer,
			String ingredients) {
		this.ndbNumber.set(ndbNumber);
		this.productName.set(productName);
		this.manufacturer.set(manufacturer);
		this.ingredients.set(ingredients);
	}
	
	

	public String getNdbNumber() {
		return ndbNumber.get();
	}

	public String getProductName() {
		return productName.get();
	}

	public String getManufacturer() {
		return manufacturer.get();
	}

	public String getIngredients() {
		return ingredients.get();
	}

	public float getServingSize() {
		return servingSize.get();
	}

	public String getServingUom() {
		return servingUom.get();
	}

	public float getHouseholdSize() {
		return householdSize.get();
	}

	public String getHouseholdUom() {
		return householdUom.get();
	}

	public void setNdbNumber(String ndbNumber) {
		this.ndbNumber.set(ndbNumber);
	}

	public void setProductName(String productName) {
		this.productName.set(productName);
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer.set(manufacturer);
	}

	public void setIngredients(String ingredients) {
		this.ingredients.set(ingredients);
	}

	public void setServingSize(float servingSize) {
		this.servingSize.set(servingSize);
	}

	public void setServingUom(String servingUom) {
		this.servingUom.set(servingUom);
	}

	public void setHouseholdSize(float householdSize) {
		this.householdSize.set(householdSize);
	}

	public void setHouseholdUom(String householdUom) {
		this.householdUom.set(householdUom);
	}


	public ObservableMap<String, ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	public void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	public StringProperty nbdNumberProperty() {return ndbNumber;}
	public StringProperty productNameProperty() {return productName;}
	public StringProperty manufacturerProperty() {return manufacturer;}
	public StringProperty ingredientsProperty() {return ingredients;}
	public FloatProperty servingSizeProperty() {return servingSize;}
	public StringProperty servingUomProperty() {return servingUom;}
	public FloatProperty householdSizeProperty() {return householdSize;}
	public StringProperty householdUomProperty() {return householdUom;}
	public ObservableMap<String, ProductNutrient> productNutrientsProperty() {return productNutrients;}

	/**
	 * A Java bean defined as inner class. It has nutrientCode and nutrientQuantity as its two properties.
	 */

	public class ProductNutrient{
		
		private StringProperty nutrientCode = new SimpleStringProperty();
		private FloatProperty nutrientQuantity = new SimpleFloatProperty();
		
		/*initializes nutrientCode to empty string */
		ProductNutrient() {
			nutrientCode.set("");
		}
		
		/*initializes nutrientCode and nutrientQuantity.*/
		ProductNutrient(String nutrientCode, float nutrientQuantity) {
			this.nutrientCode.set(nutrientCode);
			this.nutrientQuantity.set(nutrientQuantity);
		}

		public String getNutrientCode() {
			return nutrientCode.get();
		}

		public float getNutrientQuantity() {
			return nutrientQuantity.get();
		}

		public void setNutrientCode(String nutrientCode) {
			this.nutrientCode.set(nutrientCode);
		}

		public void setNutrientQuantity(float nutrientQuantity) {
			this.nutrientQuantity.set(nutrientQuantity);
		}

		public StringProperty nutrientCodeProperty() {return nutrientCode;}
		public FloatProperty nutrientQuantityProperty() {return nutrientQuantity;}


	}

	@Override
	public String toString() {
		return this.getProductName() + " by " + this.getManufacturer();
	}

}
