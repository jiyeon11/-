package 노래추천;
//https://blog.naver.com/kor705/222647343182 <=참고

import java.util.*;
import javazoom.jl.player.MP3Player;
import java.sql.ResultSet;
import java.sql.SQLException;

class User_Information{  //사용자 정보
	public static String nickname;  //닉네임
	public static String password;  //비번
	public static String id;        //id
}
public class Song_Recommendation{
	
	public static void main(String[] args) throws SQLException {
		Scanner scan = new Scanner(System.in);
		String genre = "";  //장르
		String situ = "";   //상황
		
		while(true) {
			join_membership_rogin_screen();    //회원가입 or 로그인 화면
			start_screen();                    //시작화면
			String enter = scan.nextLine();    //엔터 눌러야 시작
			
			if(enter.isEmpty()) {              //엔터 누르면 취향 조사하는 메서드 갖고 오기
				clear_screen();               //시작화면 지워줌
				while(true) {
					Selection_songs(genre, situ);  //그냥 여기서 다함
					if(go_back() == 1) {    //1번이면 처음으로 돌아감
						clear_screen();
					}else {    //2번이면 프로그램 종료
						break;
					}
				}
				break;
			}else {
				clear_screen();
			}
		}//while
		scan.close();
	}// main 끝

	static void join_membership_rogin_screen() throws SQLException {  //회원가입, 로그인 화면
		Scanner scan = new Scanner(System.in);
		boolean ok_notOk = false;  //잘못 입력했을 때를 대비한 변수
		int choice; //번호 고르는 변수
		
		do {
			System.out.println(
					  " _                              __    __            ___                ___             _\r\n"
					+ "| |                            |  |  |  |      ____|   |____      ____|   |____       | |\r\n"
					+ "| |              ___________   |  |  |  |     |____     ____|    |____     ____|      | |\r\n"
					+ "| |              |________  |  |  |  |  |         /  _  \\            /  _  \\     _____| |\r\n"
					+ "| |___________    _______|  |  |  |__|  |        /  / \\  \\          /  / \\  \\   |_____  |\r\n"
					+ "|_____________|  |  ________|  |   __   |       /  /   \\  \\        /  /   \\  \\        | |\r\n"
					+ "      | |        |  |________  |  |  |  |      /_ /     \\ _\\      /_ /     \\ _\\       | |\r\n"
					+ "      | |        |__________|  |  |  |  |    _________________          __            |_|\r\n"
					+ "______| |_______               |  |  |  |   |_______   _______|        |  |             \r\n"
					+ "|______________|               |__|  |__|           | |                |  |              \r\n"
					+ "                                                    | |                |  |____________   \r\n"
					+ "                                                    |_|                |_______________|");
			
			System.out.println("                      __________________________________");
			System.out.println("                     |                                 	|");
			System.out.println("                     |       1번 회원가입 | 2번 로그인      	|");
			System.out.println("                     |                              	|");
			System.out.println("                     |                                 	|");
			System.out.println("                     |__________________________________|");
			System.out.print("                                    >>>");
			choice = scan.nextInt();
			
			switch(choice) {
			case 1 : //회원가입
				clear_screen();  //화면 지우기
				join_membership(); //회원가입 하러가기
				ok_notOk = false;
				
			case 2 :  //로그인
				clear_screen();  //화면 지우기
				rogin(); //로그인 하러가기
				ok_notOk = false;
				break;
			default : 
				System.out.println("\n                         잘못 입력하셨습니다. 다시 입력해주세요!\n");
				clear_screen();  //1초 뒤 다시 나옴
				ok_notOk = true;
			}
		}while(ok_notOk);  //번호 잘못 쓰면 계속 반복함
		clear_screen();    //화면 지우기
	}//join_membership_rogin_screen끝
	
