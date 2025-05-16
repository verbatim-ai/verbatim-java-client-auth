package cloud.verbatim.client.auth;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

/**
 * KeyLoader is responsible for loading and managing the `Key` object
 * in a secure and reliable manner. It provides multiple methods to load
 * a `Key` instance from various input sources such as a file, InputStream, or Reader.
 * <p>
 * The class ensures integrity and correctness by validating the key's
 * state during loading and access operations.
 */
public class KeyLoader {

    private Key key = null;

    /**
     * Loads a key from the provided file system path.
     * <p>
     * This method ensures that the specified path is not null and delegat_es
     * the loading process to the {@link #from(File)} method after converting
     * the Path to a File object.
     *
     * @param path the file system path pointing to the key file, must not be null
     * @return the current instance of KeyLoader
     * @throws FileNotFoundException if the file specified by the path does not exist
     * @throws NullPointerException  if the path is null
     */
    public KeyLoader from(Path path) throws FileNotFoundException {
        Objects.requireNonNull(path, "path cannot be null");
        return from(path.toFile());
    }

    /**
     * Loads a key from the provided File.
     * <p>
     * This method reads the file containing the key data in JSON format,
     * creates a FileReader for the file, and delegates the loading process
     * to the {@link #from(Reader)} method.
     *
     * @param file the File object pointing to the key file, must not be null
     * @return the current instance of KeyLoader
     * @throws FileNotFoundException if the specified file does not exist
     * @throws NullPointerException  if the file is null
     */
    public KeyLoader from(File file) throws FileNotFoundException {
        Objects.requireNonNull(file, "file cannot be null");
        return from(new FileReader(file));
    }

    /**
     * Loads a key from the given InputStream.
     * <p>
     * This method requires a valid InputStream that contains the key data in JSON format.
     * It uses an InputStreamReader to process the InputStream and delegates the loading process
     * to the {@link #from(Reader)} method.
     *
     * @param inputStream the InputStream containing the key data, must not be null
     * @return the current instance of KeyLoader
     * @throws NullPointerException if the inputStream is null
     */
    public KeyLoader from(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream cannot be null");
        return from(new InputStreamReader(inputStream));
    }


    /**
     * Loads a key from the provided Reader.
     * <p>
     * This method parses the key data in JSON format from the given Reader
     * and initializes the `Key` instance. It ensures that the Reader is not null
     * and validates the internal state of the KeyLoader to prevent multiple keys
     * from being loaded concurrently.
     *
     * @param reader the Reader containing the key data in JSON format, must not be null
     * @return the current instance of KeyLoader
     * @throws NullPointerException if the reader is null
     * @throws RuntimeException     if a key is already loaded
     */
    public KeyLoader from(Reader reader) {
        Objects.requireNonNull(reader, "reader cannot be null");
        assertState();
        key = new Gson().fromJson(reader, Key.class);
        return this;
    }

    /**
     * Retrieves the currently loaded Key instance.
     * <p>
     * This method ensures that the Key object has been properly initialized
     * and validated before it is returned
     */
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
