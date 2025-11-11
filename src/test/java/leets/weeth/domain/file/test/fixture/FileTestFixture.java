package leets.weeth.domain.file.test.fixture;

import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.file.domain.entity.File;

public class FileTestFixture {
    public static File createFile(Long id, String fileName, String fileUrl, Post post){
        return File.builder()
                .id(id)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .post(post)
                .build();
    }
}
