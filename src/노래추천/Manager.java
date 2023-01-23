package 노래추천;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
//https://blog.naver.com/wusemr2/222205479918

public class Manager extends JFrame{
	JTextField id_tf, pw_tf;  //id와 비번 텍스트 필드
	JButton rogin_button = new JButton("로그인"); //로그인 버튼
	JPanel rogin_panel = new JPanel();  //로그인 화면 패널
	JPanel insertion_panel = new JPanel();  //노래 추가 화면 패널
	JPanel delete_panel = new JPanel();  //노래 삭제 화면 패널
	JPanel panel_rogin_north = new JPanel();  //로그인 화면 north패널
	JPanel panel_rogin_center = new JPanel();//로그인 화면 south패널
	String[] todoList = {"노래 추가","노래 삭제","노래 목록 보기","오노추 등록","게시판 조회"}; //할일 리스트
	JComboBox<String> todoCombo = new JComboBox<>(todoList);  //할일 콤보박스
	String[] genreList = {"여자아이돌","남자아이돌","가요","팝송","제이팝"};  //장르 콤보박스 리스트
	JComboBox<String> genre_combo = new JComboBox<String>(genreList);  //장르 콤보박스
	String[] situList = {"기분 좋을 때","집중할 때","우울할 때"};  //노래 상황 리스트
	JComboBox<String> situ_combo = new JComboBox<String>(situList);  //상황 콤보박스
	String[] singerList; //가수 리스트 /이거 ㅆㅂ 가수 수를 어떻게 구해 내가??
	JComboBox<String> singer_combo; //가수 콤보
	Font title_font = new Font("상주곶감체",Font.BOLD,55);
	JButton choice = new JButton("선택");
	JPanel panel_todo = new JPanel();  //할 일 선택 패널
	Font part_title_font = new Font("상주곶감체",Font.BOLD,25);  //부제목?폰트
	
	public static void main(String[] args) throws SQLException {
		new Manager();
	}//main
	
