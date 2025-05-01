# BroadcastReceiver(브로드캐스트 리시버)란 무엇인가?

**BroadcastReceiver(브로드캐스트 리시버)** 는 앱이 시스템 전역 브로드캐스트 메시지나 앱 특정 브로드캐스트를 수신하고 이에 응답할 수 있게 하는 컴포넌트입니다. 이러한 브로드캐스트는 일반적으로 시스템이나 다른 애플리케이션에 의해 트리거(발생)되어 배터리 상태 변경, 네트워크 연결 상태 업데이트, 또는 앱 내에서 전송된 사용자 정의 인텐트(custom intents)와 같은 다양한 이벤트를 알립니다. BroadcastReceiver는 동적인 시스템 또는 앱 수준 이벤트에 반응하는 반응형 애플리케이션을 구축하는 데 유용한 메커니즘입니다.

**BroadcastReceiver의 목적**

BroadcastReceiver는 액티비티(Activity)나 서비스(Service)의 라이프사이클에 직접적으로 묶여 있지 않을 수 있는 이벤트를 처리하는 데 사용됩니다. 이는 앱이 백그라운드에서 계속 실행되지 않고도 변화에 반응할 수 있게 하는 메시징 시스템 역할을 하여 리소스를 절약합니다.

**브로드캐스트 유형**

* **시스템 브로드캐스트(System Broadcasts):** 안드로이드 운영체제가 배터리 잔량 변경, 시간대 업데이트, 또는 네트워크 연결 변경과 같은 시스템 이벤트를 앱에 알리기 위해 전송합니다.
* **사용자 정의 브로드캐스트(Custom Broadcasts):** 애플리케이션이 앱 내부 또는 앱 간에 특정 정보나 이벤트를 전달하기 위해 전송합니다.

**BroadcastReceiver 선언하기**

BroadcastReceiver를 생성하려면, `BroadcastReceiver` 클래스를 상속(extend)하고 브로드캐스트 처리를 위한 로직을 정의하는 `onReceive` 메서드를 재정의(override)해야 합니다.

```kotlin
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BATTERY_LOW) {
            // Handle battery low event
            Log.d("MyBroadcastReceiver", "Battery is low!")
        }
    }
}
```

**BroadcastReceiver 등록하기**

BroadcastReceiver는 두 가지 방법으로 등록할 수 있습니다:

1.  **정적 등록 (매니페스트를 통해 - Static Registration):** 앱이 실행 중이지 않을 때도 처리해야 하는 이벤트에 사용합니다. `AndroidManifest.xml` 파일에 `<intent-filter>`를 추가합니다.
```xml
<receiver android:name=".MyBroadcastReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BATTERY_LOW" />
    </intent-filter>
</receiver>
```

2.  **동적 등록 (컨텍스트를 통해 - Dynamic Registration):** `Context.registerReceiver()`를 사용하여 코드 내에서 리시버를 등록합니다. 앱이 활성화되어 있거나 특정 상태일 때만 처리해야 하는 이벤트에 이 기능을 사용합니다.

```kotlin
val receiver = MyBroadcastReceiver()
2 val intentFilter = IntentFilter(Intent.ACTION_BATTERY_LOW)
3 registerReceiver(receiver, intentFilter)
```

**중요 고려 사항**

