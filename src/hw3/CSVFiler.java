//Shiven Shashidhar; sshashid
package hw3;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import hw2.NutriProfiler.PhysicalActivityEnum;
import javafx.collections.ObservableList;

/**
 * @author Shiven
 * DataFiler’s child class
 */
public class CSVFiler extends DataFiler {

	/*
	 * Uses file data to create a Male or Female object and assigns it to NutriByte.person.
	 * Returns true if file read successfully
	 * @see hw2.DataFiler#readFile(java.lang.String)
	 */
	@Override
	public boolean readFile(String filename) {
		CSVFormat csvFormat = CSVFormat.DEFAULT;
		String ingredientsToWatch = null;
		StringBuilder sb = new StringBuilder();
		Product p1 = null, p2 = null;
		String ndbNum;

		if(validatePersonData(filename) && validateProductData(filename)) {

			if(!NutriByte.person.dietProductsList.isEmpty()) {
				NutriByte.person.populateDietNutrientMap();
			}

			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void writeFile(String filename) {
		try (
				BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
				){

			String gender = null;
			if(NutriByte.person instanceof Male) {
				gender = "Male";
			}
			else if(NutriByte.person instanceof Female) {
				gender = "Female";
			}

			bw.write(String.format("%s,%s,%s,%s,%s", gender, Float.toString(NutriByte.person.age), Float.toString(NutriByte.person.weight), Float.toString(NutriByte.person.height), Float.toString(NutriByte.person.physicalActivityLevel)));

			String[] ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getText().trim().split(",");
			for(int i=0;i<ingredientsToWatch.length; i++) {
				bw.write(",");
				bw.write(ingredientsToWatch[i].trim());
			}

			bw.newLine();

			for(Product prod: NutriByte.person.dietProductsList) {
				bw.write(prod.getNdbNumber().trim());
				bw.write(",");
				bw.write(Float.toString(prod.getServingSize()));
				bw.write(",");
				bw.write(Float.toString(prod.getHouseholdSize()));
				bw.newLine();
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	boolean validatePersonData(String filename) {
		CSVFormat csvFormat = CSVFormat.DEFAULT;
		String ingredientsToWatch = null;
		StringBuilder sb = new StringBuilder();
		Float temp;
		String cellValue = null;
		int index=0;
		boolean flgPhysical = false;

		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				if(csvRecord.getRecordNumber() == 1) {

					if(!(csvRecord.get(0).equals("Female") || csvRecord.get(0).equals("Male"))){
						throw new InvalidProfileException("The profile must have gender: Female or Male as first word");
					}

					index = 1;
					cellValue = csvRecord.get(1);
					temp = Float.parseFloat(csvRecord.get(1));

					index = 2;
					cellValue = csvRecord.get(2);
					temp = Float.parseFloat(csvRecord.get(2));

					index = 3;
					cellValue = csvRecord.get(3);
					temp = Float.parseFloat(csvRecord.get(3));

					index = 4;
					cellValue = csvRecord.get(4);
					temp = Float.parseFloat(csvRecord.get(4));
					for(NutriProfiler.PhysicalActivityEnum pa: NutriProfiler.PhysicalActivityEnum.values()) {
						if(temp == pa.getPhysicalActivityLevel() ) {
							flgPhysical = true;
						}
					}
					if(flgPhysical == false) {
						throw new NumberFormatException();
					}

					for(int i=5; i<csvRecord.size();i++) {
						sb.append(csvRecord.get(i)).append(", ");
					}

					//replace last comma
					if (sb.length() > 0) {
						sb.replace(sb.toString().length()-2, sb.toString().length()-1, "");
					}

					ingredientsToWatch = sb.toString().trim();

					if(csvRecord.get(0).equals("Female")) {
						NutriByte.person = new Female(Float.parseFloat(csvRecord.get(1)), Float.parseFloat(csvRecord.get(2)), Float.parseFloat(csvRecord.get(3)), Float.parseFloat(csvRecord.get(4)), ingredientsToWatch);
					}
					else if(csvRecord.get(0).equals("Male")) {
						NutriByte.person = new Male(Float.parseFloat(csvRecord.get(1)), Float.parseFloat(csvRecord.get(2)), Float.parseFloat(csvRecord.get(3)), Float.parseFloat(csvRecord.get(4)), ingredientsToWatch);
					}
				}
			}

			return true;
		}

		catch (FileNotFoundException e1) { e1.printStackTrace(); return false;}
		catch(InvalidProfileException ip) {
			return false;
		}
		catch(NumberFormatException n) {
			try {
				if(index == 1) {
					throw new InvalidProfileException("Invalid data for Age: " + cellValue + "\n" + "Age must be a number");
				}
				else if(index == 2) {
					throw new InvalidProfileException("Invalid data for Weight: " + cellValue + "\n" + "Weight must be a number");
				}
				else if(index == 3) {
					throw new InvalidProfileException("Invalid data for Height: " + cellValue + "\n" + "Height must be a number");
				}
				else if(index == 4) {
					throw new InvalidProfileException("Invalid physical activity level: " + cellValue + "\n" + "Must be 1.0, 1.1, 1.25 or 1.48");
				}
				return false;
			}catch(InvalidProfileException p) {
				return false;
			}
		}
		catch (IOException e1) { e1.printStackTrace(); return false;}
	}

	boolean validateProductData(String filename) {
		CSVFormat csvFormat = CSVFormat.DEFAULT;
		Product p1 = null, p2 = null;
		String ndbNum = null;
		boolean flgInvalidProduct = false;
		boolean flgMissingQty = false;
		String invalidProduct = null;
		String missingRecord = null;
		String singleRecord = null;
		String msg = null;
		int index = 0;
		StringBuilder sb = new StringBuilder();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {
				if(csvRecord.getRecordNumber() != 1) {
					if(!csvRecord.get(0).isEmpty()) {
						ndbNum = csvRecord.get(0).trim();
						if(NutriByte.model.productsMap.containsKey(ndbNum)) {
							p1 = NutriByte.model.productsMap.get(ndbNum);
							p2 = new Product(p1.getNdbNumber(), p1.getProductName(), p1.getManufacturer(), p1.getIngredients());
							p2.setHouseholdUom(p1.getHouseholdUom());
							p2.setServingUom(p1.getServingUom());
							p2.setProductNutrients(p1.getProductNutrients());

							sb = new StringBuilder();
							for(int i=0; i<csvRecord.size(); i++) {
								if(!csvRecord.get(i).isEmpty()) {
									sb.append(csvRecord.get(i)).append(",");
								}
							}

							if(sb.length()!=0) {
								sb.replace(sb.toString().length()-1, sb.toString().length(), "");
							}
							singleRecord = sb.toString();

							if(csvRecord.size() >= 3) {
								if(!csvRecord.get(1).isEmpty()) {
									p2.setServingSize(Float.parseFloat(csvRecord.get(1).trim()));
									index = 1;

									if(!csvRecord.get(2).isEmpty()) {
										p2.setHouseholdSize(Float.parseFloat(csvRecord.get(2).trim()));
										index = 1;
									}
									else {
										flgMissingQty = true;
										missingRecord = singleRecord;
									}
								}
								NutriByte.person.dietProductsList.add(p2);
							}
							else {
								flgMissingQty = true;
								missingRecord = singleRecord;
							}
							
						}
						else {
							flgInvalidProduct = true;
							invalidProduct = ndbNum;
						}
					}
				}
			}

			if(flgInvalidProduct == true) {
				throw new InvalidProfileException("No product found with this code: " + invalidProduct);
			}
			if(flgMissingQty == true) {
				msg = "Cannot read: " + missingRecord + "\n" + "The data must be - String, number, number - for ndb number," + "\n serving size, household size";
				throw new InvalidProfileException(msg);
			}

			return true;
		}

		catch(NumberFormatException n) {

			msg = "Cannot read: " + singleRecord + "\n" + "The data must be - String, number, number - for ndb number," + "\n serving size, household size";
			try {
				NutriByte.person.dietProductsList.clear();
				throw new InvalidProfileException(msg);
			}
			catch(InvalidProfileException exc) {
				return true;
			}
		}
		catch (InvalidProfileException p) {
			return true;
		}
		catch (FileNotFoundException e1) { e1.printStackTrace(); return false;}
		catch (IOException e1) { e1.printStackTrace(); return false;}
	}


}