	static void join_membership() throws SQLException {
		Scanner scan = new Scanner(System.in);
		DB db = new DB();
		db.connect();
		String id;            //id
		String nickname;      //닉네임
		String password;      //비밀번호
		String id_sql;        //id 중복 확인용 sql문
		String membership_sql;//회원가입 sql문
		
		while(true){  //id 중복 확인
			System.out.println("                                         회원가입");
			System.out.println("                      ---------------------------------------------");
			System.out.print("                     |id를 입력해주세요>>");
			id = scan.next();
			
			id_sql = "SELECT * FROM join_membership WHERE id = '"+id+"'"; //회원가입 테이블에 입력한 id가 있는지 보는 SELECT문
			ResultSet id_check = db.stmt.executeQuery(id_sql);
			
			if(id_check.next()) {  //이미 id가 있으면
				System.out.println("\n                         이미 입력한 id가 있습니다. 다른 id를 입력해주세요\n");
				clear_screen();
			}else {
				break;
			}
		}//while
		
		System.out.println("                      ---------------------------------------------");
		System.out.print("                     |닉네임을 입력해주세요>>");
		nickname = scan.next();
		
		System.out.println("                      ---------------------------------------------");
		System.out.print("                     |비밀번호를 입력해주세요>>");
		password = scan.next();
		System.out.println("                      ---------------------------------------------");
		
		membership_sql = "INSERT INTO `song`.`join_membership` (`id`, `nickname`, `password`) VALUES ('"+id+"', '"+nickname+"', '"+password+"')";
		try {
			db.stmt.executeUpdate(membership_sql);  //회원가입
			System.out.println("\n                                       회원가입 완료!\n");
		}catch(Exception e) {
			System.out.println("\n                                       회원가입 실패!\n");
		}
	}//join_membership끝
	
	static void rogin() throws SQLException {  //로그인
		Scanner scan = new Scanner(System.in);
		DB db = new DB();
		db.connect();
		User_Information user = new User_Information();
		
		String rogin_sql;    //확인용 sql문
		
		do {   //로그인
			System.out.println("                                          로그인");
			System.out.println("                      ---------------------------------------------");
			System.out.print("                      |id를 입력해주세요>>");
			user.id = scan.next();     //id입력
			System.out.println("                      ---------------------------------------------");
			System.out.print("                      |비밀번호를 입력해주세요>>");
			user.password = scan.next();  //비번 입력
			System.out.println("                      ---------------------------------------------");
			
			rogin_sql = "SELECT * FROM join_membership WHERE id = '" + user.id + "'";  // id가 있는지 확인용 sql문
			ResultSet rogin = db.stmt.executeQuery(rogin_sql);
			
			if(rogin.next()) {  //id가 있으면
				if(user.password.equals(rogin.getString("password"))){ //입력한 비번과 id 비번이 일치하면 로그인 성공
					System.out.println("\n\n                                        로그인 성공!");
					break;
				}else {                                               //불일치일 경우
					System.out.println("\n                           비밀번호를 잘못 입력하셨습니다. 다시 시도해주세요\n");
					clear_screen();
				}
			}else {  //id가 없을 경우
				System.out.println("\n                             존재하지 않는 id입니다. 다시 시도해주세요\n");
				clear_screen();
			}
		}while(true);
		
	}//rogin끝
	
	static void start_screen() {  //시작화면
		System.out.println(
				  " _                              __    __            ___                ___             _\r\n"
				+ "| |                            |  |  |  |      ____|   |____      ____|   |____       | |\r\n"
				+ "| |              ___________   |  |  |  |     |____     ____|    |____     ____|      | |\r\n"
				+ "| |              |________  |  |  |  |  |         /  _  \\            /  _  \\     _____| |\r\n"
				+ "| |___________    _______|  |  |  |__|  |        /  / \\  \\          /  / \\  \\   |_____  |\r\n"
				+ "|_____________|  |  ________|  |   __   |       /  /   \\  \\        /  /   \\  \\        | |\r\n"
				+ "      | |        |  |________  |  |  |  |      /_ /     \\ _\\      /_ /     \\ _\\       | |\r\n"
				+ "      | |        |__________|  |  |  |  |    _________________          __            |_|\r\n"
				+ "______| |_______               |  |  |  |   |_______   _______|        |  |             \r\n"
				+ "|______________|               |__|  |__|           | |                |  |              \r\n"
				+ "                                                    | |                |  |____________   \r\n"
				+ "                                                    |_|                |_______________|");

		System.out.println("");
		System.out.println("                      =========================================");
		System.out.println("                      =                                       =");
		System.out.println("                      =      노래 추천을 받으려면 엔터를 눌러주세요      =");
		System.out.println("                      =                                       =");
		System.out.println("                      =                                       =");
		System.out.println("                      =                                       =");
		System.out.println("                      =========================================");
	}
	
