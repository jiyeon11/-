package 노래추천;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
//https://blog.naver.com/wusemr2/222205479918

public class Manager extends JFrame{
	JTextField id_tf, pw_tf;  //id와 비번 텍스트 필드
	JButton rogin_button = new JButton("로그인"); //로그인 버튼
	JPanel rogin_panel = new JPanel();  //로그인 화면 패널
	JPanel insertion_panel = new JPanel();  //노래 추가 화면 패널
	JPanel delete_panel = new JPanel();  //노래 삭제 화면 패널
	JPanel todo_panel = new JPanel();  //할 일 선택 패널
	JPanel showList_panel = new JPanel();  //조회 패널
	JPanel today_song_panel = new JPanel();  //오노추 등록 패널
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
			todo_panel.add(todoCombo); //할일 콤보박스 추가
			todo_panel.add(choice);  //선택 버튼 추가
			todo_panel.setBackground(Color.LIGHT_GRAY); //north부분 라이트그레이로 컬러설정
			add(todo_panel,"North");
			todo_panel.setVisible(false);
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
					todo_panel.setVisible(true);
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
					delete_panel.removeAll();
					showList_panel.removeAll();
					today_song_panel.removeAll();
					delete_panel.setVisible(false);
					showList_panel.setVisible(false);
					today_song_panel.setVisible(false);
					try {
						todo.insertion();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					insertion_panel.setVisible(true);
					break;
					
				case "노래 삭제" :
					showList_panel.removeAll();
					today_song_panel.removeAll();
					insertion_panel.removeAll();
					insertion_panel.setVisible(false);
					showList_panel.setVisible(false);
					today_song_panel.setVisible(false);
					try {
						todo.delete();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					delete_panel.setVisible(true);
					break;
					
				case "노래 목록 보기" : 
					delete_panel.removeAll();
					today_song_panel.removeAll();
					insertion_panel.removeAll();
					insertion_panel.setVisible(false);
					delete_panel.setVisible(false);
					today_song_panel.setVisible(false);
					todo.showList();
					showList_panel.setVisible(true);
					break;
					
				case "오노추 등록" : 
					delete_panel.removeAll();
					showList_panel.removeAll();
					insertion_panel.removeAll();
					insertion_panel.setVisible(false);
					delete_panel.setVisible(false);
					showList_panel.setVisible(false);
					todo.today_song_recommendation();
					today_song_panel.setVisible(true);
					break;
					
				case "게시판 조회" :
					delete_panel.removeAll();
					showList_panel.removeAll();
					today_song_panel.removeAll();
					insertion_panel.removeAll();
					insertion_panel.setVisible(false);
					delete_panel.setVisible(false);
					showList_panel.setVisible(false);
					today_song_panel.setVisible(false);
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
			singerList_check[0] = "-";  //텍필용
			
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
			
			insertion_label.setBounds(185,-30,300, 300);  //노래 추가 타이틀라벨 
			genre_label.setBounds(130, 130, 130, 130); //"노래 장르" 라벨 
			genre_combo.setBounds(300, 185, 170, 30);  //장르 콤보 
			title_label.setBounds(130, 200, 130, 130);  //"노래 제목" 라벨 
			title_tf.setBounds(300, 255, 170, 30);   //노래제목 텍필 
			singer_label.setBounds(130, 270, 130, 130); //"노래 가수" 라벨 
			singer_tf.setBounds(300, 365, 170, 30);   //가수 텍필 
			situ_label.setBounds(130, 380, 130, 130);  //"노래 상황" 라벨 
			situ_combo.setBounds(300, 435, 170, 30);  //노래 상황 콤보 
			route_label.setBounds(130, 450, 130, 130);  //"노래 경로" 라벨 
			route_tf.setBounds(300, 500, 170, 30);  //경로 텍필 
			insertion_button.setBounds(200, 600, 200, 80);  //노래 추가 버튼 
			
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
						if(singer_combo.getSelectedItem().toString().equals("-")) {
							singer = singer_tf.getText().toString();
						}else {
							singer = singer_combo.getSelectedItem().toString();
						}
						System.out.println(singer);
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
								System.out.println(insertsong);
								String ohnochoo = "INSERT INTO `song`.`ohnochoo_all_song` (`singer`, `title`, `situation`, `song_path`) VALUES ('"+singer+"', '"+title+"', '"+situ+"', '"+song_path+"')";  //오노추 테이블에도 동시에 추가
								db.stmt.executeUpdate(insertsong);
								db.stmt.executeUpdate(ohnochoo);
								JOptionPane.showMessageDialog(Manager.this, "노래 추가 완료");
							} catch(Exception e1) {
								JOptionPane.showMessageDialog(Manager.this, "노래 추가 실패");
							}
						}
					}
				}
			};//ActionListener
			
			insertion_button.addActionListener(insertion);  //노래 추가 버튼 액션리스너
			insertion_panel.add(insertion_button);
			insertion_panel.add(singer_combo);
			insertion_panel.add(singer_tf);
