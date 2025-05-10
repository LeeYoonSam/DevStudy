# 런타임 권한은 어떻게 처리하나요?

안드로이드에서 **런타임 권한(Runtime Permissions)** 을 처리하는 것은 원활한 사용자 경험을 보장하면서 사용자의 민감한 데이터에 접근하는 데 필수적입니다. 안드로이드 6.0 (API 레벨 23)부터 앱은 설치 시 자동으로 권한을 부여받는 대신, **위험 권한(dangerous permissions)** 을 런타임에 명시적으로 요청해야 합니다. 이 접근 방식은 사용자가 필요할 때만 권한을 부여할 수 있도록 하여 사용자 개인 정보 보호를 강화합니다.

### 권한 선언 및 확인

권한을 요청하기 전에 앱은 `AndroidManifest.xml` 파일에 해당 권한을 선언해야 합니다. 런타임에는 사용자가 해당 권한을 필요로 하는 기능과 상호작용할 때만 권한을 요청해야 합니다. 사용자에게 권한 요청 대화상자를 표시하기 전에, `ContextCompat.checkSelfPermission()`을 사용하여 권한이 이미 부여되었는지 확인하는 것이 중요합니다. 권한이 부여되었다면 해당 기능을 계속 진행할 수 있고, 그렇지 않다면 앱은 권한을 요청해야 합니다.

```kotlin
when {
    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED -> {
        // Permission granted, proceed with the feature
    }
    ActivityCompat.shouldShowRequestPermissionRationale(
        this, Manifest.permission.CAMERA
    ) -> {
        // Show rationale before requesting permission
        showPermissionRationale()
    }
    else -> {
        // Directly request the permission
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}
```

### 권한 요청하기

권한을 요청하는 권장 방법은 권한 처리를 단순화하는 `ActivityResultLauncher` API를 사용하는 것입니다. 그러면 시스템은 사용자에게 요청을 허용하거나 거부할지를 묻는 대화상자를 표시합니다.

```kotlin
val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with functionality
        } else {
            // Permission denied, handle gracefully
        }
    }
```
시스템이 요청을 관리하여 사용자에게 대화상자를 제시하며, 사용자는 권한을 허용하거나 거부할 수 있습니다.

### 권한 요청 근거(Rationale) 제공

경우에 따라, 시스템은 `shouldShowRequestPermissionRationale()`을 사용하여 권한을 요청하기 전에 사용자에게 권한이 필요한 이유를 설명하는 UI(근거, Rationale)를 표시할 것을 권장합니다. 이 메서드가 `true`를 반환하면, 왜 해당 권한이 필요한지 설명하는 UI를 표시해야 합니다. 이는 사용자 경험을 개선하고 권한을 얻을 가능성을 높입니다.

```kotlin
fun showPermissionRationale() {
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
       .setMessage("This feature needs access to your camera to function properly.")
        .setPositiveButton("OK") { _, _ ->
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        .setNegativeButton("Cancel", null)
        .show()
}
```

### 권한 거부 처리

사용자가 권한을 여러 번 거부하면 안드로이드는 이를 **영구적 거부**로 간주하며, 이는 앱이 더 이상 해당 권한을 다시 요청할 수 없음을 의미합니다. 앱은 사용자에게 기능이 제한됨을 알리고, 필요한 경우 시스템 설정으로 안내해야 합니다.

```kotlin
if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA
)) {
    // User permanently denied the permission
    showSettingsDialog()
}

fun showSettingsDialog() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:$packageName")
    }
    startActivity(intent)
}
```

### 위치 권한 처리

위치 권한은 포그라운드 접근과 백그라운드 접근으로 분류됩니다. 포그라운드 위치 접근에는 `ACCESS_FINE_LOCATION` 또는 `ACCESS_COARSE_LOCATION` 권한이 필요하며, 백그라운드 위치 접근에는 `ACCESS_BACKGROUND_LOCATION` 권한이 필요하며 이는 추가적인 정당화(사유 설명)가 필요합니다.

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

