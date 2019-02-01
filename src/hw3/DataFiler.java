//Shiven Shashidhar; sshashid
package hw3;

/**
 * @author Shiven
 * An abstract class to handle read and write operations from files
 */
public abstract class DataFiler {

	public abstract void writeFile(String filename);

	public abstract boolean readFile(String filename);

}
