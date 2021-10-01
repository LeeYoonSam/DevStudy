# DevStudy
개발하는데 필요한것을 공부하고 기록하기위한 저장소

### 참고
- [Android Interview Questions](https://github.com/MindorksOpenSource/android-interview-questions)
- [Technical Interview Guidelines for Beginners](https://github.com/JaeYeopHan/Interview_Question_for_Beginner)
- [tech-interview-for-developer](https://github.com/gyoogle/tech-interview-for-developer)
- [Tech Interview Handbook](https://techinterviewhandbook.org/)

# [OS](./OS)
- 프로세스
- 문맥 교환(Context Switching)
- 스레드

# [자료구조](./DataStructure)
- Array vs Linked List
- Stack and Queue
- Tree
  - Binary Tree
  - Full Binary Tree
  - Complete Binary Tree
- BST (Binary Search Tree)
- Binary Heap
- Red-Black Tree
  - 정의
  - 특징
  - 삽입
  - 삭제
- Hash Table
  - Hash Function
  - Resolve Collision
    - Open Addressing
    - Separate Chaining
  - Resize
- Graph
  - Graph 용어 정리
  - Graph 구현
  - Graph 탐색
  - Minimum Spanning Tree
    - Kruskal algorithm
    - Prim algorithm

# [Android](./Android)
### Core Android
- 안드로이드 어플리케이션 컴포넌트
- 매니페스트 파일
- 안드로이드 어플리케이션의 프로젝트 구조
- Android Context
### Activity and Fragment
- Activity 생명주기
- onSavedInstanceState onRestoreInstanceState
- 프래그먼트 생명주기
- Fragment 사용하는 이유
- 언제 Activity가 아닌 Fragment를 사용 하는지?
- FragmentPagerAdapter와 FragmentStatePagerAdapter의 차이점
- Fragment 백스택에서 Add, Replace 차이점
- 기본 생성자만 사용하여 fragment 를 만드는것이 권장되는 이유
- 두 fragment 간 데이터 전달 방법
- retained Fragment 란?
- Fragment transaction 을 커밋하는 동안 addToBackStack()의 목적은?
- 액티비티 launchMode 설명

### Views and ViewGroups
- 안드로이드에서 View 란?
- Custom View 만들기
- ViewGroups 이란 무엇이며 View와 어떻게 다른지?
- ConstraintLayout 이란?
- ViewTreeObserver가 무엇인지?

### Displaying Lists of Content
- ListView 와 RecyclerView 의 차이점
- ViewHolder 패턴을 사용하는 이유
- RecyclerView 최적화 방법
- SnapHelper

### Intent
- Intent란?

### Long-running Operations
- ANR 이 무엇이며 어떻게 방지할 수 있는지?
- AsyncTask 의 생명 주기와 액티비티의 관계 및 발생할수 있는 문제
- Looper, Handler and HandlerThread

### Architecture
- Architecture 란?
- 아키텍처 패턴
- MVVM Architecture
- MVP Architecture
- MVC Architecture
- MVI Architecture
- Clean Architecture
- 모바일 클린 아키텍처
- Clean Code

# [Kotlin](./Kotlin)
- 코틀린 기초
- Data class
- Sealed Class
- Scope functions
- 람다로 프로그래밍
- 코틀린 타입 시스템
- 연산자 오버로딩과 기타 관례
- 고차 함수: 파라미터와 반환 값으로 람다 사용
- inline, noinline, crossinline, reified
- Kotlin 클로저
- 제네릭스
- 유용하게 사용하는 Extension