package com.skillswap.repository;

import com.skillswap.entity.LearningRequest;
import com.skillswap.entity.LearningRequest.RequestStatus;
import com.skillswap.entity.LearningRequest.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LearningRequestRepository – data access for skill exchange and mentoring requests.
 *
 * Key query methods and their purposes:
 *
 * findByReceiverId / findByRequesterId:
 *   Powers the "Incoming Requests" and "Sent Requests" sections on the dashboard.
 *
 * existsByRequesterIdAndReceiverIdAndSkillToLearnIdAndStatusIn:
 *   Prevents duplicate pending requests.
 *   Before creating a new request, we check if a PENDING or ACCEPTED request
 *   for the same skill already exists between these two students.
 *
 * findByReceiverIdAndStatus:
 *   Shows only PENDING requests in the notification badge.
 *
 * findByRequesterIdOrReceiverIdAndStatus:
 *   Finds all ACCEPTED exchanges/sessions for the dashboard's "Active" section.
 */
@Repository
public interface LearningRequestRepository extends JpaRepository<LearningRequest, Long> {

    // Dashboard: incoming requests (I am the receiver)
    List<LearningRequest> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    // Dashboard: sent requests (I am the requester)
    List<LearningRequest> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);

    // Dashboard: filter by receiver + status
    List<LearningRequest> findByReceiverIdAndStatus(Long receiverId, RequestStatus status);

    // Dashboard: filter by requester + status
    List<LearningRequest> findByRequesterIdAndStatus(Long requesterId, RequestStatus status);

    // Active sessions: where I am requester or receiver AND status = ACCEPTED
    List<LearningRequest> findByRequesterIdAndStatusOrReceiverIdAndStatus(
            Long requesterId, RequestStatus status1,
            Long receiverId, RequestStatus status2
    );


    /**
     * Duplicate-request prevention:
     * Returns true if there is already a PENDING or ACCEPTED request
     * from the same requester, to the same receiver, for the same skill.
     */
    boolean existsByRequesterIdAndReceiverIdAndSkillToLearnIdAndStatusIn(
            Long requesterId,
            Long receiverId,
            Long skillToLearnId,
            List<RequestStatus> statuses
    );

    // Dashboard stats: count by receiver + status
    long countByReceiverIdAndStatus(Long receiverId, RequestStatus status);

    // Dashboard stats: count by requester + status
    long countByRequesterIdAndStatus(Long requesterId, RequestStatus status);

    // Dashboard stats: requests by type
    List<LearningRequest> findByReceiverIdAndRequestType(Long receiverId, RequestType requestType);
    List<LearningRequest> findByRequesterIdAndRequestType(Long requesterId, RequestType requestType);
}
