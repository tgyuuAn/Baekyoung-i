package com.tgyuu.baekyoung_i.consulting.chatting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pknu.model.consulting.ChattingRole
import com.pknu.model.consulting.ConsultingChatting
import com.tgyuu.baekyoung_i.R
import com.tgyuu.baekyoung_i.main.navigation.TopLevelDestination
import com.tgyuu.common.util.UiState
import com.tgyuu.designsystem.component.BaekyoungTopAppBar
import com.tgyuu.designsystem.component.Loader
import com.tgyuu.designsystem.theme.BaekyoungTheme

@Composable
internal fun ChattingRoute(viewModel: ChattingViewModel = hiltViewModel()) {
    val chat by viewModel.chat.collectAsStateWithLifecycle()
    val userInput by viewModel.userInput.collectAsStateWithLifecycle()

    ChattingScreen(
        chat,
        userInput,
        viewModel::setUserInput
    )
}

@Composable
internal fun ChattingScreen(
    chat: UiState<List<ConsultingChatting>>,
    userInput: String,
    onUserInputChanged: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BaekyoungTheme.colors.blueEFF)
    ) {
        if (chat is UiState.Loading) {
            Loader()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            BaekyoungTopAppBar(TopLevelDestination.CONSULTING.titleTextId)

            if (chat is UiState.Success) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(chat.data, key = null) { chat ->
                        when (chat.role) {
                            ChattingRole.SYSTEM -> {}
                            ChattingRole.USER -> UserChatting(chat = chat)
                            ChattingRole.ASSISTANT -> AssistantChatting(chat = chat)
                        }
                    }
                }
            }

            BasicTextField(
                value = userInput,
                onValueChange = onUserInputChanged,
                textStyle = BaekyoungTheme.typography.contentNormal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }

    }
}

@Composable
fun UserChatting(chat: ConsultingChatting) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 15.dp)
            .padding(start = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(BaekyoungTheme.colors.white)
            ) {
                Text(
                    text = chat.content,
                    style = BaekyoungTheme.typography.contentNormal,
                    modifier = Modifier.padding(horizontal = 10.dp),
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.padding(horizontal = 10.dp),
        )
    }
}

@Composable
fun AssistantChatting(chat: ConsultingChatting) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 15.dp, horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baekgyoung_face),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(52.dp, 52.dp)
        )

        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(BaekyoungTheme.colors.white)
        ) {
            Text(
                text = chat.content,
                style = BaekyoungTheme.typography.contentNormal,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
        }
    }
}