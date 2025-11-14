# EARS(Easy Approach to Requirements Syntax) 표기법

> 명세의 명확성에서 검증 가능성까지

## 1. 서론: '보이지 않는 엄격함(Stealth Rigour)'을 통한 요구사항 명확성 확보

### 1.1. EARS의 정의: 자연어에 대한 '부드러운 제약'

**EARS(Easy Approach to Requirements Syntax)** 는 '요구사항을 작성하는 쉬운 접근법'을 의미하는 약어입니다. 이는 본질적으로 비정형적이고 제약이 없는 `자연어(Natural Language, NL)`로 작성된 시스템 요구사항 명세서에 **부드러운 제약(gently constrain)** 을 가하는 간결하고 강력한 메커니즘입니다.

EARS는 완전히 새로운 프로그래밍 언어나 복잡한 형식적 언어를 발명하는 것이 아닙니다. 대신, **명확성(clarity)** 과 **정밀성(precision)** 을 최적화하기 위해 기존 자연어(주로 영어)의 공통적인 사용법을 반영하는 간단한 **키워드 집합(ruleset)** 과 구문 구조를 제안합니다.

### 1.2. 문제의 근원: 비정형 자연어(NL) 명세서의 고질적 한계

전통적으로 시스템 요구사항은 제약 없는 자연어(unconstrained natural language)로 작성되며, 이는 본질적으로 **부정확(imprecise)** 합니다. 이러한 부정확성은 시스템 개발 수명 주기 전반에 걸쳐 심각한 문제를 야기합니다.

**비정형 자연어 명세서에서 발견되는 8가지 주요 문제:**

- **모호성(Ambiguity)**: 단어나 구문이 둘 이상의 의미로 해석될 여지가 있는 경우
- **애매성(Vagueness)**: 정밀성, 구조 또는 세부 사항이 부족한 경우
- **복잡성(Complexity)**: 여러 하위 절이나 여러 상호 연관된 문장을 포함하는 복합 요구사항
- **누락(Omission)**: 필수적인 요구사항, 특히 `원치 않는 동작(unwanted behavior)`을 처리하는 요구사항이 빠진 경우
- **중복(Duplication)**: 동일한 필요성을 정의하는 요구사항이 반복되는 경우
- **장황함(Wordiness)**: 불필요하게 많은 단어를 사용하여 의미를 혼탁하게 하는 경우
- **부적절한 구현 명세(Inappropriate implementation)**: 시스템이 `무엇을(what)` 해야 하는지가 아닌 `어떻게(how)` 구축되어야 하는지를 기술하는 경우
- **검증 불가능성(Poor testability)**: 요구사항이 너무 애매하여 통과/실패를 객관적으로 증명할 수 없는 경우

이러한 상위 수준 요구사항의 문제점은 개발 생명주기 하위 단계(설계, 구현, 테스트)로 필연적으로 전파됩니다. 이는 불필요한 **변동성(volatility)** 과 **위험(risk)** 을 생성하며, 궁극적으로 프로젝트의 **품질 저하, 일정 지연, 비용 상승**이라는 치명적인 결과로 이어집니다.

### 1.3. EARS의 탄생: 롤스로이스(Rolls-Royce)의 안전 필수(Safety-Critical) 시스템

EARS는 이러한 근본적인 문제를 해결하기 위해 **Alistair Mavin**과 그의 동료들이 `롤스로이스(Rolls-Royce PLC)`에서 제트 엔진 제어 시스템의 감항성 규제(airworthiness regulations) 문서를 분석하는 과정에서 개발되었습니다.

**안전 필수(safety-critical) 시스템**에서는 요구사항의 모호성이나 누락이 치명적인 인명 및 재산 피해로 이어질 수 있으므로, 명세의 정밀성이 절대적으로 중요합니다.

롤스로이스의 사례 연구는 EARS 규칙 집합을 적용하여 기존의 규제 문서를 재작성했을 때, 전통적인 텍스트 요구사항 명세서 대비 **정성적, 정량적 측면 모두에서 뚜렷한 개선**이 있었음을 입증했습니다.

### 1.4. EARS의 핵심 목적과 '보이지 않는 엄격함(Stealth Rigour)'

EARS의 핵심 목적은 요구사항의 다음을 획기적으로 높이는 것입니다:

- **명확성(clear)**
- **간결성(concise)**
- **비모호성(unambiguous)**
- **검증 가능성(testable)**

EARS는 복잡한 `형식적 방법론(formal methods)`이 요구하는 막대한 **훈련 오버헤드(training overhead)** 없이도 명세의 품질을 높일 수 있습니다. 전문적인 도구 지원이 반드시 필요하지도 않습니다. 사람들은 본능적으로 자연어 사용을 선호하며, EARS는 `자연어이면서도 쉽다`는 장점을 그대로 유지합니다.

이것이 바로 EARS가 **'보이지 않는 엄격함(Stealth Rigour)'** 이라고 불리는 이유입니다. EARS는 사용자에게 복잡한 규칙을 학습하도록 강요하는 대신, 간단한 템플릿을 통해 자연스럽게 엄격한 구조를 따르도록 유도합니다.

#### EARS의 다층적 역할

**조직의 사회-기술적 합의 도구**

EARS는 단순한 `표기법(notation)`을 넘어, 조직의 `사회-기술적(socio-technical) 합의 도구`로 기능합니다. EARS의 `부드러운(gently)` 제약은 기술적 이해관계자(개발자, 테스터)가 요구하는 **정밀성**과 비기술적 이해관계자(고객, 관리자)가 선호하는 **가독성** 사이의 거대한 간극을 메웁니다.

**사고(思考) 도구**

> "만약 당신이 EARS로 (요구사항을) 작성할 수 없다면, 당신은 그것을 이해하지 못한 것이다"  
> (If you can't write it in EARS, you don't understand it)

EARS는 `작성 도구` 이전에 **사고(思考) 도구**입니다. EARS 템플릿(예: `While... When...`)을 적용하는 행위는 작성자에게 다음을 강제로 질문하게 만듭니다:

- "이 요구사항이 활성화되는 선행 조건(state)은 무엇인가?"
- "정확한 트리거(trigger)는 무엇인가?"

