package com.tgyuu.feature.storage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.FWheelPickerState
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import com.tgyuu.designsystem.R.drawable
import com.tgyuu.designsystem.component.BaekyoungDialog
import com.tgyuu.designsystem.component.BaekyoungModalBottomSheet
import com.tgyuu.designsystem.component.BaekyoungTopBar
import com.tgyuu.designsystem.theme.BaekyoungTheme
import com.tgyuu.model.storage.ChattingRoom
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun StorageRoute(
    navigateToChatting: (String) -> Unit,
    viewModel: StorageViewModel = hiltViewModel(),
) {
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val chattingRooms by viewModel.chattingLogs.collectAsStateWithLifecycle()
    val yearPickerState = rememberFWheelPickerState()
    val (showChatLogDeleteDialog, setChatLogDeleteDialog) = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.getAllChattingLogs()

        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is StorageViewModel.StorageEvent.DeleteSuccess -> setChatLogDeleteDialog(false)
                is StorageViewModel.StorageEvent.EventFailed ->
                    snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    StorageScreen(
        snackbarHostState = snackbarHostState,
        selectedYear = selectedYear,
        chattingRooms = chattingRooms,
        yearPickerState = yearPickerState,
        showChatLogDeleteDialog = showChatLogDeleteDialog,
        setChatLogDeleteDialog = setChatLogDeleteDialog,
        deleteChattingRoom = viewModel::deleteChattingRoom,
        navigateToChatting = navigateToChatting,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StorageScreen(
    snackbarHostState: SnackbarHostState,
    selectedYear: String,
    chattingRooms: List<ChattingRoom>,
    yearPickerState: FWheelPickerState,
    showChatLogDeleteDialog: Boolean,
    setChatLogDeleteDialog: (Boolean) -> Unit,
    deleteChattingRoom: (String) -> Unit,
    navigateToChatting: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedRoomId by remember { mutableStateOf("") }

    if (showChatLogDeleteDialog) {
        BaekyoungDialog(
            onDismissRequest = { setChatLogDeleteDialog(false) },
            title = stringResource(id = R.string.delete_chatlog_dialog_title),
            description = stringResource(id = R.string.delete_chatlog_dialog_description),
            leftButtonText = stringResource(R.string.cancel),
            rightButtonText = stringResource(R.string.complete),
            onLeftButtonClick = { setChatLogDeleteDialog(false) },
            onRightButtonClick = { deleteChattingRoom(selectedRoomId) },
        )
    }

    Scaffold(
        topBar = { BaekyoungTopBar(titleTextId = R.string.storage) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BaekyoungTheme.colors.grayF5,
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        if (showBottomSheet) {
            BaekyoungModalBottomSheet(
                sheetState = sheetState,
                onDissmissRequest = { showBottomSheet = false },
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(id = R.string.change_year_bottom_sheet_title),
                        style = BaekyoungTheme.typography.labelBold.copy(
                            fontSize = 14.sp,
                        ),
                        color = BaekyoungTheme.colors.black,
                        modifier = Modifier.padding(top = 20.dp),
                    )

                    FVerticalWheelPicker(
                        modifier = Modifier.width(70.dp),
                        count = 1,
                        state = yearPickerState,
                    ) { year ->
                        Text(
                            text = "${year + 2024} 년",
                            style = BaekyoungTheme.typography.labelBold.copy(
                                fontSize = 14.sp,
                            ),
                            color = BaekyoungTheme.colors.black,
                        )
                    }

                    Button(
                        onClick = { showBottomSheet = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BaekyoungTheme.colors.gray95,
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.padding(
                            bottom = 20.dp,
                            start = 20.dp,
                            end = 20.dp,
                        ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.complete),
                            style = BaekyoungTheme.typography.labelBold,
                            color = BaekyoungTheme.colors.white,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)
                                .padding(vertical = 8.dp),
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            HorizontalDivider(color = BaekyoungTheme.colors.grayDC)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    .clickable { showBottomSheet = !showBottomSheet },
            ) {
                Text(
                    text = "$selectedYear 년",
                    style = BaekyoungTheme.typography.labelBold.copy(fontSize = 13.sp),
                    color = BaekyoungTheme.colors.gray95,
                )

                Image(
                    painter = painterResource(id = drawable.ic_spinner_arrow),
                    contentDescription = null,
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
            ) {
                items(chattingRooms) { chattingRoom ->
                    Column(
                        modifier = Modifier
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(BaekyoungTheme.colors.white)
                            .clickable { navigateToChatting(chattingRoom.id) },
                    ) {
                        Column(modifier = Modifier.padding(2.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(horizontal = 20.dp, vertical = 15.dp),
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(0.7f),
                                ) {
                                    Text(
                                        text = chattingRoom.getFormattedUpdateTime(),
                                        style = BaekyoungTheme.typography.labelRegular.copy(fontSize = 10.sp),
                                        color = BaekyoungTheme.colors.gray95,
                                        textAlign = TextAlign.Center,
                                    )

                                    Text(
                                        text = chattingRoom.lastMessage + "\n\n",
                                        style = BaekyoungTheme.typography.labelBold,
                                        color = BaekyoungTheme.colors.black,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }

                                Spacer(modifier = Modifier.weight(0.2f))

                                Image(
                                    painter = painterResource(id = R.drawable.ic_trash_can),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .clickable {
                                            selectedRoomId = chattingRoom.id
                                            setChatLogDeleteDialog(true)
                                        },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
