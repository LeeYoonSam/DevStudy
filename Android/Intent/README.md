# Intent
- [Intent란?](#intent란)

---

## [Intent란?](https://blog.mindorks.com/what-are-intents-in-android)

### Intent 는 무엇인가?
- Intent는 다른 앱 구성 요소에서 작업을 요청하는데 사용할 수 있는 메시징 개체이다.
- Intent는 Android 애플리케이션의 다양한 구성 요소간에 통신하는 데 사용되는 메시징 서비스로 생각할 수 있다.

### Intent Types
**명시적 인텐트(Explicit Intents)**
- 애플리케이션 구성 요소 간의 통신만 원하면 명시적 인텐트를 사용할 수 있다.
- 동일한 응용 프로그램의 특정 구성 요소와 통신하는데 사용
- 현재 액티비티에서 버튼 클릭으로 액티비티를 시작하려면 원하는 액티비티의 정규화된 주소를 지정하여 해당 액티비티를 시작할 수 있다.
- 정규화된 주소가 필요하므로 자신의 애플리케이션에서 이 접근 방식을 사용할 수 있다.

**Implicit Intents**
- 정규화된 주소를 지정할 필요가 없다.
- 인텐트가 수행할 작업을 지정해서 사용
- 기기에 있는 다양한 애플리케이션간에 통신할 수 있다.
- 다른 애플리케이션에서 위치 데이터에 액세스하여 현재 위치에 액세스할 수 있다.
- Intent Filter는 애플리케이션에서 수신할 수 있는 구성요소 또는 작업의 유형을 지정하는데 사용되는 표현식이며, AndroidManifest.xml 에 선언 되어 있다.

### Intent 사용 사례
- Android 애플리케이션의 다양한 구성 요소 간에 통신하는 데 사용된다.
- 통신은 다양한 방법으로 수행할 수 있지만 일반적으로 Intents 의 세 가지 사용 사례가 있다.

1. 액티비티를 시작
- Intent 를 사용하여 특정 액티비티를 시작할 수 있다.
- startActivity() 를 사용해서 다른 액티비티를 시작

2. 서비스를 시작
- 서비스는 백그라운드에서 특정 작업을 수행하는 구성 요소
- Intent 를 사용하여 서비스를 시작할 수 있다.
- API 레벨 21 이상에서는 JobScheduler 를 사용하여 서비스를 시작할 수 있다.
- API 레벨 21 보다 낮은 경우 서비스 클래스를 사용하여 동일한 결과를 얻을 수 있다.

3. 브로드캐스트를 전달
- 브로드캐스트는 시스템에서 애플리케이션이 수신하는 메시지
- 브로드캐스트를 사용하여 디바이스에 있는 애플리케이션에 일종의 메시지를 보낼 수 있다.
- sendBroadcast(), sendOrderedBroadcast() 에 전달해서 수행

### Intent 에 있는 정보
- Android 시스템이 특정 액티비티 또는 작업이 호출되어야 하는 정보를 Intent 에 있는 정보를 읽어서 수행
- 인텐트에 있는 정보를 읽고 이정보를 기반으로 시스템이 시작할 액티비티를 결정

***Intent 에 포함된 몇 가지 기본 정보*
1. Action
    - 액션은 특정 액티비티에서 수행할 작업을 지정하는 문자열
    - 다른 애플리케이션과 일부 데이터를 공유하는 데 사용
    - ACTION_VIEW: 이미지 등의 정보가 포함되어있을때 startActivity 와 같이 사용
    - ACTION_SEND 를 실행해서 다른 애플리케이션에 데이터를 공유

2. Data
    - Intent를 생성하는 동안 Intent의 도움으로 Android 시스템에서 작업을 수행할 데이터 및 데이터 유형을 전달할 수 있습니다.
    - ACTION_EDIT 작업에서 데이터의 URI 를 전달해야 한다.

3. Category
    - 특정 작업을 수행하는 데 사용할 애플리케이션 유형을 지정하는 경우에 사용
    - 일부 데이터를 보내려면 데이터 전송 애플리케이션만 사용자가 선택할 수 있어야 하는데 이때 addCategory() 를 사용하여 Category 를 지정

4. Component Name
    - 시작할 구성 요소의 이름
    - setComponent() 또는 setClass() 또는 Intent Constructor 를 사용하여 구성 요소 이름을 설정할 수 있다.

5. Extras
- 키-값 쌍의 형태로 인텐트에 데이터를 추가할 수 있으며, Extra 정보는 한 액티비티에서 다른 액티비티로 전달할 수 있다.
- putExtra() 는 인텐트에 일부 데이터를 추가하는데 사용되며 이 메서드는 두 개의 매개변수, 즉 Key 와 Value 를 허용

### 명시적 인텐트(Explicit Intent) 샘플
- 구성 요소의 정규화된 주소를 제공하여 구성 요소를 시작할 수 있다.
- 특정 구성 요소만 명시적 인텐트를 사용하려면 Intent 객체를 만들어야 한다.

```kotlin
// Executed in an Activity, so 'this' is the Context
// The fileUrl is a string URL, such as "http://www.example.com/image.png"
val downloadIntent = Intent(this, DownloadService::class.java)
downloadIntent.data = Uri.parse(fileUrl)
startService(downloadIntent)
```
- 위의 예에서 Intent 생성자를 사용하여 Intent를 생성하고 생성자에서 현재 컨텍스트와 실행하려는 구성 요소를 전달한 것을 볼 수 있습니다. 
- 여기에서 DownloadService가 명시적으로 호출됩니다.

### 암시적 인텐트(Implicit Intent) 샘플
- Android 시스템이 특정 작업을 수행하기 위해 디바이스에 있는 애플리케이션 중에서 선택하도록 하려면 암시적 인텐트를 사용
- 예를 들면 이미지를 보내려면 Android 시스템에서 Gmail, Instagram, Fb, WhatsApp 등의 옵션을 제공합니다.
- 액션 유형을 전달해야 하며 이 액션 유형의 도움으로 시스템이 원하는 애플리케이션을 호출합니다.

```kotlin
// Create the text message with a string
val sendIntent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, textMessage)
    type = "text/plain"
}

// Verify that the intent will resolve to an activity
if (sendIntent.resolveActivity(packageManager) != null) {
    startActivity(sendIntent)
}
```
- 위의 예에서 startActivity()가 호출되면 시스템은 문자 메시지를 보낼 수 있는 애플리케이션을 검색하고 동일한 작업을 수행할 수 있는 애플리케이션 목록을 사용자에게 제공합니다.



