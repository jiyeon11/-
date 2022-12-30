package 노래추천;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Calendar;
import java.util.Scanner;
//https://blog.naver.com/wusemr2/222205479918

public class Manager {
	
	public static void main(String[] args) throws SQLException{
		Scanner scan = new Scanner(System.in);
		DB db = new DB();
		String user, password;
		int choice; //할 일 고르기
		boolean end = true;  //할 일 고르기 while문 조건
		
		System.out.println("                 관리자 서버");
		System.out.println("================로그인 해주세요================");
		
		//id와 비번을 입력해 로그인될 때까지 한다.
		do {       
			System.out.print("id 입력 >>");
			user = scan.next();
		
			System.out.print("비밀번호 입력 >>");
			password = scan.next();
			if(user.equals(DB.uesr) && password.equals(DB.password)) {
				Sleep(); //1초 뒤
				System.out.println("\n로그인 성공!\n");
				break;
			}else {
				System.out.println("\nid나 비밀번호를 잘못 입력하셨습니다. 다시 입력해주세요!\n");
			}
		}while(true);
		
		do {     //할 일 고르기
			System.out.println("=============================");
			System.out.println("하실 일을 선택해주세요");
			System.out.println("▶1번 노래 추가");
			System.out.println("▷2번 노래 삭제");
			System.out.println("▶3번 노래 목록 보기");
			System.out.println("▷4번 오늘의 추천 노래 등록");
			System.out.println("▶5번 노래 추천 게시판 보기");
			System.out.println("▷6번 종료");
			System.out.println("=============================");
			System.out.print("번호를 입력해주세요 >>");
			choice = scan.nextInt();
			
			switch(choice) {
			case 1 : Insertion(); break;                  //1.노래 추가
			case 2 : Delete(); break;                     //2.노래 삭제
			case 3 : Show_SongList(); break;              //3.노래 조회
			case 4 : today_song_recommendation(); break;  //4.오노추 등록
			case 5 : Show_message_board(); break;         //5.게시판 조회
			case 6 :                                      //6.프로그램 종료
				System.out.println("\n프로그램을 종료합니다!\n");
				end = false; 
				break;
			default : System.out.println("\n잘못 입력하셨습니다. 다시 입력해주세요!\n");
			}
			
		}while(end);
		
	}//메인 끝
	
	static void Delete() throws SQLException { //노래 삭제 메서드
		Scanner scan = new Scanner(System.in);
		Scanner sc = new Scanner(System.in);
		DB db = new DB();
		DB db1 = new DB();
		db.connect();
		
		String del_title = "";  //삭제할 제목 변수
		String del_table = "";  //삭제할 노래의 테이블 변수
		String del_singer_name ="";  //삭제할 노래의 가수
		String del_check_sql = "";   //sql문
		
		do {
			System.out.println("=============================");
			System.out.println("삭제할 노래 장르를 선택해주세요");
			System.out.println("▶1번 여자아이돌");
			System.out.println("▷2번 남자아이돌");
			System.out.println("▶3번 가요");
			System.out.println("▷4번 팝송");
			//System.out.println("▶5번 제이팝");
			System.out.println("=============================");
			System.out.print("번호를 입력해주세요 >>");
			int select = scan.nextInt();
			
			switch(select) {         //노래 테이블 선택
			case 1 : del_table = "girl_group"; break;
			case 2 : del_table = "boy_group"; break;
			case 3 : del_table = "kpop"; break;
			case 4 : del_table = "pop"; break;
			//case 5 : del_table = "jpop"; break;
			default : 
				del_table = "입력실패"; 
				System.out.println("\n잘못 입력하셨습니다. 다시 입력해주세요!\n");
			}
		}while(del_table.equals("입력실패"));
		
		System.out.println("=============================");
		System.out.println("삭제하실 노래의 가수명을 입력해주세요");
		System.out.print(">>>");
		del_singer_name = sc.nextLine();
		
		del_check_sql = "SELECT * FROM " + del_table + " WHERE singer = '" + del_singer_name + "'";  //입력한 가수 select문
		ResultSet delete = db.stmt.executeQuery(del_check_sql);
		
		if(delete.next()) {  //입력한 가수가 있으면
			System.out.println("=============================");
			System.out.println(">현재 입력하신 가수의 노래 리스트입니다<");
			
			while(delete.next()) {  //가수꺼 노래 쭉 출력
					System.out.println(delete.getString("title"));
			}
			
		
			System.out.println("=============================");
			System.out.println("삭제하실 노래 제목을 입력해주세요");
			System.out.print(">>>");
			del_title = sc.nextLine();
			String del_check = "SELECT * FROM " + del_table + " WHERE title = '"+ del_title + "'";
			ResultSet del_song = db.stmt.executeQuery(del_check);
			
			if(del_song.next()) {  //노래 제목이 일치하면 삭제
				try {
					String removeSong = "DELETE FROM "+del_table+" where title='"+del_title+"'"; //노래 제목 입력하면 지우는 sql문
					String removeOhnochooSong = "DELETE FROM ohnochoo_all_song WHERE title='"+del_title+"'"; //오노추 노래 테이블도 같이 삭제
					db.stmt.executeUpdate(removeOhnochooSong);
					db.stmt.executeUpdate(removeSong);
					System.out.println("\n노래 삭제 성공!\n");
				} catch(Exception e) {
					System.out.println("\n노래 삭제 실패 이유 : " + e.toString() + "\n");
				}
			}else {
				System.out.println("\n입력하신 노래는 존재하지 않습니다.\n");
			}
		
		}else {  //입력한 가수가 db에 없으면
			System.out.println("=============================");
			System.out.println("입력하신 가수는 현재 db에 없습니다.");
		}
		Sleep();
	}//노래 삭제 메서드 끝
	
