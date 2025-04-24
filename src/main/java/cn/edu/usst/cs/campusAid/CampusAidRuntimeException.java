package cn.edu.usst.cs.campusAid;

public class CampusAidRuntimeException extends RuntimeException{
    public CampusAidRuntimeException() {
        super();
    }

    public CampusAidRuntimeException(String message) {
        super(message);
    }

    public CampusAidRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampusAidRuntimeException(Throwable cause) {
        super(cause);
    }

    protected CampusAidRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