EARS의 절(clauses)들은 **누락된 정보를 식별**하도록 돕습니다. 만약 작성자가 이 질문에 답할 수 없다면, 이는 곧 요구사항 자체가 불완전하거나 작성자가 해당 요구사항을 완전히 이해하지 못했음을 의미합니다. EARS는 이처럼 요구사항의 누락된 구성 요소를 드러내는 **인지적 진단 도구** 역할을 수행합니다.

## 2. EARS 프레임워크: 구문론 및 핵심 구성요소

### 2.1. 일반 구문 (Generic EARS Syntax) 분석

EARS의 모든 패턴은 하나의 일반적인 구문(Generic Syntax) 구조에서 파생됩니다.

**EARS 일반 구문:**

```text
While <pre-condition(s)>, when <trigger>, the <system name> shall <system response>
```

이 구조의 핵심 특징은 절(clause)의 순서가 항상 동일하게 유지된다는 것입니다. 이는 시스템이 동작하는 방식에 대한 **시간적 논리(temporal logic)** 를 자연스럽게 따릅니다.

**논리적 흐름:**

1. 특정 상태가 유지되는 동안 (`While`)
2. 특정 이벤트가 발생하면 (`When`)
3. 해당 시스템은 (`The`)
4. 반드시 특정 응답을 해야 한다 (`shall`)

### 2.2. EARS 절(Clause)의 정밀 해부

EARS의 일반 구문은 네 가지 핵심 구성요소로 분해할 수 있습니다:

#### `While <pre-condition(s)>` (선행 조건)

**정의:** 요구사항이 활성화되기 위해(active) 반드시 `참(true)`이어야 하는 조건(condition)을 정의합니다.

**특징:**

- 시스템의 **상태(state)** 를 나타냅니다
- 이 조건이 충족되지 않으면, 뒤따르는 트리거(`When`)가 발생하더라도 요구사항은 활성화되지 않습니다
- 이벤트 기반 요구사항을 위한 일종의 **가드(guard)** 역할을 수행합니다

#### `When <trigger>` (트리거)

**정의:** 시스템이 감지하는 **이산적인(discrete) 이벤트(event)** 를 정의합니다.

**특징:**

- 요구사항의 즉각적인 활성화를 유발하는 `방아쇠` 역할

#### `The <system name>` (시스템 이름)

**정의:** 요구사항에 대한 책임을 지는 시스템의 명칭입니다.

**특징:**

- EARS는 이 시스템 이름이 반드시 **명시적(explicit)** 이어야 한다고 강조합니다
- 요구사항이 시스템의 내부 구현(`how`)이 아닌 외부에서 관찰 가능한 행동(`what`)에 초점을 맞추는 **블랙박스(black box) 관점**에서 작성되도록 보장합니다
- 시스템의 **경계(boundary)** 와 **책임**을 명확히 합니다

#### `shall <system response>` (시스템 응답)

**정의:** 요구사항이 활성화되었을 때 시스템이 반드시 수행해야 하는 동작(action) 또는 출력(output)을 정의합니다.

**특징:**

- `shall`이라는 강력한 **명령형(imperative)** 의 사용
- 반드시 충족되어야 하며 객관적으로 **검증 가능한(testable)** 요구사항임을 명시합니다

### 2.3. 구문 규칙 요약

EARS의 규칙 집합(ruleset)에 따르면, 모든 요구사항은 다음의 구조적 규칙을 따라야 합니다:

- `0개 이상`의 선행 조건 (`While` 절)
- `0개 또는 1개`의 트리거 (`When` 절)
- `1개`의 시스템 이름
- `1개 이상`의 시스템 응답

이 규칙들은 EARS의 **유연성과 엄격함의 균형**을 보여줍니다. 예를 들어, `While` 절과 `When` 절이 모두 0개인 요구사항은 `Ubiquitous` 패턴이 되고, `While` 절만 0개인 경우는 `Event-Driven` 패턴이 됩니다.

### 2.4. EARS와 유한 상태 기계(FSM) 모델의 관계

EARS 구문은 그 자체로 자연어 형태의 **유한 상태 기계(Finite State Machine, FSM)** 모델을 표현합니다.

```mermaid
graph LR
    A[While: 현재 상태<br/>Current State] --> B[When: 이벤트<br/>Event/Input]
    B --> C[shall: 행동/출력<br/>Action/Output]
    
    style A fill:#e1f5ff
    style B fill:#fff4e1
    style C fill:#e8f5e9
```

**형식적 모델링 관점에서 EARS의 구성 요소:**

- `While <pre-condition>` 절 = FSM의 **현재 상태(Current State)** (요구사항이 활성화될 수 있는 상태)
- `When <trigger>` 절 = FSM의 **이벤트(Event)** 또는 **입력(Input)** (상태 전이를 유발하는)
- `shall <system response>` 절 = FSM의 **행동(Action)** 또는 **출력(Output)** (해당 상태 전이가 발생할 때 수행되는)

이 **State - Event - Action**의 명시적 구조는 EARS가 비정형적 자연어와 정형적(formal) 시스템 모델 사이의 간극을 메우는 강력한 **다리(bridge)** 역할을 함을 증명합니다. EARS로 잘 작성된 요구사항은 시스템 설계자가 UML 상태 다이어그램(Statechart)이나 기타 모델로 거의 직접 변환할 수 있을 만큼 충분히 구조화되어 있으며, 이는 **모델 기반 시스템 엔지니어링(MBSE)** 과의 강력한 시너지를 제공합니다.

## 3. EARS 패턴 상세 분석 (정상 작동: "Sunny Day" 시나리오)

EARS는 요구사항을 몇 가지 명확한 패턴으로 분류합니다. 그 중 4가지 패턴은 시스템이 예상대로 작동하는 **정상 작동(Normal operation)** 또는 **"sunny day" 시나리오**를 명세하는 데 사용됩니다.

### 3.1. 패턴 1: Ubiquitous (보편적 요구사항)

**목적:** 항상 활성화(always active)되어야 하는 시스템의 근본적인 속성, 전역적 제약 조건, 또는 비기능적 요구사항(NFR)을 정의합니다.

