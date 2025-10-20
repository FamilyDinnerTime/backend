package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.request.AddDishToVotingRequest
import ru.kreslavski.family.dinnertime.dto.request.CreateMenuVotingRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveDishFromVotingRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateMenuVotingRequest
import ru.kreslavski.family.dinnertime.dto.response.MenuVotingDto
import ru.kreslavski.family.dinnertime.dto.response.VotingOptionDto
import ru.kreslavski.family.dinnertime.exception.DishAlreadyInVotingException
import ru.kreslavski.family.dinnertime.exception.MenuVotingNotFoundException
import ru.kreslavski.family.dinnertime.exception.UnauthorizedAccessException
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.MenuVotingRecord
import ru.kreslavski.family.dinnertime.repository.MenuVotingRepository
import ru.kreslavski.family.dinnertime.repository.VotingOptionRepository
import java.util.UUID

@Service
class MenuVotingService(
    private val menuVotingRepository: MenuVotingRepository,
    private val votingOptionRepository: VotingOptionRepository,
) {

    fun createMenuVoting(request: CreateMenuVotingRequest, createdBy: UUID): MenuVotingDto {
        val record = menuVotingRepository.createMenuVoting(request.name, createdBy)
        return record.toDto()
    }

    fun findMenuVotingsByName(name: String, userId: UUID): List<MenuVotingDto> {
        // Only return votings from groups where the user participates
        return menuVotingRepository.findByNameLikeAndUserGroups(name, userId).map { it.toDto() }
    }

    fun findMenuVotingsByUser(userId: UUID): List<MenuVotingDto> {
        return menuVotingRepository.findByCreatedBy(userId).map { it.toDto() }
    }

    fun findMenuVotingsFromUserGroups(userId: UUID): List<MenuVotingDto> {
        return menuVotingRepository.findByUserGroups(userId).map { it.toDto() }
    }

    fun findMenuVotingById(id: UUID): MenuVotingDto {
        val record = menuVotingRepository.findById(id) ?: throw MenuVotingNotFoundException(id)
        return record.toDto()
    }

    fun updateMenuVoting(id: UUID, request: UpdateMenuVotingRequest, userId: UUID): MenuVotingDto {
        val existingVoting = menuVotingRepository.findById(id) ?: throw MenuVotingNotFoundException(id)

        // Only creator can update the voting
        if (existingVoting.createdBy != userId) {
            throw UnauthorizedAccessException("You can only update votings you created")
        }

        val record = menuVotingRepository.updateMenuVoting(id, request.name)
            ?: throw MenuVotingNotFoundException(id)
        return record.toDto()
    }

    fun deleteMenuVoting(id: UUID, userId: UUID) {
        val existingVoting = menuVotingRepository.findById(id) ?: throw MenuVotingNotFoundException(id)

        // Only creator can delete the voting
        if (existingVoting.createdBy != userId) {
            throw UnauthorizedAccessException("You can only delete votings you created")
        }

        val deleted = menuVotingRepository.deleteMenuVoting(id)
        if (deleted == 0) {
            throw MenuVotingNotFoundException(id)
        }
    }

    fun addDishToVoting(votingId: UUID, request: AddDishToVotingRequest, userId: UUID): VotingOptionDto {
        val existingVoting = menuVotingRepository.findById(votingId) ?: throw MenuVotingNotFoundException(votingId)

        // Only creator can add dishes to voting
        if (existingVoting.createdBy != userId) {
            throw UnauthorizedAccessException("You can only add dishes to votings you created")
        }

        // Check if dish is already in the voting
        if (votingOptionRepository.isDishInVoting(votingId, request.dishId)) {
            throw DishAlreadyInVotingException(votingId, request.dishId)
        }

        val record = votingOptionRepository.addDishToVoting(votingId, request.dishId)
        return record.toDto()
    }

    fun removeDishFromVoting(votingId: UUID, request: RemoveDishFromVotingRequest, userId: UUID) {
        val existingVoting = menuVotingRepository.findById(votingId) ?: throw MenuVotingNotFoundException(votingId)

        // Only creator can remove dishes from voting
        if (existingVoting.createdBy != userId) {
            throw UnauthorizedAccessException("You can only remove dishes from votings you created")
        }

        val deleted = votingOptionRepository.removeDishFromVoting(votingId, request.dishId)
        if (deleted == 0) {
            throw MenuVotingNotFoundException(votingId)
        }
    }

    fun getVotingOptions(votingId: UUID): List<VotingOptionDto> {
        // Check if voting exists
        menuVotingRepository.findById(votingId) ?: throw MenuVotingNotFoundException(votingId)
        // Note: menu_id in voting_option table actually refers to menu_voting.guid
        return votingOptionRepository.findByMenuId(votingId).map { it.toDto() }
    }

    private fun MenuVotingRecord.toDto(): MenuVotingDto = MenuVotingDto(
        guid = this.guid,
        name = this.name,
        createdBy = this.createdBy,
        createdAt = this.createdAt
    )
}
