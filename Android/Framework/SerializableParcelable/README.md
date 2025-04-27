# Serializable 과 Parcelizable

- [Q. Android에서 Serializable과 Parcelable의 주요 차이점은 무엇이며, Parcelable이 일반적으로 구성 요소 간에 데이터를 전달하는 데 선호되는 이유는 무엇입니까?](#q-android에서-serializable과-parcelable의-주요-차이점은-무엇이며-parcelable이-일반적으로-구성-요소-간에-데이터를-전달하는-데-선호되는-이유는-무엇입니까)

---

## Serializable 과 Parcelable 의 차이점은 무엇인가요?
Android에서 Serializable과 Parcelable은 모두 서로 다른 구성 요소(예: Activity 또는 Fragment) 간에 데이터를 전달하는 데 사용되는 메커니즘이지만 성능과 구현 측면에서 다르게 작동합니다.

### Serializable

- **Java Standard Interface**: Serializable은 객체를 바이트 스트림으로 변환하는 데 사용되는 표준 Java 인터페이스이므로 액티비티 간에 전달되거나 디스크에 쓸 수 있습니다.
- **리플렉션 기반**: 자바 리플렉션을 통해 작동합니다. 즉, 시스템이 런타임에 클래스와 필드를 동적으로 검사하여 객체를 직렬화합니다.
- **성능**: Serializable은 리플렉션 프로세스가 느리기 때문에 Parcelable에 비해 느립니다. 또한 직렬화 중에 많은 임시 개체를 생성하여 메모리 오버헤드를 증가시킵니다.
- **사용 사례**: Serializable은 성능이 중요하지 않은 시나리오나 Android와 관련되지 않은 코드베이스를 다룰 때 유용합니다.

### Parcelable

- **안드로이드 전용 인터페이스**: Parcelable은 안드로이드 구성 요소 내에서 고성능 프로세스 간 통신(IPC)을 위해 특별히 설계된 안드로이드 전용 인터페이스입니다.
- **성능**: Parcelable은 Android에 최적화되어 있고 리플렉션에 의존하지 않기 때문에 Serializable보다 빠릅니다. 많은 임시 개체를 생성하지 않음으로써 가비지 수집을 최소화합니다.
- **사용 사례**: Parcelable은 성능이 중요할 때, 특히 IPC 또는 액티비티 또는 서비스 간에 데이터를 전달할 때 Android에서 데이터를 전달하는 데 선호됩니다.

현대 안드로이드 개발에서 kotlin-parcelize 플러그인은 구현을 자동으로 생성하여 Parcelable 객체를 생성하는 과정을 단순화합니다. 이 접근법은 이전의 수동 메커니즘에 비해 더 효율적이다. 단순히 @Parcelize로 클래스에 주석을 달면 플러그인은 필요한 Parcelable 구현을 생성합니다. 작동 방식을 보여주는 예는 다음과 같습니다.

```kotlin
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
 
@Parcelize
class User(val firstName: String, val lastName: String, val age: Int) : Parcelable
```

### 요약

- 일반적으로 안드로이드 애플리케이션의 경우, Parcelable은 대부분의 사용 사례에서 더 나은 성능으로 인해 권장되는 접근 방식입니다.
- 더 간단한 경우나 성능에 중요하지 않은 작업을 처리하거나 안드로이드와 관련되지 않은 코드로 작업할 때 Serializable을 사용하십시오.
- Android의 IPC 메커니즘에 대해 훨씬 더 효율적이기 때문에 성능이 중요한 Android 전용 구성 요소로 작업할 때 Parcelable을 사용하십시오.


### Q. Android에서 Serializable과 Parcelable의 주요 차이점은 무엇이며, Parcelable이 일반적으로 구성 요소 간에 데이터를 전달하는 데 선호되는 이유는 무엇입니까?

Android에서 `Serializable`과 `Parcelable`은 객체를 다른 컴포넌트나 프로세스로 전달하기 위해 직렬화(Serialization)하는 두 가지 주요 메커니즘입니다. 둘 사이의 주요 차이점과 `Parcelable`이 선호되는 이유는 다음과 같습니다.

**1. Serializable과 Parcelable의 주요 차이점**

| 특징             | Serializable (`java.io.Serializable`)                     | Parcelable (`android.os.Parcelable`)                         |
|------------------|-----------------------------------------------------------|--------------------------------------------------------------|
| **기원** | 표준 자바(Java) 인터페이스                                 | 안드로이드(Android) SDK 인터페이스                           |
| **구현 방식** | 인터페이스 구현만 하면 자바 리플렉션(Reflection)을 통해 자동 처리 | `writeToParcel()`, `describeContents()`, `CREATOR` 객체 등 직접 구현 필요 |
| **처리 메커니즘** | 리플렉션을 사용 (느리고, 임시 객체 생성 많음)                | 직접 데이터를 Parcel 객체에 쓰고 읽음 (빠르고, 효율적)             |
| **성능** | 상대적으로 느리고 메모리 사용량 많음                        | **훨씬 빠르고 메모리 효율적** |
| **구현 복잡도** | 간단 (주로 인터페이스 선언만 필요)                           | 상대적으로 복잡 (보일러플레이트 코드 필요)                      |
| **용도** | 일반적인 자바 객체 직렬화 (파일 저장, 네트워크 전송 등)        | **안드로이드의 IPC(프로세스 간 통신)** 및 컴포넌트 간 데이터 전달에 최적화 |

**2. Parcelable이 선호되는 이유**

안드로이드에서 액티비티(Activity), 프래그먼트(Fragment), 서비스(Service) 등 컴포넌트 간에 데이터를 인텐트(Intent)나 번들(Bundle)을 통해 전달할 때 `Parcelable`이 일반적으로 선호되는 주된 이유는 **성능** 때문입니다.

* **속도와 효율성:** `Parcelable`은 안드로이드의 프로세스 간 통신(IPC) 메커니즘인 Binder 통신에 맞게 특별히 설계되었습니다. 리플렉션을 사용하는 `Serializable`과 달리, `Parcelable`은 개발자가 명시적으로 데이터를 `Parcel` 객체에 쓰고 읽는 코드를 작성하므로 훨씬 빠릅니다. 리플렉션은 실행 시간에 객체의 구조를 분석해야 하므로 오버헤드가 크고, 이로 인해 UI 끊김(Jank)이나 성능 저하를 유발할 수 있습니다.
* **메모리 사용:** `Serializable`은 직렬화 과정에서 많은 임시 객체를 생성하여 가비지 컬렉션(GC) 부담을 증가시킬 수 있습니다. 반면, `Parcelable`은 이러한 오버헤드가 훨씬 적어 메모리 사용에 더 효율적입니다. 이는 메모리가 제한적인 모바일 환경에서 중요한 장점입니다.
* **안드로이드 플랫폼 표준:** 안드로이드 프레임워크 자체의 많은 객체들이 컴포넌트 간 데이터 전달을 위해 `Parcelable`을 사용합니다. 따라서 안드로이드 개발 시 `Parcelable`을 사용하는 것이 플랫폼의 설계 철학과 더 잘 부합합니다.

**결론:**

`Serializable`은 구현이 매우 간편하다는 장점이 있지만, 성능 오버헤드가 커서 안드로이드 앱의 성능에 부정적인 영향을 줄 수 있습니다. 반면, `Parcelable`은 구현에 더 많은 노력이 필요하지만, 월등한 성능과 효율성 덕분에 안드로이드 컴포넌트 간 데이터 전달(특히 IPC가 포함될 수 있는 경우)에 강력히 권장되는 방식입니다.


### 💡 프로 팁: Parcel and Parcelable

- Parcel은 애플리케이션의 서로 다른 구성 요소(예: ,액티비티 서비스 또는 브로드캐스트 수신기) 간에 고성능 프로세스 간 통신(IPC)을 가능하게 하는 Android의 컨테이너 클래스입니다. 그것은 주로 마샬링(평탄화) 및 언마샬링(객체화) 데이터에 사용되어 안드로이드의 IPC 경계를 넘어 전달될 수 있습니다.
- Parcel은 프로세스 간 통신(IPC) 메커니즘을 통해 플랫화된 데이터와 참조를 라이브 IBinder 객체로 보내는 데 사용되는 컨테이너입니다. 고성능 IPC 전송을 위해 설계되어 개체(Parcelable 인터페이스 사용)를 직렬화하고 구성 요소 간에 효율적으로 전달할 수 있습니다. Parcel은 범용 직렬화 도구가 아니며 기본 구현이 변경되어 이전 데이터를 읽을 수 없게 만들 수 있으므로 영구 저장에 사용해서는 안 됩니다.
- API에는 원시 데이터 유형, 배열 및 Parcelable 객체를 읽고 쓰기 위한 다양한 방법이 포함되어 있어 객체가 필요할 때 직렬화하고 재구성할 수 있습니다. 또한, 쓰기 클래스 정보를 생략하는 Parcelables로 작업하기 위한 최적화된 방법이 있으며, 독자가 유형을 알아야 합니다.
- Parcelable은 Parcel을 통해 전달할 수 있도록 개체를 직렬화하는 데 사용되는 Android 전용 인터페이스입니다. Parcelable을 구현하는 개체는 Parcel에 기록되고 Parcel에서 복원될 수 있어 Android 구성 요소 간에 복잡한 데이터를 전달하는 데 적합합니다.

### 요약

Parcel은 다양한 데이터 유형을 지원하는 IPC를 사용하여 구성 요소 간에 데이터를 전송하는 컨테이너입니다. Parcelable은 효율적인 전송을 위해 개체를 Parcel으로 평평하게 할 수 있는 인터페이스입니다. Parcel의 실제 구현과 작동 메커니즘에 대해 자세히 알아보기 위해 [AOSP - Parcel.java](https://android.googlesource.com/platform/frameworks/base/+/27f592d/core/java/android/os/Parcel.java)에서 소스 코드를 탐색할 수 있습니다.
