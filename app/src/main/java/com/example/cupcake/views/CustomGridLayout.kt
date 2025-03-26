package com.example.cupcake.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import kotlin.random.Random

@Composable
fun CustomGridLayout(
    modifier: Modifier = Modifier,
    columnsCount: Int,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }

            val groupedPlaceables = mutableListOf<List<Placeable>>()
            var currentGroup = mutableListOf<Placeable>()
            var currentGroupWidth = 0

            placeables.forEach { placeable ->
                if (currentGroupWidth + placeable.width <= constraints.maxWidth && currentGroup.size < columnsCount) {
                    currentGroup.add(placeable)
                    currentGroupWidth += placeable.width
                } else {
                    groupedPlaceables.add(currentGroup)
                    currentGroup = mutableListOf(placeable)
                    currentGroupWidth = placeable.width
                }
            }

            if (currentGroup.isNotEmpty()) {
                groupedPlaceables.add(currentGroup)
            }

            val totalHeight = groupedPlaceables.sumOf { row -> row.maxOfOrNull { it.height } ?: 0 }

            layout(
                width = constraints.maxWidth,
                height = totalHeight
            ) {
                var yPosition = 0
                groupedPlaceables.forEach { row ->
                    var xPosition = 0
                    val rowHeight = row.maxOfOrNull { it.height } ?: 0
                    row.forEach { placeable ->
                        placeable.place(x = xPosition, y = yPosition)
                        xPosition += placeable.width
                    }
                    yPosition += rowHeight
                }
            }
        },
        content = content
    )
}

@Preview
@Composable
fun CustomGridLayoutPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CustomGridLayout(
            modifier = Modifier,
            columnsCount = 3
        ) {
            repeat(50) {
                Box(
                    modifier = Modifier
                        .width(Random.nextInt(50, 200).dp)
                        .height(Random.nextInt(50, 200).dp)
                        .background(Color(Random.nextLong(0xFFFFFFFF)))
                ) {
                    Image(
                        painter = painterResource(R.drawable.cupcake),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}