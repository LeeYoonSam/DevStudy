# 드로어블(Drawable)이란 무엇이며, UI 개발에서 어떻게 활용되나요?

**드로어블(Drawable)** 은 화면에 그려질 수 있는 모든 것에 대한 **일반적인 추상화**입니다. 이는 이미지, 벡터 그래픽, 모양 기반 요소 등 다양한 유형의 그래픽 콘텐츠를 위한 기본 클래스 역할을 합니다. 드로어블은 UI 컴포넌트의 배경, 버튼, 아이콘, 사용자 정의 뷰 등 UI 개발 전반에 걸쳐 널리 사용됩니다.

안드로이드는 특정 사용 사례에 맞게 설계된 다양한 유형의 드로어블 객체를 제공합니다.

---

### 주요 드로어블(Drawable) 유형 및 활용법

#### 1. BitmapDrawable (래스터 이미지)

`BitmapDrawable`은 PNG, JPG 또는 GIF와 같은 **래스터 이미지를 표시**하는 데 사용됩니다. 비트맵 이미지의 크기 조절(scaling), 바둑판식 배열(tiling), 필터링 기능을 제공합니다.

**XML 예시:**
```xml
<bitmap xmlns:android="http://schemas.android.com/apk/res/android"
    android:src="@drawable/sample_image"
    android:tileMode="repeat"/>
```
이는 일반적으로 `ImageView` 컴포넌트에서 이미지를 표시하거나 배경으로 사용될 때 흔히 사용됩니다.

#### 2. VectorDrawable (확장 가능한 벡터 그래픽)

`VectorDrawable`은 XML 경로 데이터를 사용하여 **확장 가능한 벡터 그래픽(SVG와 유사)** 을 나타냅니다. 비트맵과 달리 벡터는 어떤 해상도에서도 품질을 유지합니다.

**XML 예시:**
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FF0000"
        android:pathData="M12,2L15,8H9L12,2Z"/> </vector>
```
`VectorDrawable`은 다양한 화면 밀도에서 픽셀 깨짐 문제를 피하기 위해 아이콘, 로고 및 크기 조절이 가능한 UI 요소에 이상적입니다.

#### 3. NinePatchDrawable (패딩을 포함한 크기 조절 가능 이미지)

`NinePatchDrawable`은 모서리나 패딩과 같은 **특정 영역을 보존하면서 크기를 조절**할 수 있는 특별한 유형의 비트맵입니다. 채팅 말풍선이나 버튼과 같이 늘어나는 UI 컴포넌트를 만드는 데 유용합니다.

Nine-Patch 이미지(`.9.png`)는 늘어나는 영역과 고정된 영역을 정의하는 추가적인 1픽셀 테두리를 포함합니다.

**XML 예시:**
```xml
<nine-patch xmlns:android="http://schemas.android.com/apk/res/android"
    android:src="@drawable/chat_bubble_nine_patch"/>
```
*(역자 주: 실제 파일은 `chat_bubble_nine_patch.9.png`와 같이 `.9.png` 확장자를 가집니다. XML에서는 `.9`를 생략합니다.)*

`.9.png` 파일을 만들려면 Android Studio의 NinePatch 도구를 사용하여 늘어나는 영역을 정의합니다.

#### 4. ShapeDrawable (사용자 정의 모양)

`ShapeDrawable`은 XML로 정의되며 이미지를 사용하지 않고도 둥근 모서리 사각형, 타원 또는 기타 단순한 모양을 만드는 데 사용할 수 있습니다.

**XML 예시:**
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FF5733"/>
    <corners android:radius="8dp"/>
</shape>
```
`ShapeDrawable`은 버튼, 배경 및 사용자 정의 UI 컴포넌트에 유용합니다.

#### 5. LayerDrawable (여러 드로어블 중첩)

`LayerDrawable`은 여러 드로어블을 단일 계층 구조로 결합하는 데 사용되며, 복잡한 UI 배경을 만드는 데 유용합니다.

**XML 예시:**
```xml
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="#000000"/>
        </shape>
    </item>
    <item android:drawable="@drawable/icon" android:top="10dp"/>
</layer-list>
```
이는 오버레이 효과나 중첩된 시각적 요소를 만드는 데 유용합니다.

---

### 요약

`Drawable` 클래스는 안드로이드에서 다양한 유형의 그래픽을 처리하는 유연한 방법을 제공합니다. 올바른 드로어블을 선택하는 것은 디자인 요구 사항, 확장성 및 UI 복잡성과 같은 사용 사례에 따라 달라집니다. 이러한 다양한 드로어블 유형을 활용하여 개발자는 안드로이드 애플리케이션에서 최적화되고 시각적으로 매력적인 UI 컴포넌트를 만들 수 있습니다.

---

## Q. 사용자의 상호작용에 따라 모양과 색상이 변경되는 동적 배경을 가진 버튼을 드로어블만을 사용하여 어떻게 만들 수 있나요?

