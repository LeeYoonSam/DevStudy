# JSON 형식을 객체로 직렬화하는 방법

## 서론: 안드로이드 개발과 JSON 직렬화

**JSON을 객체로 직렬화(serializing)** 하는 것은 현대 안드로이드 앱이 원격 서버(REST API, GraphQL 엔드포인트, Firebase 등)와 빈번하게 상호작용하기 때문에 안드로이드 개발에서 매우 일반적인 작업입니다. 이러한 서버들은 가볍고, 사람이 읽기 쉬우며, 플랫폼에 독립적이라는 장점 때문에 주로 JSON 형식으로 데이터를 교환합니다.

사용자 데이터나 환경설정을 보내거나, 콘텐츠(예: 뉴스 기사, 메시지)를 가져오기 위해, 앱은 코틀린 객체를 JSON으로 직렬화하고 서버의 JSON 응답을 다시 사용 가능한 객체로 역직렬화(deserialize)해야 합니다.

-----

## 직렬화(Serialization)와 역직렬화(Deserialization)란?

  * **직렬화 (Serialization):**
    객체나 데이터 구조를 나중에 쉽게 저장, 전송 또는 재구성할 수 있는 형식으로 변환하는 과정입니다. 안드로이드와 백엔드 통신 맥락에서는, 종종 객체를 JSON 문자열이나 유사한 구조화된 형식으로 변환하는 것을 의미합니다.
  * **역직렬화 (Deserialization):**
    직렬화의 반대 과정입니다. 직렬화된 데이터(JSON 문자열 등)를 가져와 애플리케이션이 작업할 수 있는 인메모리(in-memory) 객체로 다시 재구성하는 것을 포함합니다.

-----

## 수동 직렬화 및 역직렬화 (라이브러리 미사용)

직접적인 문자열 조작 및 파싱 기법을 통해 객체를 JSON 문자열로 또는 그 반대로 변환하여 수동으로 직렬화 및 역직렬화를 할 수 있습니다.

### 수동 직렬화

수동 직렬화는 객체의 속성으로부터 JSON 문자열을 만드는 것을 포함합니다.

```kotlin
data class User(val name: String, val age: Int)

fun serializeUser(user: User): String {
    return """{
        "name": "${user.name}",
        "age": ${user.age}
    }""".trimIndent()
}

// 사용 예시
val user = User("John", 30)
val jsonString = serializeUser(user)
// 출력: {"name":"John","age":30}
```

### 수동 역직렬화

반면에, 수동 역직렬화는 JSON 문자열을 파싱하고 값을 추출하여 객체를 재구성하는 것을 포함합니다.

```kotlin
fun deserializeUser(json: String): User {
    // 정규 표현식을 사용한 간단한 파싱 예시
    val nameRegex = """"name"\s*:\s*"([^"]*)"""".toRegex()
    val ageRegex = """"age"\s*:\s*(\d+)""".toRegex()

    val name = nameRegex.find(json)?.groups?.get(1)?.value ?: ""
    val age = ageRegex.find(json)?.groups?.get(1)?.value?.toIntOrNull() ?: 0

    return User(name, age)
}

// 사용 예시
val jsonString = """{"name":"John","age":30}"""
val user = deserializeUser(jsonString)
// 출력: User(name="John", age=30)
```

이 방식은 유지보수, 안전성, 확장성 문제 때문에 운영 앱에는 권장되지 않지만, 학습 목적이나 의존성을 최소화해야 하는 가벼운 경우에 유용할 수 있습니다. 대신, **`kotlinx.serialization`**, **`Moshi`**, **`Gson`** 과 같은 라이브러리들은 이 과정을 단순화하여 JSON 문자열을 코틀린이나 자바 객체로 또는 그 반대로 훨씬 더 효과적으로 변환할 수 있게 해줍니다.

-----

## JSON 직렬화 라이브러리 비교