//			insertion_panel.setBackground(new Color(217,229,255));
			add(insertion_panel,"Center");  //할 일 부분
			insertion_panel.setVisible(false);
		}//insertion()
		
		//문제점 : 노래 가수 콤보박스에 첫번째를 수정해야함
		void delete() throws SQLException {  //노래 삭제
			DB db = new DB();
			db.connect();
			JLabel delete_label = new JLabel("노래 삭제"); //제목
			JLabel singer_label = new JLabel("노래 가수");
			JLabel title_label = new JLabel("노래 제목");
			JLabel genre_label = new JLabel("노래 장르");
			JButton choice_button = new JButton("선택");
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
			delete_label.setBounds(185, -30, 300, 300);  //노래 삭제 타이틀라벨 
			genre_label.setBounds(80, 150, 160, 160);  //"노래 장르" 라벨 
			genre_combo.setBounds(270,220,170,30);    //장르 콤보 
			singer_label.setBounds(80, 250, 160, 160); //"노래 가수" 라벨 
			singer_combo.setBounds(270, 320, 170, 30);   //가수 콤보 
			choice_button.setBounds(450, 320, 60, 30); //가수 선택 버튼 
			title_label.setBounds(80, 350, 160, 160);  //"노래 제목" 라벨 
			delete_button.setBounds(200, 550, 200, 80);//노래 삭제 버튼 
			title_combo.setBounds(270, 420, 170, 30);  //제목 콤보박스 
			
			ActionListener delete_Listener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Vector<String> vector = new Vector<String>();
					JButton check = (JButton)e.getSource();
					if(check.getText().equals("선택")) {
						vector.clear(); //요소 삭제
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
						ResultSet delete = null;
						
						try {
							delete = db.stmt.executeQuery(del_check_sql);
							while(delete.next()) { //선택한 가수의 노래 제목을 백터에 넣기
								vector.add(delete.getString("title"));
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						ComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
						title_combo.setModel(model);
						
					}else if (check.getText().equals("노래 삭제")){  //노래 삭제
						del_title = title_combo.getSelectedItem().toString();
						String deleteSong = "DELETE FROM "+ genre +" where title='"+del_title+"'"; //노래 지우는 sql
						String deleteOhnochooSong = "DELETE FROM ohnochoo_all_song WHERE title='"+del_title+"'"; //오노추도 같이 삭제
						
						try {
							db.stmt.executeUpdate(deleteOhnochooSong);
							db.stmt.executeUpdate(deleteSong);
							JOptionPane.showMessageDialog(Manager.this, "노래 삭제 완료");
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(Manager.this, "노래 삭제 실패");
						}
					}
				}
			};//액션리스너
			
			delete_panel.add(title_combo);
			choice_button.addActionListener(delete_Listener);
			delete_button.addActionListener(delete_Listener);
			delete_panel.add(delete_button);
			delete_panel.add(delete_label);
			delete_panel.add(singer_label);
			delete_panel.add(singer_combo);
			delete_panel.add(title_label);
			delete_panel.add(genre_label);
			delete_panel.add(genre_combo);
			delete_panel.add(choice_button);
//			delete_panel.setBackground(new Color(217,229,255));
			add(delete_panel,"Center");
			delete_panel.setVisible(false);
		}//delete()
