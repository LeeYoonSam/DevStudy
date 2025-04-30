# 서비스(Service)란 무엇인가?

**서비스(Service)** 는 앱이 사용자 상호작용과 독립적으로 **오래 실행되는 작업**을 수행할 수 있도록 하는 **백그라운드 컴포넌트**입니다. 액티비티(Activity)와 달리, 서비스는 사용자 인터페이스(UI)를 가지지 않으며 앱이 포그라운드(foreground)에 있지 않을 때도 계속 실행될 수 있습니다. 주로 파일 다운로드, 음악 재생, 데이터 동기화 또는 네트워크 작업 처리와 같은 백그라운드 작업에 사용됩니다.

1.  **시작된 서비스 (Started Service)**
    * 애플리케이션 컴포넌트가 `startService()`를 호출하면 서비스가 시작됩니다.
    * 서비스는 `stopSelf()`를 사용하여 스스로 중지하거나 `stopService()`를 사용하여 명시적으로 중지될 때까지 백그라운드에서 무기한 실행됩니다.
    * **사용 예시:**
        * 백그라운드 음악 재생
        * 파일 업로드 또는 다운로드
    ```kotlin
    class MyService : Service() {
      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
          // Perform long-running task in background
          return START_STICKY
      }

      override fun onBind(intent: Intent?): IBinder? = null
    }
    ```

2.  **바인드된 서비스 (Bound Service)**
    * 바인드된 서비스는 컴포넌트가 `bindService()`를 사용하여 서비스에 바인딩(연결)할 수 있도록 합니다.
    * 서비스는 바인드된 클라이언트가 있는 동안 활성 상태를 유지하며, 모든 클라이언트의 연결이 끊어지면 자동으로 중지됩니다.
    * **사용 예시:**
        * 원격 서버에서 데이터 가져오기
        * 백그라운드 블루투스 연결 관리
    ```kotlin
    class BoundService : Service() {
        private val binder = LocalBinder()

        inner class LocalBinder : Binder() {
            fun getService(): BoundService = this@BoundService
        }

        override fun onBind(intent: Intent?): IBinder = binder
    }
    ```

3.  **포그라운드 서비스 (Foreground Service)**
    * 포그라운드 서비스는 **사라지지 않는 알림(persistent notification)** 을 표시하면서 활성 상태를 유지하는 특별한 유형의 서비스입니다.
    * 음악 재생, 내비게이션 또는 위치 추적과 같이 지속적인 사용자 인지가 필요한 작업에 사용됩니다.
    ```kotlin
    class ForegroundService : Service() {
      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val notification = createNotification()
            startForeground(1, notification)
            return START_STICKY
        }

        private fun createNotification(): Notification {
            return NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Foreground Service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_notification)
                .build()
        }
    }
    ```

### 서비스 유형 간의 주요 차이점
Service Type | Runs in Background | Stops Automatically | UI Notification Required
--- | --- | --- | ---
Started Service | ✅ Yes | ❌ No | ❌ No
Bound Service | ✅ Yes | ✅ Yes(모든 클라이언트가 바인딩을 해제할 때) | ❌ No
Foreground Service | ✅ Yes | ❌ No | ✅ Yes

**서비스 사용 모범 사례**

* 즉시 실행할 필요가 없는 백그라운드 작업에는 서비스 대신 **Jetpack WorkManager** 사용을 고려하세요.
* 작업이 완료되면 서비스를 중지하여 불필요한 리소스 소비를 방지하세요.
* 포그라운드 서비스는 투명성을 위해 명확한 알림을 제공하여 책임감 있게 사용하세요.
* 메모리 누수를 방지하기 위해 라이프사이클 변경(서비스 라이프사이클)을 적절하게 처리하세요.

**요약**

서비스는 사용자 상호작용 없이 백그라운드 처리를 가능하게 합니다. 시작된 서비스는 수동으로 중지될 때까지 실행되고, 바인드된 서비스는 다른 컴포넌트와 상호작용하며, 포그라운드 서비스는 사라지지 않는 알림과 함께 활성 상태를 유지합니다. 서비스를 적절하게 관리하면 효율적인 시스템 리소스 사용과 원활한 사용자 경험을 보장할 수 있습니다.

---

## Q. 안드로이드에서 Started Service와 Bound Service의 차이점은 무엇이며, 각각 언제 사용해야 하나요?

안드로이드에서 **시작된 서비스(Started Service)** 와 **바인드된 서비스(Bound Service)** 는 백그라운드 작업을 처리하는 두 가지 주요 방식이며, 라이프사이클 관리 방식과 컴포넌트와의 상호작용 방식에서 중요한 차이가 있습니다.

**1. 시작된 서비스와 바인드된 서비스의 차이점**

