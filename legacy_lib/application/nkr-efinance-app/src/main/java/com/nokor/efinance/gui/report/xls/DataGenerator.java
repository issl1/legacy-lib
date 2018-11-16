package com.nokor.efinance.gui.report.xls;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Point;

import org.jCharts.chartData.ScatterPlotDataSet;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LabelAxisProperties;
import org.jCharts.properties.util.ChartStroke;
/**
 * 
 * @author sok.vina
 *
 */
public class DataGenerator {
	 private final static Font[] ALL_FONTS=GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	 private static int numColorToCreate = 1;

	  /*****************************************************************************************
	   * Random font generator based on the available Fonts on this machine.
	   *
	   * @param minSize
	   * @param maxSize
	   * @return Font
	   ******************************************************************************************/
	  public static Font getRandomFont( double minSize, double maxSize )
	  {
	    Font font=ALL_FONTS[ (int) DataGenerator.getRandomNumber( ALL_FONTS.length ) ];
	    font=font.deriveFont( (float) DataGenerator.getRandomNumber( minSize, maxSize ) );
	    return font;
	  }


	  /*****************************************************************************************
	   * Random number generator.
	   *
	   * @param maxValue
	   * @return double
	   ******************************************************************************************/
	  public static double getRandomNumber( double maxValue )
	  {
	    return Math.random() * maxValue;
	  }


	  /*****************************************************************************************
	   * Random number generator in specified range.
	   *
	   * @param minValue
	   * @param maxValue
	   * @return double
	   ******************************************************************************************/
	  protected static double getRandomNumber( double minValue, double maxValue )
	  {
	    return ( minValue + ( Math.random() * ( maxValue - minValue ) ) );
	  }


	  /*****************************************************************************************
	   * Random number generator in specified range. Will include zeros a small percentage of
	   *   the time for testing.
	   *
	   * @param minValue
	   * @param maxValue
	   * @return double
	   ******************************************************************************************/
	  protected static double getRandomNumberIncludingZero( double minValue, double maxValue )
	  {
	    if( ( DataGenerator.getRandomNumber( 1 ) > 0.8d ) )
	    {
	      return 0.0d;
	    }
	    else
	    {
	      return getRandomNumber( minValue, maxValue );
	    }
	  }

	  
	  /*****************************************************************************************
	   * Random numbers generator in specified range.
	   *
	   * @param numToGenerate the number of doubles to generate
	   * @param minValue
	   * @param maxValue
	   * @return double[]
	   ******************************************************************************************/
	  public static double[] getRandomNumbers( int numToGenerate, double minValue, double maxValue )
	  {
	    double[] data=new double[ numToGenerate ];
	    for( int i=0; i < numToGenerate; i++ )
	    {
	      data[ i ]=getRandomNumber( minValue, maxValue );
	    }
	    return data;
	  }


	  /*****************************************************************************************
	   * Random numbers generator in specified range.
	   *
	   * @param numberOfDataSets to generate
	   * @param numToGenerate the number of doubles to generate
	   * @param minValue
	   * @param maxValue
	   * @return double[]
	   ******************************************************************************************/
	  public static double[][] getRandomNumbers( int numberOfDataSets, int numToGenerate, double minValue, double maxValue )
	  {
	    double[][] data=new double[ numberOfDataSets ][ numToGenerate ];
	    for( int j=0; j < numberOfDataSets; j++ )
	    {
	      for( int i=0; i < numToGenerate; i++ )
	      {
	        data[ j ][ i ]=getRandomNumberIncludingZero( minValue, maxValue );
	      }
	    }
	    return data;
	  }



	  /*****************************************************************************************
	   * Random numbers generator in specified range.
	   *
	   * @param numToGenerate the number of doubles to generate
	   * @param xMinValue
	   * @param xMaxValue
	   * @param yMinValue
	   * @param yMaxValue
	   * @return Point.Double[]
	   ******************************************************************************************/
	  public static Point.Double[] getRandomPoints( int numToGenerate,
	                                 double xMinValue,
	                                 double xMaxValue,
	                                 double yMinValue,
	                                 double yMaxValue )
	  {
	    Point.Double[] points= new Point.Double[ numToGenerate ];
	    for( int j=0; j < numToGenerate; j++ )
	    {
	      points[ j ]= ScatterPlotDataSet.createPoint2DDouble();
	      points[ j ].setLocation( getRandomNumber( xMinValue, xMaxValue ), getRandomNumber( yMinValue, yMaxValue ) );
	    }
	    return points;
	  }


