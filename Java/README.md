# Java
- [Serialization and Deserialization](#serialization-and-deserialization)

---

## Serialization and Deserialization
> 직렬화는 객체의 상태를 바이트 스트림으로 변환하는 메커니즘입니다. 역직렬화는 바이트 스트림을 사용하여 메모리에서 실제 Java 객체를 재생성하는 역 프로세스입니다.
이 메커니즘은 개체를 유지하는 데 사용됩니다.

- 생성된 바이트 스트림은 플랫폼에 독립적입니다. 따라서 한 플랫폼에서 직렬화된 개체는 다른 플랫폼에서 역직렬화될 수 있습니다.
- Java 객체를 직렬화 가능하게 만들기 위해 java.io.Serializable 인터페이스를 구현합니다. ObjectOutputStream 클래스에는 Object 직렬화를 위한 writeObject() 메서드가 포함되어 있습니다.
```java
public final void writeObject(Object obj) throws IOException
```

- ObjectInputStream 클래스에는 객체를 역직렬화하기 위한 readObject() 메서드가 포함되어 있습니다.
```java
public final Object readObject() throws IOException, ClassNotFoundException
```

### 직렬화의 장점
1. 객체의 상태를 저장/유지합니다. 
2. 네트워크를 통해 개체를 이동합니다.

- java.io.Serializable 인터페이스를 구현하는 클래스의 객체만 직렬화할 수 있습니다.
- Serializable은 마커 인터페이스입니다(데이터 멤버 및 메서드가 없음).
- 이 클래스의 객체가 특정 기능을 가질 수 있도록 Java 클래스를 표시하는 데 사용됩니다. 마커 인터페이스의 다른 예는 다음과 같습니다. - 복제 가능 및 원격.

**기억할 점**
1. 부모 클래스가 Serializable 인터페이스를 구현했다면 자식 클래스는 구현할 필요가 없지만 그 반대의 경우도 마찬가지입니다. 
2. 비정적 데이터 멤버만 직렬화 프로세스를 통해 저장됩니다. 
3. 정적 데이터 멤버와 임시 데이터 멤버는 직렬화 프로세스를 통해 저장되지 않습니다. 따라서 비정적 데이터 멤버의 값을 저장하지 않으려면 임시로 설정합니다. 
4. 객체의 생성자는 객체가 직렬화 해제될 때 호출되지 않습니다. 
5. 연결된 개체는 직렬화 가능한 인터페이스를 구현해야 합니다.

```java
class A implements Serializable{
    // B also implements Serializable
    // interface.
    B ob = new B();  
}
```

### SerialVersionUID
- 직렬화 런타임은 버전 번호를 SerialVersionUID라고 하는 각 직렬화 가능 클래스와 연결합니다. 이 클래스는 직렬화와 관련하여 호환되는 해당 개체에 대해 로드된 클래스가 직렬화된 개체의 송신자와 수신자에게 로드되었는지 확인하기 위해 직렬화 해제 중에 사용됩니다.
- 수신자가 해당 발신자의 클래스와 UID가 다른 객체에 대한 클래스를 로드한 경우 Deserialization은 InvalidClassException을 발생시킵니다.
- Serializable 클래스는 필드 이름을 선언하여 명시적으로 자체 UID를 선언할 수 있습니다.
- 직렬화 가능한 클래스가 serialVersionUID를 명시적으로 선언하지 않으면 직렬화 런타임은 Java Object Serialization Specification에 설명된 대로 클래스의 다양한 측면을 기반으로 해당 클래스에 대한 기본 값을 계산합니다.
- 모든 직렬화 가능 클래스는 명시적으로 serialVersionUID 값을 선언하는 것이 좋습니다. 그 계산은 컴파일러 구현에 따라 다를 수 있는 클래스 세부사항에 매우 민감하기 때문에 클래스를 변경하거나 다른 ID를 사용하면 직렬화된 데이터에 영향을 미칠 수 있습니다.
