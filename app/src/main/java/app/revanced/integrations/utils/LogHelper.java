package app.revanced.integrations.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LogHelper {

    /**
     * Log messages using lambdas.
     */
    public interface LogMessage {
        @NonNull
        String buildMessageString();

        /**
         * @return For outer classes, this returns {@link Class#getSimpleName()}.
         * For inner, static, or anonymous classes, this returns the simple name of the enclosing class.<br>
         * <br>
         * For example, each of these classes return 'SomethingView':
         * <code>
         * com.company.SomethingView
         * com.company.SomethingView$StaticClass
         * com.company.SomethingView$1
         * </code>
         */
        private String findOuterClassSimpleName() {
            var selfClass = this.getClass();

            String fullClassName = selfClass.getName();
            final int dollarSignIndex = fullClassName.indexOf('$');
            if (dollarSignIndex == -1) {
                return selfClass.getSimpleName(); // already an outer class
            }

            // class is inner, static, or anonymous
            // parse the simple name full name
            // a class with no package returns index of -1, but incrementing gives index zero which is correct
            final int simpleClassNameStartIndex = fullClassName.lastIndexOf('.') + 1;
            return fullClassName.substring(simpleClassNameStartIndex, dollarSignIndex);
        }
    }

    private static final String LOG_PREFIX = "Extended: ";

    /**
     * Logs exceptions under the outer class name of the code calling this method.
     */
    public static void printException(@NonNull LogMessage message) {
        printException(message, null);
    }

    /**
     * Logs exceptions under the outer class name of the code calling this method.
     * <p>
     * If the calling code is showing it's own error toast,
     *
     * @param message          log message
     * @param ex               exception (optional)
     */
    public static void printException(@NonNull LogMessage message, @Nullable Throwable ex) {
        String messageString = message.buildMessageString();
        String outerClassSimpleName = message.findOuterClassSimpleName();
        String logMessage = LOG_PREFIX + outerClassSimpleName;
        if (ex == null) {
            Log.e(logMessage, messageString);
        } else {
            Log.e(logMessage, messageString, ex);
        }
    }

}