	static void Insertion() {  //노래 추가 메서드  
		Scanner scan = new Scanner(System.in);
		Scanner sc = new Scanner(System.in);
		DB db = new DB();
		String table = "";   //테이블
		String genre = "";   //장르
		String singer = "";  //가수
		String title = "";   //제목
		String situ = "";    //상황
		String route = "";   //노래 파일 경로
		
		do {
			System.out.println("=============================");
			System.out.println("추가할 노래 장르를 선택해주세요");
			System.out.println("▶1번 여자아이돌");
			System.out.println("▷2번 남자아이돌");
			System.out.println("▶3번 가요");
			System.out.println("▷4번 팝송");
			//System.out.println("▶5번 제이팝");
			System.out.println("=============================");
			System.out.print("번호를 입력해주세요 >>");
			int select = scan.nextInt();
			
			switch(select) {
			case 1 : table = "girl_group"; break;
			case 2 : table = "boy_group"; break;
			case 3 : table = "kpop"; break;
			case 4 : table = "pop"; break;
			//case 5 : table = "jpop"; break;
			default : table = "입력실패";
				System.out.println("\n!잘못 입력하셨습니다. 다시 입력해주세요!\n");
			}
		}while(table.equals("입력실패"));
		
		System.out.println("=============================");
		System.out.print("추가할 가수 이름을 입력해주세요 >>");
		singer = sc.nextLine();
		
		System.out.println("=============================");
		System.out.print("추가할 노래 제목을 입력해주세요 >>");
		title = sc.nextLine();
		
		do {
			System.out.println("=============================");
			System.out.println("추가할 노래 상황 선택해주세요");
			System.out.println("▶1번 기분 좋을 때");
			System.out.println("▷2번 집중할 때");
			System.out.println("▶3번 우울할 때");
			System.out.print("번호를 입력해주세요 >>");
			int select_situ = scan.nextInt();
			
			switch(select_situ) {
			case 1 : situ = "기분 좋을 때"; break;
			case 2 : situ = "집중할 때"; break;
			case 3 : situ = "우울할 때"; break;
			default : situ = "입력실패";
				System.out.println("\n!잘못 입력하셨습니다. 다시 입력해주세요!\n");
			}
		}while(situ.equals("입력실패"));
		
		System.out.println("=============================");
		System.out.print("노래 파일 경로를 입력해주세요 >>");
		route = sc.nextLine();
		db.connect();
		
		try {     //노래추가                                                 
			String insertsong = "INSERT INTO `song`.`"+table+"` (`singer`, `title`, `situation`, `song_path`) VALUES ('"+singer+"', '"+title+"', '"+situ+"', '"+route+"')";
			String ohnochoo = "INSERT INTO `song`.`ohnochoo_all_song` (`singer`, `title`, `situation`, `song_path`) VALUES ('"+singer+"', '"+title+"', '"+situ+"', '"+route+"')";  //오노추 테이블에도 동시에 추가
			db.stmt.executeUpdate(insertsong);
			db.stmt.executeUpdate(ohnochoo);
			System.out.println("\n노래 추가 성공!\n");
		} catch(Exception e) {
			System.out.println("\n노래 추가 실패!: " + e.toString() + "\n");
		}
	}//Insertion 끝
	
