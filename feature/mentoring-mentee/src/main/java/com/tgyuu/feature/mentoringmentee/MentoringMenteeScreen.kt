package com.tgyuu.feature.mentoringmentee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tgyuu.designsystem.component.BaekyoungCenterTopBar
import com.tgyuu.designsystem.theme.BaekyoungTheme
import com.tgyuu.feature.mentoring.mentee.R

@Composable
internal fun MentoringMenteeRoute(
    navigateToFindMentor: () -> Unit,
    popBackStack: () -> Unit,
) {
    MentoringMenteeScreen(
        navigateToFindMentor = navigateToFindMentor,
        popBackStack = popBackStack,
    )
}

@Composable
fun MentoringMenteeScreen(
    navigateToFindMentor: () -> Unit,
    popBackStack: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = BaekyoungTheme.colors.grayF5,
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BaekyoungTheme.colors.white),
        ) {
            BaekyoungCenterTopBar(
                titleTextId = R.string.chatting_room,
                showBackButton = true,
                onClickBackButton = popBackStack,
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                item {
                    Text(
                        text = "진행중인 채팅방",
                        style = BaekyoungTheme.typography.contentBold,
                        color = BaekyoungTheme.colors.black,
                    )
                }

                items(listOf("종디기", "안태규")) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = BaekyoungTheme.colors.white),
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = BaekyoungTheme.colors.grayDC,
                            ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 15.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_user_default),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    Text(
                                        text = it,
                                        style = BaekyoungTheme.typography.contentBold,
                                        color = BaekyoungTheme.colors.black,
                                    )

                                    Text(
                                        text = "멘토",
                                        style = BaekyoungTheme.typography.labelRegular,
                                        color = BaekyoungTheme.colors.gray95,
                                    )
                                }

                                Text(
                                    text = "안녕하세요",
                                    style = BaekyoungTheme.typography.labelRegular,
                                    color = BaekyoungTheme.colors.gray95,
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxHeight(),
                            ) {
                                Text(
                                    text = "오후 6:29",
                                    style = BaekyoungTheme.typography.labelRegular.copy(fontSize = 10.sp),
                                    color = BaekyoungTheme.colors.gray95,
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(30.dp))
                                        .background(BaekyoungTheme.colors.red),
                                ) {
                                    Text(
                                        text = "99+",
                                        style = BaekyoungTheme.typography.labelBold,
                                        color = BaekyoungTheme.colors.white,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(horizontal = 8.dp, vertical = 1.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { navigateToFindMentor() }
                    .padding(20.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_find_mentor),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Text(
                    text = "멘토 찾으러 가기",
                    style = BaekyoungTheme.typography.labelRegular,
                    color = BaekyoungTheme.colors.black,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}