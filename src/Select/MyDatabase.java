
package Select;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MyDatabase {
	
	public ArrayList<MovieInfo> SelectMovie(Connection Conn, String MovieName, String MovieType, String Definition, String Area, String Year) throws Exception {
		
		String SQL = null;
		ArrayList<MovieInfo> MovieInfos = new ArrayList<MovieInfo>();
		List<String> MovieTypes = FileUtils.readLines(new File("TypeList.txt"));
		
		for ( String Type:MovieTypes ) {
			int flag = 0;
			if ( MovieType.compareTo("不限") != 0 ) {
				SQL = "SELECT * FROM "+MovieType+" WHERE MovieName like '%"+MovieName+"%'";
				flag = 1;
			}
			else {
				SQL = "SELECT * FROM "+Type+" WHERE MovieName like '%"+MovieName+"%'";
			}
			if ( Definition.compareTo("不限") != 0 ) {
				SQL += " AND Definition like '%"+Definition+"%'";
			}
			if ( Area.compareTo("不限") != 0 ) {
				SQL += " AND Area like '%"+Area+"%'";
			}
			if ( Year.compareTo("不限") != 0 ) {
				SQL += " AND Year like '%"+Year+"%'";
			}
			
			ResultSet RS = Conn.createStatement().executeQuery(SQL);
			while( RS.next() ) {
				MovieInfo Info = new MovieInfo();
				Info.setMovieName(RS.getString("MovieName").substring(0, RS.getString("MovieName").indexOf('_')));
				Info.setScore(RS.getString("Score"));
				Info.setDefinition(RS.getString("Definition"));
				Info.setType(RS.getString("Type"));
				Info.setArea(RS.getString("Area"));
				Info.setYear(RS.getString("Year"));
				Info.setDirector_Screenwriter(RS.getString("Director_Screenwriter"));
				Info.setStarring(RS.getString("Starring"));
				if ( !IsRepeat( MovieInfos, Info ) ) {
					MovieInfos.add(Info);
				}
			}
			if ( flag == 1 ) {
				break;
			}
			//if ( MovieInfos.size() > 0 ) {
			//	break;
			//}
		}
		return MovieInfos;
	}
	
	public ArrayList<MovieInfo> SelectType(Connection Conn, String MovieType, String Definition, String Area, String Year) throws Exception {
		
		String SQL = null;
		ArrayList<MovieInfo> MovieInfos = new ArrayList<MovieInfo>();
		int flag = 0;
		
		SQL = "SELECT * FROM "+MovieType;
		if ( Definition.compareTo("不限") != 0 || Area.compareTo("不限") != 0 || Year.compareTo("不限") != 0 ) {
			SQL += " WHERE ";
		}
		if ( Definition.compareTo("不限") != 0 ) {
			SQL += "Definition like '%"+Definition+"%'";
			flag = 1;
		}
		if ( Area.compareTo("不限") != 0 ) {
			if ( flag == 1 ) {
				SQL += " AND ";
			}
			SQL += "Area like '%"+Area+"%'";
			flag = 1;
		}
		if ( Year.compareTo("不限") != 0 ) {
			if ( flag == 1 ) {
				SQL += " AND ";
			}
			SQL += "Year like '%"+Year+"%'";
		}
		ResultSet RS = Conn.createStatement().executeQuery(SQL);
		while( RS.next() ) {
			MovieInfo Info = new MovieInfo();
			Info.setMovieName(RS.getString("MovieName").substring(0, RS.getString("MovieName").indexOf('_')));
			Info.setScore(RS.getString("Score"));
			Info.setDefinition(RS.getString("Definition"));
			Info.setType(RS.getString("Type"));
			Info.setArea(RS.getString("Area"));
			Info.setYear(RS.getString("Year"));
			Info.setDirector_Screenwriter(RS.getString("Director_Screenwriter"));
			Info.setStarring(RS.getString("Starring"));
			if ( !IsRepeat( MovieInfos, Info ) ) {
				MovieInfos.add(Info);
			}
		}
		return MovieInfos;
	}
	
	public boolean IsRepeat( ArrayList<MovieInfo> MovieInfos, MovieInfo Info ) {
		
		for ( MovieInfo TestInfo:MovieInfos ) {
			String TestMovieName = TestInfo.getMovieName()+"_"+TestInfo.getDefinition();
			String MovieName = Info.getMovieName()+"_"+Info.getDefinition();
			if ( TestMovieName.compareTo(MovieName) == 0 ) {
				return true;
			}
		}
		return false;
	}
	
	public Connection ConnectionDB( String Database ) {
		
		String Driver = "com.mysql.jdbc.Driver";
		String URL = null;
		String User = "root";
		String Password = "root";
		Connection conn = null;
			
		if ( Database == null ) {
			URL = "jdbc:mysql://localhost/test";
		}
		else {
			URL = "jdbc:mysql://localhost/"+Database;
		}	
		try {
			Class.forName(Driver);
			conn = DriverManager.getConnection(URL, User, Password);
			if ( !conn.isClosed() ) {
			}
			else {
			}
		}catch ( SQLException e ) {
			conn = ConnectionDB( Database );
		}catch ( Exception e ) {
			e.printStackTrace();
		}
	return conn;
	}
	
	public void CutConnectionDB( Connection conn ) throws Exception {
		
			try {
				if ( conn == null ) {
					//System.out.println("Connection is null");
				}
				else {
					//System.out.println("Connection is not null");
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
	}
}