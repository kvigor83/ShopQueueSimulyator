package by.kastsiuchenka.kassa.exception;

public class BundleException extends Exception {
    public BundleException() {
        super();
    }

    public BundleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BundleException(String message) {
        super(message);
    }

    public BundleException(Throwable cause) {
        super(cause);
    }
}
