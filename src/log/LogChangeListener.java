package log;

public interface LogChangeListener {
    void onLogChanged();

    void close();

    boolean on = true;
}