**키워드:** `While`이나 `When` 같은 조건 절이 존재하지 않습니다. EARS 키워드가 없는 유일한 패턴입니다.

**구문:**

```text
The <system name> shall <system response>
```

**예시:**

- "The mobile phone shall have a mass of less than XX grams." (휴대폰은 XX 그램 미만의 질량을 가져야 한다.) - 시스템의 물리적 속성에 대한 보편적 제약
- "The software shall be written in Python." (소프트웨어는 Python으로 작성되어야 한다.) - 구현에 대한 전역적 제약조건

### 3.2. 패턴 2: State-Driven (상태 기반 요구사항)

**목적:** 시스템이 특정 상태(state)에 머무는 동안 지속적으로 참(true)이어야 하는 요구사항을 정의합니다.

**키워드:** `While`

**구문:**

```text
While <pre-condition(s)>, the <system name> shall <system response>
```

**예시:**

- "While there is no card in the ATM, the ATM shall display 'insert card to begin'." (ATM에 카드가 없는 동안, ATM은 '카드를 넣어주세요'를 표시해야 한다.) - ATM이 '카드 없음'이라는 상태에 머무는 내내 이 요구사항은 활성화됩니다.
- "While the aircraft is in-flight, the control system shall..." (항공기가 비행 중인 동안, 제어 시스템은...) - '비행 중'이라는 지속적인 상태 동안 적용되는 요구사항입니다.

### 3.3. 패턴 3: Event-Driven (이벤트 기반 요구사항)

**목적:** 특정 이산적(discrete) 트리거(trigger)가 감지되었을 때 즉각적으로, 그리고 단 한 번 발생하는 시스템 응답을 정의합니다.

**키워드:** `When`

**구문:**

```text
When <trigger>, the <system name> shall <system response>
```

**예시:**

- "When 'mute' is selected, the laptop shall suppress all audio output." ('음소거'가 선택되었을 때, 노트북은 모든 오디오 출력을 억제해야 한다.) - '선택'이라는 순간적인 이벤트에 반응합니다.
- "When the payment is received, then the app shall send a notification." (결제가 수신되었을 때, 앱은 알림을 보내야 한다.) - '수신'이라는 이산적 이벤트에 반응합니다.

### 3.4. 패턴 4: Optional Feature (선택적 기능 요구사항)

**목적:** 특정 선택적 기능(optional feature)을 포함하는 시스템 변형(variation) 또는 제품군(product line)에만 적용되는 요구사항을 정의합니다.

**키워드:** `Where`

**구문:**

```text
Where <feature is included>, the <system name> shall <system response>
```

**예시:**

- "Where the car has a sunroof, the car shall have a sunroof control panel on the driver door." (자동차에 선루프가 있는 경우, 자동차는 운전석 도어에 선루프 제어 패널을 가져야 한다.) - 선루프가 없는 기본형 차량 모델에는 이 요구사항이 적용되지 않음을 명확히 합니다.
- "Where the DP port is present, then the software shall allow user to display maximum supported refresh rate." (DP 포트가 존재하는 경우, 소프트웨어는 사용자가 최대 지원 주사율을 표시할 수 있도록 허용해야 한다.) - DP 포트가 없는 하드웨어 변형에는 이 요구사항이 적용되지 않습니다.

### 3.5. While과 When의 핵심 구분: 지속성 vs 순간성

EARS의 가장 강력한 기여 중 하나는 `While`과 `When` 키워드를 명확히 구분하는 것입니다. 이는 **지속성(Continuity)** 대 **순간성(Atomicity)** 의 대립으로, 요구사항 공학의 핵심 난제 중 하나를 해결합니다.

비정형 자연어에서 "사용자가 로그인했을 때..."라는 문장은 '로그인 이벤트가 발생하는 순간'을 의미하는지, '로그인 상태인 동안 계속'을 의미하는지 극도로 모호합니다. EARS는 작성자에게 `While` (State-Driven, 지속적 상태)과 `When` (Event-Driven, 순간적 이벤트) 중 하나를 강제로 선택하게 함으로써, 시스템 엔지니어링에서 가장 흔하게 발생하는 **상태(State)와 이벤트(Event)의 혼동**이라는 치명적인 모호성을 근본적으로 제거합니다.

### 3.6. Where 패턴과 제품 라인 엔지니어링(PLE)

`Where` 패턴은 EARS를 단순한 요구사항 구문을 넘어 **제품 라인 엔지니어링(Product Line Engineering, PLE)** 의 전략적 도구로 격상시킵니다.

현대 엔지니어링(예: 자동차, 항공, 전자)은 단일 제품이 아닌, 수많은 변형(variants)을 가진 제품군(product family)을 개발합니다. `Where` 패턴은 각 요구사항을 특정 `기능(feature)` 또는 `구성(configuration)`에 명시적으로 연결하여, 요구사항의 **적용 범위(scope)**를 관리하는 강력한 메커니즘을 제공합니다.

이는 롤스로이스, 지멘스, NASA와 같은 대규모 엔지니어링 조직에서 복잡한 시스템 변형을 관리하기 위해 EARS를 채택한 핵심 이유 중 하나입니다.

## 4. EARS 패턴 상세 분석 (예외 및 복합 사례)

### 4.1. 패턴 5: Unwanted Behavior (원치 않는 동작 요구사항)

**목적:** **"비 오는 날(rainy day)" 시나리오**를 다룹니다. 이는 오류(errors), 실패(failures), 장애, 또는 예상치 못한 사용자 행동과 같은 바람직하지 않은 상황(undesired situations)에 대한 시스템의 필수적인 응답 또는 완화(mitigation) 조치를 명세합니다.

**키워드:** `If... Then`

**구문:**

```text
If <trigger>, then the <system name> shall <system response>
```

**예시:**

- "If an invalid credit card number is entered, then the website shall display 'please re-enter credit card details'." (유효하지 않은 신용카드 번호가 입력되면, 웹사이트는 '신용카드 정보를 다시 입력하세요'를 표시해야 한다.)
- "If there is an obstruction blocking the Garage Door, then the Garage Door shall enter Emergency Mode." (차고 문을 막는 장애물이 있다면, 차고 문은 비상 모드로 진입해야 한다.)

