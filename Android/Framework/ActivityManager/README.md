# ActivityManager란 무엇인가?

**ActivityManager(액티비티 매니저)** 는 기기에서 실행 중인 액티비티, 태스크, 프로세스에 대한 정보를 제공하고 이를 관리하는 안드로이드의 **시스템 서비스**입니다. 이는 안드로이드 프레임워크의 일부로, 개발자가 앱 생명주기, 메모리 사용량, 태스크 관리의 여러 측면과 상호작용하고 제어할 수 있게 해줍니다. ActivityManager의 주요 기능은 다음과 같습니다.

* **태스크 및 액티비티 정보:** ActivityManager는 실행 중인 태스크, 액티비티 및 해당 스택 상태에 대한 세부 정보를 검색할 수 있습니다. 이는 개발자가 앱 동작 및 시스템 리소스 사용량을 모니터링하는 데 도움이 됩니다.
* **메모리 관리:** 앱별 메모리 소비량 및 시스템 전체 메모리 상태를 포함하여 시스템 전반의 메모리 사용량에 대한 정보를 제공합니다. 개발자는 이를 사용하여 앱 성능을 최적화하고 메모리 부족 상황을 처리할 수 있습니다.
* **앱 프로세스 관리:** ActivityManager를 사용하면 실행 중인 앱 프로세스 및 서비스에 대한 세부 정보를 조회할 수 있습니다. 개발자는 이 정보를 사용하여 앱 상태를 감지하거나 프로세스 수준 변경에 응답할 수 있습니다.
* **디버깅 및 진단:** 힙 덤프 생성이나 앱 프로파일링과 같은 디버깅 도구를 제공하여 성능 병목 현상이나 메모리 누수를 식별하는 데 도움을 줄 수 있습니다.

### ActivityManager의 일반적인 메서드

* **`getRunningAppProcesses()`**: 현재 기기에서 실행 중인 프로세스 목록을 반환합니다.
* **`getMemoryInfo(ActivityManager.MemoryInfo memoryInfo)`**: 사용 가능한 메모리, 임계 메모리, 기기가 메모리 부족 상태인지 여부 등 시스템에 대한 상세한 메모리 정보를 검색합니다. 이는 메모리 부족 상황에서 앱 동작을 최적화하는 데 유용합니다.
* **`killBackgroundProcesses(String packageName)`**: 시스템 리소스를 확보하기 위해 지정된 앱의 백그라운드 프로세스를 종료합니다. 리소스 집약적인 앱을 테스트하거나 관리하는 데 유용합니다.
* **`isLowRamDevice()`**: 기기가 저사양 RAM으로 분류되는지 확인하여, 앱이 저사양 메모리 기기에 맞게 리소스 사용량을 최적화하는 데 도움을 줍니다.
* **`appNotResponding(String message)`**: 테스트 목적으로 ANR(Application Not Responding, 애플리케이션 응답 없음) 이벤트를 시뮬레이션합니다. 디버깅 중에 앱이 ANR 상황에서 어떻게 동작하거나 응답하는지 이해하는 데 사용할 수 있습니다.
* **`clearApplicationUserData()`**: 파일, 데이터베이스, 공유 환경설정(SharedPreferences)을 포함하여 애플리케이션과 관련된 모든 사용자별 데이터를 지웁니다. 공장 초기화나 앱을 기본 상태로 재설정하는 경우에 자주 사용됩니다.

### 사용 예시

아래 코드는 ActivityManager를 사용하여 메모리 정보를 가져오는 방법을 보여줍니다.

```kotlin
val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
val memoryInfo = ActivityManager.MemoryInfo()
activityManager.getMemoryInfo(memoryInfo)

Log.d(TAG, "Low memory state: ${memoryInfo.lowMemory}")
Log.d(TAG, "Threshold memory: ${memoryInfo.threshold / (1024 * 1024)} MB")

val processes = activityManager.runningAppProcesses
Log.d(TAG, "Process name: ${processes.first().processName}")

// Method for the app to tell system that it's wedged and would like to trigger an ANR.
activityManager.appNotResponding("App is not responding")

// Permits an application to erase its own data from disk.
activityManager.clearApplicationUserData()
```

### LeakCanary에서의 ActivityManager

