//Shiven Shashidhar; sshashid
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Shiven
 * A Java bean capturing nutrient code, nutrient name and nutrient Unit of Measurement.
 */
public class Nutrient {
	private StringProperty nutrientCode = new SimpleStringProperty();
	private StringProperty nutrientName = new SimpleStringProperty();
	private StringProperty nutrientUom = new SimpleStringProperty();
	
	/*initializes all string properties to empty strings*/
	Nutrient() {
		nutrientCode.set("");
		nutrientName.set("");
		nutrientUom.set("");
	}
	
	/*initializes nutrientCode, nutrientName, and nutrientUom properties.*/
	Nutrient(String nutrientCode, String nutrientName, String nutrientUom) {
		this.nutrientCode.set(nutrientCode);
		this.nutrientName.set(nutrientName);
		this.nutrientUom.set(nutrientUom);
	}

	public String getNutrientCode() {
		return nutrientCode.get();
	}

	public String getNutrientName() {
		return nutrientName.get();
	}

	public String getNutrientUom() {
		return nutrientUom.get();
	}

	public void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);;
	}

	public void setNutrientName(String nutrientName) {
		this.nutrientName.set(nutrientName);;
	}

	public void setNutrientUom(String nutrientUom) {
		this.nutrientUom.set(nutrientUom);;
	}

	public StringProperty nutrientCodeProperty() {return nutrientCode;}

	public StringProperty nutrientNameProperty() {return nutrientName;}

	public StringProperty nutrientUomProperty() {return nutrientUom;}

}
