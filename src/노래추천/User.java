package 노래추천;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import javazoom.jl.player.MP3Player;

public class User extends JFrame{
	int frame_width = 700;
	int frame_height = 900;
	JPanel join_panel = new JPanel();  //회원가입화면
	JPanel rogin_panel = new JPanel(); //로그인화면
	JPanel first_panel = new JPanel();  //처음 화면
	JPanel menu_panel = new JPanel();  //메뉴 화면
	JPanel ohnochoo_panel = new JPanel();  //오노추 화면
	JPanel recommend_situation_question_panel = new JPanel();  //노래 상황 질문 화면
	JPanel recommend_genre_question_panel = new JPanel();  //노래 장르 질문 화면
	JPanel waiting_panel = new JPanel();  //대기화면
	JPanel recommend_song_listen_panel = new JPanel();  //추천 노래 듣는 화면
	JPanel roginORjoin_panel = new JPanel(); //로그인or회원가입 화면
	JLabel title_label = new JLabel("<html><center>지연이의<br>노래 추천</html></center>");
	String path;  //경로
	JLabel album;  //앨범이미지
	String genre;  //장르
	String situation;  //상황
	Information user = new Information();
	
	
	public User() {
		first_panel.setLayout(null);
		JButton go_recommend_button = new JButton("노래 추천 받으러 가기");
		ImageIcon start_guide_image = new ImageIcon("image/start_guide.png");
		JLabel start_guide = new JLabel(start_guide_image);
		go_recommend_button.addActionListener(start_listener);
		//font
		title_label.setFont(new Font("휴먼엑스포",Font.PLAIN,80));
		go_recommend_button.setFont(new Font("휴먼엑스포",Font.PLAIN,25));
		//setBounds
		title_label.setBounds(170, -30, 500, 400);
		go_recommend_button.setBounds(200,600,300,100);
		start_guide.setBounds(0, -50, frame_width, frame_height);
		//add
		first_panel.add(go_recommend_button);
		first_panel.add(title_label);
		first_panel.add(start_guide);
		add(first_panel);
		//기본설정
		setTitle("지연이의 노래 추천");
		setBounds(500, 70, frame_width, frame_height);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}//User()
	
