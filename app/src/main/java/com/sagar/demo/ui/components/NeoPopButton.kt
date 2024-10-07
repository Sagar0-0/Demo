package com.sagar.demo.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sagar.demo.ui.theme.DemoTheme

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    DemoTheme {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                NeoPopButton(
                    content = {
                        Text(
                            text = "Pay Now",
                            color = Color.White,
                        )
                    }
                )
                NeoPopButton(
                    content = {
                        Text(
                            text = "Pay later",
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )

                    }
                )
            }
            Height(value = 30)
        }
    }
}

@Composable
fun NeoPopButton(
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 30.dp,
    buttonWidth: Dp = 90.dp,
    buttonDropHeight: Dp = 3.dp,
    buttonColor: Color = Color.Black,
    buttonStrokeColor: Color = Color.Green,
    buttonRightColor: Color = Color(0xFF6EB11C),
    buttonBottomColor: Color = Color(0xFF3D6011),
    buttonStrokeWidth: Dp = 0.3.dp,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val offset by animateDpAsState(
        targetValue = if (pressed) buttonDropHeight else 0.dp, label = ""
    )

    val sideHeight by animateDpAsState(
        targetValue = if (pressed) 0.dp else buttonDropHeight, label = ""
    )

    Box(
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) {
                onClick()
            }
            .drawWithCache {
                onDrawWithContent {
                    //Box
                    drawRect(
                        color = buttonColor,
                        topLeft = Offset(x = offset.toPx(), y = offset.toPx()),
                        size = Size(width = buttonWidth.toPx(), height = buttonHeight.toPx()),
                    )
                    //Stroke
                    drawRect(
                        color = buttonStrokeColor,
                        topLeft = Offset(x = offset.toPx(), y = offset.toPx()),
                        size = Size(width = buttonWidth.toPx(), height = buttonHeight.toPx()),
                        style = Stroke(buttonStrokeWidth.toPx())
                    )

                    val rightPath = Path()
                    rightPath.moveTo(x = offset.toPx() + buttonWidth.toPx(), y = offset.toPx())
                    rightPath.lineTo(
                        x = offset.toPx() + buttonWidth.toPx() + sideHeight.toPx(),
                        y = offset.toPx() + sideHeight.toPx()
                    )
                    rightPath.lineTo(
                        x = buttonWidth.toPx() + offset.toPx() + sideHeight.toPx(),
                        y = buttonHeight.toPx() + offset.toPx() + sideHeight.toPx()
                    )
                    rightPath.lineTo(
                        x = buttonWidth.toPx() + offset.toPx(),
                        y = buttonHeight.toPx() + offset.toPx()
                    )
                    rightPath.close()
                    drawPath(
                        path = rightPath, color = buttonRightColor
                    )


                    val bottomPath = Path()
                    bottomPath.moveTo(x = offset.toPx(), y = offset.toPx() + buttonHeight.toPx())
                    bottomPath.lineTo(
                        x = offset.toPx() + sideHeight.toPx(),
                        y = offset.toPx() + buttonHeight.toPx() + sideHeight.toPx()
                    )
                    bottomPath.lineTo(
                        x = buttonWidth.toPx() + offset.toPx() + sideHeight.toPx(),
                        y = buttonHeight.toPx() + offset.toPx() + sideHeight.toPx()
                    )
                    bottomPath.lineTo(
                        x = buttonWidth.toPx() + offset.toPx(),
                        y = buttonHeight.toPx() + offset.toPx()
                    )
                    bottomPath.close()
                    drawPath(
                        path = bottomPath,
                        color = buttonBottomColor
                    )

                    drawContent()
                }
            }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .offset {
                    IntOffset(x = offset.toPx().toInt(), y = offset.toPx().toInt())
                }
        ) {
            content()
        }
    }

}