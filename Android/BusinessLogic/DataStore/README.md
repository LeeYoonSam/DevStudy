# 데이터를 로컬에 저장하고 유지하는 방법

안드로이드는 **데이터를 로컬에 저장하고 유지**하기 위한 여러 메커니즘을 제공합니다. 각 방식은 경량의 키-값 저장소, 구조화된 데이터베이스 관리, 또는 파일 처리와 같은 특정 사용 사례에 맞게 설계되었습니다. 아래는 주요 로컬 저장소 옵션입니다.

---
## 주요 로컬 저장소 옵션

### 1. SharedPreferences

[**`SharedPreferences`**](https://developer.android.com/training/data-storage/shared-preferences) 는 앱 설정이나 사용자 환경설정과 같은 **경량 데이터**에 가장 적합한 간단한 **키-값(key-value) 저장 메커니즘**입니다. `Boolean`, `Int`, `String`, `Float`과 같은 기본(원시) 데이터 타입을 저장하고 앱 재시작 후에도 유지할 수 있게 해줍니다. `SharedPreferences`는 동기적으로 작동하지만, `DataStore`의 도입으로 현대 애플리케이션에서는 선호도가 낮아지고 있습니다.

```kotlin
val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
val editor = sharedPreferences.edit { // 코틀린 KTX 확장 함수 사용
    putString("user_name", "skydoves")
    // commit() 또는 apply()가 자동으로 호출됨
}
```

### 2. DataStore

[**Jetpack DataStore**](https://developer.android.com/topic/libraries/architecture/datastore)는 `SharedPreferences`를 대체하는 더 현대적이고, 확장 가능하며, 효율적인 솔루션입니다. 키-값 저장을 위한 **PreferencesDataStore**와 구조화된 데이터를 위한 **ProtoDataStore** 두 가지 유형을 제공합니다. `SharedPreferences`와 달리, `DataStore`는 **비동기적**으로 작동하여 메인 스레드를 차단하는 잠재적인 문제를 피합니다.

```kotlin
val dataStore: DataStore<Preferences> = context.createDataStore(name = "settings")

val userNameKey = stringPreferencesKey("user_name")
// 코루틴 스코프 내에서 비동기적으로 데이터 저장
runBlocking { // 예시를 위한 runBlocking, 실제로는 viewModelScope 등 사용
    dataStore.edit { settings ->
        settings[userNameKey] = "John Doe"
    }
}
```

### 3. Room 데이터베이스

[**Room 데이터베이스**](https://developer.android.com/training/data-storage/room)는 SQLite에 대한 고수준 추상화 계층으로, **구조화되고 관계형인 데이터**를 처리하도록 설계되었습니다. 어노테이션, 컴파일 시점 검사, 그리고 반응형 프로그래밍을 위한 `LiveData` 또는 `Flow` 지원을 통해 데이터베이스 관리를 단순화합니다. Room은 복잡한 쿼리나 대량의 구조화된 데이터가 필요한 앱에 이상적입니다.

```kotlin
// Entity: 데이터베이스 테이블 정의
@Entity
data class User(
    @PrimaryKey val id: Int,
    val name: String
)

// Dao: 데이터베이스 접근 메서드 정의
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: Int): User
}

// Database: 데이터베이스 홀더 정의
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

### 4. 파일 저장소 (File Storage)

바이너리(binary) 또는 사용자 정의 데이터의 경우, 안드로이드는 **내부 또는 외부 저장소**에 파일을 저장할 수 있게 허용합니다. 내부 저장소는 앱에 비공개이며, 외부 저장소는 다른 앱과 공유될 수 있습니다. 파일 입출력(File I/O) 작업은 이미지, 비디오 또는 사용자 정의 직렬화 데이터와 같은 작업을 저장하는 데 사용될 수 있습니다.

```kotlin
val file = File(context.filesDir, "user_data.txt")
file.writeText("샘플 사용자 데이터")
```

---
## 요약: 어떤 저장소를 선택해야 할까?

안드로이드에서 저장 메커니즘의 선택은 데이터의 유형과 복잡성에 따라 달라집니다.
* **`SharedPreferences` 또는 `DataStore`**: 사용자 설정이나 기능 플래그(feature flags)와 같은 경량의 키-값 데이터를 저장하는 데 이상적입니다. (최신 앱에서는 `DataStore` 권장)
* **Room**: 구조화된 쿼리를 사용하여 복잡한 관계형 데이터를 관리하는 데 적합합니다.
* **파일 저장소**: 바이너리 파일이나 대규모 사용자 정의 데이터셋을 처리하는 데 가장 좋습니다.

각 방법은 애플리케이션 요구 사항에 따라 효율적이고 신뢰할 수 있는 데이터 저장을 보장하는 특정 이점을 제공합니다.

---
## Q. 네트워크 API로부터 받은 대용량 JSON 응답을 오프라인 접근을 위해 저장해야 하는 시나리오에서 어떤 로컬 저장 메커니즘을 사용하고, 그 이유는 무엇인가요?

API로부터 받은 대용량 JSON 응답을 오프라인 접근을 위해 저장해야 하는 시나리오에서는 **Room 데이터베이스**를 사용하는 것이 가장 이상적이고 효율적인 방법입니다.

---
### 1. 선택: Room 데이터베이스 (SQLite 추상화 라이브러리)

**`Room`** 은 SQLite 데이터베이스 위에 추상화 계층을 제공하는 Jetpack 라이브러리로, 대규모의 구조화된 데이터를 저장, 관리 및 쿼리하는 데 최적화되어 있습니다.

---
### 2. 이유: 왜 Room 데이터베이스인가?

파일 저장소나 SharedPreferences/DataStore와 비교했을 때, Room이 이 시나리오에 가장 적합한 이유는 다음과 같습니다.

#### 2.1. 구조화된 데이터 저장 및 강력한 쿼리 기능 🗃️
* **구조화:** JSON 응답은 일반적으로 객체의 리스트와 같이 구조를 가지고 있습니다. 이 JSON을 코틀린 데이터 클래스(Room에서는 `@Entity`로 정의)로 역직렬화(deserialize)한 후 Room에 저장하면, 데이터는 체계적인 테이블 형태로 보관됩니다.
* **강력한 쿼리:** `File`이나 `SharedPreferences`에 거대한 JSON 문자열을 통째로 저장하면, 특정 데이터 하나를 찾기 위해 매번 전체 문자열을 읽고 파싱해야 합니다. 이는 매우 비효율적입니다. 반면, Room을 사용하면 **SQL 쿼리**를 통해 필요한 데이터만 정확하고 빠르게 가져올 수 있습니다 (예: 특정 ID를 가진 아이템만 조회, 특정 조건으로 필터링, 이름순으로 정렬 등).

#### 2.2. 성능 및 효율성 ⚡️
* SQLite는 대용량 데이터셋에 대한 복잡한 쿼리를 수행하는 데 고도로 최적화되어 있습니다. Room은 이러한 SQLite의 성능을 활용하면서, 컴파일 시점에 SQL 쿼리의 유효성을 검사해주어 런타임 오류를 방지하고 안정성을 높여줍니다.
* 전체 데이터를 메모리에 올릴 필요 없이 필요한 부분만 조회하므로 메모리 사용량이 매우 효율적입니다.

#### 2.3. 데이터의 부분적 업데이트 용이성 🔄
* API로부터 새로운 데이터를 받거나 사용자가 특정 데이터를 수정했을 때, 전체 데이터셋을 덮어쓸 필요 없이 특정 레코드(행)만 삽입, 수정 또는 삭제하는 것이 간단하고 효율적입니다. 이는 DAO(`@Dao`) 인터페이스에 정의된 메서드를 통해 쉽게 구현할 수 있습니다.

#### 2.4. Jetpack Paging 라이브러리와의 완벽한 통합 📖
* Room은 Jetpack Paging 라이브러리를 네이티브하게 지원합니다. Room 데이터베이스를 직접 데이터 소스로 사용하는 `PagingSource`를 매우 쉽게 구현할 수 있습니다.
* 이를 통해, 로컬에 저장된 대용량 데이터를 오프라인 상태에서도 `RecyclerView`에 효율적으로 페이징하여 보여줄 수 있어, 부드러운 스크롤과 뛰어난 사용자 경험을 제공합니다.

#### 2.5. 관찰 가능한 쿼리 (Observable Queries)
* Room은 쿼리 결과로 `Flow`나 `LiveData`를 반환할 수 있습니다. 이를 통해 로컬 데이터베이스의 데이터가 변경될 때마다 UI가 자동으로 업데이트되는 반응형 아키텍처를 쉽게 구축할 수 있습니다.

---
### 3. 다른 저장 메커니즘이 부적합한 이유

* **파일 저장소 (File Storage):**
    * 대용량 JSON 문자열 자체를 파일로 저장하는 것은 가능합니다. 하지만 데이터를 사용하려면 결국 파일 전체를 읽고 JSON 파싱을 거쳐야 하므로, 특정 데이터만 필요할 때도 매우 비효율적입니다. 데이터 검색이나 정렬, 필터링 기능이 사실상 불가능합니다.
* **SharedPreferences / DataStore (Preferences):**
    * 이들은 간단한 키-값 쌍의 작은 데이터를 저장하기 위해 설계되었습니다(예: 사용자 설정, 로그인 토큰 등). 대용량의 구조화된 데이터를 저장하는 용도로는 부적합하며, 성능 문제를 야기할 수 있습니다.

---
### 4. 구현 흐름 요약

1.  **데이터 요청 및 파싱:** Retrofit 등을 사용하여 API로부터 JSON 응답을 받습니다.
2.  **역직렬화:** `kotlinx.serialization`이나 `Moshi`와 같은 라이브러리를 사용하여 JSON 문자열을 코틀린 데이터 객체(`@Entity` 어노테이션이 붙은)의 리스트로 변환합니다.
3.  **데이터베이스 저장:** Room의 DAO(`@Dao`)를 통해 이 객체 리스트를 데이터베이스에 삽입(insert)합니다.
4.  **데이터 접근:** UI에서는 네트워크 상태에 따라 온라인일 때는 API를, 오프라인일 때는 Room 데이터베이스를 쿼리하여 데이터를 가져와 표시합니다. `Repository` 패턴을 사용하여 이러한 데이터 소스 선택 로직을 추상화하는 것이 좋습니다.

---
### 5. 결론

네트워크로부터 받은 **대용량의 구조화된 JSON 응답**을 오프라인에서 활용하기 위해서는, 단순히 데이터를 저장하는 것을 넘어 **효율적인 검색과 관리가 가능**해야 합니다. 이러한 요구사항을 가장 완벽하게 만족시키는 솔루션은 **Room 데이터베이스**입니다. Room은 구조화된 저장, 강력한 쿼리 기능, 성능, 그리고 다른 Jetpack 라이브러리와의 뛰어난 통합성을 제공하여 이 시나리오에 가장 적합한 선택입니다.