	ActionListener start_listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			first_panel.setVisible(false);//처음 화면 안보이게
			menu();
			menu_panel.setVisible(true);
			roginORjoin(); //로그인할지 회원가입할지 묻는 메서드
			roginORjoin_panel.setVisible(true);
		}
	};
	
	ActionListener rogin_join_listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			roginORjoin_panel.setVisible(false);
			JButton check = (JButton) e.getSource();
			if(check.getText().equals("고..고멘...! 지금 바로 로그인할게...!!")) {
				rogin();
				rogin_panel.setVisible(true);
			}
			if(check.getText().equals("에엣? 로그인? 아직 회원가입도 안했다구...")) {
				join();
				join_panel.setVisible(true);
			}
		}
	};
	
	void roginORjoin() {
		roginORjoin_panel.setLayout(null);
		ImageIcon stop_guide_image = new ImageIcon("image/stop_guide.png");
		JLabel stop_guide = new JLabel(stop_guide_image);
		JLabel guide_say = new JLabel("<html>에에.. 너 아직 로그인도 안 한 거야?<br>로그인해야지 추천받을 수 있다구~!!</html>");
		JLabel guide_name_label = new JLabel("가이드");
		JButton rogin_option_button = new JButton("고..고멘...! 지금 바로 로그인할게...!!");//
		JButton join_option_button = new JButton("에엣? 로그인? 아직 회원가입도 안했다구...");
		
		//font
		Font font = new Font("휴먼엑스포", Font.PLAIN, 25);
		guide_name_label.setFont(font);
		guide_say.setFont(font);
		rogin_option_button.setFont(font);
		join_option_button.setFont(font);
		//setVisible
		stop_guide.setBounds(-10,0,frame_width, frame_height);
		guide_say.setBounds(150,470,500,200);
		rogin_option_button.setBounds(80,700,530,60);
		join_option_button.setBounds(80,780,530,60);
		guide_name_label.setBounds(80,780,530,60);
		//add
		roginORjoin_panel.add(stop_guide);
		stop_guide.add(guide_say);
		roginORjoin_panel.add(rogin_option_button);
		roginORjoin_panel.add(join_option_button);
		rogin_option_button.addActionListener(rogin_join_listener);
		join_option_button.addActionListener(rogin_join_listener);
		add(roginORjoin_panel);
		roginORjoin_panel.setVisible(false);
	}//roginORjoin()
	
	void rogin() {//로그인화면
		DB db = new DB();
		db.connect();
		rogin_panel.setLayout(null);
		JLabel rogin_label = new JLabel("로그인");
		JLabel id_label = new JLabel("아이디");
		JLabel pw_label = new JLabel("비밀번호");
		JTextField id_tf = new JTextField();
		JTextField pw_tf = new JTextField();
		JButton rogin_button = new JButton(new ImageIcon("image/rogin1.png"));
		JButton back_button = new JButton(new ImageIcon("image/back.png")); //뒤로가기
		JLabel kind_guide_label = new JLabel(new ImageIcon("image/kind_guide.png"));  //친절하게 안내하는 가이드
		
		//버튼이미지만 보이게
		back_button.setBorderPainted(false);
		back_button.setFocusPainted(false);
		back_button.setContentAreaFilled(false);
		rogin_button.setBorderPainted(false);
		rogin_button.setFocusPainted(false);
		rogin_button.setContentAreaFilled(false);
		
		rogin_button.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				String rogin_sql = "SELECT * FROM join_membership WHERE id = '" + id_tf.getText() + "'";  // id가 있는지 확인용 sql문
				ResultSet rogin;
				try {
					rogin = db.stmt.executeQuery(rogin_sql);
					if(rogin.next()) {  //id가 있으면
						if(pw_tf.getText().equals(rogin.getString("password"))){ //입력한 비번과 id 비번이 일치하면 로그인 성공
							JOptionPane.showMessageDialog(kind_guide_label, "로긘 셩굥~><");
							user.setId(id_tf.getText());
							user.setNickname(rogin.getString("nickname"));
							menu();
							menu_panel.setVisible(true);
						}else {   //불일치일 경우
							JOptionPane.showMessageDialog(kind_guide_label, "비밀번호가 틀렸다구~!!");
						}
					}else {  //id가 없을 경우
						JOptionPane.showMessageDialog(kind_guide_label, "아이디가 존재하지 않는걸?");
					}
				} catch (SQLException e1) {
					
				}
				rogin_panel.setVisible(false);
			}
			
			@Override
			public void mouseExited(MouseEvent e) { //마우스 나가면
				rogin_button.setIcon(new ImageIcon("image/rogin1.png"));
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) { //마우스 갖다대면
				rogin_button.setIcon(new ImageIcon("image/rogin2.png"));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//font
		rogin_label.setFont(new Font("휴먼모음T",Font.PLAIN,40));
		id_label.setFont(new Font("휴먼모음T",Font.PLAIN,35));
		pw_label.setFont(new Font("휴먼모음T",Font.PLAIN,35));
		
		//setBounds
		rogin_label.setBounds(290,200,150,150); //로그인라벨
		id_label.setBounds(220,320,100,100); //아이디라벨
		id_tf.setBounds(370,355,100,30);   //아이디텍필
		pw_label.setBounds(220,400,130,100); //비번라벨
		pw_tf.setBounds(370,435,100,30);  //비번텍필
		kind_guide_label.setBounds(0,0,frame_width,frame_height);  //로그인 가이드
		back_button.setBounds(600,0,80,40);  //뒤로가기 버튼
		rogin_button.setBounds(270,530,200,80);  //로그인버튼
		
		ActionListener back_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rogin_panel.setVisible(false);
				roginORjoin_panel.setVisible(true);
			}
		};
		
		//add
		rogin_panel.add(rogin_label);
		rogin_panel.add(id_label);
		rogin_panel.add(id_tf);
		rogin_panel.add(pw_label);
		rogin_panel.add(pw_tf);
		rogin_panel.add(kind_guide_label);
		rogin_panel.add(back_button);
		back_button.addActionListener(back_listener);
		kind_guide_label.add(rogin_button);
		add(rogin_panel);
		rogin_panel.setVisible(false);
	}//rogin()
	
	void join(){  //회원가입화면
		DB db = new DB();
		db.connect();
		
		join_panel.setLayout(null);
		JLabel join_label = new JLabel("회원가입");
		JLabel id_label = new JLabel("아이디");
		JLabel pw_label = new JLabel("비밀번호");
		JLabel nickname_label = new JLabel("닉네임");
		JTextField id_tf = new JTextField();
		JTextField pw_tf = new JTextField();
		JTextField nickname_tf = new JTextField();
		JButton join_button = new JButton(new ImageIcon("image/join1.png"));//*
		JButton back_button = new JButton(new ImageIcon("image/back.png")); //뒤로가기
		JLabel sulk_guide_label = new JLabel(new ImageIcon("image/sulk_guide.png"));  //삐진 안내하는 가이드
		
		//버튼이미지만 보이게
		back_button.setBorderPainted(false);
		back_button.setFocusPainted(false);
		back_button.setContentAreaFilled(false);
		join_button.setBorderPainted(false);
		join_button.setFocusPainted(false);
		join_button.setContentAreaFilled(false);
		join_label.setFont(new Font("휴먼모음T",Font.PLAIN,40));
		id_label.setFont(new Font("휴먼모음T",Font.PLAIN,35));
		pw_label.setFont(new Font("휴먼모음T",Font.PLAIN,35));
		nickname_label.setFont(new Font("휴먼모음T",Font.PLAIN,35));
		
		join_button.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				String id_sql = "SELECT * FROM join_membership WHERE id = '"+id_tf.getText()+"'"; //회원가입 테이블에 입력한 id가 있는지 보는 SELECT문
				ResultSet id_check;
				try {
					id_check = db.stmt.executeQuery(id_sql);
					if(id_check.next()) {  //이미 id가 있으면
						JOptionPane.showMessageDialog(sulk_guide_label, "입력한 아이디가 이미 있는걸..?");
					}else {
						String membership_sql = "INSERT INTO `song`.`join_membership` (`id`, `nickname`, `password`) VALUES ('"+id_tf.getText()+"', '"+nickname_tf.getText()+"', '"+pw_tf.getText()+"')";
						try {
							db.stmt.executeUpdate(membership_sql);
							user.setId(id_tf.getText());
							user.setNickname(nickname_tf.getText());
							JOptionPane.showMessageDialog(sulk_guide_label, "회원가입 완료야!");
							menu();
							menu_panel.setVisible(true);
						}catch(Exception e2) {
							JOptionPane.showMessageDialog(sulk_guide_label, "회원가입 실패? 어째서일까나~?");
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				join_panel.setVisible(false);
			}
			
			@Override
			public void mouseExited(MouseEvent e) { //마우스 나가면
				join_button.setIcon(new ImageIcon("image/join1.png"));
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) { //마우스 갖다대면
				join_button.setIcon(new ImageIcon("image/join2.png"));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		//setBounds
		join_label.setBounds(290,190,150,150); //로그인라벨
		id_label.setBounds(220,280,100,100); //아이디라벨
		id_tf.setBounds(370,315,100,30);   //아이디텍필
		nickname_label.setBounds(220,350,100,100);  //닉네임라벨
		nickname_tf.setBounds(370,385,100,30);  //닉네임텍필
		pw_label.setBounds(220,420,130,100); //비번라벨
		pw_tf.setBounds(370,455,100,30);  //비번텍필
		sulk_guide_label.setBounds(0,0,frame_width,frame_height);  //회원가입 가이드
		back_button.setBounds(600,0,80,40);  //뒤로가기 버튼
		join_button.setBounds(250,530,230,80);  //회원가입 버튼
		
		ActionListener back_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rogin_panel.setVisible(false);
				roginORjoin_panel.setVisible(true);
			}
		};
		
		//add
		join_panel.add(join_label);
		join_panel.add(id_label);
		join_panel.add(id_tf);
		join_panel.add(pw_label);
		join_panel.add(pw_tf);
		join_panel.add(nickname_label);
		join_panel.add(nickname_tf);
		join_panel.add(sulk_guide_label);
		join_panel.add(back_button);
		back_button.addActionListener(back_listener);
		sulk_guide_label.add(join_button);
		add(join_panel);
		join_panel.setVisible(false);
	}
	
	void menu() {
		/*지연이의 노래 추천 라벨,폰트
		 * 가이드 라벨
		 * 오노추 버튼
		 * 추천 버튼
		 * 게시판 버튼
		 */
		menu_panel.setLayout(null);
		JLabel menu_guide = new JLabel(new ImageIcon("image/main_screen_guide.png"));
		JButton go_ohnochoo_button = new JButton("오노추 들으러 갈게!");
		JButton go_recommend_button = new JButton("노래 추천 받아야지~");
		JButton go_message_board_button = new JButton("게시판 쓰러 갈래~");
		JLabel guide_say = new JLabel("<html>뭐 할지 고민돼?<br>지연이가 선정한 오노추도 꽤 좋다구~?</html>");
		
		//font
		Font font = new Font("휴먼엑스포", Font.PLAIN, 27);
		title_label.setFont(new Font("휴먼엑스포",Font.PLAIN,80));
		guide_say.setFont(font);
		go_ohnochoo_button.setFont(font);
		go_message_board_button.setFont(font);
		go_recommend_button.setFont(font);
		
		//setBounds
		title_label.setBounds(280,0,400,300);
		menu_guide.setBounds(0,0,frame_width,frame_height);
		guide_say.setBounds(90,620,500,200);
		go_recommend_button.setBounds(285,400,400,60);
		go_message_board_button.setBounds(285,490,400,60);
		go_ohnochoo_button.setBounds(285,580,400,60);
		
		//add
		go_message_board_button.addActionListener(todo);
		go_ohnochoo_button.addActionListener(todo);
		go_recommend_button.addActionListener(todo);
		menu_panel.add(menu_guide);
		menu_panel.add(title_label);
		menu_panel.add(go_ohnochoo_button);
		menu_panel.add(go_message_board_button);
		menu_panel.add(go_recommend_button);
		menu_guide.add(guide_say);
		add(menu_panel);
		menu_panel.setVisible(false);
	}//menu()
	
	ActionListener todo = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			menu_panel.setVisible(false);
			JButton check = (JButton)e.getSource();
			
			if(check.getText().equals("오노추 들으러 갈게!")) {
				try {
					ohnochoo();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				ohnochoo_panel.setVisible(true);
			}
			if(check.getText().equals("노래 추천 받아야지~")) {
				genre_question(); //장르 물어보는 화면으로 이동
				recommend_genre_question_panel.setVisible(true);
			}
			if(check.getText().equals("게시판 쓰러 갈래~")) {
				
			}
		}
	};//todo
	
	void ohnochoo() throws SQLException {  //오노추
		MP3Player mp3 = new MP3Player();
		DB db = new DB();
		db.connect();
		ohnochoo_panel.setLayout(null);
		String ohnochoo_title = null;
		String ohnochoo_singer = null;
		String ohnochoo_comment = null;
		JLabel album = new JLabel();
		JButton play_button = new JButton(new ImageIcon("image/stop.png"));
		JButton back_button = new JButton(new ImageIcon("image/back.png")); //뒤로가기
		String sql_ohnochoo = "SELECT * FROM ohnochoo WHERE DATE = curdate()";
		ResultSet ohnochoo = db.stmt.executeQuery(sql_ohnochoo);
		
		if(ohnochoo.next()) {  //변수에 넣기
			ohnochoo_title = ohnochoo.getString("Select_song");
			ohnochoo_singer = ohnochoo.getString("singer");
			ohnochoo_comment = ohnochoo.getString("comment");
		}
		
		JLabel title = new JLabel(ohnochoo_title,JLabel.CENTER);
		JLabel singer = new JLabel(ohnochoo_singer,JLabel.CENTER);
		JLabel comment = new JLabel(ohnochoo_comment,JLabel.CENTER);
		//font
		Font font = new Font("휴먼엑스포", Font.PLAIN, 27);
		title.setFont(new Font("휴먼엑스포", Font.PLAIN, 35));
		singer.setFont(font);
		comment.setFont(font);
		
		String sql = "SELECT * FROM ohnochoo_all_song WHERE title = '"+ohnochoo_title+"'";  //제목 맞으면 SELECT문
		ResultSet ohnochoo_song = db.stmt.executeQuery(sql);
		
		if(ohnochoo_song.next()) { //노래 재생을 위한 경로
			path = System.getProperty("user.dir")+ohnochoo_song.getString("song_path");
			album = new JLabel(new ImageIcon("album/"+ohnochoo_title+".jpg"));//앨범커버
		}
		
		play_button.setBorderPainted(false);
		play_button.setFocusPainted(false);
		play_button.setContentAreaFilled(false);
		back_button.setBorderPainted(false);
		back_button.setFocusPainted(false);
		back_button.setContentAreaFilled(false);
		
		ActionListener play_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton check = (JButton)e.getSource();
				ImageIcon stop = new ImageIcon("image/stop.png");
				ImageIcon play = new ImageIcon("image/play.png");
				
				if(check.getIcon().toString().equals("image/stop.png")){
					mp3.play(path); //노래 재생
					play_button.setIcon(play);
				}else if(check.getIcon().toString().equals("image/play.png")) {
					mp3.stop();  //노래 멈춤
					play_button.setIcon(stop);
				}
			}
		};//play_listener
		
		ActionListener ohnochoo_back_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ohnochoo_panel.removeAll();
				menu_panel.setVisible(true);
				if(mp3.isPlaying()) {
					mp3.stop();
				}
			}
		};//ohnochoo_back_listener
		
		//setBounds
		album.setBounds(150,150,400,400);  //앨범커버
		play_button.setBounds(315,630,80,80);  //재생버튼
		title.setBounds(0,50,700,100);  //제목
		singer.setBounds(0,530,700,100);  //가수
		comment.setBounds(0,650,700,200);  //선정이유
		back_button.setBounds(600,0,80,40);  //뒤로가기 버튼
		//add
		play_button.addActionListener(play_listener);
		back_button.addActionListener(ohnochoo_back_listener);
		ohnochoo_panel.add(album);
		ohnochoo_panel.add(title);
		ohnochoo_panel.add(singer);
		ohnochoo_panel.add(comment);
		ohnochoo_panel.add(play_button);
		ohnochoo_panel.add(back_button);
		add(ohnochoo_panel);
		ohnochoo_panel.setVisible(false);
	}//ohnochoo()
	
	void genre_question(){  //장르 물어보는 화면
		recommend_genre_question_panel.setLayout(null);
		JLabel first_question_label = new JLabel(new ImageIcon("image/first_question.png"));
		JLabel guide_say_label = new JLabel("<html>먼저 첫번째 질문~!<br>"+user.getNickname()+"군은 어떤 장르가 좋아~?</html>");
		JButton boy_group_button = new JButton("음 남돌 노래가 듣고 싶달까나..?");
		JButton girl_group_button = new JButton("여돌이 역시 짱이지~!");
		JButton jpop_button = new JButton("「후후... 오늘은 제이포푸이려나...」");
		JButton pop_button = new JButton("팝송이 듣기 좋지~");
		JButton kpop_button = new JButton("아이돌 말고 그냥 가요?가 듣고 싶네");
		
		//font
		Font font = new Font("휴먼엑스포",Font.PLAIN,22);
		guide_say_label.setFont(new Font("휴먼엑스포",Font.PLAIN,27));
		boy_group_button.setFont(font);
		girl_group_button.setFont(font);
		jpop_button.setFont(font);
		pop_button.setFont(font);
		kpop_button.setFont(font);
		
		//setBounds
		first_question_label.setBounds(-7,0,frame_width,frame_height);
		guide_say_label.setBounds(90,610,500,100);
		boy_group_button.setBounds(280,250,400,50);
		girl_group_button.setBounds(280,320,400,50);
		jpop_button.setBounds(280,390,400,50);
		pop_button.setBounds(280,460,400,50);
		kpop_button.setBounds(280,530,400,50);
		
		boy_group_button.setName("남돌");
		girl_group_button.setName("여돌");
		jpop_button.setName("제이팝");
		pop_button.setName("팝송");
		kpop_button.setName("가요");
		
		ActionListener genre_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton check = (JButton)e.getSource();
				switch(check.getName()) {
				case "여돌" : genre = "girl_group"; break;
				case "남돌" : genre = "boy_group"; break;
				case "가요" : genre = "kpop"; break;
				case "팝송" : genre = "pop"; break;
				case "제이팝" : genre = "jpop"; break;
				}
				recommend_genre_question_panel.setVisible(false);
				situation_question();
				recommend_situation_question_panel.setVisible(true);
			}
		};//genre_listener
		
		//add
		boy_group_button.addActionListener(genre_listener);
		girl_group_button.addActionListener(genre_listener);
		jpop_button.addActionListener(genre_listener);
		pop_button.addActionListener(genre_listener);
		kpop_button.addActionListener(genre_listener);
		recommend_genre_question_panel.add(first_question_label);
		first_question_label.add(guide_say_label);
		recommend_genre_question_panel.add(boy_group_button);
		recommend_genre_question_panel.add(kpop_button);
		recommend_genre_question_panel.add(pop_button);
		recommend_genre_question_panel.add(girl_group_button);
		recommend_genre_question_panel.add(jpop_button);
		add(recommend_genre_question_panel);
		recommend_genre_question_panel.setVisible(false);
	}//genre_question()
	
	void situation_question() {  //상황 물어보는 화면
		recommend_situation_question_panel.setLayout(null);
		JLabel second_question_label = new JLabel(new ImageIcon("image/second_question.png"));
		JLabel guide_say_label = new JLabel("<html>두번째 질문!<br>노래의 분위기랄까나... 듣고 싶은 상황은?</html>");
		JButton happy_button = new JButton("룰루~ 기분이 좋을 때!!");
		JButton concentrate_button = new JButton("집중하기 좋은 노래가 좋겠어.");
		JButton sad_button = new JButton("ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ");
		
		//font
		Font font = new Font("휴먼엑스포",Font.PLAIN,22);
		guide_say_label.setFont(new Font("휴먼엑스포",Font.PLAIN,27));
		happy_button.setFont(font);
		concentrate_button.setFont(font);
		sad_button.setFont(font);
		
		//setBounds
		second_question_label.setBounds(-7,0,frame_width,frame_height);
		guide_say_label.setBounds(80,610,600,100);
		happy_button.setBounds(280,320+70,400,50);
		concentrate_button.setBounds(280,390+70,400,50);
		sad_button.setBounds(280,460+70,400,50);
		happy_button.setName("기분 좋을 때");
		concentrate_button.setName("집중할 때");
		sad_button.setName("우울할 때");
		
		ActionListener situation_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton check = (JButton)e.getSource();
				switch(check.getName()) {
				case "기분 좋을 때" : situation = "기분 좋을 때"; break;
				case "집중할 때" : situation = "집중할 때"; break;
				case "우울할 때" : situation = "집중할 때"; break;
				}
				recommend_situation_question_panel.setVisible(false);
				waiting();//대기화면메서드
				waiting_panel.setVisible(true);//대기화면
			}
		};//situation_listener
		
		//add
		happy_button.addActionListener(situation_listener);
		concentrate_button.addActionListener(situation_listener);
		sad_button.addActionListener(situation_listener);
		recommend_situation_question_panel.add(second_question_label);
		second_question_label.add(guide_say_label);
		recommend_situation_question_panel.add(happy_button);
		recommend_situation_question_panel.add(sad_button);
		recommend_situation_question_panel.add(concentrate_button);
		add(recommend_situation_question_panel);
		recommend_situation_question_panel.setVisible(false);
	}//situation_question()
	
	void waiting() {//대기화면
		waiting_panel.setLayout(null);
		JLabel thinking_guide_label = new JLabel(new ImageIcon("image/thinking_guide.png"));
		JLabel guide_say_label = new JLabel("<html>응응...그런 너낌...<br>좋아 결정했어!<br>"+user.getNickname()+"군 맘에 들었으면 좋겠네~</html>");
		JButton listen_button = new JButton(new ImageIcon("image/go_listen1.png"));
		
		listen_button.setBorderPainted(false);
		listen_button.setFocusPainted(false);
		listen_button.setContentAreaFilled(false);
		//font
		guide_say_label.setFont(new Font("휴먼엑스포",Font.PLAIN,27));
		
		//setBounds
		thinking_guide_label.setBounds(-7,0,frame_width,frame_height);
		guide_say_label.setBounds(80,610,600,100);
		listen_button.setBounds(240,70,200,80);
		
		listen_button.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				waiting_panel.removeAll();
				waiting_panel.setVisible(false);
				try {
					recommend_song_listen();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				recommend_song_listen_panel.setVisible(true);
			}
			@Override
			public void mouseExited(MouseEvent e) { //마우스 나가면
				listen_button.setIcon(new ImageIcon("image/go_listen1.png"));
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) { //마우스 갖다대면
				listen_button.setIcon(new ImageIcon("image/go_listen2.png"));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		//add
		waiting_panel.add(thinking_guide_label);
		thinking_guide_label.add(guide_say_label);
		waiting_panel.add(listen_button);
		add(waiting_panel);
		waiting_panel.setVisible(false);
	}//waiting()
	
	void recommend_song_listen() throws SQLException {
		recommend_song_listen_panel.setLayout(null);
		MP3Player mp3 = new MP3Player();
		DB db = new DB();
		db.connect();
		String title = null;
		String singer = null;
		JLabel album = new JLabel();
		JButton play_button = new JButton(new ImageIcon("image/stop.png"));
		JButton main_button = new JButton(new ImageIcon("image/go_main1.png"));  //메인으로 가는 버튼
		String sql_listen = "SELECT * FROM " + genre +" where situation = '"+ situation + "'order by rand() limit 1";
		ResultSet select = db.stmt.executeQuery(sql_listen);
		
		if(select.next()) {  //변수에 넣기
			title = select.getString("title");
			singer = select.getString("singer");
		}
		
		JLabel title_label = new JLabel(title,JLabel.CENTER);
		JLabel singer_label = new JLabel(singer,JLabel.CENTER);
		
		//font
		Font font = new Font("휴먼엑스포", Font.PLAIN, 27);
		title_label.setFont(new Font("휴먼엑스포", Font.PLAIN, 35));
		singer_label.setFont(font);
		
		String sql = "SELECT * FROM ohnochoo_all_song WHERE title = '"+ title+"'";
		ResultSet song = db.stmt.executeQuery(sql);
		
		if(song.next()) { //노래 재생을 위한 경로
			path = System.getProperty("user.dir")+song.getString("song_path");
			album = new JLabel(new ImageIcon("album/"+title+".jpg"));//앨범커버
		}
		
		play_button.setBorderPainted(false);
		play_button.setFocusPainted(false);
		play_button.setContentAreaFilled(false);
		main_button.setBorderPainted(false);
		main_button.setFocusPainted(false);
		main_button.setContentAreaFilled(false);
		
		ActionListener play_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton check = (JButton)e.getSource();
				ImageIcon stop = new ImageIcon("image/stop.png");
				ImageIcon play = new ImageIcon("image/play.png");
				
				if(check.getIcon().toString().equals("image/stop.png")){
					mp3.play(path); //노래 재생
					play_button.setIcon(play);
				}else if(check.getIcon().toString().equals("image/play.png")) {
					mp3.stop();  //노래 멈춤
					play_button.setIcon(stop);
				}
			}
		};//play_listener
		
		
		//setBounds
		album.setBounds(150,150,400,400);  //앨범커버
		play_button.setBounds(315,630,80,80);  //재생버튼
		main_button.setBounds(520,0,160,80);  //메인버튼
		title_label.setBounds(0,50,700,100);  //제목
		singer_label.setBounds(0,530,700,100);  //가수
		
		//add
		play_button.addActionListener(play_listener);
		recommend_song_listen_panel.add(album);
		recommend_song_listen_panel.add(title_label);
		recommend_song_listen_panel.add(singer_label);
		recommend_song_listen_panel.add(play_button);
		recommend_song_listen_panel.add(main_button);
		add(recommend_song_listen_panel);
		recommend_song_listen_panel.setVisible(false);
	}//recommend_song_listen()
	
	public static void main(String[] args) {
		new User();
	}//main
}//User

class Information{
	private String id;
	private String pw;
	private String nickname;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}