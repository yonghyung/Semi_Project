package Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.SearchDAO;
import DTO.SearchDTO;
import SearchApp.Search;


@WebServlet("*.search")
public class SearchController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SearchDAO sDAO = SearchDAO.getInstance();
		Search searchApp = new Search();
		
		String uri =  request.getRequestURI();
		
		try {
			if (uri.equals("/search.search")) {
				request.setCharacterEncoding("utf-8");
				
				// 검색 조건
				boolean[] searchType = new boolean[] {false, false, false, false, false};
				
				// 검색 객체
				String[] alcholArr = new String[] {"null", "null", "null", "null", "null"}; // 5개
				String[] areaArr = new String[] {"null", "null", "null", "null", "null", "null"}; // 6개
				String[] gradeString;
				String product_name;
				float abv;
				int grade = 0;
				
				String sql;
				
				
				String search_text = request.getParameter("search_text");
				String search_avb = request.getParameter("avb");
				
				// 검색 조건 분류
				
				if(!(search_text.isEmpty())) { // 입력어가 있다면
					
					product_name = sDAO.getWord(search_text); // 파싱하고
					searchType[0] = true; // 입력 검색을 활성화하라
					
				} else {
					
					product_name = null;
					
				}
				
				
				if(!(search_avb.equals(""))) { // 술 도수를 입력했으면
					
					abv = Float.parseFloat(search_avb); // float로 바꾸고
					searchType[4] = true;
					
				} else {
					
					abv = 0;
					
				}
				
				if(!(request.getParameterValues("alchol") == null)) { // 술 종류를 택했다면
					
					searchType[1] = true; // 검색 조건을 활성화하고
					
					for(int i=0; i<request.getParameterValues("alchol").length; i++) {
						
						alcholArr[i] = request.getParameterValues("alchol")[i];
						
					}
					
					
				} else {
					
					alcholArr = null;
				}
				
				if(!(request.getParameterValues("area") == null)) {
					
					searchType[2] = true; // 검색 조건을 활성화
					
					for(int i=0; i<request.getParameterValues("area").length; i++) {
						
						areaArr[i] = request.getParameterValues("area")[i];
						
					}
					
					
				} else {
					
					areaArr = null;
				
				}
				
				if(!(request.getParameterValues("grade") == null)) {
					
					gradeString = request.getParameterValues("grade");
					
					grade = Integer.parseInt(gradeString[0]);
					
					searchType[3] = true;
					
				} else {
					
					gradeString = null;
				}
				
				// -------------------------------------------- 검색 --------------------------------------------------------------
				
				// 일반 검색
				if ((product_name) != null && areaArr == null && gradeString == null && abv == 0 && alcholArr == null ) {

					System.out.println(product_name);
					List<SearchDTO> list = sDAO.SearchByText(product_name);
					
					int count = list.size();
					
					// JSP로 넘길 예정
					for (SearchDTO dto : list) {
						
						System.out.println("게시글 번호 : " + dto.getSeq());
						System.out.println("상품명 : " + dto.getProduct_name());
						System.out.println("술 종류 : " + dto.getKind());
						System.out.println("가격 : " + dto.getPrice());
						System.out.println("도수 : " + dto.getAbv());
						System.out.println("평점 : " + dto.getGrade());
						System.out.println("설명 : " + dto.getSmry());
						System.out.println();
						
					}
					
					
				// 상세 검색
				} else if ((product_name) == null && (areaArr != null || gradeString != null || abv != 0 || alcholArr != null || grade != 0)) { // 입력 텍스트 안 보냄
					
					sql = searchApp.getSql(searchType);
					
					System.out.println(sql);
					
					List<SearchDTO> list = sDAO.searchDetail(searchType, alcholArr, areaArr, grade, abv, sql);
					
					int count = list.size();
					
					// JSP로 넘길 예정
					for (SearchDTO dto : list) {
						
						System.out.println("게시글 번호 : " + dto.getSeq());
						System.out.println("상품명 : " + dto.getProduct_name());
						System.out.println("술 종류 : " + dto.getKind());
						System.out.println("가격 : " + dto.getPrice());
						System.out.println("도수 : " + dto.getAbv());
						System.out.println("평점 : " + dto.getGrade());
						System.out.println("설명 : " + dto.getSmry());
						System.out.println();
						
					}
					
					
				} else if ((product_name) != null && (areaArr != null || gradeString != null || abv != 0 || alcholArr != null || grade != 0)) { // 입력 텍스트 보냄
					
					sql = searchApp.getSql(searchType);
					System.out.println(sql);
					
					List<SearchDTO> list = sDAO.searchDetailHasPname(searchType, product_name, alcholArr, areaArr, grade, abv, sql);
					
					int count = list.size();
					
					// JSP로 넘길 예정
					for (SearchDTO dto : list) {
						
						System.out.println("게시글 번호 : " + dto.getSeq());
						System.out.println("상품명 : " + dto.getProduct_name());
						System.out.println("술 종류 : " + dto.getKind());
						System.out.println("가격 : " + dto.getPrice());
						System.out.println("도수 : " + dto.getAbv());
						System.out.println("평점 : " + dto.getGrade());
						System.out.println("설명 : " + dto.getSmry());
						System.out.println();
						
					}
				}
			}
			
			
			
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("error.html");
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
