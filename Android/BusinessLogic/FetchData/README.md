# 데이터를 가져오기 위한 네트워크 요청을 어떻게 처리하고, 효율성과 신뢰성을 위해 어떤 라이브러리나 기술을 사용합니까?

안드로이드 개발에서 [**Retrofit**](https://square.github.io/retrofit/)과 [**OkHttp**](https://square.github.io/okhttp/)는 네트워크 요청을 처리하는 데 일반적으로 사용되는 라이브러리입니다. Retrofit은 선언적 인터페이스를 제공하여 API 상호작용을 단순화하는 반면, OkHttp는 그 기반이 되는 HTTP 클라이언트 역할을 하며 커넥션 풀링(connection pooling), 캐싱(caching), 효율적인 통신을 제공합니다.

-----

## Retrofit을 사용한 네트워크 요청

Retrofit은 HTTP 요청을 깔끔하고 타입 세이프(type-safe)한 API 인터페이스로 추상화합니다. JSON 응답을 코틀린이나 자바 객체로 변환하기 위해 Gson이나 Moshi와 같은 직렬화 라이브러리와 원활하게 작동합니다.

Retrofit을 사용하여 데이터를 가져오는 단계는 다음과 같습니다.

### 1. API 인터페이스 정의

어노테이션을 사용하여 API 엔드포인트와 HTTP 메서드를 선언합니다.

```kotlin
interface ApiService {
    @GET("data")
    suspend fun fetchData(): Response<DataModel>
}
```

### 2. Retrofit 인스턴스 설정

기본 URL(base URL)과 JSON 직렬화를 위한 컨버터 팩토리(converter factory)로 Retrofit을 설정합니다.

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com/")
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())) // kotlinx.serialization 사용 예시
    .build()

