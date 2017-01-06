package org.tiwindetea.magicmetro;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Point2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Segment2dfx;
import org.tiwindetea.magicmetro.model.StationType;
import org.tiwindetea.magicmetro.model.TrainType;
import org.tiwindetea.magicmetro.view.ConcreteStationView;
import org.tiwindetea.magicmetro.view.ConcreteTrainView;
import org.tiwindetea.magicmetro.view.StationView;
import org.tiwindetea.magicmetro.view.TrainView;
import org.tiwindetea.magicmetro.view.ViewManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		test1(primaryStage);
		//test2(primaryStage);
		//test3(primaryStage);
		//test4(primaryStage);

	}

	private void test1(Stage primaryStage) {
		Pane pane = new Pane();
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.show();

		ViewManager viewManager = new ViewManager();
		TrainView trainView = viewManager.createTrainView(TrainType.FAST);

		pane.getChildren().add((ConcreteTrainView) trainView);
		trainView.setPosition(new Point2d(250, 250));
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
					trainView.addPassenger(StationType.TRIANGLE);
					Thread.sleep(100);
					trainView.addPassenger(StationType.SQUARE);
					Thread.sleep(100);
					trainView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					trainView.addPassenger(StationType.CIRCLE);
					Thread.sleep(100);
					trainView.addPassenger(StationType.SQUARE);
					Thread.sleep(100);
					trainView.removePassenger(StationType.TRIANGLE);
					Thread.sleep(100);
					trainView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					trainView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					trainView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					trainView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					trainView.addPassenger(StationType.DIAMOND);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				/*long i = 0;
				while(true) {
					try {
						i += 10;
						trainView.setRotation(i);
						Thread.sleep(70);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}*/
			}
		});


		StationView stationView = viewManager.createStationView(StationType.DIAMOND);
		pane.getChildren().add((ConcreteStationView) stationView);
		stationView.setPosition(new Point2d(400, 400));
		ExecutorService executorService2 = Executors.newSingleThreadExecutor();
		executorService2.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
					stationView.addPassenger(StationType.TRIANGLE);
					Thread.sleep(100);
					stationView.addPassenger(StationType.SQUARE);
					Thread.sleep(100);
					stationView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					stationView.addPassenger(StationType.CIRCLE);
					Thread.sleep(100);
					stationView.addPassenger(StationType.SQUARE);
					Thread.sleep(100);
					stationView.removePassenger(StationType.TRIANGLE);
					Thread.sleep(100);
					stationView.addPassenger(StationType.DIAMOND);
					Thread.sleep(100);
					stationView.addPassenger(StationType.DIAMOND);

					Thread.sleep(100);
					stationView.warn();

					Thread.sleep(1000);
					stationView.removePassenger(StationType.DIAMOND);
					Thread.sleep(1000);
					stationView.addPassenger(StationType.DIAMOND);
					Thread.sleep(1000);
					stationView.removePassenger(StationType.SQUARE);
					Thread.sleep(1000);
					stationView.addPassenger(StationType.TRIANGLE);
					Thread.sleep(1000);
					stationView.addPassenger(StationType.SQUARE);
					Thread.sleep(1000);
					stationView.addPassenger(StationType.DIAMOND);
					Thread.sleep(1000);
					stationView.addPassenger(StationType.CIRCLE);

					Thread.sleep(1000);
					stationView.unWard();

					Thread.sleep(4000);
					stationView.warn();

					Thread.sleep(5000);
					stationView.unWard();
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				/*long i = 0;
				while(true) {
					try {
						i += 10;
						trainView.setRotation(i);
						Thread.sleep(70);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}*/
			}
		});
	}

	/*private void test3(Stage primaryStage) {
		//MultiShape2d<Rectangle2d> multiShape2d = new MultiShape2d<>();
		//MultiShape2dfx<Rectangle2dfx> multiShape2dfx = new MultiShape2dfx<>();
		Rectangle2d rectangle2d = new Rectangle2d();

		Rectangle2dfx rectangle2dfx = new Rectangle2dfx();
		Rectangle rectangle = new Rectangle();
		rectangle.setFill(Color.PINK);
		rectangle.widthProperty().bind(rectangle2dfx.widthProperty());
		rectangle.heightProperty().bind(rectangle2dfx.heightProperty());
		rectangle.translateXProperty().bind(rectangle2dfx.minXProperty());
		rectangle.translateYProperty().bind(rectangle2dfx.minYProperty());
		rectangle2dfx.setFromCorners(100, 100, 400, 200);

		Rectangle2dfx rectangle2dfx2 = new Rectangle2dfx();
		Rectangle rectangle2 = new Rectangle();
		rectangle2.setFill(Color.PINK);
		rectangle2.widthProperty().bind(rectangle2dfx2.widthProperty());
		rectangle2.heightProperty().bind(rectangle2dfx2.heightProperty());
		rectangle2.translateXProperty().bind(rectangle2dfx2.minXProperty());
		rectangle2.translateYProperty().bind(rectangle2dfx2.minYProperty());
		rectangle2dfx2.setFromCorners(100, 200, 200, 500);

		MultiShape2dfx<Rectangle2dfx> multiShape2dfx = new MultiShape2dfx<>();
		multiShape2dfx.add(rectangle2dfx);
		multiShape2dfx.add(rectangle2dfx2);


		Polyline polyline = new Polyline();
		polyline.getPoints().add(20d);
		polyline.getPoints().add(700d);
		polyline.getPoints().add(200d);
		polyline.getPoints().add(200d);
		polyline.getPoints().add(20d);
		polyline.getPoints().add(400d);
		polyline.setStroke(Color.BLUE);

		Segment2dfx segment2dfx = new Segment2dfx(20d, 700d, 200d, 200d);
		segment2dfx.x1Property()
		  .addListener((observable, oldValue, newValue) -> polyline.getPoints().set(0, newValue.doubleValue()));
		segment2dfx.y1Property()
		  .addListener((observable, oldValue, newValue) -> polyline.getPoints().set(1, newValue.doubleValue()));
		segment2dfx.x2Property()
		  .addListener((observable, oldValue, newValue) -> polyline.getPoints().set(2, newValue.doubleValue()));
		segment2dfx.y2Property()
		  .addListener((observable, oldValue, newValue) -> polyline.getPoints().set(3, newValue.doubleValue()));

		Pane pane = new Pane();
		pane.setMinSize(800, 600);
		Random random = new Random();
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				segment2dfx.x2Property().setValue(event.getSceneX());
				segment2dfx.y2Property().setValue(event.getSceneY());
				if(segment2dfx.intersects(multiShape2dfx)) {
					polyline.setStroke(Color.RED);
				}
				else {
					polyline.setStroke(Color.BLUE);
				}
			}
		});
		Scene scene = new Scene(pane);
		pane.getChildren().add(polyline);
		pane.getChildren().add(rectangle);
		pane.getChildren().add(rectangle2);
		primaryStage.setScene(scene);
		primaryStage.show();
	}*/

	private void test4(Stage primaryStage) {
		Rectangle2d rectangle2d = new Rectangle2d();

		Rectangle2dfx rectangle2dfx = new Rectangle2dfx();
		Rectangle rectangle = new Rectangle();
		rectangle.setFill(Color.PINK);
		rectangle.widthProperty().bind(rectangle2dfx.widthProperty());
		rectangle.heightProperty().bind(rectangle2dfx.heightProperty());
		rectangle.translateXProperty().bind(rectangle2dfx.minXProperty());
		rectangle.translateYProperty().bind(rectangle2dfx.minYProperty());
		rectangle2dfx.setFromCorners(100, 100, 400, 200);

		Rectangle2dfx rectangle2dfx2 = new Rectangle2dfx();
		Rectangle rectangle2 = new Rectangle();
		rectangle2.setFill(Color.PINK);
		rectangle2.widthProperty().bind(rectangle2dfx2.widthProperty());
		rectangle2.heightProperty().bind(rectangle2dfx2.heightProperty());
		rectangle2.translateXProperty().bind(rectangle2dfx2.minXProperty());
		rectangle2.translateYProperty().bind(rectangle2dfx2.minYProperty());
		rectangle2dfx2.setFromCorners(100, 200, 200, 500);

		MultiShape2dfx<Rectangle2dfx> multiShape2dfx = new MultiShape2dfx<>();
		multiShape2dfx.add(rectangle2dfx);
		multiShape2dfx.add(rectangle2dfx2);


		Polyline polyline = new Polyline();
		polyline.getPoints().add(20d);
		polyline.getPoints().add(700d);
		polyline.getPoints().add(200d);
		polyline.getPoints().add(200d);
		polyline.getPoints().add(20d);
		polyline.getPoints().add(400d);
		polyline.setStroke(Color.BLUE);

		Point2dfx a = new Point2dfx();
		a.xProperty()
		  .addListener(((observable, oldValue, newValue) -> polyline.getPoints().set(0, newValue.doubleValue())));
		a.yProperty()
		  .addListener(((observable, oldValue, newValue) -> polyline.getPoints().set(1, newValue.doubleValue())));
		Point2dfx b = new Point2dfx();
		b.xProperty()
		  .addListener(((observable, oldValue, newValue) -> polyline.getPoints().set(2, newValue.doubleValue())));
		b.yProperty()
		  .addListener(((observable, oldValue, newValue) -> polyline.getPoints().set(3, newValue.doubleValue())));
		Point2dfx c = new Point2dfx();
		c.xProperty()
		  .addListener(((observable, oldValue, newValue) -> polyline.getPoints().set(4, newValue.doubleValue())));
		c.yProperty()
		  .addListener(((observable, oldValue, newValue) -> polyline.getPoints().set(5, newValue.doubleValue())));

		Segment2dfx ab = new Segment2dfx(a, b);
		Segment2dfx bc = new Segment2dfx(b, c);

		Pane pane = new Pane();
		pane.setMinSize(800, 600);
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				a.set(event.getX(), event.getY());
				b.set(event.getX(), event.getY());
				c.set(event.getX(), event.getY());
			}
		});
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				c.xProperty().setValue(event.getSceneX());
				c.yProperty().setValue(event.getSceneY());
				if(Math.abs(c.getX() - a.getX()) > Math.abs(c.getY() - a.getY())) {
					b.setX(a.getX() + (c.getX() - a.getX()) - (c.getX() > a.getX() ? 1 : -1) * Math.abs(c.getY() - a.getY()));
					b.setY(a.getY());
				}
				else {
					b.setX(a.getX());
					b.setY(a.getY() + (c.getY() - a.getY()) - (c.getY() > a.getY() ? 1 : -1) * Math.abs(c.getX() - a.getX()));
				}
				ab.set(a, b);
				bc.set(b, c);
				if(ab.intersects(multiShape2dfx) || bc.intersects(multiShape2dfx)) {
					polyline.setStroke(Color.RED);
				}
				else {
					polyline.setStroke(Color.BLUE);
				}
			}
		});
		Scene scene = new Scene(pane);
		pane.getChildren().add(polyline);
		pane.getChildren().add(rectangle);
		pane.getChildren().add(rectangle2);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
