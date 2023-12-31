package com.tgyuu.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tgyuu.designsystem.R

val gangwonBold: FontFamily =
    FontFamily(
        Font(
            resId = R.font.gangwon_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        )
    )

val gangwonLight: FontFamily =
    FontFamily(
        Font(
            resId = R.font.gangwon_light,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
        )
    )

val teunteunNormal: FontFamily =
    FontFamily(
        Font(
            resId = R.font.gangwon_teunteun,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        )
    )

val teunteunBold: FontFamily =
    FontFamily(
        Font(
            resId = R.font.gangwon_teunteun,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        )
    )

val notoSansKrBold: FontFamily =
    FontFamily(
        Font(
            resId = R.font.noto_sans_kr_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        )
    )

val notoSansKrRegular: FontFamily =
    FontFamily(
        Font(
            resId = R.font.noto_sans_kr_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        )
    )

val notoSansKrMedium: FontFamily =
    FontFamily(
        Font(
            resId = R.font.noto_sans_kr_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
        )
    )

@Stable
class BaekKyoungTypogrphy internal constructor(
    contentNormal: TextStyle,
    contentBig: TextStyle,
    titleNormal: TextStyle,
    labelNormal: TextStyle,
) {
    var contentBig: TextStyle by mutableStateOf(contentBig)
        private set
    var contentNormal: TextStyle by mutableStateOf(contentNormal)
        private set
    var titleNormal: TextStyle by mutableStateOf(titleNormal)
        private set
    var labelNormal: TextStyle by mutableStateOf(labelNormal)
        private set
}

@Composable
fun BaekKyoungTypoGraphy(): BaekKyoungTypogrphy {
    return BaekKyoungTypogrphy(
        contentNormal = TextStyle(
            fontSize = 16.sp,
            fontFamily = notoSansKrMedium,
        ),
        contentBig = TextStyle(
            fontSize = 20.sp,
            fontFamily = notoSansKrBold,
        ),
        titleNormal = TextStyle(
            fontSize = 24.sp,
            fontFamily = notoSansKrMedium,
        ),
        labelNormal = TextStyle(
            fontSize = 12.sp,
            fontFamily = notoSansKrMedium,
        )
    )
}