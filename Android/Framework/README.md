# Android Framework

- [Android란 무엇인가요?](./Android/README.md)
- [Intent란 무엇인가요?](./Intent/README.md)
- [Pending Intent 의 목적은 무엇인가요?](./PendingIntent/README.md)
- [Serializable 과 Parcelable 의 차이점은 무엇인가요?](./SerializableParcelable/README.md)
- [Context란 무엇이며 어떤 유형의 컨텍스트가 있나요?](./Context/README.md)
- [Application class란 무엇인가요?](./Application/README.md)
- [AndroidManifest 파일의 용도는 무엇인가요?](./Manifest/README.md)
- [액티비티 생명주기 설명](./Activity/README.md)
- [프래그먼트 생명주기 설명](./Fragment/README.md)
- [서비스(Service)란 무엇인가?](./Service/README.md)
- [BroadcastReceiver(브로드캐스트 리시버)란 무엇인가?](./BroadcastReceiver/README.md)
- [콘텐츠 제공자(ContentProvider)의 목적은 무엇이며, 애플리케이션 간의 안전한 데이터 공유를 어떻게 용이하게 하나요?](./ContentProvider/README.md)

---

# ANR 오류의 주요 원인은 무엇이며, 발생을 어떻게 예방할 수 있나요?
ANR (Application Not Responding, 애플리케이션 응답 없음) 은 안드로이드에서 앱의 메인 스레드(UI 스레드) 가 너무 오랫동안(일반적으로 5초 이상) 차단(blocked)될 때 발생하는 오류입니다. ANR이 발생하면 안드로이드는 사용자에게 앱을 닫거나 응답을 기다릴지 묻는 메시지를 표시합니다. ANR은 사용자 경험을 저하시키며 다음과 같은 다양한 요인에 의해 발생할 수 있습니다.
 * 메인 스레드에서 5초 이상 걸리는 과도한(무거운) 연산 수행
 * 오래 실행되는 네트워크 또는 데이터베이스 작업
 * UI를 차단하는 작업 (예: UI 스레드에서의 동기(synchronous) 작업)
ANR 예방 방법
ANR을 예방하려면 무겁거나 시간이 오래 걸리는 작업을 다른 곳으로 옮겨 메인 스레드의 응답성을 유지하는 것이 중요합니다. 다음은 몇 가지 모범 사례입니다.
 * 집중적인 작업을 메인 스레드 밖으로 옮기기: 파일 입출력, 네트워크 요청 또는 데이터베이스 작업과 같은 작업을 처리하기 위해 백그라운드 스레드(예: AsyncTask(현재 deprecated), Executors, Thread)를 사용하세요. 더 현대적이고 안전한 접근 방식을 원한다면, 효율적인 백그라운드 작업 관리를 위해 Dispatchers.IO와 함께 코틀린 코루틴(Kotlin Coroutines) 을 활용하세요.
 * 지속적인 작업에 WorkManager 사용: 데이터 동기화와 같이 백그라운드에서 실행되어야 하는 작업에는 WorkManager(카테고리 3: 비즈니스 로직에서 나중에 다룰 내용)를 사용하세요. 이 API는 작업이 메인 스레드 외부에서 예약되고 실행되도록 보장합니다.
 * 데이터 가져오기 최적화: Paging 라이브러리를 구현하여 대규모 데이터 세트를 효율적으로 처리하세요. 데이터를 관리 가능한 작은 단위(청크)로 가져와 UI 과부하를 방지하고 성능을 개선합니다.
 * 구성 변경 시 UI 작업 최소화: ViewModel을 활용하여 UI 관련 데이터를 유지하고, 화면 회전과 같은 구성 변경 중에 불필요한 UI 재로드를 피하세요.
 * Android Studio로 모니터링 및 프로파일링: Android Studio의 프로파일러(Profiler) 도구를 활용하여 CPU, 메모리 및 네트워크 사용량을 모니터링하세요. 이러한 도구는 ANR을 유발할 수 있는 성능 병목 현상을 식별하고 해결하는 데 도움이 됩니다.
 * 차단 호출 피하기: 부드러운 앱 성능을 보장하기 위해 메인 스레드에서 긴 루프, sleep 호출 또는 동기식 네트워크 요청과 같은 차단(blocking) 작업을 피하세요.
 * 짧은 지연에 Handler 사용: 반응성 좋은 앱 경험을 위해 Thread.sleep() 대신 Handler.postDelayed()를 사용하여 메인 스레드를 차단하지 않고 짧은 지연을 도입하세요.