안드로이드 10 (API 레벨 29)부터 백그라운드 위치를 요청하는 앱은 먼저 포그라운드 접근 권한을 요청한 후, 별도로 백그라운드 권한을 요청해야 합니다.

### 일회성 권한 (One-Time Permissions)

안드로이드 11 (API 레벨 30)에서는 위치, 카메라, 마이크에 대한 [**일회성 권한**](https://developer.android.com/training/permissions/requesting#one-time)이 도입되었습니다. 사용자는 앱이 닫히면 취소되는 임시 접근 권한을 부여할 수 있습니다.

### 요약

런타임 권한을 올바르게 처리하면 보안, 규정 준수 및 사용자 신뢰를 보장할 수 있습니다. 권한 상태 확인, 근거 제공, 맥락에 맞는 권한 요청, 거부 상황의 적절한 처리와 같은 모범 사례를 따르면 개발자는 원활하고 개인 정보를 중시하는 사용자 경험을 만들 수 있습니다.

---

## Q. 안드로이드의 런타임 권한 시스템이 사용자 개인 정보 보호를 어떻게 개선하며, 앱이 민감한 권한을 요청하기 전에 어떤 시나리오(조치)를 취해야 하나요?

### 안드로이드 런타임 권한 시스템의 개인 정보 보호 개선 효과 및 민감한 권한 요청 전 앱의 조치

**1. 안드로이드 런타임 권한 시스템이 사용자 개인 정보 보호를 개선하는 방식**

안드로이드 6.0 (API 레벨 23)부터 도입된 런타임 권한 시스템은 다음과 같은 방식으로 사용자 개인 정보 보호를 크게 향상시킵니다.

* **사용자 제어권 및 투명성 강화:**
    * **세분화된 선택:** 이전에는 앱 설치 시 모든 권한을 일괄적으로 동의해야 했지만, 런타임 권한 시스템에서는 앱이 실제로 해당 기능을 사용하려고 할 때 각 권한을 개별적으로 요청합니다. 이를 통해 사용자는 앱이 어떤 데이터나 기능에 접근하려는지 명확히 인지하고 직접 제어할 수 있습니다.
    * **맥락적 요청:** 권한이 필요한 특정 기능을 사용자가 사용하려는 시점에 권한 요청이 이루어지므로, 사용자는 왜 해당 권한이 필요한지 더 쉽게 이해하고 정보에 기반한 결정을 내릴 수 있습니다.
* **과도한 권한 부여 감소:**
    * 앱은 실제로 필요한 기능에 대해서만, 그리고 필요할 때만 권한을 요청하게 됩니다. 이로써 사용하지 않는 기능에 대한 불필요한 권한 부여를 막고, 앱이 과도한 개인 정보에 접근하는 것을 방지합니다.
    * 사용자가 특정 기능을 사용하지 않는다면 해당 권한을 부여할 필요가 없어집니다.
* **언제든지 권한 철회 가능:**
    * 사용자는 앱 사용 중이더라도 언제든지 시스템 설정 메뉴를 통해 특정 앱에 부여된 권한을 개별적으로 확인하고 철회할 수 있습니다. 이는 지속적인 사용자 제어권을 보장합니다.
* **사용자 인식 제고:**
    * 런타임에 권한을 요청하는 과정 자체가 사용자에게 앱이 어떤 정보에 접근하려 하는지 더 명확하게 인지시켜, 앱의 동작 방식에 대한 이해도를 높이고 개인 정보 보호에 대한 경각심을 갖게 합니다.
* **일회성 권한 (Android 11 이상):**
    * 위치, 카메라, 마이크와 같이 매우 민감한 권한에 대해 사용자는 "이번만 허용" 옵션을 선택하여 현재 세션에 대해서만 임시로 권한을 부여할 수 있습니다. 앱이 닫히거나 일정 시간 백그라운드에 있으면 이 권한은 자동으로 철회되어, 더 세밀한 개인 정보 제어가 가능해집니다.

**2. 앱이 민감한 권한을 요청하기 전에 취해야 할 조치 (시나리오/단계)**

앱이 사용자에게 민감한 권한(예: 위치, 카메라, 연락처 접근 등)을 요청하기 전에는 다음과 같은 단계별 조치를 취하는 것이 바람직합니다.

1.  **매니페스트(Manifest)에 권한 선언 (필수 준비 단계):**
    * 가장 먼저, 앱이 사용하려는 모든 권한은 `AndroidManifest.xml` 파일에 `<uses-permission>` 태그를 사용하여 명시적으로 선언해야 합니다. 이것은 런타임 권한 요청의 기본 전제 조건입니다.

2.  **권한 필요성 식별 - 필요할 때만 요청:**
    * 앱이 시작되자마자 모든 권한을 요청하는 것은 지양해야 합니다 (핵심 기능에 절대적으로 필요한 경우가 아니라면).
    * 사용자가 해당 권한을 필요로 하는 특정 기능을 사용하려고 시도하는 **바로 그 시점에** 권한을 요청해야 합니다. 예를 들어, 사진 촬영 버튼을 눌렀을 때 카메라 권한을 요청하는 것이 사용자에게 명확한 맥락을 제공합니다.

3.  **이미 권한이 부여되었는지 확인:**
    * 권한을 요청하기 전에 항상 `ContextCompat.checkSelfPermission()` 메서드를 사용하여 해당 권한이 이미 사용자로부터 부여받았는지 확인합니다.
    * 이미 권한이 있다면, 추가 요청 없이 해당 기능을 바로 실행합니다.

4.  **권한 요청 근거(Rationale) 제공 (필요한 경우):**
    * **표시 시점:** `shouldShowRequestPermissionRationale()` 메서드가 `true`를 반환할 때입니다. 이는 사용자가 이전에 해당 권한 요청을 한 번 이상 거부했지만, "다시 묻지 않음"을 선택하지는 않은 경우를 의미합니다. 이 상황에서 사용자는 왜 이 권한이 필요한지 충분히 이해하지 못했을 수 있습니다.
    * **목적:** 사용자 친화적인 UI(예: 다이얼로그, 스낵바)를 통해 사용자가 접근하려는 기능에 왜 이 특정 권한이 필요한지 명확하고 간결하게 설명합니다. 권한을 부여했을 때의 이점이나 거부했을 때의 기능 제한을 안내합니다.
    * **주의:** 처음 권한을 요청하거나, 사용자가 "다시 묻지 않음"을 선택한 경우에는 이 설명을 표시하지 않아야 합니다.

5.  **권한 요청 실행:**
    * 근거 설명 UI를 보여준 후(해당하는 경우), 또는 처음 요청하거나 근거 설명이 필요 없는 경우, `ActivityResultLauncher`와 `RequestPermission` (단일 권한) 또는 `RequestMultiplePermissions` (다중 권한) 계약(contract)을 사용하여 실제 권한 요청 대화상자를 시스템에 요청합니다.

6.  **요청 결과 처리 (허용/거부/영구 거부):**
    * `ActivityResultLauncher`의 콜백을 통해 사용자의 응답(허용 또는 거부)을 처리합니다.
        * **허용된 경우:** 해당 기능을 계속 진행합니다.
        * **거부된 경우 (일반 거부):** 기능이 제한됨을 사용자에게 알리고, 해당 기능 없이 앱을 사용할 수 있는 다른 방법을 안내하거나, 기능을 사용하지 못함을 명확히 합니다. 즉시 반복적으로 권한을 요청하는 것은 피해야 합니다.
        * **영구적으로 거부된 경우 (사용자가 "다시 묻지 않음" 선택):** 시스템 권한 요청 대화상자가 더 이상 나타나지 않습니다. 앱은 이 결정을 존중해야 합니다. 사용자에게 해당 기능 사용에 권한이 필수적이며, 원한다면 앱의 시스템 설정에서 수동으로 권한을 부여할 수 있음을 안내합니다. 시스템 설정 화면으로 쉽게 이동할 수 있는 방법을 제공하는 것이 좋습니다 (예: 설정 바로가기 버튼).

이러한 단계를 따르면 사용자 개인 정보를 존중하면서 앱 기능을 원활하게 제공하고, 사용자로부터 권한을 얻을 가능성을 높일 수 있습니다.