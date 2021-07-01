package me.tongfei.progressbar;

import java.io.PrintStream;

import static me.tongfei.progressbar.TerminalUtils.*;

/**
 * Progress bar consumer for terminals supporting moving cursor up/down.
 * @since 0.9.0
 * @author Martin Vehovsky
 */
public class InteractiveConsoleProgressBarConsumer extends ConsoleProgressBarConsumer {

    private boolean initialized = false;
    private int position = 1;

    public InteractiveConsoleProgressBarConsumer(PrintStream out) {
        super(out);
    }

    public InteractiveConsoleProgressBarConsumer(PrintStream out, int predefinedWidth) {
        super(out, predefinedWidth);
    }

    @Override
    public void accept(String str) {
        String s = StringDisplayUtils.trimDisplayLength(str, getMaxRenderedLength());
        if (!initialized) {
            TerminalUtils.filterActiveConsumers(InteractiveConsoleProgressBarConsumer.class).forEach(c -> c.position++);
            TerminalUtils.activeConsumers.add(this);
            out.println(CARRIAGE_RETURN + s);
            initialized = true;
        }
        else out.print(moveCursorUp(position) + s + moveCursorDown(position));
    }

    @Override
    public void close() {
        out.flush();
        TerminalUtils.activeConsumers.remove(this);
    }
}
