//Shiven Shashidhar; sshashid
package hw3;

import java.util.Map;

import hw3.Product.ProductNutrient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * @author Shiven
 * An abstract class that captures the profile data of a person
 */
public abstract class Person {

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	NutriProfiler.AgeGroupEnum ageGroup;

	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductsList = FXCollections.observableArrayList();
	ObservableMap<String, RecommendedNutrient> dietNutrientsMap =  FXCollections.observableHashMap(); 

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();

	/*initializes age, weight, height, physicalActivityLevel, ingredientsToWatch, and ageGroup*/

	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;

		for(NutriProfiler.AgeGroupEnum ag: NutriProfiler.AgeGroupEnum.values()) {
			if(this.age <= ag.getAge()) {
				this.ageGroup = ag;
				break;
			}
		}

	}

	/*returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
	  Each value is calculated as follows:
	  For Protein, it multiples the constant with the person's weight.
	  For Carb and Fiber, it simply takes the constant from the 
	  nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	  For others, it multiples the constant with the person's weight and divides by 1000 */

	float[] calculateNutriRequirement() {
		float[] nutriRequirement = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];

		for (NutriProfiler.NutriEnum nutriEnum : NutriProfiler.NutriEnum.values()) {

			if (nutriEnum.equals(NutriProfiler.NutriEnum.PROTEIN)) {

				nutriRequirement[nutriEnum.getNutriIndex()] = weight *

						nutriConstantsTable[nutriEnum.getNutriIndex()][ageGroup.getAgeGroupIndex()];
			}

			else if(nutriEnum.equals(NutriProfiler.NutriEnum.CARBOHYDRATE) || nutriEnum.equals(NutriProfiler.NutriEnum.FIBER) ) {
				nutriRequirement[nutriEnum.getNutriIndex()] = nutriConstantsTable[nutriEnum.getNutriIndex()][ageGroup.getAgeGroupIndex()]; 
			}

			else{
				nutriRequirement[nutriEnum.getNutriIndex()] = (weight *
						nutriConstantsTable[nutriEnum.getNutriIndex()][ageGroup.getAgeGroupIndex()])/1000;
			}
		}

		return nutriRequirement;
	}

	void populateDietNutrientMap() {
		ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();
		RecommendedNutrient rn = null;
		if(!dietNutrientsMap.isEmpty()) {
			for(String nutriCode: dietNutrientsMap.keySet()) {
				dietNutrientsMap.get(nutriCode).setNutrientQuantity(0.00f);
			}
		}

		for(Product dietProduct: NutriByte.person.dietProductsList) {
			productNutrients = dietProduct.getProductNutrients();
			for(Map.Entry<String, ProductNutrient> aProdNutrient: productNutrients.entrySet()) {
				if(!dietNutrientsMap.containsKey(aProdNutrient.getKey())) {
					rn = new RecommendedNutrient(aProdNutrient.getKey(), dietProduct.getServingSize() * aProdNutrient.getValue().getNutrientQuantity()/100);
					dietNutrientsMap.put(aProdNutrient.getKey(), rn);
				}
				else {
					if(dietNutrientsMap.get(aProdNutrient.getKey()) != null) {
						rn = dietNutrientsMap.get(aProdNutrient.getKey());
						rn.setNutrientQuantity(rn.getNutrientQuantity() + (dietProduct.getServingSize() * aProdNutrient.getValue().getNutrientQuantity()/100));
					}
				}
			}
		}
	}
}
