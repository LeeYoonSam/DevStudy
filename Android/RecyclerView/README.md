### Displaying Lists of Content
- [RecyclerView](#recyclerview)
- [ListView 와 RecyclerView 의 차이점](#listview-와-recyclerview-의-차이점)
- [ViewHolder 패턴을 사용하는 이유](#viewholder-패턴을-사용하는-이유)
- [RecyclerView 최적화 방법](#recyclerview-최적화-방법)
- [SnapHelper](#snaphelper)

---

## [RecyclerView](https://kimdabang.tistory.com/entry/Recycler-View-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0-RecyclerView-lifecycle)
- Google은 2014년 롤리팝 출시와 함께 RecyclerView 를 공개
- RecyclerView의 아이디어는 사용자가 스크롤 할때마다 뷰가 생성되는 게 아니라 **뷰는 처음 한번만 생성되고 필요할때마다 재사용** 하는 방법이다.
- LayoutManager, ItemDecoration, ItemAnimator 등과 같은 다른 개선사항들을 제공
- RecyclerView에서는 ViewHolder를 필수적으로 갖는것을 강제하고 있다.

### Main Components
- LayoutManager: 리스트 항목을 배치
- ItemAnimator: 아이템 추가, 제거 정렬될 때의 애니메이션 처리
- Adapter: 리스트의 항목을 구성

### LayoutManager란?
- LayoutManager 는 RecyclerView가 언제 child view 를 재사용할지 알려주는 역할을 한다.
- LayoutManager 가 없다면 RecyclerView는 어떤 종류의 레이아웃을 화면에 배치해야하는지 알 수 없다.
<br>

**LayoutManager 종류**
1. LinearLayoutManager : 간단한 수직 또는 수평의 레이아웃을 제공합니다
2. GridLayoutManger : spans(colums)을 가지는 격자 레이아웃을 제공합니다
3. StaggeredLayoutManager : 크기가 제각각인 엇갈린 레이아웃을 제공합니다.

### Adapter 란?
- 뷰와 데이터들을 bind 하는 역할
- 일반적인 adapter는 데이터셋의 크기에 의해 결정되는 getCount()를 가지며, 새로운 뷰를 생성하고 그곳에 데이터를 bind 시키는것을 반복하는 일을 한다.
- RecyclerView는 ViewHolder 를 기본적으로 사용
- Adapter 는 뷰를 생성하지 않고, infalted(xml의 레이아웃이 메모리에 객체화된) 뷰를 갖는 ViewHolder 를 생성
- ViewHolder 가 생성되어 cache 에 쌓이면 필요할때 재사용
- ViewHolder 는 position 이 아닌 itemViewType 에 의해 생성된다는 것이다.

### Life of a ViewHolder - Birth
- LayoutManager 가 레이아웃을 찾는동안 RecyclerView 에게 뷰 객체를 요청(recyclerview.getViewForPosition())
- RecyclerView 는 Cache 를 확인해서 찾는 view가 있다면 LayoutManager 에게 전달
- Cache 에 없다면 RecyclerView 는 Adapter 에게 ViewType 을 가져온다(adapter.getViewType())
- ViewType 을 알게되면 Recycled Pool 을 확인하여 이 타입의 ViewHolder 를 가져온다.
- Recycled Pool 은 여러 RecyclerView 들 끼리 공유할 수 있는 Pool 이다.
- Recycled Pool 에 ViewHolder 가 없다면 RecyclerView 는 adapter 에게 뷰홀더를 새로 만들것을 요청(createViewHolder())
- Adapter 에게 bindViewHolder()를 호출해서 특정 position 에 이 ViewHolder 를 bind 하라고 요청
- RecyclerView 는 리턴받은 뷰를 다시 LayoutManager 에게 전달
- LayoutManager 는 RecyclerView 에게 이 뷰가 레이아웃에 추가되었다고 알리고, RecyclerView는 Adapter 에게 알린다.

### Reserves
> 사용자가 스크롤을 올려서 기존의 리스트가 화면에 보이지 않을때

- LayoutManager 는 View 를 제거하고 나중에 재활용
- LayoutManager 는 이를 RecyclerView 에게 알려주고 (removeAndRecyclerView()), RecyclerView 는 Adapter 에게 알려준다(onViewDetachedFromWindow())
- 이 뷰가 이 position 에 유효한지 확인, 유효한 경우 cache 에 유지시키라고 지시
- LayoutManager 가 이 position 에 대해 뷰를 다시 요청할 경우 Adapter 를 거칠 필요 없이 알려줄수 있다.
- Cache 는 Pool 에게 오래된 항목이 삭제되었는지 알려주고, Pool은 Adapter 에게 메로리를 절약하기 위해 오래된 항목 내용을 삭제해도 된다고 알려준다.
- 뷰가 유효하지 않을때, 예를 들어 아이템 항목하나가 제거되었다고 가정하면 그런 경우 더이상 유효하지 않기 때문에 이를 Pool 에게 보내고 Adapter 에게 알려준다.

### Fancy Reserves
> LayoutManager 가 다시 레이아웃을 계산하는 동안 Adapter 가 변경된 경우

- 사용하지 않는 뷰는 각각 숨겨진 Adapter List 에 추가
- itemAnimator 가 끝나고(onAnimationFinished())나서 Adapter 에게 알려주므로 (onViewDetachedFromWindow()) RecyclerView 가 Cache 를 업데이트하고 사용하지 않는 항목을 재활용

### Death
> 레이아웃에 뷰가 필요하지 않고 뷰가 trasient state(예를 들어 fading out)인 경우

- 뷰가 재사용될 수 없으므로 Adapter 에게 요청(onFailedToRecycle())
- Adapter 는 ViewHolder 의 상태를 다시 세팅하고 반환
- Pool 은 유형별로 고정된 크기를 가지고, 가득 찬 경우 ViewHolder 가 삭제된다.

### 참고
- [RecyclerView ins and outs - Google I/O 2016](https://www.youtube.com/watch?v=LqBlYJTfLP4)
- [Android Recycler View Lifecycle diagrams and notes](http://landenlabs.com/android/info/recycler/recycler.html)

---

## ListView 와 RecyclerView 의 차이점
1. 스크롤시 셀을 재활용 - ListView Adapter 에서도 ViewHolder 를 사용하면 가능하지만 옵션 사항이고, RecyclerView 는 adapter 구현시 기본으로 적용되는 방법이다.
2. 컨테이너에서 List를 분리 - LayoutManager를 설정하여 런타임에 다른 컨테이너(linearLayout, horizontal, gridLayout)에 List를 쉽게 넣을 수 있다.
```java
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//or
mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
```
3. 일반적인 List 작업에 애니메이션을 적용 - 애니메이션은 분리되어 ItemAnimator에 위임된다.

---

## ViewHolder 패턴을 사용하는 이유
- 스크롤하는 동안 findViewById()를 자주 호출하여 성능을 저하시킬 수 있다.
- 어댑터가 재활용을 위해 `inflated view`를 반환하더라도 여전히 요소를 찾아 업데이트해야 한다.
- findViewById()를 반복적으로 사용하는 방법은 "뷰 홀더" 디자인 패턴을 사용하는 것

---

## RecyclerView 최적화 방법
1. 이미지 로드 라이브러리 사용하기
- GC(Garbage Collection)가 메인 스레드에서 실행되기 때문에 UI가 응답하지 않는 이유 중 하나는 메모리의 지속적인 할당 및 할당 해제로 인해 GC 실행이 매우 자주 발생하기 때문입니다.
- 비트맵 풀 개념을 사용하면 이를 피할 수 있습니다.
- Glide, Fresco와 같은 Image-Loading 라이브러리가 이 비트맵 풀 개념을 사용하기 때문에 이미지에 관한 작업을 이미지 라이브러리로 위임하는것이 좋다.
2. 이미지 너비 및 높이 설정
- 이미지 너비와 높이가 동적(고정 아님)이고 서버에서 imageUrl을 가져오는 경우
- 정확한 이미지 너비와 높이를 미리 설정하지 않으면 로딩(이미지 다운로드)과 이미지를 ImageView로 설정하는 과정(다운로드 완료 시 실제로 보이게 됨) 동안 UI가 깜박거립니다.
- 너비와 높이를 먼저 설정하면 깜박임이 없습니다.
3. onBindViewHolder 메서드에서 작업을 줄인다.
-  onBindViewHolder 메서드는 훨씬 적은 작업을 수행해야 합니다. 
-  onBindViewHolder 메서드를 확인하고 최적화해야 합니다. 
-  이렇게 하면 RecyclerView가 개선되는 것을 볼 수 있습니다.
4. RecyclerView API의 Notify 사용
- remove, update, add 작업이 있을 때마다 Notify Item API를 사용합니다.
```java
adapter.notifyItemRemoved(position)
adapter.notifyItemChanged(position)
adapter.notifyItemInserted(position)
adapter.notifyItemRangeInserted(start, end)
```
- 사용 사례에 따라 notifyDataSetChanged()를 사용해야 하는 경우 setHasStableIds(true)를 시도할 수 있습니다.
```java
adapter.setHasStableIds(true)
```
- notifyDataSetChanged()를 호출하더라도 전체 어댑터의 완전한 재정렬을 처리할 필요가 없습니다. 위치에 있는 항목이 이전과 동일한지 확인하고 작업을 덜 수행할 수 있기 때문입니다.
5. 중첩된 View 피하기
- 가능하면 중첩된 View를 피하고 가능한 한 평면 View를 생성하도록 노력해야 합니다.
- 중첩은 RecyclerView 성능을 감소시킵니다. 
- 평면 보기는 성능을 향상시킵니다.
6. setHasFixedSize 사용
- 모든 항목의 높이가 동일한 경우 이 방법을 사용해야 합니다. 아래를 추가하고 성능을 확인하십시오.
```java
recyclerView.setHasFixedSize(true)
```
7. 중첩된 RecyclerView 최적화를 위해 setRecycledViewPool 사용
- RecyclerView는 가능한 한 "풀을 사용하여 View 재사용" 원칙에 따라 작동한다는 것을 알고 있습니다.
- 풀은 동일한 유형의 View 를 가진 두 RecyclerView 간에 공유되지 않습니다.
```kotlin
class OuterRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // code removed for brevity

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // code removed for brevity
        
        viewHolderOne.innerRecyclerViewOne.setRecycledViewPool(viewPool)
        
        // code removed for brevity

        viewHolderTwo.innerRecyclerViewTwo.setRecycledViewPool(viewPool)
        
    }
    
    // code removed for brevity
}
```
- 공유 ViewPool에서 뷰를 재사용하기 시작하므로 스크롤 성능이 향상됩니다.

8. setItemViewCacheSize 사용
```java
recyclerView.setItemViewCacheSize(cacheSize)
```
- 공식 문서에 따르면 잠재적으로 공유된 재활용 뷰 풀에 추가하기 전에 유지할 오프스크린 뷰 수를 설정
- 오프스크린 뷰 캐시는 연결된 어댑터의 변경 사항을 계속 인식하므로 LayoutManager가 뷰를 다시 바인딩하기 위해 어댑터로 돌아갈 필요 없이 수정되지 않은 뷰를 재사용할 수 있습니다.
-  RecyclerView를 스크롤하여 화면에서 거의 완전히 벗어난 뷰가 있을 때 RecyclerView는 해당 뷰를 계속 유지하므로 onBindViewHolder()를 다시 호출하지 않고도 뷰로 다시 스크롤할 수 있습니다.

---

## [SnapHelper](https://blog.mindorks.com/using-snaphelper-in-recyclerview-fc616b6833e8)
- SnapHelper는 RecyclerView의 자식 View 를 스냅하는데 사용하는 도우미 클래스입니다. 
- 예를 들어, 플레이 스토어 애플리케이션에서 스크롤이 유휴 위치에 올 때 firstVisibleItem이 항상 완전히 표시되어야 한다면 RecyclerView의 firstVisibleItem을 스냅할 수 있습니다.
- 아이템이 정확환 위치가 아닌 스크롤에 걸쳐있을때 정확환 위치로 snap 을 해준다.

```kotlin
SnapHelper snapHelper = new LinearSnapHelper();
snapHelper.attachToRecyclerView(yourRecyclerView);
```

### [Custom SnapHelper 구현시](https://github.com/MindorksOpenSource/SnapHelperExample)
1. calculateDistanceToFinalSnap 
- 대상 뷰 또는 축의 컨테이너 뷰 내의 특정 지점에 스냅하려면 이 메서드를 재정의합니다.
- 이 메서드는 SnapHelper가 플링을 가로채고 대상 뷰에 맞추기 위해 스크롤하는 데 필요한 정확한 거리를 알아야 할 때 호출됩니다.

2. findSnapView
- 특정 타겟에 snap을 하려면 이 메소드를 재정의
- SnapHelper가 스냅을 시작할 준비가 되어 있고 스냅할 대상 뷰가 필요할 때 호출
- 스크롤 후 스크롤 상태가 유휴 상태가 되면 명시적으로 호출됩니다.
- SnapHelper가 플링 후 스냅을 준비하고 현재 자식 뷰 세트의 참조 뷰가 필요할 때도 호출됩니다.
- 이 메서드가 null을 반환하면 SnapHelper는 어떤 뷰에도 스냅하지 않습니다.