요약
ANR(Application Not Responding)은 앱의 메인 스레드(UI 스레드)가 일반적으로 5초 이상 차단될 때 발생하는 안드로이드 오류이며, 사용자 경험을 저하시키고 현재 사용자의 모든 상태를 잃게 만듭니다. ANR을 예방하려면 네트워크에서 데이터 요청, 데이터베이스 쿼리, 무거운 연산 작업 수행 등 집중적인 작업을 백그라운드 스레드로 옮겨 메인 스레드를 가볍게 유지해야 합니다. 또한 데이터 작업을 최적화하고 Android Studio 프로파일러를 사용하여 앱을 프로파일링할 수 있습니다. 자세한 내용은 ANR에 대한 공식 안드로이드 문서를 참조하세요.

## Q. ANR을 어떻게 탐지 및 진단하고 앱 성능을 개선할 수 있나요?
ANR(애플리케이션 응답 없음) 탐지 및 진단 방법
ANR이 발생하는 것을 감지하고 그 원인을 진단하는 방법은 다음과 같습니다.
 * 사용자 보고 및 Google Play Console (Android Vitals):
   * 사용자가 직접 "앱이 응답하지 않습니다" 대화 상자를 경험하고 보고하는 것이 가장 직접적인 신호입니다.
   * Google Play Console의 'Android Vitals' 섹션은 실제 사용자 환경에서 발생한 ANR 데이터를 자동으로 수집하고 집계해 보여줍니다. ANR 발생률, 유사한 ANR 클러스터, 발생 시점의 스택 트레이스(메인 스레드뿐 아니라 관련 스레드 정보 포함), 앱 버전, 기기, OS 버전별 통계를 제공하여 운영 환경에서의 ANR 문제를 파악하는 데 가장 중요합니다.
 * Firebase Crashlytics:
   * 주로 비정상 종료(Crash) 리포팅 도구이지만, ANR 발생 시 관련 정보(스택 트레이스, 기기 정보 등)를 함께 리포팅하도록 설정할 수 있어 통합적인 분석에 도움이 됩니다.
 * Android Studio CPU Profiler:
   * 개발 및 테스트 단계에서 CPU 프로파일러를 사용하여 앱의 스레드 활동을 모니터링합니다. 앱이 응답하지 않을 때 메인 스레드(보통 'main' 또는 'UI Thread'로 표시됨)의 상태를 확인합니다. 메인 스레드에서 오랜 시간 동안 멈춰 있거나 특정 메서드가 길게 실행되는 구간이 보인다면 ANR의 원인일 가능성이 높습니다. 'Sampled (Java)' 또는 'Trace System Calls' 모드를 활용하여 분석할 수 있습니다.
 * /data/anr/ traces.txt 파일 분석 (ADB 또는 루팅 필요):
   * 기기에서 ANR이 발생하면 시스템은 /data/anr/ 디렉토리에 traces.txt (또는 유사한 이름) 파일을 생성합니다. 이 파일에는 ANR 발생 시점의 모든 스레드에 대한 스택 트레이스가 포함되어 있어 가장 상세한 진단 정보를 제공합니다. 특히 메인 스레드가 어떤 작업을 하고 있었는지, 어떤 락(lock)을 기다리고 있었는지, 다른 스레드(특히 메인 스레드가 필요로 하는 락을 점유 중인 스레드)는 무엇을 하고 있었는지 분석하는 데 유용합니다. adb bugreport 명령어나 루팅된 기기에서 직접 접근하여 파일을 얻을 수 있습니다.
 * StrictMode:
   * 개발용 빌드에서 프로그래밍 방식(StrictMode.setThreadPolicy, StrictMode.setVmPolicy)이나 개발자 옵션을 통해 StrictMode를 활성화합니다. 이는 메인 스레드에서의 디스크 I/O나 네트워크 호출과 같은 잠재적인 차단 작업을 감지하여 로그를 남기거나 앱을 강제 종료(디버그 빌드에서)시켜 개발 초기에 ANR 유발 요인을 찾는 데 도움을 줄 수 있습니다.
 * 수동 테스트:
   * 느린 네트워크 환경, 대용량 데이터 처리, 복잡한 사용자 인터랙션 등 다양한 엣지 케이스(edge case)를 직접 테스트하며 앱이 응답하지 않는 상황이 발생하는지 확인합니다.
