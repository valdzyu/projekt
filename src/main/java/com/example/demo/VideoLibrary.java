package com.example.demo;

import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;

public class VideoLibrary {


    // Metoda pro filtraci nalezených souborů podle jejich přípony (typu)
    public Collection<Video> getFiles(String path, String suffix){
        Collection<File> files = scanFiles(path);

        files.removeIf(file -> !FilenameUtils.getExtension(file.getName()).contains(suffix));
        Collection<Video> videos = new ArrayList<>();
        for (File file: files){
            videos.add(new Video(file, file.getParentFile().getName() + "/" + file.getName()));
        }
        return videos;
    }

    // Metoda pro přetvoření objektů file z objektu path
    private Collection<File> scanFiles(String path){
        Collection<File> files = new ArrayList<File>();
        Path directory = Path.of(path);
        try {
            Collection<Path> paths = discoverFiles(directory);
            for (Path p: paths){
                files.add(new File(String.valueOf(p)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    // Metoda pro automatické zjisťování souborů ze zadané cesty
    // Využívá walkFileTree, která naplní předanou kolekci all
    static Collection<Path> discoverFiles(Path directory) throws IOException{
        Collection<Path> paths = new ArrayList<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                paths.add(Path.of(String.valueOf(file)));
                return super.visitFile(file, attrs);
            }
        });
        return paths;
    }
}
