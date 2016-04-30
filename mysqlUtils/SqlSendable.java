package mysqlUtils;

import java.sql.Connection;

import mysqlUtils.SqlUtils.SqlPackager;

public interface SqlSendable {
	public void sqlSend(SqlUtils conUtils,SqlPackager pack);
	//public void sqlRead(Connection con);
}