val apiService = retrofit.create(ApiService::class.java)
```

### 3. 네트워크 호출 실행

코루틴을 사용하여 API를 비동기적으로 호출합니다.

```kotlin
viewModelScope.launch {
    try {
        val response = apiService.fetchData()
        if (response.isSuccessful) {
            val data = response.body()
            Log.d(TAG, data.toString())
        } else {
            Log.d(TAG, "Error: ${response.code()}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
```

-----

## OkHttp를 사용한 사용자 정의 HTTP 요청

OkHttp는 헤더, 캐싱 등을 세밀하게 제어하며 HTTP 요청을 직접적으로 관리하는 접근 방식을 제공합니다.

```kotlin
val client = OkHttpClient()

val request = Request.Builder()
    .url("https://api.example.com/data")
    .build()

client.newCall(request).enqueue(object : Callback {
    override fun onFailure(call: Call, e: IOException) {
        e.printStackTrace()
    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            Log.d(TAG, response.body?.string())
        } else {
            Log.d(TAG, "Error: ${response.code}")
        }
    }
})
```

-----

## Retrofit과 OkHttp 통합하기

Retrofit은 내부적으로 OkHttp를 HTTP 클라이언트로 사용합니다. 로깅, 인증 또는 캐싱을 처리하기 위해 인터셉터(interceptor)를 추가하여 OkHttp의 동작을 사용자 정의할 수 있습니다.

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        // 모든 요청에 인증 토큰 헤더 추가
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer your_token")
            .build()
        chain.proceed(request)
    }
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com/")
    .client(okHttpClient) // 사용자 정의 OkHttpClient 설정
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

이 설정을 사용하면 인증 토큰을 첨부하거나 디버깅 목적으로 상세 로깅을 활성화하는 등의 기능을 추가할 수 있습니다.

-----

## 요약: Retrofit과 OkHttp

Retrofit과 OkHttp는 함께 안드로이드에서 네트워크 요청을 처리하기 위한 견고한 프레임워크를 제공합니다. Retrofit은 HTTP 호출의 생성 및 실행을 단순화하는 반면, OkHttp는 네트워크 동작을 사용자 정의할 수 있는 유연성을 제공합니다. 두 라이브러리를 함께 활용하면 안드로이드 애플리케이션에서 효율적이고 유지보수하기 쉬운 네트워킹을 보장할 수 있습니다.

-----

## 💡 프로 팁: OkHttp Authenticator와 Interceptor를 사용하여 OAuth 토큰을 갱신하는 방법

OAuth로 보안된 API로 작업할 때, 토큰 만료 및 갱신 시나리오를 처리하는 것은 일반적입니다. OkHttp는 토큰을 가로채고 갱신하기 위한 두 가지 주요 메커니즘인 [**Authenticator**](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Authenticator.html)와 [**Interceptor**](https://square.github.io/okhttp/features/interceptors/)를 제공합니다. 둘 다 다른 목적을 가지며 애플리케이션의 특정 요구 사항에 따라 사용될 수 있습니다.

### OkHttp Authenticator 사용하기

OkHttp의 `Authenticator` 인터페이스는 토큰 만료와 같은 **인증 문제**를 처리하도록 특별히 설계되었습니다. 서버가 **401 Unauthorized 상태 코드**로 응답하면, Authenticator는 업데이트된 인증 자격 증명이 포함된 새 요청을 제공하기 위해 호출됩니다.

다음은 토큰을 갱신하기 위해 Authenticator를 구현하는 방법입니다.

```kotlin
class TokenAuthenticator(
    private val tokenProvider: TokenProvider // 토큰 제공 및 갱신 책임이 있는 커스텀 클래스
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // 토큰 제공자로부터 새 토큰을 가져옴
        val newToken = tokenProvider.refreshToken() ?: return null // 갱신 실패 시 null 반환하여 종료

        // 새 토큰으로 요청 재시도
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }
}
```

`TokenProvider`는 일반적으로 갱신 엔드포인트에 동기 네트워크 호출을 하여 토큰을 갱신하는 책임을 지는 사용자 정의 클래스입니다.

Authenticator를 사용하려면 `OkHttpClient`에 설정합니다.

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .authenticator(TokenAuthenticator(tokenProvider))
    .build()
```

### OkHttp Interceptor 사용하기

`Interceptor`는 토큰 추가 및 갱신 로직을 처리할 수 있는 더 유연한 접근 방식입니다. Authenticator와 달리, Interceptor는 처리되기 전에 요청이나 응답을 검사하고 수정할 수 있습니다.

일반적인 구현은 401 상태 코드에 대한 응답을 확인하고 토큰을 직접 갱신하는 것을 포함합니다.

```kotlin
class TokenInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. 요청에 토큰 추가
        var request = chain.request().newBuilder()
            .header("Authorization", "Bearer ${tokenProvider.getToken()}")
            .build()

        // 2. 요청 진행
        var response = chain.proceed(request)

        // 3. 토큰 만료 여부 확인 (401 코드)
        if (response.code == 401) {
            synchronized(this) { // 동시성 문제를 피하기 위한 동기화 블록
                // 토큰 갱신
                val newToken = tokenProvider.refreshToken() ?: return response // 갱신 실패 시 원래 응답 반환

                // 새 토큰으로 요청 재시도
                request = request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
                
                // 이전 응답을 닫고 새 요청 진행
                response.close()
                return chain.proceed(request)
            }
        }
        return response
    }
}
```

인터셉터를 `OkHttpClient`에 설정합니다.

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(TokenInterceptor(tokenProvider))
    .build()
```

### Authenticator와 Interceptor의 주요 차이점

| 구분 | Authenticator | Interceptor |
| --- | --- | --- |
| **목적** | 주로 401 응답으로 트리거되는 **인증 문제** 처리를 위해 설계됨. | 요청과 응답 처리 모두에 대한 더 **세분화된 제어**를 제공함. |
| **자동 트리거** | 401 응답에 대해 **자동으로 호출됨.** | 이러한 시나리오를 감지하고 처리하기 위해 **수동 로직이 필요함.** |
| **사용 사례** | 간단한 인증 문제 처리에 사용. | 복잡한 요청/응답 핸들링이 필요한 시나리오에 사용. |

#### 요약: 토큰 갱신

OAuth 토큰을 갱신하기 위해, **Authenticator**는 서버에서 트리거된 401 문제를 자동으로 처리하는 데 적합하며, **Interceptor**는 사용자 정의 토큰 관리에 더 큰 유연성을 제공합니다. 둘 중 어느 것을 선택할지는 애플리케이션의 특정 요구 사항과 복잡성에 따라 달라집니다. 두 접근 방식 모두 API 호출을 중단하지 않고 투명하게 토큰을 갱신하여 원활한 사용자 경험을 보장합니다.

-----

## 💡 프로 팁: Retrofit CallAdapter란 무엇인가?

Retrofit의 [**CallAdapter(콜 어댑터)**](https://square.github.io/retrofit/2.x/retrofit/retrofit2/CallAdapter.html) 는 개발자가 Retrofit API 메서드의 **반환 타입을 수정**할 수 있게 해주는 추상화입니다. 기본적으로 Retrofit API 메서드는 동기적 또는 비동기적으로 실행될 수 있는 HTTP 요청을 나타내는 `Call<T>` 객체를 반환합니다. 그러나 CallAdapter를 사용하면 이 기본 반환 타입을 `LiveData`, `Flow`, RxJava 타입 또는 사용자 정의 타입과 같은 다른 타입으로 변환할 수 있습니다.

CallAdapter는 Retrofit이 반응형 프로그래밍이나 코루틴 기반 접근 방식과 같은 안드로이드 개발에서 사용되는 다양한 프로그래밍 패러다임이나 라이브러리에 적응할 수 있도록 만드는 데 중요한 역할을 합니다.

### CallAdapter 작동 방식

Retrofit은 `CallAdapter.Factory`를 사용하여 `CallAdapter` 인스턴스를 생성합니다. CallAdapter는 런타임에 `Call<T>` 객체를 원하는 타입으로 변환하는 책임을 집니다. 이 과정은 Retrofit이 API 인터페이스에 대한 프록시를 생성할 때 발생합니다.

### Retrofit의 기본 CallAdapter

기본적으로 Retrofit은 `Call<T>` 객체를 직접 반환하는 CallAdapter를 포함합니다. 다른 타입을 원한다면 적절한 라이브러리를 포함하거나 사용자 정의 CallAdapter를 작성해야 합니다.

### 예시: Retrofit과 코루틴 CallAdapter 사용

코틀린에서 코루틴 어댑터는 Retrofit API 인터페이스에서 `suspend` 함수를 사용할 수 있게 합니다. 어댑터는 `Call<T>`를 반환하는 대신, 메서드를 실제 결과를 반환하거나 오류에 대한 예외를 던지는 `suspend` 함수로 변환합니다.

Retrofit과 `suspend` 제어자를 사용하는 방법은 다음과 같습니다.

```kotlin
interface ExampleApi {
    @GET("users")
    suspend fun getUsers(): List<User> // Call<List<User>> 대신 List<User>를 직접 반환
}
```

이 경우, Retrofit은 내부적으로 CallAdapter를 사용하여 `Call<List<User>>`를 `List<User>`를 반환하는 `suspend` 함수로 변환합니다.

### 예시: 사용자 정의 CallAdapter (LiveData)

`LiveData`를 Retrofit과 통합하려면 사용자 정의 CallAdapter를 만들 수 있습니다.

```kotlin
// LiveDataCallAdapter.kt
class LiveDataCallAdapter<T>(private val responseType: Type) : CallAdapter<T, LiveData<T>> {
    override fun responseType() = responseType

    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {
            override fun onActive() { // LiveData가 활성화될 때
                super.onActive()
                call.enqueue(object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        postValue(response.body()) // postValue로 값 업데이트
                    }
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        // 실패 처리
                        postValue(null)
                    }
                })
            }
        }
    }
}