	static String choice_Genre(String genre) {  //장르 고르기
		Scanner scan = new Scanner(System.in);
		int select_genre;  //장르 번호로 고르는 변수
		
		do {      //장르 선택
			System.out.println("                      =========================================");
			System.out.println("                                어떤 장르를 원하십니까?(숫자 입력)    ");
			System.out.println("                                      ▶ 1번 여자아이돌");
			System.out.println("                                      ▷ 2번 남자아이돌");
			System.out.println("                                      ▶ 3번 가요");
			System.out.println("                                      ▷ 4번 팝송");
			System.out.println("                                      ▶ 5번 제이팝");
			System.out.println("                      =========================================");
			System.out.print("                                   입력해주세요▷");
			select_genre = scan.nextInt();  //장르
			
			switch(select_genre) {
			case 1: genre = "girl_group"; break;
			case 2: genre = "boy_group"; break;
			case 3: genre = "kpop"; break;
			case 4: genre = "pop"; break;
			case 5: genre = "jpop"; break;
			default : genre = "입력실패"; 
				System.out.println("\n\n                                  ※다시 입력해주세요※");
			}
			clear_screen();
		}while(genre.equals("입력실패"));
		
		clear_screen();  //화면 초기화
		
		return genre;
	} //choice_Genre 끝
	
	static String choice_situation(String situ) {  //상황 고르기
		Scanner scan = new Scanner(System.in);
		int correct_num;  //상황 번호 고르기 변수
		
		do {
			System.out.println("                      ===========================================");
			System.out.println("                              어떤 상황에서 듣고 싶으십니까?(숫자 입력)     ");
			System.out.println("                                      ▶1번 기분 좋을 때           	");
			System.out.println("                                      ▷2번 우울할 때                 ");
			System.out.println("                                      ▶3번 집중할 때                 ");
			System.out.println("                      ===========================================");
			System.out.print("                                  입력해주세요▷");
			correct_num = scan.nextInt();

			switch(correct_num) {
			case 1 : situ = "기분 좋을 때"; break;
			case 2 : situ = "우울할 때"; break;
			case 3 : situ = "집중할 때"; break;
			default: System.out.println("\n\n                                    ※다시 입력해주세요※");
				situ = "입력실패";
			}
			clear_screen();   //화면 지우기
		}while(situ.equals("입력실패"));
		return situ;
	}//choice_situation 끝
	
	static void choosing_song_screen(){        //노래 고르는 거 대기화면
		System.out.println("                      =========================================");
		System.out.println("                                                           ");
		System.out.println("                                추천할 노래를 고르는 중입니다~~!     ");
        System.out.println("                                    잠시만 기다려주세요~!        ");
        System.out.println("                                                           ");
        System.out.println("                      =========================================");
        clear_screen();
	}//choosing_song_screen끝
	
	static void Selection_songs(String genre, String situ) throws SQLException {   //추천 노래 등장 메서드
		DB db = new DB();
		db.connect();
		Scanner scan = new Scanner(System.in);
		MP3Player mp3 = new MP3Player();//player jar 안에 있는 mp3를 실행시켜주는 객체

		String table_name = choice_Genre(genre);  //테이블명 = genre //장르 고르기 메소드
		String situation = choice_situation(situ); //상황 = situ   //상황 고르기 메소드
		choosing_song_screen();  //노래 추천 대기화면 메소드
		int choice_num = 0;   //노래 재생, 정지 고를 변수
		
		do{
			mp3.play(Song_play(table_name, situation));  //노래 재생 화면 및 노래 재생
			
			System.out.println("                 =====================================================");
			System.out.println("                       1번 재생 중지  | 2번 다음 노래 재생 | 3번 게시판 작성");
			System.out.print("\n                                    번호 입력>>>");
			choice_num = scan.nextInt();
			
			switch(choice_num) {
			case 1:   //노래 재생 중지
				System.out.println("\n\n                                   |-------------|\r\n"
							 	     + "                                       재생 중지    \r\n"
								     + "                                   |-------------|\n\n");
				mp3.stop();  //노래 멈춤
				clear_screen();  //화면 지우기
				break;
			case 2:  //다음 노래 재생
				System.out.println("\n\n                                   |--------------|\r\n"
						             + "                                      다음 노래 재생    \r\n"
						             + "                                   |--------------|\n\n");
				mp3.stop();  //노래 멈춤
				clear_screen();  //화면 지우기
				break;
			case 3:  //게시판 작성하러 가기
				mp3.stop();
				System.out.println("\n\n                                |--------------------|\r\n"
									+ "                                   게시판 작성 하러 가기    \r\n"
									+ "                                |--------------------|\n\n");
				clear_screen();  //화면 지우기
				message_board();  //게시판 작성 메서드
				break;
			}
			if(choice_num == 1) break;  //1번 노래 정지 하면 while문 빠져 나감
			else if(choice_num == 3) break;  //3번 게시판 작성 하러 가기 하면 while문 빠져나감
		}while(true);
	
	}//Selection_songs끝
	
