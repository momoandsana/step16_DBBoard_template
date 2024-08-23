package app.mvc.dao;

import java.lang.invoke.StringConcatException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.mvc.common.DBManager;
import app.mvc.dto.BoardDTO;
import app.mvc.dto.ReplyDTO;
import app.mvc.exception.DMLException;
import app.mvc.exception.SearchWrongException;

public class BoardDAOImpl implements BoardDAO {
	
	private static BoardDAO instance = new BoardDAOImpl();
	
	private BoardDAOImpl() {}
	
	public static BoardDAO getInstance() {
		return instance;
	}
	

	@Override
	public List<BoardDTO> boardSelectAll() throws SearchWrongException {
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		List<BoardDTO> list = new ArrayList<>();
		String sql="select * from board order by board_no desc";

		try {
			con = DBManager.getConnection();
			ps = con.prepareStatement(sql);

			//여기 코딩
			rs=ps.executeQuery();

			while(rs.next())
			{
				int board_no = rs.getInt("board_no");
				String subject=rs.getString("subject");
				String writer=rs.getString("writer");
				String content=rs.getString("content");
				String boardDate=rs.getString("board_date");

				BoardDTO dto=new BoardDTO(board_no,subject,writer,content,boardDate);
				list.add(dto);
			}


		}
		catch (SQLException e)
		{
			//e.printStackTrace();
			throw new SearchWrongException("DB에 문제가 있어 다시 진행해주요^^");
			
		}finally
		{
			DBManager.dbClose(con, ps, rs);
		}			

		return list;
	}

	@Override
	public List<BoardDTO> boardSelectBySubject(String keyWord) throws SearchWrongException {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<BoardDTO> list=new ArrayList<>();
		String sql="select * from board where subject like ?";

		try
		{
			con=DBManager.getConnection();
			ps=con.prepareStatement(sql);
			ps.setString(1,"%"+keyWord+"%"); // 중요

			rs=ps.executeQuery();

			while(rs.next())
			{
				int board_no = rs.getInt("board_no");
				String subject=rs.getString("subject");
				String writer=rs.getString("writer");
				String content=rs.getString("content");
				String boardDate=rs.getString("board_date");
				list.add(new BoardDTO(board_no,subject,writer,content,boardDate));
			}

		}
		catch(SQLException e)
		{
			throw new SearchWrongException("DB에 문제가 있어 다시 진행해주요^^");
		}
		finally
		{
			DBManager.dbClose(con, ps, rs);
		}

		return list;
	}

	@Override
	public BoardDTO boardSelectByNo(int boardNo) throws SearchWrongException {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		BoardDTO dto=null;
		String sql="select * from board where board_no=?";

		try
		{
			con=DBManager.getConnection();
			ps=con.prepareStatement(sql);
			ps.setInt(1,boardNo);

			rs=ps.executeQuery();

			if(rs.next())
			{
				dto=new BoardDTO(rs.getInt("board_no"),rs.getString("subject"),
						rs.getString("writer"),rs.getString("content"),
						rs.getString("board_date"));
			}

		}
		catch(SQLException e)
		{
			throw new SearchWrongException("DB에 문제가 있어 다시 진행해주요^^");
		}
		finally
		{
			DBManager.dbClose(con,ps,rs);
		}

		return dto;
	}

	@Override
	public int boardInsert(BoardDTO boardDTO) throws DMLException {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement ps=null;
		//ResultSet rs=null; insert이므로 resultset이 필요하지 않다
		String sql="insert into board (subject,writer,content) values(?,?,?)";
		int result=0;

		try
		{
			con=DBManager.getConnection();
			ps=con.prepareStatement(sql);
			ps.setString(1,boardDTO.getSubject());
			ps.setString(2,boardDTO.getWriter());
			ps.setString(3, boardDTO.getContent());

			result=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			throw new DMLException("DB에 문제가 있어 다시 진행해주요^^");
		}
		finally
		{
			DBManager.dbClose(con, ps);
		}

		return result;
	}

