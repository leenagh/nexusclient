package com.lxr.cli.nexus.Service;

import java.net.URI;
import java.util.List;

import com.lxr.cli.getversion.Item;
import com.lxr.cli.getversion.NexusResult;
import com.lxr.cli.nexus.NexusConstants;
import com.lxr.cli.nexus.NexusURL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NexusAPIClient {

    @Qualifier("simpleWebClient")
    private final WebClient webClient;
    private final NexusURL nexusURLObject;
    


    private Mono<String> transform(NexusResult response){
        List<Item> list = response.getItems();
		String downloadURL = null;
        if ( !list.isEmpty()){
			Item item = list.get(0);
			downloadURL = item.getDownloadUrl();
		}
        return Mono.just(downloadURL);
    }

  


    public Mono<String> getDownlaodURL(){
        Mono<NexusResult> nexusResultMono =  webClient.get()
        .uri(this::getURI)
        .retrieve().onStatus(HttpStatus::is4xxClientError, response ->{
           log.error("400 Error",response);     
           return Mono.error(new HttpClientErrorException(HttpStatus.NOT_FOUND, response.statusCode().getReasonPhrase()));
        })
        .onStatus(HttpStatus::is5xxServerError, response -> {
            log.error("500 Error", response);
            return Mono.error(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));

            }
            )
        .bodyToMono(NexusResult.class)
        .onErrorResume(WebClientResponseException.class,this::fallback);
         return nexusResultMono.flatMap(this::transform); 
    }

    public Mono<NexusResult> fallback(WebClientResponseException e){
        log.error("Error Message", e.getRawStatusCode());
        return Mono.empty();
    }
    public URI getURI(UriBuilder uriBuilder){
         URI uri=  uriBuilder.path(nexusURLObject.getNexusSearchURI())
        .queryParam(NexusConstants.REPOSITORY.toString(), nexusURLObject.getNexusRepoName())
        .queryParam(NexusConstants.GROUP.toString(), nexusURLObject.getGroupName())
        .queryParam(NexusConstants.NAME.toString(), nexusURLObject.getArtifactName())
        .queryParam(NexusConstants.MAVENEXTENSION.toString(),nexusURLObject.getMavenExtension())
        .queryParam(NexusConstants.SORT.toString(), nexusURLObject.getSort())
        .queryParam(NexusConstants.DIRECTION.toString(), nexusURLObject.getDirection())
        .queryParam(NexusConstants.MAVENCLASSIFIER.toString(), nexusURLObject.getMavenClassifier())
        .build();
        return uri;
    }

}
