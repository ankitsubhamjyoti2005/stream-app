package com.stream.app.spring_stream_backend.controller;

import com.stream.app.spring_stream_backend.payload.CustomMessage;
import com.stream.app.spring_stream_backend.services.VideoServices;
import com.stream.app.spring_stream_backend.entities.Video;
//import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {
    private VideoServices videoServices;

    public VideoController(VideoServices videoServices) {
        this.videoServices = videoServices;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam("file")MultipartFile file,
                                                @RequestParam("title")String title,
                                                @RequestParam("description")String description){
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());

        Video savedVideo = videoServices.save(video,file);

        if(savedVideo != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(video);
        }else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomMessage.builder()
                            .message("the video is not uploaded")
                            .success(false));
        }
    }

    //get all the video in the db
    @GetMapping
    public List<Video> getAllVideos(){
        return videoServices.getAll();
    }
    //stream at once
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(@PathVariable String videoId){
        Video video = videoServices.get(videoId);
        String ContentType = video.getContentType();
        String  filePath = video.getFilePath();

        Resource resource = new FileSystemResource(filePath);
        if(ContentType == null){
            ContentType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(ContentType))
                .body(resource);

    }

    //get the video in chunks
    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamRange(@PathVariable String videoId,@RequestHeader(value="Range",required= false)String Range){
        System.out.println(Range);

        Video video = videoServices.get(videoId);
        Path path = Paths.get(video.getFilePath());

        Resource resource = new FileSystemResource(path);

        String ContentType = video.getContentType();

        if(ContentType == null){
            ContentType = "application/octet-stream";
        }

        long fileLength = path.toFile().length();

        //if header not present send full video
        if(Range.equals(null)){
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(ContentType))
                    .body(resource);
        }

        long rangeStart;
        long rangeEnd;

        String[] ranges = Range.replace("bytes=","").split("-");
        rangeStart = Long.parseLong(ranges[0]);

        //set the end of the range
        if(ranges.length > 1){
            rangeEnd = Long.parseLong(ranges[1]);
        }else{
            rangeEnd = fileLength - 1;
        }

        if(rangeEnd > fileLength - 1){
            rangeEnd = fileLength - 1;
        }

        InputStream inputStream ;
        try{
            inputStream = Files.newInputStream(path);

            //skip the initial data
            inputStream.skip(rangeStart-1);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        long contentLength = rangeEnd - rangeStart;


        byte[] data = new byte[(int) contentLength];
        int read;

        try {
            read = inputStream.read(data,0,data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("read(number of bytes are:)"+read);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("X-Content-Type-Options", "nosniff");
        headers.setContentLength(contentLength);

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .body(new ByteArrayResource(data));
        //return ResponseEntity.ok().contentType(MediaType.parseMediaType(ContentType)).headers(headers).build();
    }
}