* **[라이프사이클 관리(Lifecycle Management)](https://developer.android.com/develop/background-work/background-tasks/broadcasts#unregister-broadcast):** 동적 등록을 사용하는 경우, 메모리 누수를 방지하기 위해 `unregisterReceiver`를 사용하여 리시버 등록을 해제해야 합니다.
* **[백그라운드 실행 제한(Background Execution Limits)](https://developer.android.com/about/versions/oreo/background):** 안드로이드 8.0 (API 레벨 26)부터 백그라운드 앱은 특정 **[암시적 브로드캐스트 예외](https://developer.android.com/develop/background-work/background-tasks/broadcasts/broadcast-exceptions)** 를 제외하고는 브로드캐스트 수신에 제한을 받습니다. 이러한 경우를 처리하려면 `Context.registerReceiver` 또는 `JobScheduler` 등을 사용해야 합니다.
* **[보안(Security)](https://developer.android.com/develop/background-work/background-tasks/broadcasts#security-considerations):** 브로드캐스트에 민감한 정보가 포함된 경우, 무단 접근을 방지하기 위해 권한(permissions)으로 보호해야 합니다.

**BroadcastReceiver 사용 사례**

* 네트워크 연결 상태 변경 모니터링.
* SMS 또는 전화 이벤트에 응답.
* 충전 상태와 같은 시스템 이벤트에 반응하여 UI 업데이트.
* 사용자 정의 브로드캐스트를 이용한 작업 또는 알람 예약.

**요약**

BroadcastReceiver는 반응형 애플리케이션을 구축하는 데 필수적인 컴포넌트이며, 특히 OS 시스템과 상호작용하는 데 중요합니다. 이를 통해 앱은 시스템 또는 앱 이벤트를 효율적으로 수신하고 응답할 수 있습니다. 라이프사이클을 인식하는 등록 방식과 새로운 안드로이드 제한 사항 준수를 포함한 적절한 사용은 앱을 견고하고 리소스 효율적으로 유지하는 데 도움이 됩니다.

---

## Q. 브로드캐스트의 다른 유형들은 무엇이며, 시스템 브로드캐스트는 기능 및 사용 측면에서 사용자 정의 브로드캐스트와 어떻게 다른가요?

브로드캐스트에는 주로 메시지를 보내는 주체에 따라 **시스템 브로드캐스트(System Broadcasts)** 와 **사용자 정의 브로드캐스트(Custom Broadcasts)** 두 가지 주요 유형이 있습니다. 이 둘은 기능과 사용 측면에서 다음과 같은 차이점을 가집니다.

**1. 시스템 브로드캐스트 (System Broadcasts)**

* **발생 주체 (Origin):** 안드로이드 운영체제(OS) 자체.
* **목적 및 내용 (Purpose/Content):** 시스템 수준의 중요한 이벤트 발생을 알리는 것이 목적입니다. 예를 들어, 기기 부팅 완료(`ACTION_BOOT_COMPLETED`), 네트워크 연결 상태 변경(`CONNECTIVITY_ACTION`), 배터리 부족(`ACTION_BATTERY_LOW`), 시간대 변경, 패키지 설치/삭제 등의 이벤트가 있습니다. 내용은 해당 이벤트와 관련된 정보이며, 액션 문자열은 안드로이드 시스템에 의해 미리 정의되어 있습니다.
* **기능 및 사용 (Functionality & Usage):**
    * 앱이 시스템 상태 변화를 감지하고 이에 적절하게 반응하도록 돕습니다 (예: 네트워크가 끊겼을 때 데이터 동기화 중지, 배터리가 부족할 때 백그라운드 작업 제한).
    * 앱이 실행 중이지 않을 때도 시스템 이벤트를 수신해야 하는 경우가 많습니다 (단, Android 8.0 이상에서는 백그라운드 제한으로 인해 매니페스트에 등록하여 수신할 수 있는 암시적 시스템 브로드캐스트가 크게 제한됩니다).
    * 수신하려면 일반적으로 `AndroidManifest.xml`에 리시버를 등록(정적 등록)하거나 코드 내에서 `Context.registerReceiver()`를 사용(동적 등록)하고, 적절한 시스템 액션을 포함하는 `<intent-filter>`를 정의해야 합니다. 일부 브로드캐스트는 특정 시스템 권한(예: `RECEIVE_BOOT_COMPLETED`)을 요구합니다.

**2. 사용자 정의 브로드캐스트 (Custom Broadcasts)**

* **발생 주체 (Origin):** 개별 애플리케이션 (자신의 앱 또는 다른 앱).
* **목적 및 내용 (Purpose/Content):** 앱 고유의 이벤트나 정보를 다른 컴포넌트 또는 다른 앱에 알리는 것이 목적입니다. 예를 들어, "백그라운드 다운로드 완료", "사용자 로그아웃", "새로운 데이터 수신" 등 앱 개발자가 정의한 특정 상황을 알리는 데 사용됩니다. 전달되는 내용과 액션 문자열은 전적으로 브로드캐스트를 보내는 앱이 정의합니다.
* **기능 및 사용 (Functionality & Usage):**
    * **앱 내부 컴포넌트 간 통신:** 하나의 액티비티에서 발생한 이벤트를 다른 액티비티나 서비스에 알리는 데 사용될 수 있습니다. (단, 앱 내부 통신에는 `LocalBroadcastManager` - 현재는 deprecated - 대신 `LiveData`, `Flow`, 명시적 인텐트 등이 더 권장됩니다.)
    * **앱 간 통신:** 서로 다른 앱 간에 정보를 교환하는 데 사용될 수 있습니다. 이 경우, 수신 측 앱은 발신 측 앱이 정의한 액션 문자열로 `<intent-filter>`를 설정해야 합니다.
    * 개발자가 `sendBroadcast()` 메서드 등을 사용하여 직접 브로드캐스트를 전송합니다.
    * 보안이 중요합니다. 민감한 정보를 포함하거나 특정 앱만 수신하도록 하려면, 사용자 정의 권한(custom permission)을 정의하여 브로드캐스트를 보내거나 받는 데 해당 권한을 요구하도록 설정할 수 있습니다.

**주요 차이점 요약:**

| 구분                 | 시스템 브로드캐스트                               | 사용자 정의 브로드캐스트                             |
| :------------------- | :---------------------------------------------- | :------------------------------------------------- |
| **발생 주체** | 안드로이드 OS                                     | 애플리케이션                                         |
| **목적** | 시스템 전반의 이벤트 알림                           | 앱 고유의 이벤트 또는 정보 전달                        |
| **액션 문자열** | 시스템 정의 (예: `ACTION_BOOT_COMPLETED`)         | 앱 개발자 정의 (예: `com.myapp.ACTION_DOWNLOAD_COMPLETE`) |
| **제어권** | 앱은 수신 여부만 제어 가능                         | 앱이 전송 시점, 내용, 수신 대상(권한) 제어 가능        |
| **주요 용도** | 시스템 상태 변화 감지 및 대응                     | 앱 내/앱 간 컴포넌트 통신, 특정 이벤트 알림             |
| **백그라운드 제한** | Android 8.0+ 에서 암시적 브로드캐스트 수신 제한 | 명시적 전달 또는 권한 설정 시 제한 영향 적음            |
| **보안** | 일부 수신 시 시스템 권한 필요                     | 필요 시 사용자 정의 권한으로 보호 가능/권장             |