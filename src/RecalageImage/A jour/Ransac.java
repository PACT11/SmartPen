import java.awt.Point;
import java.util.ArrayList;

public class Ransac {
	
	  //=== Input
	  // The list of points to analyze (data)
	  private ArrayList<Point> m_points;
	  // A model that can be fitted to the data
	  private Line m_currentModel = new Line(0,1);
	  // The number of iterations to be performed by the algorithm
	  private int m_iterationNb = 0;
	  // The threshold value for determining when a datum fits a model
	  private float m_threshold;
	  // The number of close data values required to assert that a model fits well to data
	  private int m_minNbOfCloseData;

	  //=== Output
	  // The model fitting the best to the data
	  private Line m_bestModel = new Line(0,0);
	  // The data points from which the best model have been estimated
	  private ArrayList<Point> m_bestInliers = new ArrayList<Point>();
	  // The score (error) of this model relative to the data
	  private float m_bestScore = Integer.MAX_VALUE;

	  private ArrayList<Point> m_currentSample;
	  private ArrayList<Point> m_currentInliers = new ArrayList<Point>();
	  private int m_currentIterationNb;
	  private float m_currentScore;

	  private FittingInterface m_fitting;
    
    public Ransac(FittingInterface fitting, ArrayList<Point> points, float threshold)
    {
      m_fitting = fitting;
      m_points = points;
      m_threshold = threshold;
      m_minNbOfCloseData = m_fitting.getCloseDataNb();
      m_iterationNb =  getIterationNb(0.99, 0.5, m_fitting.getNeededPointNb());
    }

    public Line getCurrentModel() { return m_currentModel; }
    public ArrayList<Point> getSample() { return m_currentSample; }
    public float getCurrentScore() { return m_currentScore; }
    public int getCurrentIterationNb() { return m_currentIterationNb; }
    public int getIterationNb() { return m_iterationNb; }
    public ArrayList<Point> getInliers() { return m_bestInliers; }
    public Line getBestModel() { return m_bestModel; }
    public float getBestScore() { return m_bestScore; }
    
    public void removePoint(Point point) {
    	m_points.remove(point);
    }
    
    public void resetIterationNb() {
    	m_currentIterationNb = 0;
    }
    

    public boolean isFinished()
    {
      return m_currentIterationNb > m_iterationNb;
    }

    // "When the number of iterations computed is limited the solution obtained may not be optimal, and it may not even be one that fits the data in a good way."
    private int getIterationNb(double ransacProbability, double outlierRatio, int sampleSize)
    {
      return (int) Math.ceil(Math.log(1 - ransacProbability) /
          Math.log(1 - Math.pow(1 - outlierRatio, sampleSize)));
    }
    
    /** Takes pointNb distinct random points from the data. */
    private ArrayList<Point> takeRandomSample(int pointNb)
    {
      int totalPointNb = m_points.size();
      // Base assertion. Actually, totalPointNb should be much greater than pointNb,
      // otherwise the algorithm can take some time rejecting the already taken points.
      // Should that be the case, it would be more efficient to shuffle a copy of the data... But unlikely, anyway.
     
      // assert totalPointNb > pointNb : "Not enough points to work! Need " + pointNb + " got " + totalPointNb;
      ArrayList<Point> points = new ArrayList<Point>();
      for (int i = 0; i < pointNb; )
      {
        int t = (int) (Math.random()*(totalPointNb-1));
        Point pt = m_points.get(t);
        if (!points.contains(pt)) // Not already chosen
        {
          points.add(pt);
          i++;
        }
      }
      return points;
    }
    
    /**
     * Computes the next step of the algorithm
     *
     * @return the score for the current (last tried) model
     */
    public float computeNextStep()
    {
      if (isFinished())
      {
        m_currentSample.clear();
        m_currentInliers.clear();
        m_currentModel = null;
        return 0;
      }

      checkSample(takeRandomSample(m_fitting.getNeededPointNb()));
      m_currentIterationNb++;

      return m_currentScore;
    }

    public void checkSample(ArrayList<Point> points)
    {
      m_currentSample = points;
      m_currentModel = m_fitting.estimateModel(m_currentSample);
      m_currentInliers.clear();
      m_currentScore = 0;
      // For each point in the data
      for (Point point : m_points)
      {
        // ignore points which are in the sample
        if (m_currentSample.contains(point))
          continue;

        // Compute the error with the potential model
        double error = m_fitting.estimateError(point, m_currentModel);
        if (error > m_threshold) // Big error
        {
          m_currentScore += m_threshold; // Cap the error at the threshold
        }
        else // Error below threshold
        {
          m_currentScore += error; // Score it
          // This close point becomes an inlier
          m_currentInliers.add(point);
        }
      }
      if (m_currentInliers.size() > m_minNbOfCloseData && m_currentScore < m_bestScore)
      {
        m_bestModel = m_currentModel;
        m_bestInliers = m_currentInliers;
        m_bestScore = m_currentScore;
      }
    }

    public String toString()
    {
      return "Ransac(thresold=" + m_threshold +
          ", iteration=" + m_currentIterationNb + "/" + m_iterationNb +
          ", best=" + m_bestModel +
          ")";
    }
}