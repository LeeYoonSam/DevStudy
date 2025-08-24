# 라이브 리터럴 활성화 (Enabling Live Literals)

> 면책 조항: 이 섹션은 구현 세부 사항에 매우 가깝고 지속적으로 발전 중입니다. 미래에도 변경될 수 있으며, 더 효율적인 구현이 발견되면 다양한 방식으로 개선될 수 있습니다.

## 개요

- **핵심**: `라이브 리터럴(Live Literals)`은 미리보기에서 코드 변경을 **리컴파일 없이 실시간 반영**하도록 돕는 기능입니다.
- **활성화 플래그**: 두 가지가 존재합니다.
  - `liveLiterals` (v1) — 최신 Android Studio에서는 동작하지 않을 수 있습니다.
  - `liveLiteralsEnabled` (v2) — 현재 사용 가능한 버전입니다.

> 릴리스 빌드에서는 절대 활성화하지 마세요. 성능 저하와 최적화 방해를 유발할 수 있습니다.

## 동작 원리

`Compose Compiler`는 상수 표현식을 `MutableState` 기반으로 **치환**하여, 런타임이 리컴파일 없이 값 변화를 **관찰**할 수 있게 합니다. 변환 시 다음과 같은 규칙이 적용됩니다.

- 파일 단위로 `LiveLiterals$(클래스명)` **싱글톤 클래스**를 생성합니다.
- 각 상수 표현식에 대해 **고유 ID**를 부여합니다.
- 상수 접근은 `MutableState`의 **getter**로 대체됩니다.
- 런타임은 해당 키를 통해 변경된 상수값을 획득합니다.

### kdocs 요점

> “이 변환은 개발자 경험을 개선하기 위한 것이며, 성능에 민감한 코드의 속도를 크게 저하시킬 수 있으므로 **릴리스 빌드에서는 활성화하지 마세요**.”

## 예시

아래 `@Composable` 함수가 있을 때:

```kotlin
@Composable
fun Foo() {
  print("Hello World")
}
```

컴파일러가 다음과 같이 변환합니다:

```kotlin
// file: Foo.kt
@Composable
fun Foo() {
  print(LiveLiterals$FooKt.`getString$arg-0$call-print$fun-Foo`())
}

object LiveLiterals$FooKt {
  var `String$arg-0$call-print$fun-Foo`: String = "Hello World"
  var `State$String$arg-0$call-print$fun-Foo`: MutableState<String>? = null

  fun `getString$arg-0$call-print$fun-Foo`(): String {
    val field = this.`String$arg-0$call-print$fun-Foo`

    val state = if (field == null) {
      val tmp = liveLiteral(
        "String$arg-0$call-print$fun-Foo",
        this.`String$arg-0$call-print$fun-Foo`
      )
      this.`String$arg-0$call-print$fun-Foo` = tmp
      tmp
    } else field

    return field.value
  }
}
```

위 변환에서 주목할 점:
- `LiveLiterals$FooKt` 싱글톤이 생성됩니다.
- 상수 문자열은 `MutableState`로 래핑되어 getter를 통해 접근됩니다.
- 런타임은 `liveLiteral(key, default)`를 통해 값을 주입/관찰합니다.

## 사용 시 유의사항

- **개발 단계 전용**: 미리보기/빠른 피드백을 위한 기능입니다.
- **릴리스 비활성화**: 최적화에 방해가 되며 성능 저하를 유발합니다.
- **상수만 대상**: 상수 표현식의 치환/관찰에 집중합니다.

## 요약
- 라이브 리터럴은 미리보기 변경을 **리컴파일 없이 실시간 반영**.
- `LiveLiterals$(클래스)` 싱글톤과 `MutableState`로 상수를 **관찰 가능**하게 변환.
- **릴리스 빌드 금지**: 성능 저하 가능성 때문에 개발 환경에서만 사용.