package com.lxr.cli.nexus;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@Configuration
public class NexusURL {

    @Value("${nexusURL:http://localhost:8081}")
    private String nexusurl;
	@Value("${nexusSearchURI:/service/rest/v1/search/assets}")
    private String nexusSearchURI;
	@Value("${nexusRepoName:maven-releases}")
	private String nexusRepoName;
    //@Value("${groupName:com.lxr.*}")
	@Value("${groupName:com.rushikesh.apis}")
	private String groupName;
	//@Value("${artifactName:getVersion}")
	@Value("${artifactName:example}")
	private String artifactName;
	
	@Value("${maven.extension:jar}")
	private String mavenExtension;

	@Value("${maven.classifier:}")   
	private String mavenClassifier;

    private final String sort = "version";
    private final String direction = "desc";
    @Value("${output:./build-output/}")
	private String  output;
	


}