	public Manager() throws SQLException{
		new Manager_rogin();  //로그인 화면 뭐.
		add(rogin_panel);  //로그인 화면
		rogin_button.addActionListener(button_Listener);  //로그인 버튼 액션 리스너
		setTitle("지연이의 노래 추천 관리자창");
		setBounds(500, 150, 600, 800);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	class Manager_rogin{
		JLabel id_label, pw_label;  //id와 비번 라벨
		JLabel rogin_label = new JLabel("<html><center>지연이의 노래추천 프로젝트<br>관리자 로그인</center></html>"); //로그인 라벨
		
		public Manager_rogin(){
			rogin_panel.setLayout(new BorderLayout()); //보더레이아웃으로 설정
			panel_rogin_center.setLayout(null);
			panel_rogin_north.setBackground(Color.lightGray);
			Font font = new Font("맑은 고딕",Font.BOLD,35);
			id_label = new JLabel("아이디 ");
			pw_label = new JLabel("비밀번호 ");
			id_tf = new JTextField("jiyeon");
			pw_tf = new JTextField("jiyeon2329!");
			
			//폰트 설정
			rogin_label.setFont(font);
			id_label.setFont(font);
			pw_label.setFont(font);
			rogin_button.setFont(font);
			
			//위치설정
			id_label.setBounds(100, 30, 130, 130);
			id_tf.setBounds(300, 70, 150, 50);
			pw_label.setBounds(100, 100, 170, 170);
			pw_tf.setBounds(300, 160, 150, 50);
			rogin_button.setBounds(200,400,200,100);
			
			//add
			panel_rogin_north.add(rogin_label); //로그인라벨
			panel_rogin_center.add(id_label);  //아이디라벨
			panel_rogin_center.add(id_tf);    //아이디 텍스트필드
			panel_rogin_center.add(pw_label);  //비번라벨
			panel_rogin_center.add(pw_tf);    //비번 텍스트필드
			panel_rogin_center.add(rogin_button);
			rogin_panel.add(panel_rogin_north,"North");
			rogin_panel.add(panel_rogin_center,"Center");
			choice.addActionListener(button_Listener); //할일 선택버튼 액션리스너 
			//맨 위에 할일 부분 panel
			panel_todo.add(todoCombo); //할일 콤보박스 추가
			panel_todo.add(choice);  //선택 버튼 추가
			panel_todo.setBackground(Color.LIGHT_GRAY); //north부분 라이트그레이로 컬러설정
			add(panel_todo,"North");
			panel_todo.setVisible(false);
		}
	}
	
	ActionListener button_Listener = new ActionListener() { //로그인버튼 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton check = (JButton)e.getSource();
			
			if(check.getText().equals("로그인")) {
				if(id_tf.getText().equals(DB.getId()) && pw_tf.getText().equals(DB.getPassword())) { //아이디와 패스워드가 일치하면
					JOptionPane.showMessageDialog(Manager.this, "로그인 성공!  환영합니다");
					rogin_panel.setVisible(false);
					panel_todo.setVisible(true);
				}else { //불일치
					JOptionPane.showMessageDialog(Manager.this, "로그인 실패\n다시 시도해주세요");
				}
			}
			if(check.getText().equals("선택")) {
				Todo todo = null;
				try {
					todo = new Todo();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				switch(todoCombo.getSelectedItem().toString()) {
				case "노래 추가" :
					try {
						todo.insertion();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					insertion_panel.setVisible(true);
					delete_panel.setVisible(false);
					break;
					
				case "노래 삭제" :
					insertion_panel.setVisible(false);
					try {
						todo.delete();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					delete_panel.setVisible(true);
					break;
					
				case "노래 목록 보기" : 
					insertion_panel.setVisible(false);
					delete_panel.setVisible(false);
					break;
					
				case "오노추 등록" : 
					insertion_panel.setVisible(false);
					delete_panel.setVisible(false);
					break;
					
				case "게시판 조회" :
					insertion_panel.setVisible(false);
					delete_panel.setVisible(false);
					break;
				}
			}
		}
	};
	
	class Todo{  //노래 추가
		//sql용
		String singer = null; //sql 가수
		String genre = null;  //sql 장르
		String title = null;  //sql 제목
		String situ = null;   //sql 상황
		String song_path = null; //sql 노래 경로
		String del_title = "";  //삭제할 제목
		String del_table = "";  //삭제할 노래의 테이블
		String del_singer ="";  //삭제할 노래의 가수

		String[] title_List = {"노래없음"};  //제목 리스트
		JComboBox title_combo = new JComboBox<String>(title_List);  //제목 콤보박스; //제목 콤보박스
		public Todo() throws SQLException {
			DB db = new DB();
			db.connect();
			int i = 1;
			String[] singerList_check = new String[200];  //가수 리스트 확인용
			String singer_sql = "SELECT * FROM song.ohnochoo_all_song order by singer ASC";
			ResultSet singer_check = db.stmt.executeQuery(singer_sql);
			int count = 1;
			singerList_check[0] = "여기없음(직접입력)";  //텍필용
			
			while(singer_check.next()) {  //가수 수 세기... 하..
				singerList_check[i] = singer_check.getString("singer");
				
				if(singerList_check[i].equals(singerList_check[i-1])) {  //가수 목록이 같으면
					continue;
				}
				else {
					singerList_check[i] = singer_check.getString("singer");
					count++;
					i++;
				}
			}
			singerList = new String[count];
			
			for(i = 0; i<count; i++) {  //가수 리스트 생성
				singerList[i] = singerList_check[i];
			}
			
			singer_combo = new JComboBox<String>(singerList);//선택한 장르에 있는 가수 리스트
		}//Todo()
		
		void insertion() throws SQLException {
			DB db = new DB();
			db.connect();
			JLabel insertion_label = new JLabel("노래 추가");  //제목?
			JButton insertion_button = new JButton("노래 추가");
			JLabel title_label = new JLabel("노래 제목");
			JLabel genre_label = new JLabel("노래 장르");
			JLabel singer_label = new JLabel("노래 가수");
			JLabel situ_label = new JLabel("노래 상황");
			JLabel route_label = new JLabel("노래 경로");
			JTextField route_tf = new JTextField();  //노래 경로 텍필
			JTextField singer_tf = new JTextField(); //가수 텍필
			JTextField title_tf = new JTextField();  //제목 텍필
			
			//배치관리자
			setLayout(new BorderLayout());
			insertion_panel.setLayout(null);
			
			//폰트 설정
			insertion_label.setFont(title_font);
			genre_label.setFont(part_title_font);
			title_label.setFont(part_title_font);
			singer_label.setFont(part_title_font);
			situ_label.setFont(part_title_font);
			route_label.setFont(part_title_font);
			insertion_button.setFont(part_title_font);
			
			insertion_label.setBounds(185,-30,300, 300);  //노래 추가 타이틀라벨 셋바운즈
			genre_label.setBounds(130, 130, 130, 130); //"노래 장르" 라벨 셋바운즈
			genre_combo.setBounds(300, 185, 170, 30);  //장르 콤보 셋바운즈
			title_label.setBounds(130, 200, 130, 130);  //"노래 제목" 라벨 셋바운즈
			title_tf.setBounds(300, 255, 170, 30);   //노래제목 텍필 셋바운즈
			singer_label.setBounds(130, 270, 130, 130); //"노래 가수" 라벨 셋바운즈
			singer_tf.setBounds(300, 365, 170, 30);   //가수 텍필 셋바운즈
			situ_label.setBounds(130, 380, 130, 130);  //"노래 상황" 라벨 셋바운즈
			situ_combo.setBounds(300, 435, 170, 30);  //노래 상황 콤보 셋바운즈
			route_label.setBounds(130, 450, 130, 130);  //"노래 경로" 라벨 셋바운즈
			route_tf.setBounds(300, 500, 170, 30);  //경로 텍필 셋바운즈
			insertion_button.setBounds(200, 600, 200, 80);  //노래 추가 버튼 셋바운즈
			
			insertion_panel.add(insertion_label);
			insertion_panel.add(genre_label);
			insertion_panel.add(genre_combo);
			insertion_panel.add(title_label);
			insertion_panel.add(title_tf);
			insertion_panel.add(singer_label);
			insertion_panel.add(situ_label);
			insertion_panel.add(situ_combo);
			insertion_panel.add(route_label);
			insertion_panel.add(route_tf);
			
			singer_combo.setBounds(300, 325, 170, 30);  //가수 콤보 셋바운즈
			
			ActionListener insertion = new ActionListener() {  //노래 추가용 액션리스너
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton check = (JButton)e.getSource();
					if(check.getText().equals("노래 추가")) {
						//가수 갖고오기~
						if(singer_combo.getSelectedItem().toString().equals("여기없음(직접입력)")) {
							singer = singer_tf.getText().toString();
						}else {
							singer = singer_combo.getSelectedItem().toString();
						}
						//장르 갖고오기~
						switch(genre_combo.getSelectedItem().toString()) {
						case "여자아이돌" : genre = "girl_group"; break;
						case "남자아이돌" : genre = "boy_group"; break;
						case "가요" : genre = "kpop"; break;
						case "팝송" : genre = "pop"; break;
						case "제이팝" : genre = "jpop"; break;
						}
						//제목 갖고오기~
						title = title_tf.getText().toString();
						//상황 갖고오기~
						situ = situ_combo.getSelectedItem().toString();
						//경로 갖고오기~
						song_path = route_tf.getText().toString();
						//노래 추가
						if(title.equals("") || song_path.equals("")) { //제목이랑 경로 입력안하면 추가안됨
							JOptionPane.showMessageDialog(Manager.this, "빈칸이 있습니다. 모두 채워주세요.");
						}else {
							try {                                                
								String insertsong = "INSERT INTO `song`.`"+genre+"` (`singer`, `title`, `situation`, `song_path`) VALUES ('"+singer+"', '"+title+"', '"+situ+"', '"+song_path+"')";
								String ohnochoo = "INSERT INTO `song`.`ohnochoo_all_song` (`singer`, `title`, `situation`, `song_path`) VALUES ('"+singer+"', '"+title+"', '"+situ+"', '"+song_path+"')";  //오노추 테이블에도 동시에 추가
								db.stmt.executeUpdate(insertsong);
								db.stmt.executeUpdate(ohnochoo);
								System.out.println("\n노래 추가 성공!\n");
							} catch(Exception e1) {
								System.out.println("\n노래 추가 실패!: " + e.toString() + "\n");
							}
						}
					}
				}
			};//ActionListener
			
			insertion_button.addActionListener(insertion);  //노래 추가 버튼 액션리스너
			insertion_panel.add(insertion_button);
			insertion_panel.add(singer_combo);
			insertion_panel.add(singer_tf);
			insertion_panel.setBackground(new Color(217,229,255));
			add(insertion_panel,"Center");  //할 일 부분
			insertion_panel.setVisible(false);
		}//insertion()
		
		void delete() throws SQLException {  //노래 삭제
			DB db = new DB();
			db.connect();
			JLabel delete_label = new JLabel("노래 삭제"); //제목
			JLabel singer_label = new JLabel("노래 가수");
			JLabel title_label = new JLabel("노래 제목");
			JLabel genre_label = new JLabel("노래 장르");
			JButton choice = new JButton("선택");
			JButton delete_button = new JButton("노래 삭제");
			Font part_title_font = new Font("상주곶감체",Font.BOLD,35);
			
			delete_panel.setLayout(null);  //배치관리자
			//폰트설정
			delete_label.setFont(title_font);
			title_label.setFont(part_title_font);
			singer_label.setFont(part_title_font);
			genre_label.setFont(part_title_font);
			delete_button.setFont(part_title_font);
			
			//셋바운즈
			delete_label.setBounds(185, -30, 300, 300);  //노래 삭제 타이틀라벨 셋바운즈
			genre_label.setBounds(80, 150, 160, 160);  //"노래 장르" 라벨 셋바운즈
			genre_combo.setBounds(270,220,170,30);    //장르 콤보 셋바운즈
			singer_label.setBounds(80, 250, 160, 160); //"노래 가수" 라벨 셋바운즈
			singer_combo.setBounds(270, 320, 170, 30);   //가수 콤보 셋바운즈
			choice.setBounds(450, 320, 60, 30); //가수 선택 버튼 셋바운즈
			title_label.setBounds(80, 350, 160, 160);  //"노래 제목" 라벨 셋바운즈
			delete_button.setBounds(200, 600, 200, 80);//노래 삭제 버튼 셋바운즈
			
			ActionListener delete_Listener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int count = 0;
					int i = 0;
					Vector vector = new Vector<String>();
					JButton check = (JButton)e.getSource();
					if(check.getText().equals("선택")) {
						//가수 갖고오기~
						del_singer = singer_combo.getSelectedItem().toString();
						//장르 갖고오기~
						switch(genre_combo.getSelectedItem().toString()) {
						case "여자아이돌" : genre = "girl_group"; break;
						case "남자아이돌" : genre = "boy_group"; break;
						case "가요" : genre = "kpop"; break;
						case "팝송" : genre = "pop"; break;
						case "제이팝" : genre = "jpop"; break;
						}
						
						String del_check_sql = "SELECT * FROM " + genre + " WHERE singer = '" + del_singer+ "'";  //입력한 가수 select문
						System.out.println(del_check_sql);
						ResultSet delete = null;
						ResultSet delete1 = null;
						try {
							delete = db.stmt.executeQuery(del_check_sql);
							delete1 = db.stmt1.executeQuery(del_check_sql);
							while(delete.next()) {  //선택한 가수 노래 갯수 세기
								count++;
							}
							title_List = new String[count];
							while(delete1.next()) { //선택한 가수 제목을 배열에 넣기
								title_List[i] = delete1.getString("title");
								System.out.println(title_List[i]);
								vector.add(title_List[i]);
								i++;
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						ComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
						title_combo.setModel(model);
					}
				}
			};//액션리스너
			
			title_combo.setBounds(270, 420, 170, 30);  //제목 콤보박스 셋바운즈
			delete_panel.add(title_combo);
			choice.addActionListener(delete_Listener);
			delete_panel.add(delete_button);
			delete_panel.add(delete_label);
			delete_panel.add(singer_label);
			delete_panel.add(singer_combo);
			delete_panel.add(title_label);
			delete_panel.add(genre_label);
			delete_panel.add(genre_combo);
			delete_panel.add(choice);
			add(delete_panel,"Center");
			delete_panel.setVisible(false);
		}//delete()
		
	}//Todo
}//Manager

//		System.out.println("=============================");
//		System.out.println("삭제하실 노래의 가수명을 입력해주세요");
//		System.out.print(">>>");
//		del_singer_name = sc.nextLine();
//		
//		del_check_sql = "SELECT * FROM " + del_table + " WHERE singer = '" + del_singer_name + "'";  //입력한 가수 select문
//		ResultSet delete = db.stmt.executeQuery(del_check_sql);
//		
//			String del_check = "SELECT * FROM " + del_table + " WHERE title = '"+ del_title + "'";
//			ResultSet del_song = db.stmt.executeQuery(del_check);
//			
//			if(del_song.next()) {  //노래 제목이 일치하면 삭제
//				try {
//					String removeSong = "DELETE FROM "+del_table+" where title='"+del_title+"'"; //노래 제목 입력하면 지우는 sql문
//					String removeOhnochooSong = "DELETE FROM ohnochoo_all_song WHERE title='"+del_title+"'"; //오노추 노래 테이블도 같이 삭제
//					db.stmt.executeUpdate(removeOhnochooSong);
//					db.stmt.executeUpdate(removeSong);
//					System.out.println("\n노래 삭제 성공!\n");
//				} catch(Exception e) {
//					System.out.println("\n노래 삭제 실패 이유 : " + e.toString() + "\n");
//				}
//			}else {
//				System.out.println("\n입력하신 노래는 존재하지 않습니다.\n");
//			}
//		
//		}else {  //입력한 가수가 db에 없으면
//			System.out.println("=============================");
//			System.out.println("입력하신 가수는 현재 db에 없습니다.");
//		}
//		Sleep();
//	}//노래 삭제 메서드 끝
//	
//	
//	
//	static void Show_SongList() throws SQLException{   //노래 목록 보기
//		Scanner scan = new Scanner(System.in);
//		DB db = new DB();
//		db.connect();
//		String song_table = null;
//		do {
//			System.out.println("=============================");
//			System.out.println("조회할 노래 장르를 선택해주세요");
//			System.out.println("▶1번 여자아이돌");
//			System.out.println("▷2번 남자아이돌");
//			System.out.println("▶3번 가요");
//			System.out.println("▷4번 팝송");
//			//System.out.println("▶5번 제이팝");
//			System.out.println("=============================");
//			System.out.print("번호를 입력해주세요 >>");
//			int select_table = scan.nextInt();
//			switch(select_table) {
//			case 1 : song_table = "girl_group"; break;
//			case 2 : song_table = "boy_group"; break;
//			case 3 : song_table = "kpop"; break;
//			case 4 : song_table = "pop"; break;
//			//case 5 : song_table = "jpop"; break;
//			default : song_table = "입력실패";
//				System.out.println("\n!잘못 입력하셨습니다. 다시 입력해주세요!\n");
//			}
//		}while(song_table.equals("입력실패"));
//		
//		System.out.println("               ======"+song_table + " 테이블 조회======");
//		String viewTable = "SELECT * FROM " + song_table +" ORDER BY singer ASC";  //가수 오름차순 정렬
//		ResultSet table = db.stmt.executeQuery(viewTable);
//		
//		System.out.println("---------------------------------------------------------------------------------");
//		try {  //테이블에 있는 가수 제목 상황 출력
//			while(table.next()){
//				System.out.printf("|%-23s	|%-25s	|%-7s	|\n",
//						table.getString("singer"),    //가수
//						table.getString("title"),     //제목
//						table.getString("situation")); //상황
//				System.out.println("---------------------------------------------------------------------------------");
//			}
//			
//		}catch(Exception e) {
//			System.out.println("테이블 조회 실패! : " + e.toString());
//		}
//		
//	}//SongList_show 끝
//	
//	static void today_song_recommendation() throws SQLException {   //오노추 등록
//		Scanner scan = new Scanner(System.in);
//		Scanner sc = new Scanner(System.in);
//		Calendar now = Calendar.getInstance();  //명품자바
//		DB db = new DB();
//		db.connect();
//		
//		int year = now.get(Calendar.YEAR);            //년도
//		int month = now.get(Calendar.MONTH)+1;        //달
//		int day = now.get(Calendar.DAY_OF_MONTH);     //일
//		int dayOfweek = now.get(Calendar.DAY_OF_WEEK);//요일
//		
//		String dayOfWeek_str = "";  //요일 String변수
//		String comment = "";  //코멘트
//		String title = ""; //노래제목
//		String singer = "";  //노래 가수
//		
//		switch(dayOfweek) {
//		case Calendar.SUNDAY : dayOfWeek_str = "일요일"; break;
//		case Calendar.MONDAY : dayOfWeek_str = "월요일"; break;
//		case Calendar.TUESDAY : dayOfWeek_str = "화요일"; break;
//		case Calendar.WEDNESDAY : dayOfWeek_str = "수요일"; break;
//		case Calendar.THURSDAY : dayOfWeek_str = "목요일"; break;
//		case Calendar.FRIDAY : dayOfWeek_str = "금요일"; break;
//		case Calendar.SATURDAY : dayOfWeek_str = "토요일"; break;
//		}
//		
//		System.out.println("==========오늘의 노래 추천==========");
//		System.out.print("노래 제목 입력 >>>");
//		title = scan.nextLine();
//		System.out.print("간략한 선정 이유를 작성해주세요 >>>");
//		comment = sc.nextLine();
//		
//		String check_song = "SELECT * FROM ohnochoo_all_song WHERE title = '"+title+"'"; //노래가 디비에 있는지 제목 확인
//		ResultSet check = db.stmt.executeQuery(check_song);
//		
//		if(check.next()) {  //노래가 db에 있으면 
//			try {
//				singer =  check.getString("singer");  //가수를 singer 변수에 넣기
//				String insert_Title_Comment = "INSERT INTO `song`.`ohnochoo` (`DATE`, `dayOfWeek`, `Select_song`, `singer`, `comment`) VALUES ('"+year+"-"+month+"-"+day+"', '"+dayOfWeek_str+"', '"+title+"', '" +singer +"', '"+ comment +"')"; //오노추 제목과 코멘트 추가
//				db.stmt.executeUpdate(insert_Title_Comment);
//				System.out.println("\n"+year+"-"+month+"-"+day + " " +dayOfWeek_str+" 오노추 작성 완료!\n"); //작성완료 출력
//			}catch (SQLIntegrityConstraintViolationException e) {  //이미 오늘의 노래가 등록되어 있다면 
//				System.out.println("\n오늘의 추천 노래를 이미 등록하셨습니다.\n");
//			}
//		}else {  //노래가 db에 없으면
//			System.out.println("\n제목을 잘못 적으셨거나 해당 노래가 db에 있지 않습니다! 다시 한 번 확인해주세요\n");
//		}
//		
//	}//today_song_recommendation 끝
//	
//	static void Show_message_board() throws SQLException {
//		DB db = new DB();
//		db.connect();
//		
//		String message_board_show = "SELECT * FROM song.message_board";  //게시판 테이블 select문
//		ResultSet board = db.stmt.executeQuery(message_board_show);
//		Sleep();
//		System.out.println("\n게시판");
//		
//		while(board.next()) { //닉네임, 글 출력
//			System.out.println("-------------------------------------------------------------------");
//			System.out.println("|" + board.getString("nickname"));
//			System.out.println("|"+ board.getString("request"));
//		}
//		System.out.println("-------------------------------------------------------------------\n");
//		Sleep();
//	}//Show_message_board
//	
//	static void Sleep() {   //느리게 나오기
//		try {
//			Thread.sleep(1000);  //1초 뒤에 나오도록 함
//		}catch(InterruptedException e) {
//			e.printStackTrace();  
//		}
//	}
