package at.brigot.l33t;

import at.brigot.l33t.bl.GameCommandExecutor;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;
import javafx.scene.control.Tab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import static java.lang.System.out;

public class GameClient extends ApplicationAdapter {
	private Stage stage;
	private Skin skin;

	private float gameWidth;
	private float gameHeight;

	private Table loginTable, chatRoomTable, fileSystemTable, commandLineTable;

	private Console console;

	private Socket socket;

	private String username;

	PrintWriter pw;
	BufferedReader br;

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		console = new GUIConsole();
		console.setSizePercent(100, 33);
		console.setPositionPercent(0, 67);
		console.setDisplayKeyID(Input.Keys.Z);

		console.setCommandExecutor(new GameCommandExecutor(console));

		gameWidth = stage.getWidth();
		gameHeight = stage.getHeight();

		loginTable = buildLoginTable();
		chatRoomTable = buildChatRoomTable();
		fileSystemTable = buildFileSystem();
		commandLineTable = buildCommandLine();

		stage.addActor(loginTable);
		stage.addActor(chatRoomTable);
		stage.addActor(fileSystemTable);
		stage.addActor(commandLineTable);
	}

	// login table actors

	private TextButton join_button;
	private TextButton testArea;
	private TextField name_field;
	private TextField password_field;

	private Table buildLoginTable(){
		final Table table = new Table();
		table.setFillParent(true);

		Window window = new Window("Login", skin);
		window.getTitleLabel().setAlignment(Align.center);

		join_button = new TextButton("Join", skin);
		testArea = new TextButton("Test Area", skin);
		name_field = new TextField("", skin);
		password_field = new TextField("", skin);

		window.add(new Label("Enter Your Name", skin));
		window.row();
		window.add(name_field);
		window.row();
		window.add(new Label("Enter Your Password",skin));
		window.row();
		window.add(password_field);
		window.row();
		window.add(join_button);
		window.add(testArea);
		table.add(window);

		testArea.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				loginTable.setVisible(false);
				commandLineTable.setVisible(true);
			}
		});

		join_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				username = name_field.getText();

				if(!username.isEmpty()){
					loginTable.setVisible(false);
					chatRoomTable.setVisible(true);

					try {
						socket = new Socket("10.151.71.83",9999);
						br = new BufferedReader( new InputStreamReader( socket.getInputStream()) ) ;
						pw = new PrintWriter(socket.getOutputStream(),true);
						pw.println(username);  // send name to server
						//out.println("TEST");
						new MessagesThread().start();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}


				}
			}
		});



		return table;
	}

	//Command Line table actors
	private TextButton back_button;

	private ScrollPane command_scroll;
	private Label command_label;
	private TextArea command_area;

	private Table buildCommandLine(){
		Table table = new Table();
		table.setFillParent(true);

		command_label = new Label("", skin);
		command_label.setWrap(true);
		command_label.setAlignment(Align.topLeft);

		command_scroll = new ScrollPane(command_label, skin);
		command_scroll.setFadeScrollBars(false);

		command_area = new TextArea(">",skin);



		//table.add(command_scroll).width(Gdx.graphics.getWidth()-100f).height(400f).colspan(1).center();
		//table.row();

		back_button = new TextButton("Back", skin);
		back_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				commandLineTable.setVisible(false);
				loginTable.setVisible(true);
			}
		});

		//table.add(back_button).colspan(2);

		table.row().colspan(2).expand();
		table.add(command_area).fill();
		table.row().colspan(2).expandX().fillX();
		table.add(back_button);

		//table.add(commandInput_scroll).width(300f).colspan(2);

		table.setVisible(false);

		return table;
	}

	// File System table actors

	private List filelist;
	private List.ListStyle fileStyle;
	private ScrollPane scrollPane;

	private Table buildFileSystem(){
		Table table = new Table();
		table.setFillParent(true);

		filelist = new List(skin);
		fileStyle = new List.ListStyle();

		String[] files = new String[5];
		files[0] = "test1";
		files[1] = "test2";
		files[2] = "test3";
		files[3] = "test4";
		files[4] = "test5";
		filelist.setItems(files);

		scrollPane = new ScrollPane(filelist);
		scrollPane.setFillParent(true);
		scrollPane.setSmoothScrolling(false);
		scrollPane.setTransform(true);
		scrollPane.setScale(1f);

		table.addActor(scrollPane);

		table.setVisible(false);

		return table;
	}

	// chat room table actors

	private TextButton send_button;
	private TextArea message_field;
	private ScrollPane input_scroll;

	private List<String> users_list;
	private ScrollPane chat_scroll;
	private ScrollPane users_scroll;
	private Label chat_label;


	private Table buildChatRoomTable(){
		Table table = new Table();
		table.setFillParent(true);

		chat_label = new Label("", skin);
		chat_label.setWrap(true);
		chat_label.setAlignment(Align.topLeft);

		chat_scroll = new ScrollPane(chat_label, skin);
		chat_scroll.setFadeScrollBars(false);

		table.add(chat_scroll).width(300f).height(400f).colspan(2);

		users_list = new List<String>(skin, "dimmed");

		users_scroll = new ScrollPane(users_list, skin);
		users_scroll.setFadeScrollBars(false);

		table.add(users_scroll).width(150f).height(400f).colspan(2);

		message_field = new TextArea("", skin);
		message_field.setPrefRows(2);

		input_scroll = new ScrollPane(message_field, skin);
		input_scroll.setFadeScrollBars(false);

		table.row();
		table.add(input_scroll).width(300f).colspan(2);

		send_button = new TextButton("Send", skin);
		table.add(send_button).colspan(2);

		table.setVisible(false);

		send_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				String text = message_field.getText();

				if(!text.isEmpty()){
					//Text senden
					pw.println(message_field.getText());
					message_field.setText("");
				}

			}
		});

		return table;
	}

	class  MessagesThread extends Thread {
		public void run() {
			String line;
			try {
				while(true) {
					line = br.readLine();
					String[] lineParts = line.split(">");
					if(lineParts[0].equals("json")){

					}
					else{
						chat_label.setText(chat_label.getText() + line + "\n");
					}

				} // end of while
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		console.draw();
	}

	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
		console.dispose();
	}
}
