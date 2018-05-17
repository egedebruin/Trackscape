package gui;

import camera.Camera;
import handlers.CameraHandler;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.opencv.core.Mat;

/**
 * Controller class for controlling GUI elements.
 */
class Controller {

    /**
     * Class parameters.
     */
    private CameraHandler cameraHandler;
    private boolean cameraActive;

    /**
     * Constructor method.
     */
    Controller() {
        cameraHandler = new CameraHandler();
    }

    /**
     * retrieveFrame.
     * Retrieve last frame from video reader in handlers.CameraHandler
     * @param cam the camera that is used
     * @return Image
     */
    private Image retrieveFrame(final Camera cam) {
        Image frame;
        Mat matrixFrame = cameraHandler.getNewFrame(cam);
        if (!cam.isChanged()) {
            File streamEnd = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\black.png");
            frame = new Image(streamEnd.toURI().toString());
            cameraHandler.clearList();
            cameraActive = false;
        } else {
            BufferedImage bufferedFrame = matToBufferedImage(matrixFrame);
            frame = SwingFXUtils.toFXImage(bufferedFrame, null);
        }
        return frame;
    }

    /**
     * Converts a Mat to a BufferedImage.
     * @param videoMatImage The frame in Mat.
     * @return The BufferedImage.
     */
    private BufferedImage matToBufferedImage(final Mat videoMatImage) {
        int type;
        if (videoMatImage.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = videoMatImage.channels()
            * videoMatImage.cols() * videoMatImage.rows();
        byte[] buffer = new byte[bufferSize];
        videoMatImage.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(
            videoMatImage.cols(), videoMatImage.rows(), type);
        final byte[] targetPixels =
            ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    /**
     * Method to show a popup in which
     * you can specify a stream url to initialize a connection.
     * @param streamStage The popup window
     * @param field the specified url.
     */
    void createStream(final Stage streamStage, final TextField field) {
        String streamUrl = field.getText();
        streamStage.close();
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with our active camera(stream).
     * @param streamUrl THE url
     */
    void createTheStream(final String streamUrl) {
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with a video.
     * @param file the video file
     */
    void createVideo(final File file) {
        String fileUrl = file.toString();
        cameraHandler.addCamera(fileUrl);
    }

    /**
     * grabTimeFrame.
     * Call updateImageView method every period of time to retrieve a new frame
     * @param imageView shows the image
     * @param plotView shows the plot
     */
    void grabTimeFrame(final ImageView imageView, final ImageView plotView) {
        if (!cameraActive) {
            ScheduledExecutorService timer;
            final int period = 1;
            Runnable frameGrabber = () -> updateViews(imageView, plotView);
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(
                frameGrabber, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * updateViews.
     * @param imageView the view for the video
     * @param plotView the view for the activity plot
     */
    void updateViews(final ImageView imageView, final ImageView plotView) {
        updateImageView(imageView);
        updatePlotView(plotView);
    }

    /**
     * updateImageView.
     * Retrieve current frame and show in ImageView
     * @param imageView shows the image
     */
    private void updateImageView(final ImageView imageView) {
        final int width = 600;
        for (int i = 0; i < cameraHandler.listSize(); i++) {
            cameraActive = true;
            Image currentFrame = retrieveFrame(cameraHandler.getCamera(i));
            imageView.setImage(currentFrame);
            imageView.setFitWidth(width);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
        }
    }

    /**
     * updatePlotView.
     * Updates the plot.
     * @param plotView the ImageView for the plot
     */
    private void updatePlotView(final ImageView plotView) {
        final int width = 300;
        final int height = 300;
        final JFreeChart chart = updateGraph();

        // Convert chart to image to show in Gui
        BufferedImage bufferedImageChart
            = chart.createBufferedImage(width, height);
        Image plot = SwingFXUtils.toFXImage(bufferedImageChart, null);

        plotView.setImage(plot);
        plotView.setFitWidth(width);
        plotView.setPreserveRatio(true);
        plotView.setSmooth(true);
        plotView.setCache(true);
    }

    /**
     * Draws graph from data about movement difference.
     * @return ChartPanel the plotted graph
     */
    public JFreeChart updateGraph() {
        final XYSeries series =  new XYSeries("Random Data");
        series.add(1.0, 500.2);
        series.add(5.0, 694.1);
        series.add(4.0, 100.0);
        series.add(12.5, 734.4);
        series.add(17.3, 453.2);
        series.add(21.2, 500.2);
        series.add(21.9, null);
        series.add(25.6, 734.4);
        series.add(30.0, 453.2);
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart activityChart = ChartFactory.createXYLineChart(
            "",
            "Time",
            "Movement Change",
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        return activityChart;
    }

    /**
     * Method that closes a stream.
     * @param imageView View where the stream is displayed in
     */
    void closeStream(final ImageView imageView) {
        final int width = 500;
        if (cameraActive) {
            cameraHandler.clearList();
            cameraActive = false;
            File image = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\nostream.png");
            Image noStreamAvailable = new Image(image.toURI().toString());
            imageView.setImage(noStreamAvailable);
            imageView.setFitWidth(width);
            imageView.setPreserveRatio(true);
        }
    }

}