LeakCanary는 Block 사에서 유지보수하는 안드로이드 애플리케이션용 오픈소스 메모리 누수 탐지 라이브러리입니다. 개발 중에 앱의 메모리 누수를 자동으로 모니터링하고 감지하며, 누수를 효율적으로 수정하는 데 도움이 되는 상세한 분석과 실행 가능한 통찰력을 제공합니다. LeakCanary는 메모리 상태 및 정보를 추적하기 위해 ActivityManager를 활용합니다.

### 요약

ActivityManager는 시스템 수준 관리, 성능 튜닝 및 앱 동작 모니터링을 위한 것입니다. 최신 안드로이드에서는 그 기능이 더 특화된 API로 부분적으로 대체되었지만, 안드로이드 애플리케이션에서 리소스 사용량을 관리하고 최적화하는 도구로 여전히 남아 있습니다. 개발자는 의도하지 않은 시스템 성능 영향을 피하기 위해 책임감 있게 사용해야 합니다.

---

## Q. `ActivityManager.getMemoryInfo()`를 사용하여 앱 성능을 어떻게 최적화할 수 있으며, 시스템이 메모리 부족 상태에 들어갔을 때 개발자는 어떤 조치를 취해야 하나요?

1. ActivityManager.getMemoryInfo()를 사용한 앱 성능 최적화 방법
ActivityManager.getMemoryInfo()는 시스템의 현재 메모리 상태에 대한 정보를 제공하여 앱 성능을 간접적으로 최적화하는 데 도움을 줄 수 있습니다. 하지만 실시간으로 앱 동작을 직접 바꾸는 것보다는, 주로 앱의 전반적인 메모리 사용 패턴을 이해하고 특정 상황에 대응하는 데 활용됩니다.
 * isLowRamDevice() 확인 (사전 최적화):
   ActivityManager.isLowRamDevice() (API 19 이상)를 앱 시작 시 호출하여 기기가 저사양 RAM 기기인지 확인할 수 있습니다. true일 경우, 앱은 처음부터 메모리를 적게 사용하는 모드로 동작하도록 설계할 수 있습니다. 예를 들어, 고해상도 이미지 대신 저해상도 이미지 사용, 복잡한 애니메이션 비활성화, 백그라운드 작업 빈도 줄이기 등의 전략을 취할 수 있습니다.
 * 현재 메모리 상태 파악 (정보 기반 결정):
   activityManager.getMemoryInfo(memoryInfo)를 호출하면 ActivityManager.MemoryInfo 객체를 통해 다음 정보를 얻을 수 있습니다.
   * memoryInfo.availMem: 현재 사용 가능한 시스템 메모리.
   * memoryInfo.totalMem: 시스템 전체 메모리.
   * memoryInfo.threshold: 시스템이 메모리 부족 상태로 간주하는 임계값. 사용 가능한 메모리가 이 값 이하로 떨어지면 lowMemory 플래그가 true가 됩니다.
   * memoryInfo.lowMemory: 시스템이 현재 메모리 부족 상태인지 여부를 나타내는 boolean 값.
   이 정보를 바탕으로 앱은 다음과 같은 결정을 내릴 수 있습니다.
   * 만약 availMem이 지속적으로 낮다면, 앱 자체의 메모리 사용량이 너무 크거나 기기에 실행 중인 다른 프로세스가 많다는 신호일 수 있습니다. 이는 앱의 전반적인 메모리 최적화(예: 메모리 누수 제거, 불필요한 객체 줄이기) 필요성을 나타냅니다.
   * memoryInfo.lowMemory가 true일 때, 앱은 즉시 메모리 사용량을 줄이는 조치를 취해야 합니다. (아래 "메모리 부족 상태 시 개발자 조치" 참조)
 * 주의사항: 콜백 우선 사용:
   getMemoryInfo()를 주기적으로 호출하여 메모리 상태를 폴링(polling)하는 것보다, 안드로이드 시스템이 제공하는 생명주기 콜백 메서드인 onLowMemory() (Activity, Fragment, Service 등에서 사용 가능) 또는 onTrimMemory(int level) (ComponentCallbacks2 인터페이스, 주로 Activity에서 구현)를 사용하는 것이 훨씬 효율적이고 권장되는 방식입니다. 이 콜백들은 시스템이 메모리 부족 상황을 감지했을 때 앱에 직접 알려주므로, 앱이 적시에 대응할 수 있게 합니다. getMemoryInfo()는 이러한 콜백 외부에서 현재 메모리 상태를 확인해야 하는 특정 상황이나 진단 목적으로 유용할 수 있습니다.

