package mysqlUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlUtils {

	public SqlUtils() {
		// TODO Auto-generated constructor stub
	}

	Connection con;
	String table = "";
	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public SqlUtils(Connection con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}

	public SqlUtils(String connection, String user, String pass)
			throws SQLException {
		this.con = DriverManager.getConnection(connection, user, pass);
	}
	
	
	

	public class SqlPackager {
		ArrayList<Object> itemsToSend = new ArrayList<Object>();
		private String requestType;
		private String format;
		// utilize the Object types to determine datatype
		public SqlPackager() {

		}

		public void add(Integer o) {
			itemsToSend.add(o);
		}

		public void add(Byte o) {
			itemsToSend.add(o);
		}

		public void add(String o) {
			itemsToSend.add(o);
		}

		public void add(Double o) {
			itemsToSend.add(o);
		}

		public void add(Float o) {
			itemsToSend.add(o);
		}
		public void insertObject() throws SQLException {
			String fields = "?";
			for (int i=0;i<itemsToSend.size()-1;i++) {
				fields += ",?";
			}
			String statement ="insert into " + table+"("+format+") values ("+fields+")";
			PreparedStatement pst = con.prepareStatement(statement);
			int i=0;
			for (Object o: itemsToSend) {
				Class<? extends Object> c = o.getClass();
				if (c.equals(new Integer(0).getClass())) {
					pst.setInt(i, (int) o);
				}
				if (c.equals(new Byte((byte) 0).getClass())) {
					pst.setByte(i, (byte) o);
				}
				if (c.equals(new String("").getClass())) {
					pst.setString(i, (String) o);
				}
				if (c.equals(new Double(0.0).getClass())) {
					pst.setDouble(i, (Double) o);
				}
				if (c.equals(new Float(0.0).getClass())) {
					pst.setFloat(i, (Float) o);
				}
				i++;
			}
			pst.executeUpdate();
		}
		
		/**
		 * This might be more trouble then it's worth
		 * it can update a single field in the table
		 * This assumes that the database is sorted by an int id
		 * @param id the id variable to search for
		 * @param field the field to modify
		 * @param o the object to send, must be an int,byte,string,double or float
		 * @throws SQLException
		 */
		public void updateObject(int id,String field,Object o) throws SQLException {
			
			String statement ="update " + table+" set "+field+"=? where id="+id;
			PreparedStatement pst = con.prepareStatement(statement);
			int i=0;
				Class<? extends Object> c = o.getClass();
				if (c.equals(new Integer(0).getClass())) {
					pst.setInt(i, (int) o);
				}
				if (c.equals(new Byte((byte) 0).getClass())) {
					pst.setByte(i, (byte) o);
				}
				if (c.equals(new String("").getClass())) {
					pst.setString(i, (String) o);
				}
				if (c.equals(new Double(0.0).getClass())) {
					pst.setDouble(i, (Double) o);
				}
				if (c.equals(new Float(0.0).getClass())) {
					pst.setFloat(i, (Float) o);
				}
			pst.executeUpdate();
		}
		/**
		 * sets the format for the table
		 * this needs to include the variable names in this format:
		 * fieldName , FieldName2 ...
		 * @param s
		 */
		public void SetFormat(String s) {
			// TODO Auto-generated method stub
			format = s;
		}

	}

}
