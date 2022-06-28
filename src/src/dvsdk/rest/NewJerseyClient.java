package dvsdk.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import dvsdk.GlobalConfig;
import java.io.File;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/** Jersey REST client generated for REST resource:UserResource [generic]<br>
 *  USAGE:<pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 *  </pre>
 * @author develop-dvs
 */
public class NewJerseyClient {
    private FormDataMultiPart multiPart = new FormDataMultiPart();
    private WebResource webResource;
    private Client client;
    private String BASE_URI = "http://"+GlobalConfig.GLOBAL_MULIS_SERVER+":"+GlobalConfig.GLOBAL_MULIS_SERVER_PORT+"/"+GlobalConfig.GLOBAL_MULIS_SERVER_URI;

    public NewJerseyClient(String path) {
        this(path,GlobalConfig.GLOBAL_MULIS_SERVER,GlobalConfig.GLOBAL_MULIS_SERVER_PORT,GlobalConfig.GLOBAL_MULIS_SERVER_URI);
    }
    
    private void buildUri(String host, String port, String uri) {
        BASE_URI="http://"+host+":"+port+"/"+uri;
    }
    
    public NewJerseyClient(String path, String host, String port, String uri) {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        buildUri(host, port, uri);
        client = Client.create(config);
        //client.addFilter(new GZIPContentEncodingFilter(true));
        webResource = client.resource(BASE_URI).path(path).queryParam("XDEBUG_SESSION_START", "netbeans-xdebug");
    }
    
    public void changePath(String path) {
        webResource = client.resource(BASE_URI).path(path);
    }
    
    public void putJson(Object requestEntity) {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(requestEntity);
    }

    public String getState() {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }
    
    public String postRequest(Object requestEntity, String pwd) {
        WebResource resource = webResource;
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("dt", requestEntity.toString());
        if (!pwd.isEmpty()) formData.add("dvsauth", pwd);
        ClientResponse clientResponse = resource.type(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class,formData);
        return clientResponse.getEntity(String.class);
    }
    
    public WebResource getWebResource() {
        return webResource;
    }
    
    public void close() {
        client.destroy();
    }

    public void setUsernamePassword(String username, String password) {
        client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(username, password));
    }

    public void putFileUpload(File fileToUpload) {
        multiPart.bodyPart(new FormDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class,multiPart);
    }
    
    public String postFileUpload(File fileToUpload, String pwd) {
        WebResource resource = webResource;
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.getBodyParts().add(new FileDataBodyPart("filex", fileToUpload));
        formDataMultiPart.field("context", fileToUpload.getName(), MediaType.TEXT_PLAIN_TYPE);
        if (!pwd.isEmpty()) formDataMultiPart.field("dvsauth", pwd, MediaType.TEXT_PLAIN_TYPE);
        ClientResponse clientResponse = resource.accept(MediaType.TEXT_PLAIN_TYPE).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, formDataMultiPart);
        return clientResponse.getEntity(String.class);
    }
}