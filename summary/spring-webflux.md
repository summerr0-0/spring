# WebFlux

- Spring 5, Spring boot2 에서 추가됨

- 클라이언트, 서버에서 reactive 스타일의 어플리케이션개발을 도와준다

- **적은 수의 스레드로 동시성을 처리하기 위해서** 사용한다

- webflux가 빠르다기보단 **적은리소스(스레드)로 많은 트래픽을 감당할 수 있다** 

- **reactive-stack web framework**이며 **non-blocking**에 **reactive stream**을 지원합니다.



### reactive stack

- spring 5에서 기존의 Servlet Stack과 다른 Reactive 스택이 나옴
- **webflux란 Spring Reactive Stack의 Web Framework(Spring MVC 포지션) 역할을 함**
- 만약 **reactive stack과 servlet stack을 섞어 쓰면 mvc보다도 성능이 나오지 않는다**
  - spring security reactive
  - spring weflux
  - spring data reactive repositories 



### nonbloocking

- Sppring MVC
  - blocking 동기 방식
    - 1:1로 요청을 처리하기 때문에 트래필이 몰리면 많은 쓰레드가 생긴다
    - ThreadPoolHell 현상 (요청이 몰리면 스레드 요청이 계속 큐에 대기)
  - 쓰레드를 늘린다면?
    - CPU, 메모리가 충문하지만 쓰레드가 모자라 처리율 저하
    - 쓰레드를 과도하게 늘리면 메모리, CPU에 부하로 성능저하
    - CPU간 전환과정은 엄청난 부하가 있음 (컨텍스트 스위칭)
    - 즉 쓰레드를 무조건 늘린다고 문제를 해결할 수 있는 것이 아니다

- webFlux
  - Event-Driven, 
  - Asynchronous Non-blocking
  - 요청이 처리될 때까지 기다리지 않기 때문에 리소스 낭비를 줄일 수 있음





### Reactive Programming

- 리액티브는 이벤트를 중점으로 둔 프로그래밍 모델을 말한다
- non-blocking이 reactive인 이유는 blocking되지 않고 작업이 완료된다, 데이터 사용가능 이라는 알림에 반응하기 때ㅑ문

- reactive stream
  - `Asyncronous` , `non-blocking` 으로 작동하는 stream
  - 기능이 기존것보다 많은 stream임
- publisher(웹 클라이언트, 데이터베이스) 에서 변경이 생기면 subscriber에 변경된 데이터들을 stream으로 전달
- **이 stream으로 프로그래밍 하는 패러다임이 reactive programming**



- backpressure
  - `Subscriber`로 들어오는 stream양을 조정 적은 컴퓨팅 자원으로 일을 처리하기 가능한 정도만 받기
    - pub이 계속 데이터를 주면 sub이 받을 수 있는 양만 받는 것



- reactor 와 rxjava
  - jdk9스펙의 reactive stream을 구현한 라이브러리



### Mono와 Flux

- `Mono`와 `Flux` 는 `Reactor` 객체다 

- `Flux` 는 0~N개의 데이터 전달

- `Mono` 는 0~1개의 데이터 전달

  - 명백히 하나의 결과값만 받을 경우 사용한다.

- 여러 스트림을 하나의 결과를 모아줄 때 `Mono`

- 각각의 `Mono`를 합쳐 하나의 여러 값을 처리할 때 `Flux` 사용

  