package exception;

public class DBConnectionException extends Exception{
    public DBConnectionException(String message){
        super(message);
    }
    public DBConnectionException(String message, Throwable e){
        super(message, e);
    }
}
