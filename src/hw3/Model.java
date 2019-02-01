//Shiven Shashidhar; sshashid
package hw3;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * @author Shiven
 * This class handles the data aspect of the application (i.e., the 'M' in MVC)
 */
public class Model {
	static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
	static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();
	ObservableList<Product> searchResultsList = FXCollections.observableArrayList();

	/*
	 * Reads NutriByte.PRODUCT_FILE file to load product objects in the productsMap
	 */
	void readProducts(String filename) {	
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		CSVParser csvParser;
		Product prod = null;
		try {
			csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for(CSVRecord csvRecord: csvParser) {
				prod = new Product(csvRecord.get(0), csvRecord.get(1), csvRecord.get(4), csvRecord.get(7));
				productsMap.put(csvRecord.get(0), prod);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Reads NutriByte.NUTRIENT_FILE to load objects in the nutrients and products maps
	 */
	void readNutrients(String filename) {
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		CSVParser csvParser;
		Product product = null;
		Product.ProductNutrient productNutrient = null;

		try {
			csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for(CSVRecord csvRecord: csvParser) {
				if(!nutrientsMap.containsKey(csvRecord.get(1).trim())) {
					nutrientsMap.put(csvRecord.get(1).trim(), new Nutrient(csvRecord.get(1).trim(), csvRecord.get(2).trim(), csvRecord.get(5).trim()));
				}

				product = productsMap.get(csvRecord.get(0).trim());

				if(product != null) {
					if(Float.parseFloat(csvRecord.get(4).trim()) != 0 ) {				
						productNutrient = product.new ProductNutrient(csvRecord.get(1).trim(), Float.parseFloat(csvRecord.get(4).trim()));
						product.getProductNutrients().put(csvRecord.get(1).trim(), productNutrient);

					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Reads NutriByte.SERVING_SIZE_FILE to populate four fields:
	 * servingSize, servingUom, householdSize, householdUom
	 */
	void readServingSizes(String filename) {
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		CSVParser csvParser;
		Product prod;
		try {
			csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for(CSVRecord csvRecord: csvParser) {
				if((prod = productsMap.get(csvRecord.get(0))) != null) {

					if(csvRecord.get(1).trim().isEmpty() || csvRecord.get(1).trim() == null) {
						prod.setServingSize(0f);}
					else {
						prod.setServingSize(Float.parseFloat(csvRecord.get(1).trim()));
					}

					prod.setServingUom(csvRecord.get(2).trim());

					if(csvRecord.get(3).trim().isEmpty() || csvRecord.get(3).trim() == null) {
						prod.setHouseholdSize(0f);
					}
					else {
						prod.setHouseholdSize(Float.parseFloat(csvRecord.get(3).trim()));
					}

					prod.setHouseholdUom(csvRecord.get(4).trim());

				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Reads the profile file chosen by the user.
	 * Returns true or false depending on the value returned by the readFile() method of CSVFiler or XMLFiler
	 */
	boolean readProfiles(String filename) {

		String extension = filename.substring(filename.length()-3);
		DataFiler df = null;

		if(extension.equalsIgnoreCase("csv")) {
			df = new CSVFiler();
			return df.readFile(filename);
		}
		else if(extension.equalsIgnoreCase("xml")) {
			df = new XMLFiler();
			return df.readFile(filename);
		}

		return false;
	}

	void writeProfile(String filename) {
		String extension = filename.substring(filename.length()-3);
		DataFiler df = null;

		if(extension.equalsIgnoreCase("csv")) {
			df = new CSVFiler();
			df.writeFile(filename);
		}
		else if(extension.equalsIgnoreCase("xml")) {
			df = new XMLFiler();
			df.writeFile(filename);
		}

		return;
	}

}
