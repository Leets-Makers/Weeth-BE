package leets.weeth.domain.board.application.exception;

import leets.weeth.global.common.exception.BusinessLogicException;

public class NoSearchResultException extends BusinessLogicException {
	public NoSearchResultException(){
		super(404, "일치하는 검색 결과를 찾을 수 없습니다.");
	}
}
