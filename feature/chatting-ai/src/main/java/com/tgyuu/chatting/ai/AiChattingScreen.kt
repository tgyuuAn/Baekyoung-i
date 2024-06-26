package com.tgyuu.chatting.ai

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgyuu.common.util.UiState
import com.tgyuu.common.util.addFocusCleaner
import com.tgyuu.designsystem.R.string
import com.tgyuu.designsystem.component.BaekyoungCenterTopBar
import com.tgyuu.designsystem.component.BaekyoungChatTextField
import com.tgyuu.designsystem.component.BaekyoungSpeechBubble
import com.tgyuu.designsystem.component.ChattingLoader
import com.tgyuu.designsystem.component.SpeechBubbleType
import com.tgyuu.designsystem.theme.BaekyoungTheme
import com.tgyuu.domain.usecase.chatting.SearchResult
import com.tgyuu.feature.chatting.ai.R
import com.tgyuu.model.chatting.AiMessage
import com.tgyuu.model.chatting.ChattingRole

@Composable
internal fun AiChattingRoute(
    roomId: String,
    popBackStack: () -> Unit,
    viewModel: AiChattingViewModel = hiltViewModel(),
) {
    val chatText by viewModel.chatText.collectAsStateWithLifecycle()
    val chatLog = viewModel.chatLog.toList()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val chatState by viewModel.chatState.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.setRoomId(roomId)
    }

    AiChattingScreen(
        chatText = chatText,
        searchText = searchText,
        chatLog = chatLog,
        chatState = chatState,
        searchResult = searchResult,
        searchMode = searchMode,
        onChatTextChanged = viewModel::setChatText,
        onSearchTextChanged = viewModel::setSearchText,
        postUserChatting = viewModel::postUserChatting,
        onSearchExecuted = viewModel::onSearchExecuted,
        setSearchMode = viewModel::setSearchMode,
        popBackStack = popBackStack,
    )
}

@Composable
internal fun AiChattingScreen(
    chatText: String,
    searchText: String,
    chatLog: List<AiMessage>,
    chatState: UiState<Unit>,
    searchResult: SearchResult,
    searchMode: Boolean,
    onChatTextChanged: (String) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchExecuted: (Int?) -> Unit,
    setSearchMode: (Boolean) -> Unit,
    postUserChatting: () -> Unit,
    popBackStack: () -> Unit,
) {
    var textFieldHeight by remember { mutableStateOf(0.dp) }
    var topBarHeight by remember { mutableStateOf(0.dp) }
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    var previousChatSize by remember { mutableStateOf(1) }
    var previousSearchResult: SearchResult by remember { mutableStateOf(SearchResult()) }
    val backgroundColor = Brush.verticalGradient(
        listOf(
            BaekyoungTheme.colors.blue4E,
            BaekyoungTheme.colors.blue4E.copy(alpha = 0.66f),
        ),
    )

    LaunchedEffect(searchResult) {
        if (searchResult.initialMatch != null) {
            listState.scrollToItem(searchResult.initialMatch?.first ?: (chatLog.size - 1))
            previousSearchResult = searchResult
            return@LaunchedEffect
        }
    }

    LaunchedEffect(previousChatSize != chatLog.size) {
        listState.animateScrollToItem(chatLog.size)
        previousChatSize = chatLog.size
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp)) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .addFocusCleaner(focusManager),
        ) {
            ConsultingChattingBackground()

            BaekyoungCenterTopBar(
                titleTextId = string.consulting,
                textColor = BaekyoungTheme.colors.white,
                showSearchButton = true,
                onSearchExcuted = { onSearchExecuted(it) },
                setSearchMode = {
                    setSearchMode(!searchMode)
                    if (searchMode) {
                        previousSearchResult = SearchResult()
                    }
                },
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                clearSearchText = { onSearchTextChanged("") },
                onClickBackButton = popBackStack,
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)

                    topBarHeight = placeable.height.toDp()

                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                },
            )

            Image(
                painter = painterResource(id = R.drawable.ic_ai_chat_background),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            )

            Image(
                painter = painterResource(id = R.drawable.ic_consulting_baekgyoung),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 20.dp, bottom = 80.dp),
            )

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 25.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topBarHeight, bottom = textFieldHeight + 20.dp),
            ) {
                itemsIndexed(items = chatLog) { idx, message ->
                    val speechBubbleType = when (message.role) {
                        ChattingRole.USER -> SpeechBubbleType.AI_USER
                        ChattingRole.ASSISTANT -> SpeechBubbleType.AI_CHAT
                        ChattingRole.SYSTEM, ChattingRole.FUNCTION -> return@itemsIndexed
                    }

                    val styledText = if (searchResult.initialMatch?.first == idx) {
                        highlightSearchResults(message.content, searchResult.initialMatch!!.second)
                    } else {
                        AnnotatedString(message.content)
                    }

                    BaekyoungSpeechBubble(
                        type = speechBubbleType,
                        text = styledText,
                    )
                }
            }

            when (chatState) {
                is UiState.Loading -> ChattingLoader(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 135.dp),
                )

                else -> Unit
            }

            BaekyoungChatTextField(
                chatText = chatText,
                onTextChanged = onChatTextChanged,
                sendMessage = postUserChatting,
                textColor = BaekyoungTheme.colors.black,
                searchMode = searchMode,
                searchResult = searchResult,
                onSearchExecuted = { index -> onSearchExecuted(index) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)

                        textFieldHeight = placeable.height.toDp() + 16.dp

                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
            )
        }
    }
}

fun highlightSearchResults(text: String, ranges: List<IntRange>): AnnotatedString {
    return buildAnnotatedString {
        ranges.forEach { range ->
            append(text.substring(0, range.first))
            withStyle(style = SpanStyle(background = Color.Black, color = Color.White)) {
                append(text.substring(range.first, range.last + 1))
            }
            append(text.substring(range.last + 1))
        }
    }
}

@Composable
private fun ConsultingChattingBackground() {
    val localConfiguration = LocalConfiguration.current

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(100.dp)
            .offset(
                x = localConfiguration.screenWidthDp.dp * 5 / 8,
                y = -localConfiguration.screenHeightDp.dp * 9 / 20,
            ),
    ) {
        drawCircle(
            color = Color(0xFF6CEDFF),
            radius = size.width * 2 / 3,
            alpha = 0.4F,
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(100.dp)
            .offset(
                x = -localConfiguration.screenWidthDp.dp * 3 / 7,
                y = localConfiguration.screenHeightDp.dp * 1 / 9,
            ),
    ) {
        drawCircle(
            color = Color(0xFF6CEDFF),
            radius = size.width * 1 / 5,
            alpha = 0.4F,
        )
    }
}