	  /*****************************************************************************************
	   * Random Paint generator.
	   *
	   * @return Paint
	   ******************************************************************************************/
	  protected static Paint getRandomPaint()
	  {
	    if( getRandomNumber( 1 ) > 0.5 )
	    {
	      return getRandomColor(numColorToCreate);
	    }
	    else
	    {
	      float width=(float) DataGenerator.getRandomNumber( 10, 800 );
	      float height=(float) DataGenerator.getRandomNumber( 10, 600 );
	      float x=(float) DataGenerator.getRandomNumber( 0, 800 );
	      float y=(float) DataGenerator.getRandomNumber( 0, 600 );
	      return new GradientPaint( x, y, getRandomColor(numColorToCreate), width, height, getRandomColor(numColorToCreate) );
	    }
	  }

	  /*****************************************************************************************
	   * Random Paint generator.
	   *
	   * @return Paint
	   ******************************************************************************************/
	  protected static Paint getPaint(Color color)
	  {
	    if( getRandomNumber( 1 ) > 0.5 )
	    {
	      return getColor();
	    }
	    else
	    {
	      float width=(float) DataGenerator.getRandomNumber( 10, 00 );
	      float height=(float) DataGenerator.getRandomNumber( 10, 500 );
	      float x=(float) DataGenerator.getRandomNumber( 0, 800 );
	      float y=(float) DataGenerator.getRandomNumber( 0, 600 );
	      return new GradientPaint( x, y, color, width, height, color);
	    }
	  }
	  
	  public static Paint[] getPaints( int numToCreate )
	  {
		numColorToCreate = numToCreate;
	    Paint paints[]=new Paint[ numToCreate ];
	    Color color = Color.blue;
	    for( int i=0; i < numToCreate; i++ )
	    {
			if (i == 1) {
				color = Color.green;
			} else if (i == 2) {
				color = Color.orange;
			} else if (i == 3) {
				color = Color.pink;
			} else if (i == 4) {
				color = Color.lightGray;
			} else if (i == 5) {
				color = Color.yellow;
			} else if (i == 6) {
				color = Color.cyan;
			} else if (i == 7) {
				color = Color.RED;
			} else if (i == 8) {
				color = Color.gray;
			}
	      paints[ i ]= color;
	    }
	    return paints;
	  }
	  
	  /*****************************************************************************************
	   * Random Color generator.
	   *
	   * @return Color
	   ******************************************************************************************/
	  protected static Color getRandomColor(int numToCreate)
	  {
		    Color color = Color.blue;
		    for( int i=0; i < numToCreate; i++ )
		    {
				if (i == 1) {
					color = Color.green;
				} else if (i == 2) {
					color = Color.orange;
				} else if (i == 3) {
					color = Color.pink;
				} else if (i == 4) {
					color = Color.lightGray;
				} else if (i == 5) {
					color = Color.yellow;
				} else if (i == 6) {
					color = Color.cyan;
				} else if (i == 7) {
					color = Color.RED;
				} else if (i == 8) {
					color = Color.gray;
				}
		    }
	    return color;
	  }
	  /*****************************************************************************************
	   *  Color generator.
	   *
	   * @return Color
	   ******************************************************************************************/
	  protected static Color getColor()
	  {
	    int transparency=(int) getRandomNumber( 100, 375 );
	    if( transparency > 255 )
	    {
	      transparency=255;
	    }

	    return new Color( (int) getRandomNumber( 255 ), (int) getRandomNumber( 255 ), (int) getRandomNumber( 255 ), transparency );
	  }

	  /*****************************************************************************************
	   * Random String generator.
	   *
	   * @param maxStringLength
	   * @param canBeNull
	   * @return String
	   ******************************************************************************************/
	  protected static String getRandomString( int maxStringLength, boolean canBeNull )
	  {
	    if( canBeNull )
	    {
	      if( DataGenerator.getRandomNumber( 10 ) <= 1 )
	      {
	        return null;
	      }
	    }


	    int tempVal;

	    int stringLength=1 + (int) getRandomNumber( maxStringLength );
	    StringBuffer stringBuffer=new StringBuffer( stringLength );

	    while( stringLength-- > 0 )
	    {
	      tempVal=65 + (int) getRandomNumber( 58 );
	      while( tempVal > 90 && tempVal < 97 )
	      {
	        tempVal=65 + (int) getRandomNumber( 58 );
	      }

	      stringBuffer.append( (char) tempVal );
	    }

	    return stringBuffer.toString();
	  }


