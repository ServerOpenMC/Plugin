package fr.communaywen.core.utils;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DatapackUtils {
    private static final String separator = FileSystems.getDefault().getSeparator();

    public static void fileWalk(String datapacksFolderPath, File file, boolean isZip, String folderName) throws IOException {
        String targetFilePath = datapacksFolderPath + separator
                + folderName;
        File targetFile = new File(targetFilePath);
        if (targetFile.exists()) {
            return;
        }

        if (isZip) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(file.toPath()))) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    String childPath = targetFilePath + separator + zipEntry.getName();
                    File childFile = new File(childPath);
                    if (!zipEntry.isDirectory()) {
                        File parentFile = childFile.getParentFile();
                        if (!parentFile.mkdirs() && !parentFile.exists()) {
                            throw new IOException("Could not create directory '" + parentFile + "'!");
                        }

                        copy(new BufferedInputStream(zipInputStream), childPath);
                    } else {
                        if (!childFile.mkdirs() && !childFile.exists()) {
                            throw new IOException("Could not create directory '" + childFile + "'!");
                        }
                    }

                    zipEntry = zipInputStream.getNextEntry();
                }
            }

            if (true) {
                file.delete();
                return;
            }
        }

        Files.walkFileTree(Paths.get(targetFilePath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                String dirName = dir.getFileName().toString();
                if (dirName.equals("data") || dirName.equals("assets")) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(".zip")) {
                    DatapackUtils.fileWalk(datapacksFolderPath, new File(String.valueOf(file)),
                            true, targetFilePath);
                    return FileVisitResult.CONTINUE;
                }

                Path parentPath = file.getParent();
                if (!fileName.equals("pack.mcmeta")) {
                    return FileVisitResult.CONTINUE;
                }

                String parentName = parentPath.getFileName().toString();
                File datapackTargetFile = new File(
                        datapacksFolderPath + separator + parentName);
                if (datapackTargetFile.exists()) {
                    return FileVisitResult.CONTINUE;
                }

                copyDirectory(parentPath.toFile(), datapackTargetFile);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                throw new IOException("IOException: Failed to visit file '" + file + "'!\n", e);
            }
        });

        deleteDirectory(targetFile);
        if (isZip) {
            file.delete();
        }
    }

    public static void copy(BufferedInputStream inputStream, String outFilePath) throws IOException {
        ReadableByteChannel in = Channels.newChannel(inputStream);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFilePath)) {
            fileOutputStream.getChannel().transferFrom(in, 0, Long.MAX_VALUE);
        }
    }

    private static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        for (String f : sourceDirectory.list()) {
            copyDirectoryCompatibityMode(new File(sourceDirectory, f), new File(destinationDirectory, f));
        }
    }
    public static void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }
    private static void copyFile(File sourceFile, File destinationFile)
            throws IOException {
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
