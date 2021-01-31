package ch.sebastianhaeni.prophector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProphectorResourceLoader {

    private static final String LINE_ENDING = "\n";

    /**
     * Loads a file on the classpath. Usually inside the jar, but may also be outside.
     *
     * @param classPath Absolute path starting from the top of the requested jar.
     * @return found stream or <code>null</code>. IMPORTANT: the user is responsible to close the stream itself!
     */
    public InputStream findClasspathFileAsStream(String classPath) throws FileNotFoundException {
        var inputStream = this.getClass().getResourceAsStream(classPath);
        if (inputStream == null) {
            throw new FileNotFoundException(String.format("could not retrieve %s from jar/classpath", classPath));
        }
        return inputStream;
    }

    /**
     * @throws FileNotFoundException when the file does not exist.
     * @throws IOException           when an error reading the file occurred.
     */
    public byte[] readClasspathFileAsByteArray(String classPath) throws IOException {
        try (var fileStream = this.findClasspathFileAsStream(classPath)) {
            var offset = 0;
            int bytesRead;
            var byteData = new byte[fileStream.available()];
            //noinspection NestedAssignment
            while ((bytesRead = fileStream.read(byteData, offset, byteData.length - offset)) != -1) {
                offset += bytesRead;
                if (offset >= byteData.length) {
                    log.warn(
                            "Reading classpath file wanted to read more than it initially said! Classpath: {}",
                            classPath);
                    break;
                }
            }
            if (offset < byteData.length - 1) {
                log.warn(
                        "Reading classpath file wanted to read less than it initially said! Classpath: {}", classPath);
            }
            return byteData;
        }
    }

    /**
     * @throws FileNotFoundException when the file does not exist.
     * @throws IOException           when an error reading the file occurred.
     */
    public String readClasspathFileAsString(String classPath) throws IOException {
        try (var fileStream = this.findClasspathFileAsStream(classPath);
             var buffer = new BufferedReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8))) {
            return buffer.lines().collect(Collectors.joining(LINE_ENDING));
        }
    }

    /**
     * Loads a file on the classpath or from the local development web-directory when in localmode.
     *
     * @param webFilePath Absolute path starting from the top of the requested jar.
     * @return found stream or <code>null</code>. IMPORTANT: the user is responsible to close the stream itself!
     */
    public InputStream findWebFileAsStream(String webFilePath) throws FileNotFoundException {
        return this.findClasspathFileAsStream("/static" + webFilePath);
    }

    /**
     * @throws FileNotFoundException when the file does not exist.
     * @throws IOException           when an error reading the file occurred.
     */
    public String readWebFileAsString(String classPath) throws IOException {
        try (var fileStream = this.findWebFileAsStream(classPath);
             var buffer = new BufferedReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8))) {
            return buffer.lines().collect(Collectors.joining(LINE_ENDING));
        }
    }
}
