package com.tgyuu.baekyoung_i.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tgyuu.baekyoung_i.R
import com.tgyuu.baekyoung_i.auth.signup.component.SignUpTextField
import com.tgyuu.designsystem.component.BaekyoungButton
import com.tgyuu.designsystem.theme.BaekyoungTheme

@Composable
internal fun SignUpRoute(navigateToHome: () -> Unit) {
    SignUpScreen(
        navigateToHome = navigateToHome,
    )
}

@Composable
internal fun SignUpScreen(navigateToHome: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaekyoungTheme.colors.blueF5FF)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 60.dp),
        ) {
            SignUpTextField(
                title = R.string.nickname,
                hint = R.string.nickname_hint,
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.sex),
                style = BaekyoungTheme.typography.contentBold,
                color = BaekyoungTheme.colors.black56,
                modifier = Modifier.padding(start = 5.dp),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = BaekyoungTheme.colors.white,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = BaekyoungTheme.colors.grayD0,
                        shape = RoundedCornerShape(10.dp),
                    )
            ) {
                Text(
                    text = "남성",
                    style = BaekyoungTheme.typography.labelRegular,
                    color = BaekyoungTheme.colors.black56,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

            }
        }

        BaekyoungButton(
            text = R.string.confirm,
            onButtonClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        )
    }
}