	static void Show_SongList() throws SQLException{   //노래 목록 보기
		Scanner scan = new Scanner(System.in);
		DB db = new DB();
		db.connect();
		String song_table = null;
		do {
			System.out.println("=============================");
			System.out.println("조회할 노래 장르를 선택해주세요");
			System.out.println("▶1번 여자아이돌");
			System.out.println("▷2번 남자아이돌");
			System.out.println("▶3번 가요");
			System.out.println("▷4번 팝송");
			//System.out.println("▶5번 제이팝");
			System.out.println("=============================");
			System.out.print("번호를 입력해주세요 >>");
			int select_table = scan.nextInt();
			switch(select_table) {
			case 1 : song_table = "girl_group"; break;
			case 2 : song_table = "boy_group"; break;
			case 3 : song_table = "kpop"; break;
			case 4 : song_table = "pop"; break;
			//case 5 : song_table = "jpop"; break;
			default : song_table = "입력실패";
				System.out.println("\n!잘못 입력하셨습니다. 다시 입력해주세요!\n");
			}
		}while(song_table.equals("입력실패"));
		
		System.out.println("               ======"+song_table + " 테이블 조회======");
		String viewTable = "SELECT * FROM " + song_table +" ORDER BY singer ASC";  //가수 오름차순 정렬
		ResultSet table = db.stmt.executeQuery(viewTable);
		
		System.out.println("---------------------------------------------------------------------------------");
		try {  //테이블에 있는 가수 제목 상황 출력
			while(table.next()){
				System.out.printf("|%-23s	|%-25s	|%-7s	|\n",
						table.getString("singer"),    //가수
						table.getString("title"),     //제목
						table.getString("situation")); //상황
				System.out.println("---------------------------------------------------------------------------------");
			}
			
		}catch(Exception e) {
			System.out.println("테이블 조회 실패! : " + e.toString());
		}
		
	}//SongList_show 끝
	
	static void today_song_recommendation() throws SQLException {   //오노추 등록
		Scanner scan = new Scanner(System.in);
		Scanner sc = new Scanner(System.in);
		Calendar now = Calendar.getInstance();  //명품자바
		DB db = new DB();
		db.connect();
		
		int year = now.get(Calendar.YEAR);            //년도
		int month = now.get(Calendar.MONTH)+1;        //달
		int day = now.get(Calendar.DAY_OF_MONTH);     //일
		int dayOfweek = now.get(Calendar.DAY_OF_WEEK);//요일
		
		String dayOfWeek_str = "";  //요일 String변수
		String comment = "";  //코멘트
		String title = ""; //노래제목
		String singer = "";  //노래 가수
		
		switch(dayOfweek) {
		case Calendar.SUNDAY : dayOfWeek_str = "일요일"; break;
		case Calendar.MONDAY : dayOfWeek_str = "월요일"; break;
		case Calendar.TUESDAY : dayOfWeek_str = "화요일"; break;
		case Calendar.WEDNESDAY : dayOfWeek_str = "수요일"; break;
		case Calendar.THURSDAY : dayOfWeek_str = "목요일"; break;
		case Calendar.FRIDAY : dayOfWeek_str = "금요일"; break;
		case Calendar.SATURDAY : dayOfWeek_str = "토요일"; break;
		}
		
		System.out.println("==========오늘의 노래 추천==========");
		System.out.print("노래 제목 입력 >>>");
		title = scan.nextLine();
		System.out.print("간략한 선정 이유를 작성해주세요 >>>");
		comment = sc.nextLine();
		
		String check_song = "SELECT * FROM ohnochoo_all_song WHERE title = '"+title+"'"; //노래가 디비에 있는지 제목 확인
		ResultSet check = db.stmt.executeQuery(check_song);
		
		if(check.next()) {  //노래가 db에 있으면 
			try {
				singer =  check.getString("singer");  //가수를 singer 변수에 넣기
				String insert_Title_Comment = "INSERT INTO `song`.`ohnochoo` (`DATE`, `dayOfWeek`, `Select_song`, `singer`, `comment`) VALUES ('"+year+"-"+month+"-"+day+"', '"+dayOfWeek_str+"', '"+title+"', '" +singer +"', '"+ comment +"')"; //오노추 제목과 코멘트 추가
				db.stmt.executeUpdate(insert_Title_Comment);
				System.out.println("\n"+year+"-"+month+"-"+day + " " +dayOfWeek_str+" 오노추 작성 완료!\n"); //작성완료 출력
			}catch (SQLIntegrityConstraintViolationException e) {  //이미 오늘의 노래가 등록되어 있다면 
				System.out.println("\n오늘의 추천 노래를 이미 등록하셨습니다.\n");
			}
		}else {  //노래가 db에 없으면
			System.out.println("\n제목을 잘못 적으셨거나 해당 노래가 db에 있지 않습니다! 다시 한 번 확인해주세요\n");
		}
		
	}//today_song_recommendation 끝
	
	static void Show_message_board() throws SQLException {
		DB db = new DB();
		db.connect();
		
		String message_board_show = "SELECT * FROM song.message_board";  //게시판 테이블 select문
		ResultSet board = db.stmt.executeQuery(message_board_show);
		Sleep();
		System.out.println("\n게시판");
		
		while(board.next()) { //닉네임, 글 출력
			System.out.println("-------------------------------------------------------------------");
			System.out.println("|" + board.getString("nickname"));
			System.out.println("|"+ board.getString("request"));
		}
		System.out.println("-------------------------------------------------------------------\n");
		Sleep();
	}//Show_message_board
	
	static void Sleep() {   //느리게 나오기
		try {
			Thread.sleep(1000);  //1초 뒤에 나오도록 함
		}catch(InterruptedException e) {
			e.printStackTrace();  
		}
	}
}