2. 시스템이 메모리 부족 상태에 들어갔을 때 개발자가 취해야 할 조치
시스템이 메모리 부족 상태임을 알렸을 때 (예: memoryInfo.lowMemory가 true이거나, onLowMemory(), onTrimMemory() 콜백이 호출되었을 때), 개발자는 앱이 시스템에 의해 강제 종료(killed)되는 것을 피하기 위해 적극적으로 메모리 사용량을 줄여야 합니다.
 * 일반 원칙: 현재 앱의 핵심 기능 수행에 필수적이지 않은 모든 메모리를 해제합니다. 특히 사용자에게 보이지 않는 컴포넌트의 리소스를 우선적으로 정리합니다.
 * 구체적인 조치 사항:
   * 캐시(Cache) 해제:
     * 메모리 내 캐시(예: LruCache를 사용한 비트맵 캐시, 데이터 객체 캐시)를 비웁니다.
     * 디스크 캐시도 너무 크다면 정리하여 저장 공간을 확보하고, I/O 작업을 줄여 간접적으로 메모리 압박을 줄일 수 있습니다.
   * UI 관련 리소스 해제:
     * 현재 사용자에게 보이지 않는 UI 컴포넌트(예: 백 스택에 있는 액티비티, 화면에서 사라진 프래그먼트)와 관련된 리소스(큰 이미지, 복잡한 드로어블, 쉽게 다시 만들 수 있는 뷰 객체 등)를 해제합니다. onTrimMemory()의 TRIM_MEMORY_UI_HIDDEN 레벨이 이를 위한 신호입니다.
   * 백그라운드 작업 축소 또는 중단:
     * 필수적이지 않은 백그라운드 스레드나 서비스의 실행을 중지하거나 일시 중지합니다.
     * 데이터 동기화나 업데이트 빈도를 줄입니다.
   * 대형 객체 참조 해제:
     * 즉시 필요하지 않은 대형 배열, 컬렉션, 디코딩된 비트맵 등의 객체에 대한 참조를 명시적으로 해제(null 할당 등)하여 GC가 수집할 수 있도록 합니다.
   * 데이터 구조 최적화:
     * 메모리에 큰 데이터 세트를 유지하고 있다면, 페이징(paging)이나 윈도윙(windowing) 같은 전략을 사용하거나 더 메모리 효율적인 데이터 구조를 사용하는 것을 고려합니다.
   * onLowMemory() 및 onTrimMemory(int level) 콜백 적극 활용:
     * onLowMemory(): 일반적으로 onTrimMemory(TRIM_MEMORY_COMPLETE)와 유사하게 간주되며, 가능한 많은 리소스를 해제해야 합니다.
     * onTrimMemory(int level): 메모리 부족 수준에 따라 더 세분화된 조치를 취할 수 있습니다.
       * TRIM_MEMORY_RUNNING_MODERATE, TRIM_MEMORY_RUNNING_LOW, TRIM_MEMORY_RUNNING_CRITICAL: 앱이 포그라운드에 있지만 시스템 메모리가 부족해지고 있는 상황입니다. 중요하지 않은 리소스를 점진적으로 해제합니다.
       * TRIM_MEMORY_UI_HIDDEN: 앱의 UI가 더 이상 보이지 않습니다. UI 관련 리소스를 대거 해제합니다.
       * TRIM_MEMORY_BACKGROUND, TRIM_MEMORY_MODERATE, TRIM_MEMORY_COMPLETE: 앱이 LRU(Least Recently Used) 목록에 있으며 시스템에 의해 종료될 가능성이 높습니다. TRIM_MEMORY_COMPLETE 수준에서는 앱이 곧 종료될 수 있으므로, 다시 시작될 때 상태를 복원하는 데 필요한 최소한의 것을 제외하고 모든 것을 해제해야 합니다.

이러한 조치들을 통해 앱은 시스템 리소스를 효율적으로 사용하고, 메모리 부족 상황에서도 안정적으로 동작하며, 사용자 경험을 향상시킬 수 있습니다.

