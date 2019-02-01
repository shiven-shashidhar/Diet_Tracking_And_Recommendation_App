//Shiven Shashidhar; sshashid
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Shiven
 *A Java bean with nutrientCode and nutrientQuantity as private properties,
 */
public class RecommendedNutrient {
	private StringProperty nutrientCode = new SimpleStringProperty();
	private FloatProperty nutrientQuantity = new SimpleFloatProperty();
	
	/*initializes nutrientCode to empty string*/
	RecommendedNutrient() {
		nutrientCode.set("");
	}
	
	/* initializes nutrientCode and nutrientQuantity */
	RecommendedNutrient(String nutrientCode, Float nutrientQuantity) {
		this.nutrientCode.set(nutrientCode);;
		this.nutrientQuantity.set(nutrientQuantity);;
	}

	public String getNutrientCode() {
		return nutrientCode.get();
	}

	public float getNutrientQuantity() {
		return nutrientQuantity.get();
	}

	public void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);;
	}

	public void setNutrientQuantity(Float nutrientQuantity) {
		this.nutrientQuantity.set(nutrientQuantity);
	}

	public StringProperty nutrientCodeProperty() {return nutrientCode;}

	public FloatProperty nutrientQuantityProperty() {return nutrientQuantity;}		

}