| 특징               | 시작된 서비스 (Started Service)                                   | 바인드된 서비스 (Bound Service)                                                |
| :----------------- | :---------------------------------------------------------------- | :----------------------------------------------------------------------------- |
| **시작 방법** | `startService()` 호출                                             | `bindService()` 호출 (첫 클라이언트가 바인딩할 때)                               |
| **종료 방법** | `stopSelf()` 또는 `stopService()` 호출 시 명시적으로 중지             | 마지막 클라이언트가 `unbindService()` 호출 시 자동으로 중지                         |
| **라이프사이클** | 자신을 시작한 컴포넌트와 독립적. 명시적으로 중지될 때까지 계속 실행   | 자신에게 바인딩된 컴포넌트에 의존적. 바인딩된 클라이언트가 없을 시 소멸           |
| **상호작용 방식** | 주로 단방향 통신 (작업 수행 요청). 결과는 Broadcast, 알림 등으로 전달 | 양방향 통신 (클라이언트-서버). `onBind()` 통해 `IBinder` 인터페이스 제공, 직접 메서드 호출 가능 |
| **주요 목적** | 결과 반환 없이 독립적인 백그라운드 작업 수행 (Fire-and-forget)      | 다른 컴포넌트에 기능/데이터 제공, 지속적인 상호작용 또는 요청/응답 처리             |
| **다중 요청 처리** | `startService()` 여러 번 호출 시 `onStartCommand()`만 반복 호출됨   | `bindService()` 여러 번 호출 시 `onBind()`는 첫 바인딩 시에만 호출됨             |

**2. 각각의 사용 시나리오**

* **시작된 서비스 (Started Service) 사용 시나리오:**
    * **결과를 즉시 반환받을 필요 없는 장기 실행 작업:**
        * 백그라운드에서 음악 또는 비디오 재생 (제어는 알림이나 다른 방식으로 가능).
        * 대용량 파일 다운로드 또는 업로드.
        * 주기적인 데이터 동기화.
        * 네트워크 상태 변경 감지 후 특정 작업 수행 등.
    * **특정 이벤트에 의해 시작되어 독립적으로 완료되는 작업:**
        * 앱이 종료된 후에도 완료되어야 하는 작업 (단, 시스템에 의해 종료될 수 있음).

* **바인드된 서비스 (Bound Service) 사용 시나리오:**
    * **액티비티나 다른 컴포넌트와 지속적인 상호작용이 필요할 때:**
        * 액티비티가 백그라운드 음악 재생 서비스의 재생/일시정지/건너뛰기 등을 직접 제어해야 할 때.
        * 서비스가 수행하는 작업의 진행 상태를 UI에 실시간으로 표시해야 할 때.
    * **다른 컴포넌트에 특정 기능(API)을 제공해야 할 때:**
        * 복잡한 계산이나 데이터 처리 로직을 서비스에 두고 여러 액티비티/프래그먼트가 이를 호출하여 사용해야 할 때.
    * **애플리케이션 내 여러 컴포넌트가 공유하는 상태나 리소스를 관리할 때:**
        * 블루투스 연결 상태, 네트워크 연결 관리 등.
    * **프로세스 간 통신 (IPC)이 필요할 때:**
        * AIDL을 사용하여 다른 애플리케이션의 서비스와 직접 상호작용해야 할 때.

**요약:**

작업을 백그라운드에서 독립적으로 실행하고 완료하면 그만인 경우에는 **시작된 서비스**를 사용합니다. 반면, 액티비티나 다른 컴포넌트가 백그라운드 작업과 지속적으로 통신하거나 서비스가 제공하는 기능을 직접 호출해야 하는 경우에는 **바인드된 서비스**를 사용합니다. 때로는 하나의 서비스가 시작되면서 동시에 바인딩될 수도 있습니다 (예: 음악 플레이어).

---

### 💡 프로 팁 : 포그라운드 서비스는 어떻게 처리하나요?

**포그라운드 서비스(Foreground Service)** 는 사용자가 인지할 수 있는 작업을 수행하는 특별한 유형의 서비스입니다. 알림 표시줄에 **사라지지 않는 알림**을 표시하여 사용자가 서비스 작동을 인지하도록 보장합니다. 포그라운드 서비스는 미디어 재생, 위치 추적 또는 파일 업로드와 같은 높은 우선순위의 작업에 사용됩니다.

일반 서비스와의 주요 차이점은 포그라운드 서비스는 시작 즉시 `startForeground()`를 호출하고 알림을 표시해야 한다는 것입니다.

```kotlin
class ForegroundService : Service() {
 
     override fun onCreate() {
         super.onCreate()
         // Initialize resources
     }
 
     override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification) // Start service as foreground
        // Perform task
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ForegroundServiceChannel"
        val channel = NotificationChannel(
            notificationChannelId,
            "Foreground Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(chann\
el)
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Foreground Service")
            .setContentText("Running in the foreground")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
```

**일반 서비스와 포그라운드 서비스의 주요 차이점**

