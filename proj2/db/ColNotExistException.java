package db;
public class ColNotExistException extends Exception{
	private static final long serialVersionUID = -5814975480420186025L;

	public ColNotExistException(String col){
        super(String.format("ERROR: COLUMN " + col + " DOES NOT EXIST."));
    }
}