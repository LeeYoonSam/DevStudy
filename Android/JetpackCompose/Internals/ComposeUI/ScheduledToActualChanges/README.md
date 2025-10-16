# 예약된 변경 목록을 실제 트리의 변경 목록으로 대응 (Mapping scheduled changes to actual changes to the tree)

## 개요

**Composition**과 **Recomposition** 과정 중 `Composable` 함수가 실행될 때, Composable 함수는 자신의 변경 사항을 방출합니다. 이와 함께, `Composition`이라는 **side table**이 사용됩니다.

> **참고**: `composition` 과정과 구분하기 위해 `Composition`은 대문자로 표기합니다.

## Composition의 역할

`Composition` 테이블은 다음과 같은 정보를 관리합니다:

- **Composable 함수의 실행** (예정된 변경 목록)
- **실제 노드 트리의 변경**으로의 대응 (매핑)

### 동작 흐름

```mermaid
flowchart LR
  A[Composable 함수 실행] --> B[변경 사항 방출]
  B --> C[Composition Table]
  C --> D[실제 노드 트리 변경]
  
  style A fill:#e3f2fd
  style B fill:#fff3e0
  style C fill:#f3e5f5
  style D fill:#e8f5e9
```

## 다중 Composition 개념

Compose UI를 사용하는 애플리케이션에서는 **표현해야 할 노드 트리의 수만큼 많은 Composition을 가질 수 있습니다**.

### 주요 특징

| 특징 | 설명 |
|-----|------|
| **다중 Composition** | 하나의 앱에서 여러 Composition 인스턴스 존재 가능 |
| **노드 트리 연관** | 각 Composition은 하나의 노드 트리에 대응 |
| **독립적 관리** | 각 Composition은 독립적으로 변경 사항을 추적 |

> **💡 Tip**: 이 책에서는 Composition이 여러 개일 수 있다는 사실을 초반에 언급하지 않았기 때문에 다소 놀라실 수 있습니다. 하지만 실제로는 여러 개입니다!

### 학습 포인트

이에 대해 더 알아보면서 다음을 이해하게 됩니다:

- **레이아웃 트리**가 어떻게 만들어지는지
- 어떤 **노드 타입**이 사용되는지
- 각 Composition이 어떻게 **독립적으로 동작**하는지

## 요약

- `Composition`은 Composable 함수의 실행(예정된 변경 목록)을 실제 노드 트리의 변경으로 대응시키는 **side table** 역할을 수행합니다
- Compose UI 애플리케이션은 표현해야 할 노드 트리의 수만큼 **여러 개의 Composition 인스턴스**를 가질 수 있습니다
- 각 Composition은 독립적으로 관리되며, 이를 통해 레이아웃 트리 구성과 노드 타입 활용을 이해할 수 있습니다