package com.stream.app.spring_stream_backend.controller;

import com.stream.app.spring_stream_backend.payload.CustomMessage;
import com.stream.app.spring_stream_backend.services.VideoServices;
import com.stream.app.spring_stream_backend.entities.Video;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private VideoServices   videoServices;
    public VideoController(VideoServices videoServices) {
        this.videoServices = videoServices;
    }

    @PostMapping
    public ResponseEntity<CustomMessage> create(@RequestParam("file")MultipartFile file,
                                                @RequestParam("title")String title,
                                                @RequestParam("description")String description){
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());

        videoServices.save(video,file);

        return null;
    }
}
