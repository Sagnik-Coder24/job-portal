package com.project.jobportal.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {

    private static Path foundFile;

    public static Resource getFileAsResource(String downloadDirectory, String fileName) throws IOException {

        Path path = Paths.get(downloadDirectory);
        Files.list(path).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileName)) {
                foundFile = file;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}
