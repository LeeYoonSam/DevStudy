# 코툴린을 다루는 기술

## 이 책의 목표

코틀린을 사용해 더 안전한 프로그램을 작성하는 방법을 익히도록 하는 것

- 오래전부터 다양한 환경에서 발전해 온 기법을 가르쳐준다. 사실 그 기법의 대부분은 함 수형 프로그래밍에서 왔다.
- 실용적이고 안전한 프로그래밍에 관한 책이다.
- 여기서 설명한 모든 기법은 자바 생태계에서 여러 해 동안 프로덕션에 사용해 왔던 것으로, 전통 적인 명령형 프로그래밍 기법보다 버그 발생을 줄이는 데 효과적임이 입증됐다. 이런 안전한 기법은 어떤 언어로도 구현할 수 있으며 자바에서도 다년간 이런 기법을 사용해 왔다. 하지만 이런 기법의 채택은 자바의 한계를 극복하려는 몸부림을 통해 이뤄지곤 했다.
- 자신의 전문 분야 에서 버그가 없는 프로그램을 더 쉽고 안전하게 작성하고 싶은 프로그래머를 위한 책

## 책에서 다루는 내용

이 책에서 다루는 기법은 다음과 같다.
• 좀 더 추상화하기
• 불변성(immutability) 우선 사용하기
• 참조 투명성(referential transparency) 이해하기
• 상태 변이(state mutation) 공유 캡슐화하기
• 제어 흐름과 제어 구조 추상화하기
• 올바른 타입 사용하기
• 지연(lazy) 계산 활용하기
• 기타 등등

### 좀 더 추상화하기

여러분이 배울 중요한 기법의 하나로 추상화를 더 많이 하는 것을 들 수 있다. 그런데 전통적 로그래머들은 섣부른 추상화를 섣부른 최적화와 마찬가지로 나쁘다고 생각한다. 하지만 추 더 진행하면 해결하려는 문제를 더 잘 이해할 수 있고, 그에 따라 엉뚱한 문제 대신 제대로 제를 해결할 수 있다.
추상화를 좀 더 한다는 말이 어떤 의미인지 궁금할 것이다. 간단히 말해 여러 계산에 존 공통 패턴을 인식해 이런 패턴을 추상화함으로써 같은 패턴을 쓰고 또 쓰는 일을 피하는 한다.

### 불변성

불변성(immutability)은 변경 불가능한 데이터만 사용하는 기법이다. 전통적인 프로그래 경할 수 없는 데이터만 사용해 쓸모 있는 프로그램을 작성하는 것이 어떻게 가능한지 상 려울 수 있다. 프로그래밍은 데이터의 변경에 기반을 둔 행위가 아니던가? 그러나 이런 치 회계라는 행위가 주로 장부에 있는 값의 변경에 기반을 둔다는 믿음과 비슷하다.
변경 가능한 회계에서 변경 불가능한 회계로 전환이 이뤄진 때가 15세기다. 그 이후 불변성이라 는 원칙은 회계의 안전성을 이루는 주요소로 간주해 왔다.' 같은 원칙을 프로그래밍에도 적용할 수 있다. 이에 관해 이 책에서 살펴본다.

### 참조 투명성(referential transparency)

참조 투명성(referential transparency)은 결정적인(deterministic) 프로그램을 작성할 수 있거 기법이다. 이때 결정적이라 함은 프로그램의 결과를 예측하고 추론할 수 있다는 뜻이다 프로그램은 같은 입력이 주어지면 항상 같은 결과를 내놓는다. 이 말은 프로그램이 힝 과를 내놓는다는 뜻이 아니고, 다른 어떤 외부 요소에 의해 결과가 달라지지 않고 입 때만 결과가 달라진다는 뜻이다.
이런 프로그램은 (프로그램의 동작을 항상 알 수 있기 때문에) 더 안전할 뿐 아니라 더 하고, 유지 보수하고, 변경하고, 테스트할 수 있다. 그리고 테스트하기 쉬운 프로그램 로 테스트가 더 철저하게 이뤄지며 그에 따라 신뢰성이 더 높아진다.

### 상태 변이 공유의 캡슐화

