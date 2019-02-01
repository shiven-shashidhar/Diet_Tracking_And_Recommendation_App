//Shiven Shashidhar; sshashid
package hw3;

/**
 * @author Shiven
 *This class captures the profile data and energy requirements of a male
 */
public class Male extends Person{
	float[][] nutriConstantsTableMale = new float[][]{
		//AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE 
		{1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.73f, 0.8f, 0.8f, 0.8f}, //Protein
		{60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //Carbohydrate
		{19, 19, 19, 19, 25, 31, 38, 38, 38, 30},       //Fiber 
		{36, 36, 32, 21, 16, 17, 15, 14, 14, 14	},  //Histidine
		{88, 88, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//isoleucine
		{156, 156, 93, 63, 49, 49, 47, 42, 42, 42},//leucine
		{107, 107, 89, 58, 46, 46, 43, 38, 38, 38 },//lysine
		{59, 59, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//methionine 
		{59, 59, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//cysteine
		{135, 135, 84, 54, 41, 41, 38, 33, 33, 33 },//phenylalanine 
		{135, 135, 84, 54, 41, 41, 38, 33, 33, 33 },//tyrosine
		{73, 73, 49, 32, 24, 24, 22, 20, 20, 20}, 	//threonine
		{28, 28, 13, 8, 6, 6, 6, 5, 5, 5}, 			//tryptophan
		{87, 87, 58, 37, 28, 28, 27, 24, 24, 24}  	//valine
	};
	
	/*
	 * invokes super’s constructor, and also invokes initializeNutriConstantsTable() method
	 */
	Male(float age, float weight, float height, float physicalActivityLevel, String ingredientsToAvoid) {
		super(age, weight, height, physicalActivityLevel, ingredientsToAvoid);
		initializeNutriConstantsTable();
	}

	/*
	 * calculates and returns energy requirement for the male profile based on various profile attributes
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
			float A=88.5f, B=61.9f, C=26.7f, D=903;
			return (A - (B * age) + physicalActivityLevel * (C * weight + D * height / 100) + 20);
		}
		else {
			float A=662, B=9.53f, C=15.91f, D=539.6f;
			return (A - (B * age) + physicalActivityLevel * (C * weight + D * height / 100));
		}
	}
	
	/*
	 * Initializes nutritional constants table
	 * @see hw2.Person#initializeNutriConstantsTable()
	 */
	@Override
	void initializeNutriConstantsTable() {
		nutriConstantsTable = nutriConstantsTableMale;
	}
}
