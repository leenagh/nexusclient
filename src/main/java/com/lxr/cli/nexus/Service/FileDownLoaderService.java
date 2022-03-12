package com.lxr.cli.nexus.Service;

import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Service
public class FileDownLoaderService {

    @Qualifier("fileDownloadWebClient")
    private final WebClient webclient;


    FileDownLoaderService(WebClient webclient){
        this.webclient = webclient;
    }


    
    public void downloadUsingFlux(String url, Path destination) throws IOException {
        Files.createDirectories(destination.getParent());
        if (Files.notExists(destination, LinkOption.NOFOLLOW_LINKS)) {
            Files.createFile(destination);
        }

        Flux<DataBuffer> dataBuffer = webclient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(DataBuffer.class);

        DataBufferUtils.write(dataBuffer, destination,
                StandardOpenOption.CREATE)
                .share().block();
    }
}