### 4.2. 패턴 6: Complex (복합 요구사항)

**목적:** EARS의 기본 빌딩 블록(simple building blocks)을 결합하여 더 풍부하고 복잡한, 현실 세계의 시스템 동작을 명세합니다.

**키워드:** `While`, `When`, `If` 등의 조합

**구문 (일반 복합):**

```text
While <pre-condition(s)>, when <trigger>, the <system name> shall <system response>
```

**구문 (원치 않는 동작 복합):**

```text
While <pre-condition(s)>, if <trigger>, then the <system name> shall <system response>
```

**예시 (일반 복합):**

"While the aircraft is on ground, when reverse thrust is commanded, the engine control system shall enable reverse thrust." (항공기가 지상에 있는 동안, 역추진이 명령되면, 엔진 제어 시스템은 역추진을 활성화해야 한다.)

- 이 요구사항은 '지상'이라는 상태에서만 '역추진 명령'이라는 이벤트가 유효함을 명확히 합니다.
- 만약 '비행 중' 상태라면 이 요구사항은 활성화되지 않습니다.

### 4.3. If...Then 패턴의 인지적 분리 설계

`If...Then` 패턴이 `When` 패턴과 별도로 존재하는 것은 의도적인 **인지적 분리** 설계입니다.

비정형 자연어의 주요 문제 중 하나는 `누락(Omission)`, 특히 **원치 않는 동작에 대한 요구사항 누락**입니다. EARS는 "정상 작동"을 위한 `When`과 "예외 처리"를 위한 `If...Then`을 구문론적으로 분리합니다.

`If/Then` 키워드는 독자에게 "이것이 원치 않는 이벤트를 완화하기 위한 요구사항"임을 알려주는 **유용한 신호(useful cue)** 역할을 합니다. 이는 엔지니어가 "happy path"(sunny day)에만 집중하고 "exception path"(rainy day)를 잊지 않도록 돕는 심리적, 시각적 **안전 장치**입니다.

### 4.4. Complex 패턴의 실무적 중요성

`Complex` 패턴은 EARS의 예외적인 사용법이 아니라, 현실 세계의 복잡한 시스템, 특히 **안전 필수 시스템 명세를 위한 표준 형태**입니다.

롤스로이스의 핵심 예제 자체가 단순한 `When` 패턴이 아닌 `While... When...`의 **복합** 패턴이라는 점이 이를 증명합니다. 안전 필수 시스템의 동작(예: 역추진)은 단순한 이벤트(명령)에 의해서만 결정되어서는 안 되며, 반드시 현재 상태(지상)에 따라 엄격하게 제어(guard)되어야 합니다.

단순한 패턴들은 이 복합 패턴을 만들기 위한 재료이며, 이 **상태-의존적 이벤트 처리**야말로 EARS가 제공하는 진정한 가치입니다.

### 4.5. EARS 6가지 패턴 구문 마스터 테이블

| 패턴 유형 | EARS 키워드 | 전체 구문 (Syntax) | 핵심 목적 및 적용 |
|---------|------------|-------------------|----------------|
| **Ubiquitous** | (없음) | `The <system name> shall <system response>` | 항상 활성화되어야 하는 시스템의 보편적 속성, 비기능적 요구사항(NFR) 또는 전역적 제약 조건 |
| **State-Driven** | `While...` | `While <pre-condition(s)>, the <system name> shall <system response>` | 시스템이 특정 상태에 머무는 동안 지속적으로 만족해야 하는 요구사항 |
| **Event-Driven** | `When...` | `When <trigger>, the <system name> shall <system response>` | 특정 이산적 이벤트(트리거)가 발생하는 순간에 실행되어야 하는 응답 |
| **Optional Feature** | `Where...` | `Where <feature is included>, the <system name> shall <system response>` | 특정 선택적 기능이나 시스템 변형(variant)에만 적용되는 요구사항 (제품 라인 관리) |
| **Unwanted Behavior** | `If... Then` | `If <trigger>, then the <system name> shall <system response>` | 오류, 장애 등 "rainy day" 시나리오에 대한 시스템의 예외 처리 및 완화(mitigation) 동작 |
| **Complex** | (조합) | `While <pre-condition(s)>, when <trigger>, the <system name> shall <system response>` | 상태와 이벤트를 조합하여, 특정 상태에서만 특정 이벤트가 유효하도록 하는 복잡하고 현실적인 시스템 동작 |

## 5. 전략적 이점 및 엔지니어링 파급 효과

### 5.1. 요구사항 품질의 근본적 향상

EARS의 채택은 요구사항 명세서의 품질을 여러 차원에서 즉각적으로 향상시킵니다:

**명확성 및 정밀성 (Clarity & Precision)**

- EARS는 모호하고 장황한(ambiguous or verbose) 전통적 요구사항과 달리, 명확하고 간결한(clear and concise) 문장을 생성합니다
- 시간적 논리와 명시적 키워드는 정밀성을 최적화합니다

**일관성 및 표준화 (Consistency & Standardization)**

- EARS는 모든 요구사항에 걸쳐 **균일한 구문(uniform syntax)** 을 제공합니다
- 여러 명의 저자가 또는 분산된 팀이 요구사항을 작성하더라도 일관된 스타일과 구조를 유지하게 합니다

**검증 가능성 (Testability)**

- EARS의 구조(특히 `When` 또는 `If`의 조건과 `shall`의 응답을 명확히 분리)는 요구사항을 본질적으로 **검증 가능하게(testable)** 만듭니다
- 명확한 트리거와 예상되는 시스템 응답은 테스트 케이스 설계를 직접적으로 지원하며, 이는 사양 기반 테스트(specification-based testing)의 전제 조건입니다

### 5.2. 자연어 요구사항의 고질적 문제 해결

EARS는 앞서 1.2절에서 언급된 비정형 자연어의 8가지 고질적 문제를 **감소시키거나 심지어 제거합니다**.

