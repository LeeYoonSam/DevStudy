# 컴파일러 확장 등록 (Registering Compiler Extensions)

Jetpack Compose의 **Compose Compiler 플러그인**은 Kotlin 컴파일러의 **ComponentRegistrar** 메커니즘을 통해 자신을 등록하고, 여러 종류의 **컴파일러 확장(extensions)** 을 연결하여 Compose에 필요한 코드 생성과 최적화를 수행합니다.

## 개요
- **목적**: 런타임에서 필요한 코드(예: slot table 조작, 재구성 최적화)에 맞춰 IR 변환과 코드 생성을 수행.
- **핵심 포인트**:
  - Kotlin 컴파일러의 `ComponentRegistrar`에 **`ComposeComponentRegistrar`** 가 등록됨.
  - 다양한 **컴파일러 확장**이 함께 등록되어, 분석/IR 변환/코드 생성 단계에서 Compose 전용 처리를 수행.
  - 특정 **컴파일러 플래그**에 따라 확장 동작이 달라짐(예: 라이브 리터럴, 소스 정보 포함 등).

## 등록 흐름
```mermaid
  flowchart LR
    A["Kotlin Source"] --> B["Kotlin Compiler"]
    B --> C["ComponentRegistrar"]
    C --> D["ComposeComponentRegistrar"]
    D --> E["Extensions 등록"]
    E --> F["IR Transform / Codegen"]
    F --> G["Compose Runtime 및 개발 도구 연계"]
```

## 주요 확장과 역할(개념)
| **영역** | **역할** |
| --- | --- |
| 등록(Registrar) | Kotlin 컴파일러 파이프라인에 Compose 전용 확장을 연결 |
| 분석/확대(Analysis) | 어노테이션 처리, 안정성/불변성 신호 해석, 메타데이터 수집 |
| IR 변환 | `@Composable` 호출 처리, remember/스코프 최적화, 호출 시그니처 보강(필요 시 디코이 메서드 생성) |
| 코드 생성 | 런타임 연동을 위한 보일러플레이트 생성, 소스 위치 정보 주입(옵션) |

## 컴파일러 플래그/기능 예
| **기능/플래그** | **효과** |
| --- | --- |
| 라이브 리터럴(Live Literals) | 런타임에서 리터럴 값을 동적으로 바꿔 UI 반영 |
| 소스 정보 포함(Source info) | 생성 코드에 소스 매핑 정보를 포함하여 **Layout Inspector** 등 도구의 분석 지원 |
| Kotlin 버전 호환성 검사 무시 | 특정 환경에서 엄격한 버전 체크를 완화 |
| 디코이(Decoy) 메서드 생성 | IR 변환 과정에서 안정적 호출·시그니처 유지를 위한 보조 메서드 생성 |

> 참고: 실제 플래그 세트와 내부 구현은 Kotlin/Compose 버전에 따라 달라질 수 있습니다.

## 간단 예시(개념 코드)
아래는 `ComponentRegistrar`를 통해 IR 확장을 등록하는 **개념적** 예시입니다. 실제 Compose 소스는 AOSP에서 확인하세요.

```kotlin
  class ComposeComponentRegistrar : ComponentRegistrar {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
      // 예: IR 변환 확장 등록(개념)
      IrGenerationExtension.registerExtension(ComposeIrGenerationExtension())

      // 예: 플래그에 따라 라이브 리터럴/소스 정보 확장 등록(개념)
      if (configuration.get(COMPILER_FLAG_LIVE_LITERALS) == true) {
        IrGenerationExtension.registerExtension(LiveLiteralsExtension())
      }
    }
  }
```

## 더 알아보기
- 구현과 세부 동작은 **AOSP** 코드에서 확인할 수 있습니다: [AOSP 코드 검색](https://cs.android.com)

---

## 요약
- Compose Compiler는 **`ComponentRegistrar`** 를 통해 **여러 확장**을 등록하여 분석/IR/코드 생성 전 단계에서 Compose 전용 처리를 수행합니다.
- **컴파일러 플래그**에 따라 **라이브 리터럴**, **소스 정보 포함**, **디코이 메서드 생성** 등의 기능이 켜집니다.
- 자세한 구현은 **AOSP 소스**를 참고하세요.
컴파일러 확장 등록 (Registering Compiler extensions)

지금까지 Compose Runtime에서 제공하는 런타임의 동작과 가장 관련성이 높고 실질적으로 활용이 가능한 어노테이션에 대해 살펴보았으며, 다음으로는 Compose Compiler 플러그인의 작동 방식과 해당 어노테이션을 사용하는 방법에 이해할 차례입니다.


Compose Compiler 플러그인이 수행하는 첫 번째 작업은 Kotlin 컴파일러가 제공하는 메커니즘인 ComponentRegistrar를 사용하여 Kotlin 컴파일러 파이프라인에 자신을 등록하는 것입니다. ComposeComponentRegistrar는 다양한 목적을 위해 일련의 컴파일러 익스텐션16을 등록합니다. 이러한 익스텐션은 라이브러리 사용을 용이하게 하고 런타임에 필요한 코드를 생성하는 역할을 합니다. 등록된 모든 익스텐션 프로그램은 Kotlin 컴파일러와 함께 실행됩니다.


Compose Compiler는 활성화된 컴파일러 플래그17에 따라 몇 가지 익스텐션도 등록합니다. Jetpack Compose를 사용하는 개발자들은 라이브 리터럴18 같은 기능을 활성화하고, 생성된 코드에 소스 정보를 포함시킵니다. 따라서 Android Studio 및 기타 툴들이 composition을 검사하거나, remember 함수에 대한 최적화, Kotlin 버전 호환성 검사 무시 및 IR 변환 과정에서 미끼용(decoy) 메서드 생성 등을 수행하도록 하는 특정 컴파일러 플래그를 활성화할 수 있습니다.


컴파일러 익스텐션이 어떻게 컴파일러 플러그인에 의해 등록되는지 궁금하시다면, 언제든지 cs.android.com에서 소스를 탐색할 수 있다는 사실을 기억하시길 바랍니다.