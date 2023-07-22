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
