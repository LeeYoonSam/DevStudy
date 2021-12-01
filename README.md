# DevStudy
개발하는데 필요한것을 공부하고 기록하기위한 저장소

### 참고
- [Android Interview Questions](https://github.com/MindorksOpenSource/android-interview-questions)
- [Technical Interview Guidelines for Beginners](https://github.com/JaeYeopHan/Interview_Question_for_Beginner)
- [tech-interview-for-developer](https://github.com/gyoogle/tech-interview-for-developer)
- [Tech Interview Handbook](https://techinterviewhandbook.org/)

# [OS](./OS)
- [프로세스](./OS#프로세스)
- [문맥 교환(Context Switching)](./OS#문맥-교환context-switching)
- [스레드](./OS#스레드)

# [자료구조](./DataStructure)
- [Array vs Linked List](./DataStructure#array-vs-linked-list)
- [Stack and Queue](./DataStructure#stack-and-queue)
- [Tree](./DataStructure#tree)
- [Binary Heap](./DataStructure#binary-heap)
- [Red Black Tree](./DataStructure#red-black-tree)
- [Hash Table](./DataStructure#hash-table)
- [Graph](./DataStructure#graph)


# [Android](./Android)

## [Core Android](./Android/CoreAndroid)
- [안드로이드 어플리케이션 컴포넌트](./Android/CoreAndroid#안드로이드-어플리케이션-컴포넌트)
- [매니페스트 파일](./Android/CoreAndroid#매니페스트-파일)
- [안드로이드 어플리케이션의 프로젝트 구조](./Android/CoreAndroid#안드로이드-어플리케이션의-프로젝트-구조)
- [Android Context](./Android/CoreAndroid#android-context)
- [What is requireActivity?](./Android/CoreAndroid#what-is-requireactivity)
- [Pro-guard 의 용도는?](./Android/CoreAndroid#pro-guard-의-용도는)
- [Pending Intent 를 사용해서 액티비티를 시작하는 방법은?](./Android/CoreAndroid#pending-intent-를-사용해서-액티비티를-시작하는-방법은)
- [안드로이드 앱 프로세스 분리하기](./Android/CoreAndroid#안드로이드-앱-프로세스-분리하기)
- [서비스와 액티비티 간에 활용할 수 있는 IPC](./Android/CoreAndroid#서비스와-액티비티-간에-활용할-수-있는-ipc)

## [Activity and Fragment](./Android/ActivityAndFragment)
- [Activity 생명주기](./Android/ActivityAndFragment#activity-생명주기)
- [onSavedInstanceState onRestoreInstanceState](./Android/ActivityAndFragment#onsavedinstancestate-onrestoreinstancestate)
- [프래그먼트 생명주기](./Android/ActivityAndFragment#프래그먼트-생명주기)
- [Fragment 사용하는 이유](./Android/ActivityAndFragment#fragment-사용하는-이유)
- [언제 Activity가 아닌 Fragment를 사용 하는지?](./Android/ActivityAndFragment#언제-activity가-아닌-fragment를-사용-하는지)
- [FragmentPagerAdapter와 FragmentStatePagerAdapter의 차이점](./Android/ActivityAndFragment#fragmentpageradapter와-fragmentstatepageradapter의-차이점)
- [Fragment 백스택에서 Add, Replace 차이점](./Android/ActivityAndFragment#fragment-백스택에서-add-replace-차이점)
- [기본 생성자만 사용하여 fragment 를 만드는것이 권장되는 이유](./Android/ActivityAndFragment#기본-생성자만-사용하여-fragment-를-만드는것이-권장되는-이유)
- [두 fragment 간 데이터 전달 방법](./Android/ActivityAndFragment#두-fragment-간-데이터-전달-방법)
- [retained Fragment 란?](./Android/ActivityAndFragment#retained-fragment-란)
- [Fragment transaction 을 커밋하는 동안 addToBackStack()의 목적은?](./Android/ActivityAndFragment#fragment-transaction-을-커밋하는-동안-addtobackstack의-목적은)
- [액티비티 launchMode 설명](./Android/ActivityAndFragment#액티비티-launchmode-설명)

## [Android UI](./Android/Ui)
- [안드로이드에서 View 란?](./Android/Ui#안드로이드에서-view-란)
- [Custom View 만들기](./Android/Ui#custom-view-만들기)
- [ViewGroups 이란 무엇이며 View와 어떻게 다른지?](./Android/Ui#viewgroups-이란-무엇이며-view와-어떻게-다른지)
- [ConstraintLayout 이란?](./Android/Ui#constraintlayout-이란)
- [ViewTreeObserver가 무엇인지?](./Android/Ui#viewtreeobserver가-무엇인지)
- [include, merge, ViewStub 차이](./Android/Ui#include-merge-viewstub-차이)

## [Displaying Lists of Content](./Android/RecyclerView)
- [RecyclerView](./Android/RecyclerView#recyclerview)
- [ListView 와 RecyclerView 의 차이점](./Android/RecyclerView#listview-와-recyclerview-의-차이점)
- [ViewHolder 패턴을 사용하는 이유](./Android/RecyclerView#viewholder-패턴을-사용하는-이유)
- [RecyclerView 최적화 방법](./Android/RecyclerView#recyclerview-최적화-방법)
- [SnapHelper](./Android/RecyclerView#snaphelper)

## [Intent](./Android/Intent)
- [Intent란?](./Android/Intent#intent란)

## [Long-running Operations](./Android/LongRunningOperations)
- [ANR 이 무엇이며 어떻게 방지할 수 있는지?](./Android/LongRunningOperations#anr-이-무엇이며-어떻게-방지할-수-있는지)
- [AsyncTask 의 생명 주기와 액티비티의 관계 및 발생할수 있는 문제](./Android/LongRunningOperations#asynctask-의-생명-주기와-액티비티의-관계-및-발생할수-있는-문제)
- [Looper, Handler and HandlerThread](./Android/LongRunningOperations#looper-handler-and-handlerthread)
- [RxJava, Coroutine 비교](./Android/LongRunningOperations#rxjava-coroutine-비교)

## [Architecture](./Android/Architecture)
- [Architecture 란?](./Android/Architecture#architecture-란)
- [아키텍처 패턴](./Android/Architecture#아키텍처-패턴)
- [MVVM Architecture](./Android/Architecture#mvvm-architecture)
- [MVP Architecture](./Android/Architecture#mvp-architecture)
- [MVC Architecture](./Android/Architecture#mvc-architecture)
- [MVI Architecture](./Android/Architecture#mvi-architecture)
- [Clean Architecture](./Android/Architecture#clean-architecture)
- [모바일 클린 아키텍처](./Android/Architecture#모바일-클린-아키텍처)
- [Clean Code](./Android/Architecture#clean-code)

## [ImageGraphic](./Android/ImageGraphic)
- [비트맵 처리](./Android/ImageGraphic#비트맵-처리)

## [Library](./Android/Library)
- [Dagger Hilt Koin 비교](./Android/Library#dagger-hilt-koin-비교)
- [RxJava](./Android/Library#rxjava)

# [Kotlin](./Kotlin)
- [코틀린 기초](./Kotlin/#코틀린-기초)
- [Data class](./Kotlin/#data-class)
- [Sealed Class](./Kotlin/#sealed-class)
- [Scope functions](./Kotlin/#scope-functions)
- [람다로 프로그래밍](./Kotlin/#람다로-프로그래밍)
- [코틀린 타입 시스템](./Kotlin/#코틀린-타입-시스템)
- [연산자 오버로딩과 기타 관례](./Kotlin/#연산자-오버로딩과-기타-관례)
- [고차 함수: 파라미터와 반환 값으로 람다 사용](./Kotlin/#고차-함수-파라미터와-반환-값으로-람다-사용)
- [inline, noinline, crossinline, reified](./Kotlin/#inline-noinline-crossinline-reified)
- [Kotlin 클로저](./Kotlin/#kotlin-클로저)
- [제네릭스](./Kotlin/#제네릭스)
- [유용하게 사용하는 Extension](./Kotlin/#유용하게-사용하는-extension)
- [코루틴 Core](./Kotlin#코루틴-core)
- [코틀린 버전별 비교](./Kotlin/#코틀린-버전별-비교)
- [코루틴과 Async/Await](./Kotlin/#코루틴과-asyncawait)