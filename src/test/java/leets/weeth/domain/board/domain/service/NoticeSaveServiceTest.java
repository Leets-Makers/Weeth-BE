package leets.weeth.domain.board.domain.service;

import leets.weeth.config.TestContainersConfig;
import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.board.domain.repository.NoticeRepository;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeSaveServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    NoticeRepository noticeRepository;



    @Test
    void save() {
        // given
        User user = userRepository.save(User.builder()
                .email("abc@test.com")
                .name("홍길동")
                .position(Position.BE)
                .department(Department.SW)
                .role(Role.USER)
                .build());

        Notice notice = Notice.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();

        // when
        Notice savedNotice = noticeRepository.save(notice);

        // then
        assertThat(noticeRepository.findAll()).hasSize(1);
        assertThat(savedNotice.getTitle()).isEqualTo(notice.getTitle());
        assertThat(savedNotice.getContent()).isEqualTo(notice.getContent());
        assertThat(savedNotice.getUser().getId()).isEqualTo(user.getId());
    }
}
