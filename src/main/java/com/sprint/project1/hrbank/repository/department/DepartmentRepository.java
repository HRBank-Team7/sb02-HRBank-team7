package com.sprint.project1.hrbank.repository.department;

import com.sprint.project1.hrbank.entity.department.Department;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY d.name ASC
    """)
    List<Department> findFirstPageByNameAsc(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
       AND d.name > :cursorName
    ORDER BY d.name ASC
    """)
    List<Department> findNextPageByNameAsc(@Param("keyword") String keyword, @Param("cursorName") String cursorName, Pageable pageable);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY d.name DESC 
    """)
    List<Department> findFirstPageByNameDesc(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
       AND d.name < :cursorName
    ORDER BY d.name Desc
    """)
    List<Department> findNextPageByNameDesc(@Param("keyword") String keyword, @Param("cursorName") String cursorName, Pageable pageable);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY d.establishedDate ASC
    """)
    List<Department> findFirstPageByEstablishedDateAsc(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
       AND d.establishedDate > :cursorDate
    ORDER BY d.establishedDate ASC
    """)
    List<Department> findNextPageByEstablishedDateAsc(@Param("keyword") String keyword, @Param("cursorDate") LocalDate cursorDate, Pageable pageable);


    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY d.establishedDate DESC
    """)
    List<Department> findFirstPageByEstablishedDateDesc(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT d FROM Department d
    WHERE 
      (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
      AND d.establishedDate < :cursorDate
    ORDER BY d.establishedDate DESC
    """)
    List<Department> findNextPageByEstablishedDateDesc(@Param("keyword") String keyword, @Param("cursorDate") LocalDate cursorDate, Pageable pageable);

    Long countByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameKeyword, String descriptionKeyword);

    @Query("""
    SELECT e.department.id, COUNT(e.id)
    FROM Employee e 
    WHERE e.department.id IN :departmentIds
    GROUP BY e.department.id
    """)
    List<Object[]> countEmployeesByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);







    
}
