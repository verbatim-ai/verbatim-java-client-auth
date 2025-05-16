package cloud.verbatim.client.auth;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class KeyLoader {

    private Key key = null;

    public KeyLoader from(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream cannot be null");
        return from(new InputStreamReader(inputStream));
    }

    public KeyLoader from(Reader reader) {
        Objects.requireNonNull(reader, "reader cannot be null");
        assertState();
        key = new Gson().fromJson(reader, Key.class);
        return this;
    }

    public KeyLoader from(File file) throws FileNotFoundException {
        Objects.requireNonNull(file, "file cannot be null");
        return from(new FileReader(file));
    }

    public KeyLoader from(Path path) throws FileNotFoundException {
        Objects.requireNonNull(path, "path cannot be null");
        return from(path.toFile());
    }

    public Key get() {
        assertKey();
        return key;
    }

    private void assertState() {
        if (key != null) {
            throw new RuntimeException("Key is already loaded");
        }
    }

    private void assertKey() {
        Objects.requireNonNull(key, "key cannot be null");
        key.assertContent();
    }
}
