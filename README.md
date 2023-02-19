# Spring Application에 등록된 API 리소스를 Java Reflection으로 조회

## 개요
Controller annotation을 소유하고 있는 bean을 대상으로 어떤 API 리소스를 가지고 있는지 조회하는 예제 코드입니다.

## 조회 대상 데이터 
아래 데이터들을 확인할 수 있습니다.
- Global URI Path (Class Level RequestMapping URI)
- URI Path (Method Level RequestMapping URI)
- HTTP Method (Method Level RequestMethod)
- Class Name(Simple)
- Class Name(fqcn, fullly qualified class name)
- API Method Name
- is Rest (Controller or RestController) 


## 데이터 조회 시점
이벤트를 사용하여 애플리케이션이 사용자의 요청을 받을 준비가 되는 순간 애플리케이션에 등록된 리소스를 조회합니다. 
```java
@EventListener(ApplicationReadyEvent.class)
```

## 데이터 구조
```json
[
    {
        "typeUri":"/dummy",
        "methodUri":"/test2",
        "uri":"/dummy/test2",
        "httpMethod":"GET",
        "className":"RestController2",
        "fullClassPathName":"com.example.demo.dummy.RestController2",
        "methodName":"rest",
        "rest":true
    },
    {
        "typeUri":"/dummy",
        "methodUri":"/test3",
        "uri":"/dummy/test3",
        "httpMethod":"DELETE",
        "className":"RestController3",
        "fullClassPathName":"com.example.demo.dummy.RestController3",
        "methodName":"rest",
        "rest":true
    },
    {
        "typeUri":"/dummy",
        "methodUri":"/test3",
        "uri":"/dummy/test3",
        "httpMethod":"GET",
        "className":"RestController3",
        "fullClassPathName":"com.example.demo.dummy.RestController3",
        "methodName":"rest",
        "rest":true
    }
]
```

## 사용 예제
Git clone 후 Application 구동 시 IDE console에서 확인하실 수 있습니다.
ResourceCollector.java 파일이 해당 기능을 수행하며, 브라우저에서 /call api를 호출하면 example 데이터를 확인하실  수 있습니다.  