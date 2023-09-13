package exception;

public class DAOException extends Exception {
    public DAOException(String mes){
        super();
    }
    public DAOException(String mes, Throwable e){
        super(mes,e);
    }
}
