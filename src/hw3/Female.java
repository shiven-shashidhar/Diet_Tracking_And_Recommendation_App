//Shiven Shashidhar; sshashid
package hw3;

/**
 * @author Shiven
 * This class captures the profile data and energy requirements of a Female
 */
public class Female extends Person {

	float[][] nutriConstantsTableFemale = new float[][]{
		//AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE 
		{1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.71f, 0.8f, 0.8f, 0.8f}, //0: Protein constants
		{60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //1: Carbohydrate
		{19, 19, 19, 19, 25, 26, 26, 25, 25, 21},  //2: Fiber constants
		{36, 36, 32, 21, 16, 15, 14, 14, 14, 14}, 	//3: Histidine
		{88, 88, 43, 28, 22, 21, 19, 19, 19, 19}, 	//4: isoleucine
		{156, 156, 93, 63, 49, 47, 44 , 42, 42, 42},//5: leucine
		{107, 107, 89, 58, 46, 43, 40, 38, 38, 38}, //6: lysine
		{59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//7: methionine
		{59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//8: cysteine
		{135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //9: phenylalanine
		{135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //10: phenylalanine
		{73, 73, 49, 32, 24, 22, 21, 20, 20, 20}, 	//11: threonine
		{28, 28, 13, 8, 6, 6, 5, 5, 5, 5}, 			//12: tryptophan
		{87, 87, 58, 37, 28, 27, 24, 24, 24, 24	}  	//13: valine
	};

	/*
	 * invokes super’s constructor, and also invokes initializeNutriConstantsTable() method
	 */
	Female(float age, float weight, float height, float physicalActivityLevel, String ingredientsToAvoid) {
		super(age, weight, height, physicalActivityLevel, ingredientsToAvoid);
		initializeNutriConstantsTable();
	}
	
	/*
	 * calculates and returns energy requirement for the female profile based on various profile attributes
	 * @see hw2.Person#calculateEnergyRequirement()
	 */
	@Override
	float calculateEnergyRequirement() {
		int X=0;
		if(ageGroup.getAgeGroupIndex() >= 0 && ageGroup.getAgeGroupIndex() <=3) {
			if(ageGroup.getAgeGroupIndex() == 0) {X=-75;}
			else if(ageGroup.getAgeGroupIndex() == 1) {X=44;}
			else if(ageGroup.getAgeGroupIndex() == 2) {X=78;}
			else if(ageGroup.getAgeGroupIndex() == 3) {X=80;}
			return (89 * super.weight - X);
		}
		else if(ageGroup.getAgeGroupIndex() >= 4 && ageGroup.getAgeGroupIndex() <=6) {
			float A=135.3f, B=30.8f, C=10, D=934;
			return (A - (B * age) + physicalActivityLevel * (C * weight + D * height / 100) + 20);
		}
		else {
			float A=354, B=6.91f, C=9.36f, D=726;
			return (A - (B * age) + physicalActivityLevel * (C * weight + D * height / 100));
		}
	}
	
	/*
	 * Initializes nutritional constants table
	 * @see hw2.Person#initializeNutriConstantsTable()
	 */
	@Override
	void initializeNutriConstantsTable() {
		nutriConstantsTable = nutriConstantsTableFemale;
	}
}
