package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

/**
 * @author Prashant Sutar
 */
public class BMCostants {
	public static final int REPLICATIONS = 30;
        public static final int TOTAL_SESSION = 1;
        public static final int TRAINING_SESSION = 1;
	public static final String SAMPLE_GESTURES[] = { "G", "Y", "M", "Z", "5" };
	public static final String X = "X-Coordinates";
	public static final String Y = "Y-Coordinates";
	public static final String T = "Time";
	public static final String HV = "HorizontalVelocity";
	public static final String VV = "VerticalVelocity";
	public static final String TV = "TangentialVelocity";
	public static final String THTi = "SlopeAngleOfTangent";
	public static final String L = "DistanceFromOrigin";
	public static final String C = "Curvature";
	public static final String TA = "TangentialAccelaration";
	public static final String TJ = "TangentialJerk";
	public static final String dC = "CurvatureRateChange";
        public static final int THRESHOLD = 2;

        public static final String MODULES[] = {"FS1", "FS2", "FS3", "FS4"};

	String FS1[] = { X, Y, T };
	String FS2[] = { L, C, dC };
	String FS3[] = { HV, VV, TV, TA, TJ };
	String FS4[] = { L, THTi };

	public static final double R[][] = { { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
			{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
			{ 1.196, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 1.383, 1.078, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
			{ 1.509, 1.200, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 1.610, 1.299, 1.099, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
			{ 1.693, 1.382, 1.187, 1.022, 0.0, 0.0, 0.0, 0.0, 0.0 },
			{ 1.763, 1.453, 1.261, 1.109, 0.0, 0.0, 0.0, 0.0, 0.0 },
			{ 1.824, 1.515, 1.324, 1.178, 1.045, 0.0, 0.0, 0.0, 0.0 },
			{ 1.878, 1.570, 1.380, 1.237, 1.114, 0.0, 0.0, 0.0, 0.0 },
			{ 1.925, 1.619, 1.430, 1.289, 1.172, 1.059, 0.0, 0.0, 0.0 },
			{ 1.969, 1.663, 1.475, 1.336, 1.221, 1.118, 1.009, 0.0, 0.0 },
			{ 2.007, 1.704, 1.516, 1.379, 1.266, 1.167, 1.070, 0.0, 0.0 },
			{ 2.043, 1.741, 1.554, 1.417, 1.307, 1.210, 1.120, 1.026, 0.0 },
			{ 2.076, 1.775, 1.589, 1.453, 1.344, 1.249, 1.164, 1.078, 0.0 },
			{ 2.106, 1.807, 1.622, 1.486, 1.378, 1.285, 1.202, 1.122, 1.039 },
			{ 2.134, 1.836, 1.652, 1.517, 1.409, 1.318, 1.237, 1.161, 1.084 },
			{ 2.161, 1.864, 1.680, 1.546, 1.438, 1.348, 1.268, 1.195, 1.123 },
			{ 2.185, 1.890, 1.707, 1.573, 1.466, 1.377, 1.298, 1.226, 1.158 },
			{ 2.209, 1.914, 1.732, 1.599, 1.492, 1.404, 1.326, 1.255, 1.190 },
			{ 2.230, 1.938, 1.756, 1.623, 1.517, 1.429, 1.352, 1.282, 1.218 },
			{ 2.251, 1.960, 1.779, 1.646, 1.540, 1.452, 1.376, 1.308, 1.245 },
			{ 2.271, 1.981, 1.800, 1.668, 1.563, 1.475, 1.399, 1.332, 1.270 },
			{ 2.290, 2.000, 1.821, 1.689, 1.584, 1.497, 1.421, 1.354, 1.293 },
			{ 2.307, 2.019, 1.840, 1.709, 1.604, 1.517, 1.442, 1.375, 1.315 },
			{ 2.324, 2.037, 1.859, 1.728, 1.624, 1.537, 1.462, 1.396, 1.336 },
			{ 2.341, 2.055, 1.877, 1.746, 1.642, 1.556, 1.481, 1.415, 1.356 },
			{ 2.356, 2.071, 1.894, 1.764, 1.660, 1.574, 1.500, 1.434, 1.375 },
			{ 2.371, 2.088, 1.911, 1.781, 1.677, 1.591, 1.517, 1.452, 1.393 },
			{ 2.385, 2.103, 1.927, 1.797, 1.694, 1.608, 1.534, 1.469, 1.411 },
			{ 2.399, 2.118, 1.942, 1.812, 1.710, 1.624, 1.550, 1.486, 1.428 },
			{ 2.412, 2.132, 1.957, 1.828, 1.725, 1.640, 1.567, 1.502, 1.444 },
			{ 2.425, 2.146, 1.971, 1.842, 1.740, 1.655, 1.582, 1.517, 1.459 },
			{ 2.438, 2.159, 1.985, 1.856, 1.754, 1.669, 1.597, 1.532, 1.475 },
			{ 2.450, 2.172, 1.998, 1.870, 1.768, 1.683, 1.611, 1.547, 1.489 },
			{ 2.461, 2.184, 2.011, 1.883, 1.782, 1.697, 1.624, 1.561, 1.504 },
			{ 2.472, 2.196, 2.024, 1.896, 1.795, 1.711, 1.638, 1.574, 1.517 },
			{ 2.483, 2.208, 2.036, 1.909, 1.807, 1.723, 1.651, 1.587, 1.531 },
			{ 2.494, 2.219, 2.047, 1.921, 1.820, 1.736, 1.664, 1.600, 1.544 },
			{ 2.504, 2.230, 2.059, 1.932, 1.832, 1.748, 1.676, 1.613, 1.556 },
			{ 2.514, 2.241, 2.070, 1.944, 1.843, 1.760, 1.688, 1.625, 1.568 },
			{ 2.524, 2.251, 2.081, 1.955, 1.855, 1.771, 1.699, 1.636, 1.580 },
			{ 2.533, 2.261, 2.092, 1.966, 1.866, 1.783, 1.711, 1.648, 1.592 },
			{ 2.542, 2.271, 2.102, 1.976, 1.876, 1.794, 1.722, 1.659, 1.603 },
			{ 2.551, 2.281, 2.112, 1.987, 1.887, 1.804, 1.733, 1.670, 1.614 },
			{ 2.560, 2.290, 2.122, 1.997, 1.897, 1.815, 1.743, 1.681, 1.625 },
			{ 2.568, 2.299, 2.131, 2.006, 1.907, 1.825, 1.754, 1.691, 1.636 },
			{ 2.577, 2.308, 2.140, 2.016, 1.917, 1.835, 1.764, 1.701, 1.646 },
			{ 2.585, 2.317, 2.149, 2.026, 1.927, 1.844, 1.773, 1.711, 1.656 },
			{ 2.592, 2.326, 2.158, 2.035, 1.936, 1.854, 1.783, 1.721, 1.666 },
			{ 2.600, 2.334, 2.167, 2.044, 1.945, 1.863, 1.792, 1.730, 1.675 },
			{ 2.608, 2.342, 2.175, 2.052, 1.954, 1.872, 1.802, 1.740, 1.685 },
			{ 2.615, 2.350, 2.184, 2.061, 1.963, 1.881, 1.811, 1.749, 1.694 },
			{ 2.622, 2.358, 2.192, 2.069, 1.972, 1.890, 1.820, 1.758, 1.703 },
			{ 2.629, 2.365, 2.200, 2.077, 1.980, 1.898, 1.828, 1.767, 1.711 },
			{ 2.636, 2.373, 2.207, 2.085, 1.988, 1.907, 1.837, 1.775, 1.720 },
			{ 2.643, 2.380, 2.215, 2.093, 1.996, 1.915, 1.845, 1.784, 1.729 },
			{ 2.650, 2.387, 2.223, 2.101, 2.004, 1.923, 1.853, 1.792, 1.737 },
			{ 2.656, 2.394, 2.230, 2.109, 2.012, 1.931, 1.861, 1.800, 1.745 },
			{ 2.663, 2.401, 2.237, 2.116, 2.019, 1.939, 1.869, 1.808, 1.753 },
			{ 2.670, 2.408, 2.244, 2.123, 2.025, 1.946, 1.876, 1.815, 1.760 },
			{ 2.677, 2.415, 2.251, 2.130, 2.032, 1.953, 1.883, 1.822, 1.767 },
			{ 2.683, 2.422, 2.258, 2.137, 2.039, 1.960, 1.890, 1.829, 1.774 },
			{ 2.690, 2.430, 2.265, 2.143, 2.046, 1.967, 1.897, 1.836, 1.781 } };

	public static final int CLICK_POINTS[][] = { 
			{ 602, 125 }, { 506, 75 },  { 201, 149 }, { 187, 172 }, { 172, 211 }, { 157, 253 },
			{ 152, 290 }, { 152, 332 }, { 155, 370 }, { 592, 455 }, { 617, 349 }, { 616, 302 }};

}