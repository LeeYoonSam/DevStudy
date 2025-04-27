# AndroidManifest 파일의 용도는 무엇인가요?

AndroidManifest.xml 파일은 Android 운영 체제에 대한 응용 프로그램에 대한 필수 정보를 정의하는 Android 프로젝트의 중요한 구성 파일입니다. 애플리케이션과 OS 사이의 다리 역할을 하며 앱의 구성 요소, 권한, 하드웨어 및 소프트웨어 기능 등에 대해 알려줍니다.

### AndroidManifest.xml의 주요 기능

1. 응용 프로그램 구성 요소 선언: Android 시스템이 시작하거나 상호 작용하는 방법을 알 수 있도록 액티비티, 서비스, 방송 수신기 및 콘텐츠 공급자와 같은 필수 구성 요소를 등록합니다.
2. 권한: INTERNET, ACCESS_FINE_LOCATION 또는 READ_CONTACTS와 같이 앱이 필요로 하는 권한을 선언하므로 사용자는 앱이 어떤 리소스에 액세스할지 알 수 있고 이러한 권한을 부여하거나 거부할 수 있습니다.
3. 하드웨어 및 소프트웨어 요구 사항: 카메라, GPS 또는 특정 화면 크기와 같이 앱이 의존하는 기능을 지정하여 Play Store가 이러한 요구 사항을 충족하지 않는 기기를 필터링하는 데 도움이 됩니다.
4. 앱 메타데이터: 앱이 설치 및 실행에 사용하는 앱의 패키지 이름, 버전, 최소 및 대상 API 수준, 테마 및 스타일과 같은 필수 정보를 제공합니다.
5. 인텐트 필터: 구성 요소(예: 액티비티)에 대한 인텐트 필터를 정의하여 링크 열기 또는 콘텐츠 공유와 같이 응답할 수 있는 인텐트의 종류를 지정하여 다른 앱이 귀하의 앱과 상호 작용할 수 있도록 합니다.
6. 앱 구성 및 설정: 메인 런처 액티비티 설정, 백업 동작 구성 및 테마 지정과 같은 구성이 포함되어 있어 앱이 어떻게 동작하고 표시되는지 제어하는 데 도움이 됩니다.

아래는 AndroidManifest.xml 파일의 예입니다.

```kotlin
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">

       <!-- Main Activity -->
       <activity android:name=".MainActivity">
           <intent-filter>
               <action android:name="android.intent.action.MAIN" />
               <category android:name="android.intent.category.LAUNCHER" />
           </intent-filter>
       </activity>

       <!-- Additional Components -->
       <service android:name=".MyService" />
       <receiver android:name=".MyBroadcastReceiver" />

    </application>
</manifest>
```

### 요약

AndroidManifest.xml 파일은 모든 Android 앱의 기본이며 Android OS에 앱의 수명 주기, 권한 및 상호 작용을 관리하는 데 필요한 세부 정보를 제공합니다. 그것은 본질적으로 앱의 구조와 요구 사항을 정의하는 청사진 역할을 합니다.


## Q. AndroidManifest의 인텐트 필터는 어떻게 앱 상호 작용을 활성화하며, 액티비티 클래스가 AndroidManifest에 등록되지 않은 경우 어떻게 됩니까?
    
  **1. 인텐트 필터(Intent Filter)와 앱 상호 작용 활성화:**
  
  - **역할:** 인텐트 필터는 `AndroidManifest.xml` 파일 안에서 특정 앱 컴포넌트(주로 액티비티, 서비스, 브로드캐스트 리시버)가 어떤 종류의 **암시적 인텐트(Implicit Intent)** 를 받아 처리할 수 있는지를 시스템에 **선언**하는 역할을 합니다. 이는 마치 해당 컴포넌트가 "나는 이런 종류의 요청(Action)과 데이터(Data)를 처리할 수 있다"고 광고하는 것과 같습니다.
  - **동작 방식:**
      1. 다른 앱이나 시스템이 특정 `Action`(예: `ACTION_VIEW`, `ACTION_SEND`)과 처리할 `Data`(예: URL 스키마 `http://`, MIME 타입 `image/jpeg`)를 지정한 암시적 인텐트를 보냅니다.
      2. 안드로이드 시스템은 설치된 모든 앱의 Manifest 파일을 검사하여, 이 인텐트의 `Action`, `Data`, `Category` 조건과 일치하는 `<intent-filter>`를 가진 컴포넌트를 찾습니다.
      3. 일치하는 컴포넌트를 찾으면, 시스템은 해당 컴포넌트(예: 액티비티)를 시작시켜 인텐트를 전달합니다. 만약 여러 컴포넌트가 일치하면 사용자에게 어떤 앱으로 작업을 완료할지 선택하도록 요청합니다(앱 선택 다이얼로그).
  - **상호 작용 활성화:** 이 메커니즘을 통해 앱은 자신의 기능을 외부에 노출하고, 다른 앱들은 특정 앱의 클래스 이름을 몰라도 "웹 브라우저 기능", "이미지 공유 기능" 등 **작업(Task) 기반으로 상호작용**할 수 있게 됩니다.
  
  **2. AndroidManifest에 등록되지 않은 액티비티:**
  
  - **결론:** 만약 액티비티 클래스가 자바 또는 코틀린 코드로 존재하더라도, `AndroidManifest.xml` 파일 내에 `<application>` 태그 안에 `<activity>` 태그를 사용하여 명시적으로 **등록(선언)되지 않으면, 안드로이드 시스템은 해당 액티비티의 존재를 전혀 알 수 없습니다.**
  - **결과:**
      - 시스템이 인식하지 못하므로, 해당 액티비티는 **절대로 시작될 수 없습니다.** 명시적 인텐트(`new Intent(context, UnregisteredActivity.class)`)를 사용하든, 암시적 인텐트를 통해 호출되기를 기대하든 불가능합니다.
      - 만약 코드 내에서 `startActivity()`를 호출하여 등록되지 않은 액티비티를 시작하려고 시도하면, 시스템은 해당 컴포넌트를 찾을 수 없다는 의미의 **`ActivityNotFoundException` 예외를 발생**시키며, 이는 보통 앱의 비정상 종료로 이어집니다.
  
  **요약:** 인텐트 필터는 암시적 인텐트를 통해 앱 컴포넌트가 외부와 상호작용할 수 있도록 하는 '광고판' 역할을 하며, 액티비티는 반드시 `AndroidManifest.xml`에 등록되어야만 시스템이 인식하고 실행시킬 수 있습니다. 미등록 시 `ActivityNotFoundException`이 발생합니다.