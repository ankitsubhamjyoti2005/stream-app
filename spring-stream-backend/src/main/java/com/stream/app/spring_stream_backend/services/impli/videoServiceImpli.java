package com.stream.app.spring_stream_backend.services.impli;

import com.stream.app.spring_stream_backend.entities.Video;
import com.stream.app.spring_stream_backend.repositories.VideoRepository;
import com.stream.app.spring_stream_backend.services.VideoServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class videoServiceImpli implements VideoServices {

    @Value("${files.video}")
    private String DIR;


    private VideoRepository videoRepository;

    public videoServiceImpli(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @PostConstruct
    public void init() {
        File file = new File(DIR);
        if (!file.exists()) {
            file.mkdir(); //creat the folder id not present
            System.out.println("folder is created");
        }else {
            System.out.println("folder already exists");
        }
    }

    @Override
    public Video save(Video video, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();//get original name of the file
            String contentType = file.getContentType(); //type of the cont mp4,mp3 ...
            InputStream inputStream = file.getInputStream();//read the data

            //folder path : create
            String cleanFileName = StringUtils.cleanPath(filename);
            String cleanFolder = StringUtils.cleanPath(DIR);

            //folder path with file name
            Path path = Paths.get(cleanFolder,cleanFileName);
            System.out.println(path);

            //copy file to folder
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);

            //metadata
            video.setContentType(contentType);
            video.setFilePath(path.toString());

            return videoRepository.save(video);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Video get(String videoId) {

        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException());
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @Override
    public String processVideo(String videoId) {
        return "";
    }
}
