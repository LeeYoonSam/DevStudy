# Kotlin
- [코틀린 기초](#코틀린-기초)
- [Data class](#data-class)
- [Sealed Class](#sealed-class)
- [Scope functions](#scope-functions)
- [람다로 프로그래밍](#람다로-프로그래밍)
- [코틀린 타입 시스템](#코틀린-타입-시스템)
- [연산자 오버로딩과 기타 관례](#연산자-오버로딩과-기타-관례)
- [고차 함수: 파라미터와 반환 값으로 람다 사용](#고차-함수-파라미터와-반환-값으로-람다-사용)
- [inline, noinline, crossinline, reified](#inline-noinline-crossinline-reified)
- [Kotlin 클로저](#kotlin-클로저)
- [제네릭스](#제네릭스)
- [유용하게 사용하는 Extension](#유용하게-사용하는-extension)
- [코틀린 버전별 비교](#코틀린-버전별-비교)
- [코루틴 Core](#코루틴-core)
- [코루틴과 Async/Await](#코루틴과-asyncawait)
- [코루틴 Dispatchers](#코루틴-dispatchers)
- [Serialization](#serialization)

### 참고
- [Kotlin in Action](http://acornpub.co.kr/book/kotlin-in-action)

---

## 코틀린 기초

### 값 객체(value object)
- 코드가 없이 데이터만 저장하는 클래스

### 코틀린 기본 가시성
- 비공개(private)

### val
- 값을 뜻하는 value 에서 따옴
- 변경 불가능한(immutable) 참조를 저장하는 변수
- 읽기 전용 프로퍼티
- 코틀린은 비공개 필드와 (공개)게터를 만듬

### var
- 변수를 뜻하는 variable 에서 따옴
- 변경 가능한(mutable) 참조
- 쓰기 가능한 프로퍼티
- 코틀린은 비공개 필드와 (공개)게터/세터를 만듬

### 프로퍼티(property)
- 자바에서는 필드와 접근자를 한데 묶어 프로퍼티라고 부름
- 코틀린에서는 기본 제공

### 필드(field)
- 대부분의 프로퍼티에는 그 프로퍼티의 값을 저장하기 위한 필드가 있다.
    - 프로퍼티를 뒷받침하는 필드(backing field)

### 소프트 키워드
- 코틀린에서의 enum

### 스마트 캐스트(smart cast)
- 어떤 변수를 원하는 타입으로 캐스팅 하지 않아도 마치 처음부터 그 변수가 원하는 타입으로 선언된 것처럼 사용할 수 있다.
- 실제로는 컴파일러가 캐스팅을 수행을 해준다.

### 수열(progression)
- 어떤 범위에 속한 값을 일정한 순서로 Iteration 하는 것

### 확장함수(extension function)
- 어떤 클래스의 멤버 메서드인것처럼 호출 할 수 있지만 그 클래스의 밖에 선언된 함수
- 수신 객체 타입(receiver type)
    - 클래스의 이름
- 수신 객체(receiver object)
    - 확장 함수가 호출되는 대상이 되는 값

```kotlin
fun String.lastChar(): Char = this.get(this.length -1)
```
- String: 수신 객체 타입
- this: 수신 객체

### 확장 프로퍼티
- 기존 클래스 객체에 대한 프로퍼티 형식의 구문으로 사용할 수 있는 API 를 추가할 수 있다.

### vararg(가변인자) 키워드
- 호출시 인자 개수가 달라질 수 있는 함수를 정의

### 중위(infix) 함수 호출
- 인자가 하나뿐인 메서드를 간편하게 호출

### 구조 분해 선언(restructuring declaration)
- 복합적인 값을 분해해서 여러 변수에 나눠 담는다.

```kotlin
val (number, name) = 1 to “one"
```
- 구조분해를 사용해서 number, name 변수에 나눠 담음
- to: 중위(infix) 함수

### 스프레드(spread) 연산자
- 이미 배열에 들어있는 원소를 가변 길이 인자로 넘길 때, 배열을 명시적으로 풀어서 배열의 각 원소가 인자로 전달되게 해야 할 때 사용

```kotlin
fun main(args: List<String>) {
    val list = listOf(“test”, *args)
}
```
- * : 스프레드 연산자

### 가시성 변경자(visibility modifier)
- 코드 기반에 있는 선언에 대한 클래스 외부 접근을 제어한다.

### 모듈(module)
- 한꺼번에 컴파일 되는 코틀린 파일들을 의미

### interanl(가시성 변경자)
- internal 은 모듈 내부에서만 볼 수 있음

### 주(primary) 생성자
- 클래스 이름 뒤에 오는 괄호로 둘러 쌓인 코드
- 클래스를 초기화 할 때 주로 사용하는 간략한 생성자로, 클래스 본문 밖에서 정의

### 부(secondary) 생성자
- 클래스 본문 안에서 정의

### object
- 싱글턴 객체 선언
- 클래스를 정의함과 동시에 객체를 생성

### 프로퍼티 접근자
- 게터/세터

---

## Sealed Class
> Sealed Class 및 인터페이스는 상속에 대한 더 많은 제어를 제공하는 제한된 클래스 계층을 나타냅니다.<br>
sealed class 의 모든 하위 클래스는 컴파일 시간에 알려지므로 sealed class 가 있는 모듈이 컴파일된 후에는 다른 하위 클래스가 나타날 수 없습니다.
<br>
어떤 의미에서 sealed class는 enum class와 유사합니다. enum type 에 대한 값 집합도 제한되지만 각 enum 상수는 단일 인스턴스로만 존재하는 반면 sealed class의 하위 클래스에는 각각 고유한 속성이 있는 여러 인스턴스가 있을 수 있습니다.

```kotlin
sealed interface Error

sealed class IOError(): Error

class FileReadError(val f: File): IOError()
class DatabaseError(val source: DataSource): IOError()

object RuntimeError : Error
```

### 장점
- 서브 클래스에 대해서 여러가지 다양한 인스턴스 생성 가능
- when 표현식을 사용해서 else 구문 제거 가능
- enum과 가장 큰 차이는 서로 다른 생성자를 갖는 점이다.

### 단점
- 최초 지정한 타입 클래스에 의존적인 처리가 필요하지만 제네릭을 통해 일반화 하면 공통적으로 여러곳에서 재사용 가능

---

## Data class
> 데이터를 보관하는 것이 주 목적인 클래스를 만드는 것은 드문 일이 아닙니다. 이러한 클래스에서 일부 표준 기능과 일부 유틸리티 기능은 종종 데이터에서 기계적으로 파생됩니다. Kotlin에서는 이를 data class라고 하며 데이터로 표시됩니다.

```kotlin
data class User(val name: String, val age: Int)
```
<br>

### 컴파일러는 기본 생성자에 선언된 모든 속성에서 다음 멤버를 자동으로 파생합니다.
- equals()/hashCode() 쌍 
- "User(name=John, age=42)" 형식의 toString() 
- componentN() 함수는 선언 순서대로 속성에 해당합니다. 
- copy() 함수

### 생성된 코드의 일관성과 의미 있는 동작을 보장하기 위해 데이터 클래스는 다음 요구 사항을 충족해야 합니다.
- 기본 생성자에는 최소한 하나의 매개변수가 있어야 합니다.
- 모든 기본 생성자 매개변수는 val 또는 var로 표시해야 합니다.
- 데이터 클래스는 abstract, open, sealed, inner 가 될 수 없습니다.

### 클래스 본문에 선언된 속성
- 컴파일러는 자동으로 생성된 함수에 대해 기본 생성자 내부에 정의된 속성만 사용합니다. 
- 생성된 구현에서 속성을 제외하려면 클래스 본문 내에서 속성을 선언합니다.

```kotlin
data class Person(val name: String) {
    var age: Int = 0
}
```
- toString(), equals(), hashCode() 및 copy() 구현 내에서는 속성 이름만 사용되며 구성 요소 함수 component1()은 하나만 있습니다. 두 개의 Person 개체는 다른 연령을 가질 수 있지만 동일한 것으로 처리됩니다.

```kotlin
data class Person(val name: String) {
    var age: Int = 0
}
fun main() {
    val person1 = Person("John")
    val person2 = Person("John")
    person1.age = 10
    person2.age = 20
    println("person1 == person2: ${person1 == person2}")
    println("person1 with age ${person1.age}: ${person1}")
    println("person2 with age ${person2.age}: ${person2}")
}

>>>
person1 == person2: true
person1 with age 10: Person(name=John)
person2 with age 20: Person(name=John)
```

---

## Scope functions

> Kotlin 표준 라이브러리에는 객체 컨텍스트 내에서 코드 블록을 실행하는 것이 유일한 목적인 여러 함수가 포함되어 있습니다.<br>
제공된 람다 식이 있는 개체에서 이러한 함수를 호출하면 임시 범위가 형성됩니다. 이 범위에서는 이름 없이 개체에 액세스할 수 있습니다. <br>
이러한 기능을 Scope functions이라고 합니다.<br>
`let`, `run`, `with`, `apply`, `also` 5가지 scope function 이 존재한다.

### let
> 컨텍스트 개체는 인수(it)로 사용할 수 있습니다. 반환 값은 람다 결과입니다.


```kotlin
val numbers = mutableListOf("one", "two", "three", "four", "five")
numbers.map { it.length }.filter { it > 3 }.let { 
    println(it)
    // and more function calls if needed
} 
```

```kotlin
val str: String? = "Hello"   
//processNonNullString(str)       // compilation error: str can be null
val length = str?.let { 
    println("let() called on $it")        
    processNonNullString(it)      // OK: 'it' is not null inside '?.let { }'
    it.length
}
```
- let은 null이 아닌 값으로만 ​​코드 블록을 실행하는 데 자주 사용됩니다. null이 아닌 개체에 대해 작업을 수행하려면 안전 호출 연산자 ?를 사용합니다. 람다에 있는 작업으로 let을 호출합니다.

```kotlin
val numbers = listOf("one", "two", "three", "four")
val modifiedFirstItem = numbers.first().let { firstItem ->
    println("The first item of the list is '$firstItem'")
    if (firstItem.length >= 5) firstItem else "!" + firstItem + "!"
}.uppercase()
println("First item after modifications: '$modifiedFirstItem'")
```
- let을 사용하는 또 다른 경우는 코드 가독성을 향상시키기 위해 제한된 범위의 지역 변수를 도입하는 것입니다. 컨텍스트 개체에 대한 새 변수를 정의하려면 기본값 대신 사용할 수 있도록 해당 이름을 람다 인수로 제공합니다.

### with
> 비확장 함수: 컨텍스트 개체는 인수로 전달되지만 람다 내부에서는 수신기(this)로 사용할 수 있습니다. 반환 값은 람다 결과입니다.
<br>

람다 결과를 제공하지 않고 컨텍스트 개체에서 함수를 호출하는 경우 with를 사용하는 것이 좋습니다. 코드에서 with는 "이 개체를 사용하여 다음을 수행합니다."로 읽을 수 있습니다.
```kotlin
val numbers = mutableListOf("one", "two", "three")
with(numbers) {
    println("'with' is called with argument $this")
    println("It contains $size elements")
}
```
<br>

with의 또 다른 사용 사례는 속성이나 기능이 값을 계산하는 데 사용되는 도우미 개체를 도입하는 것입니다.
```kotlin
val numbers = mutableListOf("one", "two", "three")
val firstAndLast = with(numbers) {
    "The first element is ${first()}," +
    " the last element is ${last()}"
}
println(firstAndLast)
```

### run
> 컨텍스트 개체는 수신기(this)로 사용할 수 있습니다. 반환 값은 람다 결과입니다.
<br>

- run은 with와 동일하지만 컨텍스트 개체의 확장 기능으로 let-을 호출합니다.
- run 은 람다에 개체 초기화와 반환 값 계산이 모두 포함되어 있을 때 유용합니다.

```kotlin
val service = MultiportService("https://example.kotlinlang.org", 80)

val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}

// the same code written with let() function:
val letResult = service.let {
    it.port = 8080
    it.query(it.prepareRequest() + " to port ${it.port}")
}
```

- 수신기 개체에서 실행을 호출하는 것 외에도 확장 기능이 아닌 함수로 사용할 수 있습니다. 비확장 실행을 사용하면 표현식이 필요한 여러 명령문의 블록을 실행할 수 있습니다.

```kotlin
val hexNumberRegex = run {
    val digits = "0-9"
    val hexDigits = "A-Fa-f"
    val sign = "+-"

    Regex("[$sign]?[$digits$hexDigits]+")
}

for (match in hexNumberRegex.findAll("+1234 -FFFF not-a-number")) {
    println(match.value)
}
```

### apply
> 컨텍스트 개체는 수신기(this)로 사용할 수 있습니다. 반환 값은 개체 자체입니다.

- 값을 반환하지 않고 주로 수신자 개체의 구성원에 대해 작동하는 코드 블록에 적용을 사용합니다. 적용의 일반적인 경우는 개체 구성입니다. 이러한 호출은 "다음 할당을 개체에 적용"으로 읽을 수 있습니다.

```kotlin
val adam = Person("Adam").apply {
    age = 32
    city = "London"        
}
println(adam)
```

### also
> 컨텍스트 개체는 인수(it)로 사용할 수 있습니다. 반환 값은 개체 자체입니다.

- 컨텍스트 개체를 인수로 사용하는 일부 작업을 수행하는 데도 좋습니다. 속성 및 기능보다 개체에 대한 참조가 필요한 작업이나 외부 범위에서 이 참조를 숨기고 싶지 않은 경우에도 사용합니다.
- 코드에서도 볼 수 있는 경우 "그리고 객체에 대해 다음을 수행합니다."로 읽을 수도 있습니다.

```kotlin
val numbers = mutableListOf("one", "two", "three")
numbers
    .also { println("The list elements before adding new one: $it") }
    .add("four")
```

## 람다로 프로그래밍

### 람다가 포획(capture)한 변수
- 람다안에서 사용하는 외부 변수

### 클로저(Closure)
- 람다를 실행 시점에 표현하는 데이터 구조는 람다에서 시작하는 모든 참조가 포함된 닫힌(closed) 객체 그래프를 람다 코드와 함께 저장하는 데이터 구조

### 멤버참조(member reference)
- :: 를 사용하는 식

### 에타변환(eta conversion, eta 는 그리스 알파벳 n)
- 함수 f 와 람다 { x -> f(x) } 를 서로 바꿔쓰는 것을 뜻한다.

### 고차함수(HOF, High Order Function)
- 람다나 다른 함수를 인자로 받거나 함수를 반환하는 함수

### 컴비네이터 패턴(combinator pattern)
- 고차함수와 단순한 함수를 이리저리 조합해서 모드를 작성하는 기법

### 컴비네이터(combinator)
- 컴비네이터 패턴에서 복잡한 연산을 만들기 위해 값이나 함수를 조합할때 사용하는 고차함수

### filter 함수
- 컬렉션을 이터레이션 하면서 주어진 람다에 각 원소를 넘겨서 람다가 true 를 변환하는 원소만 모은다.

### predicate(술어)
- 참/거짓을 반환하는 함수

### map 함수
- 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션으로 만든다.

### all, any
- 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산

### groupBy
- 리스트를 여러 그룹으로 이루어진 맵으로 변경

### flatMap, flatten
> 중첩된 컬렉션 안의 원소 처리

- flatMap
    - 중첩된 리스트의 원소를 한 리스트로 모아야 할 때 사용
    - 먼저 인자가 주어진 람다를 컬렉션의 모든 객체에 적용하고(또는 매핑하기) 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스로 한데 모은다.(또는 펼치기 - flatten)
- flatten
    - 특별히 반환해야 할 내용이 없다면 리스트의 리스트를 평평하게 펼치기만 한다.

### 지연계산(lazy) 컬렉션 연산
- 시퀀스(sequence)를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄 할 수 있다.
- 시퀀스의 원소는 필요할 때 비로소 계산된다.

```kotlin
people.asSequence.map(Person::name).filter{it.startWith(“A"}
```

### 중간(intermediate) 연산
- 다른 시퀀스를 반환

### 최종(terminal) 연산
- 결과를 반환

```kotlin
sequence.map{…}.filter{…}.toList()
```
- 중간 계산
    - map {...}
    - filter {...}
- 최종 계산
    - toList()

### 즉시계산(컬렉션 사용)과 지연계산(시퀀스 사용)
- 즉시 계산은 전체 컬렉션에 연산을 적용하지만 지연 계산은 원소를 한번에 하나씩 처리

### 함수형 인터페이스(functional Interface) / SAM 인터페이스(단일 추상 메소드, single abstract method)
- 추상 메소드가 단 하나만 있는 인터페이스

### 수신객체 지정 람다(lambda with receiver)
- 수신 객체를 명시하지 않고 람다의 본문안에서 다른 객체의 메소드를 호출 할 수 있게 하는 것

### with 함수
- 어떤 객체의 이름을 반복하지 않고도 그 객체에 대해 다양한 연산을 수행
- 첫번째 인자로 받은 객체를 두번째 인자로 받은 람다의 수신객체로 만든다.
- 실제로 파라미터가 2개며, 첫번째는 전달된 객체, 두번째는 람다 이다.
- with가 반환하는 값은 람다 코드를 실행할 결과며, 그 결과는 람다식의 본문에 있는 마지막 식의 값

### apply 함수
- `with` 와 동일하며, 유일한 차이는 apply는 항상 자신에게 전달된 객체(즉 수신객체)를 반환한다.
- 객체의 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화 해야하는 경우에 유용하다

---

## 코틀린 타입 시스템

### 코드의 가독성을 향상시키는데 도움이 되는 특성
- 널이 될수 있는 타입(nullable type)
- 읽기전용 컬렉션

### 코틀린의 원시 타입
- Int
- Boolean
- Any 등

### 원시 타입 (Primitive type) 
- int 등의 변수에는 그 값이 직접 들어가지만, 참조 타입(reference type) String 등의 변수에는 메모리상의 객체 위치가 들어간다.

### 자바 원시 타입 종류
- 정수 타입 : Byte, Short, Int, Long
- 부동소수점 수 타입 : Float, Double
- 문자 타입 : Char
- 불리언 타입 : Boolean

### Any, Unit, Nothing 타입
- Any: 모든 널이 될수없는 타입의 조상 타입
	- 최상위 타입 
	- Int 등의 원시타입을 포함한 모든 타입의 조상 타입
	- 코틀린 함수가 Any를 사용하면 자바 바이트코드의 Object 로 컴파일
- Unit 타입: 코틀린의 void
	- 관심을 가질만한 내용을 전혀 반환하지 않는 함수의 반환타입으로 Unit 을 쓸 수 있다.
	- 자바의 void 와 차이점
		- Unit은 모든 기능을 갖는 일반적인 타입
		- void와 달리 Unit을 타입 인자로 쓸 수 있다.
		- Unit 타입의 함수는 Unit 값을 묵시적으로 반환
		- 제네릭 파라미터를 반환하는 함수를 오버라이드 하면서 반환 타입을 Unit 을 쓸 때 유용

```kotlin
interface Processor<T> {
    fun process(): T
}

class NoResultProcessor: Processor<Unit> {
    override fun process() {
        // 업무처리 (return을 명시할 필요 없음)
    }
}
```

### Nothing 타입: 이 함수는 결코 정상적으로 끝나지 않는다.
```kotlin
fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}

>>> fail(“Error occurred”)
java.lang.IllegalStateException: Error occurred
```
- Nothing 타입은 아무 값도 포함하지 않는다.

### 방어적 복사 (defensive copy)
- 어떤 컴포넌트의 내부 상태에 컬렉션이 포함된다면 그 컬렉션을 MutableCollection을 인자로 받는 함수에 전달할 때는 어쩌면 원본의 변경을 막기 위해 컬렉션을 복사해야 할 수 도 있다. 이런 패턴을 방어적 복사 라고 부른다.

---

## 연산자 오버로딩과 기타 관례

### 관례 (Convention)
- 어떤 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법을 코틀린에서는 관례라고 부른다.
- 어떤 클래스 안에 plus 라는 이름의 특별한 메소드를 정의하면 그 클래스의 인스턴스에 대해 + 연산자를 사용 할 수 있다.

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

>>> val p1 = Point(10, 20)
>>> val p2 = Point(30, 40)
>>> println(p1 + p2)
Point(x=40, y=60)
```
- plus 함수 앞에 operator 키워드를 붙여야 한다.

### 오버로딩 가능한 이항 산술 연산자

| 식 | 함수이름 |
| --- | --- |
| a * b | times |
| a / b | div |
| a % b | mod(1.1부터 rem) |
| a + b | plus |
| a - b | minus |

### 복합 대입(compound assignment) 연산자
- plus 와 같은 연산자를 오버로딩하면 코틀린은 + 연산자뿐 아니라 그와 관련 있는 연산자인 += 도 자동으로 함께 지원한다.
- +=, -= 등의 연산자는 복합대입 연산자라고 불린다.

### 오버로딩 할 수 있는 단항 산술 연산자

| 식 | 함수이름 |
| --- | --- |
| +a | times |
| -a | div |
| !a | mod(1.1부터 rem) |
| ++a,a++ | plus |
| --a, a-- | minus |

### 구조 분해 선언(destructuring declaration)
- 구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다.
```kotlin
>>> val p = Point(10, 20)
>>> val (x, y) = p
>>> println(x)
10
>>> println(y)
20

// 컴파일
val (x, y) -> 
val x = p.component1()
val y = p.component2()
```
- 구조 분해 선언의 각 변수를 초기화하기 위해 componentN 이라는 함수를 호출

### 위임 프로퍼티(delegated property)
- 코틀린이 제공하는 관례에 의존하는 특성 중에 독특하면서 강력한 기능인 위임 프로퍼티
- 위임 프로퍼티를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 손쉽게 구현 가능
- 프로퍼티는 위임을 사용해 자신의 값을 필드가 아니라 데이터베이스 테이블이나 브라우저, 세션, 맵 등에 저장 할 수 있다.
```kotlin
class Foo {
    var p: Type by Delegate()
}
```
- p 프로퍼티는 접근자 로직을 다른 객체에게 위임

```kotlin
class Foo {
    private val delegate = Delegate() //  컴파일러가 생성한 도우미 프로퍼티
    var p: Type // p 프로퍼티를 위해 컴파일러가 생성한 접근자는 “delegate”의 getValue와 getValue 메소드를 호출
    set(value: Type) = delegate.setValue(…, value)
    get() = delegate.getValue(…)
}
```
- 프로퍼티 위임 관례를 따르는 Delegate 클래스는 getValue 와 setValue 메소드를 제공해야 한다.
- 관례를 사용하는 다른 경우와 마찬가지로 getValue와 setValue는 멤버 메소드이거나 확장 함수일 수 있다.

### 지연 초기화(lazy initialization)
- 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 흔히 쓰이는 패턴
- 초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 꼭 초기화하지 않아도 되는 프로퍼티에 대해 지연 초기화 패턴을 사용 할 수 있다.
```kotlin
class Email { /*…*/ }
fun loadEmails(person: Person): List<Email> {
    println(“${person.name}의 이메일을 가져옴”)
    return listOf(/*…*/)
}

// 지연 초기화를 뒷받침하는 프로퍼티를 통해 구현하기
class Person(val name: String) {
    private var _emails: List<Email>? = null
    val emails: List<Email>
        get() {
            if(_emails == null) {
                _emails = loadEmails(this)
            }
            return _emails!!
        }
}

>>> val p = Person(“Alice”)
>>> p.emails
Load emails for Alice
>>> p.emails
```
- 여기서는 뒷받침하는 프로퍼티(backing property)라는 기법을 사용한다.

### 뒷받침하는 프로퍼티(backing property)
- _emails 라는 프로퍼티는 값을 저장하고, 다른 프로퍼티인 emails 는 _emails 라는 프로퍼티에 대한 읽기 연산을 제공
- _emails 는 널이 될 수 있는 타입인 반면 emails는 널이 될수 없는 타입이므로 프로퍼티를 두 개 사용해야 한다.

```kotlin
// 지연 초기화를 위임 프로퍼티를 통해 구현하기
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```
- lazy 함수는 코틀린 관례에 맞는 시그니처의 getValue 메소드가 들어있는 객체를 반환한다.
- lazy 를 by 키워드와 함께 사용해 위임 프로퍼티를 만들 수 있다.
- lazy 함수는 기본적으로 스레드 안전한다.

### 확장 가능한 객체(expando object)
- 자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용하는 경우가 자주 있다. 그런 객체를 확장 가능한 객체 라고 부르기도 한다.
- 예를 들어 연락처 관리 시스템에서 연락처별로 임의의 정보를 저장할 수 있게 허용하는 경우

---

## 고차 함수: 파라미터와 반환 값으로 람다 사용

### 고차함수(high order function)
- 람다를 인자로 받거나 반환하는 함수
- 고차함수로 코드를 더 간결하게 다듬고 코드 중복을 없애고 더 나은 추상화를 구축

### 인라인(inline) 함수
- 람다를 사용함에 따라 발생할 수 있는 성능상 부가 비용을 없애고 람다 안에서 더 유연하게 흐름을 제어할 수 있는 코틀린 특성

### 코틀린 use 
- 자바 try-with-resource 와 같은 기능을 제공
- 닫을 수 있는 자원에 대한 확장 함수며, 람다를 인자로 받는다.
- use는 람다를 호출한 다음에 자원을 닫아준다.

---

## [inline, noinline, crossinline, reified](https://leveloper.tistory.com/171)

### inline
> 고차 함수를 사용하면 런타임 패널티가 있기 때문에 함수 구현 자체를 코드에 넣음으로써 오버헤드를 줄일 수 있다.

```kotlin
fun doSomething(action: () -> Unit) { 
    action() 
} 

fun callFunc() { 
    doSomething { 
        println("문자열 출력!") 
    } 
}
```
- 일반적인 고차함수가 있고 자바로 변환하면 아래 코드로 변환된다.

```java
public void doSomething(Function action) { 
    action.invoke(); 
} 

public void callFunc() { 
    doSomething(System.out.println("문자열 출력!"); 
}
```
- 그리고 이 자바 코드는 아래 코드로 변환된다.

```java
public void callFunc() { 
    doSomething(new Function() { 
        @Override 
        public void invoke() { 
            System.out.println("문자열 출력!"); 
        } 
    } 
}
```
- 여기서 내부적으로 객체 생성과 함께 함수 호출을 하게 되어 있어서, 이런 부분에서 오버헤드가 생길 수 있다. 
- inline 키워드는 이런 오버헤드를 없애기 위해 사용한다.

```kotlin
inline fun doSomething(action: () -> Unit) { 
    action() 
} 

fun callFunc() { 
    doSomething { 
        println("문자열 출력!") 
    } 
}
```
- 위의 코드를 자바로 변환하면 아래 코드로 변환된다.

```java
public void callFunc() { 
    System.out.println("문자열 출력!"); 
}
```
- 위와 같이 Function 인스턴스를 만들지 않고, callFunc() 내부에 삽입되어 바로 선언된다. 
- 위의 코드가 컴파일될 때 컴파일러는 함수 내부의 코드를 호출하는 위치에 복사한다. 
- 컴파일되는 바이트코드의 양은 많아지겠지만, 함수 호출을 하거나 추가적인 객체 생성은 없다.
- 이와 같은 이유로 inline 함수는 일반 함수보다 성능이 좋다. 
- 하지만 inline 함수는 내부적으로 코드를 복사하기 때문에, 인자로 전달받은 함수는 다른 함수로 전달되거나 참조할 수 없다.

```kotlin
inline fun doSomething(action1: () -> Unit, action2: () -> Unit) {
    action1()
    anotherFunc(action2) // error
}

fun anotherFunc(action: () -> Unit) {
    action()
}

fun main() {
    doSomething({
        println("1")
    }, {
        println("2")
    })
}
```
- 위의 코드에서 doSomething()에 두 번째 인자로 넘겨받은 action2를 또 다른 고차 함수인 anotherFunc()에 인자로 넘겨주려 하고 있다. 
- 이때 doSomething()은 inline 함수로 선언되어 있기 때문에 인자로 전달받은 action2를 참조할 수 없기 때문에 전달하는 것이 불가능하다. 

### noinline
> 모든 인자를 inline으로 처리하고 싶지 않을 때 사용하는 것이 noinline 키워드다.<br>
인자 앞에 noinline 키워드를 붙이면 해당 인자는 inline에서 제외된다.<br>
따라서 noinline 키워드가 붙은 인자는 다른 함수의 인자로 전달하는 것이 가능하다.

```
inline fun doSomething(action1: () -> Unit, noinline action2: () -> Unit) { 
    action1() 
    anotherFunc(action2) 
} 

fun anotherFunc(action: () -> Unit) { 
    action() 
} 

fun main() { 
    doSomething({ 
        println("1") 
    }, { 
        println("2") 
    }) 
}
```

### crossinline
> 일부 인라인 함수는 파라미터로 전달받은 람다를 호출할 때 함수 몸체에서 직접 호출하지 않고 다른 실행 컨텍스트를 통해(예, 로컬 객체나 중첩 함수,스레드) 호출해야 할 때 람다 안에서 비-로컬 흐름을 제어할 수 없다.<br>
이럴 때 사용하는 것이 crossinline 키워드다.

```kotlin
inline fun View.click(block: (View) -> Unit) {
    setOnClickListener { view ->
        block(view) // error
    }
}
```
- View의 클릭 이벤트를 보다 쉽게 연결해주기 위한 확장 함수다.

```kotlin
inline fun View.click(crossinline block: (View) -> Unit) { 
    setOnClickListener { view -> 
        block(view) 
    } 
}
```
- 람다 함수에 crossinline 키워드를 사용해서 함수 안에서 비-로컬 흐름을 제어

### reified
> inline 함수에서 특정 타입을 가졌는지 판단할 수 없기때문에 타입을 한정하기 위해 사용
<br>

- 타입 파라미터에 reified 키워드를 붙여주면 마치 클래스처럼 타입 파라미터에 접근할 수 있다. 
- 인라인 함수이므로 리플렉션이 필요 없고 is나 as와 같은 일반 연산자가 동작합니다. 
- 참고로 인라인이 아닌 일반 함수에는 reified를 사용할 수 없습니다.

<br>

위의 예시에서 보인 확장 함수를 제네릭을 사용해서 좀 더 확장해보자.

```kotlin
inline fun <T: View> T.click(crossinline block: (T) -> Unit) { 
    setOnClickListener { view -> 
        block(view as T) 
    } 
}
```
- 위의 코드는 제네릭을 사용해서 block의 인자로 View가 아닌 T를 넣어준다. 예를 들어, TextView.click으로 사용한다고 하면 인자로 TextView를 받기 위함이다. 위의 코드에서는 오류는 아니지만 경고 메시지가 뜬다.
`Unchecked cast: View! as T`
-  view를 T로 캐스팅하려고 할 때 발생하는 경고 메시지이다. 이는 inline 함수에서 특정 타입을 가졌는지 판단할 수 없기 때문이다. 이럴 때 reified 키워드를 사용한다.

```kotlin
inline fun <reified T: View> T.click(crossinline block: (T) -> Unit) { 
    setOnClickListener { view -> 
        block(view as T) 
    } 
}
```
- 타입 파라미터에 reified 키워드를 붙여주면 마치 클래스처럼 타입 파라미터에 접근할 수 있다.

---

## [Kotlin 클로저](https://blog.naver.com/lys1900/222085445500)
> 코틀린 함수는 종종 함수 밖에 있는 요소에 접근한다. 그 요소는 패키지 수준의 프로퍼티이거나 클래스나 객체의 프로퍼티일 수 있다.<br>
심지어 함수가 다른 클래스의 동반 객체나 다른 패키지의 멤버에 접근할 수도 있다.<br>
순수 함수가 참조 투명성을 지키는 함수, 이는 함수를 반환하는 것 외에 외부에서 관찰 가능한 효과가 없어야 한다는 뜻이다.<br>
하지만 반환 값이 자신의 인자뿐 아니라 자신을 둘러싸고 있는 영역에 있는 요소에 의해서도 결정된다면? 이런 경우 자신을 둘러싸고 있는 영역의 요소를 함수가 사용하는 암시적 파라미터로 간주할 수 있다.<br>
자바 람다와 달리 코틀린 람다는 자신을 둘러싸고 있는 영역에 있는 가변 변수에 자유롭게 접근할 수 있다.<br>

```kotlin
fun main() {
    println("클로저 사용: ${addTax(12.0)}")
    
    println("클로저 대신 튜플을 인자로 받는 함수를 사용: ${addTax(taxRate, 12.0)}")
    
    println("함수 값에 적용: ${addTaxFunctionValue(taxRate, 12.0)}")
    
    println("커리한 버전: ${addTaxCurried(taxRate)(12.0)}")
}
```

```kotlin
val taxRate = 0.09
fun addTax(price: Double) = price + price * taxRate

```
- 이 코드에서 addTax 함수는 taxRate 라는 변수에 대해 닫혀 있다.
- 여기서 addTax 가 price의 함수가 아니라는 점이 중요하다. 이유는 인자가 같아도 결과가 달라질 수 있기 때문이다.
- 하지만 이를 (price, taxRate)라는 튜플에 대한 함수로 볼 수 있다.

```kotlin
// 클로저 대신 튜플을 인자로 받는 함수 사용
fun addTax(taxRate: Double, price: Double) = price + price * taxRate

// 동일한 방법을 함수 값에도 적용
val addTaxFunctionValue = { taxRate: Double, price: Double -> price + price * taxRate }

// addTax를 함수 값으로 사용하는 커리한 버전
val addTaxCurried = 
    { taxRate: Double -> 
        { price: Double -> 
            price + price * taxRate
        }
    }
```
### 참고
- [테스트 코드](https://pl.kotl.in/vNESIM-zS)

---

## 제네릭스

### 실체화한 타입 파라미터(reified type parameter)
- 실체화한 타입 파라미터를 사용하면 인라인 함수 호출에서 타입 인자로 쓰인 구체적인 타입을 실행 시점에 알 수 있다.

### 선언 지점 변성(declaration-site variance)
- 선언 지점 변성을 사용하면 기저 타입은 같지만 타입 인자가 다른 두 제네릭 타입 `Type<A>`와 `Type<B>`가 있을 때 타입 인자 A와 B의 상위/하위 타입 관계에 따라 두 제네릭 타입의 상위/하위 타입 관계가 어떻게 되는지 지정할 수 있다.
- 예를 들어 `List<Any>` 를 인자로 받는 함수에게 `List<Int>` 타입의 값을 전달할 수 있을지 여부를 선언 지점 변성을 통해 지정할 수 있다.

### 사용 지점 변성(use-site variance)
- 같은 목표(제네릭 타입 값 사이의 상위/하위 타입 관계 지정)를 제네릭 타입 값을 사용하는 위치에서 파라미터 타입에 대한 제약을 표시하는 방식으로 달성

### 제네릭 타입 파라미터
- 타입 파라미터 (type parameter)

```kotlin
Map<K, V>
List<Any>
List<String>
```
- 타입 인자 (type argument)

```kotlin
val readers: MutableList<String> = mutableListOf()
val readers = mutableListOf<String>
```

### 타입 파라미터 제약(type parameter constraint)
- 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능
- 예를 들어 리스트에 속한 모든 원소의 합을 구하는 sum 함수를 생각해보면 List<Int> 나 List<Double>에 그 함수를 적용할 수 있지만 List<String>등에는 그 함수를 적용할 수 없다. sum 함수가 타입 파라미터로 숫자 타입만을 허용하게 정의하면 이런조건을 표현할 수 있다.
- 어떤 타입을 제네릭 타입의 타입 파라미터에 대한 상한(upper bound)으로 지정하면 그 제네릭 타입을 인스턴스화할 때 사용하는 타입 인자는 반드시 그 상한 타입이거나 그 상한 타입의 하위 타입이어야 한다.

```kotlin
fun <T: Number> List<T>.sum(): T
```
- 타입 파라미터 뒤에 상한을 지정함으로써 제약을 정의할 수 있다.

---

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

## 코틀린 버전별 비교
### 1.1
1. 타입 별명
    - 기존 타입에 대한 별명(alias)을 만드는 기능으로 typealias 라는 키워드가 생김
    - 새로운 타입을 정의하지는 않지만 기존 타입을 다른 이름으로 부르거나 더 짧은 이름으로 부를 수 있다.

    ```kotlin
    // 콜백 함수 타입에 대한 타입 별명
    typealias MyHandler = (Int, String, Any) -> Unit

    // MyHandler 를 받는 고차 함수
    fun addHandler(h:MyHandler) { ... }
    ```

2. 봉인 클래스와 데이터 클래스 
    - 봉인 클래스의 하위 클래스를 봉인 클래스 내부에 정의할 필요가 없고 같은 소스 파일 안에만 정의
    - 데이터 클래스로 봉인 클래스를 확장하는 것도 가능
    - 데이터 클래스로 봉인 클래스를 상속하는 경우 재귀적인 계층 구조의 값을 자연스럽게 표시해주는 toString 함수를 쉽게 얻을 수 있다는 장점이 있다.(보기는 좋지 않음)

    ```kotlin
    sealed class Expr
    data class Num(val value: Int) : Expr()
    data class Sum(val left: Expt, val right: Expr) : Expr()
    ```

3. 바운드 멤버 참조
    - 코틀린 1.0에서는 클래스의 메소드나 프로퍼티에 대한 참조를 얻은 다음에 그 참조를 호출할때 인스턴스 객체를 제공해야 했다.
    - 코틀린 1.1부터는 바운드 멤버참조를 지원
    - 바운드 멤버 참조를 사용하면 멤버 참조를 생성할때 클래스 인스턴스를 함께 저장한 다음 나중에 그 인스턴스에 대해 멤버를 호출해준다.
    - 수신 대상 객체를 별도로 지정해줄 필요가 없다.

    ```kotlin
    val p = Person("albert", 20)
    val ageFunction = p::age << 바운드 멤버 참조
    println(ageFunction())
    ```

    - `ageFunction`은 인자가 하나이지만, `ageFunction` 은 인자가 없는(참조를 만들 때 p가 가리키던 사람의 나이를 반환) 함수라는 점에 유의

4. 람다 파라미터에서 구조 분해 사용
> 구조 분해 선언을 람다의 파라미터 목록에서도 사용할 수 있다.

```kotlin
val nums = listOf(1,2,3)
val names = listOf("One", "Two", "Three")
(nums zip names).forEach { (num, name) -> println("$(num)") }

// 결과
1 == One
2 == Two
3 == Three
```

5. 밑줄(_)로 파라미터 무시
> 람다를 정의하면서 여러 파라미터 중에 사용하지 않는 파라미터가 있다면 _을 그 위치에 넣으면 따로 파라미터 이름을 붙이지 않고 람다를 정의할 수 있다. 마찬가지로 구조분해 시에도 관심이 없는 값을 _로 무시할 수 있다.

```kotlin
data class YMD(val year: Int, val month: Int, val day: Int)

typealias YMDFUN = (YMD) -> Unit

fun aplyYMD(v: YMD, f: YMDFUN) = f(v)

val now = YMD(2017, 10, 9)
val 삼일운동 = YMD(1919, 3, 1)
val (삼일운동이_일어난_해, _, _) = 삼일운동 // 1919
applyYMD(now) { (year, month, _) -> println("year = ${year}, month = $(month)") }

// 결과
year = 2017, month = 10
```

6. 식이 본문인 게터만 있는 읽기 전용 프로퍼티의 타입 상략
> 읽기 전용 프로퍼티를 정의할 때 게터의 분문이 시식이라면 타입을 생략해도 컴파일러가 프로퍼티 타입을 추론해준다.

```kotlin
data class Foo(val value: Int) {
    val double get() = value * 2
}
```

7. 프로퍼티 접근자 인라이닝
> 접근자를 inline 으로 선언할 수 있다. 게터뿐 아니라 세터도 인라이닝이 가능하며, 일반 멤버 프로퍼티뿐 아니라 확장 멤버 프로퍼티나 최상위 프로퍼티도 인라이닝이 가능하다.

- 프로퍼티에 뒷받침하는 필드가 있으면 그 프로퍼티의 게터나 세터를 인라이닝 할 수 없다.

```kotlin
val toplevel: Double
    inline get() = Math.PI // 게터 인라이닝

class InlinePropExample(var value: Int) {
    val setOnly: Int
    get() = value
    inline set(v) { value = v } // 세터 인라이닝

    // 컴파일 오류: Inline property cannot have backing field
    val backing: Int = 10
    inline get() = field * 1000
}

inline var InlinePropExample.square: Int // 게터 세터 인라이닝
    get() = value * value
    set(v) { value = Math.sqrt(v.toDouble()).toInt() }
```

8. 제네릭 타입으로 이넘 값 접근
> 제네릭 파라미터로 쓰이는 이넘의 모든 이념 값을 이터레이션하거나 값을 찾아봐야 하는 경우, 인라인 제네릭 함수 안에서 실체화한(reified) 타입을 활용해 이넘에 제네릭하게 접근할 수 있다.

9. DSL 의 수신 객체 제한

10. 로컬 변수 등을 위임
> 클래스나 객체의 프로퍼티 외에 로컬 변수나 최상위 수준의 변수에도 이험을 사용할 수 있다.

11. 위임 객체 프로바이더
- provideDelegate() 연산자 안에서는 프로퍼티가 위임 객체에 자신을 위임하는 시점에 프로퍼티 이름과 객체에 따라 적절한 프로퍼티 검증을 진행하거나 상황에 맞는 적절한 프로퍼티 위임 객체를 제공하는 등의 작업을 수행 할 수 있다.

12. mod 와 rem
> mod 대신 rem 이 % 연산자로 해석된다. 이유는 기존 자바 BigInteger 구현과 다른 정수형 타입(Int 등의)의 % 연산 결과를 맞추기 위험

13. 표준 라이브러리 변화
    - 문자열-숫자 변환
        - 문자열을 수로 변환할 때 예외를 던지는 대신 Null 을 돌려주는 메소드가 추가되었다.
    - onEach()
    - also(), takeIf(), takeUnless()
        - also 는 apply 와 비슷하지만 람다 안에서 this 가 바뀌지 않기 때문에 수신 객체를 it 으로 활용해야 한다는 점이 다르다.
        - this가 가려지면 불편한 경우 also 가 유용하다.
    - groupingBy()
        - groupingBy는 컬렉션을 키에 따라 분류한다. 이때 Grouping 이라는 타입의 값을 변환한다는 점에 유의
    - Map.toMap() 과 Map.toMutableMap()
        - 맵을 복사할 때 사용한다.
    - minOf(), maxOf()
        - 둘 또는 세 값 중 최소(minOf)나 최대(maxOf)인 값을 구할 때 사용한다. 
    - 람다를 사용한 리스트 초기화
        - Array 생성자처럼 리스트 생성자 중에도 람다를 파라미터로 받는 생성자가 생겼다.
        ```kotlin
        fun initListWithConst(v: Int, size: Int) = MutableList(size) { V }
        val evens = List(10) { 2 * it }
        val thirtyZeros = initListWithConst(0, 30)
        ```

    - Map.getValue()
    - 추상 컬렉션 클래스
    - 배열 연산 추가

### 1.2
1. 애노테이션의 배열 리터럴
    - 1.1 까지는 애노테이션에서 여러 값을 배열로 넘길 때 arrayOf 를 써야 했다.
    - 1.2 에서는 [] 사이에 원소를 넣어서 표시할 수 있다.

2. 지연 초기화(lateinit) 개선
3. 인라인 함수의 디폴트 함수 타입 파라미터 지원
4. 함수/메소드 참조 개선
5. 타입 추론 개선
6. 경고를 오류로 처리
> 커맨드라인 옵션에 -Werror 지정하면 모든 경고를 오류로 처리한다.

```gradle
compileKotlin {
    kotlinOptions.warningsAsErrors = true
}
```

7. 스마트 캐스트 개선
8. 이넘 원소 안의 클래스는 내부 클래스로
9. (기존 코드를 깨는 변경) try 블록의 스마트 캐스트 안전성 향상
10. 사용 금지 예고된 기능
    - 다른 클래스를 상속한 하위 데이터 클래스의 자동 생성 copy로 인한 문제
    - 이넘 원소 안에서 중첩 타입 정의
    - 가변 인자에게 이름 붙은 인자로 원소 하나만 넘기기
    - Throwable 을 확장하는 제네릭 클래스의 내부 클래스
    - 읽기 전용 프로퍼티를 뒷받침하는 필드 덮어쓰기

11. 표준 라이브러리
    - 호환 패키지 분리
    - 컬렉션
    - kotlin.math
    - Regex 클래스를 직렬화 가능하게 바꿈

12. JVM 백엔드 변경
    - 생성자 호출 정규화
    자바 디폴트 메소드 호출
    (기존 코드를 깨는 변경) 플랫폼 코드의 x.equals(null) 동작 일관성

### 1.3
> 코루틴과 코루틴을 활용한 비동기 프로그래밍(async/await)이 1.3부터 정식 버전으로 포함됐다.

1. 코틀린 네이티브 개선
2. 다중 플랫폼 프로젝트
3. 컨트랙트(Contract, 계약) - null 가능한 타입에 대해 스마트 캐스트가 되도록 구현
    - 사용자 정의 컨트랙트
4. When 의 대상을 변수에 포획 - When 의 대상에 변수를 선언해서 사용하고 When 문 바깥의 코드를 깔끔하게 유지 
5. 인터페이스의 동반 객체에 있는 멤버를 @JvmStatic 이나 @JvmField 로 애노테이션
    - 애노테이션 클래스 안에 선언을 중첩시킬 수 있음
6. 파라미터 없는 메인
7. 함수 파라미터 수 제한 완화
8. 프로그래시브 모드
9. 인라인 클래스(실험적 기능)
10. 부호 없는 정수(실험적 기능)
11. @JvmDefault(실험적 기능)
12. 표준 라이브러리 변화
    - Random 다중 플랫폼 지원
    - isNullOrEmpty와 orEmpty 확장을 여러 클래스에 추가
    - 배열 원소 복사 확장 함수 copyInto() 추가
    - 맵 연관 쌍 추가 함수 associateWith()
    - ifEmpty 와 ifBlank 함수
    - 봉인 클래스에 접근할 수 있는 리플렉션 추가
    - 기타 소소한 변경
        - Boolean 에 동반 객체가 생김
        - Any?.hashCode() 확장이 null에 대해 0을 반환
        - Char 도 MIN_VALUE 와 MAX_VALUE 를 제공
        - 기본 타입들의 동반 객체에 SIZE_BYTES 와 SIZE_BITS 를 추가


## 코루틴 Core
> 코루틴과 함께 작동하는 핵심 프리미티브

### 코루틴 빌더 기능
| Name | Result | Scope | Description |
| --- | --- | --- | --- |
| [launch](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/launch.html) | Job | [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html) | 결과가 없는 코루틴을 시작합니다. |
| [async](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/async.html) | Deferred | [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html) | 미래 결과와 함께 단일 값을 반환합니다. |
| [produce](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/produce.html) | ReceiveChannel | https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-producer-scope/index.html | 요소 스트림을 생성합니다. |
| runBlocking | T | [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html) | 코루틴이 실행되는 동안 스레드를 차단합니다. |

<br/>

### CoroutineDispatcher
| Name | Description |
| --- | --- |
| [Dispatchers.Default](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-default.html) | 코루틴 실행을 백그라운드 스레드의 공유 풀로 제한 |
| [Dispatchers.Unconfined](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-unconfined.html) | 어떤 식으로든 코루틴 실행을 제한하지 않습니다. |

<br/>

### 추가 컨텍스트 요소
| Name | Description |
| --- | --- |
| NonCancellable | 항상 활성 상태인 취소할 수 없는 작업 |
| CoroutineExceptionHandler | 잡히지 않은 예외 처리기 |

<br/>

### 코루틴을 위한 동기화 프리미티브
| Name | Suspending functions | Description |
| --- | --- | --- |
| Mutex | lock | 상호 배제 |
| Channel | 	send, receive | 통신 채널(큐 또는 교환기라고도 함) |

<br/>

### 최상위 suspending functions
| Name | Description |
| --- | --- |
| delay | Non-blocking sleep |
| yield | 단일 스레드 디스패처에서 스레드를 생성합니다. |
| withContext | 다른 컨텍스트로 전환 |
| withTimeout | 시간 초과 시 예외로 실행 시간 제한 설정 |
| withTimeoutOrNull | 실행 시간 제한 설정은 시간 초과 시 null이 됩니다. |
| awaitAll | 주어진 모든 작업의 ​​성공적인 완료 또는 모든 작업의 ​​예외적인 완료를 기다립니다. |
| joinAll | 주어진 모든 작업에 대한 조인 |

<br/>

### Select 표현식은 여러 일시 중단 함수의 결과를 동시에 기다립니다.
| Receiver | Suspending function | Select clause | Non-suspending version |
| --- | --- | --- | --- |
| Job | join | onJoin | isCompleted |
| Deferred | await | onAwait | isCompleted |
| SendChannel | send | onSend | trySend |
| ReceiveChannel | receive | onReceive | tryReceive |
| ReceiveChannel | 	kotlinx.coroutines.channels.receiveCatching | kotlinx.coroutines.channels.onReceiveCatching | tryReceive |
| none | delay | onTimeout | none |

### 참고
- [kotlinx-coroutines-core](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/index.html)

## 코루틴과 Async/Await
> 코틀린 1.3부터는 코루틴이 표준 라이브러리에 정식 포함이 되었다.

### 1. 코루틴이란?

**위키피디아 코루틴 정의**

```
코루틴은 컴퓨터 프로그램 구성 요소 중 하나로 비선점형 멀티태스킹(non-preemptive multitasking)을 수행하는 일반화한 서브루틴(subroutine) 이다.
코루틴은 실행을 일시 중단(suspend)하고 재개(resume) 할 수 있는 여러 진입 지점(entry point)을 허용한다.
```
- 서브루틴은 여러 명령어를 모아 이름을 부여해서 반복 호출할 수 있게 정의한 프로그램 구성 요소로, 다른 말로 함수라고 부르기도 한다.
- 객체지향 언어에서는 메소드도 서브루틴이라 할 수 있다.
- 어떤 서브루틴에 진입하는 방법은 오직 한 가지(해당 함수를 호출하면 서브루틴의 맨 처음부터 실행이 시작된다.)뿐이며, 그때마다 활성 레코드(activation record)라는 것이 스택에 할당되면서 서브루틴 내부의 로컬 변수 등이 초기화된다.
- 서브루틴 안에서 여러 번 return 을 사용할 수 있기 때문에 서브루틴이 실행을 중단하고 제어를 호출한쪽(caller)에게 돌려주는 지점은 여럿 있을 수 있다.
- 서브루틴에서 반환되고 나면 활성 레코드가 스택에서 사리지기 때문에 실행 중이던 모든 상태를 잃어버린다. 그래서 서브루틴을 여러 번 반복 실행해도 (전역 변수나 다른 부수 효과가 있지 않는 한) 항상 같은 결과를 반복해서 얻게 된다.
- 멀티태스킹은 여러 작업을(적어도 사용자가 볼 때는) 동시에 수행하는 것처럼 보이거나 실제로 동시에 수행하는 것이다.
- 코루틴이란 서로 협력해서 실행을 주고 받으면서 작동하는 여러 서브루틴을 말한다.
- 코루틴의 대표격인 제네레이터(generator)를 예로 들면 어떤 함수 A가 실행되다가 제네레이터인 코루틴 B를 호출하면 A가 실행을 B에 양보한다(yield 라는 명령을 사용하는 경우가 많다.) A는 다시 코루틴을 호출했던 바로 다음 부분부터 실행을 계속 진행하다가 또 코루틴 B를 호출한다. 이때 B가 일반적인 함수라면 로컬 변수를 초기화하면서 처음부터 실행을 다시 시작하겠지만, 코루틴이면 이전에 yield 로 실행을 양보했던 지점부터 실행을 계속하게 된다.

### 2. 코틀린의 코루틴 지원: 일반적인 코루틴
> 언어에 따라 제네레이터 등 특정 형태의 코루틴만을 지원하는 경우도 있고, 좀 더 일반적인 코루틴을 만들 수 있는 기능을 언어가 기본 제공하고, 제네레이터, async/await 등 다양한 코루틴은 그런 기본 기능을 활용해 직접 사용자가 만들거나 라이브러리를 통해 사용하도록 하는 형태가 있다. 제네레이터만 제공하는 경우에도 yield 시 퓨처 등 비동기 처리가 가능한 객체를 넘기는 방법을 사용하면 async/await 등을 비교적 쉽게 구현할 수 있다.

- 코틀린이 지원하는 기본 기능을 활용해 만든 다양한 형태의 코루틴들은 kotlinx.coroutines 패키지 밑에 있다.

### 2.1 여러 가지 코루틴
> 다음은 kotlinx.coroutines.core 모듈에 들어있는 코루틴이다. 정확히 이야기하자면 각각은 코루틴을 만들어주는 코루틴 빌더라고 부른다. 코틀린에서는 코루틴 빌더에 원하는 동작을 람다로 념겨서 코루틴을 만들어 실행하는 방식으로, 코루틴을 활용한다.

**kotlinx.coroutines.CoroutineScope.launch**
- launch 는 코루틴을 잡(Job) 으로 변환하며, 만들어진 코루틴은 기본적으로 즉시 실행, 원하면 launch 가 반환한 Job 의 cancel() 을 호출해 코루틴 실행을 중단시킬 수 있다.
- launch 가 작동하려면 CoroutineScope 객체가 블록의 this 로 지정돼야 하는데(API 문서나 소스를 보면 launch 가 받는 블록의 타입이 suspend CoroutineScope.() -> Unit 임을 알 수 있다.)
- 다른 suspend 함수 내부라면 해당 함수가 사용 중인 CoroutineScope 가 있겠지만, 그렇지 않은 경우에는 GlobalScope 를 사용하면 된다.
- 메인 함수와 GlobalScope.launch 가 만들어낸 코루틴이 서로 다른 스레드에서 실행된다.
- GlobalScope는 메인 스레드가 실행중인 동안만 코루틴의 동작을 보장해준다.
- 코루틴의 실행이 끝날 때까지 현재 스레드를 블록시키는 함수로 runBlocking 이 있다. runBlocking 은 CoroutineScope 의 확장 함수가 아닌 일반 함수이기 때문에 별도의 코루틴 스코프 객체 없이 사용 가능하다.

```kotli
fun yieldExample() {
    runBlocking {
        launch {
            log("1")
            yield()
            log("3")
            yield()
            log("5")
        }

        log("after first launch")

        launch {
            log("2")
            delay(1000L)
            log("4")
            delay(1000L)
            log("6")
        }

        log("after second launch")
    }
}

Thread[main]: after first launch
Thread[main]: after second launch
Thread[main]: 1
Thread[main]: 2
Thread[main]: 3
Thread[main]: 5
Thread[main]: 4
Thread[main]: 6
```
- launch 는 즉시 반환
- runBlocking 은 내부 코루틴이 모두 끝난 다음에 반환
- delay() 를 사용한 코루틴은 그 시간이 지날 때까지 다른 코루틴에게 실행을 양보한다. 앞 코드에서 delay(1000L) 대신 yield() 를 쓰면 차례대로 1,2,3,4,5,6 이 표시 될것이다. yield() 를 했지만 다른 코루틴이 delay() 상태에 있으면 다시 제어가 돌아온다.

**kotlinx.coroutines.CoroutineScope.async**
- async 는 사실상 launch 와 같은 일을 한다. 유일한 차이는 launch 가 Job을 반환하는 반면 async 는 Deferred 를 반환한다는 점뿐이다. 심지어 Deferred 는 Job 을 상속한 클래스이기 때문에 launch 대신 async를 사용해도 항상 아무 문제가 없다. 
- Deferred 와 Job 의 차이는, Job 은 아무 타입 파라미터가 없는데 Deferred 는 타입 파라미터가 있는 제네릭 타입이라는 점과 Deferred 안에는 await() 함수가 정의돼 있다는 점이다.
- Deferred 의 타입 파라미터는 바로 Deferred 코루틴이 계산을 하고 돌려주는 값의 타입이다. Job 은 Unit 을 돌려주는 Deferred<Unit> 이라고 생각할 수도 있다.
- 따라서 async 는 코드 블록을 비동기로 실행할 수 있고(제공하는 코루틴 컨텍스트에 따라 여러 스레드를 사용하거나 한 스레드 안에서 제어만 왔다 갔다 할 수도 있다), async 가 반환하는 Deferred 의 await 을 사용해서 코루틴이 결과 값을 내놓을 때까지 기다렸다가 결과 값을 얻어낼 수 있다.

```kotlin
fun sumAll() {
    runBlocking {
        val d1 = async { delay(1000L); 1)}
        log("after async(d1)")
        val d2 = async { delay(2000L); 2)}
        log("after async(d2)")
        val d3 = async { delay(3000L); 3)}
        log("after async(d3)")

        log("1+2+3 = ${ d1.await() + d2.await() + d3.await() }")
        log("after await all & add")
    }
}

결과
Thread[main]: after async(d1)
Thread[main]: after async(d2)
Thread[main]: after async(d3)
Thread[main]: 1+2+3 = 6
Thread[main]: await all & add
```
- 잘 살펴보면 d1, d2, d3 을 하나하나 순서대로(병렬 처리에서 이 런 경우를 직렬화해 실행한다고 말한다) 실행하면 총 6초 이상이 걸려야 하지만, 6이라는 결과를 얻을 때까지 총 3초가 걸렸음을 알 수 있다. 또한 async 로 코드를 실행하는 데는 시간이 거의 걸리지 않음을 알 수 있다. 그럼에도 불구하고 스레드 여럿 사용하는 병럴 처리와 달리 모든 async 함수들이 메인 스레드 안에서 실행됨을 볼 수 있다. 이 부분이 async/await 과 스레드를 사용한 병렬 처리의 큰 차이다.
- 특히 이 예제에서는 겨우 3개의 비동기 코드만을 실행했지만, 비동기 코드가 늘어남에 따라 async/await 을 사용한 비동기가 빛을 발한다.
- 실행하려는 작업이 시간이 얼마 걸리지 않거나 I/O 에 의한 대기 시간이 크고, CPU 코어 수가 작아 동시에 실행할 수 있는 스레드 개수가 한정된 경우에는 특히 코루틴과 일반 스레드를 사용한 비동기 처리 사이에 차이가 커진다.

### 2.2 코루틴 컨텍스트와 디스패처
> launch, async 등은 모두 CoroutineScope 의 확장 함수다. 그런데 CoroutineScope 에는 CoroutineContext 타입의 필드 하나만 들어있다. 사실 CoroutineScope 는 CoroutineContext 필드를 launch 등의 확장 함수 내부에서 사용하기 위한 매개체 역할만을 담당한다. 원한다면 launch 등에 CoroutineContext 를 넘길 수도 있다는 점에서 실제로 CoroutineScope 보다 CoroutineContext 가 코루틴 실행에 더 중요한 의미가 있음을 유추할 수 있을 것이다.

- CoroutineContext 는 실제로 코루틴이 실행 중인 여러 작업(Job 타입)과 디스패처를 저장하는 일종의 맵이라 할 수 있다.
- 코틀린 런타임은 이 CoroutineContext 를 사용해서 다음에 실행할 작업을 선정하고, 어떻게 스레드에 배정할지에 대한 방법을 결정한다.

### 2.3 코루틴 빌더와 일시 중단 함수
> launch 나 async, runBlocking 은 모두 코루틴 빌더라고 불린다. 이들은 코루틴을 만들어주는 함수다.

- `kotlinx-coroutines-core` 모듈이 제공하는 코루틴 빌더는 다음과 같이 2가지가 더 있다.
    - produce: 정해진 채널로 데이터를 스트림으로 보내는 코루틴을 만든다. 이 함수는 ReceiveChannel<>을 반환한다. 그 채널로부터 메시지를 전달받아 사용할 수 있다.
    - actor: 정해진 채널로 메시지를 받아 처리하는 액터를 코루틴으로 만든다. 이 함수가 반환하는 SendChannel<> 채널의 send() 메소드를 통해 액터에게 메시지를 보낼 수 있다.

- 한편 delay() 와 yield() 는 코루틴 안에서 특별한 의미를 지니는 함수들이다. 이런 함수를 일시 중단(suspending) 함수라고 부른다. 예제에서 살펴본 delay() 와 yield() 외에 다음 함수들이 `kotlinx-coroutines-core` 모듈의 최상위에 정의된 일시 중단 함수들이다.
    - withContext: 다른 컨텍스트로 코루틴을 전환한다.
    - withTimeout: 코루틴이 정해진 시간 안에 실행되지 않으면 예외를 발생시키게 한다.
    - withTimeoutOrNull: 코루틴이 정해진 시간 안에 실행되지 않으면 null을 결과로 돌려준다.
    - awaitAll: 모든 작업의 성공을 기다린다. 작업 중 어느 하나가 예외로 실패하면 awaitAll 도 그 예외로 실패한다.
    - joinAll: 모든 작업이 끝날 때까지 현재 작업을 일시 중단시킨다.

### 3 suspend 키워드와 코틀린의 일시 중단 함수 컴파일 방법
> 함수 정의의 `fun` 앞에 `suspend` 를 넣으면 일시 중단 함수를 만들 수 있다.
예를 들어 launch 시 호출할 코드가 복잡하다면 별도의 suspend 함수를 정의해 호출하는것도 가능하다

```kotlin
suspend fun yieldThreeTimes() {
    log("1")
    delay(1000L)
    yield()
    log("2")
    delay(1000L)
    yield()
    log("3")
    delay(1000L)
    yield()
    log("4")
}

fun suspendExample() {
    GlobalScope.launch { yieldThreeTimes() }
}
```

- suspend 함수는 어떻게 동작하는 것인가? 예를 들어 일시 중단 함수 안에서 yield()를 해야 하는 경우 어떤 동작이 필요할 것인가?
    - 코루틴에 진입할 때와 코루틴에서 나갈 때 코루틴이 실행 중이던 상태를 저장하고 복구하는 등의 작업을 할 수 있어야 한다.
    - 현재 실행 중이던 위치를 저장하고 다시 코루틴이 재개될 때 해당 위치부터 실행을 재개할 수 있어야 한다.
    - 다음에 어떤 코루틴을 실행할지 결정한다.

- 이 세가지 중 마지막 동작은 코루틴 컨텍스트에 있는 디스패치에 의해 수행된다. 일시 중단 함수를 컴파일하는 컴파일러는 앞의 두 가지 작업을 할 수 있는 코드를 생성해내야 한다. 이때 코틀린은 컨티뉴에이션 패싱 스타일(CPS, continuation passing style) 변환과 상태기계(state machine)를 활용해 코드를 생성해낸다.
- CPS 변환은 프로그램의 실행 중 특정 시점 이후에 진행해야 하는 내용을 별도의 함수로 뽑고(이런 함수를 컨티뉴에이션 이라 부른다), 그 함수에게 현재 시점까지 실행한 결과를 넘겨서 처리하게 만드는 소스코드 변환 기술이다.

### 4 코루틴 빌더 만들기
> 일반적으로 직접 코루틴 빌더를 만들 필요는 없다. 기존 async, launch 만으로도 상당히 다양한 작업을 처리할 수 있기 때문이다.
간단히 코틀린 코루틴 예제에 들어있는 제네레이터 빌더를 살펴본다.

### 4.1 제네레이터 빌더 사용법
```kotlin
fun idMaker() = generate(Int, Unit) {
    var index = 0
    while (index < 3)
        yield(index++)
}

fun main(args: Array<String>) {
    val gen = idMaker()
    println(gen.next(Unit)) // 0
    println(gen.next(Unit)) // 1
    println(gen.next(Unit)) // 2
    println(gen.next(Unit)) // null
}
```
- 파이썬이나 자바스크립트 등의 다른 언어에 있는 제네레이터와 그리 다르지 않다.

### 4.2 제네레이터 빌더 구현

**launch 구현**

```kotlin
fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) = 
    block.startCoroutine(Continuation(context) { result -> 
        result.onFailure { exception ->
            val currentThread = Thread.currentThread()
            currentThread.uncaughExceptionHandler.uncaughException(currentThread, exception)
        }
    })
```
- 여기서는 즉시 코루틴을 실행하며, 익명 클래스를 사용해 Continuation 클래스를 직접 만들어서 startCorouine 에 전달하다.
- 블록을 코루틴으로 실행하고 나면 아무 처리도 필요 없기 때문에 result 의 onSuccess 쪽에는 아무 콜백을 지정하지 않고, 예외로 실패한 경우에만 예외를 다시 던진다.
- result는 코루틴(launch에 전달한 블록)이 반환하는 값을 코틀린 런타임이 Continuation 에 전달해 준다는 점에 유의

### 5 결론
> 주체 자체가 처음 접하는 개발자에겐 어려울 수 있는 주제이므로 코루틴 빌더 구현까지 이해하려면 여러 예제를 보면서 비교하고 중간 중간 로그와 스택 트레이스를 넣어가면서 쫓아가 볼 필요가 있다.
하지만 코루틴 빌더 구현 쪽을 이해하지 못하더라도 launch, async, await 정도의 기본 제공 코루틴 빌더만으로도 충분히 코딩이 가능하다.
비동기 코딩에서 async/await 를 사용하는 경우와 콜백이나 퓨처를 사용하는 경우의 어려움을 비교해보고, async/await 을 사용하는 간결한 프로그래밍을 해보자.

### 참고
- Kotlin IN ACTION
- [코루틴 가이드](https://kotlinlang.org/docs/coroutines-guide.html)
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [actor](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/actor.html)
- [produce](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/produce.html)

## 코루틴 Dispatchers
- 코루틴 컨텍스트에는 해당 코루틴이 실행에 사용하는 스레드를 결정하는 코루틴 디스패처(CoroutineDispatcher 참조)가 포함됩니다. 
- 코루틴 디스패처는 코루틴 실행을 특정 스레드로 제한하거나 스레드 풀에 디스패치하거나 제한 없이 실행되도록 할 수 있습니다.
- launch 및 async와 같은 모든 코루틴 빌더는 새 코루틴 및 기타 컨텍스트 요소에 대한 디스패처를 명시적으로 지정하는 데 사용할 수 있는 선택적 CoroutineContext 매개변수를 허용합니다.

<br/>

### 모든 코루틴 디스패처 구현에 의해 확장될 기본 클래스입니다.
> 다음 표준 구현은 kotlinx.coroutines에서 Dispatchers 객체의 속성으로 제공됩니다.

| Dispatchers | Description |
| --- | --- |
| Dispatchers.Default | 디스패처 또는 다른 ContinuationInterceptor가 컨텍스트에 지정되지 않은 경우 모든 표준 빌더에서 사용됩니다. 공유 배경 스레드의 공통 풀을 사용합니다. 이는 CPU 리소스를 소비하는 컴퓨팅 집약적 코루틴에 적합한 선택입니다. |
| Dispatchers.IO | 주문형 생성 스레드의 공유 풀을 사용하며 IO 집약적인 차단 작업(예: 파일 I/O 및 차단 소켓 I/O)의 부담을 덜어주기 위해 설계되었습니다. |
| Dispatchers.Unconfined | 첫 번째 일시 중단이 있을 때까지 현재 호출 프레임에서 코루틴 실행을 시작하고 그 후 코루틴 빌더 함수가 반환됩니다. 코루틴은 나중에 특정 스레드나 풀로 제한하지 않고 해당 일시 중단 함수에서 사용하는 스레드에서 다시 시작됩니다. Unconfined 디스패처는 일반적으로 코드에서 사용하면 안 됩니다. |

- newSingleThreadContext 및 newFixedThreadPoolContext를 사용하여 프라이빗 스레드 풀을 생성할 수 있습니다.
- 임의의 java.util.concurrent.Executor는 asCoroutineDispatcher 확장 기능을 사용하여 디스패처로 변환될 수 있습니다.





```kotlin
launch { // context of the parent, main runBlocking coroutine
    println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
    println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
}
launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher 
    println("Default               : I'm working in thread ${Thread.currentThread().name}")
}
launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
    println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
}

Unconfined            : I'm working in thread main @coroutine#3
Default               : I'm working in thread DefaultDispatcher-worker-1 @coroutine#4
newSingleThreadContext: I'm working in thread MyOwnThread @coroutine#5
main runBlocking      : I'm working in thread main @coroutine#2
```

- launch { ... }가 매개변수 없이 사용되면 시작되는 CoroutineScope에서 컨텍스트(따라서 디스패처)를 상속합니다. 이 경우 메인 스레드에서 실행되는 메인 runBlocking 코루틴의 컨텍스트를 상속합니다.


### 참고
- [Coroutine context and dispatchers](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html)
- [CoroutineDispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/index.html)


## Serialization
> 직렬화는 응용 프로그램에서 사용하는 데이터를 네트워크를 통해 전송하거나 데이터베이스 또는 파일에 저장할 수 있는 형식으로 변환하는 프로세스입니다. 역직렬화는 외부 소스에서 데이터를 읽고 이를 런타임 개체로 변환하는 반대 프로세스입니다. 이들은 함께 제3자와 데이터를 교환하는 대부분의 애플리케이션에서 필수적인 부분입니다.

- JSON 및 프로토콜 버퍼와 같은 일부 데이터 직렬화 형식은 특히 일반적입니다. 언어 중립적이고 플랫폼 중립적이기 때문에 모든 현대 언어로 작성된 시스템 간에 데이터 교환이 가능합니다.
- Kotlin에서 데이터 직렬화 도구는 별도의 구성요소인 kotlinx.serialization에서 사용할 수 있습니다. Gradle 플러그인인 org.jetbrains.kotlin.plugin.serialization과 런타임 라이브러리의 두 가지 주요 부분으로 구성됩니다.

### JSON serialization
> Kotlin 객체를 JSON으로 직렬화하는 방법

- 시작하기 전에 프로젝트에서 Kotlin 직렬화 도구를 사용할 수 있도록 빌드 스크립트를 구성해야 합니다.

1. Kotlin 직렬화 Gradle 플러그인 적용
```
plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
}
```

2. JSON 직렬화 라이브러리 종속성 추가
```
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}
```

- 이제 코드에서 직렬화 API를 사용할 준비가 되었습니다. API는 kotlinx.serialization 패키지와 kotlinx.serialization.json과 같은 형식별 하위 패키지에 있습니다.

<br/>

- 먼저 @Serializable 주석을 달아 클래스를 직렬화 가능하게 만듭니다.
```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class Data(val a: Int, val b: String)
```

<br/>

- 이제 Json.encodeToString()을 호출하여 이 클래스의 인스턴스를 직렬화할 수 있습니다.
```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Serializable
data class Data(val a: Int, val b: String)

fun main() {
   val json = Json.encodeToString(Data(42, "str"))
}
```

<br/>

- 결과적으로 이 객체의 상태를 JSON 형식으로 포함하는 문자열을 얻습니다.
```
{"a": 42, "b": "str"}
```

- 단일 호출로 목록과 같은 개체 컬렉션을 직렬화할 수도 있습니다.
```kotlin
val dataList = listOf(Data(42, "str"), Data(12, "test"))
val jsonList = Json.encodeToString(dataList)
```

- JSON에서 객체를 역직렬화하려면 decodeFromString() 함수를 사용하십시오
```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
data class Data(val a: Int, val b: String)

fun main() {
   val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")
}
```

### 참고
- [Kotlin Serialization Guide](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md)