### 1. kotlinx.serialization

[**`kotlinx.serialization`**](https://github.com/Kotlin/kotlinx.serialization) 은 JetBrains에서 개발했으며, 코틀린과 긴밀하게 통합되어 코틀린의 언어 기능을 활용하도록 설계되었습니다. 어노테이션을 사용하여 직렬화 동작을 정의하며 JSON뿐만 아니라 ProtoBuf와 같은 다른 형식과도 원활하게 작동합니다.

```kotlin
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable // 직렬화 가능함을 나타내는 어노테이션
data class User(val name: String, val age: Int)

// JSON을 객체로 역직렬화
val json = """{"name": "John", "age": 30}"""
val user: User = Json.decodeFromString<User>(json)

// 객체를 JSON으로 직렬화
val serializedJson: String = Json.encodeToString(user)
```

`kotlinx.serialization`은 **코틀린 컴파일러 플러그인**을 사용하여, Moshi의 리플렉션 모드나 Gson과는 달리 무거운 **런타임 리플렉션 없이도** 효율적인 직렬화를 수행하기 위한 코드를 컴파일러가 생성해주기 때문에, 현대 안드로이드 및 코틀린 개발에서 가장 선호되는 방식 중 하나입니다. 이는 타입 세이프(type-safe)하고 리플렉션이 없는 메커니즘을 제공합니다.

### 2. Moshi

[**Moshi**](https://github.com/square/moshi)는 Square에서 개발한 현대적인 JSON 라이브러리로, 타입 안전성과 코틀린 지원을 강조합니다. Gson과 달리, Moshi는 코틀린의 Null 허용 여부(nullability)와 기본 파라미터를 별도의 설정 없이 바로 지원합니다. 이로 인해 Moshi는 코틀린 우선(Kotlin-first) 개발에 더 적합합니다.

```kotlin
import com.squareup.moshi.Moshi

data class User(val name: String, val age: Int)

val moshi = Moshi.Builder().build()
val adapter = moshi.adapter(User::class.java)
val json = """{"name": "John", "age": 30}"""

// JSON을 객체로 역직렬화
val user = adapter.fromJson(json)

// 객체를 JSON으로 직렬화
val serializedJson = adapter.toJson(user)
```

Moshi에는 JSON 직렬화 및 역직렬화를 처리하는 두 가지 주요 접근 방식이 있습니다: **리플렉션 기반**과 **코드 생성(codegen) 기반**. 둘 다 JSON을 코틀린/자바 객체로 또는 그 반대로 변환할 수 있지만, 성능, 런타임 동작, 그리고 내부 작동 방식에서 상당한 차이가 있습니다.

  * [**리플렉션 기반 Moshi:**](https://github.com/square/moshi?tab=readme-ov-file#reflection) 런타임에 자바 리플렉션을 사용하여 동적으로 JSON 어댑터를 생성합니다. 추가 설정이 필요 없지만 런타임 오버헤드가 발생합니다.
  * [**코드 생성 기반 Moshi:**](https://github.com/square/moshi?tab=readme-ov-file#codegen) 컴파일 타임에 어노테이션 처리([Kotlin Symbol Processor, KSP](https://kotlinlang.org/docs/ksp-overview.html))를 통해 JSON 어댑터를 생성하여, 더 빠른 런타임 성능과 컴파일 시점 오류 검사를 제공합니다.

Moshi의 리플렉션 기반 어댑터는 빠른 설정을 제공하지만 런타임 성능 오버헤드와 제한적인 멀티플랫폼 지원의 단점이 있습니다. 반면에, Moshi의 코드 생성 접근 방식은 컴파일 시점에 최적화된 어댑터를 생성하여 더 나은 성능을 제공하므로, 일반적으로 **코드 생성 방식이 더 선호됩니다.**

> 💡 **추가 팁:** 커뮤니티에서 만든 [**MoshiX**](https://github.com/ZacSweers/MoshiX)라는 라이브러리도 있습니다. 이는 코틀린 IR(중간 표현) 컴파일러 플러그인을 사용하여 빌드되었으며, 컴파일 시점에 고도로 최적화된 코드를 생성하여 KSP 기반 또는 리플렉션 기반 워크플로우에 비해 더 나은 성능을 제공합니다.

### 3. Gson

**Gson**은 Google에서 개발한 널리 사용되는 JSON 라이브러리입니다. 자바 객체를 JSON으로 직렬화하고 JSON을 다시 자바 객체로 역직렬화할 수 있습니다. 간단한 API와 쉬운 통합으로 인해 인기 있는 선택지입니다.

```kotlin
import com.google.gson.Gson

data class User(val name: String, val age: Int)

val gson = Gson()
val json = """{"name": "John", "age": 30}"""

// JSON을 객체로 역직렬화
val user = gson.fromJson(json, User::class.java)

// 객체를 JSON으로 직렬화
val serializedJson = gson.toJson(user)
```

하지만, `kotlinx.serialization`이나 `Moshi`를 Gson보다 선호해야 할 강력한 이유들이 있습니다.

#### 왜 Gson보다 `kotlinx.serialization` 또는 `Moshi`를 선호해야 하는가?

  * **더 나은 코틀린 지원:** Gson은 자바를 위해 설계되었으며 코틀린의 기능(기본 파라미터, `val`/`var` 차이, nullability 등)을 `kotlinx.serialization`이나 Moshi만큼 자연스럽게 처리하지 못합니다.
  * **성능 및 효율성:** `kotlinx.serialization`과 Moshi(특히 코드 생성 방식)는 런타임에 리플렉션에 크게 의존하는 Gson보다 더 빠르고 메모리 효율적입니다.
  * **멀티플랫폼 호환성:** `kotlinx.serialization`은 코틀린 멀티플랫폼(KMP)을 완벽하게 지원하는 반면, Moshi/Gson은 JVM에 묶여 있어 크로스플랫폼 앱에는 부적합합니다.
  * **컴파일 시점 안전성:** `kotlinx.serialization`과 코드 생성 방식의 Moshi는 많은 오류를 컴파일 시점에 잡아내어 런타임 충돌을 줄이는 반면, Gson은 종종 오류를 런타임까지 지연시킵니다.

Gson과 Moshi의 주요 기여자 중 한 명인 **Jake Wharton**의 통찰력은 다음과 같습니다.

> [**Jake Wharton (reddit:r/androiddev):**](https://www.reddit.com/r/androiddev/comments/dhjdk2/moshi_vs_kotlinx_serilization_with_retrofit/) "Moshi는 이름만 빼면 모든 면에서 Gson v3와 같기 때문에 마이그레이션하기 가장 쉬울 것입니다. 코틀린 지원에는 약간의 설정이 필요하지만, 간단할 것입니다. 가장 큰 단점은 엄청나게 큰 의존성인 kotlin-reflect를 사용하거나 빌드 성능에 영향을 미치는 코드 생성을 사용해야 한다는 것입니다."
>
> "**kotlinx.serialization도 훌륭하지만, 기능이 더 적습니다(Moshi가 Gson보다 기능이 적게 설계되었다는 점을 감안하면 의미 있는 말이죠). 하지만 멀티플랫폼과 같은 멋진 것들을 가능하게 합니다. 유일한 단점은 현재 스트리밍이 없다는 것이므로, 거대한 응답 본문이 있다면 추가적인 메모리 압박이 있을 것입니다.**"
>
> "**저는 JS와 모델을 공유하기 때문에 kotlinx.serialization을 사용합니다. 그래서 멀티플랫폼이 필요하죠.**"

-----

### 요약: 어떤 라이브러리를 선택할 것인가?

JSON을 객체로 직렬화하기 위해 `Gson`, `Moshi`, `kotlinx.serialization`과 같은 라이브러리들은 JSON 문자열을 코틀린 객체로 매핑하는 효율적인 API를 제공합니다.

  * **`kotlinx.serialization`**: 코틀린 우선(Kotlin-first) 및 멀티플랫폼 프로젝트에 **최고의 선택**이며, 컴파일 시점 안전성, 가벼운 런타임 및 완전한 네이티브 코틀린 지원을 제공합니다.
  * **`Moshi`**: 빠른 성능과 더 안전한 JSON 처리(특히 코드 생성 방식)가 필요한 **안드로이드 중심 앱**에 이상적입니다.
  * **`Gson`**: 빠른 JVM 전용 프로젝트에 **통합하기는 쉽지만**, 성능, 코틀린 지원 및 현대적인 모범 사례 측면에서는 뒤처집니다.

더 많은 통찰력을 얻고 싶다면, 레딧에서 제기된 토론인 [Why use Moshi over Gson?](https://www.reddit.com/r/androiddev/comments/684flw/why_use_moshi_over_gson/)을 확인해 볼 수 있습니다.

-----

## Q. API로부터 받은 JSON 응답이 주어졌을 때, 이를 코틀린 데이터 클래스로 어떻게 역직렬화할 건가요? 코틀린 우선(Kotlin-first) 프로젝트를 위해 어떤 라이브러리를 선택하고 그 이유는 무엇인가요?

### 1. 라이브러리 선택: 코틀린 우선(Kotlin-first) 프로젝트를 위한 권장 사항

코틀린 우선 프로젝트에서 JSON을 처리할 때, 가장 권장되는 라이브러리는 **`kotlinx.serialization`** 입니다.

#### 1.1. 🥇 주요 선택: `kotlinx.serialization`
* **선택 이유:**
    * **코틀린 네이티브:** 코틀린 언어를 개발한 JetBrains에서 직접 개발하여 코틀린의 언어적 특징(null 안전성, 기본값 등)을 완벽하게 지원합니다.
    * **리플렉션-프리(Reflection-Free):** 코틀린 컴파일러 플러그인을 사용하여 컴파일 시점에 필요한 코드를 자동으로 생성합니다. 런타임에 리플렉션을 사용하지 않으므로 성능이 뛰어나고, ProGuard/R8 설정이 더 간단합니다.
    * **컴파일 시점 안전성:** 많은 오류를 런타임이 아닌 컴파일 시점에 발견할 수 있습니다.
    * **멀티플랫폼 지원:** 코틀린 멀티플랫폼(KMP) 프로젝트를 지원하는 유일한 주요 라이브러리로, 안드로이드와 iOS, 웹 등에서 코드를 공유할 때 매우 강력합니다.

#### 1.2. 🥈 강력한 대안: `Moshi` (코드 생성 방식)
* `Moshi` 역시 코틀린을 매우 잘 지원하며, 특히 KSP(Kotlin Symbol Processing)를 사용한 코드 생성(codegen) 방식을 채택하면 `kotlinx.serialization`과 유사한 성능 및 안전성 이점을 얻을 수 있습니다. 코틀린 우선 프로젝트에서 훌륭한 대안이 될 수 있습니다.

> 이 답변에서는 가장 권장되는 **`kotlinx.serialization`** 을 기준으로 역직렬화 단계를 설명하겠습니다.

---
### 2. JSON을 코틀린 데이터 클래스로 역직렬화하는 단계 (`kotlinx.serialization` 기준)

API로부터 다음과 같은 JSON 응답을 받았다고 가정해 보겠습니다.
```json
{
  "id": 123,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "isActive": true
}
```

#### 2.1. 1단계: 프로젝트 설정 (Gradle)
모듈 수준의 `build.gradle.kts` 파일에 `kotlinx.serialization` 플러그인과 라이브러리 의존성을 추가합니다.

```kotlin
// build.gradle.kts (모듈 수준)

plugins {
    id("kotlinx-serialization")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") // 최신 버전 확인
}
```

#### 2.2. 2단계: 데이터 클래스 정의
JSON 구조에 맞춰 코틀린 `data class`를 정의하고, **`@Serializable`** 어노테이션을 반드시 추가합니다. 변수 이름은 JSON 키와 일치시키는 것이 좋습니다.

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val isActive: Boolean
)
```

#### 2.3. 3단계: 역직렬화 실행
`Json.decodeFromString()` 메서드를 사용하여 JSON 문자열을 정의된 데이터 클래스 객체로 변환합니다.

```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

fun deserializeUser(jsonString: String): UserProfile {
    // Json 객체 생성 (기본 설정 사용)
    val json = Json

    // JSON 문자열을 UserProfile 객체로 역직렬화
    return json.decodeFromString<UserProfile>(jsonString)
}

fun main() { // 예시 실행
    val apiResponse = """
        {
          "id": 123,
          "name": "John Doe",
          "email": "john.doe@example.com",
          "isActive": true
        }
    """.trimIndent()

    val userProfileObject = deserializeUser(apiResponse)
    println(userProfileObject)
    // 출력: UserProfile(id=123, name=John Doe, email=john.doe@example.com, isActive=true)
}
```

---
### 3. `kotlinx.serialization`을 선택한 이유 (상세)

`kotlinx.serialization`이 코틀린 우선 프로젝트에 가장 적합한 이유는 다음과 같습니다.

#### 3.1. 코틀린 언어와의 완벽한 통합
* 코틀린 컴파일러 플러그인을 통해 작동하므로, 코틀린의 타입 시스템, null 허용 여부(nullability), 기본 파라미터(default parameters) 등의 기능을 별도의 어댑터나 복잡한 설정 없이 네이티브하게 이해하고 처리합니다.

#### 3.2. 컴파일 시점 안전성 (Compile-Time Safety)
* JSON 키와 데이터 클래스의 프로퍼티가 일치하지 않거나, 타입이 호환되지 않는 경우 등 많은 잠재적 오류를 런타임이 아닌 컴파일 시점에 발견할 수 있게 도와줍니다. 이는 런타임 오류로 인한 앱 비정상 종료를 크게 줄여줍니다.

#### 3.3. 성능 (Performance)
* 런타임에 클래스 구조를 분석하는 리플렉션(reflection)을 사용하지 않습니다. 대신 컴파일 시점에 직렬화/역직렬화를 위한 코드를 미리 생성하므로, Gson이나 Moshi의 리플렉션 기반 모드보다 일반적으로 더 빠르고 메모리 효율적입니다.

#### 3.4. 멀티플랫폼 지원 (Multiplatform Support)
* 코틀린 멀티플랫폼(KMP)을 완벽하게 지원하는 유일한 주요 라이브러리입니다. 안드로이드와 iOS, 웹(JS), 데스크톱(JVM) 등 여러 플랫폼에서 동일한 데이터 모델과 직렬화 로직을 공유할 수 있어 코드 재사용성을 극대화할 수 있습니다.

#### 3.5. 간결성과 가독성
* `@Serializable` 어노테이션 하나만으로 대부분의 경우 직렬화가 가능하여 코드가 매우 간결해집니다. 복잡한 타입을 처리하기 위해 다른 라이브러리들에서 종종 요구되는 커스텀 어댑터 작성보다 더 직관적일 수 있습니다.

---
### 4. 결론

API 응답을 코틀린 데이터 클래스로 역직렬화할 때, **코틀린 우선 프로젝트라면 `kotlinx.serialization`을 선택하는 것이 가장 좋습니다.** 이는 코틀린 언어와의 완벽한 통합, 컴파일 시점 안전성, 뛰어난 성능, 그리고 멀티플랫폼 지원이라는 확실한 장점을 제공하여, 현대적이고 견고하며 유지보수하기 쉬운 안드로이드 애플리케이션을 구축하는 데 가장 이상적인 솔루션입니다.

## Q. 코틀린 데이터 클래스에 정의되지 않은 필드가 누락되거나 추가된 JSON 객체를 역직렬화해야 하는 경우, 이 시나리오를 어떻게 처리할 건가요?

API로부터 받은 JSON 응답의 구조가 코틀린 데이터 클래스(Data Class)의 구조와 항상 완벽하게 일치하지는 않습니다. API 버전 업데이트 등으로 인해 JSON에 필드가 추가되거나 누락될 수 있습니다. 이러한 시나리오를 안정적으로 처리하는 것은 견고한 애플리케이션을 만드는 데 매우 중요합니다.

이전 답변에 이어, 코틀린 우선(Kotlin-first) 프로젝트에 가장 권장되는 **`kotlinx.serialization`** 라이브러리를 기준으로 처리 방법을 설명하겠습니다.

---
### 1. JSON에 필드가 누락된 경우 (Data Class에는 존재)

API 응답 JSON에 특정 키-값이 없는데, 매핑하려는 데이터 클래스에는 해당 필드가 non-nullable로 선언되어 있는 경우입니다.

#### 1.1. 기본 동작: `MissingFieldException` 발생
`kotlinx.serialization`은 기본적으로 매우 엄격합니다. 데이터 클래스에 정의된 non-nullable 필드가 JSON에 없으면, 역직렬화 과정에서 **`MissingFieldException`을 발생시켜 앱이 비정상 종료**될 수 있습니다. 이는 API와 데이터 모델 간의 불일치를 즉시 파악하고 의도치 않은 `null` 값으로 인한 2차적인 버그를 방지하는 안전한 기본 동작입니다.

#### 1.2. 해결책: 데이터 클래스에 기본값(Default Value) 제공
누락될 수 있는 필드에 대해 데이터 클래스를 정의할 때 **기본값**을 지정하면, `kotlinx.serialization`은 JSON에 해당 키가 없을 경우 이 기본값을 사용합니다.

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String? = null, // email은 nullable. JSON에 없으면 null이 됨.
    val isPremiumUser: Boolean = false // isPremiumUser는 non-nullable이지만 기본값이 있음.
)

// 예시 JSON (email과 isPremiumUser 필드 누락)
val jsonString = """
    {
      "id": 123,
      "name": "John Doe"
    }
""".trimIndent()

// 역직렬화 실행
val user = Json.decodeFromString<UserProfile>(jsonString)

// 결과:
// user.id -> 123
// user.name -> "John Doe"
// user.email -> null
// user.isPremiumUser -> false (기본값 사용)
```
이처럼, `email` 필드는 nullable이므로 누락 시 `null`이 할당되고, `isPremiumUser` 필드는 non-nullable이지만 기본값 `false`가 지정되어 있어 예외 없이 역직렬화가 성공합니다.

---
### 2. JSON에 추가 필드가 있는 경우 (Data Class에는 없음)

API 응답 JSON에는 존재하지만, 앱의 데이터 클래스에는 정의되지 않은 필드가 포함된 경우입니다.

#### 2.1. 기본 동작: `SerializationException` (Unknown Key) 발생
`kotlinx.serialization`의 엄격한 기본 정책에 따라, 데이터 클래스에 매핑될 프로퍼티가 없는 키가 JSON에 존재하면 역직렬화 시 **알 수 없는 키(Unknown Key)를 발견했다는 `SerializationException`이 발생**합니다. 이는 예상치 못한 데이터가 수신되었음을 개발자에게 알려주는 안전장치 역할을 합니다.

#### 2.2. 해결책: `ignoreUnknownKeys = true` 설정
향후 API 확장 등을 고려하여 데이터 클래스에 정의되지 않은 키를 무시하고 싶다면, `Json` 객체를 설정할 때 `ignoreUnknownKeys` 속성을 `true`로 지정합니다.

```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

// Json 객체 생성 시 설정
val jsonParser = Json {
    ignoreUnknownKeys = true // 알 수 없는 키를 무시하도록 설정
}

// 예시 JSON (데이터 클래스에 없는 'location' 필드 포함)
val jsonStringWithExtraField = """
    {
      "id": 123,
      "name": "John Doe",
      "location": "New York" 
    }
""".trimIndent()

// 역직렬화 실행
val user = jsonParser.decodeFromString<UserProfile>(jsonStringWithExtraField) // UserProfile에는 location이 없음

// 결과: 'location' 필드는 무시되고 역직렬화 성공
// user.id -> 123
// user.name -> "John Doe"
```

---
### 3. 전체 예시 코드 (`kotlinx.serialization`)

누락된 필드와 추가 필드를 모두 처리하는 전체 예시입니다.

```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

// 1. 데이터 클래스 정의 (기본값 및 Nullable 사용)
@Serializable
data class User(
    val id: Int,
    val username: String,
    val points: Int = 0, // 누락 시 기본값 0
    val nickname: String? = null // 누락 시 null
)

fun main() {
    // 2. Json 파서 설정
    val jsonParser = Json {
        ignoreUnknownKeys = true // 정의되지 않은 키는 무시
        isLenient = true // 따옴표 등 사소한 JSON 형식 오류 관대하게 처리 (선택 사항)
    }

    // 3. API 응답 시뮬레이션
    // 'points'와 'nickname' 필드가 누락되었고, 'country'라는 추가 필드가 있음
    val apiResponse = """
        {
            "id": 456,
            "username": "jane_doe",
            "country": "Canada"
        }
    """.trimIndent()

    // 4. 역직렬화
    val userObject = jsonParser.decodeFromString<User>(apiResponse)

    // 5. 결과 확인
    println(userObject)
    // 출력: User(id=456, username=jane_doe, points=0, nickname=null)
}
```

---
### 4. (참고) 다른 라이브러리에서의 처리 방식

* **Moshi:**
    * **누락된 필드:** `kotlinx.serialization`과 유사하게, non-nullable 필드가 누락되면 예외를 발생시킵니다. 데이터 클래스에 기본값을 제공하면 해당 값을 사용합니다.
    * **추가 필드:** 기본적으로 **알 수 없는 필드를 무시**합니다. 이는 `kotlinx.serialization`의 기본 동작과 반대입니다.
* **Gson:**
    * **누락된 필드:** non-nullable 필드가 누락되면 예외를 발생시키지 않고 해당 타입의 기본값(예: `Int`는 `0`, `Boolean`은 `false`, 객체는 `null`)을 할당합니다. 이는 API의 변경을 인지하지 못하게 할 수 있어 위험할 수 있습니다.
    * **추가 필드:** Moshi와 마찬가지로 기본적으로 **알 수 없는 필드를 무시**합니다.

---
### 5. 결론: 견고한 역직렬화를 위한 전략

API 응답 변화에 유연하고 안정적으로 대응하기 위한 모범 사례는 다음과 같습니다.

1.  **필수적이지 않은 필드는 기본값 또는 Nullable로 선언:** JSON 응답에서 선택적으로 존재할 수 있는 필드는 코틀린 데이터 클래스에서 기본값을 갖는 non-nullable 프로퍼티나 nullable 프로퍼티(`Type? = null`)로 선언합니다.
2.  **알 수 없는 키는 무시하도록 설정:** `Json { ignoreUnknownKeys = true }` 설정을 통해, 향후 API에 새로운 필드가 추가되더라도 기존 앱이 비정상 종료되지 않도록 합니다.

이러한 전략을 통해, API의 사소한 변경에도 앱이 안정적으로 작동하도록 만들 수 있습니다.