- EARS 템플릿은 불필요한 단어(장황함)를 허용하지 않으며, 조건과 응답을 분리하여 복잡성을 낮춥니다
- 특히 `If...Then` 패턴은 **원치 않는 동작(unwanted behavior)** 의 누락을 방지합니다
- 템플릿 자체는 **누락된 정보**(예: 트리거 또는 선행 조건)를 식별하도록 강제합니다

### 5.3. 엔지니어링 및 관리적 이점

**'블랙박스(Black Box)' 시스템 사고 증진**

EARS 템플릿은 저자가 시스템의 내부 구현('어떻게')이 아닌, 시스템의 관찰 가능한 책임('무엇을')에 집중하도록 강제합니다. 이는 `부적절한 구현(Inappropriate implementation)` 명세를 방지하는 시스템 엔지니어링의 핵심 원칙입니다.

**이해관계자 간 커뮤니케이션 향상**

EARS는 기술적이지 않은 이해관계자(non-technical stakeholders)도 쉽게 이해할 수 있는 자연어 기반입니다. 이는 요구사항 검토(review) 주기를 더 빠르고 효과적으로 만들며, 모든 당사자 간의 공통된 이해를 증진시킵니다.

**접근성 (비영어권 저자)**

EARS는 영어가 모국어가 아니지만 영어로 요구사항을 작성해야 하는 저자들에게 **특히 도움이 됩니다**. 간단하고 명확한 템플릿은 문법적, 구조적 실수를 줄여주고 명확한 의사소통을 돕습니다.

**도구 지원 및 자동화**

EARS는 도구 지원 없이도 매우 효과적이지만, `Jama Connect`, `QVscribe`와 같은 상용 요구사항 관리 및 품질 분석 도구들은 EARS 템플릿을 내장하고 있습니다. 이러한 도구는 EARS 구문 사용을 최적화하고, 품질 분석, 일관성 검사 및 테스트 생성을 자동화하는 데 큰 도움을 줍니다.

### 5.4. 정형화 스펙트럼의 최적 지점: Sweet Spot

EARS가 이처럼 광범위한 이점을 제공하는 이유는, **정형화의 비용-편익 스펙트럼에서 최적의 'Sweet Spot'** 을 점유하기 때문입니다.

요구사항 명세에는 두 가지 극단이 존재합니다:

1. **비정형 자연어** - 이해하기 쉽지만 모호하고 위험함
2. **완전한 정형적 방법(Formal Methods)** (예: Z, VDM) - 매우 정확하지만 배우기 어렵고 막대한 훈련 오버헤드와 비용을 유발함

EARS는 이 스펙트럼의 완벽한 중간 지점에 위치합니다:

- **경량(lightweight)**이면서 **자연어**의 접근성을 유지
- **엄격함(rigour)** 과 **정밀성(precision)** 이라는 정형성의 핵심 이점을 대부분 제공

EARS는 **비정형과 정형 사이의 효과적인 다리(bridge)** 역할을 수행하며, Bosch, Intel, NASA, Siemens와 같은 대규모 엔지니어링 조직이 막대한 훈련 비용 없이도 요구사항 품질을 획기적으로 향상시키기 위해 EARS를 광범위하게 채택한 이유가 바로 여기에 있습니다.

### 5.5. 전통적 자연어(NL) vs. EARS 요구사항 비교

| 평가 측면 | 전통적 자연어(NL) 요구사항 | EARS 표기법 요구사항 |
|---------|------------------------|---------------------|
| **명확성 (Clarity)** | 종종 모호하고(ambiguous) 장황함(verbose). 다의성(ambiguity)과 애매성(vagueness) 내포 | 명확하고(Clear) 간결함(concise). 정밀성이 최적화됨 |
| **표준화 (Standardization)** | 팀과 저자에 따라 매우 다양함(Varies widely). 일관성 없음 | 모든 요구사항에 걸쳐 균일한 구문(Uniform syntax) 제공 |
| **이해 용이성 (Ease of Understanding)** | 비기술적 이해관계자가 이해하기 어려움. 혼란스러움(confusing) | 모든 이해관계자가 쉽게 이해할 수 있음. 직관적으로 단순함 |
| **추적/검증 (Traceability/Testability)** | 유지 관리가 어려움. 검증 가능성(testability)이 낮음 | 구조화된 구문으로 추적성 향상. 검증 가능성이 높음 |
| **누락 (Omission)** | 특히 예외 처리(unwanted behavior) 누락이 빈번함 | 누락된 정보를 식별하도록 강제함. `If...Then` 패턴이 예외 처리를 명시적으로 다룸 |

## 6. EARS의 한계 및 실무적 제약 사항

### 6.1. EARS를 사용하지 않아야 하는 경우 (When Not to Use EARS)

EARS는 매우 강력하고 널리 적용 가능한 접근법이지만, **만병통치약은 아닙니다**. 모든 요구사항을 EARS 형식으로 작성해야 하는 것은 아니며, 경우에 따라 EARS 형식을 고수하는 것이 오히려 명확성을 해칠 수 있습니다.

> **핵심 원칙:** 요구사항은 언제나 '해당 명세서의 독자(user of the specification)에게 가장 적절한 표기법'으로 작성되어야 한다

### 6.2. 제약 1: 과도한 복잡성 및 3개 초과 선행 조건

요구사항이 EARS 형식으로 표현하기에 **너무 복잡한(too complicated)** 경우가 있습니다. EARS는 요구사항이 단일 문장이어야 한다는 모범 사례를 따릅니다.

**실무적인 경험적 규칙(Rule of thumb):**

만약 요구사항이 **3개 이상의 선행 조건(preconditions)** 을 갖는다면 EARS 문장은 매우 길고 번거로워져(cumbersome) 가독성이 떨어집니다.

**대안:**

- 조건을 명확한 **목록(list)** 형식으로 나열
- 복잡한 조건과 그에 따른 응답의 조합을 명시하기 위해 **의사결정표(decision table)** 사용

### 6.3. 제약 2: 수학적 및 알고리즘적 요구사항