	static String Song_play(String table_name, String situation) throws SQLException {  //다음 노래 재생 메서드
		MP3Player mp3 = new MP3Player();
		DB db = new DB();
		db.connect();
		String path = null;  //경로
		String sql = "SELECT * FROM " + table_name +" where situation = '"+ situation + "'order by rand() limit 1";  //랜덤으로 하나 보이게하는 sql문
		ResultSet select_one = DB.stmt.executeQuery(sql);
		
		appearSong();//노래 등장! 출력 메서드
		
		if(select_one.next()){
			System.out.printf("\n                                      [%s]\n",select_one.getString("singer"));  //가수 출력
			System.out.printf("                                      %s\n",select_one.getString("title"));      //제목 출력
			path = System.getProperty("user.dir")+select_one.getString("song_path");  //path에 경로 저장
		}
		return path;
	}//Song_play 끝 
	
	static void appearSong() {  //노래 등장! 글씨
		System.out.println(""
				+ " _                              __    __      ______________                       __          __\r\n"
				+ "| |                            |  |  |  |    |   ___________|     _____________   |  |        |  |\r\n"
				+ "| |              ___________   |  |  |  |    |  |                |____     ____|  |  |        |  |\r\n"
				+ "| |              |________  |  |  |  |  |    |  |___________         /  _  \\      |  |_____   |  |\r\n"
				+ "| |___________    _______|  |  |  |__|  |    |______________|       /  / \\  \\     |   _____|  |  |\r\n"
				+ "|_____________|  |  ________|  |   __   |    _________________     /  /   \\  \\    |  |        |  |\r\n"
				+ "      | |        |  |________  |  |  |  |   |_________________|   /  /     \\  \\   |  |        |  |\r\n"
				+ "      | |        |__________|  |  |  |  |       __________       /_ /       \\ _\\  |__|        |  |\r\n"
				+ "______| |_______               |  |  |  |      /  ______  \\               __________          |  |\r\n"
				+ "|______________|               |__|  |__|     /  /      \\  \\             /  ______  \\         |  |\r\n"
				+ "                                              \\  \\______/  /            /  /      \\  \\        |__|\r\n"
				+ "                                               \\__________/             \\  \\______/  /         __\r\n"
				+ "                                                                         \\__________/         (..)");
	}//appearSong끝
	
	static int go_back() throws SQLException {  //처음으로 돌아갈지 말지 고르는 메소드
		Scanner scan = new Scanner(System.in);
		int choice;      //노래추천 다시 받을지 여부 고르는 변수
		boolean ok_notOk = true; //잘못 입력하면 다시 입력받도록
		do {
			clear_screen();  //화면 지우기
			System.out.println("                 =====================================================");
			System.out.println("                              노래추천을 처음부터 다시 받으시겠습니까?");
			System.out.println("                                   1번 예 | 2번 아니오\n");
			System.out.print("                                 번호 입력>>>");
			choice= scan.nextInt();
			
			switch(choice) {
			case 1 : //처음으로 돌아가서 다시 추천받음
				ok_notOk = false;
				break;
			case 2: //노래 추천 받기 끝냄
				ohnochoo_question();  //오노추 들어볼건지 말건지 물어보는 메서드
				System.out.println("\n\n\n\n                               |---------------------|\r\n"
								        + "                                   프로그램을 종료합니다.\r\n"
								         + "                               |---------------------|");
				ok_notOk = false;
				break;
			default: 
				System.out.println("\n                              잘못 입력하셨습니다. 다시 입력해주세요!\n");
				ok_notOk = true;
			}
		}while(ok_notOk);
		
		return choice;
	}//go_back끝
	static void ohnochoo_question() throws SQLException {
		Scanner scan = new Scanner(System.in);
		int choice;
		boolean ok_notOk = false;
		
		do { 
			clear_screen();
			System.out.println("                                      히든 메뉴 오노추");
			System.out.println("                 =====================================================");
			System.out.println("                         잠깐! 이지연의 오늘의 노래 추천을 들어보시겠습니까?");
			System.out.println("                                  1번 싫어요  |  2번 들어볼래요");
			System.out.print("\n                                      >>>");
			choice = scan.nextInt();
			clear_screen();
			
			switch(choice) {
			case 1 :  //싫어요 하면 바로 프로그램 종료
				ok_notOk = false;
				break; 
			case 2 : ohnochoo();  //오노추 메서드
				ok_notOk = false;
				break;
			default : //잘못 입력
				System.out.println("\n\n                          잘못 입력하셨습니다. 다시 입력해주세요!\n");
				ok_notOk = true;
			}
		}while(ok_notOk);
		
	}//ohnochoo_question 끝
	
