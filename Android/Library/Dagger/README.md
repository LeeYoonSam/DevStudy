# Dagger

## @Module 의 @Binds, @Provides 차이
Dagger의 @Binds와 @Provides는 의존성 주입(Dependency Injection)을 위한 두 가지 다른 방식입니다.

### @Binds 방식
@Binds는 주로 인터페이스 구현체를 바인딩할 때 사용되는 메서드입니다. 

특징:
**간결성**
- 추상 메서드로 구현
- 구현체를 직접 반환
- 컴파일 타임에 Dagger가 자동으로 바인딩 생성

사용 조건:
- 구현체가 이미 생성자 주입이 가능해야 함
- 인터페이스나 추상 클래스의 구현체를 제공할 때 주로 사용

코드 예시:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsTopicRepository(
        topicsRepository: OfflineFirstTopicsRepository
    ): TopicsRepository
}
```

### @Provides 방식
@Provides는 직접 객체 생성 로직을 제공할 때 사용되는 메서드입니다. 

특징:
1. 복잡한 객체 생성
- 구체적인 객체 생성 로직 구현 가능
- 외부 라이브러리 객체나 복잡한 생성 과정이 필요한 객체에 적합
2. 유연성
- 생성자 주입이 불가능한 객체도 제공 가능
- 추가적인 설정이나 의존성 주입 가능

코드 예시:
```
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://example.com")
            .client(client)
            .build()
    }
}
```

### 주요 차이점

사용 상황:
- @Binds: 간단한 인터페이스 구현체 바인딩
- @Provides: 복잡한 객체 생성, 외부 라이브러리 객체

성능:
- @Binds: 컴파일 타임 바인딩, 성능 오버헤드 최소화
- @Provides: 런타임 시 객체 생성, 약간의 성능 오버헤드 존재

코드 복잡성:
- @Binds: 매우 간결하고 명확한 코드
- @Provides: 더 많은 로직과 코드 필요

### 결론
- 가능하면 @Binds 사용을 권장
- 복잡한 객체 생성이 필요한 경우 @Provides 사용
- 두 방식을 상황에 맞게 적절히 선택

