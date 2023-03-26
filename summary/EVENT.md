# 이벤트 발행하기

예제코드는 [여기](https://github.com/summerr0-0/spring/tree/main/src/main/java/com/example/spring/event) 

## 왜 이벤트 기반으로 코드를 짜야할까?

- 도메인의 강결합을 낮출 수 있다

- 강결합

  - 결제를 할 때 환불에도 결제정보를 추가 해야 한다.

  - 환불을 할 때에도 결제 상태를 변경해 주어야 한다.

  - **두 상태는 결합이 강하게 된 상태다.**
  - 이런 경우 두 로직이 계속 섞일 수 밖에 없다.


- 이벤트 사용 시
  - 결제 로직에서 결제 후 **결제 이벤트**를 발생한다.
    - 결제이벤트를 받으면 환불로직에서 환불에 결제정보를 추가한다.

  - 환불 로직에서 환불 후 **환불 이벤트**를 발생한다
    - 환불이벤트를 받으면 결제로직에서 결제상태를 변경한다.
    
  - 이런 식으로 환불과 결제를 구분지어서 개발할 수 있어 소스가 간편해진다.



## 이벤트 관련 구성 요소

- 이멘트를 도입할 때 **이벤트**, **이벤트 생성 주체,** **이벤트 디스패처**, **이벤드 핸들러** 를 구현한다.

- 이벤트 생성 주체가 이벤트를 발생시킨다.

- 이벤트 핸들러는 발생한 이벤트에 반응한다.

- 이벤트 디스패처가 이벤트를 전파한다.
  - 이벤트 생성주체는 이벤트 디스패처에게 이벤트를 전달한다.
  - 이벤트 디스패처는 이벤트를 처리할 수 있는 핸들러에게 이벤트를 전달한다.




## 스프링에서의 이벤트
- 스프링에서 제공하는 `ApplicationEventPublisher`와 `@EventListener` 를 이용해서 쉽게 이벤트 발행과 구독이 가능하다

- `ApplicationEventPublisher`
  -  Spring의 ApplicationContext가 상속하는 인터페이스 중 하나
  - 옵저버 패턴의 구현체로 이벤트 프로그래밍에 필요한 기능을 제공해준다


```java
   public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
      ...
   }

```


- `@EventListener`
  - 이벤트 리스너로 등록시켜준다.



## 적용해보기

- 회원가입 후 메시지 전송보내기.
  - 메시지 전송 로직과 회원가입 도메인이 섞이게 된다.
  - 후에 다른 기능이 추가된다면 코드가 복잡해진다.

- 가입 이벤트를 발행하고 가입 이벤트를 구독하면 된다.


### **이벤트 발생과 출판을 위한** `Events`

- ApplicationEventPublish가 제공하는 publishEvent를 이용해서 이벤트를 발생시킨다.


```java
public class Events {
    private static ApplicationEventPublisher publisher;

    static void setPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void raise(Object event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}
```




### **이벤트 설정을 위한** `EventsConfiguration`

- `Events`의 `setPublisher()` 에 이벤트 퍼블리셔를 전달하기 위한 스프링 설정 클래스

```java
@Configuration
public class EventsConfiguration {

    private final ApplicationContext applicationContext;

    public EventsConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public InitializingBean eventsInitializer() {
        return () -> Events.setPublisher(applicationContext);
    }
}
```


### **유저 가입 이벤트** `UserJoinedEvent`

- 이벤트 발생시 전달할 데이터들을 넣어주면 된다.

```java
public class UserJoinedEvent {
    private final String userId;
    private final String userName;

    public static UserJoinedEvent of(String userId, String userName) {
        return new UserJoinedEvent(userId, userName);
    }

    private UserJoinedEvent(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
  
 	 //getter 생략
}
```



### **이벤트를 처리할 핸들러** `UserJoinedEventHandler`

- 이벤트를 처리하는 핸들러를 생성한다.

- Hadler는 빈이 등록되어 있어야 한다

- 여기서는 간단하게 출력만 했지만 핸들러에서 다른 도메인에게 요청을 하면 된다.

- @EventListener에서 구독중인 이벤트가 `UserJoinedEvent.class` 인 것이다.
  - `UserJoinedEvent` 발생시 아래 로직 실행

```java
@Component
public class MessageHandler{
	@EventListener(UserJoinedEvent.class)
	public void handle(UserJoinedEvent event) {
    System.out.println(event.getUserName() + "에게 메시지 전송");
  }
}
```



### **이벤트가 발생하는** `UserController`

- 가입시 `Events.raise` 를 이용해 이벤트를 발생시킨다.1

- 이벤트는 `UserJoinedEvent` 다

- 이벤트를 발생하면 `UserJoinedEvent` 값을 지닌 핸들러가 실행된다.

```java
@RestController
public class UserController {
    @GetMapping("/join")
    public void join() {
        System.out.println("유저 가입");
        Events.raise(UserJoinedEvent.of("userId", "김포도"));
    }
}

```



# 트랜잭션

- 일련의 작업에서 트랜잭션을 보장해야 한다면 이벤트에 `@TransactionalEventListener` 를 사용하자
- 이벤트핸들러에 `@EventListener` 대신 `@TransactionalEventListener` 를 사용하면 된다
- 설정값
  - `AFTER_COMMIT` (기본값) - 트랜잭션이 성공적으로 마무리(commit)됬을 때 이벤트 실행
  - `AFTER_ROLLBACK` - 트랜잭션이 rollback 됬을 때 이벤트 실행
  - `AFTER_COMPLETION` - 트랜잭션이 마무리 됬을 때(commit or rollback) 이벤트 실행
  - `BEFORE_COMMIT` - 트랜잭션의 커밋 전에 이벤트 실행

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT , 
                            classes = UserJoinedEvent.class)
public void handle(UserJoinedEvent event) {
  //...
}
```
