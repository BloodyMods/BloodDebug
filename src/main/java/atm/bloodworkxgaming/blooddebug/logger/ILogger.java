package atm.bloodworkxgaming.blooddebug.logger;

public interface ILogger {

    void logCommand(String message);

    void logInfo(String message);

    void logWarning(String message);

    void logError(String message);

    void logError(String message, Throwable exception);
}
