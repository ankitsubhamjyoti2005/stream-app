package com.stream.app.spring_stream_backend.services.impli;

import com.stream.app.spring_stream_backend.entities.Video;
import com.stream.app.spring_stream_backend.services.VideoServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class videoServiceImpli implements VideoServices {

    @Value("${files.video}")
    private String DIR;

    @Override
    public Video save(Video video, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();//get original name of the file
            String contentType = file.getContentType(); //type of the cont mp4,mp3 ...
            InputStream inputStream = file.getInputStream();//read the data

            //folder path : create
            String cleanFileName = StringUtils.cleanPath(filename);
            String cleanFolder = StringUtils.cleanPath(DIR);

            Path path = Paths.get(cleanFolder,cleanFileName);

            System.out.println(path);
            //folder path with fiile name

            //copy file to folder

            //metadata
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Video get(String videoId) {
        return null;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return List.of();
    }

    @Override
    public String processVideo(String videoId) {
        return "";
    }
}