요구사항이 **수학적인(mathematical)** 경우, EARS 템플릿은 적합하지 않습니다. 알고리즘, 복잡한 수식, 또는 계산 로직을 EARS의 자연어 구문으로 명확하게 표현하는 것은 거의 불가능하며 바람직하지도 않습니다.

**대안:**

- **의사 코드(Pseudocode)**
- **수학 공식**
- **시각적 모델** (예: 순서도, 상태 다이어그램)

### 6.4. 제약 3: 비기능적 요구사항 (NFR) 및 성능

EARS가 요구사항의 **성능 측면(performance aspects)**(예: 응답 시간, 처리량, 신뢰성)을 명시하는 좋은 방법을 "지원하지 않는다"고 지적됩니다.

EARS는 본질적으로 **블랙박스** 기능적 동작('무엇을' 하는가)에 초점을 맞추기 때문에, '얼마나 잘' 수행하는지를 명세하는 데는 자연스럽게 최적화되어 있지 않습니다.

**대안:**

- `Ubiquitous` 또는 `State-Driven` 요구사항의 `<system response>` 절 내에 성능 제약 조건 포함 (예: "...within 0.5 seconds...")
- `Planguage` (예: 'scale', 'meter' 키워드 사용)
- NFR 프레임워크와 같은 보완적인 접근 방식 병용

### 6.5. 제약 4: 대상 독자(Audience) 부적합성

EARS는 **고수준(high-level) 시스템 요구사항**이나 다양한 이해관계자(기술, 비기술)가 함께 보는 문서에 이상적입니다.

그러나 명세서의 대상 독자가 명확하게 **저수준(low-level) 소프트웨어 개발자들**인 경우, 그들은 EARS의 자연어 문장보다 다음에 더 익숙할 수 있습니다:

- 의사 코드(pseudocode)
- 상태 전이 다이어그램
- API 시그니처

### 6.6. 제약 5: 도메인 특수성(Domain-Specificity)의 한계

EARS는 광범위한 채택을 용이하게 하기 위해 의도적으로 **도메인 독립적(domain-independently)**으로 설계되었습니다. 이는 장점이지만, 동시에 특정 도메인(예: 금융 거래 프로토콜, 통신)에서 요구하는 고유의 구문이나 의미 구조(semantics)를 지원하는 데는 한계가 있음을 의미합니다.

**이러한 한계는 EARS의 '실패'가 아니라 '설계 철학'의 반영입니다.**

EARS의 핵심 설계 목표는 **경량성(lightweight)**, **쉬움(easy)**, 그리고 **명확성(clarity)**입니다. 3개 이상의 조건을 가진 복잡한 논리나 수학 공식을 EARS 템플릿에 억지로 끼워 맞추는 것은 이 핵심 목표를 위반합니다.

EARS는 자신이 빛날 수 있는 영역(기능적 명세)에 집중하고, 그렇지 않은 영역(알고리즘, 복잡 논리)은 다른 표기법에 기꺼이 자리를 양보하는 **실용적인 가이드라인**입니다.

### 6.7. EARS 적용성 및 한계 매트릭스

| 요구사항 유형 | EARS 적용 적합성 | 권장되는 EARS 패턴 | 한계 및 대안 (Alternative) |
|------------|----------------|------------------|-------------------------|
| 단순 기능 (Simple Function) | 높음 | Event-Driven, State-Driven | - |
| 기본 제약 (Core Constraint) | 높음 | Ubiquitous | - |
| 예외 처리 (Error Handling) | 높음 | Unwanted Behavior, Complex | - |
| 제품 변형 (Product Variants) | 높음 | Optional Feature | - |
| 복합 논리 (Complex Logic)<br/>(예: 3개 초과 선행 조건) | 낮음 (권장 안 함) | - | 대안: 목록(Lists) 또는 의사결정표(Decision Tables) 사용 |
| 알고리즘 / 수학 (Algorithm / Math) | 매우 낮음 (권장 안 함) | - | 대안: 의사 코드(Pseudocode), 수학 공식, 상태 다이어그램 |
| 성능 (Performance)<br/>(예: 응답 시간) | 중간 | Ubiquitous (NFR로 기술) | EARS가 명시적으로 지원하지 않음. 대안: 요구사항 텍스트 내에 정량적 수치 명시, Planguage 등 NFR 전문 표기법 병용 |
| 저수준 소프트웨어 설계 (Low-Level Design) | 낮음 (권장 안 함) | - | 개발자는 EARS보다 의사 코드나 다이어그램을 선호할 수 있음 |

## 7. 광범위한 개발 라이프사이클 내 EARS의 위치

EARS는 독립적인 표기법을 넘어, 현대의 **애자일(Agile)** 및 **BDD(Behavior-Driven Development)** 개발 라이프사이클과 강력하게 통합될 수 있습니다.

### 7.1. 애자일(Agile): 사용자 스토리(User Story)의 모호성 극복

애자일 개발의 핵심 산출물인 사용자 스토리(User Stories)는 다음 형식으로 작성됩니다:

```text
As a <role>, I want <feature>, so that <benefit>
```

이는 '왜'와 '무엇을'에 집중하지만, 종종 모호한 언어와 불분명한 요구사항으로 인해 오해와 **범위 변경(scope creep)**을 유발할 수 있습니다.

EARS는 이러한 사용자 스토리를 뒷받침하는 구체적인 **인수 기준(Acceptance Criteria, AC)**을 작성하는 데 매우 강력하고 구조화된 템플릿을 제공합니다. EARS로 작성된 AC는 개발팀에게 구현해야 할 정확한 동작을 모호함 없이 전달합니다.

**예시 (전자상거래 결제 스토리):**

**사용자 스토리:**

"As a shopper, I want to pay for my items, so that I can complete my purchase."

**EARS 기반 인수 기준 (AC):**

- **(Ubiquitous AC)**: The system shall display available payment methods to the user.
- **(Event-Driven AC)**: When the user selects a payment method, the system shall prompt for payment details.
- **(State-Driven AC)**: While processing the payment, the system shall display a confirmation message.
- **(Unwanted AC)**: If the payment is declined, then the system shall display an error message.

### 7.2. BDD(Behavior-Driven Development)로의 '연결고리(Segue)'

