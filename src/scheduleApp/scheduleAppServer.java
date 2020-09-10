package scheduleApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class scheduleAppServer {// ڵ
	private static scheduleAppServer instance = new scheduleAppServer();

	public static scheduleAppServer getInstance() {
		return instance;
	}

	public scheduleAppServer() {

	}

	String jdbc_url = "jdbc:mysql://localhost:3306/we_meet?characterEncoding=euckr&useUnicode=true&mysqlEncoding=euckr&useSSL=false&serverTimezone=Asia/Seoul";
	String dbId = "root"; // MySQL
	String dbPw = "1234"; // й ȣ
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	Statement stmt = null;

	ResultSet rs = null;
	String sql = "";
	String sql2 = "";
	String sql3 = ""; // 추가
	String sql4 = "";
	String returns = "";
	String returns2 = "";
	String returns3 = "";
	StringBuilder sb = new StringBuilder();
	StringBuilder sb2 = new StringBuilder();
	final int FRIEND_STATUS = 1;
	final int REQUEST_STATUS = 0; // 나에게 친구요청한 경우
	final int WAITING_STATUS = -1; // 내가 친구요청한 경우

	public String joindb(String id, String pwd, String name) {
		try {
			sb = new StringBuilder();
			sb2 = new StringBuilder();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			if (id.equals("")) {
				return "emptyid";
			} else if (pwd.equals("")) {
				return "emptypw";
			} else if (name.equals("")) {
				return "emptyname";
			} else {
				sql = "select id from user where id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getString("id").equals(id)) { // ̹ ̵ ִ
						returns = "id";
					}
				} else { // Է ̵

					sql2 = "insert into user values(?,?,?,?)";
					pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setString(1, id);
					pstmt2.setString(2, pwd);
					pstmt2.setString(3, name);
					pstmt2.setString(4, "지역");

					pstmt2.executeUpdate();

					stmt = conn.createStatement();
					
					sql3 = sb.append("create table calendar_").append(id).append("( id char(15) NOT NULL, ")
							.append("date date NOT NULL, ").append("schedule varchar(80), ").append("memo text, ")
							.append("foreign key(id) references user(id), ").append("primary key(id, date, schedule));")
							.toString();

					stmt.execute(sql3);

					sql4 = sb2.append("create table friends_").append(id).append("( id char(15) NOT NULL, ")
							.append("name char(10) NOT NULL, ").append("status int(1) NOT NULL,").append("foreign key(id) references user(id), ")
							.append("primary key(id));").toString();
					pstmt3 = conn.prepareStatement(sql4);
					
					pstmt3.executeUpdate();
					
					returns = "ok";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			if (pstmt2 != null)
				try {
					pstmt2.close();
				} catch (SQLException ex) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
			if (pstmt3 != null)
				try {
					pstmt3.close();
				} catch (SQLException ex) {
				}
		}
		return returns;
	}

	public String logindb(String id, String pwd) {
		try {
			System.out.println("DB 접속 전");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			System.out.println("DB 접속 후");
			sql = "select id,pw from user where id=? and pw=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("id").equals(id) && rs.getString("pw").equals(pwd)) {
					returns2 = "true";// 로그인 가능
				} else {
					returns2 = "false"; // 로그인 실패
				}
			} else {
				returns2 = "noId"; // 아이디 또는 비밀번호 존재 X
			}

		} catch (Exception e) {
			returns2 = "NO!";
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		System.out.println(returns2);
		return returns2;
	}

	public String calendardb(String id, String date, String schedule, String memo) {
		String user_calendar = "calendar" + id;
		String rowdb = "";
		returns3 = "";

		try {
			sb = new StringBuilder();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = sb.append("select date, schedule, memo from ").append(user_calendar).append(" where date = ?")
					.toString();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			System.out.println(rs);
			while (rs.next()) {
				rowdb = rowdb + rs.getString("date") + "," + rs.getString("schedule") + "," + rs.getString("memo")
						+ ","; // date, schedule, memo
			}
			System.out.println(rowdb);
			returns3 = rowdb;

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns3;
	}

	public String addCalendar(String id, String date, String schedule, String memo) {
		String user_calendar = "calendar" + id;

		returns3 = "";

		try {
			sb = new StringBuilder();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = sb.append("select date, schedule from ").append(user_calendar)
					.append(" where date = ? and schedule = ? ").toString();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setString(2, schedule);
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			System.out.println(rs);
			if (rs.next()) {
				returns3 = "false";

			} else {
				sb = new StringBuilder();
				sql2 = sb.append("insert into ").append(user_calendar).append(" values(?,?,?,?) ").toString();
				System.out.println(sql2 + "\n");
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, id);
				pstmt2.setString(2, date);
				pstmt2.setString(3, schedule);
				pstmt2.setString(4, memo);
				System.out.println(pstmt2);

				pstmt2.executeUpdate();

				returns3 = "done";
			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			if (pstmt2 != null)
				try {
					pstmt2.close();
				} catch (SQLException ex) {
				}
		}
		return returns3;
	}

	public String editCalendar(String id, String date, String schedule, String memo, String old) {
		String user_calendar = "calendar" + id;

		returns3 = "";

		try {
			sb = new StringBuilder();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = sb.append("select date, schedule from ").append(user_calendar)
					.append(" where date = ? and schedule = ? ").toString();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			pstmt.setString(2, schedule);
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			System.out.println(rs);
			if (rs.next()) {
				returns3 = "false";

			} else {
				sb = new StringBuilder();
				sql2 = sb.append("update ").append(user_calendar)
						.append(" set schedule = ?, memo = ? where schedule =?").toString();
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, schedule);
				pstmt2.setString(2, memo);
				pstmt2.setString(3, old);
				System.out.println(pstmt2);

				pstmt2.executeUpdate();

				returns3 = "done";
			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			if (pstmt2 != null)
				try {
					pstmt2.close();
				} catch (SQLException ex) {
				}
		}
		return returns3;
	}

	public String loadSchedule() {
		returns = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from schedule";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				returns += rs.getString("schedule_name") + "\t" + rs.getInt("schedule_id") + "\t";

			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		System.out.println(returns);
		return returns;

	}
	
	public String addSchedule(String id, String sche_name, String date, String location,String participants) {

	      returns3 = "";
	      int cnt = 0;
	      
	      System.out.println(id);
	      System.out.println(sche_name); //자꾸  null이 들어옴

	      try {
	         sb = new StringBuilder();
	         Class.forName("com.mysql.jdbc.Driver");
	         conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
	         pstmt = conn.prepareStatement("select count(*) from schedule");
	         rs = pstmt.executeQuery();
	         if (rs.next()) {
	            System.out.println(rs.getInt(1));
	            cnt = rs.getInt(1); ++cnt;
	         } else
	            System.out.println("????");
	         System.out.println(cnt);
	         sql = sb.append("insert into schedule(schedule_id, id, schedule_name)").append(" values(?,?,?) ").toString();
	         System.out.println("insert");
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, cnt);
	         pstmt.setString(2, id);
	         pstmt.setString(3, sche_name);
	         System.out.println(pstmt);

	         pstmt.executeUpdate();

	         returns3 = "success";

	      } catch (Exception e) {

	      } finally {
	         if (rs != null)
	            try {
	               rs.close();
	            } catch (SQLException ex) {
	            }
	         if (pstmt != null)
	            try {
	               pstmt.close();
	            } catch (SQLException ex) {
	            }
	         if (conn != null)
	            try {
	               conn.close();
	            } catch (SQLException ex) {
	            }
	         if (pstmt2 != null)
	            try {
	               pstmt2.close();
	            } catch (SQLException ex) {
	            }
	      }
	      System.out.println(returns3);
	      return returns3;
	   }
	
	public String loadUser(String userId) {
		returns = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from user where id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				returns += rs.getString("name");
			}
			
		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}
	
	public String loadOthers(String userId) {
		returns = "";
		try {
			System.out.println("들어와찌");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from user a left outer join friends_" + userId + " b"
					+ " on a.id = b.id"
					+ " where b.id is null";
			pstmt = conn.prepareStatement(sql);
			System.out.println(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				returns += rs.getString("name") + "\t";
				System.out.println(returns);
			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}
	
	public String loadFriends(String userId) {
		returns = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from friends_" + userId + " where status=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, FRIEND_STATUS);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				returns += rs.getString("name") + "\t";
			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}
	
	public String loadWaiters(String userId) {
		returns = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from friends_" + userId + " where status=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, REQUEST_STATUS);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				returns += rs.getString("name") + "\t";
			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}

	public String loadAllUsers() {
		returns = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from user";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				returns += rs.getString("name") + "\t";
			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}
	
	public String addFriend(String userId, String friendName) {
		returns = "";
		String friendId = ""; // 상대방 아이디
		String userName = ""; // 사용자 이름
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from user where name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, friendName);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				friendId += rs.getString("id");
			}
			
			sql = "update friends_" + userId + " set status = ? where id = ?"; // 사용자한테 추가
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, FRIEND_STATUS);
			pstmt.setString(2, friendId);
			pstmt.executeUpdate();
			
			sql = "select * from user where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				userName += rs.getString("name");
			}
			
			sql = "update friends_" + friendId + " set status = ? where id = ?"; // 상대방한테 추가
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, FRIEND_STATUS);
			pstmt.setString(2, userId);
			pstmt.executeUpdate();
			
			returns = "true";
		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}
	
	public String friendRequest(String userId, String friendName) {
		returns = "";
		String friendId = ""; // 상대방 아이디
		String userName = ""; // 사용자 이름
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbc_url, dbId, dbPw);
			sql = "select * from user where name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, friendName);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				friendId += rs.getString("id");
			}
			
			sql = "insert into friends_" + userId + " values (?,?,?)"; // 상대방한테 추가
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, friendId);
			pstmt.setString(2, friendName);
			pstmt.setInt(3, WAITING_STATUS);
			pstmt.executeUpdate();
			
			sql = "select * from user where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				userName += rs.getString("name");
			}
			
			sql = "insert into friends_" + friendId + " values (?,?,?)"; // 상대방한테 추가
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userName);
			pstmt.setInt(3, REQUEST_STATUS);
			pstmt.executeUpdate();
			
			returns = "true";
		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;

	}
}