	static void ohnochoo() throws SQLException {
		Scanner scan = new Scanner(System.in);
		DB db = new DB();
		db.connect();
		MP3Player mp3 = new MP3Player();
		
		String ohnochoo_title = null;  //오노추 노래 제목
		String ohnochoo_singer = null;  //오노추 가수
		String ohnochoo_comment = null;  //오노추 선정 이유
		String path = null;             //오노추 노래 경로 
		String sql_ohnochoo = "SELECT * FROM ohnochoo WHERE DATE = curdate()";  //https://snepbnt.tistory.com/490 오늘 날짜 데이터 조회
		ResultSet ohnochoo = db.stmt.executeQuery(sql_ohnochoo);
		
		if(ohnochoo.next()) {  //변수에 넣기
			ohnochoo_title = ohnochoo.getString("Select_song");
			ohnochoo_singer = ohnochoo.getString("singer");
			ohnochoo_comment = ohnochoo.getString("comment");
		}
		
		String sql = "SELECT * FROM ohnochoo_all_song WHERE title = '"+ohnochoo_title+"'";  //제목 맞으면 SELECT문
		ResultSet ohnochoo_song = db.stmt.executeQuery(sql);
		
		if(ohnochoo_song.next()) { //노래 재생을 위한 경로
			path = System.getProperty("user.dir")+ohnochoo_song.getString("song_path");
		}
		mp3.play(path); //노래 재생
		
		System.out.println("                       _________         _                   _\r\n"
				         + "                      /  _____  \\       | |             ____| |____\r\n"
				         + "                     /  /     \\  \\      | |            |___  _   __|\r\n"
				         + "                     \\  \\_____/  /      | |________       / / \\ \\\r\n"
				         + "                      \\_________/       |__________|     /_/   \\_\\\r\n"
				         + "                          | |               | |        _____________\r\n"
				         + "                     ____ | |_____     _____| |_____  |_____   _____|\r\n"
				         + "                    |_____________|   |_____________|       | |\r\n"
				         + "                                                            |_|"); 
		
		System.out.println("                 =======================================================");
		System.out.println("                 지연이의 오늘의 추천 노래");
		System.out.println("                 " + ohnochoo_title);
		System.out.println("                 -------------------------------------------------------");
		System.out.println("                 지연이의 오늘의 추천 노래의 가수");
		System.out.println("                 " + ohnochoo_singer);
		System.out.println("                 -------------------------------------------------------");
		System.out.println("                 지연이의 오노추 선정 이유");
		System.out.println("                 " + ohnochoo_comment);
		System.out.println("                 -------------------------------------------------------");
		
		System.out.println("\n                  프로그램을 이용해주셔서 감사합니다. 노래 정지 & 프로그램 종료는 1번 입력");
		System.out.print("                  >>>");
		int num = scan.nextInt();
		
		if(num==1)  //1번 입력하면 노래 정지
			mp3.stop();
	}//ohnochoo끝
	
	static void message_board() {  //게시판
		Scanner scan = new Scanner(System.in);
		DB db = new DB();
		db.connect();
		User_Information user = new User_Information();
		
		String nickname = null; //닉네임
		String request;         //추천 글
		String request_sql;     //게시판 테이블에 값 넣는 sql문
		
		System.out.println("                  __________________________________________________________");
		System.out.println("                 |                                                          |");
		System.out.println("                 |추천하고 싶은 노래가 있다면 적어주세요~ 자세히 적을수록 좋습니다(곡명, 가수 등)|");
		System.out.println("                 |__________________________________________________________|");
		System.out.print("\n                  >>");
		request = scan.nextLine();
		
		try {
			String putNickname = "SELECT * FROM song.join_membership WHERE id = '"+ user.id +"'"; //id SELECT문
			ResultSet put = db.stmt.executeQuery(putNickname);
			
			if(put.next()) {
			nickname = put.getString("nickname");
			}
			request_sql = "INSERT INTO song.message_board (`id`, `nickname`, `request`) VALUES ('"+user.id+"', '"+nickname+"', '"+request+"')";//게시판 테이블에 값 저장
			db.stmt.executeUpdate(request_sql);
			System.out.println("\n\n                                노래 신청 게시판 작성 완료! 감사합니다! \n\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
	}//message_board
	
	static void clear_screen() {  //화면 깔끔하게 지우는 메소드
		try {
			Thread.sleep(1000);  //1초 뒤에 나오도록 함
		}catch(InterruptedException e) {
			e.printStackTrace();  
		}
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	} //clear_screen끝
	
	static void sleep() {  //1초 후에 나오게 하는 메서드
		try {
			Thread.sleep(1000);
		}catch(InterruptedException e) {
			e.printStackTrace();  
		}
	}
}//Song_Recommendation 끝


