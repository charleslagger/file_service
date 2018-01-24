package service;

import static config.Headers.clientDevice;
import static config.Headers.clientIp;
import static config.Headers.clientSession;
import static config.Headers.epAuth;
import static config.Headers.epMac;
import static config.Headers.epNAME;
import static config.Headers.userName;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.vega.core.CoreResponse;

import db.entities.FileCore;
import db.entities.FileCore.AckUpload;
import util.JsonMapper;

@Service
public class NotificationService {
	private Logger log = Logger.getLogger(getClass());

	private final String HOST;
	private final String CONTEXT_PATH;
	private ParameterizedTypeReference<CoreResponse> parameterizedTypeReference = new ParameterizedTypeReference<CoreResponse>() {
	};
	private HttpHeaders headers = new HttpHeaders();

	public NotificationService(Environment env) {
		HOST = env.getProperty("host");
		CONTEXT_PATH = env.getProperty("context_path");
		addHeader(headers);
	}

	private void addHeader(HttpHeaders headers) {
		System.out.println("==>> epName" + epNAME);

		headers.add("EP_NAME", epNAME);
		headers.add("EP_MAC", epMac);
		headers.add("EP_AUTH", epAuth);
		headers.add("Client_Session", clientSession);
		headers.add("Client_Device", clientDevice);
		headers.add("Client_Ip", clientIp);
		headers.add("User_Name", userName);
	}

	public void sendLoanDocuments(FileCore.LoanDocuments loanDocuments) {
		String urlAckResponse = HOST + CONTEXT_PATH + loanDocuments.getAckUrl();
		log.info("===>>>UrlAckResponse: " + urlAckResponse);
		log.info("===>>>LoanDocuments: " + JsonMapper.writeValueAsString(loanDocuments));
		HttpHeaders headers = new HttpHeaders();

		addHeader(headers);
		// set up
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

		HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connectionManager)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(5, true));

		HttpClient httpClient = clientBuilder.build();// Return a new httpClient
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
		// post
		ResponseEntity<CoreResponse> response = restTemplate.exchange(urlAckResponse, HttpMethod.POST,
				new HttpEntity<List<FileCore.LoanDocument>>(loanDocuments.getLoanDocuments(), headers),
				parameterizedTypeReference);
		log.info("Response body: " + response.getBody().getMessageCode());

		log.info("==>>Response Body: " + response.getBody());
	}

	public void sendAcknowledgeUploadContract(AckUpload ackUpload) {

		String urlAckResponse = HOST + CONTEXT_PATH + ackUpload.getAckUrl();
		log.info("===>>>UrlAckResponse: " + urlAckResponse);
		log.info("===>>>AckUploaded: " + JsonMapper.writeValueAsString(ackUpload.getAcknowledgeUploaded()));

		// set up
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

		HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connectionManager)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(5, true));

		HttpClient httpClient = clientBuilder.build();// Return a new httpClient
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
		// post
		ResponseEntity<CoreResponse> response = restTemplate.exchange(urlAckResponse, HttpMethod.POST,
				new HttpEntity<AckUpload.AcknowledgeUploaded>(ackUpload.getAcknowledgeUploaded(), headers),
				parameterizedTypeReference);
		log.info("Response body: " + response.getBody().getMessageCode());

		log.info("==>>Response Body: " + response.getBody());
	}

	public void sendDisbursement(String ackUrl, Object... objects) {
		String urlAckResponse = HOST + CONTEXT_PATH + ackUrl;

		log.info("\n[GET] call url: " + urlAckResponse);

		// set up
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

		HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connectionManager)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(5, true));

		HttpClient httpClient = clientBuilder.build();
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
		// get
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlAckResponse);
		for (int i = 0; i < objects.length; i += 2) {
			builder.queryParam(objects[i].toString(), objects[i + 1]);
		}

		log.info("Builder.toUriString: " + builder.toUriString());
		ResponseEntity<CoreResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				new HttpEntity<Object>(headers), parameterizedTypeReference);

		log.info("Response body: " + response.getBody().getMessageCode());
	}
}