앱 성능 개선 방법 (ANR 예방 및 사용자 경험 향상)
앱 성능을 개선하는 것은 ANR을 예방하고 전반적인 사용자 경험을 향상시키는 핵심입니다.
 * 메인 스레드 작업 부하 줄이기 (가장 중요):
   * 코틀린 코루틴: I/O 작업에는 Dispatchers.IO, CPU 집약적 작업에는 Dispatchers.Default를 사용하여 백그라운드에서 실행합니다. viewModelScope나 lifecycleScope 등 적절한 CoroutineScope를 사용하여 생명주기를 관리합니다.
   * WorkManager: 앱이 종료되어도 안정적으로 실행되어야 하는 백그라운드 작업(데이터 동기화 등)에 사용합니다.
   * 스레드 풀/스레드: 기타 백그라운드 작업에는 Executors나 Thread를 사용하되, 생명주기 관리에 주의합니다.
 * UI 렌더링 최적화:
   * 뷰 계층 구조를 단순화하고 ConstraintLayout을 효율적으로 사용합니다.
   * 측정 및 레이아웃 단계(onMeasure, onLayout)에서 복잡한 계산을 피합니다.
   * RecyclerView를 효율적으로 사용합니다 (ListAdapter와 DiffUtil 사용, 아이템 레이아웃 최적화, onBindViewHolder 로직 간소화).
   * Jetpack Compose 사용을 고려하여 선언적 UI 방식으로 상태 관리 및 리컴포지션(recomposition)을 최적화합니다.
 * 데이터베이스 및 네트워크 작업 최적화:
   * 모든 DB 및 네트워크 호출은 반드시 백그라운드 스레드에서 수행합니다.
   * 효율적인 DB 쿼리(인덱싱 활용, 필요한 컬럼만 조회)를 사용하고 Room 라이브러리 사용을 권장합니다.
   * Retrofit, OkHttp 등 비동기 호출을 지원하는 네트워킹 라이브러리를 사용합니다.
   * 메모리 및 디스크 캐싱 전략을 구현하여 불필요한 데이터 요청을 줄입니다.
   * 대용량 데이터 로딩에는 Paging 라이브러리를 사용합니다.
 * 앱 시작 시간 단축:
   * 앱 시작 시 메인 스레드를 차단하는 작업이 너무 길면 ANR의 원인이 될 수 있습니다. App Startup 라이브러리를 사용하거나 Application.onCreate 내 초기화 로직을 최적화합니다. Baseline Profile 적용을 고려합니다.
 * 메모리 관리:
   * 메모리 누수를 방지하고 비트맵 사용을 최적화하여 잦은 GC로 인한 멈춤 현상을 줄입니다.
 * 주기적인 프로파일링:
   * 개발 중 Android Studio 프로파일러(CPU, 메모리, 네트워크, 에너지)를 꾸준히 사용하여 성능 병목 지점을 미리 찾아냅니다. 메인 스레드에서 오래 걸리는 메서드, 과도한 메모리 할당 등을 중점적으로 확인합니다.
 * 성능 모니터링 도구 활용:
   * Firebase Performance Monitoring과 같은 도구를 통합하여 운영 환경 사용자로부터 실제 성능 데이터(특정 작업 소요 시간, 네트워크 요청 시간 등)를 수집하고 분석합니다.
이러한 진단 및 개선 방법을 통해 ANR 발생 가능성을 줄이고 사용자에게 더 빠르고 안정적인 앱 경험을 제공할 수 있습니다.

---