	  /*****************************************************************************************
	   * Random String generator.
	   *
	   * @return String[]
	   ******************************************************************************************/
	  protected static String[] getRandomStrings( int numToCreate, int maxStringLength, boolean canBeNull )
	  {
	    if( canBeNull )
	    {
	      if( (int) DataGenerator.getRandomNumber( 10 ) <= 1 )
	      {
	        return null;
	      }
	    }

	    String strings[]=new String[ numToCreate ];

	    for( int i=0; i < numToCreate; i++ )
	    {
	      strings[ i ]=getRandomString( maxStringLength, false );
	    }

	    return strings;
	  }


	  /******************************************************************************************
	   * Takes the passed AxisProperties and randomizes it.
	   *
	   * @param axisProperties
	   ******************************************************************************************/
	  protected static void randomizeAxisProperties( AxisProperties axisProperties )
	  {
	    DataAxisProperties dataAxisProperties;
	    LabelAxisProperties labelAxisProperties;
	    if( axisProperties.isPlotHorizontal() )
	    {
	      dataAxisProperties= (DataAxisProperties) axisProperties.getXAxisProperties();
	      labelAxisProperties= (LabelAxisProperties) axisProperties.getYAxisProperties();
	    }
	    else
	    {
	      dataAxisProperties= (DataAxisProperties) axisProperties.getYAxisProperties();
	      labelAxisProperties= (LabelAxisProperties) axisProperties.getXAxisProperties();
	    }

	    dataAxisProperties.setNumItems( (int) DataGenerator.getRandomNumber( 2, 15 ) );
	    dataAxisProperties.setRoundToNearest( (int) DataGenerator.getRandomNumber( -5, 3 ) );

	    dataAxisProperties.setUseDollarSigns( DataGenerator.getRandomNumber( 1 ) > 0.5d );
	    dataAxisProperties.setUseCommas( DataGenerator.getRandomNumber( 1 ) > 0.5d );

	    //axisProperties.setShowAxisTitle( AxisProperties.X_AXIS, TestDataGenerator.getRandomNumber( 1 ) > 0.5d );
	    //axisProperties.setShowAxisTitle( AxisProperties.Y_AXIS, TestDataGenerator.getRandomNumber( 1 ) > 0.5d );

	    //axisProperties.setShowGridLine( AxisProperties.X_AXIS, (int) TestDataGenerator.getRandomNumber( 3 ) );
	    //axisProperties.setShowGridLine( AxisProperties.X_AXIS, AxisProperties.GRID_LINES_ONLY_WITH_LABELS );
	    //axisProperties.setShowGridLine( AxisProperties.Y_AXIS, (int) TestDataGenerator.getRandomNumber( 3 ) );

	    dataAxisProperties.setShowEndBorder( DataGenerator.getRandomNumber( 1 ) > 0.5d );
	    labelAxisProperties.setShowEndBorder( DataGenerator.getRandomNumber( 1 ) > 0.5d );

//	    axisProperties.setShowTicks( AxisProperties.X_AXIS, (int) TestDataGenerator.getRandomNumber( 3 ) );
	    //axisProperties.setShowTicks( AxisProperties.X_AXIS, AxisProperties.TICKS_ONLY_WITH_LABELS );
	    //axisProperties.setShowTicks( AxisProperties.Y_AXIS, (int) TestDataGenerator.getRandomNumber( 3 ) );

	    //axisProperties.setShowZeroLine( TestDataGenerator.getRandomNumber( 1 ) > 0.5d );
	    //axisProperties.setZeroLinePaint( TestDataGenerator.getRandomPaint() );


//	    axisProperties.setScaleFont( TestDataGenerator.getRandomFont( 12.0, 15.0 ) );
	    //axisProperties.setScaleFontColor( TestDataGenerator.getRandomPaint() );

	    //axisProperties.setAxisTitleFont( TestDataGenerator.getRandomFont( 6.0, 20.0 ) );

	    axisProperties.getXAxisProperties().setAxisStroke( new ChartStroke( new BasicStroke( 1.5f ), DataGenerator.getRandomPaint() ) );
	    axisProperties.getYAxisProperties().setAxisStroke( new ChartStroke( new BasicStroke( 1.5f ), DataGenerator.getRandomPaint() ) );


	    //axisProperties.setBackgroundPaint( TestDataGenerator.getRandomPaint() );
	  }
}
