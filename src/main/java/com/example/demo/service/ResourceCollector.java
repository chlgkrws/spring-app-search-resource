package com.example.demo.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ResourceCollector {

    private List<ApiResource> resources;            // API 리소스 리스트

    private final ApplicationContext applicationContext;

    @EventListener(ApplicationReadyEvent.class)
    private void run() {
        ArrayList<ApiResource> findResource = new ArrayList<>();

        // 전체 Bean 중 Controller annotation을 가진 bean 조회
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Controller.class);

        beans.forEach((beanName, bean) -> {
            Class<?> userClass = ClassUtils.getUserClass(bean);                   // search class name without EnhancerBySpringCGLIB
            String className = userClass.getSimpleName();
            String fcpn = userClass.getName();

            // RestController 인지 확인
            RestController existRestController = userClass.getAnnotation(RestController.class);
            boolean isRest = existRestController != null;

            Method[] methods = bean.getClass().getDeclaredMethods();

            // Per method
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                Annotation[] methodAnnotations = method.getDeclaredAnnotations();

                String methodName = method.getName();

                // Per annotation of method
                for (int j = 0; j < methodAnnotations.length; j++) {
                    Annotation methodAnnotation = methodAnnotations[j];
                    RequestMapping methodRequestMapping = AnnotationUtils.getAnnotation(methodAnnotation, RequestMapping.class);

                    // Found api resource
                    // GetMapping, PostMapping, PutMapping ... 등 비슷한 타입의 어노테이션에 대해 동일한 액션로직이 있는지 찾아보기
                    if (methodRequestMapping != null) {
                        Class<? extends Annotation> annotationType = methodAnnotation.annotationType();

                        String[] unitURIs = new String[]{};
                        if (annotationType == GetMapping.class) {
                            GetMapping annotation = method.getAnnotation(GetMapping.class);
                            unitURIs = annotation.value();

                        } else if (annotationType == PostMapping.class) {
                            PostMapping annotation = method.getAnnotation(PostMapping.class);
                            unitURIs = annotation.value();

                        } else if (annotationType == PutMapping.class) {
                            PutMapping annotation = method.getAnnotation(PutMapping.class);
                            unitURIs = annotation.value();

                        } else if (annotationType == DeleteMapping.class) {
                            DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
                            unitURIs = annotation.value();

                        } else {
                            unitURIs = methodRequestMapping.value();
                        }

                        // api uri (리스트 형태)
                        for (String unitURI : unitURIs) {
                            RequestMethod[] requestMethods = methodRequestMapping.method();

                            // RequestMapping으로 HttpMethod를 여러개 지정한 경우 대비
                            for (RequestMethod unitMethod : requestMethods) {

                                // Class level uri가 존재하는지 확인
                                RequestMapping requestMapping = userClass.getAnnotation(RequestMapping.class);
                                if (requestMapping != null) {
                                    String[] classRequestMappings = requestMapping.value();
                                    // Class level에 단순히 RequestMapping만 지정한 경우 (uri 작성하지 않은 경우 대비)
                                    if (classRequestMappings.length == 0) {
                                        classRequestMappings = new String[]{""};
                                    }
                                    for (String classUri : classRequestMappings) {
                                        ApiResource resource = ApiResource.builder()
                                                .typeUri(classUri)
                                                .methodUri(unitURI)
                                                .httpMethod(unitMethod.name())
                                                .uri(classUri + unitURI)
                                                .fullClassPathName(fcpn)
                                                .className(className)
                                                .methodName(methodName)
                                                .isRest(isRest)
                                                .build();

                                        findResource.add(resource);
                                    }

                                } else {
                                    ApiResource resource = ApiResource.builder()
                                            .methodUri(unitURI)
                                            .uri(unitURI)
                                            .httpMethod(unitMethod.name())
                                            .fullClassPathName(fcpn)
                                            .className(className)
                                            .methodName(methodName)
                                            .isRest(isRest)
                                            .build();

                                    findResource.add(resource);
                                }
                            }
                        }
                    }
                }
            }
        });

        this.resources = Collections.unmodifiableList(findResource);
        System.out.println("this.resources = " + this.resources);
    }

    public List<ApiResource> getResources() {
        return this.resources;
    }


    @Builder
    @Getter
    @ToString
    public static class ApiResource {
        private String typeUri;
        private String methodUri;
        private String uri;
        private String httpMethod;
        private String className;
        private String fullClassPathName;
        private String methodName;
        private boolean isRest;
    }
}