	@Override
	public int boardUpdate(BoardDTO boardDTO) throws DMLException {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement ps=null;
		String sql="update board set content=? where board_no=? ";
		int result=0;

		// 글번호만 입력 받아서 글 내용만 수정함
		try
		{
			con=DBManager.getConnection();
			ps=con.prepareStatement(sql);

			ps.setString(1,boardDTO.getContent());
			ps.setInt(2,boardDTO.getBoardNo());

			result=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			throw new DMLException("DB에 문제가 있어 다시 진행해주요^^");
		}
		finally
		{
			DBManager.dbClose(con, ps);
		}

		return result;
	}

	@Override
	public int boardDelete(int boardNo) throws DMLException
	{
		Connection con=null;
		PreparedStatement ps=null;
		String sql="delete from board where board_no=?";
		int result=0;

		try
		{
			con=DBManager.getConnection();
			ps=con.prepareStatement(sql);

			ps.setInt(1,boardNo);

			result=ps.executeUpdate();


		}
		catch(SQLException e)
		{
			throw new DMLException("DB에 문제가 있어 다시 진행해주요^^");
		}
		finally
		{
			DBManager.dbClose(con, ps);
		}

		return result;
	}

	@Override
	public int replyInsert(ReplyDTO replyDTO) throws DMLException
	{
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement ps=null;
		String sql="insert into reply values(reply_no_seq.nextval , ?, ? , sysdate)";
		int result=0;

		try
		{
			con=DBManager.getConnection();
			ps=con.prepareStatement(sql);

			ps.setString(1,replyDTO.getReplyContent());
			ps.setInt(2,replyDTO.getBoardNo());
			//날짜 처리?

			result=ps.executeUpdate();//select아니니까 resultset이 필요없음
		}
		catch(SQLException e)
		{
			throw new DMLException("댓글 등록 실패"); //exception을 변환
		}
		finally
		{
			DBManager.dbClose(con, ps);
		}

		return result;
	}

	@Override
	public BoardDTO replySelectByParentNo(int boardNo) throws SearchWrongException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BoardDTO boardDTO = null;
		List<ReplyDTO> replyList = new ArrayList<>();

		try {
			con = DBManager.getConnection();
			// 게시글 정보 조회
			ps = con.prepareStatement("select * from board where board_no=?");
			ps.setInt(1, boardNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				boardDTO = new BoardDTO(
						rs.getInt("board_no"), // 컬럼명이 맞아야 합니다
						rs.getString("subject"),
						rs.getString("writer"),
						rs.getString("content"),
						rs.getString("board_date")
				);
				// 댓글 정보 조회
				replyList = replySelect(con, boardNo); // 댓글 목록을 조회하는 메소드 호출
				boardDTO.setRepliesList(replyList); // 댓글 목록을 BoardDTO에 설정
			} else {
				throw new SearchWrongException("해당 글이 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new SearchWrongException("DB에서 데이터를 조회하는 중 문제가 발생했습니다");
		} finally {
			DBManager.dbClose(con, ps, rs);
		}

		return boardDTO;
	}

	/***
	 * 부모글에 해당하는 댓글 정보 가져오기
	 */
	private List<ReplyDTO> replySelect(Connection con, int boardNo) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ReplyDTO> replyList = new ArrayList<>();
		String sql = "select * from reply where board_no=?"; // 댓글을 조회하는 SQL 쿼리로 수정

		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, boardNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				ReplyDTO replyDTO = new ReplyDTO(
						rs.getInt("reply_no"), // 컬럼명이 맞아야 합니다
						rs.getString("reply_content"),
						rs.getInt("board_no"),
						rs.getString("reply_date")
				);
				replyList.add(replyDTO);
			}
		} finally {
			DBManager.dbClose(null, ps, rs); // Connection은 외부에서 닫힘
		}

		return replyList;
	}



}
/**
 * select는 모든 요소를 가지고 와야 하지만 update,insert는 내가 넣고 싶은 항목들만 넣어서 보낼 수 있어
 * 디비에서 알아서 관리해줌
 */