EARS는 요구사항 명세에서 BDD로의 전환, 즉 **"segue"** 를 훨씬 더 쉽게 만듭니다.

개발 현장에서 사용자 스토리와 인수 기준(AC)을 BDD의 `Gherkin` (Given-When-Then) 테스트 시나리오로 변환하는 작업은 "항상 쉽지는 않습니다". EARS는 이 간극을 완벽하게 메워줍니다.

EARS로 잘 작성된 요구사항은 BDD 시나리오로 **거의 1:1 변환이 가능**하며, 한 개발자는 EARS 요구사항을 (SpecFlow) 피처 파일에 그대로 복사하여 테스트를 쉽게 작성했다고 증언합니다.

### 7.3. EARS와 Gherkin (Given/When/Then) 구문 매핑

EARS의 구조는 Gherkin의 구조와 다음과 같이 자연스럽게 정렬됩니다:

**Given (컨텍스트/상태)**

Gherkin의 `Given`은 시나리오가 실행되기 위한 **선행 조건** 또는 **컨텍스트**를 설정합니다. 이는 EARS의 다음 절에 해당합니다:

- `While <pre-condition>` (시스템이 특정 상태에 있음)
- `If <trigger>` (예외적 조건이 주어짐)

**When (이벤트/트리거)**

Gherkin의 `When`은 시스템에 가해지는 **행위** 또는 **이벤트**를 나타냅니다. 이는 EARS의 `When <trigger>` (이벤트) 절과 직접 매핑됩니다.

**Then (결과/응답)**

Gherkin의 `Then`은 **기대되는 결과** 또는 **검증 가능한 출력**을 명세합니다. 이는 EARS의 다음 절과 직접 매핑됩니다:

- `The <system name> shall <system response>`
- `then the <system name> shall <system response>`

### 7.4. EARS-Gherkin 구문 매핑 테이블 (BDD 연결고리)

| EARS 구문 (요구사항) | Gherkin 구문 (BDD 시나리오) | 매핑 역할 (Mapping Role) | EARS 예시 | Gherkin 변환 예시 |
|---------------------|---------------------------|------------------------|-----------|------------------|
| `While <pre-condition(s)>` | `Given <context or state>` | 선행 조건 (State) | While the aircraft is on ground... | Given the aircraft is on ground |
| `If <unwanted trigger>` | `Given <context>` / `When <event>` | 예외적 선행 조건 (State) | If an invalid credit card number is entered... | Given an invalid credit card number is entered |
| `When <trigger>` | `When <event or action>` | 트리거 (Event) | ...when reverse thrust is commanded... | When reverse thrust is commanded |
| `The <system> shall <response>` | `Then <outcome>` | 기대 결과 (Response) | ...the engine control system shall enable reverse thrust. | Then the engine control system shall enable reverse thrust |
| `...then the <system> shall <response>` | `Then <outcome>` | 기대 결과 (Response) | ...then the website shall display "please re-enter..." | Then the website shall display "please re-enter..." |

### 7.5. EARS: Gherkin의 정체성 혼란 해결

BDD와 Gherkin의 일반적인 비판 중 하나는 "QA 부서가 요구사항을 작성하게 둔 것 같다"는 것입니다. 즉, Gherkin 피처 파일이 비즈니스 요구사항이라기보다는 기술적인 테스트 스크립트로 변질되어, 비즈니스와 개발 간의 소통 도구라는 본래의 목적을 잃는다는 것입니다.

**EARS는 이러한 Gherkin의 '정체성 혼란'을 해결하는 '단일 진실 공급원(Single Source of Truth, SSOT)' 역할을 합니다.**

EARS를 BDD 프로세스 앞단에 도입하는 워크플로우(`EARS -> Gherkin`)에서:

- EARS 문장은 그 자체로 **단일 진실 공급원**인 요구사항으로 확립됩니다
- Gherkin 시나리오는 이 EARS 요구사항을 검증하는 여러 개의 구체적인 **사례(Example)** 또는 **시나리오**가 됩니다

이는 **"사례 기반 요구사항(Requirements by Example)"** 개념을 완벽하게 구현합니다. 결론적으로 EARS는 Gherkin이 비즈니스 의도에서 벗어나 기술적 테스트 스크립트로 전락하는 것을 방지하는 강력한 **닻(anchor)** 역할을 수행합니다.

## 8. 결론적 분석 및 권고 사항

### 8.1. EARS의 본질: 단순함을 통한 엄격함의 달성

**EARS(Easy Approach to Requirements Syntax)** 는 복잡한 시스템 요구사항의 고질적인 모호성을 제거하기 위해 롤스로이스(Rolls-Royce)와 같은 안전 필수 환경에서 탄생하고 검증된, 지극히 실용적인 접근 방식입니다.

이는 **'보이지 않는 엄격함(Stealth Rigour)'** 이라는 핵심 철학을 구현합니다. EARS는 요구사항 작성자에게 과도한 훈련 부담이나 복잡한 형식주의(formalism)를 강요하지 않습니다. 대신, **5~6개의 간단하고 직관적인 템플릿**을 통해, 작성자가 자연스럽게 명확하고, 일관되며, 검증 가능한 요구사항을 작성하도록 유도합니다.

### 8.2. EARS의 핵심 가치 요약: 작성에서 검증까지

EARS의 가치는 단순한 '문장 템플릿'을 넘어, 엔지니어링 라이프사이클 전반에 걸쳐 세 가지 핵심적인 역할을 수행하는 데 있습니다.

**1. 사고의 도구 (Cognitive Tool)**

> "If you can't write it in EARS, you don't understand it"

EARS는 작성자가 스스로의 이해도를 점검하고 누락된 정보(선행 조건, 트리거, 예외 상황)를 식별하게 하는 강력한 진단 도구입니다.

**2. 커뮤니케이션 도구 (Communication Tool)**

EARS는 비기술적 이해관계자와 기술적 구현자 사이의 **공통 언어** 역할을 합니다. 이는 요구사항 검토 및 합의 과정을 가속화하고, 값비싼 오해를 방지합니다.

**3. 자동화의 교두보 (Bridge to Automation)**

