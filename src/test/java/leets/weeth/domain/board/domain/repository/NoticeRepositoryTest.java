package leets.weeth.domain.board.domain.repository;

import leets.weeth.config.TestContainersConfig;
import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Department;
import leets.weeth.domain.user.domain.entity.enums.Position;
import leets.weeth.domain.user.domain.entity.enums.Role;
import leets.weeth.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void findPageBy() {
        // given
        User user = userRepository.save(User.builder()
                .email("abc@test.com")
                .name("홍길동")
                .position(Position.BE)
                .department(Department.SW)
                .role(Role.USER)
                .build());

        Notice notice1 = noticeRepository.save(Notice.builder()
                .title("제목1")
                .content("내용1")
                .user(user)
                .build());

        Notice notice2 = noticeRepository.save(Notice.builder()
                .title("제목2")
                .content("내용2")
                .user(user)
                .build());

        Notice notice3 = noticeRepository.save(Notice.builder()
                .title("제목3")
                .content("내용3")
                .user(user)
                .build());

        Notice notice4 = noticeRepository.save(Notice.builder()
                .title("제목4")
                .content("내용4")
                .user(user)
                .build());

        Notice notice5 = noticeRepository.save(Notice.builder()
                .title("제목5")
                .content("내용5")
                .user(user)
                .build());

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));

        // when
        Slice<Notice> notices = noticeRepository.findPageBy(pageable);

        // then
        assertThat(notices.getSize()).isEqualTo(3);
        assertThat(notices.getContent().get(0).getTitle()).isEqualTo(notice5.getTitle());
        assertThat(notices.getContent().get(1).getTitle()).isEqualTo(notice4.getTitle());
        assertThat(notices.getContent().get(2).getTitle()).isEqualTo(notice3.getTitle());
        assertThat(notices.hasNext()).isEqualTo(true);
    }

    @Test
    void search() {
        // given
        User user = userRepository.save(User.builder()
                .email("abc@test.com")
                .name("홍길동")
                .position(Position.BE)
                .department(Department.SW)
                .role(Role.USER)
                .build());

        Notice notice1 = noticeRepository.save(Notice.builder()
                .title("검색1")
                .content("내용1")
                .user(user)
                .build());

        Notice notice2 = noticeRepository.save(Notice.builder()
                .title("제목2")
                .content("내용2")
                .user(user)
                .build());

        Notice notice3 = noticeRepository.save(Notice.builder()
                .title("검색3")
                .content("내용3")
                .user(user)
                .build());

        Notice notice4 = noticeRepository.save(Notice.builder()
                .title("제목4")
                .content("내용4")
                .user(user)
                .build());

        Notice notice5 = noticeRepository.save(Notice.builder()
                .title("검색5")
                .content("내용5")
                .user(user)
                .build());

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));

        // when
        Slice<Notice> searchedNotices = noticeRepository.search("검색", pageable);

        // then
        assertThat(searchedNotices.getSize()).isEqualTo(2);
        assertThat(searchedNotices.getContent().get(0).getTitle()).isEqualTo(notice5.getTitle());
        assertThat(searchedNotices.getContent().get(1).getTitle()).isEqualTo(notice3.getTitle());
        assertThat(searchedNotices.hasNext()).isEqualTo(true);
    }
}