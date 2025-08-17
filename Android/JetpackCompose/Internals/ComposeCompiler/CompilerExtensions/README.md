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
- Compose Compiler는 Kotlin 컴파일러의 `ComponentRegistrar`를 통해 `ComposeComponentRegistrar`와 여러 확장을 등록해 분석/IR 변환/코드 생성 단계에서 Compose 전용 처리를 수행합니다.
- 등록된 확장은 `@Composable` 호출 처리, remember/스코프 최적화, 호출 시그니처 보강, 런타임 연동 보일러플레이트 생성 등을 담당합니다.
- 활성화된 컴파일러 플래그에 따라 라이브 리터럴, 소스 정보 포함, 디코이(Decoy) 메서드 생성 등을 선택적으로 활성화할 수 있습니다.
- 구현 세부는 AOSP 소스에서 확인할 수 있습니다.