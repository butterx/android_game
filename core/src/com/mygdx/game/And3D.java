package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
//впоследствии будет главным классом, но видоизмененным
public class And3D extends ApplicationAdapter {

	public static final String TITLE="Car Demo";
	public SpriteBatch spritebatch;
	public Texture backgroundTexture;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Array<ModelInstance>instances=new Array<ModelInstance>();
	public boolean loading;
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;

	public Model model;
	public ModelInstance ground;
	public ModelInstance grass;
	public ModelInstance sand;
	//private Texture backgroundTexture;
	//private SpriteBatch spritebatch;
	private final Matrix4 viewMatrix = new Matrix4();

	protected Stage stage;
	protected Label label;
	protected BitmapFont font;
	protected StringBuilder stringBuilder;

	private static final String SKY_TEXTURE = "sky/Sky.jpg";
	//private static final String CAR_MODEL="models/car.obj";
	//private static final String CAR_TEXTURE = "materials/template_body.jpg";

	@Override
	public void create () {
		stage = new Stage();
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();
		spritebatch=new SpriteBatch();
		modelBatch = new ModelBatch();
		//создаем пример среды и задаем значения DirectionalLight с цветом (0.8, 0.8, 0.8) и направлением of (-1.0, -0.8f, -0.2f).
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		//инициализируем ModelBuilder который используется для создания моделей в коде
		ModelBuilder mb = new ModelBuilder();
		//создание и установка значений для перспективной камеры
		cam= new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.position.set(0f, 40f, -75f);
		cam.lookAt(0f, 5f, 5f);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();



		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		try{
			//установка фона экрана - sky
			backgroundTexture = new Texture(Gdx.files.internal(SKY_TEXTURE), Pixmap.Format.RGB565, true);
			backgroundTexture.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Linear);
			//создание или генерирование тирайна в виде 3 боксов (земля трава песок)
			mb.begin();
			mb.node().id = "ground";
			mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)))
					.box(100f, 0.2f, 100f);
			mb.node().id = "grass";
			mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
					.box(100f, 0.2f, 100f);
			mb.node().id = "sand";
			mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
					.box(100f, 0.2f, 100f);
			model = mb.end();

			ground = new ModelInstance(model, "ground");
			grass = new ModelInstance(model, "grass");
			sand = new ModelInstance(model, "sand");
			//соответствующие модели мы создали, теперь изменяем их координаты
			ground.transform.setToTranslation(6f, 0f, 0);
			grass.transform.setToTranslation(6f, 0f, 20f);
			sand.transform.setToTranslation(6f, 0f, 40f);
			//model-загрузка модели
			assets = new AssetManager();
			assets.load("models/car.g3db", Model.class);
			loading = true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

	private void doneLoading() {
		//model
		Model car = assets.get("models/car.g3db", Model.class);
		ModelInstance carInstance = new ModelInstance(car);
		instances.add(carInstance);


		//terrain(s)
		instances.add(ground);
		instances.add(grass);
		instances.add(sand);

		loading = false;
	}

	@Override

	public void render () {
		//если все составляющие созданы, то
		if (loading && assets.update()) doneLoading();
		camController.update();
		//очистка экрана
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
		/*viewMatrix.setToOrtho2D(0, 0, 480, 320);
		spritebatch.setProjectionMatrix(viewMatrix);
		spritebatch.begin();
		spritebatch.disableBlending();
		spritebatch.setColor(Color.WHITE);
		spritebatch.draw(backgroundTexture, 0, 0, 480, 320, 0, 0, 512, 512, false, false);
		spritebatch.end();*/
		//показываем проигрываем наш фон
		renderBackground();

		//вызываем modelbatch, отвечающий за проигрывание(показывание)рендер и мы инициализировали его  create
		modelBatch.begin(cam);
		//показываем (render) наши примеры ModellInstance и среды
		modelBatch.render(instances, environment);
		modelBatch.end();
		//показываем созданную текстовую метку(поле)
		renderText();
	}
	@Override
	public void dispose(){
		font.dispose();
		backgroundTexture.dispose();
		spritebatch.dispose();
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
		model.dispose();

	}

	private void renderBackground(){
		viewMatrix.setToOrtho2D(0, 0, 480, 320);
		spritebatch.setProjectionMatrix(viewMatrix);
		spritebatch.begin();
		spritebatch.disableBlending();
		spritebatch.setColor(Color.WHITE);
		spritebatch.draw(backgroundTexture, 0, 0, 480, 320, 0, 0, 512, 512, false, false);
		spritebatch.end();
	}

	public void renderText() {
		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
		label.setText(stringBuilder);
		stage.draw();
	}


}
