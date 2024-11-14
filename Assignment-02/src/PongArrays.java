public class PongArrays {
    public static final float[] barData = {0.02f, 0.2f, -0.02f, 0.2f, -0.02f, -0.2f, 0.02f, -0.2f};
    public static final float[] ballData = {0.02f, 0.02f, -0.02f, 0.02f, -0.02f, -0.02f, 0.02f, -0.02f};

    // Coordinates for rendering score numbers 0, 1, 2, and 3
    public static final float[] score0Data = {0.06f, 0.1f, 0.04f, 0.1f, 0.04f, -0.1f, 0.06f,
            -0.1f, -0.04f, 0.1f, -0.06f, 0.1f, -0.06f, -0.1f, -0.04f, -0.1f,
            0.05f, 0.1f, 0.05f, 0.08f, -0.05f, 0.08f, -0.05f, 0.1f, 0.05f,
            -0.08f, 0.05f, -0.1f, -0.05f, -0.1f, -0.05f, -0.08f};

    public static final float[] score1Data = {0.01f, 0.1f, -0.01f, 0.1f, -0.01f, -0.1f, 0.01f,
            -0.1f};

    public static final float[] score2Data = {0.06f, 0.1f, 0.04f, 0.1f, 0.04f, 0.0f,
            0.06f, 0.0f, -0.04f, 0.0f, -0.06f, 0.0f,
            -0.06f, -0.1f, -0.04f, -0.1f, 0.05f, 0.1f, 0.05f,
            0.08f, -0.05f, 0.08f, -0.05f, 0.1f, 0.05f, -0.08f, 0.05f,
            -0.1f, -0.05f, -0.1f, -0.05f, -0.08f, 0.05f,
            0.01f, 0.05f, -0.01f, -0.05f, -0.01f, -0.05f,
            0.01f};

    public static final float[] score3Data = {0.06f, 0.1f, 0.04f, 0.1f, 0.04f, -0.1f, 0.06f,
            -0.1f, 0.05f, 0.1f, 0.05f, 0.08f, -0.05f, 0.08f, -0.05f, 0.1f,
            0.05f, -0.08f, 0.05f, -0.1f, -0.05f, -0.1f, -0.05f, -0.08f, 0.05f,
            0.01f, 0.05f, -0.01f, -0.05f, -0.01f, -0.05f, 0.01f};

    // Method to get score data based on score value
    public static float[] getScoreData(int score) {
        switch (score) {
            case 0:
                return score0Data;
            case 1:
                return score1Data;
            case 2:
                return score2Data;
            case 3:
                return score3Data;
            default:
                return score0Data; // Default case, if needed
        }
    }
}
