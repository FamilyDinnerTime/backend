package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.response.VotingOptionDto
import ru.kreslavski.family.dinnertime.exception.VotingOptionNotFoundException
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.VotingOptionRecord
import ru.kreslavski.family.dinnertime.repository.VotingOptionRepository
import java.util.UUID

@Service
class VotingOptionService(
    private val votingOptionRepository: VotingOptionRepository,
) {

    fun getVotingOptionsByMenu(menuId: UUID): List<VotingOptionDto> {
        return votingOptionRepository.findByMenuId(menuId).map { it.toDto() }
    }

    fun getVotingOptionById(id: UUID): VotingOptionDto {
        val record = votingOptionRepository.findById(id) ?: throw VotingOptionNotFoundException(id)
        return record.toDto()
    }

    fun removeVotingOption(id: UUID): Int {
        return votingOptionRepository.removeVotingOption(id)
    }
}

fun VotingOptionRecord.toDto(): VotingOptionDto = VotingOptionDto(
    guid = this.guid,
    menuId = this.menuId,
    dishId = this.dishId
)
