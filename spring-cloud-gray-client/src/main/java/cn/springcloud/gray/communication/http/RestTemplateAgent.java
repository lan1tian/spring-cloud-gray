package cn.springcloud.gray.communication.http;

import cn.springcloud.gray.http.HttpParams;
import cn.springcloud.gray.http.HttpRequest;
import cn.springcloud.gray.http.HttpResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-04 22:43
 */
public class RestTemplateAgent implements HttpAgent {

    private final String baseUrl;
    private RestTemplate rest;


    public RestTemplateAgent(String baseUrl) {
        this(baseUrl, new RestTemplate());
    }


    public RestTemplateAgent(String baseUrl, int timeout) {
        this.baseUrl = baseUrl;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) timeout);
        requestFactory.setReadTimeout((int) timeout);
        this.rest = new RestTemplate(requestFactory);
    }

    public RestTemplateAgent(String baseUrl, RestTemplate rest) {
        this.baseUrl = baseUrl;
        this.rest = rest;
    }


    @Override
    public HttpResult request(HttpRequest request) {
//        String url = getCompleteUrl(request.getPath(), request.getParamValues(), request.getEncoding());
        String url = getCompleteUrl(request.getPath(), request.getParamValues(), null);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        if (Objects.nonNull(request.getHeaders())) {
            httpHeaders.putAll(request.getHeaders().toMap());
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(request.getBody(), httpHeaders);
        ResponseEntity<String> responseEntity = rest.exchange(
                url, HttpMethod.resolve(request.getMethod().name()), httpEntity, String.class);
        return toHttpResult(responseEntity);
    }


    private HttpResult toHttpResult(ResponseEntity<String> responseEntity) {
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(responseEntity.getStatusCode().value());
        httpResult.setHeaders(responseEntity.getHeaders());
        httpResult.setContent(responseEntity.getBody());
        return httpResult;
    }

    private String getCompleteUrl(String path, HttpParams paramValues, String encoding) {
        StringBuilder url = new StringBuilder();
        url.append(baseUrl).append(path);
        if (!Objects.isNull(paramValues)) {
            String queryString = StringUtils.isEmpty(encoding) ? paramValues.toQueryString() : paramValues.encodingParams(encoding);
            url.append("?").append(queryString);
        }
        return url.toString();
    }
}