변경 불가능한 데이터를 사용하면 상태 변이를 실수로 공유하는 일을 막을 수 있다. 상태 변이 를 공유하면 동시 처리나 병렬 처리에서 데드락(deadlock)과 라이브락(livelock), 스레드 기아 상태 (starvation), 최신 상태를 반영하지 못하는 데이터(stale data) 같은 문제가 일어날 수 있다. 하지만 상태 변이를 공유하지 못하게 만들면 상태 변이를 반드시 공유해야만 할 때 문제가 된다. 동시성 이나 병렬 프로그래밍에서 바로 이런 일이 벌어진다.
상태 변이를 제거하면 실수로 상태 변이를 공유하는 일이 불가능해지므로 프로그램이 더 안전해 니다. 하지만 동시성 프로그래밍과 병렬 프로그래밍을 한다는 말은 상태 변이를 공유해야 한다는
1을 암시한다. 상태 변이 공유가 없다면 동시 또는 병렬 스레드 사이의 협력이 불가능하다. 전통적인 프로그래밍에서는 상태 공유가 일어날 때마다 매번 같은 용례를 재구현해 왔지만, 그렇게 하 는 대신 구체적 용례를 추상화하고 캡슐화해서 구현한 다음에 그 구현을 완전히 테스트함으로써 여러 번 재사용해도 아무 문제가 없게끔 만들 수 있다.
이 책에서는 상태 변이 공유를 추상화하고 캡슐화하는 방법을 배운다. 이에 따라 같은 상태 공유 기법을 오직 한 번만 작성하면 된다. 그 후 필요할 때마다 이미 작성한 구현을 재사용하면 된다.

### 제어 흐름과 제어 구조 추상화

프로그램에서 상태 변이를 공유하는 것 다음으로 일반적인 버그의 원인에 제어 구조를 들 수 있 다. 전통적 프로그램은 루프나 조건 테스트 같은 제어 구조로 구성된다. 이런 구조는 잘못 사용하 기 쉬워서 프로그래머들은 가능하면 최대한 세부 제어를 추상화하고 싶어 한다. 가장 좋은 예로 대부분 언어에 존재하는 for each 루프를 들 수 있다(자바에서는 for each 루프를 여전히 그냥 for
라고 부른다).
그 외에 while이나 do while(또는 repeat until)을 제대로 사용하지 못해 생기는 문제도 흔하다.
특히 언제 조건을 검사해야 하는지 제대로 알지 못하는 경우가 많다. 또 다른 문제로는 컬렉션에 대한 루프를 수행하는 도중에 컬렉션의 원소를 변경하는 것을 들 수 있다. 이런 경우는 단일 스레 드 상에서 상태 변이를 공유함으로써 생기는 문제와 부딪칠 수 있다! 제어 구조를 추상화하면 이 런 유형의 문제를 완전히 없앨 수 있다.

### 올바른 타입의 사용

전통적 프로그래밍에서는 단위'를 고려하지 않고 int나 String과 같은 일반적인 타입을 사용해 양 을 표현하는 경우가 많다. 그 결과 킬로미터에 리터를 더하거나 시간에 금액을 더하는 등 값의 타입과 관련한 실수를 저지르기 쉽다. 값 타입을 사용하면 이런 종류의 문제를 비용을 거의 들이지 않고 해결할 수 있다. 심지어 진정한 값 타입을 제공하지 않는 언어를 사용하더라도 단위를 고려 한 값 타입을 사용하면 이런 문제를 방지할 수 있다.

### 지연 계산

일반적인 대부분의 언어는 즉시(trict) 계산을 수행한다. 이는 메서드나 함수를 호출하기 달되는 인자들을 먼저 계산한다는 뜻이다. 반대로 지연(azy) 계산은 어떤 요소가 실제로 만 계산을 수행하는 방식이다. 프로그래밍은 근본적으로 지연 계산에 근원을 두고 있다.
예를 들어 if ... else 구조에서 조건은 즉시 계산된다. 즉, if 조건을 판단하기 전에 먼저 계산한다. 하지만 1f ... else 구조의 각 분기는 지연 계산된다. 즉, 조건에 따라 어야 하는 분기만 실행된다. 이런 지연 계산은 프로그래머가 제어할 수 없다는 측면에서 전히 묵시적이다. 하지만 지연 계산을 명시적으로 다룰 수 있게 만들면 보다 더 효율적 램을 작성할 수 있다.'

