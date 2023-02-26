package io.rajkumarsutar.mousegesturedynamicsauth.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.BMApi;
import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.BMCostants;
import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.Feature;
import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.LVQ2;

public class Database {

	public static Connection getConnection() {
		Connection oDBConnection = null;
		String sJdbcDriver = "com.mysql.jdbc.Driver";
		String sDBUser = "root";
		String sDBPassword = "root";
		String sDBName = "behaviometrics";
		String sDBUrl = "jdbc:mysql://localhost/" + sDBName;
		try {
			Class.forName(sJdbcDriver);
			oDBConnection = DriverManager.getConnection(sDBUrl, sDBUser, sDBPassword);
		} catch (ClassNotFoundException | SQLException oException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oException);
		}

		return oDBConnection;
	}

	public static boolean checkValue(String sTableName, String sColumnName, String sValue) {
		Connection oConnection = getConnection();
		String sSqlQuery = "SELECT COUNT(*) FROM " + sTableName + " WHERE " + sColumnName + "=?";
		PreparedStatement oPreparedStatement;
		try {
			oPreparedStatement = oConnection.prepareStatement(sSqlQuery);
			oPreparedStatement.setString(1, sValue);
			ResultSet oResultSet = oPreparedStatement.executeQuery();

			if (oResultSet.next()) {
				int iCount = Integer.parseInt(oResultSet.getString(1));
				if (iCount == 1) {
					oConnection.close();
					return true;
				}
			}
			oConnection.close();
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return false;
	}

	public static boolean saveFeature(String sFeatureName, String sFeatureData, String sModuleName, String sUserID,
			String sGesture) {
		Connection oConnection = getConnection();
		String sInsertQuery = "INSERT INTO Features (FeatureName, FeatureData, ModuleName, UserID, GestureName, FeatureTime)"
				+ " VALUES (?, ?, ?, ?, ?, NOW())";
		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sInsertQuery);
			oPreparedStatement.setString(1, sFeatureName);
			oPreparedStatement.setString(2, sFeatureData);
			oPreparedStatement.setString(3, sModuleName);
			oPreparedStatement.setString(4, sUserID);
			oPreparedStatement.setString(5, sGesture);
			int iUpdatedRows = oPreparedStatement.executeUpdate();
			oConnection.close();
			if (iUpdatedRows > 0) {
				return true;
			}
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return false;
	}

	/**
	 *
	 * @param sUserID
	 * @param sGesture
	 * @return
	 */
	public static ArrayList<Feature> getGestureData(String sUserID, String sGesture) {
		ArrayList<Feature> oFeatureList = new ArrayList<>();
		String sInsertQuery = "SELECT * FROM features WHERE GestureName = ? AND UserID = ?";
		Connection oConnection = getConnection();
		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sInsertQuery);
			oPreparedStatement.setString(1, sGesture);
			oPreparedStatement.setString(2, sUserID);
			ResultSet oResultSet = oPreparedStatement.executeQuery();
			while (oResultSet.next()) {
				Feature oFeature = new Feature();
				oFeature.setiFeatureID(oResultSet.getInt(1));
				oFeature.setsFeatureName(oResultSet.getString(2));
				oFeature.setsFeatureData(oResultSet.getString(3));
				oFeature.setsUserID(oResultSet.getString(4));
				oFeature.setsModuleName(oResultSet.getString(5));
				oFeature.setsFeatureTime(oResultSet.getDate(6).toString());
				oFeature.setsGesture(oResultSet.getString(7));

				oFeatureList.add(oFeature);
			}
			oConnection.close();
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return oFeatureList;
	}

	public static boolean storeFeatureSet(String sUserID, String sFeatureSetData, long lSessionID, String sFeatureName,
			String sGesture) {
		Connection oConnection = getConnection();
		String sInsertQuery = "INSERT INTO featureset (FeatureSetName, FeatureSetData, UserID, SessionID, GestureName)"
				+ " VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sInsertQuery);
			oPreparedStatement.setString(1, sFeatureName);
			oPreparedStatement.setString(2, sFeatureSetData);
			oPreparedStatement.setString(3, sUserID);
			oPreparedStatement.setLong(4, lSessionID);
			oPreparedStatement.setString(5, sGesture);
			int iUpdatedRows = oPreparedStatement.executeUpdate();
			oConnection.close();
			if (iUpdatedRows > 0) {
				return true;
			}
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return false;
	}

	public static boolean storeClickFeatureSet(String sUserID, String sFeatureSetData, long lSessionID,
			String sFeatureName) {
		Connection oConnection = getConnection();
		String sInsertQuery = "INSERT INTO click (FeatureName, FeatureData, UserID, SessionID)"
				+ " VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sInsertQuery);
			oPreparedStatement.setString(1, sFeatureName);
			oPreparedStatement.setString(2, sFeatureSetData);
			oPreparedStatement.setString(3, sUserID);
			oPreparedStatement.setLong(4, lSessionID);
			int iUpdatedRows = oPreparedStatement.executeUpdate();
			oConnection.close();
			if (iUpdatedRows > 0) {
				return true;
			}
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return false;
	}

	public static double[][] getFeatureData(String sUserID, String sModuleName, String sGesture) {
		int rows = BMCostants.TOTAL_SESSION * BMCostants.REPLICATIONS;
		int column = LVQ2.sizeOfFeatureSet(sModuleName);
		double aFeatureData[][] = new double[rows][column];
		Connection oConnection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
		String sQuery = "SELECT FeatureSetData FROM featureset WHERE UserID = ? AND FeatureSetName = ? AND GestureName = ?";
		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sQuery);
			oPreparedStatement.setString(1, sUserID);
			oPreparedStatement.setString(2, sModuleName);
			oPreparedStatement.setString(3, sGesture);
			ResultSet oResultSet = oPreparedStatement.executeQuery();

			int i = 0;
			while (oResultSet.next()) {
				double aTempfeatureset[] = stringToDoubleArray(oResultSet.getString(1));
				System.arraycopy(aTempfeatureset, 0, aFeatureData[i++], 0, column);
			}
			oConnection.close();
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return aFeatureData;
	}

	public static double[] stringToDoubleArray(String sFeatureData) {
		String aStringData[] = sFeatureData.split(" ");
		double aFeatureData[] = new double[aStringData.length];

		for (int i = 0; i < aStringData.length; i++) {
			aFeatureData[i] = Double.parseDouble(aStringData[i]);
		}

		return aFeatureData;
	}

	public static String[] userList() {
		Connection oConnection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
		String sUserList = "";

		PreparedStatement oPreparedStatement;
		try {

			oPreparedStatement = oConnection.prepareStatement("SELECT DISTINCT Mobile FROM User");
			ResultSet oResultSet = oPreparedStatement.executeQuery();
			while (oResultSet.next()) {
				sUserList += oResultSet.getString(1) + " ";
			}
			oConnection.close();
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return sUserList.split(" ");
	}

	public static int getCatagoryByMobile(String sMobileNumber) {
		Connection oConnection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
		int iUserCatagory = 0;

		PreparedStatement oPreparedStatement;
		try {

			oPreparedStatement = oConnection.prepareStatement("SELECT Catagory FROM User WHERE Mobile = ?");
			oPreparedStatement.setString(1, sMobileNumber);
			ResultSet oResultSet = oPreparedStatement.executeQuery();
			if (oResultSet.next()) {
				iUserCatagory = oResultSet.getInt(1);
			}
			oConnection.close();
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return iUserCatagory;
	}

	public static boolean storeUserProfile(int iUserCategory, String sFeatureSetData, String sModuleName,
			String sGesture) {
		Connection oConnection = getConnection();
		String sInsertQuery = "INSERT INTO weights (Category, Weights, ModuleID, Gesture) VALUES (?, ?, ?, ?)";

		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sInsertQuery);
			oPreparedStatement.setInt(1, iUserCategory);
			oPreparedStatement.setString(2, sFeatureSetData);
			oPreparedStatement.setString(3, sModuleName);
			oPreparedStatement.setString(4, sGesture);
			int iUpdatedRows = oPreparedStatement.executeUpdate();
			oConnection.close();
			if (iUpdatedRows > 0) {
				return true;
			}
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return false;
	}

	public static int executeUpdateQuery(String sUpdateQuery) {
		Connection oConnection = getConnection();

		try {
			PreparedStatement oPreparedStatement = oConnection.prepareStatement(sUpdateQuery);
			int iUpdatedRows = oPreparedStatement.executeUpdate();
			oConnection.close();
			if (iUpdatedRows > 0) {
				return iUpdatedRows;
			}
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return -1;
	}

	public static String getUserByCategory(int iCategory) {
		Connection oConnection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
		String sMobile = "Not Detected", sUserName = "Not Detected";

		PreparedStatement oPreparedStatement;
		try {

			oPreparedStatement = oConnection.prepareStatement("SELECT Mobile, UserName FROM User WHERE Catagory = ?");
			oPreparedStatement.setInt(1, iCategory);
			ResultSet oResultSet = oPreparedStatement.executeQuery();
			if (oResultSet.next()) {
				sMobile = oResultSet.getString(1);
				sUserName = oResultSet.getString(2);
			}
			oConnection.close();
		} catch (SQLException oSQLException) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), oSQLException);
		}

		return "Mobile : " + sMobile + "\nUser Name : " + sUserName;
	}

	public static User isMobileNumberExists(String mobileNumber) {
		try(Connection oConnection = getConnection()) {
			PreparedStatement oPreparedStatement;
			oPreparedStatement = oConnection.prepareStatement("SELECT UserName, Mobile, Profession FROM user WHERE Mobile=?");
			oPreparedStatement.setString(1, mobileNumber);
			ResultSet oResultSet = oPreparedStatement.executeQuery();

			if (oResultSet.next()) {
				return new User(oResultSet.getString(1), oResultSet.getString(2), oResultSet.getString(3));
			}
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return null;
	}

	public static Map<Integer, Integer> getUserGestureReplicationCount(String mobileNumber) {
		Map<Integer, Integer> replication = new HashMap<>();
		try(Connection oConnection = getConnection()) {
			PreparedStatement oPreparedStatement;
			oPreparedStatement = oConnection.prepareStatement("SELECT GestureID, COUNT(*) as Total, UserID FROM behaviometrics.usergesture GROUP BY GestureID, UserID HAVING UserID = ?");
			oPreparedStatement.setString(1, mobileNumber);
			ResultSet oResultSet = oPreparedStatement.executeQuery();
			while (oResultSet.next()) {
				replication.put(oResultSet.getInt(1), oResultSet.getInt(2));
			}

			System.out.println(replication);
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return replication;
	}

	public static boolean insertUserReplication(String mobileNumber, int gestureID, int[][] data) {
		try(Connection oConnection = getConnection()) {
			String[] sb = new String[data.length];
			for (int i = 0; i < data.length; i++) {
				sb[i] = (data[i][0] + " " + data[i][1] + " " + data[i][2]);
			}
			System.out.println(sb.toString());
			PreparedStatement oPreparedStatement;
			oPreparedStatement = oConnection.prepareStatement("INSERT INTO usergesture (UserID, GestureID, GestureData) VALUES (?, ?, ?)");
			oPreparedStatement.setString(1, mobileNumber);
			oPreparedStatement.setInt(2, gestureID);
			oPreparedStatement.setString(3, String.join(",", sb));
			int result = oPreparedStatement.executeUpdate();

			if (result == 1) {
				return true;
			}
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return false;
	}

	public static List<int[][]> fetchUserReplications(String mobileNumber, int gestureID) {
		try(Connection oConnection = getConnection()) {
			PreparedStatement oPreparedStatement;
			oPreparedStatement = oConnection.prepareStatement("SELECT GestureData FROM behaviometrics.usergesture WHERE GestureID = ? AND UserID = ?");
			oPreparedStatement.setInt(1, gestureID);
			oPreparedStatement.setString(2, mobileNumber);
			ResultSet oResultSet = oPreparedStatement.executeQuery();

			List<int[][]> gestureData = new ArrayList<>();

			while(oResultSet.next()) {
				String data = oResultSet.getString(1);
				String[] pointsStr = data.split(",");
				int[][] dataPoints = new int[64][3];

				for(int i = 0; i < 64; i++) {
					String[] parts = pointsStr[i].split(" ");
					dataPoints[i][0] = Integer.parseInt(parts[0]);
					dataPoints[i][1] = Integer.parseInt(parts[1]);
					dataPoints[i][2] = Integer.parseInt(parts[2]);
				}
				gestureData.add(dataPoints);
			}
			return gestureData;
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return null;
	}
}
