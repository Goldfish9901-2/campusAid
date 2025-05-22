package cn.edu.usst.cs.campusAid.service;

/**
 * 整个校园互助平台中所有可预知的异常捕获后，都转为这个类
 * @author USST-CS
 */
public class CampusAidException extends RuntimeException {
    public CampusAidException() {
        super();
    }

    /**
     * {@link CampusAidException}
     */
    public CampusAidException(String message) {
        super(message);

    }

    /**
     * {@link CampusAidException}
     */
    public CampusAidException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@link CampusAidException}
     */
    public CampusAidException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    /**
     * {@link CampusAidException}
     */
    public CampusAidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
