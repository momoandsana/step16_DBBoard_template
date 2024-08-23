package app.mvc.service;

import java.util.List;

import app.mvc.dao.BoardDAO;
import app.mvc.dao.BoardDAOImpl;
import app.mvc.dto.BoardDTO;
import app.mvc.dto.ReplyDTO;
import app.mvc.exception.DMLException;
import app.mvc.exception.SearchWrongException;
/**
 * @author 
 * */
public class BoardServiceImpl implements BoardService {
	private static BoardService instance = new BoardServiceImpl();
	private BoardDAO boardDao = BoardDAOImpl.getInstance();
			
	private BoardServiceImpl() {}
    public static BoardService getInstance() {
		return instance;
	}
    
	@Override
	public List<BoardDTO> boardSelectAll() throws SearchWrongException {
		List<BoardDTO> list = boardDao.boardSelectAll();
		
		if(list.isEmpty())
			throw new SearchWrongException("검색된 레코드가 없습니다.");
		
		return list;
	}

	@Override
	public List<BoardDTO> boardSelectBySubject(String keyWord) throws SearchWrongException
	{
		// TODO Auto-generated method stub
		List<BoardDTO> list = boardDao.boardSelectBySubject(keyWord);

		if(list.isEmpty())throw new SearchWrongException("검색된 레코드가 없습니다");//리스트는 null이 아니라 isEmpty();
		return list;
	}

	@Override
	public BoardDTO boardSelectByNo(int boardNo) throws SearchWrongException
	{
		// TODO Auto-generated method stub
		BoardDTO board = boardDao.boardSelectByNo(boardNo);
		if(board == null)throw new SearchWrongException("검색된 레코드가 없습니다");
		return board;
	}

	@Override
	public void boardInsert(BoardDTO boardDTO) throws DMLException
	{
		int result = boardDao.boardInsert(boardDTO);
		if(result==0) throw new DMLException("등록되지 않았습니다.");

	}

	@Override
	public void boardUpdate(BoardDTO boardDTO) throws DMLException
	{
		// TODO Auto-generated method stub
		int result = boardDao.boardUpdate(boardDTO);
		if(result==0)throw new DMLException("업데이트가 되지 않았습니다");

	}

	@Override
	public void boardDelete(int boardNo) throws DMLException
	{
		int result=boardDao.boardDelete(boardNo);
		if(result==0)throw new DMLException("삭제되지 않았습니다");

	}

	@Override
	public void replyInsert(ReplyDTO replyDTO) throws DMLException
	{
		// TODO Auto-generated method stub
		int result=boardDao.replyInsert(replyDTO);
		if(result==0)throw new DMLException("댓글이 추가되었습니다");
	}

	@Override
	public BoardDTO replySelectByParentNo(int boardNo) throws SearchWrongException
	{
		BoardDTO boardDTO=boardDao.replySelectByParentNo(boardNo);
		if(boardDTO==null)throw new SearchWrongException("댓들 등록 실패");
		return boardDTO;
	}

}