// LiveDataCallAdapterFactory.kt
class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) return null
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        return LiveDataCallAdapter<Any>(observableType)
    }
}
```

사용자 정의 어댑터를 사용하려면 Retrofit 빌더에 팩토리를 추가합니다.

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addCallAdapterFactory(LiveDataCallAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

이제 API 메서드가 `LiveData`를 직접 반환할 수 있습니다.

```kotlin
interface ExampleApi {
    @GET("users")
    fun getUsers(): LiveData<List<User>>
}
```

#### 요약: CallAdapter

Retrofit의 CallAdapter는 기본 `Call<T>` 반환 타입을 `LiveData`, `Flow`, RxJava 옵저버블 또는 사용자 정의 타입 등 다양한 타입으로 변환하여 유연성을 제공합니다. 이를 통해 Retrofit을 선호하는 아키텍처나 라이브러리와 원활하게 통합할 수 있습니다. Retrofit은 `Call<T>` 및 코루틴을 위한 기본 CallAdapter를 포함하지만, 사용자 정의 어댑터를 사용하면 특정 요구 사항에 맞는 고급 사용 사례를 구현할 수 있습니다. 사용자 정의 CallAdapter에 대한 더 자세한 정보는 [Modeling Retrofit Responses With Sealed Classes and Coroutines](https://medium.com/proandroiddev/modeling-retrofit-responses-with-sealed-classes-and-coroutines-9d6302077dfe)를 확인해 보세요.

-----

## Q. 앱이 여러 동시 API 요청을 하고 그 결과를 결합하여 UI를 업데이트해야 합니다. Retrofit과 코루틴을 사용하여 이를 어떻게 효율적으로 달성할 수 있나요?

Retrofit과 코틀린 코루틴을 사용하면 여러 동시 API 요청을 효율적이고 간결하게 처리할 수 있습니다. 핵심은 코루틴의 **`async`** 와 **`await`** 를 사용하여 여러 작업을 병렬로 실행하고, 모든 작업이 완료된 후 그 결과들을 결합하는 것입니다.

이 방식은 모든 요청이 완료될 때까지 걸리는 총 시간이 **개별 요청 시간의 합이 아닌, 가장 오래 걸리는 단일 요청 시간**에 의해 결정되므로 매우 효율적입니다.

---
### 1. 핵심 전략: 코루틴의 `async`와 `await` 활용

* **`async { ... }`**: 새로운 코루틴을 시작하고 `Deferred<T>` 객체를 즉시 반환합니다. `Deferred`는 미래의 결과값을 약속하는 객체입니다. `async`를 여러 번 호출하면 각각의 네트워크 요청이 **서로를 기다리지 않고 동시에(병렬로) 시작**됩니다.
* **`await()`**: `Deferred` 객체에 대해 이 메서드를 호출하면, 해당 코루틴의 결과가 준비될 때까지 현재 코루틴을 일시 중단(suspend)하고 기다립니다. 여러 `Deferred` 객체에 대해 `await()`를 호출하면 모든 병렬 작업이 완료될 때까지 기다릴 수 있습니다.

---
### 2. 구현 단계

#### 2.1. 1단계: Retrofit API 인터페이스 정의
먼저, 필요한 각 API 호출을 Retrofit 인터페이스에 `suspend` 함수로 정의합니다.

```kotlin
interface MyApiService {
    @GET("user/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): UserProfile

    @GET("user/{id}/posts")
    suspend fun getUserPosts(@Path("id") userId: String): List<Post>

    @GET("user/{id}/friends")
    suspend fun getUserFriends(@Path("id") userId: String): List<Friend>
}
```

#### 2.2. 2단계: ViewModel에서 동시 요청 실행 및 결과 결합
`ViewModel` 내에서 `viewModelScope`를 사용하여 코루틴을 시작하고, 여러 API 요청을 `async`로 동시에 호출한 후, `await()`를 통해 모든 결과를 기다려 결합합니다.

```kotlin
// 예시 데이터 클래스
data class UserProfile(val name: String, val email: String)
data class Post(val id: Int, val title: String)
data class Friend(val id: Int, val name: String)
data class UserDashboard(val profile: UserProfile, val posts: List<Post>, val friends: List<Friend>)

// ViewModel
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val apiService: MyApiService
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<Result<UserDashboard>>(Result.Loading)
    val dashboardState: StateFlow<Result<UserDashboard>> = _dashboardState.asStateFlow()

