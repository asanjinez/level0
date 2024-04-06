package org.solution.exceptions;

public class CustomExceptions {
    private CustomExceptions(){}
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String username) {
            super("User not found: " + username);
        }
    }

    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException() {
            super("Insufficient funds");
        }
    }

    public static class FileLoadException extends RuntimeException {
        public FileLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class FileSaveException extends RuntimeException {
        public FileSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class ProgramNotAvailableException extends RuntimeException {
        public ProgramNotAvailableException(String message) {
            super(message);
        }
    }
    public static class CityNotAvailableException extends RuntimeException {
        public CityNotAvailableException(String message) {
            super(message);
        }
    }
    public static class InexistentProgram extends RuntimeException {
        public InexistentProgram(String message) {
            super(message);
        }
    }
}
