package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class LVQ2 {

	public static final double LEARNING_RATE = 0.5;

	/**
	 * This method is used to find the euclidean distance between two vectors
	 *
	 * @param x double[] Input vector
	 * @param w double[] Weight vector
	 *
	 * @return double Distance between x and w
	 */
	public static double euclideanDistance(double x[], double w[]) {
		double diff = 0;
		for (int i = 0; i < x.length; i++) {
			diff += Math.pow((x[i] - w[i]), 2);
		}
		return Math.sqrt(diff);
	}

	public static double[] updateWeights(double x[], double w[], boolean DETECTED) {
		int op = DETECTED ? (+1) : (-1);
		for (int i = 0; i < x.length; i++) {
			w[i] = w[i] + (op * LEARNING_RATE * (x[i] - w[i]));
		}
		return w;
	}

	public static double[] fetchWeightVector(int catagory, String module, String sGesture) {
		int size = sizeOfFeatureSet(module);
		double[] weightVector = new double[size];
		String weights = "";
		Scanner scanner = null;
		try {
			Connection connection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"SELECT Weights FROM Weights WHERE Category = ? AND ModuleID = ? AND Gesture = ?");
			statement.setInt(1, catagory);
			statement.setString(2, module);
			statement.setString(3, sGesture);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				weights = resultSet.getString("Weights");
			}
			connection.close();

			scanner = new Scanner(weights);
			for (int i = 0; i < size; i++) {
				weightVector[i] = scanner.nextDouble();
			}
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		} finally {
			if(Objects.nonNull(scanner)) {
				scanner.close();
			}
		}

		return weightVector;
	}

	public static boolean storeWeightVector(double x[], int category, String module, String sGesture) {
		String data = "";
		int index = 0;
		for (int i = 0; i < x.length; i++) {
			data += x[i] + " ";
		}
		Connection connection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
		try {
			String query = "UPDATE Weights SET Weights = ? WHERE Category = ? AND ModuleID = ? AND Gesture = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, data);
			preparedStatement.setInt(2, category);
			preparedStatement.setString(3, module);
			preparedStatement.setString(4, sGesture);
			index = preparedStatement.executeUpdate();
			connection.close();
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return index > 0;
	}

	public static List<Integer> getAllCatagories() {
		Connection connection = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getConnection();
		List<Integer> categories = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT Category FROM Weights");
			while (resultSet.next()) {
				categories.add(resultSet.getInt("Category"));
			}
			connection.close();
		} catch (SQLException ex) {
			BMApi.logError(Calendar.getInstance().getTime().toString(), ex);
		}
		return categories;
	}

	public static int sizeOfFeatureSet(String module) {
		int choice = Integer.parseInt(module.charAt(2) + "");
		int size = 1;
		switch (choice) {
		case 1:
			size = BMApi.TEMPLATE_SIZE * 3;
			break;
		case 2:
			size = BMApi.TEMPLATE_SIZE * 3;
			break;
		case 3:
			size = BMApi.TEMPLATE_SIZE * 5;
			break;
		case 4:
			size = BMApi.TEMPLATE_SIZE * 2;
			break;
		}
		return size;
	}

	public static void log(double d[], String m, int cat) {
		try {
			Writer w = new FileWriter("LVQ.log", true);
			PrintWriter pw = new PrintWriter(w);
			pw.write(Calendar.getInstance().getTime().toString() + ":");
			pw.write(m + "\n");
			pw.write(cat + "\n");
			pw.write(Arrays.toString(d) + "\n");
			pw.write("\n\n");
			pw.close();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Behaviometrics", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method is used in training of the neural network
	 *
	 * @param input           array
	 * @param catagoryOfInput int
	 * @param sModuleName     String
	 * @param sGesture
	 */
	public static void lvq2Training(double[] input, int catagoryOfInput, String sModuleName, String sGesture) {
		List<Integer> categories = getAllCatagories();
		double y_w[] = null;
		double y_r[] = null;
		int winnerCatagoryDetected = -1;
		int runnerCatagoryDetected = -1;

		double winnerMin = 99999999999999999999999d;
		double runnerMin = 99999999999999999999999d;

		for (Integer categorie : categories) {
			double wj[] = fetchWeightVector(categorie, sModuleName, sGesture);
			double dist = euclideanDistance(input, wj);
			if (dist < winnerMin) {
				runnerMin = winnerMin;
				winnerMin = dist;
				y_r = y_w;
				y_w = wj;
				runnerCatagoryDetected = winnerCatagoryDetected;
				winnerCatagoryDetected = categorie;
			}
		}

		if (winnerCatagoryDetected != runnerCatagoryDetected && runnerCatagoryDetected == catagoryOfInput
				&& Math.ulp(runnerMin) == Math.ulp(winnerMin) && checkUpdateCondition(winnerMin, runnerMin, 0.2)) {

			y_r = updateCodebookVector(input, y_r, true);
			y_w = updateCodebookVector(input, y_w, false);

			boolean bWinnerStored = storeWeightVector(y_w, winnerCatagoryDetected, sModuleName, sGesture);
			boolean bRunnerStored = storeWeightVector(y_r, runnerCatagoryDetected, sModuleName, sGesture);

			log(y_w, bWinnerStored ? "UPDATED" : "FAILED", winnerCatagoryDetected);
			log(y_r, bRunnerStored ? "UPDATED" : "FAILED", runnerCatagoryDetected);
		} else {
			log(y_w, "NO ACTION", winnerCatagoryDetected);
		}
	}

	public static boolean checkUpdateCondition(double dw, double dr, double E) {
		return ((dw > ((1 - E) * dr)) && (dr < ((1 + E) * dw)));
	}

	public static double[] updateCodebookVector(double input[], double current[], boolean bIsNotWinner) {
		int op = bIsNotWinner ? (+1) : (-1);
		for (int i = 0; i < current.length; i++) {
			current[i] = current[i] + (op * LEARNING_RATE * Math.floor(input[i] - current[i]));
		}

		return current;
	}

	/**
	 * This method used to initialize the codebook vectors of per user , per
	 * gesture, per module
	 *
	 * @param sUserID
	 * @param sModule
	 * @param sGesture
	 * @return
	 */
	public static boolean initialiseCodebookVector(String sUserID, String sModule, String sGesture) {
		String msg = "Initializing code book vector for user= %s | module= %s | gesture= %s";
		System.out.println(String.format(msg, sUserID, sModule, sGesture));
		double aFeatureData[][] = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getFeatureData(sUserID,
				sModule, sGesture);
		double d[] = null;
		int length = 0;
		for (double[] aFeatureData1 : aFeatureData) {
			if (d == null) {
				length = aFeatureData1.length;
				d = new double[length];
			}
			for (int i = 0; i < length; i++) {
				d[i] += aFeatureData1[i];
			}
		}

		for (int i = 0; i < length; i++) {
			d[i] /= aFeatureData.length;
		}

		String initialWeightOfCodebookVector = BMApi.arrayToString(d);
		int iUserCataogy = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getCatagoryByMobile(sUserID);
		boolean b = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeUserProfile(iUserCataogy,
				initialWeightOfCodebookVector, sModule, sGesture);

		return b;
	}

	public static void initializeUserProfiles() {
		String aUsers[] = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.userList();
		for (String sUser : aUsers) {
			for (String sGesture : BMCostants.SAMPLE_GESTURES) {
				for (String sModuleName : BMCostants.MODULES) {
					initialiseCodebookVector(sUser, sModuleName, sGesture);
				}
			}
		}
	}

	public static void level1Training() {
		String aUsers[] = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.userList();
		for (String sUser : aUsers) {
			for (String sGesture : BMCostants.SAMPLE_GESTURES) {
				for (String sModuleName : BMCostants.MODULES) {
					double aFeatureData[][] = io.rajkumarsutar.mousegesturedynamicsauth.database.Database
							.getFeatureData(sUser, sModuleName, sGesture);
					double d1[] = BMApi.averageArray(aFeatureData[0], aFeatureData[1], aFeatureData[2], aFeatureData[3],
							aFeatureData[4]);
					double d2[] = BMApi.averageArray(aFeatureData[5], aFeatureData[6], aFeatureData[7], aFeatureData[8],
							aFeatureData[9]);
					double d3[] = BMApi.averageArray(aFeatureData[10], aFeatureData[11], aFeatureData[12],
							aFeatureData[13], aFeatureData[14]);
					double d4[] = BMApi.averageArray(aFeatureData[15], aFeatureData[16], aFeatureData[17],
							aFeatureData[18], aFeatureData[19]);
					double d5[] = BMApi.averageArray(aFeatureData[20], aFeatureData[21], aFeatureData[22],
							aFeatureData[23], aFeatureData[24]);
					double d6[] = BMApi.averageArray(aFeatureData[25], aFeatureData[26], aFeatureData[27],
							aFeatureData[28], aFeatureData[29]);
					int iUserCataogy = io.rajkumarsutar.mousegesturedynamicsauth.database.Database
							.getCatagoryByMobile(sUser);

					lvq2Training(d1, iUserCataogy, sModuleName, sGesture);
					lvq2Training(d2, iUserCataogy, sModuleName, sGesture);
					lvq2Training(d3, iUserCataogy, sModuleName, sGesture);
					lvq2Training(d4, iUserCataogy, sModuleName, sGesture);
					lvq2Training(d5, iUserCataogy, sModuleName, sGesture);
					lvq2Training(d6, iUserCataogy, sModuleName, sGesture);
				}
			}
		}
	}

	public static void level2Training() {
		String aUsers[] = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.userList();
		for (String sUser : aUsers) {
			for (String sGesture : BMCostants.SAMPLE_GESTURES) {
				for (String sModuleName : BMCostants.MODULES) {
					double aFeatureData[][] = io.rajkumarsutar.mousegesturedynamicsauth.database.Database
							.getFeatureData(sUser, sModuleName, sGesture);
					int iUserCataogy = io.rajkumarsutar.mousegesturedynamicsauth.database.Database
							.getCatagoryByMobile(sUser);

					for (double[] aFeatureData1 : aFeatureData) {

						lvq2Training(aFeatureData1, iUserCataogy, sModuleName, sGesture);

					}
				}
			}
		}

	}

	public static void heirarchicalTraining() {
		System.out.println("start data outlier removal");
		BMApi.smoothDataAndExtractFeatures();
		System.out.println("start initialize user profiles");
		initializeUserProfiles();
		System.out.println("Start L1 training");
		level1Training();
		System.out.println("Start L2 Training");

		level2Training();
		System.out.println("Finish training");

		JOptionPane.showMessageDialog(null, "Training Completed", "Behaviometrics", JOptionPane.INFORMATION_MESSAGE);
	}

	public static int validation(String fs[], String sGesture) {
		int i = 0;
		int categoryDetected[] = new int[5];
		for (String s : fs) {
			double d[] = BMApi.stringToArray(s);
			int iCatagoryDetected = lvq2Validation(d, "FS" + (++i), sGesture);
			categoryDetected[i] = iCatagoryDetected;
		}

		return detectCategory(categoryDetected);
	}

	public static int detectCategory(int detectCategory[]) {
		int category = -1;
		HashMap<Integer, Integer> oHashMap = new HashMap<Integer, Integer>();

		for (int i = 1; i < detectCategory.length; i++) {
			if (oHashMap.get(detectCategory[i]) != null) {
				int c = oHashMap.get(detectCategory[i]);
				oHashMap.put(detectCategory[i], c + 1);
			} else {
				oHashMap.put(detectCategory[i], 1);
			}
		}

		int maxCount = -1;

		for (int i : oHashMap.keySet()) {
			int value = oHashMap.get(i);
			if (value > maxCount) {
				maxCount = value;
				category = i;
			}
		}

		return maxCount >= BMCostants.THRESHOLD ? category : -1;
	}

	public static int lvq2Validation(double[] input, String sModuleName, String sGesture) {

		List<Integer> categories = getAllCatagories();
		int winnerCatagoryDetected = -1;

		double winnerMin = 99999999999999999999999d;

		for (Integer categorie : categories) {
			double wj[] = fetchWeightVector(categorie, sModuleName, sGesture);
			double dist = euclideanDistance(input, wj);
			if (dist < winnerMin) {
				winnerMin = dist;
				winnerCatagoryDetected = categorie;
			}
		}

		return winnerCatagoryDetected;
	}
}
