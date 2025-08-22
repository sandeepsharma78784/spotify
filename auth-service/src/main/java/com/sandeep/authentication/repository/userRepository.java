package com.sandeep.authentication.repository;

import com.sandeep.authentication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ===== Basic lookups / existence =====
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    long countByRole(String role);
    long countBySubscription(String subscription);

    // ===== Offset pagination (PageRequest.of(page,size,Sort)) =====
    Page<User> findAll(Pageable pageable);

    Page<User> findAllByRole(String role, Pageable pageable);

    Page<User> findAllBySubscription(String subscription, Pageable pageable);

    // search (username OR email, case-insensitive) + pagination
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String usernamePart, String emailPart, Pageable pageable
    );

    // ===== Cursor (Keyset) pagination – stable with new inserts =====
    // Sort assumption: createdAt DESC, id DESC (tie-breaker)
    // "Next page" after a cursor (older items)
    @Query("""
           SELECT u FROM User u
           WHERE (u.createdAt < :createdAt)
              OR (u.createdAt = :createdAt AND u.id < :id)
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findNextPageByCreatedAtDesc(
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable // use PageRequest.of(0, size) with sort by createdAt desc, id desc
    );

    // "Previous page" before a cursor (newer items)
    @Query("""
           SELECT u FROM User u
           WHERE (u.createdAt > :createdAt)
              OR (u.createdAt = :createdAt AND u.id > :id)
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findPrevPageByCreatedAtDesc(
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable
    );

    // ===== Cursor pagination with ROLE filter =====
    @Query("""
           SELECT u FROM User u
           WHERE u.role = :role
             AND (u.createdAt < :createdAt
                  OR (u.createdAt = :createdAt AND u.id < :id))
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findNextPageByRoleAndCreatedAtDesc(
            @Param("role") String role,
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable
    );

    @Query("""
           SELECT u FROM User u
           WHERE u.role = :role
             AND (u.createdAt > :createdAt
                  OR (u.createdAt = :createdAt AND u.id > :id))
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findPrevPageByRoleAndCreatedAtDesc(
            @Param("role") String role,
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable
    );

    // ===== Cursor pagination with SUBSCRIPTION filter =====
    @Query("""
           SELECT u FROM User u
           WHERE u.subscription = :subscription
             AND (u.createdAt < :createdAt
                  OR (u.createdAt = :createdAt AND u.id < :id))
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findNextPageBySubscriptionAndCreatedAtDesc(
            @Param("subscription") String subscription,
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable
    );

    @Query("""
           SELECT u FROM User u
           WHERE u.subscription = :subscription
             AND (u.createdAt > :createdAt
                  OR (u.createdAt = :createdAt AND u.id > :id))
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findPrevPageBySubscriptionAndCreatedAtDesc(
            @Param("subscription") String subscription,
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable
    );

    // ===== Optional: cursor + free-text search (username/email) =====
    @Query("""
           SELECT u FROM User u
           WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')))
             AND (u.createdAt < :createdAt
                  OR (u.createdAt = :createdAt AND u.id < :id))
           ORDER BY u.createdAt DESC, u.id DESC
           """)
    List<User> findNextPageSearch(
            @Param("q") String query,
            @Param("createdAt") LocalDateTime createdAtCursor,
            @Param("id") Long idCursor,
            Pageable pageable
    );
}