* **사용자 인지(User Awareness):** 표준 서비스는 사용자가 인지하지 못하는 상태로 백그라운드에서 실행될 수 있지만, 포그라운드 서비스는 알림을 요구하므로 사용자가 그 작동을 볼 수 있습니다.
* **우선순위(Priority):** 포그라운드 서비스는 일반 서비스보다 우선순위가 높으며, 메모리가 부족한 상황에서 시스템에 의해 종료될 가능성이 더 낮습니다.
* **사용 사례(Use Case):** 일반 서비스는 가벼운 백그라운드 작업에 이상적이며, 포그라운드 서비스는 지속적이고 사용자가 인지할 수 있는 작업에 적합합니다.

**서비스 사용 모범 사례**

* 작업이 완료되면 항상 서비스를 중지하여 시스템 리소스를 절약하세요.
* 즉시 실행할 필요가 없는 백그라운드 작업에는 `WorkManager`를 사용하세요.
* 포그라운드 서비스의 경우, 투명성을 유지하기 위해 알림이 사용자 친화적이고 정보를 제공하는지 확인하세요.

**요약**

안드로이드 서비스는 효율적인 백그라운드 작업 실행을 가능하게 하며, 포그라운드 서비스는 사용자 가시성이 필요한 지속적인 작업을 위해 사용됩니다. 둘 다 시스템 리소스를 효율적으로 관리하면서 원활한 사용자 경험을 만드는 데 중요한 역할을 합니다.

---

### 💡 숙련자를 위한 프로 팁: 서비스의 라이프사이클은 무엇인가요?

이전에 배운 것처럼, 서비스는 두 가지 모드로 작동할 수 있습니다:

* **시작된 서비스(Started Service):** `startService()`를 사용하여 시작되며, `stopSelf()` 또는 `stopService()`를 사용하여 명시적으로 중지될 때까지 계속 실행됩니다.
* **바인드된 서비스(Bound Service):** `bindService()`를 사용하여 하나 이상의 컴포넌트에 연결(바인딩)되며, 바인딩되어 있는 동안 존재합니다.

라이프사이클은 `onCreate()`, `onStartCommand()`, `onBind()`, `onDestroy()`와 같은 메서드를 통해 관리됩니다.

**시작된 서비스의 라이프사이클 메서드**

* **`onCreate()`**: 서비스가 처음 생성될 때 호출됩니다. 서비스에 필요한 리소스를 초기화하는 데 사용됩니다.
* **`onStartCommand()`**: `startService()`로 서비스가 시작될 때 호출됩니다. 이 메서드는 실제 작업 실행을 처리하고, 서비스가 시스템에 의해 종료될 경우 반환 값(예: `START_STICKY`, `START_NOT_STICKY`)을 사용하여 재시작 동작을 결정합니다.
* **`onDestroy()`**: `stopSelf()` 또는 `stopService()`를 사용하여 서비스가 중지될 때 호출됩니다. 리소스 해제나 스레드 중지와 같은 정리 작업을 위해 사용됩니다.

```kotlin
class SimpleStartedService : Service() {
    override fun onCreate() {
        super.onCreate()
        // Initialize resources
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Perform long-running task
        return START_STICKY // Restart if service is killed
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources
    }

    override fun onBind(intent: Intent?): IBinder? = null // Not used for started service
}
```

**바인드된 서비스의 라이프사이클 메서드**

* **`onCreate()`**: 시작된 서비스와 유사하게, 서비스가 생성될 때 리소스를 초기화합니다.
* **`onBind()`**: 컴포넌트가 `bindService()`를 사용하여 서비스에 바인딩할 때 호출됩니다. 이 메서드는 서비스에 대한 인터페이스(`IBinder`)를 제공합니다.
* **`onUnbind()`**: 마지막 클라이언트가 서비스에서 언바인딩(연결 해제)될 때 호출됩니다. 바인드된 클라이언트에 특정한 리소스를 정리하는 곳입니다.
* **`onDestroy()`**: 서비스가 종료될 때 호출됩니다. 리소스 정리 및 진행 중인 작업을 중지합니다.

```kotlin
class SimpleBoundService : Service() {
    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        ‘// Initialize resources
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder // Return the interface for the bound service
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // Clean up when no clients are bound
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources
    }

    inner class LocalBinder : Binder() {
        fun getService(): SimpleBoundService = this@SimpleBoundService
    }
}
```

**시작된 서비스와 바인드된 서비스 라이프사이클의 주요 차이점**

* **시작된 서비스(Started Service):** 어떤 컴포넌트와도 독립적이며 명시적으로 중지될 때까지 실행됩니다.
* **바인드된 서비스(Bound Service):** 최소 하나 이상의 클라이언트에 바인딩되어 있는 동안에만 존재합니다.

**요약**

서비스 라이프사이클을 이해하는 것은 효율적이고 안정적인 백그라운드 작업을 구현하는 데 매우 중요합니다. 시작된 서비스는 독립적인 작업을 수행하고 중지될 때까지 지속되는 반면, 바인드된 서비스는 클라이언트 상호작용을 위한 인터페이스를 제공하고 언바인딩되면 종료됩니다. 이러한 라이프사이클을 올바르게 관리하면 최적의 리소스 활용을 보장하고 메모리 누수를 방지할 수 있습니다.