    fun fetchDashboardData(userId: String) {
        viewModelScope.launch {
            _dashboardState.value = Result.Loading
            try {
                // 1. 각 API 요청을 async로 동시에 시작합니다.
                val profileDeferred = async { apiService.getUserProfile(userId) }
                val postsDeferred = async { apiService.getUserPosts(userId) }
                val friendsDeferred = async { apiService.getUserFriends(userId) }

                // 2. await()을 호출하여 모든 요청이 완료될 때까지 기다립니다.
                val profile = profileDeferred.await()
                val posts = postsDeferred.await()
                val friends = friendsDeferred.await()

                // 3. 모든 결과를 결합하여 UI 상태를 업데이트합니다.
                _dashboardState.value = Result.Success(UserDashboard(profile, posts, friends))

            } catch (e: Exception) {
                // 4. 요청 중 하나라도 실패하면 예외를 처리합니다.
                _dashboardState.value = Result.Error(e)
            }
        }
    }
}
```
*(위 예시에서 `Result`는 로딩, 성공, 실패 상태를 나타내는 일반적인 `sealed class` 또는 `sealed interface` 라고 가정합니다.)*

#### 2.3. 3단계: UI 레이어에서 데이터 관찰 및 업데이트
Activity나 Fragment에서 ViewModel의 상태를 관찰하고, 데이터가 준비되면 UI를 업데이트합니다.

```kotlin
// DashboardFragment.kt 내
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.dashboardState.collect { result ->
                when (result) {
                    is Result.Loading -> showLoading()
                    is Result.Success -> updateUiWithData(result.data) // 모든 데이터가 결합된 객체로 UI 업데이트
                    is Result.Error -> showError(result.exception)
                }
            }
        }
    }
}
```

---
### 3. 이 방식의 주요 장점

#### 3.1. 효율성 (Efficiency) ⚡️
* 여러 네트워크 요청이 **병렬로 실행**되므로, 순차적으로 실행할 때보다 총 대기 시간이 크게 줄어듭니다. 전체 소요 시간은 개별 요청 시간의 합이 아닌, 가장 오래 걸리는 단일 요청 시간에 의해 결정됩니다.

#### 3.2. 코드 가독성 및 간결성 (Readability and Conciseness) 📖
* 콜백(callback)을 중첩해서 사용하거나(Callback Hell), 여러 데이터 스트림을 수동으로 결합하는 복잡한 로직 없이도, 마치 동기 코드처럼 순차적으로 결과를 기다리는 코드를 작성할 수 있어 매우 간결하고 읽기 쉽습니다.

#### 3.3. 구조화된 동시성 (Structured Concurrency) 🛡️
* `viewModelScope` 내에서 실행된 `async` 작업들은 서로 연결되어 있습니다. 만약 여러 요청 중 **하나라도 실패하여 예외가 발생하면, `await()` 호출 시 해당 예외가 던져지고 `catch` 블록에서 처리됩니다.** 이때 `viewModelScope`는 아직 실행 중인 다른 `async` 작업들을 **자동으로 취소**시켜 불필요한 네트워크 요청이나 리소스 낭비를 방지합니다.

---
### 4. 결론

**Retrofit과 코루틴의 `async-await` 패턴**을 함께 사용하는 것은 여러 API 요청을 동시에 처리하고 그 결과를 안전하게 결합하는 현대적인 안드로이드 개발의 표준적이고 가장 효율적인 방법입니다. 이는 코드의 복잡성을 낮추고, 성능을 최적화하며, 오류 처리까지 간결하게 만들어 견고한 애플리케이션을 구축하는 데 큰 도움이 됩니다.


## Q. API 실패를 처리하고 재시도 메커니즘을 어떻게 구현하나요?

API 요청은 네트워크 불안정, 서버 오류, 데이터 형식 불일치 등 다양한 이유로 실패할 수 있습니다. 따라서 견고한 애플리케이션을 만들기 위해서는 이러한 실패를 적절히 처리하고, 사용자에게 재시도 옵션을 제공하는 메커니즘을 구현하는 것이 매우 중요합니다. Retrofit과 코루틴을 사용하는 현대적인 안드로이드 개발 환경에서의 처리 전략은 다음과 같습니다.

---
### 1. API 실패 처리 전략

#### 1.1. 실패 유형 파악
먼저 API 요청 실패에는 어떤 종류가 있는지 이해해야 합니다.

* **네트워크 오류:** 인터넷 연결 없음, DNS 문제, 타임아웃 등. `Retrofit`과 `OkHttp`에서는 주로 `IOException`으로 나타납니다.
* **HTTP 오류 (서버 응답 오류):** 서버가 응답은 했지만, 성공 상태 코드(2xx)가 아닌 오류 코드(예: 4xx 클라이언트 오류, 5xx 서버 오류)를 반환하는 경우입니다.
* **파싱(Parsing) 오류:** 서버가 성공 코드(2xx)와 함께 응답했지만, JSON 본문의 형식이 코틀린 데이터 클래스와 일치하지 않아 직렬화 라이브러리(예: `kotlinx.serialization`, `Moshi`)가 역직렬화(deserialization)에 실패하여 예외를 발생시키는 경우입니다.

#### 1.2. `try-catch`를 이용한 예외 처리
코루틴 `suspend` 함수를 사용한 API 호출은 `try-catch` 블록으로 감싸는 것이 예외 처리의 기본입니다. 네트워크 오류나 파싱 오류는 이 블록에서 `Exception`으로 잡을 수 있습니다.

```kotlin
// ViewModel 내에서
viewModelScope.launch {
    _uiState.value = Result.Loading
    try {
        val data = apiService.fetchData() // suspend 함수 호출
        _uiState.value = Result.Success(data)
    } catch (e: Exception) {
        // IOException, SerializationException 등 대부분의 예외가 여기서 잡힘
        _uiState.value = Result.Error(e)
    }
}
```

#### 1.3. `Response<T>`를 이용한 HTTP 오류 코드 처리
`try-catch`만으로는 4xx, 5xx와 같은 HTTP 오류를 잡을 수 없습니다 (예외가 아닌 응답이므로). 이를 처리하려면 Retrofit 인터페이스의 반환 타입을 `Response<T>`로 감싸는 것이 좋습니다.

```kotlin
// API 인터페이스
interface ApiService {
    @GET("data")
    suspend fun fetchData(): Response<MyData> // 반환 타입을 Response<T>로 감쌈
}

