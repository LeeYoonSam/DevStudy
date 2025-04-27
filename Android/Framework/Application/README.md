# Application class란 무엇인가요?

안드로이드의 애플리케이션 클래스는 글로벌 애플리케이션 상태와 라이프사이클을 유지하기 위한 기본 클래스 역할을 한다. 액티비티, 서비스 또는 방송 수신기와 같은 다른 구성 요소보다 먼저 초기화되는 앱의 진입점 역할을 합니다. Application Class 는 앱의 수명 주기 전반에 걸쳐 사용할 수 있는 컨텍스트를 제공하므로 공유 리소스를 초기화하는 데 이상적입니다.

### Application Class 의 목적

Application Class는 전역 상태를 유지하고 응용 프로그램 전체 초기화를 수행하도록 설계되었습니다. 개발자는 종종 이 클래스를 재정의하여 종속성을 설정하고, 라이브러리를 구성하고, 액티비티와 서비스 전반에 걸쳐 지속해야 하는 리소스를 관리합니다.

기본적으로 AndroidManifest.xml 파일에 사용자 지정 클래스가 지정되지 않는 한 모든 Android 애플리케이션은 Application 클래스의 기본 구현을 사용합니다.

### Application Class 의 주요 메서드

1. onCreate(): 메서드는 앱 프로세스가 생성될 때 호출됩니다. 이것은 일반적으로 데이터베이스 인스턴스, 네트워크 라이브러리 또는 분석 도구와 같은 애플리케이션 전체 종속성을 초기화하는 곳입니다. 애플리케이션 수명 주기 동안 한 번만 호출됩니다.
2. onTerminate(): 이 메서드는 에뮬레이트된 환경에서 애플리케이션이 종료될 때 호출됩니다. 안드로이드는 호출을 보장하지 않기 때문에 프로덕션의 실제 장치에서 호출되지 않습니다.
3. onLowMemory() 및 onTrimMemory(): 이러한 메서드는 시스템이 낮은 메모리 조건을 감지하면 트리거됩니다. onLowMemory()는 이전 API 레벨에 사용되는 반면, onTrimMemory()는 앱의 현재 메모리 상태에 따라 더 세분화된 제어를 제공합니다.

### Application Class 를 사용하는 방법

사용자 지정 Application Class 를 정의하려면 Application Class를 확장하고 AndroidManifest.xml 파일에<application> 태그 아래에서 지정해야 합니다.

```kotlin
class CustomApplication : Application() {
 
     override fun onCreate() {
         super.onCreate()
         // Initialize global dependencies
         initializeDatabase()
         initializeAnalytics()
    }

    private fun initializeDatabase() {
        // Set up a database instance
    }

    private fun initializeAnalytics() {
        // Configure analytics tracking
    }
}

// AndroidManifest.xml
<application
    android:name=".CustomApplication"
    ... >
    ...
</application>
```

### Application Class를 위한 사용 사례

1. 글로벌 리소스 관리: 데이터베이스, 공유 환경 설정 또는 네트워크 클라이언트와 같은 리소스를 한 번 설정하고 재사용할 수 있습니다.
2. 구성 요소 초기화: Firebase Analytics, Timber 및 기타 유사한 개체와 같은 도구는 앱의 시작 프로세스 중에 적절하게 초기화되어 앱의 수명 주기 전반에 걸쳐 원활한 기능을 보장해야 합니다.
3. 종속성 주입: Dagger 또는 Hilt와 같은 프레임워크를 초기화하여 앱 전체에 종속성을 제공할 수 있습니다.

### Best Practices

1. 앱 실행 지연을 방지하기 위해 onCreate()에서 오래 실행되는 작업을 수행하지 마십시오.
2. 응용 프로그램 클래스를 관련 없는 논리의 덤핑 그라운드로 사용하지 마십시오. 글로벌 초기화 및 리소스 관리에 초점을 맞추십시오.
3. 응용 프로그램 클래스에서 공유 리소스를 관리할 때 스레드 안전을 보장합니다.

### 요약

애플리케이션 클래스는 앱 전체에서 사용할 수 있어야 하는 리소스를 초기화하고 관리하기 위한 중앙 장소입니다. 글로벌 구성을 위한 중요하고 기본적인 API이지만, 명확성을 유지하고 불필요한 복잡성을 피하기 위해 사용은 진정한 글로벌 작업으로 제한되어야 합니다.


## Q. 응용 프로그램 클래스의 목적은 무엇이며, 라이프사이클 및 리소스 관리 측면에서 액티비티와 어떻게 다른가요?
    
  **1. Application 클래스의 목적:**
  
  - 앱 **프로세스 전체**에서 공유되어야 하는 **전역 상태(global state)**를 관리합니다.
  - 앱 시작 시 **최초로 초기화**되어야 하는 **싱글톤 객체**(예: 네트워크 클라이언트, 데이터베이스 객체, DI 컨테이너, 분석 SDK 등)를 생성하고 관리하는 데 사용됩니다.
  - 앱 프로세스가 살아있는 동안 **단 하나의 인스턴스**만 존재합니다.
  
  **2. 라이프사이클 차이:**
  
  - **Application:**
      - 앱 **프로세스**의 생명주기와 거의 동일하게 동작합니다.
      - 프로세스 시작 시 `onCreate()`가 한 번 호출되고, 프로세스가 종료될 때까지 유지됩니다. (단, `onTerminate()`는 호출이 보장되지 않습니다.)
      - Activity처럼 사용자와의 상호작용에 따른 복잡한 상태 변화(Pause, Stop 등)는 거의 없습니다.
  - **Activity:**
      - **사용자 인터페이스 화면**의 생명주기를 따릅니다.
      - 사용자의 상호작용(화면 진입/이탈, 백그라운드 전환 등)에 따라 `onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()`, `onDestroy()` 등 **복잡한 상태 변화**를 겪습니다.
      - 앱 실행 중 여러 번 생성되고 소멸될 수 있습니다.
  
  **3. 리소스 관리 차이:**
  
  - **Application:**
      - **앱 전역에서 필요한, 오래 지속되는 리소스**나 싱글톤 객체를 관리하기에 적합합니다.
      - 주의: Activity Context나 View 등 특정 Activity에 종속된 리소스를 Application 클래스에서 직접 참조하면 해당 Activity가 소멸되어도 메모리에서 해제되지 않아 **메모리 누수(Memory Leak)**의 원인이 될 수 있습니다.
  - **Activity:**
      - **해당 화면(UI)에 필요한 리소스**를 관리합니다.
      - 리소스는 Activity의 라이프사이클에 맞춰 생성하고, `onStop()`이나 `onDestroy()` 등 적절한 시점에 **반드시 해제**하여 메모리 누수를 방지해야 합니다.