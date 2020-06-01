package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Coppia;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
public List<String> listRole() {
		
		String sql = "SELECT distinct role " + 
				"FROM authorship";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

			result.add(res.getString("role"));
			
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

public void getArtistiCon(String ruolo, Map<Integer, Artist> artistiMap ) {
	String sql = "SELECT DISTINCT a.artist_id, a.name " + 
			"FROM artists AS a, authorship AS au " + 
			"WHERE au.role = ? " + 
			"AND a.artist_id = au.artist_id";
	Connection conn = DBConnect.getConnection();

	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, ruolo);
		ResultSet res = st.executeQuery();
		while (res.next()) {
		
			Artist a = new Artist(res.getInt("a.artist_id"), res.getString("a.name"));
		
		artistiMap.put(res.getInt("a.artist_id"), a);
		
		}
		conn.close();
	
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
}

public List<Coppia> getCoppie(String ruolo, Map<Integer, Artist> artistiMap) {
	String sql = "SELECT a1.artist_id, a2.artist_id, COUNT(DISTINCT e1.exhibition_id) AS peso " + 
			"FROM authorship a1, authorship a2, exhibition_objects e1, exhibition_objects e2 " + 
			"WHERE a1.artist_id < a2.artist_id " + 
			"AND a1.role = ? AND a2.role = ? " + 
			"AND a1.object_id = e1.object_id AND a2.object_id = e2.object_id " + 
			"AND e1.exhibition_id = e2.exhibition_id " + 
			"GROUP BY a1.artist_id, a2.artist_id";
	List<Coppia> coppie = new LinkedList<>();
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		st.setString(1, ruolo);
		st.setString(2, ruolo);
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
				Coppia a = new Coppia(artistiMap.get(res.getInt("a1.artist_id")), artistiMap.get(res.getInt("a2.artist_id")),res.getDouble("peso"));
				coppie.add(a);
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println(res.getInt("id"));
			}
		}
		
		conn.close();
		
		return coppie ;

	} catch (SQLException e) {
		e.printStackTrace();
		return null ;
	}

	
}

	
}