## 이 책을 읽는 방법

이 책의 각 장은 앞에서 배운 내용을 바탕으로 이뤄지기 때문에 순서대로 읽어야 한다. 여기서 읽는다는 표현을 썼지만, 단지 책을 읽기만 해도 된다는 뜻은 아니다. 이론적인 내용만 들어 있는 절 은 거의 없다.
이 책에서 최대한 많은 것을 얻어내려면 연습문제를 풀어보라. 각 장에는 연습문제와 각 문제를 풀 때 필요한 지시 사항, 해법을 찾을 때 도움이 되는 힌트가 들어 있다. 연습문제마다 모범 답안 이 있으며 여러분이 푼 해법이 맞는지 검증하는 테스트도 있다.

이 책에서 사용한 코드는 길벗출판사 깃허브에서 내려받을 수 있다. 코드에는 프로젝트를 인텔리 J (Intell IDEA)에 임포트하는 데 필요한 모든 요소와 그레이들에서 컴파일하고 실행하는 데 필요 한 모든 요소가 들어 있다(필자는 인텔리)를 추천한다. 그레이들을 사용한다면 어느 텍스트 편 집기에서나 코드를 편집할 수 있다.
• 길벗출판사 깃허브 https://github.com/gilbutrTbook/080208

## 1장 프로그램을 더 안전하게 만들기

• 결값을 반환하는 함수를 외부와 상호 작용하는 효과와 명확히 분리함으로써 프로그램을 더 안전하게 만들 수 있다.
• 함수의 출력이 결정적이고 함수가 외부 상태에 의존하지 않는다면 함수를 더 쉽게 테스트하 고 함수의 성질을 더 잘 추론할 수 있다.
• 더 높은 수준까지 추상화를 추구한다면 안전성, 유지 보수 용이성, 테스트 용이성, 재사용성 이 좋아진다.
• 불변성이나 참조 투명성과 같은 안전성 원칙을 적용하면 프로그램이 상태 변이를 실수로 공 유하는 경우를 방지할 수 있다. 다중 스레드 환경에서 발생하는 버그 중 아주 많은 경우가 상태 변이 공유로 인해 생겨난다.

## 2장 코틀린 프로그래밍 개요

- 필드 선언
  - 필드를 초기화하는 값을 보고 변수 타입 추론
- 가변 필드
  - 참조가 가리키는 대상이 바뀌지 않으면 타입 추론이 쉽다.
- val, var
- 지연 초기화
  - 실제 사용전까지 초기화를 하지 않음
  - by 는 내부적으로 getValue 를 하고 있기 때문에 이 변수는 lazy 람다의 결과값의 타입의 변수가 된다.
    - ex)
      - val a = lazy { 1 } -> a 타입은 Lazy<Int>
      - val a by lzay { 1 } -> a 타입은 Int
- class, interface, data class
  - class는 기본적으로 private 이며 기본적으로 상속을 제한한다.
  - 상속을 하려면 open 키워드를 명시 해야 한다.
  - data class 는 equals 와 hashCode 가 자동으로 정의된 클래스이다.
- object
  - 선언 만으로 싱글턴 객체가 된다.
  - 자바에서 사용하려면 JvmStatic 어노테이션을 명시해야한다.
- companion object
  - 객체의 static 멤버와 같이 쓰인다.
- 컬렉션의 두 유형
  - 기본적으로 코틀린은 불변
  - Immutable
    - listOf -> 불변 리스트
  - mutable
    - mutableListOf() -> 가변 리스트
    - 불변 리스트는 원소는 공유하지만, 리스트 데이터는 공유하지 않는다.
    - 원소를 추가하면 완전히 새로운 리스트를 만들어낸다.
- 패키지
  - 하위 패키지 개념 없음
  - 클래스가 들어있는 패키지와 클래스가 정의된 파일이 들어있는 디렉토리 구조를 꼭 일치시킬 필요 없다.
  - 패키지와 소스 코드를 일치시키면 소스 파일을 찾아내기가 더 쉽다.
