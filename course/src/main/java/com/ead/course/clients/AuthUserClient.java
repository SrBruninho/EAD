package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilService;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class AuthUserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;


    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable){
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;
        String url = REQUEST_URL_AUTHUSER + utilsService.createURLGetAllUserByCourse( courseId, pageable );
        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);

        try{
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};
            result = restTemplate.exchange( url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch ( HttpStatusCodeException e){
            log.error("Error request /users {}", e);
        }
        log.info("Ending request /users courseId {}", courseId);

        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById( UUID userId ){
        String url = REQUEST_URL_AUTHUSER + utilsService.createURLgetOneUserById( userId );
        return restTemplate.exchange( url, HttpMethod.GET, null, UserDTO.class );
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + utilsService.createURLPostSubscriptionUserInCourse( userId );
        var courseUserDTO = new CourseUserDTO();

        courseUserDTO.setUserId(userId);
        courseUserDTO.setCourseId(courseId);
        restTemplate.postForObject( url, courseUserDTO, String.class);
    }
}
