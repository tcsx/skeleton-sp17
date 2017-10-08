package db;
public class TableNotExistException extends Exception{
	private static final long serialVersionUID = -6150201131858167675L;

	public TableNotExistException(String table){
        super(String.format("ERROR: THE TABLE NAMED %s DOESN'T EXIST.", table));
    }
}