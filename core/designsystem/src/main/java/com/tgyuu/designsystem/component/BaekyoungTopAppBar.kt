package com.tgyuu.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tgyuu.designsystem.theme.BaekyoungTheme

@Composable
fun BaekyoungTopAppBar(
    @StringRes titleTextId: Int,
    textColor: Color = BaekyoungTheme.colors.black,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(BaekyoungTheme.colors.white)
    ) {
        Text(
            text = stringResource(titleTextId),
            style = BaekyoungTheme.typography.contentBig,
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier.padding(start = 20.dp)
        )
        Divider(
            color = BaekyoungTheme.colors.blueFF,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}