- 가시성
  - 클래스와 인스턴스는 기본적으로 공개 가시성
  - 가시성 변경
    - private
    - protected
    - internal
    - public
  - 생성자의 가시성은 기본적으로 public 이고 생략 가능하지만, 가시성 수준을 바꾸려면 constructor 를 생략하면 안된다.
    - class Person private constructor(val name: String)
- 함수
    - 함수 선언하기
        - fun 키워드를 사용해서 함수를 선언
        - 함수 본문을 한 줄로 표현 하는 형태를 식 구문(expression syntax) 이라고 부른다
            - Fun add(a: Int, b: Int): Int = a + b
    - 로컬 함수 사용하기
        - 함수 내부에서 함수를 정의 할 수 있다.
    - 함수 오버라이드
    - 확장 함수 사용하기
        - 확장 함수(extension function)는 마치 클래스에 정의된 인스턴스 함수인 것처럼 객체를 호출 할 수 있는 함수를 말한다.
            - Ex) 리스트의 길이를 돌려주는 length 라는 확장 함수를 생성한다.
                - fun <T> List<T>.length() = this.size
    - 람다 사용하기
        - 람다는 익명함수(anonymous function)다. 익명함수라는 말은 함수를 가리키는 이름이 없이 구현만 있다는 뜻이다.
        - 코틀린에서는 중괄호 사이에 람다가 위치한다.
            - fun triple(list: List<Int>): List<Int> = list.map({ a -> a * 3 })
        - 함수의 마지막 인자로 넘길 때는 괄호 밖에 람다를 넣어도 된다.
            - fun triple(list: List<Int>): List<Int> = list.map { a-> a * 3 }
            - fun product(list: List<Int>): Int = list.fold(1) { a, b -> a * b }
        - 람다의 파라미터 타입
            - fun List<Int>.product(): Int = this.fold(1) { a: Int, b: Int -> a * b }
                - 타입을 지정하면 타입 오류로 컴파일이 이루어지지 않을때 추론 타입이 다르다 사실을 컴파일러(또는 IDE)가 알려준다.
        - 람다를 위한 간이 구문
            - 코틀린은 파라미터가 단 하나뿐인 람다를 편하게 쓸 수 있는 간이 구문을 제공한다.
                - 간이 구문에서는 유일한 파라미터의 이름을 it 으로 부른다.
                - fun triple(list: List<Int>): List<Int> = list.map { it * 3 }
        - 클로저 안의 람다
            - 람다가 자신을 둘러싸는 영역의 변수를 가두어 닫을 수 있다.
            - val multiplier = 3
            - fun multiplyAll(list: List<Int>): List<Int> = list.map { it * multiplier }
            - 클로저를 함수 인자로 바꿀 수 있고 함수 인자로 바꾸고 나면 더 안전해진다.
                - fun multiplyAll(list: List<Int>, multiplier: int): List<Int> = list.map { it * multiplier }
            - 아주 좁은 영역에 있는 변수를 가두는 경우에만 클로저를 사용해야 한다(예를 들면 함수 내부에 정의한 로컬 함수)
  - 널
    - non-nullable 널이 될 수 없는 타입
    - nullable 넣이 될 수 있는 타입

    - 널이될수 있는 타입 다루기
        - 안전한 호출(safe call) - 연산자 ?.
        - NPE 에 대한 모든 책임은 프로그래머가 지게한다. 

    - 엘비스 연산자와 기본 값
        - null이 아닌 기본값을 사용 하고싶을때 사용
        - 엘비스 연산자 ?:

- 프로그램 흐름과 제어구조
    - 제어 구조는 프로그램 흐름을 제어하는 요소

    - 조건 선택 사용하기
        - if else
        - if 블록 안에도 효과를 넣을 수 있지만 가능하면 이런 관습을 피하라
    - 다중 조건 선택 사용하기
        - 자바의 switch 대신 when 구문 사용
        - when 은 가능한 모든 경우를 처리
    - 루프 사용하기
    - 안전하게 프로그래밍 하려면 for 루프에서 범위를 사용하는 대신 추상적인 이터레이션을 제공하는 fold와 같은 함수를 사용해야 한다. 


## 용어 모음

### effect(부수효과)

- 함수나 메시드가 외부 영역의 상태를 바꾸는 것.
- 프로그램이 반환하는 결괏값에 덧붙여 프로그램 밖에서 관찰할 수 있는 어떤 변화.