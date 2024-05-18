package com.tgyuu.domain.usecase.chatting

import com.tgyuu.domain.repository.chatting.MentoringChattingRepository
import com.tgyuu.model.chatting.JoinChat
import javax.inject.Inject

class GetRemoteAllChattingRoomUseCase @Inject constructor(
    private val mentoringChattingRepository: MentoringChattingRepository,
) {
    suspend operator fun invoke(userId: String): Result<List<JoinChat>> =
        mentoringChattingRepository.getAllMentorChattingRoom(userId)
}
