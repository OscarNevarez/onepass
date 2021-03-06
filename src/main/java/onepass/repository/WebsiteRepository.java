package onepass.repository;

import onepass.domain.Website;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Website entity.
 */
@SuppressWarnings("unused")
public interface WebsiteRepository extends JpaRepository<Website,Long> {

}
