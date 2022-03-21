package uwu.narumi.tama.helper;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionHelper {

    private ExceptionHelper() {
    }

    public static String toString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
