package leets.weeth.domain.board.application.exception;

import leets.weeth.global.common.exception.BusinessLogicException;

public class CategoryAccessDeniedException extends BusinessLogicException {
  public CategoryAccessDeniedException() {
    super(403, "어드민 유저만 접근 가능한 카테고리입니다");
  }
}
