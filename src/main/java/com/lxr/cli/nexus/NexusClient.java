package com.lxr.cli.nexus; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lxr.cli.nexus.Service.FileDownLoaderService;
import com.lxr.cli.nexus.Service.NexusAPIClient;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class NexusClient implements CommandLineRunner {


	@Value("${help:''}")
	private String help;   
	@Value("${output:./build-output/}")
	private String  output;
	private final FileDownLoaderService downloadService;
	private final NexusAPIClient apiClient;
	
	
	

	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(NexusClient.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

	}
	public static void printHelpMessage(){
			String newLine = System.getProperty("line.separator");
			String helpString = String.join(newLine, 		
			newLine, 
			"Usage: java -jar getVersion-1.0.0.jar --nexusURL=http://localhost:8081 \\",
			"            --nexusSearchURI=/service/rest/v1/search/assets \\",
			"            --nexusRepoName=maven-releases \\", 
			"            --groupName=com.lxr.cli \\", 
			"            --artifactName=getversion \\", 
			"            --maven.extension=jar \\", 
			"            --maven.classifier=sources");
			log.info (helpString);
	}

	public static void downloadUrlBuilder(String[] args){
		log.info("In downloadUrlBuilder ------------->");
		for(String arg:args) {
			log.info("args[]:{}", arg);
		}
	}

	@Override
    public void run(String... args) {
		if ( args.length  <= 1 || (args.length  == 1 && args[0] != null && args[0].equalsIgnoreCase("--help")) ){
			printHelpMessage();
		}
		
		downloadUrlBuilder(args);
		
		Mono<String> downloadMono = apiClient.getDownlaodURL();
		String downloadURL = downloadMono.block();
		this.downloadFile(downloadURL);
	//    for (int i = 0; i < args.length; ++i) {
    //         log.info("args[{}]: {}", i, args[i]);
    //     }
    }

	public  void downloadFile(String downloadUrl){
		String fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/')+1);
		Path destination = Paths.get(output+fileName);
		try {
			downloadService.downloadUsingFlux(downloadUrl, destination);		
		} catch(IOException e){
			log.error("Error Downloading File", e);
		}
	}

}
