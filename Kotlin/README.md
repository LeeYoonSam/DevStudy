# Useful Kotlin Extension
- [유용하게 사용하는 Extension](#유용하게-사용하는-extension)

## 유용하게 사용하는 Extension
### 각 타입별 null or 초기화 값 할당
- Int.orZero(), Long.orZero()

```kotlin
fun Int?.orZero() = this ?: 0
```

### 색상 및 drawable Resource 가져오기
```kotlin
fun Context.getCompatColor(@ColorRes colorId: Int) = ResourcesCompat.getColor(resources, colorId, null)
fun Context.getCompatDrawable(@DrawableRes drawableId: Int) = AppCompatResources.getDrawable(this, drawableId)!!

// Usage
// Activity
getCompatColor(R.color.white)

// Fragment
requireContext().getCompatColor(R.color.white)
```

### Permission 확인
```kotlin
fun Context.hasPermissions(vararg permissions: String) = permissions.all { permission ->
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

// Usage
// Traditional way (Activity)
(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
   != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
   != PackageManager.PERMISSION_GRANTED)

// Extension (Activity)
hasPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
```

### Clipboard 에 복사
```kotlin
fun Context.copyToClipboard(content: String) {
    val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
    val clip = ClipData.newPlainText("clipboard", content)
    clipboardManager.setPrimaryClip(clip)
}
```

### Uri 에 해당하는 인텐트를 처리할 앱이 없을때 처리
```kotlin
fun Context.isResolvable(intent: Intent) = intent.resolveActivity(packageManager) != null

fun Context.view(uri: Uri, onAppMissing: () -> Unit) {
    val intent = Intent(Intent.ACTION_VIEW, uri)

    if (!isResolvable(intent)) {
        onAppMissing()
        return
    }

    startActivity(intent)
}
```

### 참고
[Useful Kotlin Extensions for Android](https://medium.com/nerd-for-tech/useful-kotlin-extensions-for-android-d8652f64bca8)
