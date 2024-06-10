package react.blog.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import react.blog.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
}