사용자의 상호작용(예: 누름, 포커스)에 따라 모양과 색상이 변경되는 동적 배경을 가진 버튼은 안드로이드의 **`Drawable` 리소스**만을 사용하여 효과적으로 구현할 수 있습니다. 핵심은 다양한 상태에 따라 다른 드로어블을 보여주는 **`StateListDrawable`** (XML에서는 `<selector>` 태그 사용)을 활용하는 것입니다.

---
### 1. 핵심 원리: `StateListDrawable` 활용

`StateListDrawable`은 뷰(View)의 현재 상태(예: 눌렸을 때, 포커스를 받았을 때, 비활성화되었을 때 등)에 따라 각기 다른 드로어블 리소스를 지정할 수 있게 해주는 특수한 드로어블입니다. 이를 통해 코드 변경 없이 XML 정의만으로 동적인 시각적 피드백을 제공할 수 있습니다.

---
### 2. 각 상태별 드로어블(ShapeDrawable) 정의

먼저 버튼의 각 상태(기본 상태, 눌린 상태 등)에 해당하는 모양과 색상을 가진 `ShapeDrawable`들을 XML로 정의합니다. `ShapeDrawable`은 코너의 둥근 정도, 채우기 색, 테두리 등을 XML로 쉽게 정의할 수 있게 해줍니다.

#### 2.1. 기본 상태 (Normal State) 드로어블
예: `res/drawable/button_normal_shape.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFDDDDDD" />
    <corners android:radius="8dp" />
    <stroke android:width="1dp" android:color="#FFBBBBBB" />
</shape>
```

#### 2.2. 눌린 상태 (Pressed State) 드로어블
예: `res/drawable/button_pressed_shape.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFBBBBBB" />
    <corners android:radius="10dp" /> <stroke android:width="1dp" android:color="#FF999999" />
</shape>
```

#### 2.3. (선택 사항) 포커스 상태 (Focused State) 드로어블
예: `res/drawable/button_focused_shape.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFEEEEEE" />
    <corners android:radius="8dp" />
    <stroke android:width="2dp" android:color="@android:color/holo_blue_light" /> </shape>
```

*(필요에 따라 비활성화 상태(`android:state_enabled="false"`) 등 다른 상태에 대한 드로어블도 유사하게 정의할 수 있습니다.)*

---
### 3. `StateListDrawable` XML 파일 생성

이제 위에서 정의한 `ShapeDrawable`들을 사용하여 `StateListDrawable`을 만듭니다. 이 파일은 `res/drawable/` 폴더에 XML 파일로 생성됩니다 (예: `button_dynamic_background.xml`).

`res/drawable/button_dynamic_background.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true"
          android:drawable="@drawable/button_pressed_shape" />

    <item android:state_focused="true"
          android:drawable="@drawable/button_focused_shape" />

    <item android:drawable="@drawable/button_normal_shape" />
</selector>
```
**주의:** `<selector>` 내의 `<item>` 태그 순서가 중요합니다. 시스템은 위에서부터 순서대로 현재 뷰 상태와 일치하는 첫 번째 `<item>`을 사용합니다. 따라서 구체적인 상태(예: `state_pressed`)를 먼저 정의하고, 가장 일반적인 기본 상태는 마지막에 두어야 합니다.

---
### 4. 버튼에 동적 배경 적용

마지막으로, 레이아웃 XML 파일에서 `Button`의 `android:background` 속성에 위에서 생성한 `StateListDrawable`을 지정합니다.

`res/layout/activity_main.xml` (예시):
```xml
<Button
    android:id="@+id/my_dynamic_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="동적 버튼"
    android:background="@drawable/button_dynamic_background" />
```

---
### 5. 결과 및 추가 팁

이제 `my_dynamic_button`은 사용자가 터치하여 누르면 `button_pressed_shape.xml`에 정의된 모양과 색상으로 변경되고, 포커스를 받으면 `button_focused_shape.xml`의 스타일로 변경되며, 평소에는 `button_normal_shape.xml`의 스타일을 가집니다.

**추가 팁:**
* **색상 변경만 필요한 경우:** 각 상태에 대해 별도의 `ShapeDrawable` XML 파일을 만드는 대신, `StateListDrawable` 내의 `<item>` 태그 안에 직접 `<shape>`를 정의하고 `<solid android:color="..."/>`만 변경하여 코드를 간결하게 유지할 수도 있습니다.
* **더 복잡한 모양 변경:** `ShapeDrawable`은 주로 사각형, 타원, 선, 링 형태와 그 변형(둥근 모서리, 그라데이션 등)을 지원합니다. 만약 상태에 따라 매우 복잡하거나 비정형적인 모양 변경이 필요하다면, `LayerDrawable`을 조합하거나, 프로그래밍 방식으로 `Drawable`을 변경하거나, 사용자 정의 뷰를 만드는 것을 고려해야 할 수 있습니다. 하지만 대부분의 일반적인 버튼 상태 변경은 `StateListDrawable`과 `ShapeDrawable`의 조합으로 충분히 구현 가능합니다.

이 방법을 사용하면 코드 변경 없이 XML 리소스만으로도 사용자 상호작용에 따라 시각적으로 반응하는 동적인 UI 요소를 만들 수 있어 매우 효율적입니다.