package ar.edu.utn.frc.tup.lciii.Exception;

public class PartidaNotFoundException extends RuntimeException {

    public PartidaNotFoundException(String message) {
        super(message);
    }

    public PartidaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
