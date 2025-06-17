# Compose Tip
실제 컴포즈를 사용하면서 자주 쓰는 코드

## 목록
- [Composable 콘텐츠가 배치되고 난 후 크기를 가져오는 방법](#composable-콘텐츠가-배치되고-난-후-크기를-가져오는-방법)
- [동적으로 뷰의 크기를 계산하기 (dp, px 계산)](#동적으로-뷰의-크기를-계산하기-dp-px-계산)

---

## Composable 콘텐츠가 배치되고 난 후 크기를 가져오는 방법

### onGloballyPositioned
- 콘텐츠의 전역 위치가 변경되었을 때 요소의 레이아웃 좌표와 함께 onGloballyPositioned를 호출합니다. 좌표가 확정된 컴포지션 이후에 호출된다는 점에 유의하세요.
- 이 콜백은 레이아웃 좌표를 사용할 수 있을 때 한 번 이상 호출되며, 창 내에서 요소의 위치가 변경될 때마다 호출됩니다. 그러나 수정된 요소의 화면 상대 위치가 변경될 때마다 반드시 호출되는 것은 아닙니다. 예를 들어, 시스템은 콜백을 실행하지 않고 창 안의 콘텐츠를 이동할 수 있습니다. 레이아웃 좌표를 사용하여 창 내부가 아닌 화면의 위치를 계산하는 경우 콜백을 받지 못할 수 있습니다.

```kotlin
Row(
    modifier = modifier
        .fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            layoutCoordinates.size.width
        },
)
```

## 동적으로 뷰의 크기를 계산하기 (dp, px 계산)
```kotlin
@Composable
fun PackagingImages(
    packagingImages: List<ProductPackagingImageItemViewModel>,
    modifier: Modifier = Modifier,
    onClickPackaging: (index: Int) -> Unit
) {
    var imageSize by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val imageSpacing = 4.dp
    val maxImageSize = 80.dp
    val numberOfImagesPerRow = packagingImages.size

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { layoutCoordinates ->
                val rowWidthPx = layoutCoordinates.size.width.toFloat()
                val totalSpacingPx = with(density) { (packagingImages.size - 1).times(imageSpacing).toPx() }
                val maxImageSizePx = with(density) { maxImageSize.toPx() }
                val availableWidthPx = rowWidthPx - totalSpacingPx
                val calculatedImageSizePx = availableWidthPx / numberOfImagesPerRow
                imageSize = if (calculatedImageSizePx < maxImageSizePx) {
                    with(density) { calculatedImageSizePx.toDp() }
                } else {
                    maxImageSize
                }
            },
        horizontalArrangement = Arrangement.spacedBy(imageSpacing)
    ) {
        packagingImages.forEach {
            PackagingImage(
                packagingImageItemViewModel = it,
                modifier = Modifier
                    .size(imageSize)
                    .aspectRatio(1f),
                onClickPackaging = onClickPackaging
            )
        }
    }
}
```
- LocalDensity.current 를 적용해서 toPx, toDp 로 변환