EARS는 비정형 자연어(NL)와 정형 모델(FSM) 및 BDD(Behavior-Driven Development)와 같은 테스트 자동화 프레임워크 사이의 핵심적인 **다리(bridge)**입니다. EARS의 구조화된 특성은 도구를 통한 구문 분석, 일관성 검사, 심지어 테스트 케이스 생성까지 용이하게 하는 기반을 제공합니다.

### 8.3. 실무 도입을 위한 권고 사항

EARS를 성공적으로 도입하고 그 가치를 극대화하기 위해 다음의 실무적 권고 사항을 제시합니다.

#### 1. EARS는 만병통치약이 아니다

EARS의 명확한 한계(복잡 논리, 수학, 성능)를 인식해야 합니다. 이러한 유형의 요구사항을 EARS 템플릿에 억지로 맞추려 하지 말고, **의사결정표, 수학 공식, NFR 명세서 등 가장 적절한 표기법**을 요구사항 명세서 내에서 함께 사용해야 합니다.

#### 2. 'Sunny Day'와 'Rainy Day'를 의식적으로 분리하라

`When` (정상) 패턴과 `If...Then` (예외) 패턴을 의식적으로 사용하여, 시스템의 예외 처리 및 오류 완화 요구사항이 치명적으로 누락되지 않도록 보장해야 합니다.

#### 3. BDD와 통합하여 SSOT를 구축하라

EARS를 단순히 요구사항 작성에만 그치지 말고, BDD의 `Given/When/Then` 시나리오를 도출하는 입력 및 **단일 진실 공급원(SSOT)** 으로 활용해야 합니다. EARS 요구사항을 먼저 정의하고, 이를 검증하는 Gherkin 시나리오를 작성함으로써, 요구사항과 테스트 케이스 간의 강력한 추적성과 일관성을 확보할 수 있습니다.

### 8.4. 최종 결론

결론적으로, EARS는 요구사항 엔지니어링 분야에서 가장 **실용적이고 비용 효율적인 '품질 향상' 개입** 중 하나입니다. 이는 비정형적인 아이디어를 검증 가능한 명세로 변환하는 구조화된 여정을 제공하며, 복잡한 시스템 개발의 첫 단추를 올바르게 끼우는 핵심적인 역할을 수행합니다.

**EARS의 채택은 논리적입니다.**

## 요약

- **EARS(Easy Approach to Requirements Syntax)** 는 자연어 기반 요구사항 명세서에 부드러운 제약을 가하는 실용적인 표기법으로, 롤스로이스의 안전 필수 시스템 개발 과정에서 탄생했습니다
- 비정형 자연어의 8가지 고질적 문제(모호성, 애매성, 복잡성, 누락, 중복, 장황함, 부적절한 구현, 검증 불가능성)를 근본적으로 해결합니다
- **"보이지 않는 엄격함(Stealth Rigour)"** 철학을 따르며, 막대한 훈련 오버헤드 없이도 명세의 품질을 획기적으로 향상시킵니다
- EARS의 일반 구문은 `While <pre-condition(s)>, when <trigger>, the <system name> shall <system response>` 형식으로, 시간적 논리를 자연스럽게 따릅니다
- 4가지 핵심 구성요소는 While(선행 조건/상태), When(트리거/이벤트), The(시스템 이름), shall(시스템 응답)입니다
- EARS는 자연어 형태의 유한 상태 기계(FSM) 모델을 표현하며, State-Event-Action 구조로 정형 모델과의 강력한 연결고리를 제공합니다
- **6가지 핵심 패턴**: Ubiquitous(보편적), State-Driven(상태 기반), Event-Driven(이벤트 기반), Optional Feature(선택적 기능), Unwanted Behavior(원치 않는 동작), Complex(복합)
- **While과 When의 핵심 구분**은 지속성(Continuity) 대 순간성(Atomicity)의 대립을 명확히 하여, 상태와 이벤트 혼동이라는 치명적 모호성을 제거합니다
- **If...Then 패턴**은 정상 작동(When)과 예외 처리를 의도적으로 분리하여, 원치 않는 동작에 대한 요구사항 누락을 방지하는 인지적 안전 장치입니다
- **Where 패턴**은 EARS를 제품 라인 엔지니어링(PLE)의 전략적 도구로 격상시켜, 복잡한 시스템 변형 관리를 가능하게 합니다
- EARS는 정형화 스펙트럼에서 최적의 Sweet Spot을 점유하며, 경량성과 자연어의 접근성을 유지하면서도 엄격함과 정밀성을 대부분 제공합니다
- 요구사항의 명확성, 간결성, 일관성, 표준화, 검증 가능성을 즉각적으로 향상시키며, 블랙박스 시스템 사고를 증진합니다
- EARS의 3가지 핵심 역할: (1) 사고의 도구(Cognitive Tool), (2) 커뮤니케이션 도구(Communication Tool), (3) 자동화의 교두보(Bridge to Automation)
- **한계 및 제약**: 3개 초과 선행 조건을 가진 복합 논리, 수학적/알고리즘적 요구사항, 성능 측면의 비기능적 요구사항(NFR), 저수준 소프트웨어 설계에는 적합하지 않으며, 이 경우 의사결정표, 수학 공식, 의사 코드 등의 대안을 사용해야 합니다
- **애자일 통합**: 사용자 스토리의 모호성을 극복하고, 구조화된 인수 기준(Acceptance Criteria) 작성을 가능하게 합니다
- **BDD 통합**: EARS 구문은 Gherkin(Given-When-Then)과 자연스럽게 1:1 매핑되며, 단일 진실 공급원(SSOT) 역할을 수행하여 Gherkin의 정체성 혼란을 해결합니다
- EARS는 비정형적 아이디어를 검증 가능한 명세로 변환하는 구조화된 여정을 제공하며, Bosch, Intel, NASA, Siemens 등 대규모 엔지니어링 조직에서 광범위하게 채택되고 있습니다
- **실무 도입 권고**: (1) EARS는 만병통치약이 아님을 인식, (2) Sunny Day와 Rainy Day를 의식적으로 분리, (3) BDD와 통합하여 SSOT 구축