// ViewModel 내에서 처리
viewModelScope.launch {
    _uiState.value = Result.Loading
    try {
        val response = apiService.fetchData()
        if (response.isSuccessful) {
            // 성공 (2xx 상태 코드)
            val data = response.body() // 실제 데이터는 body()에 있음
            _uiState.value = Result.Success(data)
        } else {
            // 실패 (4xx, 5xx 상태 코드)
            val errorCode = response.code()
            val errorMsg = response.errorBody()?.string() ?: "알 수 없는 오류"
            _uiState.value = Result.Error(Exception("API Error Code: $errorCode, Message: $errorMsg"))
        }
    } catch (e: Exception) {
        // 네트워크 연결 실패 등 통신 자체의 예외
        _uiState.value = Result.Error(e)
    }
}
```

#### 1.4. UI 상태 표현
ViewModel은 API 요청의 결과를 `Loading`, `Success`, `Error`와 같은 상태로 모델링하여 UI에 전달하는 것이 좋습니다. 이를 위해 `Sealed Class`나 `Sealed Interface`를 사용하는 것이 일반적입니다.

```kotlin
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val exception: Throwable) : UiState<Nothing>
}
```

---
### 2. 재시도(Retry) 메커니즘 구현

#### 2.1. 사용자 주도 재시도 (가장 일반적인 방식)
가장 일반적이고 직관적인 방법은 사용자에게 재시도 옵션을 제공하는 것입니다.

1.  **오류 상태 UI 표시:** ViewModel의 UI 상태가 `Error`일 때, UI(Activity/Fragment)는 오류 메시지와 함께 "재시도" 버튼을 표시합니다.
2.  **재시도 요청:** 사용자가 "재시도" 버튼을 클릭하면, ViewModel의 데이터 요청 함수를 다시 호출합니다.

```kotlin
// Fragment 또는 Activity 내
// ...
// viewModel.uiState를 관찰하는 코드 블록 내에서 ...
is Result.Error -> {
    errorView.isVisible = true // 오류 메시지와 재시도 버튼을 포함하는 뷰
    retryButton.setOnClickListener {
        viewModel.fetchData() // 데이터 로드 함수 재호출
    }
}
// ...
```

#### 2.2. 자동 재시도 (고급 방식)

일시적인 네트워크 문제와 같이 자동으로 재시도해도 괜찮은 특정 실패 상황에 대해 자동 재시도 로직을 구현할 수 있습니다.

* **코틀린 Flow의 `retry` 연산자 활용 (권장):**
    API 호출 로직을 `Flow`로 감싸면, `retry` 또는 `retryWhen` 연산자를 사용하여 선언적으로 재시도를 구현할 수 있습니다.
    ```kotlin
    // Repository 또는 UseCase에서
    fun fetchDataWithRetry(): Flow<MyData> = flow {
        emit(apiService.fetchData())
    }.retry(retries = 3) { cause ->
        // IOException일 경우에만 최대 3번까지 재시도
        cause is IOException
    }
    ```
    **지수적 백오프(Exponential Backoff)** 와 결합하면 서버에 부담을 주지 않으면서 더 안정적인 재시도를 구현할 수 있습니다. (예: 1초, 2초, 4초 간격으로 재시도)

* **OkHttp Interceptor 활용:**
    `OkHttp`의 `Interceptor`를 사용하여 네트워크 레벨에서 재시도를 구현할 수도 있습니다. 특정 HTTP 응답 코드(예: 503 Service Unavailable)를 받거나 `IOException`이 발생했을 때, 인터셉터가 요청을 몇 차례 다시 시도하도록 만들 수 있습니다. 이는 앱의 모든 네트워크 요청에 일괄적으로 적용할 수 있다는 장점이 있습니다.

---
### 3. 결론

견고한 API 실패 처리는 다음과 같은 다층적 접근 방식을 통해 이루어집니다.

1.  **`try-catch`** 블록으로 네트워크 및 파싱 예외를 잡습니다.
2.  **`Response<T>`** 래퍼를 사용하여 HTTP 상태 코드를 확인하고 서버 오류를 처리합니다.
3.  **`Sealed Class/Interface`** 를 사용하여 로딩, 성공, 실패 상태를 명확하게 모델링하고 UI에 전달합니다.
4.  사용자에게 **명시적인 재시도 옵션**을 제공하여 최상의 사용자 경험을 보장합니다.
5.  필요에 따라 **`Flow`의 `retry` 연산자**나 **`OkHttp Interceptor`** 를 사용하여 일시적인 오류에 대한 자동 재시도 로직을 구현하여 앱의 안정성을 더욱 높입니다.