//----------------------------------------------------------------------------------------
		void showList(){  //노래 조회
			DB db = new DB();
			db.connect();
			
			showList_panel.setLayout(null);
			String[] criteria_List = {"전체","장르별","상황별"};
			JComboBox<String> criteria_combo = new JComboBox<String>(criteria_List);
			JLabel refer_label = new JLabel("노래 조회");
			JLabel criteria_label = new JLabel("조회 기준");
			JButton check_button = new JButton("선택");
			JButton show_button = new JButton("조회");
			JLabel situ_label = new JLabel("상황");
			JLabel genre_label = new JLabel("장르");
			String[] columnName = {"가수","제목","상황","경로"};
			Object[][] rowData = new Object[0][4];
			DefaultTableModel dtm = new DefaultTableModel(rowData,columnName);
			JTable showList_table = new JTable(dtm);  //조회 테이블
			DefaultTableCellRenderer celAlignLeft = new DefaultTableCellRenderer();
			celAlignLeft.setHorizontalAlignment(JLabel.LEFT);
			
			showList_table.getColumn("가수").setPreferredWidth(3);		/*     */
			showList_table.getColumn("제목").setPreferredWidth(15);		/*셀 조정*/
			showList_table.getColumn("상황").setPreferredWidth(8);		/*     */
			showList_table.getColumn("경로").setPreferredWidth(170);		/*     */
			//폰트
			refer_label.setFont(title_font);
			criteria_label.setFont(new Font("맑은 고딕",Font.PLAIN,20));
			showList_table.setFont(new Font("맑은 고딕",Font.PLAIN,12));
			situ_label.setFont(new Font("맑은 고딕",Font.PLAIN,20));
			genre_label.setFont(new Font("맑은 고딕",Font.PLAIN,20));
			show_button.setFont(new Font("상주곶감체",Font.PLAIN, 15));
// --------------------------셋바운즈----------------------------------------------------
			refer_label.setBounds(185, -100, 300, 300);  //"노래 조회" 제목 라벨
			criteria_label.setBounds(20, 105, 100, 100);  //"조회 기준" 라벨
			criteria_combo.setBounds(115, 143, 70, 30);   //조회 기준 콤보박스
			check_button.setBounds(195, 143, 60, 30);    //선택 버튼 
			show_button.setBounds(480, 143, 80, 30);  //조회 버튼 
			showList_table.setBounds(0, 210, 580, 500);  //테이블  
			situ_label.setBounds(285, 105, 100, 100);  //상황 라벨 
			situ_combo.setBounds(335, 143, 100,30);   //상황 콤보 
			genre_label.setBounds(285, 105, 100, 100);  //장르 라벨 
			genre_combo.setBounds(335, 143, 100,30);  //장르 콤보 
			//add
			showList_panel.add(situ_label);
			showList_panel.add(situ_combo);
			showList_panel.add(genre_label);
			showList_panel.add(genre_combo);
			//setVisible
			situ_label.setVisible(false);
			situ_combo.setVisible(false);
			genre_label.setVisible(false);
			genre_combo.setVisible(false);
// --------------------------------------------------------------------------------------
			ActionListener showListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Vector<String> vector = new Vector<String>();
					JButton check = (JButton)e.getSource();
					String show_sql;
					ResultSet genre_show;
					ResultSet situ_show;
					ResultSet all_show;
					
					if(check.getText().equals("선택")) {
						switch(criteria_combo.getSelectedItem().toString()) {
						case "전체" : //흠? 빡대가리라 그런지 생각할 시간이 필요함
							situ_label.setVisible(false);
							situ_combo.setVisible(false);
							genre_label.setVisible(false);
							genre_combo.setVisible(false);
							break;
						case "장르별" :
							situ_label.setVisible(false);
							situ_combo.setVisible(false);
							genre_label.setVisible(true);
							genre_combo.setVisible(true);
							break;
						case "상황별" : 
							genre_label.setVisible(false);
							genre_combo.setVisible(false);
							situ_label.setVisible(true);
							situ_combo.setVisible(true);
							break;
						}
					}
//-----------------------------------------------------------------------------------------
					if(check.getText().equals("조회")) {
						dtm.getDataVector().removeAllElements();  //테이블 초기화
						if(criteria_combo.getSelectedItem().toString().equals("전체")) {
							show_sql = "SELECT * FROM song.ohnochoo_all_song ORDER BY singer ASC";
							try {
								all_show = db.stmt.executeQuery(show_sql);
								while(all_show.next()) {
									vector.clear();
									vector.add(all_show.getString(1));
									vector.add(all_show.getString(2));
									vector.add(all_show.getString(3));
									vector.add(all_show.getString(4));
									Object[] list = vector.toArray();
									dtm.addRow(list);
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
///////////////////////////////////////////////////////////////////////////////////////////////////////
						if(criteria_combo.getSelectedItem().toString().equals("장르별")) {
							switch(genre_combo.getSelectedItem().toString()) {
							case "여자아이돌" : genre = "girl_group"; break;
							case "남자아이돌" : genre = "boy_group"; break;
							case "가요" : genre = "kpop"; break;
							case "팝송" : genre = "pop"; break;
							case "제이팝" : genre = "jpop"; break;
							}
							show_sql = "SELECT * FROM song." + genre +" ORDER BY singer ASC";
							try {
								genre_show = db.stmt.executeQuery(show_sql);
								while(genre_show.next()) {
									vector.clear();
									vector.add(genre_show.getString(1));
									vector.add(genre_show.getString(2));
									vector.add(genre_show.getString(3));
									vector.add(genre_show.getString(4));
									Object[] list = vector.toArray();
									dtm.addRow(list);
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
					}
///////////////////////////////////////////////////////////////////////////////////////////////////////////
						if(criteria_combo.getSelectedItem().toString().equals("상황별")) {
							show_sql = "SELECT * FROM song.ohnochoo_all_song WHERE situation = '" + situ_combo.getSelectedItem().toString() +"' ORDER BY singer ASC";
							try {
								situ_show = db.stmt.executeQuery(show_sql);
								while(situ_show.next()) {
									vector.clear();
									vector.add(situ_show.getString(1));
									vector.add(situ_show.getString(2));
									vector.add(situ_show.getString(3));
									vector.add(situ_show.getString(4));
									Object[] list = vector.toArray();
									dtm.addRow(list);
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			};//showListener
//--------------------------------------------------------------------------------------	
			//add
			check_button.addActionListener(showListener);
			show_button.addActionListener(showListener);
			showList_panel.add(refer_label);
			showList_panel.add(criteria_label);
			showList_panel.add(criteria_combo);
			showList_panel.add(check_button);
			showList_panel.add(show_button);
			showList_panel.add(showList_table);
			add(showList_panel);
			showList_panel.setVisible(false);
		}//showList()
//--------------------------------------------------------------------------------------
		//오노추 등록 메서드
		void today_song_recommendation(){
			DB db = new DB();
			db.connect();
			today_song_panel.setLayout(null);
			JLabel register_label = new JLabel("오노추 등록");
			JLabel reason_label = new JLabel("선정 이유");
			JLabel singer_label = new JLabel("가수");
			JLabel title_label = new JLabel("노래 제목");
			JButton register_button = new JButton("오노추 등록");
			JTextField textArea = new JTextField("간략한 선정 이유를 적어주세요.");
			Font part_title_font = new Font("상주곶감체",Font.BOLD,35);
			
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);            //년도
			int month = now.get(Calendar.MONTH)+1;        //달
			int day = now.get(Calendar.DAY_OF_MONTH);     //일
			int dayOfweek = now.get(Calendar.DAY_OF_WEEK);//요일
			
			//font
			register_label.setFont(title_font);
			reason_label.setFont(part_title_font);
			singer_label.setFont(part_title_font);
			title_label.setFont(part_title_font);
			register_button.setFont(Manager.this.part_title_font);
			//셋바운즈
			register_label.setBounds(145, -80, 350, 300);  //"오노추 등록" 제목 라벨
			singer_label.setBounds(155+20, 100, 160, 160);  //가수 라벨
			singer_combo.setBounds(300, 165, 120, 30);   //가수 콤보
			reason_label.setBounds(80+20, 280, 160, 160);  //등록 이유 라벨
			title_label.setBounds(80+20, 190, 160, 160);  //노래 제목 라벨
			title_combo.setBounds(300, 260, 120, 30);  //노래 제목 콤보
			textArea.setBounds(300, 335, 180, 60);       //텍스트필드
			register_button.setBounds(200, 500, 180, 80);  //오노추 등록 버튼
			
			//콤보 액션리스너
			ActionListener combo_listener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Vector<String> vector = new Vector<>();
					JComboBox<String> check = (JComboBox<String>) e.getSource();
					singer = check.getSelectedItem().toString();
					String del_check_sql = "SELECT * FROM song.ohnochoo_all_song WHERE singer = '" + singer + "'";  //가수 노래 제목 title
					ResultSet title = null;
					//선택한 가수의 노래 제목을 백터에 넣기
					try {
						title = db.stmt.executeQuery(del_check_sql);
						while(title.next()) { 
							vector.add(title.getString("title"));
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					ComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
					title_combo.setModel(model);
				}
			};//comboActionListener
			
			ActionListener btn_listener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Object check = e.getSource();
					String dayOfWeek_str = null;  //요일 String변수
					String title = title_combo.getSelectedItem().toString();
					switch(dayOfweek) {  //요일 설정
					case Calendar.SUNDAY : dayOfWeek_str = "일요일"; break;
					case Calendar.MONDAY : dayOfWeek_str = "월요일"; break;
					case Calendar.TUESDAY : dayOfWeek_str = "화요일"; break;
					case Calendar.WEDNESDAY : dayOfWeek_str = "수요일"; break;
					case Calendar.THURSDAY : dayOfWeek_str = "목요일"; break;
					case Calendar.FRIDAY : dayOfWeek_str = "금요일"; break;
					case Calendar.SATURDAY : dayOfWeek_str = "토요일"; break;
					}
					
					if(((AbstractButton) check).getText().equals("오노추 등록")) {
						String comment = textArea.getText();
						String register_ohnochoo = "INSERT INTO `song`.`ohnochoo` (`DATE`, `dayOfWeek`, `Select_song`, `singer`, `comment`) VALUES ('"+year+"-"+month+"-"+day+"', '"+dayOfWeek_str+"', '"+title+"', '" + singer +"', '"+ comment +"')"; //오노추 제목과 코멘트 추가
						try {
							db.stmt.executeUpdate(register_ohnochoo);
							JOptionPane.showMessageDialog(Manager.this, "오노추 등록 완료!");
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(Manager.this, "오늘의 오노추를 이미 등록하셨습니다."); //오노추 이미 등록했을때
						}
					}
				}
			};//btnListener
			
			//add
			singer_combo.addActionListener(combo_listener);
			register_button.addActionListener(btn_listener);
			today_song_panel.add(title_combo);
			today_song_panel.add(textArea);
			today_song_panel.add(register_label);
			today_song_panel.add(singer_label);
			today_song_panel.add(reason_label);
			today_song_panel.add(title_label);
			today_song_panel.add(singer_combo);
			today_song_panel.add(register_button);
			add(today_song_panel);
			today_song_panel.setVisible(false);
		}//today_song_recommendation()
	}//Todo
}//Manager

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