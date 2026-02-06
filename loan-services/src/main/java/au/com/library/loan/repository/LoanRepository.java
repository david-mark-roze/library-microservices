package au.com.library.loan.repository;

import au.com.library.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for managing Loan entities in the database.
 */
public interface LoanRepository extends JpaRepository<Loan,Long> {

    static final String ACTIVE_LOAN_FOR_COPY_QUERY = """
              select l from Loan l
              where l.editionCopyId = :editionCopyId
                and l.editionCopyId = l.openCopyId
                and l.status in (
                  :#{T(java.util.List).of(
                      T(au.com.library.loan.entity.LoanStatus).BORROWED,
                      T(au.com.library.loan.entity.LoanStatus).RENEWED
                  )}
                )
            """;

    /**
     * Finds an active loan for a given edition copy ID. An active loan is defined as a loan that is currently borrowed or renewed and has not yet been returned.
     * @param editionCopyId
     * @return An Optional containing the active Loan for the specified edition copy ID, or an empty Optional if no active loan exists for that copy.
     */
    @Query(ACTIVE_LOAN_FOR_COPY_QUERY)
    Optional<Loan> findActiveLoanForEditionCopy(@Param("editionCopyId") Long editionCopyId);
}
