# redis-login

## Stack
<img src="https://img.shields.io/badge/Java-007396.svg?&style=flat&logo=Java&logoColor=white" alt="Java"> <img src="https://img.shields.io/badge/Spring_Boot-6DB33F.svg?&style=flat&logo=SpringBoot&logoColor=white" alt="Spring Boot"> <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=flat&logo=redis&logoColor=white" alt="HTML5">

## Redis의 특징과 활용처
Redis는 인메모리 DB로서 보조 기억 장치에 데이터를 저장하는 RDB 등 다른 데이터베이스보다 데이터 접근 및 응답 속도가 매우 빠르다. 또한 복제(Replication) 아키텍처를 지원하여 고가용성을 제공하고 확장이 용이하다.

Redis는 매우 빠른 데이터 접근 속도를 갖지만, RAM에 저장하기 때문에 데이터 용량이 제한적이고 장애나 전원 공급 중단 시 데이터 유실 발생 가능성이 있다. 따라서, 다음과 같은 경우에 Redis를 사용하는 것이 좋다.
* 데이터 I/O가 자주 발생하는 경우
* 저장할 데이터의 최대 크기가 작거나 제한적인 경우
* 데이터가 유실되어도 치명적인 손실이 발생하지 않는 데이터를 다루는 경우

Redis는 RAM에 데이터를 저장하지만, snapshot이나 AOF(Append Only File) 방식으로 데이터를 영속화할 수 있다. 그럼에도 snapshot 방식 사용 시 주기적인 저장 시점 이후 장애가 발생하면 해당 시점 이후의 데이터가 유실된다는 문제가 있고, AOF 방식 사용 시에는 데이터 I/O 발생 시마다 저장해야 하므로 영속화를 사용하지 않거나 snapshot 방식 사용 시보다 성능상에 손실이 있다는 문제가 있다.

이러한 Redis 특성을 고려해 보았을 때 Redis는 주로 캐시로 활용된다. 캐싱을 통해서 빠른 응답이 필요한 서비스는 DB 접근 전이라도 데이터를 미리 가져올 수 있다. Redis의 주요 용도는 다음과 같다.
* 실시간 랭킹
* 세션 저장소
* 지리 데이터 연산 및 저장(Redis 3.2 버전 이후 지리 공간 데이터 타입들을 제공하기 때문)
* 머신 러닝 및 빅데이터 처리
  * 게임 내 사기 탐지, 실시간 입찰, 데이트 앱 등 매치 메이킹 연산
* 실시간 데이터 분석
  * 소셜 미디어 분석, 광고 타겟팅, 개인화 분석

## Spring Data Redis
스프링 데이터 레디스에서는 Redis 드라이버로 Lettuce와 Jedis를 지원하며, 별도의 설정이 없을 경우 Lettuce를 기본적으로 사용한다. Jedis가 자바 Redis 드라이버의 사실상 표준임에도 불구하고 Lettuce를 사용하는 이유를 스프링 프로젝트(구체적으로는 Spring Session에서)의 issues 내 답변에서 다음과 같이 설명한다.
(https://github.com/spring-projects/spring-session/issues/789)
* Jedis는 여러 스레드에서 하나의 Jedis 인스턴스를 공유하려고 할 때 `thread-safe`하지 않다.
 * 멀티 스레드 환경에서 Jedis를 사용하기 위해서는 커넥션 풀을 사용하여야 한다.
 * 커넥션 풀은 각각의 스레드가 연결 시마다 Jedis 인스턴스를 생성해야 하기 때문에 비용이 많이 든다.
 * 반면, Lettuce는 netty 기반이며 각각의 스레드들이 connection 인스턴스(StatefulRedisConnection)을 공유할 수 있다. 따라서, 멀티 스레드 애플리케이션도 하나의 connection으로 Redis 사용이 가능하다.

### RedisTemplate 설정
* `redisTemplate.setConnectionFactory(redisConnectionFactory());`
  * 사용할 RedisConnectionFactory 구현체(LettuceConnectionFactory, JedisConnectionFactory)를 설정한다.
* `redisTemplate.setEnableTransactionSupport(true);`
  * Redis를 스프링 트랜잭션에 참여시킬지 여부를 설정한다. 이때, 스프링 트랜잭션 매니저 사용이 활성화되어 있어야 한다.
* `redisTemplate.setExposeConnection(true);`
  * Redis connection을 RedisCallback 코드에 노출시킬지 여부를 설정한다.
* `redisTemplate.setEnableDefaultSerializer(true);`,
  `redisTemplate.setDefaultSerializer(new StringRedisSerializer());`
  * 각각, 기본 Redis serializer를 사용할지, 어떤 Redis serializer를 사용할지 설정한다.
* `redisTemplate.setBeanClassLoader(ClassLoader.getPlatformClassLoader());`
  * 다른 RedisSerializer를 사용하지 않을 때, 기본 JdkSerializationRedisSerializer로 사용할 class loader를 설정한다.
* `redisTemplate.setKeySerializer(new StringRedisSerializer());`
  * 사용할 key Redis serializer를 설정한다.
* `redisTemplate.setValueSerializer(new StringRedisSerializer());`
  * 사용할 value Redis serializer를 설정한다.
* `redisTemplate.setHashKeySerializer(new StringRedisSerializer());`
  * hash 자료 구조에 사용할 key Redis serializer를 설정한다.
  `redisTemplate.setHashValueSerializer(new StringRedisSerializer());`
  * hash 자료 구조에 사용할 value Redis serializer를 설정한다.
* `redisTemplate.setStringSerializer(new StringRedisSerializer());`
  * 사용할 문자열 Redis serializer를 설정한다.
* `redisTemplate.setScriptExecutor(new DefaultScriptExecutor<>(redisTemplate));`
  * 루아 스크립트 사용 시, 스크립트를 실행시킬 script executor를 설정한다.
