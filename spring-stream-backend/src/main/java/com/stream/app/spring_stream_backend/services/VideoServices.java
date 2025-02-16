package com.stream.app.spring_stream_backend.services;
import com.stream.app.spring_stream_backend.entities.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoServices {
    //save  video
    Video save(Video video, MultipartFile file);


    // get video by  id
    Video get(String videoId);


    // get video by title

    Video getByTitle(String title);

    List<Video> getAll();


    //video processing
    String processVideo(String videoId);

}
