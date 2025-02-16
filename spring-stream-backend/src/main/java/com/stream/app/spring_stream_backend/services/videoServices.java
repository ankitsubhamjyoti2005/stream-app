package com.stream.app.spring_stream_backend.services;
import com.stream.app.spring_stream_backend.entities.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface videoServices {
    //save  video
    video save(video video, MultipartFile file);


    // get video by  id
    video get(String videoId);


    // get video by title

    video getByTitle(String title);

    List<video> getAll();


    //video processing
    String processVideo(String videoId);

}
